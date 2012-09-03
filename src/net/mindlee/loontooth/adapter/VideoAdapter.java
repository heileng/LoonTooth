package net.mindlee.loontooth.adapter;

import java.util.ArrayList;
import net.mindlee.loontooth.R;
import net.mindlee.loontooth.gui.MainActivity;
import net.mindlee.loontooth.util.Tools;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class VideoAdapter extends BaseAdapter {
	private Context context;
	private Cursor cursor;
	
	private ArrayList<VideoInfo> videoList = new ArrayList<VideoInfo>();
	
	private String[] mediaColumns = new String[] { MediaStore.Video.Media.DATA,
			MediaStore.Video.Media._ID, MediaStore.Video.Media.TITLE,
			MediaStore.Video.Media.DURATION, MediaStore.Video.Media.SIZE,
			MediaStore.Video.Media.MIME_TYPE,
			MediaStore.Video.Media.DATE_MODIFIED };

	public static class VideoInfo {
		public String filePath;
		public String mimeType;
		public String size;
		public String title;
		public String duration;
		public String dateModified;
	}

	public  ArrayList<VideoInfo> getVideoList() {
		return videoList;
	}
	
	public VideoAdapter(Context context) {
		this.context = context;
		// 首先检索SDcard上所有的video
		cursor = ((Activity) context).managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
				mediaColumns, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				VideoInfo info = new VideoInfo();

				info.filePath = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
				info.mimeType = cursor
						.getString(cursor
								.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));
				Log.w("视频格式", info.mimeType);
				info.title = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
				info.duration = cursor
						.getString(cursor
								.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
				info.size = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
				info.dateModified = cursor
						.getString(cursor
								.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED));

				// 然后将其加入到videoList
				videoList.add(info);

			} while (cursor.moveToNext());
		}
		cursor.close();

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
		holder.duration.setText(Tools.durationFormat(videoList
				.get(position).duration));
		holder.size
				.setText(Tools.sizeFormat(videoList.get(position).size));
		Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(
				videoList.get(position).filePath, Thumbnails.MICRO_KIND);
		int width = MainActivity.SCREEN_WIDTH / 4;
		int height = width * 3 / 4;
        Bitmap bitmap1 = ThumbnailUtils.extractThumbnail(bitmap, width, height);
        bitmap.recycle();
		System.out.println("宽" + bitmap1.getWidth());
		System.out.println("高" + bitmap1.getHeight());
		holder.thumb.setImageBitmap(bitmap1);

		return convertView;
	}

	class ViewHolder {
		ImageView thumb;
		TextView title;
		TextView duration;
		TextView size;
	}
}