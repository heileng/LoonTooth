package net.mindlee.loontooth.bluetooth;

import java.io.IOException;

import net.mindlee.loontooth.util.Tools;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;

/**
 * 客户端的连接线程，用于配对
 * 
 * @author 李伟
 * 
 */
public class ClientConnectThread extends Thread {

	private Handler clientServiceHandler; // 用于向客户端Service回传消息的handler
	private final BluetoothDevice serverDevice; // 服务器设备
	private final BluetoothSocket socket; // 通信Socket

	/**
	 * 构造函数
	 * 
	 * @param handler
	 * @param serverDevice
	 */
	public ClientConnectThread(Handler handler, BluetoothDevice serverDevice) {
		this.clientServiceHandler = handler;
		this.serverDevice = serverDevice;
		// Use a temporary object that is later assigned to mmSocket,
		// because mmSocket is final
		BluetoothSocket tmp = null;
		// Get a BluetoothSocket to connect with the given BluetoothDevice
		try {
			// MY_UUID is the app's UUID string, also used by the server code
			tmp = serverDevice
					.createRfcommSocketToServiceRecord(BluetoothTools.MY_UUID);
		} catch (IOException e) {
			e.printStackTrace();
		}
		socket = tmp;
	}

	public void run() {
		Tools.logThreadSignature("客户端连接线程ClientConnentThread");
		// Cancel discovery because it will slow down the connection
		BluetoothTools.getBTAdapter().cancelDiscovery();
		try {
			// Connect the device through the socket. This will block
			// until it succeeds or throws an exception
			socket.connect();
		} catch (Exception ex) {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// Send message : Connect error.
			clientServiceHandler.obtainMessage(
					BluetoothTools.MESSAGE_CONNECT_ERROR).sendToTarget();
			return;
		}
		// Send Message :Connect Success.
		clientServiceHandler.obtainMessage(
				BluetoothTools.MESSAGE_CONNECT_SUCCESS, socket).sendToTarget();
	}

	/* Will cancel an in-progress connection, and close the socket */
	public void cancel() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
