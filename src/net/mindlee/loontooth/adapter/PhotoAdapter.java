package net.mindlee.loontooth.adapter;

import java.util.ArrayList;

import net.mindlee.loontooth.R;
import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

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

	public Object getItem(int position) {
		return photoList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		final ImageView imageView;

		if (convertView == null) {
			imageView = new ImageView(context);
		} else {
			imageView = (ImageView) convertView;
		}

		imageView.setLayoutParams(new GridView.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		imageView.setBackgroundDrawable(context.getResources().getDrawable(
				R.drawable.white_border));

		imageView.setPadding(2, 2, 2, 2);
		imageView.setImageBitmap(photoList.get(position).getBitmap());

		return imageView;
	}

}