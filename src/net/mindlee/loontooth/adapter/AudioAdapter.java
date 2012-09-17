package net.mindlee.loontooth.adapter;

import java.util.ArrayList;
import java.util.List;

import net.mindlee.loontooth.R;
import net.mindlee.loontooth.gui.AudioActivity;
import net.mindlee.loontooth.gui.LoonToothApplication;
import net.mindlee.loontooth.util.MyTools;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 给AudioActivity中audioList的加载内容的Adapter
 * 
 * @author 李伟
 * 
 */
public class AudioAdapter extends BaseAdapter {
	private AudioActivity context;
	private List<AudioInfo> audioList = new ArrayList<AudioInfo>();
	private Bitmap audioBg;

	public static class AudioInfo {
		public String filePath;
		public String albumArt;
		public String title;
		public String artist;
		public String mimeType;
		public String size;
		public String dateModified;
		public Boolean isMusic;
		public Bitmap bitmap;

		public Bitmap getBitmap() {
			return bitmap;
		}

		public void print() {
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

	public AudioAdapter(AudioActivity context, List<AudioInfo> audioList) {
		this.context = context;
		this.audioList = audioList;
		Bitmap bmp = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.audio_default_background);
		int width = LoonToothApplication.getScreenWidth()/ 5;
		audioBg = Bitmap.createScaledBitmap(bmp, width, width, true);
	}

	public int getCount() {
		return audioList.size();
	}

	public Object getItem(int p) {
		return audioList.get(p);
	}

	public void clear() {
		audioList.clear();
	}

	public long getItemId(int p) {
		return p;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.audio_item, null);
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
			}

			holder.title.setText(audioList.get(position).title);
			String str = MyTools.sizeFormat(audioList.get(position).size);
			holder.size.setText(str);
			holder.artist.setText(audioList.get(position).artist);
			if (audioList.get(position).bitmap != null) {
				holder.albumArt.setImageBitmap(audioList.get(position).bitmap);
			} else {
				holder.albumArt.setImageBitmap(audioBg);
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

}