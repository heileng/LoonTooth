package net.mindlee.loontooth.gui;

import java.util.Date;

import net.mindlee.loontooth.R;
import net.mindlee.loontooth.adapter.DeviceAdapter;
import net.mindlee.loontooth.bluetooth.BluetoothTools;
import net.mindlee.loontooth.bluetooth.Client;
import net.mindlee.loontooth.bluetooth.Server;
import net.mindlee.loontooth.util.Dialog;
import net.mindlee.loontooth.util.Tools;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class MainActivity extends TabActivity implements OnTabChangeListener {
	private TabSpec tabPhoto, tabAudio, tabVideo, tabBrowse, tabHistory;
	private TabHost tabHost;
	private PopupWindow clientPopWindow;
	public static ListView deviceListView;
	public static DeviceAdapter deviceAdapter;
	private Server server = new Server(this);
	private Client client = new Client(this);
	public static int SCREEN_WIDTH;
	public static int SCREEN_HEIGHT;
	public static ProgressDialog createConnectDialog;
	private Dialog dialog;
	private long mLastBackTime = 0;
	private long TIME_DIFF = 2 * 1000;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		dialog = new Dialog(this);

		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		SCREEN_WIDTH = displaymetrics.widthPixels;
		SCREEN_HEIGHT = displaymetrics.heightPixels;
		Log.v("SCREEN_WIDTH", "" + SCREEN_WIDTH);
		Log.v("SCREEN_HEIGHT", "" + SCREEN_HEIGHT);

		setTabHost();
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
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			long now = new Date().getTime();
			if (now - mLastBackTime < TIME_DIFF) {
				return super.onKeyDown(keyCode, event);
			} else {
				mLastBackTime = now;
				Toast.makeText(this, "再点一次将推出", 2000).show();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void onTabChanged(String tabId) {
		if (tabId.equals("tab_photo")) {
		//	DisplayToast("照片");
		} else if (tabId.equals("tab_audio")) {
		//	DisplayToast("音乐");
		} else if (tabId.equals("tab_video")) {
		///	DisplayToast("视频");
		} else if (tabId.equals("tab_browse")) {
		//	DisplayToast("文件");
		} else if (tabId.equals("tab_inbox")) {
		//	DisplayToast("收件箱");
		}
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			Intent intent = new Intent(MainActivity.this, MainActivity.class);
			startActivity(intent);
		} else if (item.getItemId() == R.id.create_connection) {
			Tools.logThreadSignature("MainActivity");
			server.onStart(this);
		//	createConnectDialog = ProgressDialog.show(this, "", "正在创建连接...",
			//		true);
		} else if (item.getItemId() == R.id.search_join) {
			client.onStart(this);

			DisplayToast("开始搜索");
			Intent startSearchIntent = new Intent(
					BluetoothTools.ACTION_START_DISCOVERY);
			sendBroadcast(startSearchIntent);
			View view = View.inflate(this, R.layout.activity_main, null);
			createClientPopWindow().showAtLocation(view, Gravity.CENTER, 0, 0);
			deviceAdapter = new DeviceAdapter(this);
			deviceListView.setAdapter(deviceAdapter);

		} else if (item.getItemId() == R.id.overflow) {
			dialog.createMenuDialog();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	private void setTabHost() {
		tabHost = this.getTabHost();

		tabPhoto = tabHost.newTabSpec("tab_photo");
		tabPhoto.setIndicator(tabIndicator("照片", R.drawable.photo_selector));
		tabPhoto.setContent(new Intent(this, PhotoActivity.class));
		tabHost.addTab(tabPhoto);

		tabAudio = tabHost.newTabSpec("tab_audio");
		tabAudio.setIndicator(tabIndicator("音乐", R.drawable.audio_selector));
		tabAudio.setContent(new Intent(this, AudioActivity.class));
		tabHost.addTab(tabAudio);

		tabVideo = tabHost.newTabSpec("tab_video");
		tabVideo.setIndicator(tabIndicator("视频", R.drawable.video_selector));
		tabVideo.setContent(new Intent(this, VideoActivity.class));
		tabHost.addTab(tabVideo);

		tabBrowse = tabHost.newTabSpec("tab_browse");
		tabBrowse.setIndicator(tabIndicator("文件", R.drawable.browse_selector));
		tabBrowse.setContent(new Intent(this, BrowseActivity.class));
		tabHost.addTab(tabBrowse);

		tabHistory = tabHost.newTabSpec("tab_inbox");
		tabHistory.setIndicator(tabIndicator("收件箱", R.drawable.inbox_selector));
		tabHistory.setContent(new Intent(this, InBoxActivity.class));
		tabHost.addTab(tabHistory);

		tabHost.setCurrentTab(0);// 启动时显示第一个标签页
		tabHost.setOnTabChangedListener(this);

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
		Toast.makeText(this, str, 100).show();
	}

	public PopupWindow createClientPopWindow() {
		View deviceLayout = View.inflate(this, R.layout.client, null);
		deviceListView = (ListView) deviceLayout
				.findViewById(R.id.client_device_listView);
		ImageButton closeButton = (ImageButton) deviceLayout
				.findViewById(R.id.client_close_button);
		ProgressBar progressBar = (ProgressBar) deviceLayout
				.findViewById(R.id.client_search_device_progressBar);

		clientPopWindow = new PopupWindow(deviceLayout,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		clientPopWindow.setWidth(MainActivity.SCREEN_WIDTH * 3 / 4);
		clientPopWindow.setHeight(MainActivity.SCREEN_HEIGHT * 3 / 5);
		// clientPopWindow.setBackgroundDrawable(new ColorDrawable(0));

		closeButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				clientPopWindow.dismiss();
			}
		});

		deviceListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						if (position == 0) {
							Log.d("点击位置0", "设备");
							DisplayToast("选择第一个设备");

							Intent selectDeviceIntent = new Intent(
									BluetoothTools.ACTION_SELECTED_DEVICE);
							selectDeviceIntent.putExtra(BluetoothTools.DEVICE,
									client.getDeviceList().get(0));
							sendBroadcast(selectDeviceIntent);
						} else if (position == 1) {
							Log.d("点击位置1", "设备");
						} else if (position == 2) {
							Log.d("点击位置3", "设备");
						}
					}
				});

		return clientPopWindow;
	}
}
