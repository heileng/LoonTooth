package net.mindlee.loontooth.gui;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.mindlee.loontooth.R;
import net.mindlee.loontooth.adapter.InBoxAdapter;
import net.mindlee.loontooth.gui.PhotoActivity.LoadImagesFromSDCard;
import net.mindlee.loontooth.util.Dialog;
import net.mindlee.loontooth.util.CustomFiles;
import net.mindlee.loontooth.util.PopWindow;
import android.app.Activity;
import android.os.AsyncTask;
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
 * 
 * @author 李伟
 * 
 */
public class InBoxActivity extends Activity {
	private ListView inBoxListView;
	private CustomFiles customFiles = new CustomFiles(this);
	private Dialog dialog = new Dialog(this);
	private List<String> items = null;
	private List<String> paths = null;
	private int focusFilesItem;
	private PopupWindow downMenuPopWindow;
	private PopWindow popWindow;
	private InBoxAdapter inBoxAdapter;

	public static String sdCardPath = Environment.getExternalStorageDirectory()
			.getPath();
	public String bluetoothPath = getBluetoothPath(sdCardPath);
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
		new LoadInbox().execute();
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
								customFiles.openFile(file);
							}
						} else if (position == 2) {
							customFiles.openDetailsDialog(file);
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
	 * 从蓝牙文件夹移动文件到收件箱。
	 */
	private void moveFileFromBluetoothDirToInbox() {

		File file = new File(bluetoothPath);
		File[] files = file.listFiles();
		for (File f : files) {
			String type = customFiles.getMIMEType(f);
			type = type.substring(0, type.length() - 2);
			if (type.equals("audio")) {
				moveFile(f.getPath(), audioPath);
				f.delete();
				
			} else if (type.equals("video")) {
				moveFile(f.getPath(), videoPath);
				f.delete();
			} else if (type.equals("image")) {
				moveFile(f.getPath(), photoPath);
				f.delete();
			} else {
				moveFile(f.getPath(), otherPath);
				f.delete();
			}
		}
	}

	/**
	 * 移动文件
	 * 
	 * @param srcFileName
	 *            源文件完整路径
	 * @param destDirName
	 *            目的目录完整路径
	 * @return 文件移动成功返回true，否则返回false
	 */
	public void moveFile(String srcFileName, String destDirName) {

		File srcFile = new File(srcFileName);
		File desFile = new File(destDirName + File.separator
				+ srcFile.getName());
		if (!desFile.exists()) {
			try {
				desFile.createNewFile();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

		// 实例输入输出的文件流
		InputStream in = null;
		OutputStream out = null;
		BufferedInputStream inb = null;
		BufferedOutputStream oub = null;
		try {
			// 构建文件输入流
			in = new FileInputStream(srcFile);
			// 构建文件输出流
			out = new FileOutputStream(desFile);
			inb = new BufferedInputStream(in);
			oub = new BufferedOutputStream(out);
			// 开始复制
			byte[] read = new byte[1024];
			int len = in.read(read);
			while (len != -1) {
				oub.write(read, 0, len);
				len = in.read(read);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				oub.close();
				inb.close();
				out.close();
				in.close();
			} catch (Exception e0) {
				e0.printStackTrace();
			}
		}
	}

	/**
	 * 根据SD卡路径，获取蓝牙文件夹路径
	 * 
	 * @param sdCardPath
	 * @return 蓝牙文件夹路径
	 */
	private String getBluetoothPath(String sdCardPath) {
		String bluetoothPath = null;
		File file = new File(sdCardPath);
		File[] files = file.listFiles();
		for (File f : files) {
			if (f.getName().toLowerCase().equals("bluetooth")) {
				bluetoothPath = f.getPath();
			}
		}
		return bluetoothPath;
	}

	/**
	 * 根据文件路径创建文件
	 * 
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
	 * 
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

		inBoxAdapter = new InBoxAdapter(this, items, paths);
		inBoxListView.setAdapter(inBoxAdapter);
	}

	/**
	 * 判断listview中item是不是返回键，如果是，返回true；否则，返回false
	 * 
	 * @param position
	 * @return
	 */
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

	class LoadInbox extends AsyncTask<Object, Integer, Object> {
		protected Object doInBackground(Object... arg0) {
			moveFileFromBluetoothDirToInbox();
			publishProgress(20);
			return null;
		}
		
		protected void onProgressUpdate(Integer... values) {
			inBoxAdapter.notifyDataSetChanged();
		}
	}
}
