package net.mindlee.loontooth.bluetooth;

import java.io.IOException;

import net.mindlee.loontooth.util.Tools;

import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;

/**
 * 服务器连接线程，用于配对
 * 
 * @author 李伟
 */
public class ServerConnectThread extends Thread {

	private Handler serverServiceHandler; // 用于同Service通信的Handler
	private final BluetoothServerSocket serverSocket;

	/**
	 * 构造函数
	 * 
	 * @param handler
	 */
	public ServerConnectThread(Handler handler) {
		this.serverServiceHandler = handler;
		// Use a temporary object that is later assigned to mmServerSocket,
		// because mmServerSocket is final
		BluetoothServerSocket tmp = null;
		try {
			// MY_UUID is the app's UUID string, also used by the client code
			tmp = BluetoothTools.getBTAdapter()
					.listenUsingRfcommWithServiceRecord(BluetoothTools.SERVER,
							BluetoothTools.MY_UUID);
		} catch (IOException e) {
			e.printStackTrace();
		}
		serverSocket = tmp;
	}

	public void run() {
		Tools.logThreadSignature("服务端连接线程ServerConnectThread");
		BluetoothSocket socket = null;
		// Keep listening until exception occurs or a socket is returned
		while (true) {
			try {
				socket = serverSocket.accept();
			} catch (IOException e) {
				serverServiceHandler.obtainMessage(
						BluetoothTools.MESSAGE_CONNECT_ERROR).sendToTarget();
				break;
			}
			// If a connection was accepted
			if (socket != null) {
				serverServiceHandler.obtainMessage(
						BluetoothTools.MESSAGE_CONNECT_SUCCESS, socket)
						.sendToTarget();
				break;
			}
		}
	}

	/** Will cancel the listening socket, and cause the thread to finish */
	public void cancel() {
		try {
			serverSocket.close();
		} catch (IOException e) {
		}
	}

}
