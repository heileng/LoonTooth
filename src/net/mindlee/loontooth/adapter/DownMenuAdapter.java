package net.mindlee.loontooth.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import net.mindlee.loontooth.R;

/**
 * 下拉菜单的Adapter
 * 
 * @author 李伟
 * 
 */
public class DownMenuAdapter extends BaseAdapter {
	private int[] icons = { R.drawable.downmenu_send,
			R.drawable.downmenu_open, R.drawable.downmenu_delete,  R.drawable.downmenu_detail };
	private String[] items = { DownMenuItem.TRANSFER.name, DownMenuItem.OPEN.name, DownMenuItem.DELETE.name, DownMenuItem.DETAIL.name};
	private Context context;

	public enum DownMenuItem {
		TRANSFER("传输", 0), OPEN("打开", 1), DELETE("删除", 2), DETAIL("属性", 3);
		// 成员变量
		private String name;
		private int index;

		// 构造方法
		private DownMenuItem(String name, int index) {
			this.name = name;
			this.index = index;
		}

		// 重写toString
		public String toString() {
			return this.name;
		}

		// 普通方法
		public static String getName(int index) {
			for (DownMenuItem c : DownMenuItem.values()) {
				if (c.getIndex() == index) {
					return c.name;
				}
			}
			return null;
		}

		// get set 方法
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}
	}

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
