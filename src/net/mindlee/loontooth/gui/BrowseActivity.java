package net.mindlee.loontooth.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.mindlee.loontooth.R;
import net.mindlee.loontooth.adapter.AllFilesAdapter;
import net.mindlee.loontooth.util.Dialog;
import net.mindlee.loontooth.util.Files;
import net.mindlee.loontooth.util.PopWindow;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

public class BrowseActivity extends Activity {
	private List<String> items = null;
	private List<String> paths = null;
	private String rootPath = Environment.getExternalStorageDirectory()
			.getPath();
	private ListView allFilesListView;
	private int focusFilesItem;
	private Files filesOperate = new Files(this);
	private Dialog dialog = new Dialog(this);
	private PopupWindow downMenuPopWindow;
	private PopWindow popWindow;
	private long mLastBackTime = 0;
	private long TIME_DIFF = 2 * 1000;

	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browse);
		allFilesListView = (ListView) findViewById(R.id.all_files_listView);
		getFileDir(rootPath);

		allFilesListView
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

		allFilesListView
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

	private void getFileDir(String filePath) {
		Toast.makeText(this, filePath, Toast.LENGTH_SHORT).show();
		items = new ArrayList<String>();
		paths = new ArrayList<String>();
		File f = new File(filePath);
		File[] files = f.listFiles();

		if (!filePath.equals(rootPath)) {
			items.add("rootPath");
			paths.add(rootPath);

			items.add("parentDir");
			paths.add(f.getParent());
		}

		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			items.add(file.getName());
			paths.add(file.getPath());
		}

		allFilesListView.setAdapter(new AllFilesAdapter(this, items, paths));
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