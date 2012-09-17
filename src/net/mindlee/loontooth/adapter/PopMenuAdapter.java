package net.mindlee.loontooth.adapter;

import net.mindlee.loontooth.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 用于更多（overflow）中弹出菜单的加载adapter
 * 
 * @author 李伟
 * 
 */
public class PopMenuAdapter extends BaseAdapter {
	private Context context;
	private int[] menuImageArray = { R.drawable.menu_help,
			R.drawable.menu_about, R.drawable.menu_email,
			R.drawable.menu_website, };
	private String[] menuNameArray = { PopMenuItem.HELP.getName(),
			PopMenuItem.ABOUT.getName(), PopMenuItem.EMAIL.getName(),
			PopMenuItem.BLOG.getName(), };

	public enum PopMenuItem {
		HELP("帮助", 0), ABOUT("关于", 1), EMAIL("邮件反馈", 2), BLOG("作者博客", 3);
		// 成员变量
		private String name;
		private int index;

		public String toString() {
			return name;
		}

		// 构造方法
		private PopMenuItem(String name, int index) {
			this.name = name;
			this.index = index;
		}

		// get set 方法
		public String getName() {
			return name;
		}

		public int getIndex() {
			return index;
		}

	}

	public PopMenuAdapter(Context context) {
		this.context = context;
	}

	public int getCount() {
		return menuNameArray.length;
	}

	public Object getItem(int position) {
		return menuNameArray[position];
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v = View.inflate(context, R.layout.menu_item, null);// 绑定自定义的layout
		ImageView imageView = (ImageView) v.findViewById(R.id.item_image);
		TextView textView = (TextView) v.findViewById(R.id.item_text);
		imageView.setImageResource(menuImageArray[position]);
		textView.setText(menuNameArray[position]);
		return v;
	}

}
