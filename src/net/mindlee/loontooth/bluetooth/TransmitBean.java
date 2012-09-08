package net.mindlee.loontooth.bluetooth;

import java.io.Serializable;

/**
 * 用于传输消息的文件。
 * @author 李伟
 *
 */
public class TransmitBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3682384326613181880L;
	private String msg = "";

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return this.msg;
	}
}
