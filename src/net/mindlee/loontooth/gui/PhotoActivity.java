package net.mindlee.loontooth.gui;

import java.io.IOException;
import java.util.ArrayList;
import net.mindlee.loontooth.R;
import net.mindlee.loontooth.adapter.PhotoAdapter;
import net.mindlee.loontooth.adapter.PhotoAdapter.PhotoInfo;
import net.mindlee.loontooth.bluetooth.BluetoothTools;
import net.mindlee.loontooth.bluetooth.TransmitBean;
import net.mindlee.loontooth.util.MyFiles;
import net.mindlee.loontooth.util.MyPhoto;
import net.mindlee.loontooth.util.MyPopWindow;
import net.mindlee.loontooth.util.MyTools;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;

/**
 * PhotoActivity， 照片主界面
 * 
 * @author 李伟
 * 
 */
public class PhotoActivity extends BaseActivity {
	private GridView photoGridView;
	private int focusPhotoListItem;
	private PopupWindow downMenuPopWindow;
	private MyPhoto myPhoto;
	private MyPopWindow myPopWindow;
	private PhotoAdapter photoAdapter;
	private ArrayList<PhotoInfo> photoList = new ArrayList<PhotoInfo>();
	private MyFiles myFiles = new MyFiles(this);
	
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo);
		
		photoGridView = (GridView) findViewById(R.id.photoGridView);
		photoAdapter = new PhotoAdapter(this, photoList);
		photoGridView.setAdapter(photoAdapter);
		myPhoto = new MyPhoto(this, photoList);
		new LoadImagesFromSDCard().execute();
		photoGridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				downMenuPopWindow.showAsDropDown(view, view.getWidth() / 2,
						-view.getHeight() / 2);
				focusPhotoListItem = position;
			}
		});

	myPopWindow = new MyPopWindow(this);
		downMenuPopWindow = myPopWindow.createDownMenu();
		myPopWindow.getDownMenuListView().setOnItemClickListener(
				new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						downMenuPopWindow.dismiss();
						if (position == 0) {
							sendPhotoFiles(focusPhotoListItem);
						} else if (position == 1) {
							myPhoto.playPhoto(focusPhotoListItem);
						} else if (position == 2) {
							myPhoto.openDetailsDialog(focusPhotoListItem).show();
						}
					}
				});
				
	}

	/**
	 * 发送photoList中的第position张照片
	 * 
	 * @param position
	 */
	private void sendPhotoFiles(int position) {
		TransmitBean data = new TransmitBean();
		String title = photoList.get(position).title;

		String size = photoList.get(position).size;
		size = MyTools.sizeFormat(size);
		data.setMsg(title);
		data.setSize(size);

		String filePath = photoList.get(position).filePath;
		String fileType = photoList.get(position).mimeType;
		myFiles.sendFile(filePath, fileType);

		Intent sendDataIntent = new Intent(
				BluetoothTools.ACTION_DATA_TO_SERVICE);
		sendDataIntent.putExtra(BluetoothTools.DATA, data);
		sendBroadcast(sendDataIntent);
		downMenuPopWindow.dismiss();
	}

	protected void onDestroy() {
		super.onDestroy();
		final GridView grid = photoGridView;
		final int count = grid.getChildCount();
		ImageView v = null;
		for (int i = 0; i < count; i++) {
			v = (ImageView) grid.getChildAt(i);
			((BitmapDrawable) v.getDrawable()).setCallback(null);
		}
	}

	/**
	 * 异步任务(Asnc Task)加载SD卡中的照片文件
	 * 
	 * @author 李伟
	 * 
	 */
	class LoadImagesFromSDCard extends AsyncTask<Object, Integer, Object> {
		String[] imageColumns = new String[] { MediaStore.Images.Media.TITLE,
				MediaStore.Images.Media.MIME_TYPE,
				MediaStore.Images.Media.SIZE,
				MediaStore.Images.Media.DATE_MODIFIED,
				MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };

		protected Object doInBackground(Object... params) {
			Bitmap bitmap = null;
			Bitmap newBitmap = null;
			Uri uri = null;
			String[] projection = { MediaStore.Images.Thumbnails._ID,
					MediaStore.Images.Thumbnails.IMAGE_ID };

			Cursor thumbCursor = getContentResolver().query(
					MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
					projection, // Which columns to return
					null, // Return all rows
					null, null);

			int columnIndex = thumbCursor
					.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID);
			int originalImageId = thumbCursor
					.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.IMAGE_ID);
			int length = thumbCursor.getCount();

			for (thumbCursor.moveToFirst(); !thumbCursor.isAfterLast(); thumbCursor
					.moveToNext()) {

				String selection = MediaStore.Images.Media._ID + "=?";
				String[] selectionArgs = new String[] { thumbCursor
						.getInt(originalImageId) + "" };
				Cursor photoCursor = getContentResolver().query(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
						imageColumns, selection, selectionArgs, null);
				PhotoInfo info = new PhotoInfo();
				if (photoCursor.moveToFirst()) {
					info.title = photoCursor
							.getString(photoCursor
									.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE));
					info.mimeType = photoCursor
							.getString(photoCursor
									.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE));
					info.filePath = photoCursor
							.getString(photoCursor
									.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
					info.size = photoCursor
							.getString(photoCursor
									.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE));
					info.dateModified = photoCursor
							.getString(photoCursor
									.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED));
				}
				photoCursor.close();
				int imageID = thumbCursor.getInt(columnIndex);
				uri = Uri.withAppendedPath(
						MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, ""
								+ imageID);
				try {
					bitmap = BitmapFactory.decodeStream(getContentResolver()
							.openInputStream(uri));
					if (bitmap != null) {
						assert LoonToothApplication.getScreenWidth() > 0 : LoonToothApplication.getScreenWidth();
						int width = LoonToothApplication.getScreenWidth() * 3 / 10;
						int height = width * 3 / 4;
						newBitmap = Bitmap.createScaledBitmap(bitmap, width,
								height, true);
						info.bitmap = newBitmap;
						photoList.add(info);

						// info.print();
						bitmap.recycle();
					}
					publishProgress((int) (photoList.size() * 1.0 / length * 100.0));
					Log.w("照片" + photoList.size(), "总共" + length);
				} catch (IOException e) {
					// Error fetching image, try to recover
				}

			}
			thumbCursor.close();

			return null;
		}

		protected void onProgressUpdate(Integer... values) {
			Log.w("photoAdapter", "" + photoAdapter.getCount());
			photoAdapter.notifyDataSetChanged();
		}
	}

}