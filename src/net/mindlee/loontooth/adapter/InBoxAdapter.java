package net.mindlee.loontooth.adapter;

import java.io.File;
import java.util.List;

import net.mindlee.loontooth.R;
import net.mindlee.loontooth.util.MyFiles;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 用于加载收件箱中ListView的Adapter
 * 
 * @author MindLee
 * 
 */
public class InBoxAdapter extends BaseAdapter {

	private Bitmap floderBackDir;
	private Bitmap floderDoc;
	private Bitmap floderAudio;
	private Bitmap floderVideo;
	private Bitmap floderPhoto;
	private Bitmap floderOther;
	private Bitmap docAudio;
	private Bitmap docVideo;
	private Bitmap docPhoto;

	private Context context;
	private List<String> items;
	private List<String> paths;
	private MyFiles myFiles;

	public InBoxAdapter(Context context, List<String> items, List<String> paths) {
		this.context = context;
		this.items = items;
		this.paths = paths;
		myFiles = new MyFiles(context);
		floderBackDir = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.folder_back_dir);
		floderDoc = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.doc_doc);
		floderAudio = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.folder_inbox_music);
		floderVideo = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.folder_inbox_video);
		floderPhoto = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.floder_inbox_photo);
		floderOther = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.folder_inbox_other);
		docPhoto = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.doc_image);
		docAudio = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.doc_audio);
		docVideo = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.doc_video);

	}

	public int getCount() {
		return items.size();
	}

	public Object getItem(int position) {
		return items.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.browse_item, null);
			holder = new ViewHolder();
			holder.text = (TextView) convertView.findViewById(R.id.filePath);
			holder.icon = (ImageView) convertView.findViewById(R.id.fileIcon);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		File f = new File(paths.get(position).toString());
		String type = null;
		if (f.isFile()) {
			type = myFiles.getMIMEType(f);
			type = type.substring(0, type.length() - 2);
		}
		if (items.get(position).toString().equals("parentDir")) {
			holder.text.setText("返回上一层");
			holder.icon.setImageBitmap(floderBackDir);
		} else if (f.getName().equals("Photo")) {
			holder.text.setText("照片");
			holder.icon.setImageBitmap(floderPhoto);
		} else if (f.getName().equals("Music")) {
			holder.text.setText("音乐");
			holder.icon.setImageBitmap(floderAudio);
		} else if (f.getName().equals("Video")) {
			holder.text.setText("视频");
			holder.icon.setImageBitmap(floderVideo);
		} else if (f.getName().equals("Other")) {
			holder.text.setText("其他");
			holder.icon.setImageBitmap(floderOther);
		} else {
			holder.text.setText(f.getName());
			if (type.equals(MyFiles.AUDIO)) {
				holder.icon.setImageBitmap(docAudio);
			} else if (type.equals(MyFiles.VIDEO)) {
				holder.icon.setImageBitmap(docVideo);
			} else if (type.equals(MyFiles.IMAGE)) {
				holder.icon.setImageBitmap(docPhoto);
			} else {
				holder.icon.setImageBitmap(floderDoc);
			}
		}
		return convertView;
	}

	private class ViewHolder {
		TextView text;
		ImageView icon;
	}

}
