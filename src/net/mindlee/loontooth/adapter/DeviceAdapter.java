package net.mindlee.loontooth.adapter;

import java.util.ArrayList;
import java.util.List;

import net.mindlee.loontooth.R;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 用于显示搜索到的蓝牙设备ListView的Adapter
 * 
 * @author MindLee
 * 
 */
public class DeviceAdapter extends BaseAdapter {
	private Context context;
	private List<BluetoothDevice> deviceList = new ArrayList<BluetoothDevice>();

	public DeviceAdapter(Context context) {
		this.context = context;
	}

	public int getCount() {
		return deviceList.size();
	}

	public void addDevice(BluetoothDevice device) {
		deviceList.add(device);
	}

	public void clearDevice() {
		deviceList.clear();
	}

	public BluetoothDevice getItem(int position) {
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
		convertView = View.inflate(context, R.layout.device_searched_item, null);
		holder.avatar = (ImageView) convertView
				.findViewById(R.id.device_avatar);
		holder.deviceName = (TextView) convertView
				.findViewById(R.id.device_name);
		holder.deviceMacAddress = (TextView) convertView
				.findViewById(R.id.device_mac_address);
		holder.connectStateIcon = (ImageView) convertView
				.findViewById(R.id.device_connect_state_icon);
		holder.deviceName.setText(deviceList.get(position).getName());
		holder.deviceMacAddress.setText(deviceList.get(position).getAddress());
		return convertView;
	}

	class ViewHolder {
		ImageView avatar;
		TextView deviceName;
		TextView deviceMacAddress;
		ImageView connectStateIcon;
	}

}
