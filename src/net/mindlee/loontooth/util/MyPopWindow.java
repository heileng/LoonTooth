package net.mindlee.loontooth.util;

import net.mindlee.loontooth.R;
import net.mindlee.loontooth.adapter.DeviceAdapter;
import net.mindlee.loontooth.adapter.DownMenuAdapter;
import net.mindlee.loontooth.bluetooth.BluetoothTools;
import net.mindlee.loontooth.gui.LoonToothApplication;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;

/**
 * 弹出各种应用用到的PopupWindow
 * 
 * @author 李伟
 * 
 */
public class MyPopWindow {
	private ListView downMenuListView;
	private PopupWindow downMenuPopWindow;
	private Context context;
	private ListView deviceSearchedListView;
	private static DeviceAdapter deviceSearchedAdapter;
	private PopupWindow deviceSearchedPopWindow;

	public MyPopWindow(Context context) {
		this.context = context;
	}

	/**
	 * 创建下拉菜单弹出窗口
	 * 
	 * @return 返回下拉菜单弹出窗口
	 */
	public PopupWindow createDownMenu() {
		View downMenuLayout = View.inflate(context, R.layout.downmenu, null);
		downMenuPopWindow = new PopupWindow(downMenuLayout,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		downMenuPopWindow.setTouchable(true);
		downMenuPopWindow.setOutsideTouchable(true);
		downMenuPopWindow.setWidth(LoonToothApplication.getScreenWidth() / 3);
		downMenuPopWindow.setBackgroundDrawable(new ColorDrawable(0));

		downMenuListView = (ListView) downMenuLayout
				.findViewById(R.id.downmenu_listView);
		DownMenuAdapter downMenuAdapter = new DownMenuAdapter(context);
		downMenuListView.setAdapter(downMenuAdapter);
		return downMenuPopWindow;
	}

	/**
	 * 返回下拉菜单中的ListView
	 * 
	 * @return ListView
	 */
	public ListView getDownMenuListView() {
		return downMenuListView;
	}

	/**
	 * 弹出，客户端搜索到的蓝牙设备，以类表形式显示
	 */
	public PopupWindow createDeviceSearchedPopWindow() {
		View deviceLayout = View.inflate(context, R.layout.device_searched,
				null);
		deviceSearchedListView = (ListView) deviceLayout
				.findViewById(R.id.client_device_listView);
		ImageButton closeButton = (ImageButton) deviceLayout
				.findViewById(R.id.client_close_button);

		deviceSearchedAdapter = new DeviceAdapter(context);
		deviceSearchedListView.setAdapter(deviceSearchedAdapter);

		deviceSearchedPopWindow = new PopupWindow(deviceLayout,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		deviceSearchedPopWindow
				.setWidth(LoonToothApplication.getScreenWidth() * 3 / 4);
		deviceSearchedPopWindow.setHeight(LoonToothApplication
				.getScreenHeight() * 3 / 5);
		// clientPopWindow.setBackgroundDrawable(new ColorDrawable(0));

		closeButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				deviceSearchedPopWindow.dismiss();
			}
		});

		deviceSearchedListView
				.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						deviceSearchedPopWindow.dismiss();
						Intent selectDeviceIntent = new Intent(
								BluetoothTools.ACTION_SELECTED_DEVICE);
						Log.w("点击位置position=", "" + position);
						Log.w("设备名：",
								""
										+ deviceSearchedAdapter.getItem(
												position).getName());
						Log.w("地址", deviceSearchedAdapter.getItem(position)
								.getAddress());

						selectDeviceIntent.putExtra(BluetoothTools.DEVICE,
								deviceSearchedAdapter.getItem(position));
						context.sendBroadcast(selectDeviceIntent);
					}
				});

		return deviceSearchedPopWindow;
	}

	/**
	 * 返回搜索到设备列表的Adapter
	 * 
	 * @return AdapterF
	 */
	public static DeviceAdapter getDeviceSearchAdapter() {
		return deviceSearchedAdapter;
	}

	/**
	 * 返回搜索到设备列表的ListView
	 * 
	 * @return ListView
	 */
	public ListView getDeviceSearchedListView() {
		return deviceSearchedListView;
	}
}
