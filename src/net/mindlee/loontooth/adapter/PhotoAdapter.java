package net.mindlee.loontooth.adapter;

import java.util.ArrayList;

import net.mindlee.loontooth.R;
import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * 给PhotoActivity中photoList的加载内容的Adapter
 * 
 * @author 李伟
 * 
 */
public class PhotoAdapter extends BaseAdapter {
	private Activity context;
	private ArrayList<PhotoInfo> photoList = new ArrayList<PhotoInfo>();

	public static class PhotoInfo {
		public String title;
		public String mimeType;
		public String filePath;
		public String size;
		public String dateModified;
		public String thumbPath;
		public Bitmap bitmap;

		public void print() {
			Log.v("title", title + "");
			Log.v("mimeType", mimeType + "");
			Log.v("filePath", filePath + "");
			Log.v("size", size + "");
			Log.v("dateModified", dateModified + "");
			Log.v("thumbPath", thumbPath + "");
		}

		public Bitmap getBitmap() {
			return bitmap;
		}

	}

	public PhotoAdapter(Activity context, ArrayList<PhotoInfo> photoList) {
		this.context = context;
		this.photoList = photoList;
	}

	public ArrayList<PhotoInfo> getPhotoList() {
		return photoList;
	}

	public int getCount() {
		return photoList.size();
	}

	public void addPhoto(PhotoInfo photo) {
		photoList.add(photo);
	}

	public void removeItem(int position) {
		photoList.remove(position);
	}

	public Object getItem(int position) {
		return photoList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();

			convertView = View.inflate(context, R.layout.photo_item, null);
			holder.photo = (ImageView) convertView
					.findViewById(R.id.photo_imageview);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.photo.setImageBitmap(photoList.get(position).getBitmap());

		return convertView;
	}

	class ViewHolder {
		ImageView photo;
	}
}