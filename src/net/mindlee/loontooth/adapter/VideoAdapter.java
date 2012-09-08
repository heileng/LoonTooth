package net.mindlee.loontooth.adapter;

import java.util.ArrayList;
import net.mindlee.loontooth.R;
import net.mindlee.loontooth.util.Tools;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 给VideoActivity中videoList的加载内容的Adapter
 * @author 李伟
 *
 */
public class VideoAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<VideoInfo> videoList = new ArrayList<VideoInfo>();

	public static class VideoInfo {
		public String filePath;
		public String mimeType;
		public String size;
		public String title;
		public String duration;
		public String dateModified;
		public Bitmap bitmap;

		public Bitmap getBitmap() {
			return bitmap;
		}
	}

	public ArrayList<VideoInfo> getVideoList() {
		return videoList;
	}

	public VideoAdapter(Context context, ArrayList<VideoInfo> videoList) {
		this.context = context;
		this.videoList = videoList;
	}

	@Override
	public int getCount() {
		return videoList.size();
	}

	@Override
	public Object getItem(int p) {
		return videoList.get(p);
	}

	@Override
	public long getItemId(int p) {
		return p;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();

			convertView = View.inflate(context, R.layout.video_item, null);
			holder.thumb = (ImageView) convertView
					.findViewById(R.id.video_thumb_imageView);
			holder.title = (TextView) convertView
					.findViewById(R.id.video_title_textView);
			holder.duration = (TextView) convertView
					.findViewById(R.id.video_duration_textView);
			holder.size = (TextView) convertView
					.findViewById(R.id.video_size_textView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// 显示信息
		holder.title.setText(videoList.get(position).title);
		holder.duration
				.setText(Tools.durationFormat(videoList.get(position).duration));
		holder.size.setText(Tools.sizeFormat(videoList.get(position).size));
		holder.thumb.setImageBitmap(videoList.get(position).getBitmap());
		return convertView;
	}

	class ViewHolder {
		ImageView thumb;
		TextView title;
		TextView duration;
		TextView size;
	}
}