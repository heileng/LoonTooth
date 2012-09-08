package net.mindlee.loontooth.bluetooth;

import java.util.Date;

import net.mindlee.loontooth.gui.MainActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

/**
 * 服务器端管理
 * 
 * @author 李伟
 * 
 */
public class Server {
	private MainActivity activity;

	public Server(MainActivity context) {
		this.activity = context;
	}

	// 广播接收器
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {

			String action = intent.getAction();
			if (BluetoothTools.ACTION_DATA_TO_GAME.equals(action)) {
				// 接收数据
				TransmitBean data = (TransmitBean) intent.getExtras()
						.getSerializable(BluetoothTools.DATA);
				String msg = "请求接受数据" + data.getMsg() + "\n大小：" + data.getSize();
				Log.w( "服务器", msg);
				activity.DisplayToast( msg);

			} else if (BluetoothTools.ACTION_CONNECT_SUCCESS.equals(action)) {
				// 连接成功
				Log.w("服务器", "连接成功");
				activity.DisplayToast("服务端连接成功");

			} else if (BluetoothTools.ACTION_CREATE_CONNECTION_SUCCESS
					.equals(action)) {
				Log.w("服务器", "服务器创建连接成功");
				MainActivity.createConnectionDialog.dismiss();
				MainActivity.isCreateConnectionSuccess = true;

				activity.DisplayToast("创建连接成功，等待朋友加入。");
			}
		}
	};

	public void onStart(Context context) {
		Log.w("Server", "onStart");
		Intent startService = new Intent(context, ServerService.class);
		context.startService(startService);

		// 注册BoradcasrReceiver
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothTools.ACTION_DATA_TO_GAME);
		intentFilter.addAction(BluetoothTools.ACTION_CONNECT_SUCCESS);
		intentFilter.addAction(BluetoothTools.ACTION_CREATE_CONNECTION_SUCCESS);
		context.registerReceiver(broadcastReceiver, intentFilter);
	}

	public void onStop(Context context) {
		// 关闭后台Service
		Intent startService = new Intent(BluetoothTools.ACTION_STOP_SERVICE);
		context.sendBroadcast(startService);
		context.unregisterReceiver(broadcastReceiver);
	}
}
