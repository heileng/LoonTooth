package net.mindlee.loontooth.bluetooth;

import java.io.File;
import java.io.Serializable;

import android.net.Uri;

/**
 * 用于传输的数据类
 * @author GuoDong
 *
 */
public class TransmitBean implements Serializable{

	private String msg = "";
	private File data = null;
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public String getMsg() {
		return this.msg;
	}
	
	public void setData(String uri) {
		data = new File(uri);
	}
	
	public File getData() {
		return data;
	}
	
	
}
