package net.mindlee.loontooth.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.mindlee.loontooth.R;
import net.mindlee.loontooth.util.Tools;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AudioAdapter extends BaseAdapter {
	private Context context;
	private List<AudioInfo> audioList = new ArrayList<AudioInfo>();
	
	private String[] audioColumns = new String[] { MediaStore.Audio.Media.DATA,
			MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST,
			MediaStore.Audio.Media.MIME_TYPE, MediaStore.Audio.Media.SIZE,
			MediaStore.Audio.Media.DATE_MODIFIED,
			MediaStore.Audio.Media.IS_MUSIC, MediaStore.Audio.Albums.ALBUM_ID, };

	public static class AudioInfo {
		public String filePath;
		public String albumArt;
		public String title;
		public String artist;
		public String mimeType;
		public String size;
		public String dateModified;
		public Boolean isMusic;

		void print() {
			Log.i("filePath", filePath + "");
			Log.i("albumArt", albumArt + "");
			Log.i("title", title + "");
			Log.i("artist", artist + "");
			Log.i("mimeType", mimeType + "");
			Log.i("size", size + "");
			Log.i("dateModified", dateModified + "");
			Log.i("isMusic", isMusic + "");
		}
	}

	public AudioAdapter(Context context) {
		this.context = context;
		readAudioData();
	}

	public int getCount() {
		return audioList.size();
	}

	public Object getItem(int p) {
		return audioList.get(p);
	}

	public long getItemId(int p) {
		return p;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.audio_item, null);
			holder.albumArt = (ImageView) convertView
					.findViewById(R.id.audio_album_imageView);
			holder.title = (TextView) convertView
					.findViewById(R.id.audio_title_textView);
			holder.artist = (TextView) convertView
					.findViewById(R.id.audio_artist_textView);
			holder.size = (TextView) convertView
					.findViewById(R.id.audio_size_textView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (audioList.get(position).isMusic) {
			if (audioList.get(position).albumArt != null) {
				// holder.albumArt
				// .setImageURI(Uri.parse(audioItems.get(position).albumArt));
			}

			if (audioList.get(position).title != null) {
				holder.title.setText(audioList.get(position).title);
				String str = Tools.sizeFormat(audioList.get(position).size);
				holder.size.setText(str);
			}

			if (audioList.get(position).artist != null) {
				holder.artist.setText(audioList.get(position).artist);
			} else {
				holder.artist.setText("");
			}
		}
		return convertView;
	}

	class ViewHolder {
		ImageView albumArt;
		TextView title;
		TextView artist;
		TextView size;
	}

	public List<AudioInfo> getAudioList() {
		return audioList;
	}
	
	private void readAudioData() {
		Cursor cursor = ((Activity) context).managedQuery(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, audioColumns,
				null, null, null);
		if (cursor.moveToFirst()) {
			while (cursor.moveToNext()) {
				AudioInfo info = new AudioInfo();
				info.filePath = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
				info.title = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
				info.artist = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
				info.mimeType = cursor
						.getString(cursor
								.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE));
				info.size = cursor.getString(cursor
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
				}
				audioList.add(info);
				info.print();
			}
		}
	}

}