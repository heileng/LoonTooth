package net.mindlee.loontooth.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.mindlee.loontooth.R;
import net.mindlee.loontooth.adapter.VideoAdapter;
import net.mindlee.loontooth.adapter.VideoAdapter.VideoInfo;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

public class Video {
	private Context context;
	private VideoAdapter videoAdapter;
	private List<VideoInfo> videoList = new ArrayList<VideoInfo>();

	public Video(Context context, VideoAdapter videoAdapter) {
		this.context = context;
		this.videoAdapter = videoAdapter;
		this.videoList = videoAdapter.getVideoList();
	}

	public void playVideo(int position) {
		String mediaUri = videoList.get(position).filePath;
		String mediaType = videoList.get(position).mimeType;
		Uri data = Uri.fromFile(new File(mediaUri));
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(data, mediaType);
		context.startActivity(intent);
	}
	
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
		String type = videoList.get(position).mimeType + "\n";
		type = "\t格式：" + type.substring(type.indexOf('/') + 1, type.length());
		
		String filePath = "\t位置：" + videoList.get(position).filePath;
		filePath = filePath.substring(0, filePath.lastIndexOf('/')) + '\n';
		String size = "\t大小："
				+ Tools.sizeFormat(videoList.get(position).size) + "\n";
		String date_modified = "\t修改日期："
				+ Tools.mSecondsToDate(videoList.get(position).dateModified);
		String str = "" + name + type + filePath + size + date_modified;
		System.out.print(str);
		builder.setMessage(str); // 对话框显示内容

		AlertDialog propertyDialog = builder.create();
		return propertyDialog;
	}
}
