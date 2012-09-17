package net.mindlee.loontooth.gui;

import java.util.ArrayList;

import net.mindlee.loontooth.R;
import net.mindlee.loontooth.adapter.VideoAdapter;
import net.mindlee.loontooth.adapter.VideoAdapter.VideoInfo;
import net.mindlee.loontooth.bluetooth.BluetoothTools;
import net.mindlee.loontooth.bluetooth.TransmitBean;
import net.mindlee.loontooth.util.MyFiles;
import net.mindlee.loontooth.util.MyPopWindow;
import net.mindlee.loontooth.util.MyVideo;
import net.mindlee.loontooth.util.MyTools;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Video.Thumbnails;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

/**
 * VideoActivity， 视频主界面
 * 
 * @author 李伟
 * 
 */
public class VideoActivity extends BaseActivity {
	private ListView videoListView;
	private int focusVideoListItem;
	private PopupWindow downMenuPopWindow;
	private MyVideo myVideo;
	private MyPopWindow myPopWindow;
	private VideoAdapter videoAdapter;
	private MyFiles myFiles = new MyFiles(this);

	private ArrayList<VideoInfo> videoList = new ArrayList<VideoInfo>();

	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);
		videoListView = (ListView) findViewById(R.id.video_listView);
		videoAdapter = new VideoAdapter(this, videoList);
		myVideo = new MyVideo(this, videoList);
		videoListView.setAdapter(videoAdapter);
		new LoadVideoFromSDCard().execute();

		videoListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						downMenuPopWindow.showAsDropDown(view,
								view.getWidth() / 2, -view.getHeight() / 2);
						focusVideoListItem = position;
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
							sendVideoFiles(focusVideoListItem);
						} else if (position == 1) {
							myVideo.playVideo(focusVideoListItem);
						} else if (position == 2) {
							myVideo.openDetailsDialog(focusVideoListItem).show();
						}
					}
				});
	}

	/**
	 * 发送videoList中的第position个视频
	 * 
	 * @param position
	 */
	private void sendVideoFiles(int position) {
		TransmitBean data = new TransmitBean();

		String title = videoList.get(position).title;

		String size = videoList.get(position).size;
		size = MyTools.sizeFormat(size);
		data.setMsg(title);
		data.setSize(size);

		String filePath = videoList.get(position).filePath;
		String fileType = videoList.get(position).mimeType;
		myFiles.sendFile(filePath, fileType);

		Intent sendDataIntent = new Intent(
				BluetoothTools.ACTION_DATA_TO_SERVICE);
		sendDataIntent.putExtra(BluetoothTools.DATA, data);
		sendBroadcast(sendDataIntent);
		downMenuPopWindow.dismiss();
	}

	/**
	 * 异步加载SD卡中的视频文件
	 * 
	 * @author 李伟
	 * 
	 */
	class LoadVideoFromSDCard extends AsyncTask<Object, Integer, Object> {
		private String[] mediaColumns = new String[] {
				MediaStore.Video.Media.DATA, MediaStore.Video.Media._ID,
				MediaStore.Video.Media.TITLE, MediaStore.Video.Media.DURATION,
				MediaStore.Video.Media.SIZE, MediaStore.Video.Media.MIME_TYPE,
				MediaStore.Video.Media.DATE_MODIFIED };
		ContentResolver contentResolver = getContentResolver(); // 获取ContentResolver的引用
		Cursor cursor = contentResolver.query(
				MediaStore.Video.Media.EXTERNAL_CONTENT_URI, mediaColumns,
				null, null, null);
		int length = cursor.getCount();

		protected Object doInBackground(Object... params) {

			if (cursor.moveToFirst()) {
				do {
					VideoInfo info = new VideoInfo();
					info.filePath = cursor
							.getString(cursor
									.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
					info.mimeType = cursor
							.getString(cursor
									.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));
					info.title = cursor
							.getString(cursor
									.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
					info.duration = cursor
							.getString(cursor
									.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
					info.size = cursor
							.getString(cursor
									.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
					info.dateModified = cursor
							.getString(cursor
									.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED));
					Log.w("" + info.filePath, "" + info.size);

					// 邪恶的硬编码，问题仍未解决，bluetooth文件夹的内容不能彻底删除，留下一个没截图的诡异文件
					if (info.filePath.indexOf("luetooth") == -1) {
						Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(
								info.filePath, Thumbnails.MICRO_KIND);
						int width = LoonToothApplication.getScreenWidth() / 4;
						int height = width * 3 / 4;
						Bitmap bitmap1 = ThumbnailUtils.extractThumbnail(
								bitmap, width, height);
						info.bitmap = bitmap1;
						bitmap.recycle();
					} else {
						length--;
					}
					videoList.add(info);
					publishProgress((int) (videoList.size() * 1.0 / length * 100.0));
					Log.w("视频" + videoList.size(), "总共" + length);

				} while (cursor.moveToNext());
				cursor.close();
			}
			return null;
		}

		protected void onProgressUpdate(Integer... values) {
			videoAdapter.notifyDataSetChanged();
			Log.w("values[0]", "" + values[0]);
		}

	}
}
