package net.mindlee.loontooth.bluetooth;

import java.util.Date;

import net.mindlee.loontooth.gui.MainActivity;
import net.mindlee.loontooth.gui.PhotoActivity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class Server {
	private MainActivity activity;
	public Server(Context context) {
		this.activity = (MainActivity) context;
	}
    //广播接收器
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            
            if (BluetoothTools.ACTION_DATA_TO_GAME.equals(action)) {
                //接收数据
                TransmitBean data = (TransmitBean)intent.getExtras().getSerializable(BluetoothTools.DATA);
                String msg = "from remote " + new Date().toLocaleString() + " :\r\n" + data.getMsg() + "\r\n";
                Log.w("服务器收到信息", msg);
                activity.DisplayToast("服务器收到消息" + msg);
            
            } else if (BluetoothTools.ACTION_CONNECT_SUCCESS.equals(action)) {
                //连接成功
                Log.w("服务器", "连接成功");
                activity.DisplayToast("服务器端连接成功");
            }
            
        }
    };
	
	public void onStart(Context context) {
		Log.w("Server", "onStart");
        Intent startService = new Intent(context, ServerService.class);
        context.startService(startService);
        
        //注册BoradcasrReceiver
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothTools.ACTION_DATA_TO_GAME);
        intentFilter.addAction(BluetoothTools.ACTION_CONNECT_SUCCESS);
        context.registerReceiver(broadcastReceiver, intentFilter);
        
	}
	
	public void onStop(Context context) {
		//关闭后台Service
		Intent startService = new Intent(BluetoothTools.ACTION_STOP_SERVICE);
		context.sendBroadcast(startService);
		context.unregisterReceiver(broadcastReceiver);	
	}
}
