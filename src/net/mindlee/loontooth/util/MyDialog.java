package net.mindlee.loontooth.util;

import net.mindlee.loontooth.R;
import net.mindlee.loontooth.adapter.PopMenuAdapter;
import net.mindlee.loontooth.adapter.PopMenuAdapter.PopMenuItem;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
public class MyDialog {
	private Context context;
	private GridView menuGrid;

	public MyDialog(Context context) {
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
		menuGrid.setAdapter(new PopMenuAdapter(context));
		menuGrid.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return MotionEvent.ACTION_MOVE == event.getAction() ? true
						: false;
			}
		});

		menuGrid.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				/*
				 * if (position == PopMenuItem.SETTING.getIndex()) {
				 * 
				 * } else
				 */if (position == PopMenuItem.HELP.getIndex()) {
					createHelpDialog();
				} else if (position == PopMenuItem.ABOUT.getIndex()) {
					createAboutDialog();
				} else if (position == PopMenuItem.EMAIL.getIndex()) {
					Intent intent = new Intent(Intent.ACTION_SENDTO);
					intent.setData(Uri.parse("mailto:chinawelon@gmail.com"));
					context.startActivity(intent);
				} else if (position == PopMenuItem.BLOG.getIndex()) {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse("http://mindlee.net"));
					context.startActivity(intent);
				}

				/*
				 * else if (position == PopMenuItem.RELAX.getIndex()) {
				 * Animation shakeAnimY = AnimationUtils.loadAnimation(context,
				 * R.anim.shake_y); Animation shakeAnimX =
				 * AnimationUtils.loadAnimation(context, R.anim.shake_x);
				 * 
				 * menuGrid.startAnimation(shakeAnimX);
				 * menuGrid.startAnimation(shakeAnimY);
				 * 
				 * }
				 */
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