package net.mindlee.loontooth.gui;

import android.app.Application;
import android.util.Log;

/**
 * 继承自Application，用于存放全局变量
 * 
 * @author 李伟
 * 
 */
public class LoonToothApplication extends Application {
	private static final String TAG = LoonToothApplication.class
			.getSimpleName();
	private static int SCREEN_WIDTH;
	private static int SCREEN_HEIGHT;
	private static String selectedDeviceAddress = null;
	private static long CreateConnectStartTime = 0;

	private static boolean ConnectSuccess = false;
	private static String IS_SERVER_OR_CLIENT = null;
	public static final String IS_SERVER = "isServer";
	public static final String IS_CLIENT = "isClient";

	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "onCreate");
	}

	public void onTerminate() {
		super.onTerminate();
		Log.i(TAG, "onTerminate");
	}

	public static void setIsServer() {
		IS_SERVER_OR_CLIENT = IS_SERVER;
	}

	public static void setIsClient() {
		IS_SERVER_OR_CLIENT = IS_CLIENT;
	}

	public static String getIsServerOrClient() {
		return IS_SERVER_OR_CLIENT;

	}

	public static void setConnectConnectStartTime(long time) {
		CreateConnectStartTime = time;
	}

	public static long getConnectConnectStartTime() {
		return CreateConnectStartTime;
	}

	public static void setSelectedDeviceAddress(String address) {
		selectedDeviceAddress = address;
	}

	public static String getSelectedDeviceAddress() {
		return selectedDeviceAddress;
	}

	public static void setScreenWidth(int width) {
		SCREEN_WIDTH = width;
	}

	public static void setScreenHeight(int height) {
		SCREEN_HEIGHT = height;
	}

	public static int getScreenWidth() {
		return SCREEN_WIDTH;
	}

	public static int getScreenHeight() {
		return SCREEN_HEIGHT;
	}
	
	public static void setConnectSuccess() {
		ConnectSuccess = true;
	}
	
	public static boolean isConnectSuccess() {
		return ConnectSuccess;
	}

}
