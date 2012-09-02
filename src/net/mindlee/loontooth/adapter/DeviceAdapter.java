package net.mindlee.loontooth.adapter;

import java.util.ArrayList;
import java.util.List;

import net.mindlee.loontooth.R;
import net.mindlee.loontooth.bluetooth.Client;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//Adapter
public class DeviceAdapter extends BaseAdapter {
	private Context context;
	private Client client;
//	private List<BluetoothDevice> deviceList = new ArrayList<BluetoothDevice>();
	private List<String> deviceList = new ArrayList<String>();


	public DeviceAdapter(Context context) {
		this.context = context;
		//this.deviceList = client.getDeviceList();
		deviceList.add("haha");
	}

	public int getCount() {
		return deviceList.size();
	}

	@Override
	public Object getItem(int position) {
		return deviceList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		holder = new ViewHolder();
		convertView = View.inflate(context, R.layout.client_device_item, null);
		holder.avatar = (ImageView) convertView
				.findViewById(R.id.device_avatar);
		holder.deviceName = (TextView) convertView
				.findViewById(R.id.device_name);
		holder.connectState = (TextView) convertView
				.findViewById(R.id.device_connect_state);
		holder.connectStateIcon = (ImageView) convertView
				.findViewById(R.id.device_connect_state_icon);
		holder.deviceName.setText(deviceList.get(position));
		//	holder.deviceName.setText(deviceList.get(position).getName());
		return convertView;
	}

	class ViewHolder {
		ImageView avatar;
		TextView deviceName;
		TextView connectState;
		ImageView connectStateIcon;
	}

}
