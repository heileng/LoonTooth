package net.mindlee.loontooth.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import net.mindlee.loontooth.R;

/**
 * 用于更多（overflow）中弹出菜单的加载adapter
 * @author 李伟
 *
 */
public class MenuAdapter extends BaseAdapter{
	private Context context;
	private int[] menuImageArray = { R.drawable.menu_set, R.drawable.menu_help,
			R.drawable.menu_about, R.drawable.menu_email,
			R.drawable.menu_website, R.drawable.menu_play, };
	private String[] menuNameArray = { "设置", "帮助", "关于", "邮件反馈", "作者博客",
			"轻松一下" };

    public MenuAdapter(Context context) {
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
