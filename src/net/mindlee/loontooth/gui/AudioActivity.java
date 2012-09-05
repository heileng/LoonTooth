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
import net.mindlee.loontooth.util.PopWindow;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class AudioActivity extends Activity {
	private ListView audioListView;
	private int focusAudioListViewItem;
	private PopupWindow downMenuPopWindow;
	private AudioAdapter audioAdapter;
	private Audio audio;
	private PopWindow popWindow;
	private long mLastBackTime = 0;
	private long TIME_DIFF = 2 * 1000;
	private List<AudioInfo> audioList = new ArrayList<AudioInfo>();

	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audio);

		audioListView = (ListView) findViewById(R.id.audio_listView);
		audioAdapter = new AudioAdapter(this, audioList);
		audio = new Audio(this, audioAdapter);
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
							downMenuPopWindow.dismiss();
						} else if (position == 1) {
							downMenuPopWindow.dismiss();
							audio.playMusic(focusAudioListViewItem);
						} else if (position == 2) {
							downMenuPopWindow.dismiss();
							audio.openDetailsDialog(focusAudioListViewItem)
									.show();
						}
					}
				});
	}

	protected void onStart() {
		Log.w("onStart", "");
		super.onStart();
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

	class LoadAudioFromSDCard extends AsyncTask<Object, Integer, Object> {
		private String[] audioColumns = new String[] {
				MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.TITLE,
				MediaStore.Audio.Media.ARTIST,
				MediaStore.Audio.Media.MIME_TYPE, MediaStore.Audio.Media.SIZE,
				MediaStore.Audio.Media.DATE_MODIFIED,
				MediaStore.Audio.Media.IS_MUSIC,
				MediaStore.Audio.Albums.ALBUM_ID, };

		/*
		 * ProgressDialog pdialog;
		 * 
		 * public LoadAudioFromSDCard(Context context) { pdialog = new
		 * ProgressDialog(context, 0); pdialog.setButton("cancel", new
		 * DialogInterface.OnClickListener() { public void
		 * onClick(DialogInterface dialog, int i) { dialog.dismiss(); } });
		 * pdialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
		 * public void onCancel(DialogInterface dialog) { finish(); } });
		 * pdialog.setCancelable(true); pdialog.setMax(100);
		 * pdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		 * pdialog.show();
		 * 
		 * }
		 */
		protected Object doInBackground(Object... params) {
			Cursor cursor = managedQuery(
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
					info.print();
				}
			}
			cursor.close();
			return null;
		}

		protected void onProgressUpdate(Integer... values) {
			audioAdapter.notifyDataSetChanged();
			// pdialog.setProgress(values[0]);
			
			if (values[0] == 100) {
				Toast.makeText(AudioActivity.this, "共" + audioAdapter.getCount() + "首歌.", Toast.LENGTH_SHORT);
				Log.w("" + Toast.LENGTH_LONG, "" + Toast.LENGTH_SHORT);
			}
			// pdialog.dismiss();

		}
	}
}