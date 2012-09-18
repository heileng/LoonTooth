package net.mindlee.loontooth.gui;

import android.app.Application;
import android.util.Log;

/**
 * 继承自Application，用于存放全局变量
 * @author 李伟
 *
 */
public class LoonToothApplication extends Application{
	private static final String TAG = LoonToothApplication.class.getSimpleName();
	private static int SCREEN_WIDTH;
	private static int SCREEN_HEIGHT;
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "onCreate");
	}
	
	public void onTerminate() {
		super.onTerminate();
		Log.i(TAG, "onTerminate");
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
}
