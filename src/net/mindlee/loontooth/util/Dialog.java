package net.mindlee.loontooth.util;

import net.mindlee.loontooth.R;
import net.mindlee.loontooth.adapter.MenuAdapter;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

/**
 * 对话框工具类，用于弹出应用中用到的各种对话框
 * 
 * @author 李伟
 * 
 */
public class Dialog {
	private Context context;
	private GridView menuGrid;

	public Dialog(Context context) {
		this.context = context;
	}

	/**
	 * 弹出"权限不足"对话款
	 */
	public void createNoAccessDialog() {
		new AlertDialog.Builder(context).setTitle("Message")
				.setMessage("权限不足!")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();
	}

	/**
	 * 弹出菜单对话框
	 */
	public void createMenuDialog() {
		View menuView = View.inflate(context, R.layout.menu_gridview, null);
		// 创建AlertDialog
		final AlertDialog menuDialog = new AlertDialog.Builder(context)
				.create();
		menuDialog.setView(menuView);
		menuGrid = (GridView) menuView.findViewById(R.id.gridview);
		menuGrid.setAdapter(new MenuAdapter(context));
		menuGrid.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return MotionEvent.ACTION_MOVE == event.getAction() ? true
						: false;
			}
		});

		menuGrid.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0) {

				} else if (position == 1) {
					createHelpDialog();
				} else if (position == 2) {
					createAboutDialog();
				} else if (position == 3) {
					Intent intent = new Intent(Intent.ACTION_SENDTO);
					intent.setData(Uri.parse("mailto:chinawelon@gmail.com"));
					context.startActivity(intent);
				} else if (position == 4) {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse("http://mindlee.net"));
					context.startActivity(intent);
				} else if (position == 5) {

				}
			}
		});

		menuDialog.show();
	}

	/**
	 * 弹出帮助对话框
	 */
	public void createHelpDialog() {
		View helpView = View.inflate(context, R.layout.help_or_about, null);
		AlertDialog helpDialog = new AlertDialog.Builder(context).create();
		helpDialog.setView(helpView);
		TextView helpText = (TextView) helpView
				.findViewById(R.id.help_or_about);
		helpText.setText("  本应用采用蓝牙传输，建议两台手机距离不超过10米。\n\n" + "1)建立连接\n"
				+ "2)搜索加入\n" + "3)互相传输文件\n");
		helpDialog.show();
	}

	/**
	 * 弹出关于对话框
	 */
	public void createAboutDialog() {
		View helpView = View.inflate(context, R.layout.help_or_about, null);
		AlertDialog helpDialog = new AlertDialog.Builder(context).create();
		helpDialog.setView(helpView);
		TextView helpText = (TextView) helpView
				.findViewById(R.id.help_or_about);
		helpText.setText("\t\t\t\t\tLoonTooth\n" + "\t\t\t\t\t\t\t笨牙\n\n"
				+ "版本：LoonTooth v1.0\n" + "作者：李伟\n" + "博客：http://mindlee.net\n"
				+ "邮箱：mindlee@me.com\n\n" + "\t\t\tMabbage Studio\n"
				+ "\t\t\tCopyRight(c)2012\n" + "\t\tAll Rights Reserved");
		helpDialog.show();
	}
}
