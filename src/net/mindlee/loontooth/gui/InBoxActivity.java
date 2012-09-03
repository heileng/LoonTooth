package net.mindlee.loontooth.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.mindlee.loontooth.R;
import net.mindlee.loontooth.adapter.InBoxAdapter;
import net.mindlee.loontooth.util.Dialog;
import net.mindlee.loontooth.util.Files;
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

public class InBoxActivity extends Activity {
	private ListView inBoxListView;
	private Files filesOperate = new Files(this);
	private Dialog dialog = new Dialog(this);
	private List<String> items = null;
	private List<String> paths = null;
	private int focusFilesItem;
	private PopupWindow downMenuPopWindow;
	private PopWindow popWindow;
	private String sdCardPath = Environment.getExternalStorageDirectory()
			.getPath();
	private String inBoxPath = sdCardPath + "/LoonTooth";
	private String audioPath = inBoxPath + "/Music";
	private String videoPath = inBoxPath + "/Video";
	private String photoPath = inBoxPath + "/Photo";
	private String otherPath = inBoxPath + "/Other";
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
						downMenuPopWindow.showAsDropDown(view,
								view.getWidth() / 2, -view.getHeight() / 2);
						return false;
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

	private void createInboxFileDir() {
		createFileDir(inBoxPath);
		createFileDir(audioPath);
		createFileDir(videoPath);
		createFileDir(photoPath);
		createFileDir(otherPath);
	}

	private void createFileDir(String filePath) {
		File fileDir = new File(filePath);
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}
		Log.w("" + filePath, "" + fileDir.exists());
	}

	private void getFileDir(String filePath) {
		Toast.makeText(this, filePath, Toast.LENGTH_SHORT).show();
		items = new ArrayList<String>();
		paths = new ArrayList<String>();
		File f = new File(filePath);
		File[] files = f.listFiles();

		if (filePath.equals(inBoxPath)) {
			items.add("photoDir");
			paths.add(photoPath);

			items.add("audioDir");
			paths.add(audioPath);

			items.add("videoDir");
			paths.add(videoPath);

			items.add("otherDir");
			paths.add(otherPath);
		}

		if (!filePath.equals(inBoxPath)) {
			items.add("parentDir");
			paths.add(f.getParent());
		}

		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			items.add(file.getName());
			paths.add(file.getPath());
		}

		inBoxListView.setAdapter(new InBoxAdapter(this, items, paths));

	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			long now = new Date().getTime();
			if (now - mLastBackTime < TIME_DIFF) {
				return super.onKeyDown(keyCode, event);
			} else {
				mLastBackTime = now;
				Toast.makeText(this, "再点击一次退出程序", 2000).show();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
