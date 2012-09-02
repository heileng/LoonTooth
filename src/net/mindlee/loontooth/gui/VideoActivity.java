package net.mindlee.loontooth.gui;

import net.mindlee.loontooth.R;
import net.mindlee.loontooth.adapter.VideoAdapter;
import net.mindlee.loontooth.bluetooth.BluetoothTools;
import net.mindlee.loontooth.bluetooth.TransmitBean;
import net.mindlee.loontooth.util.PopWindow;
import net.mindlee.loontooth.util.Video;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

public class VideoActivity extends Activity {
	private ListView videoListView;
	private int focusVideoListItem;
	private PopupWindow downMenuPopWindow;
	private Video video;
	private PopWindow popWindow;

	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);
		videoListView = (ListView) findViewById(R.id.video_listView);
		VideoAdapter adapter = new VideoAdapter(this);
		video = new Video(this, adapter);
		videoListView.setAdapter(adapter);
		videoListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						downMenuPopWindow.showAsDropDown(view,
								view.getWidth() / 2, -view.getHeight() / 2);
						focusVideoListItem = position;
					}
				});

		popWindow = new PopWindow(this);
		downMenuPopWindow = popWindow.createDownMenu();

		popWindow.getDownMenuListView().setOnItemClickListener(
				new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Log.v("点击下拉菜单", "");
						if (position == 0) {
							Log.d("点击位置0", "传输");
							// 发送消息
							TransmitBean data = new TransmitBean();
							String name = BluetoothTools.getBTAdapter()
									.getName();
							Log.w("name", "");
							data.setMsg("你好");
							Intent sendDataIntent = new Intent(
									BluetoothTools.ACTION_DATA_TO_SERVICE);
							sendDataIntent.putExtra(BluetoothTools.DATA, data);
							sendBroadcast(sendDataIntent);

						} else if (position == 1) {
							downMenuPopWindow.dismiss();
							video.playVideo(position);
						} else if (position == 2) {
							downMenuPopWindow.dismiss();
							video.openDetailsDialog(focusVideoListItem).show();
							Log.d("点击位置3", "属性");
						}
					}
				});
	}
}
