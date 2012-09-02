package net.mindlee.loontooth.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import net.mindlee.loontooth.R;

public class DownMenuAdapter extends BaseAdapter {
	private int[] icons = { R.drawable.downmenu_share,
			R.drawable.downmenu_open, R.drawable.downmenu_detail };
	private String[] items = { "传输", "打开", "属性" };
	private Context context;

	public DownMenuAdapter(Context context) {
		this.context = context;
	}

	public int getCount() {
		return items.length;
	}

	public Object getItem(int position) {
		return items[position];
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v = View.inflate(context, R.layout.downmenu_item, null);// 绑定自定义的layout
		ImageView iv = (ImageView) v.findViewById(R.id.icon);
		TextView tv = (TextView) v.findViewById(R.id.title);
		iv.setImageResource(icons[position]);
		tv.setText(items[position]);
		return v;
	}
}
