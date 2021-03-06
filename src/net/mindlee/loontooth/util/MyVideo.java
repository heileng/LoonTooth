package net.mindlee.loontooth.util;

import java.io.File;
import java.util.List;

import net.mindlee.loontooth.R;
import net.mindlee.loontooth.adapter.VideoAdapter.VideoInfo;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

/**
 * 视频工具类，视频文件用到的各种操作
 * 
 * @author MindLee
 * 
 */
public class MyVideo {
	private Context context;
	private List<VideoInfo> videoList;

	public MyVideo(Context context, List<VideoInfo> videoList) {
		this.context = context;
		this.videoList = videoList;
	}

	/**
	 * 播放视频文件，播放videoList中的第position个视频
	 * 
	 * @param position
	 */
	public void playVideo(int position) {
		String mediaUri = videoList.get(position).filePath;
		String mediaType = videoList.get(position).mimeType;
		Uri data = Uri.fromFile(new File(mediaUri));
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(data, mediaType);
		context.startActivity(intent);
	}

	/**
	 * 弹出videoList中第position个视频的详细信息
	 * 
	 * @param position
	 * @return 弹出一个属性对话框
	 */
	public Dialog openDetailsDialog(int position) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("属性") // 标题
				.setIcon(R.drawable.ic_launcher) // icon
				.setCancelable(true) // 响应back按钮
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		String name = "\t名称：" + videoList.get(position).title + "\n";

		String filePath = "\t位置：" + videoList.get(position).filePath;
		String type = "\t类型: "
				+ filePath.substring(filePath.indexOf('.') + 1,
						filePath.length()) + "\n";
		filePath = filePath.substring(0, filePath.lastIndexOf('/')) + '\n';
		String size = "\t大小："
				+ MyTools.sizeFormat(videoList.get(position).size) + "\n";
		String date_modified = "\t修改日期："
				+ MyTools.secondsToDate(videoList.get(position).dateModified);
		String str = "" + name + type + filePath + size + date_modified;
		System.out.print(str);
		builder.setMessage(str); // 对话框显示内容

		AlertDialog propertyDialog = builder.create();
		return propertyDialog;
	}
}
