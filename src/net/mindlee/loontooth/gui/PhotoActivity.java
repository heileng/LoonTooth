package net.mindlee.loontooth.gui;

import net.mindlee.loontooth.R;
import net.mindlee.loontooth.adapter.PhotoAdapter;
import net.mindlee.loontooth.bluetooth.BluetoothTools;
import net.mindlee.loontooth.bluetooth.TransmitBean;
import net.mindlee.loontooth.util.Photo;
import net.mindlee.loontooth.util.PopWindow;
import net.mindlee.loontooth.util.Video;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;

public class PhotoActivity extends Activity {
	private static GridView photoGridView;
	private int focusPhotoListItem;
	private PopupWindow downMenuPopWindow;
	private Photo photo;
	private PopWindow popWindow;
	
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo);
		photoGridView = (GridView) findViewById(R.id.photoGridView);
		PhotoAdapter adapter = new PhotoAdapter(this);
		photoGridView.setAdapter(adapter);
		photo = new Photo(this, adapter);
		
		photoGridView
        .setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                downMenuPopWindow.showAsDropDown(view,
                        view.getWidth() / 2, -view.getHeight() / 2);
                focusPhotoListItem = position;
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
							photo.playPhoto(position);
						} else if (position == 2) {
							downMenuPopWindow.dismiss();
							photo.openDetailsDialog(focusPhotoListItem).show();
							Log.d("点击位置3", "属性");
						}
					}
				});
	}

}