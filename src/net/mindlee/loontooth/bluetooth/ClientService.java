package net.mindlee.loontooth.bluetooth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.mindlee.loontooth.util.MyPopWindow;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
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
 * 客户端的后台服务, service
 * 
 * @author 李伟
 * 
 */
public class ClientService extends Service {
	private Context service = this;
	// 搜索到的远程设备集合
	private List<BluetoothDevice> discoveredDevices = new ArrayList<BluetoothDevice>();
	// 蓝牙适配器
	private final BluetoothAdapter bluetoothAdapter = BluetoothAdapter
			.getDefaultAdapter();

	// 蓝牙通讯线程
	private ConnectedThread communThread;

	// 控制信息广播的接收器
	private BroadcastReceiver controlReceiver = new BroadcastReceiver() {

		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (BluetoothTools.ACTION_START_DISCOVERY.equals(action)) {
				// 开始搜索
				MyPopWindow.getDeviceSearchAdapter().clearDevice();
				discoveredDevices.clear(); // 清空存放设备的集合
				bluetoothAdapter.enable(); // 打开蓝牙
				bluetoothAdapter.startDiscovery(); // 开始搜索

			} else if (BluetoothTools.ACTION_SELECTED_DEVICE.equals(action)) {
				// 选择了连接的服务器设备
				BluetoothDevice device = (BluetoothDevice) intent.getExtras()
						.get(BluetoothTools.DEVICE);

				// 开启设备连接线程
				new ClientConnectThread(handler, device).start();

			} else if (BluetoothTools.ACTION_STOP_SERVICE.equals(action)) {
				// 停止后台服务
				if (communThread != null) {
					communThread.isRun = false;
				}
				stopSelf();

			} else if (BluetoothTools.ACTION_DATA_TO_SERVICE.equals(action)) {
				// 获取数据
				Object data = intent.getSerializableExtra(BluetoothTools.DATA);
				if (communThread != null) {
					communThread.writeObject(data);
				}
			}
		}
	};

	// 蓝牙搜索广播的接收器
	private BroadcastReceiver discoveryReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// 获取广播的Action
			String action = intent.getAction();

			if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
				Toast.makeText(service, "开始搜索", Toast.LENGTH_SHORT).show();
				// 开始搜索
			} else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// 发现远程蓝牙设备
				// 获取设备
				BluetoothDevice bluetoothDevice = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				discoveredDevices.add(bluetoothDevice);

				// 发送发现设备广播
				Intent deviceListIntent = new Intent(
						BluetoothTools.ACTION_FOUND_DEVICE);
				deviceListIntent.putExtra(BluetoothTools.DEVICE,
						bluetoothDevice);
				sendBroadcast(deviceListIntent);

			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
					.equals(action)) {
				Toast.makeText(service, "搜索结束", Toast.LENGTH_SHORT).show();
				// 搜索结束
				if (discoveredDevices.isEmpty()) {
					// 若未找到设备，则发动未发现设备广播
					Intent foundIntent = new Intent(
							BluetoothTools.ACTION_NOT_FOUND_SERVER);
					sendBroadcast(foundIntent);
				}
			}
		}
	};

	// 接收其他线程消息的Handler
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			// 处理消息
			switch (msg.what) {
			case BluetoothTools.MESSAGE_CONNECT_ERROR:
				// 连接错误
				// 发送连接错误广播
				Intent errorIntent = new Intent(
						BluetoothTools.ACTION_CONNECT_ERROR);
				sendBroadcast(errorIntent);
				break;
			case BluetoothTools.MESSAGE_CONNECT_SUCCESS:
				// 连接成功

				// 开启通讯线程
				communThread = new ConnectedThread(handler,
						(BluetoothSocket) msg.obj);
				communThread.start();
				Log.w("客户端连接成功", "ClientService");
				// 发送连接成功广播
				Intent succIntent = new Intent(
						BluetoothTools.ACTION_CONNECT_SUCCESS);
				sendBroadcast(succIntent);
				break;
			case BluetoothTools.MESSAGE_READ_OBJECT:
				Log.w("客户端正在读取数据", "ClientService");
				// 读取到对象
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
		return communThread;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	/**
	 * Service创建时的回调函数
	 */
	@Override
	public void onCreate() {
		Log.w("ClientService", "onCreate");
		// discoveryReceiver的IntentFilter
		IntentFilter discoveryFilter = new IntentFilter();
		discoveryFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		discoveryFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		discoveryFilter.addAction(BluetoothDevice.ACTION_FOUND);

		// controlReceiver的IntentFilter
		IntentFilter controlFilter = new IntentFilter();
		controlFilter.addAction(BluetoothTools.ACTION_START_DISCOVERY);
		controlFilter.addAction(BluetoothTools.ACTION_SELECTED_DEVICE);
		controlFilter.addAction(BluetoothTools.ACTION_STOP_SERVICE);
		controlFilter.addAction(BluetoothTools.ACTION_DATA_TO_SERVICE);

		// 注册BroadcastReceiver
		registerReceiver(discoveryReceiver, discoveryFilter);
		registerReceiver(controlReceiver, controlFilter);
		super.onCreate();
	}

	/**
	 * Service销毁时的回调函数
	 */
	@Override
	public void onDestroy() {
		if (communThread != null) {
			communThread.isRun = false;
		}
		// 解除绑定
		unregisterReceiver(discoveryReceiver);
		super.onDestroy();
	}

}
