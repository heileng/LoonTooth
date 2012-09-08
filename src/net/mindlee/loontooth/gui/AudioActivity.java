package net.mindlee.loontooth.gui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.mindlee.loontooth.R;
import net.mindlee.loontooth.adapter.AudioAdapter;
import net.mindlee.loontooth.adapter.AudioAdapter.AudioInfo;
import net.mindlee.loontooth.bluetooth.BluetoothTools;
import net.mindlee.loontooth.bluetooth.TransmitBean;
import net.mindlee.loontooth.util.Audio;
import net.mindlee.loontooth.util.CustomFiles;
import net.mindlee.loontooth.util.PopWindow;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

/**
 * 音频主界面
 * @author 李伟
 *
 */
public class AudioActivity extends Activity {
	private ListView audioListView;
	private static int focusAudioListViewItem;
	private PopupWindow downMenuPopWindow;
	private AudioAdapter audioAdapter;
	private Audio audio;
	private PopWindow popWindow;
	private CustomFiles customFiles = new CustomFiles(this);
	private long mLastBackTime = 0;
	private long TIME_DIFF = 2 * 1000;
	private List<AudioInfo> audioList = new ArrayList<AudioInfo>();

	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audio);

		audioListView = (ListView) findViewById(R.id.audio_listView);
		audioAdapter = new AudioAdapter(this, audioList);
		audio = new Audio(this, audioList);
		audioListView.setAdapter(audioAdapter);
		new LoadAudioFromSDCard().execute();

		audioListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						downMenuPopWindow.showAsDropDown(view,
								view.getWidth() / 2, -view.getHeight() / 2);
						focusAudioListViewItem = position;
					}
				});

		popWindow = new PopWindow(this);
		downMenuPopWindow = popWindow.createDownMenu();
		popWindow.getDownMenuListView().setOnItemClickListener(
				new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						downMenuPopWindow.dismiss();
						if (position == 0) {
							sendAudioFiles(focusAudioListViewItem);
						} else if (position == 1) {
							audio.playMusic(focusAudioListViewItem);
						} else if (position == 2) {
							audio.openDetailsDialog(focusAudioListViewItem)
									.show();
						}
					}
				});
	}

	/**
	 * 发送audioList中第position个音乐文件
	 * @param position audioList位置
	 */
	private void sendAudioFiles(int position) {
		TransmitBean data = new TransmitBean();
		String title = audioList.get(position).title;
		data.setMsg(title);

		String filePath = audioList.get(position).filePath;
		String fileType = audioList.get(position).mimeType;
		customFiles.sendFile(filePath, fileType);

		Intent sendDataIntent = new Intent(
				BluetoothTools.ACTION_DATA_TO_SERVICE);
		sendDataIntent.putExtra(BluetoothTools.DATA, data);
		sendBroadcast(sendDataIntent);
		downMenuPopWindow.dismiss();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			long now = new Date().getTime();
			if (now - mLastBackTime < TIME_DIFF) {
				return super.onKeyDown(keyCode, event);
			} else {
				mLastBackTime = now;
				Toast.makeText(this, "再点击一次退出程序", Toast.LENGTH_SHORT).show();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 利用异步任务加载SD卡中的全部音乐文件
	 * @author 李伟
	 *
	 */
	class LoadAudioFromSDCard extends AsyncTask<Object, Integer, Object> {
		private String[] audioColumns = new String[] {
				MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.TITLE,
				MediaStore.Audio.Media.ARTIST,
				MediaStore.Audio.Media.MIME_TYPE, MediaStore.Audio.Media.SIZE,
				MediaStore.Audio.Media.DATE_MODIFIED,
				MediaStore.Audio.Media.IS_MUSIC,
				MediaStore.Audio.Albums.ALBUM_ID, };

		protected Object doInBackground(Object... params) {

			ContentResolver contentResolver = getContentResolver(); // 获取ContentResolver的引用
			Cursor cursor = contentResolver.query(
					MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, audioColumns,
					null, null, null);
			int length = cursor.getCount();
			if (cursor.moveToFirst()) {
				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
						.moveToNext()) {
					AudioInfo info = new AudioInfo();

					info.filePath = cursor
							.getString(cursor
									.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
					info.title = cursor
							.getString(cursor
									.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
					info.artist = cursor
							.getString(cursor
									.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
					info.mimeType = cursor
							.getString(cursor
									.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE));
					info.size = cursor
							.getString(cursor
									.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
					info.dateModified = cursor
							.getString(cursor
									.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_MODIFIED));
					int isMusic = cursor
							.getInt(cursor
									.getColumnIndexOrThrow(MediaStore.Audio.Media.IS_MUSIC));

					if (isMusic == 1) {
						info.isMusic = true;
					} else {
						info.isMusic = false;
						length--;
					}
					Bitmap bitmap = null;
					MediaMetadataRetriever retriever = new MediaMetadataRetriever();
					try {

						retriever.setDataSource(info.filePath);
						byte[] art = retriever.getEmbeddedPicture();
						bitmap = BitmapFactory.decodeByteArray(art, 0,
								art.length);
						int width = MainActivity.SCREEN_WIDTH / 5;
						Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap,
								width, width, true);
						bitmap.recycle();
						info.bitmap = newBitmap;

					} catch (IllegalArgumentException ex) {
						Log.w("IllegalArgumentException", "");
					} catch (RuntimeException ex) {
						Log.w("RuntimeException", "");
					} finally {
						try {
							retriever.release();
						} catch (RuntimeException ex) {
							// Ignore failures while cleaning up.
						}
					}
					if (info.isMusic) {
						audioList.add(info);
					}
					Log.w("已经" + audioList.size(), "总共" + length);
					publishProgress((int) (audioList.size() * 1.0 / length * 100.0));
					// info.print();
				}
			}
			cursor.close();
			return null;
		}

		protected void onProgressUpdate(Integer... values) {
			audioAdapter.notifyDataSetChanged();
		}
	}
}