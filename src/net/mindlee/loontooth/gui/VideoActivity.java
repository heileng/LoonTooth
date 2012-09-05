package net.mindlee.loontooth.gui;

import java.util.ArrayList;
import java.util.Date;

import net.mindlee.loontooth.R;
import net.mindlee.loontooth.adapter.VideoAdapter;
import net.mindlee.loontooth.adapter.VideoAdapter.VideoInfo;
import net.mindlee.loontooth.bluetooth.BluetoothTools;
import net.mindlee.loontooth.bluetooth.TransmitBean;
import net.mindlee.loontooth.util.PopWindow;
import net.mindlee.loontooth.util.Video;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Video.Thumbnails;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

public class VideoActivity extends Activity {
	private ListView videoListView;
	private int focusVideoListItem;
	private PopupWindow downMenuPopWindow;
	private Video video;
	private PopWindow popWindow;
	private long mLastBackTime = 0;
	private long TIME_DIFF = 2 * 1000;
	private VideoAdapter videoAdapter;

	private ArrayList<VideoInfo> videoList = new ArrayList<VideoInfo>();

	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);
		videoListView = (ListView) findViewById(R.id.video_listView);
		videoAdapter = new VideoAdapter(this, videoList);
		video = new Video(this, videoAdapter);
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

		popWindow = new PopWindow(this);
		downMenuPopWindow = popWindow.createDownMenu();

		popWindow.getDownMenuListView().setOnItemClickListener(
				new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Log.v("点击下拉菜单", "");
						if (position == 0) {
							Log.d("点击位置0", "传输");
							// 发送消息
							TransmitBean data = new TransmitBean();
							String name = BluetoothTools.getBTAdapter()
									.getName();
							Log.w("name", "");
							data.setMsg("你好");
							Intent sendDataIntent = new Intent(
									BluetoothTools.ACTION_DATA_TO_SERVICE);
							sendDataIntent.putExtra(BluetoothTools.DATA, data);
							sendBroadcast(sendDataIntent);

						} else if (position == 1) {
							downMenuPopWindow.dismiss();
							video.playVideo(focusVideoListItem);
						} else if (position == 2) {
							downMenuPopWindow.dismiss();
							video.openDetailsDialog(focusVideoListItem).show();
							Log.d("点击位置3", "属性");
						}
					}
				});
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			long now = new Date().getTime();
			if (now - mLastBackTime < TIME_DIFF) {
				return super.onKeyDown(keyCode, event);
			} else {
				mLastBackTime = now;
				Toast.makeText(this, "再点击一次退出程序", 2000).show();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	class LoadVideoFromSDCard extends AsyncTask<Object, Integer, Object> {
		private String[] mediaColumns = new String[] {
				MediaStore.Video.Media.DATA, MediaStore.Video.Media._ID,
				MediaStore.Video.Media.TITLE, MediaStore.Video.Media.DURATION,
				MediaStore.Video.Media.SIZE, MediaStore.Video.Media.MIME_TYPE,
				MediaStore.Video.Media.DATE_MODIFIED };
		Cursor cursor = cursor = managedQuery(
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
					Log.w("视频格式", info.mimeType);
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

					Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(
							info.filePath, Thumbnails.MICRO_KIND);
					int width = MainActivity.SCREEN_WIDTH / 4;
					int height = width * 3 / 4;
					Bitmap bitmap1 = ThumbnailUtils.extractThumbnail(bitmap,
							width, height);
					info.bitmap = bitmap1;
					bitmap.recycle();
					videoList.add(info);
					publishProgress((int) (videoList.size() * 1.0 / length * 100.0));
					Log.w("视频" + videoList.size(), "总共" + length);
				} while (cursor.moveToNext());
			}
			cursor.close();
			return null;
		}

		protected void onProgressUpdate(Integer... values) {
			videoAdapter.notifyDataSetChanged();
			Log.w("values[0]", "" + values[0]);
			if (values[0] == 100) {
				Toast.makeText(getApplicationContext(), "共" + videoAdapter.getCount() + "个视频.", Toast.LENGTH_SHORT);
			}
		}

	}
}
