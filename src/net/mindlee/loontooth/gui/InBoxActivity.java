package net.mindlee.loontooth.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.mindlee.loontooth.R;
import net.mindlee.loontooth.adapter.InBoxAdapter;
import net.mindlee.loontooth.util.Dialog;
import net.mindlee.loontooth.util.CustomFiles;
import net.mindlee.loontooth.util.PopWindow;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

/**
 * 收件箱主界面
 * @author 李伟
 *
 */
public class InBoxActivity extends Activity {
	private ListView inBoxListView;
	private CustomFiles filesOperate = new CustomFiles(this);
	private Dialog dialog = new Dialog(this);
	private List<String> items = null;
	private List<String> paths = null;
	private int focusFilesItem;
	private PopupWindow downMenuPopWindow;
	private PopWindow popWindow;
	public static String sdCardPath = Environment.getExternalStorageDirectory()
			.getPath();
	public static String inBoxPath = sdCardPath + "/LoonTooth";
	public static String audioPath = inBoxPath + "/Music";
	public static String videoPath = inBoxPath + "/Video";
	public static String photoPath = inBoxPath + "/Photo";
	public static String otherPath = inBoxPath + "/Other";
	private long mLastBackTime = 0;
	private long TIME_DIFF = 2 * 1000;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inbox);
		inBoxListView = (ListView) findViewById(R.id.inbox_listView);
		createInboxFileDir();
		getFileDir(inBoxPath);

		inBoxListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						File file = new File(paths.get(position));
						focusFilesItem = position;
						if (file.canRead()) {
							if (file.isDirectory()) {
								getFileDir(paths.get(position));
							} else {
								downMenuPopWindow.showAsDropDown(view,
										view.getWidth() / 2,
										-view.getHeight() / 2);
							}
						} else {
							dialog.createNoAccessDialog();
						}
					}
				});

		inBoxListView
				.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						focusFilesItem = position;
						if (isOperateItem(position)) {
							getFileDir(paths.get(position));
						} else {
							downMenuPopWindow.showAsDropDown(view,
									view.getWidth() / 2, -view.getHeight() / 2);
						}
						return true;
					}
				});

		popWindow = new PopWindow(this);
		downMenuPopWindow = popWindow.createDownMenu();
		popWindow.getDownMenuListView().setOnItemClickListener(
				new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						File file = new File(paths.get(focusFilesItem));
						if (position == 0) {
							downMenuPopWindow.dismiss();
						} else if (position == 1) {
							downMenuPopWindow.dismiss();
							if (file.isDirectory()) {
								getFileDir(paths.get(focusFilesItem));
							} else {
								filesOperate.openFile(file);
							}
						} else if (position == 2) {
							filesOperate.openDetailsDialog(file);
							downMenuPopWindow.dismiss();
						}
					}
				});

	}

	/**
	 * 创建收件箱，以及收件箱中的文件夹，包括视频，音乐，照片，其他
	 */
	private void createInboxFileDir() {
		createFileDir(inBoxPath);
		createFileDir(audioPath);
		createFileDir(videoPath);
		createFileDir(photoPath);
		createFileDir(otherPath);
	}

	/**
	 * 根据文件路径创建文件
	 * @param filePath
	 */
	private void createFileDir(String filePath) {
		File fileDir = new File(filePath);
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}
		Log.w("" + filePath, "" + fileDir.exists());
	}

	/**
	 * 获取收件箱中的文件列表
	 * @param filePath
	 */
	private void getFileDir(String filePath) {
		items = new ArrayList<String>();
		paths = new ArrayList<String>();
		File f = new File(filePath);
		File[] files = f.listFiles();

		if (!filePath.equals(inBoxPath)) {
			items.add("parentDir");
			paths.add(f.getParent());
		} else {
			Toast.makeText(this, "已到达根目录", Toast.LENGTH_SHORT).show();
		}

		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			items.add(file.getName());
			paths.add(file.getPath());
		}

		inBoxListView.setAdapter(new InBoxAdapter(this, items, paths));

	}

	private boolean isOperateItem(int position) {
		if (items.get(position).toString().equals("parentDir")) {
			return true;
		}
		return false;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			long now = new Date().getTime();
			if (now - mLastBackTime < TIME_DIFF) {
				return super.onKeyDown(keyCode, event);
			} else {
				mLastBackTime = now;
				Toast.makeText(this, "再点击一次退出程序", Toast.LENGTH_SHORT).show();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public String getInboxPath() {
		return inBoxPath;
	}

	public String getAudioPath() {
		return audioPath;
	}

	public String getVideoPath() {
		return videoPath;
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public String getOtherPath() {
		return otherPath;
	}
}
