package net.mindlee.loontooth.adapter;

import java.util.ArrayList;
import net.mindlee.loontooth.gui.MainActivity;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class PhotoAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<PhotoInfo> photoList = new ArrayList<PhotoInfo>();
	private String[] imageColumns = new String[] {
			MediaStore.Images.Media.TITLE, MediaStore.Images.Media.MIME_TYPE,
			MediaStore.Images.Media.SIZE,
			MediaStore.Images.Media.DATE_MODIFIED,
			MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };

	public static class PhotoInfo {
		public String title;
		public String mimeType;
		public String filePath;
		public String size;
		public String dateModified;
		public String thumbPath;

		void print() {
			Log.v("title", title + "");
			Log.v("mimeType", mimeType + "");
			Log.v("filePath", filePath + "");
			Log.v("size", size + "");
			Log.v("dateModified", dateModified + "");
			Log.v("thumbPath", thumbPath + "");
		}
	}

	public PhotoAdapter(Context context) {
		this.context = context;

		Cursor cursor = ((Activity) context).managedQuery(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageColumns,
				null, null, null);

		if (!cursor.moveToFirst()) {
			return;
		}
		
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			PhotoInfo info = new PhotoInfo();
			info.title = cursor.getString(cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE));
			info.mimeType = cursor.getString(cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE));
			info.filePath = cursor.getString(cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
			info.size = cursor.getString(cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE));
			info.dateModified = cursor
					.getString(cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED));
			long size = Long.valueOf(cursor.getString(cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)));
			Log.w("大小", "" + size);
			//if (size < 100000) {
				//continue;
			//}
			info.print();
			photoList.add(info);
		}
		cursor.close();
	}

	public ArrayList<PhotoInfo> getPhotoList() {
		return photoList;
	}
	
	public int getCount() {
		return photoList.size();
	}

	public Object getItem(int p) {
		return photoList.get(p);
	}

	public long getItemId(int p) {
		return p;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;

		if (convertView == null) {
			imageView = new ImageView(context);
			int width = MainActivity.SCREEN_WIDTH / 3;
			int height = width * 3 / 4;
			imageView.setLayoutParams(new GridView.LayoutParams(width, height));
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setPadding(8, 8, 8, 8);
		} else {
			imageView = (ImageView) convertView;
		}

		String filePath = photoList.get(position).filePath;

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bmp = BitmapFactory.decodeFile(filePath, options);
		int width = MainActivity.SCREEN_WIDTH / 3;
		int height = width * 3 / 4;
		options.inJustDecodeBounds = false;
		options.inPreferredConfig = Bitmap.Config.ARGB_4444;
		options.outHeight = height;
		options.outWidth = width;

		bmp = BitmapFactory.decodeFile(filePath, options);
		imageView.setImageBitmap(bmp);

		return imageView;
	}
}