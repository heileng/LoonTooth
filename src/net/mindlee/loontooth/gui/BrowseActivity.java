package net.mindlee.loontooth.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.mindlee.loontooth.R;
import net.mindlee.loontooth.adapter.BrowseAdapter;
import net.mindlee.loontooth.adapter.DownMenuAdapter.DownMenuItem;
import net.mindlee.loontooth.gui.BaseActivity.ViewInfo;
import net.mindlee.loontooth.util.MyDialog;
import net.mindlee.loontooth.util.MyFiles;
import net.mindlee.loontooth.util.MyPopWindow;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

/**
 * 全部文件浏览 主界面
 * 
 * @author 李伟
 * 
 */
public class BrowseActivity extends BaseActivity {
	private List<String> items = null;
	private List<String> paths = null;
	private String rootPath = Environment.getExternalStorageDirectory()
			.getPath();
	private ListView browseListView;
	private MyFiles myFiles = new MyFiles(this);
	private MyDialog myDialog = new MyDialog(this);
	private PopupWindow downMenuPopWindow;
	private MyPopWindow myPopWindow;
	private BrowseAdapter browseAdapter;

	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browse);
		browseListView = (ListView) findViewById(R.id.browse_listView);
		getFileDir(rootPath);

		/*
		 * ListView点击事件
		 */
		browseListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						File file = new File(paths.get(position));
						ViewInfo.FOCUSED_ITEM.setValue(position);
						if (file.canRead()) {
							if (file.isDirectory()) {
								getFileDir(paths.get(position));
							} else {
								int width = 0;
								int height = 0;
								if (view.getY() < parent.getHeight() / 2) {
									width = view.getWidth() / 2;
									height = -view.getHeight() / 2;
								} else {
									width = view.getWidth() / 2;
									height = -view.getHeight() / 2
											- downMenuPopWindow.getHeight()
											+ 10;
									Log.w("popWindow宽"
											+ downMenuPopWindow.getWidth(),
											"高度"
													+ downMenuPopWindow
															.getHeight());
								}
								downMenuPopWindow.showAsDropDown(view, width,
										height);
							}
						} else {
							myDialog.createNoAccessDialog();
						}
					}
				});

		/*
		 * ListView长按事件
		 */
		browseListView
				.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						File file = new File(paths.get(position));
						ViewInfo.FOCUSED_ITEM.setValue(position);
						if (file.canRead()) {
							if (file.isDirectory()) {
								getFileDir(paths.get(position));
							} else {
								int width = 0;
								int height = 0;
								if (view.getY() < parent.getHeight() / 2) {
									width = view.getWidth() / 2;
									height = -view.getHeight() / 2;
								} else {
									width = view.getWidth() / 2;
									height = -view.getHeight() / 2
											- downMenuPopWindow.getHeight()
											+ 10;
									Log.w("popWindow宽"
											+ downMenuPopWindow.getWidth(),
											"高度"
													+ downMenuPopWindow
															.getHeight());
								}
								downMenuPopWindow.showAsDropDown(view, width,
										height);
							}
						} else {
							myDialog.createNoAccessDialog();
						}
						return true;
					}

				});

		myPopWindow = new MyPopWindow(this);
		downMenuPopWindow = myPopWindow.createDownMenu();
		/*
		 * 下拉菜单点击事件
		 */
		myPopWindow.getDownMenuListView().setOnItemClickListener(
				new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						File file = new File(paths.get(ViewInfo.FOCUSED_ITEM
								.getValue()));
						downMenuPopWindow.dismiss();
						if (position == DownMenuItem.TRANSFER.getIndex()) {

						} else if (position == DownMenuItem.OPEN.getIndex()) {

							if (file.isDirectory()) {
								getFileDir(paths.get(ViewInfo.FOCUSED_ITEM
										.getValue()));
							} else {
								myFiles.openFile(file);
							}
						} else if (position == DownMenuItem.DELETE.getIndex()) {

							boolean isDelete = myDialog
									.createIsSureDeleteDialog();
							if (isDelete) {
								file.delete();
								browseAdapter.removeItem(ViewInfo.FOCUSED_ITEM
										.getValue());
								browseAdapter.notifyDataSetChanged();
							}
						} else if (position == DownMenuItem.DETAIL.getIndex()) {
							myFiles.openDetailsDialog(file);
						}
					}
				});
	}

	/**
	 * ListView中添加搜索到的文件项
	 */
	private void getFileDir(String filePath) {
		items = new ArrayList<String>();
		paths = new ArrayList<String>();
		File f = new File(filePath);
		File[] files = f.listFiles();

		if (!filePath.equals(rootPath)) {
			items.add("rootPath");
			paths.add(rootPath);

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
		browseAdapter = new BrowseAdapter(this, items, paths);
		browseListView.setAdapter(browseAdapter);
	}
}