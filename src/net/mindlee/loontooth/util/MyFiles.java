package net.mindlee.loontooth.util;

import java.io.File;

import net.mindlee.loontooth.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

public class MyFiles {
	private Context context;
	public static final String VIDEO = "video";
	public static final String AUDIO = "audio";
	public static final String IMAGE = "image";

	public MyFiles(Context context) {
		this.context = context;
	}

	/**
	 * 打开一个包含详细信息的对话框
	 * 
	 * @param file
	 *            文件
	 */
	public void openDetailsDialog(File file) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("属性") // 标题
				.setIcon(R.drawable.ic_launcher) // icon
				.setCancelable(true) // 响应back按钮
				// 设置按钮
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});

		String name = "\t名称：" + file.getName() + "\n";
		String type = "\t类型：" + "文件" + "\n";

		String filePath = "\t位置：" + file.getAbsolutePath() + "\n";
		String size = null;
		if (file.isFile()) {
			size = "\t大小："
					+ MyTools.sizeFormat(String.valueOf(file.length())) + "\n";
		} else if (file.isDirectory()) {
			type = "\t类型：" + "文件夹" + "\n";
			size = "\t包含：" + getFloderInfo(file) + "\n";
		}
		String date_modified = "\t修改日期："
				+ MyTools
						.secondsToDate(String.valueOf(file.lastModified() / 1000));
		String str = "" + name + type + filePath + size + date_modified;
		System.out.print(str);
		builder.setMessage(str); // 对话框显示内容

		AlertDialog detailsDialog = builder.create();
		detailsDialog.show();
	}

	/**
	 * 打开文件
	 * 
	 * @param f
	 *            文件
	 */
	public void openFile(File file) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);

		String type = getMIMEType(file);

		intent.setDataAndType(Uri.fromFile(file), type);
		context.startActivity(intent);
	}

	/**
	 * 获取文件类型
	 * 
	 * @param f
	 *            文件
	 * @return 文件类型
	 */
	public String getMIMEType(File f) {
		String type = "";
		String fName = f.getName();

		String end = fName
				.substring(fName.lastIndexOf(".") + 1, fName.length())
				.toLowerCase();

		if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
				|| end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
			type = AUDIO;
		} else if (end.equals("3gp") || end.equals("mp4") || end.equals("rmvb")
				|| end.equals("mkv") || end.equals("avi") || end.equals("wmv")) {
			type = VIDEO;
		} else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg") || end.equals("bmp")) {
			type = IMAGE;
		} else {
			type = "*";
		}

		type += "/*";
		return type;
	}
	
	public String getFloderInfo(File file) {
		assert file.isDirectory() : file.isDirectory();
		File[] filesArray = file.listFiles();
		int fileCount = 0;
		int floderCount = 0;
		for (File f: filesArray) {
			if(f.isFile()) {
				fileCount++;
			} else if (f.isDirectory()) {
				floderCount++;
			}
		}
		String res = floderCount + "个文件夹和" + fileCount + "个文件";
		return res;
		
	}

	/**
	 * 调用系统的蓝牙页面，选择设备后，发送文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @param type
	 *            文件类型
	 */
	public void sendFile(String filePath, String type) {

		// 调用系统发送页面
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_SEND);
		intent.setType(type);
		intent.setClassName("com.android.bluetooth",
				"com.android.bluetooth.opp.BluetoothOppLauncherActivity");
		intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));
		context.startActivity(intent);

		/*
		 * System.out.println(filePath);
		 * System.out.println(MainActivity.deviceAddress);
		 * System.out.println(Uri.fromFile(new File(filePath)).toString());
		 * 
		 * ContentValues values = new ContentValues();
		 * values.put(BluetoothShare.URI, Uri.fromFile(new
		 * File(filePath)).toString()); values.put(BluetoothShare.DESTINATION,
		 * MainActivity.deviceAddress); values.put(BluetoothShare.DIRECTION,
		 * BluetoothShare.DIRECTION_OUTBOUND); Long ts =
		 * System.currentTimeMillis(); values.put(BluetoothShare.TIMESTAMP, ts);
		 * context.getContentResolver().insert(BluetoothShare.CONTENT_URI,
		 * values);
		 */
	}

}
