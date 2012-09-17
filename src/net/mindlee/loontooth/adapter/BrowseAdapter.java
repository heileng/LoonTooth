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
 * 用于BrowseActivity中文件列表ListView中的Adapter
 * 
 * @author 李伟
 * 
 */
public class BrowseAdapter extends BaseAdapter {
	// private LayoutInflater mInflater;
	private Bitmap floderBackDir;
	private Bitmap floderFloder;
	private Bitmap floderDoc;
	private Bitmap docAudio;
	private Bitmap docVideo;
	private Bitmap docPhoto;
	private Context context;
	private List<String> items;
	private List<String> paths;
	private MyFiles myFiles;

	public BrowseAdapter(Context context, List<String> items, List<String> paths) {
		this.context = context;
		this.items = items;
		this.paths = paths;
		myFiles = new MyFiles(context);
		floderBackDir = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.folder_back_dir);
		floderFloder = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.folder_light);
		floderDoc = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.doc_doc);
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

	public void removeItem(int position) {
		items.remove(position);
		paths.remove(position);
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
		if (items.get(position).toString().equals("rootPath")) {
			holder.text.setText("返回根目录");
			holder.icon.setImageBitmap(floderBackDir);
		} else if (items.get(position).toString().equals("parentDir")) {
			holder.text.setText("返回上一层");
			holder.icon.setImageBitmap(floderBackDir);
		} else {
			holder.text.setText(f.getName());
			if (f.isDirectory()) {
				holder.icon.setImageBitmap(floderFloder);
			} else if (type.equals(MyFiles.AUDIO)) {
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
