package net.mindlee.loontooth.bluetooth;

import java.io.Serializable;

import net.mindlee.loontooth.gui.LoonToothApplication;
import net.mindlee.loontooth.util.MyTools;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

/**
 * 服务端，后台服务（service
 * 
 * @author 李伟
 * 
 */
public class ServerService extends Service {
	private static final String TAG = ServerService.class.getSimpleName();

	// 蓝牙适配器
	private final BluetoothAdapter bluetoothAdapter = BluetoothAdapter
			.getDefaultAdapter();

	Context service = this;
	// 蓝牙通讯线程
	private ConnectedThread connectedThread;

	// 控制信息广播接收器
	private BroadcastReceiver controlReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (BluetoothTools.ACTION_STOP_SERVICE.equals(action)) {
				// 停止后台服务
				if (connectedThread != null) {
					connectedThread.isRun = false;
				}
				stopSelf();

			} else if (BluetoothTools.ACTION_DATA_TO_SERVICE.equals(action)) {
				// 发送数据
				Object data = intent.getSerializableExtra(BluetoothTools.DATA);
				if (connectedThread != null) {
					connectedThread.writeObject(data);
				}

			}
		}
	};

	// 接收其他线程消息的Handler
	private Handler serviceHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case BluetoothTools.MESSAGE_CONNECT_SUCCESS:
				Toast.makeText(service, "建立连接成功", Toast.LENGTH_SHORT).show();

				// 连接成功
				// 开启通讯线程
				connectedThread = new ConnectedThread(serviceHandler,
						(BluetoothSocket) msg.obj);
				connectedThread.start();

				// 发送连接成功消息
				Intent connSuccIntent = new Intent(
						BluetoothTools.ACTION_CONNECT_SUCCESS);
				sendBroadcast(connSuccIntent);
				break;

			case BluetoothTools.MESSAGE_CONNECT_ERROR:
				// 连接错误
				// 发送连接错误广播
				Intent errorIntent = new Intent(
						BluetoothTools.ACTION_CONNECT_ERROR);
				sendBroadcast(errorIntent);
				break;

			case BluetoothTools.MESSAGE_READ_OBJECT:
				// 读取到数据
				// 发送数据广播（包含数据对象）
				Intent dataIntent = new Intent(
						BluetoothTools.ACTION_DATA_TO_GAME);
				dataIntent
						.putExtra(BluetoothTools.DATA, (Serializable) msg.obj);
				sendBroadcast(dataIntent);

				break;
			}

			super.handleMessage(msg);
		}

	};

	/**
	 * 获取通讯线程
	 * 
	 * @return
	 */
	public ConnectedThread getBluetoothCommunThread() {
		return connectedThread;
	}

	public void onCreate() {
		Log.w(TAG, "onCreate");
		// ControlReceiver的IntentFilter
		IntentFilter controlFilter = new IntentFilter();
		controlFilter.addAction(BluetoothTools.ACTION_START_SERVER);
		controlFilter.addAction(BluetoothTools.ACTION_STOP_SERVICE);
		controlFilter.addAction(BluetoothTools.ACTION_DATA_TO_SERVICE);
		// 注册BroadcastReceiver
		registerReceiver(controlReceiver, controlFilter);

		Log.w("之前，蓝牙是否打开", "" + bluetoothAdapter.isEnabled());
		Log.w("之前状态 ", "" + bluetoothAdapter.getState());

		bluetoothAdapter.enable(); // 打开蓝牙
		if (!bluetoothAdapter.isDiscovering()) {
			// 开启蓝牙发现功能（300秒）
			Intent discoveryIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoveryIntent.putExtra(
					BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
			discoveryIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(discoveryIntent);
			LoonToothApplication.setConnectConnectStartTime(System.currentTimeMillis());
		}
		long start = System.currentTimeMillis();
		while (!bluetoothAdapter.isDiscovering()) {
			MyTools.LogCurrentTime();
			if (System.currentTimeMillis() - start >= 5000) {
				break;
			}
		}

		Log.w("之后，蓝牙是否打开", "" + bluetoothAdapter.isEnabled());
		Log.w("之后, 状态", "" + bluetoothAdapter.getState());

		// 开启后台连接线程
		new ServerConnectThread(serviceHandler).start();

		Log.w(TAG + "服务端创建成功", "onCreate");

		Intent createConnectionIntent = new Intent(
				BluetoothTools.ACTION_CREATE_CONNECTION_SUCCESS);
		sendBroadcast(createConnectionIntent);

		super.onCreate();
	}

	public void onDestroy() {
		Log.w(TAG, "onDestroy");
		if (connectedThread != null) {
			connectedThread.isRun = false;
		}
		unregisterReceiver(controlReceiver);
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

}
