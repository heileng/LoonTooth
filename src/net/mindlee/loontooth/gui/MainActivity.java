package net.mindlee.loontooth.gui;

import net.mindlee.loontooth.R;
import net.mindlee.loontooth.bluetooth.Client;
import net.mindlee.loontooth.bluetooth.Server;
import net.mindlee.loontooth.util.MyDialog;
import net.mindlee.loontooth.util.MyPopWindow;
import net.mindlee.loontooth.util.MyTools;
import android.app.ActionBar;
import android.app.ActivityGroup;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 程序入口主界面 MainActivity
 * 
 * @author 李伟
 * 
 */
public class MainActivity extends ActivityGroup {
	private PopupWindow deviceSearchedPopWindow;
	private Server server = new Server(this);
	private Client client = new Client(this);
	public static ProgressDialog createConnectionDialog;
	public static boolean isCreateConnectionSuccess = false;
	public static boolean isSearchedDevice = false;
	
	private MyDialog myDialog;
	private MyPopWindow myPopWindow;
	private static String TAG = MainActivity.class.getName();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int width = displaymetrics.widthPixels;
		int height = displaymetrics.heightPixels;
		Log.w(TAG, "onCreate");
		LoonToothApplication.setScreenWidth(width);
		LoonToothApplication.setScreenHeight(height);

		myDialog = new MyDialog(this);
		myPopWindow = new MyPopWindow(this);
		
		deviceSearchedPopWindow = myPopWindow.createDeviceSearchedPopWindow();
		
		isCreateConnectionSuccess = false;
		isSearchedDevice = false;

		TabHost tabHost = (TabHost) this.findViewById(R.id.tabhost);
		tabHost.setup();
		tabHost.setup(getLocalActivityManager());

		TabSpec tabPhoto = tabHost.newTabSpec("tab_photo");
		tabPhoto.setIndicator(tabIndicator("照片", R.drawable.photo_selector));
		tabPhoto.setContent(new Intent(this, PhotoActivity.class));
		tabHost.addTab(tabPhoto);

		TabSpec tabAudio = tabHost.newTabSpec("tab_audio");
		tabAudio.setIndicator(tabIndicator("音乐", R.drawable.audio_selector));
		tabAudio.setContent(new Intent(this, AudioActivity.class));
		tabHost.addTab(tabAudio);

		TabSpec tabVideo = tabHost.newTabSpec("tab_video");
		tabVideo.setIndicator(tabIndicator("视频", R.drawable.video_selector));
		tabVideo.setContent(new Intent(this, VideoActivity.class));
		tabHost.addTab(tabVideo);

		TabSpec tabBrowse = tabHost.newTabSpec("tab_browse");
		tabBrowse.setIndicator(tabIndicator("文件", R.drawable.browse_selector));
		tabBrowse.setContent(new Intent(this, BrowseActivity.class));
		tabHost.addTab(tabBrowse);

		TabSpec tabHistory = tabHost.newTabSpec("tab_inbox");
		tabHistory.setIndicator(tabIndicator("收件箱", R.drawable.inbox_selector));
		tabHistory.setContent(new Intent(this, InBoxActivity.class));
		tabHost.addTab(tabHistory);

	}

	public void onStart() {
		super.onStart();
		ActionBar bar = this.getActionBar();
		bar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP,
				ActionBar.NAVIGATION_MODE_LIST);
	}

	public void onDestory() {
		super.onDestroy();
		server.onStop(this);
		client.onStop(this);
		Log.w("MainActivity", "onDestroy");
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			Intent intent = new Intent(MainActivity.this, MainActivity.class);
			startActivity(intent);
		} else if (item.getItemId() == R.id.create_connection) {
			MyTools.logThreadSignature("MainActivity");
			if (!isCreateConnectionSuccess) {
				server.onStart(this);
				createConnectionDialog = ProgressDialog.show(this, "",
						"正在创建连接中...", true);
			} else {
				DisplayToast("连接已创建成功。");
			}
		} else if (item.getItemId() == R.id.search_join) {
			client.onStart(this);

			View view = View.inflate(this, R.layout.activity_main, null);
			deviceSearchedPopWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

		} else if (item.getItemId() == R.id.overflow) {
			myDialog.createMenuDialog();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.w("onCreateOptionsMenu", "准备菜单");
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public View tabIndicator(String label, int drawableId) {
		View view = View.inflate(this, R.layout.tab_indicator, null);
		TextView title = (TextView) view.findViewById(R.id.title);
		title.setText(label);
		ImageView icon = (ImageView) view.findViewById(R.id.icon);
		icon.setBackgroundResource(drawableId);
		return view;
	}

	public void DisplayToast(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}
}