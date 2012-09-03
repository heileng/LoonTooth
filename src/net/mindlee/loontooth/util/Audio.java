package net.mindlee.loontooth.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.mindlee.loontooth.R;
import net.mindlee.loontooth.adapter.AudioAdapter;
import net.mindlee.loontooth.adapter.AudioAdapter.AudioInfo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

public class Audio {
	private Context context;
	private AudioAdapter audioAdapter;
	private List<AudioInfo> audioList = new ArrayList<AudioInfo>();
	public static final int SAND = 0;
	public static final int OPEN = 1;
	public static final int DETAILS = 2;

	public Audio(Context context , AudioAdapter audioAdapter) {
		this.context = context;
		this.audioAdapter = audioAdapter;
		this.audioList = audioAdapter.getAudioList();
	}

	public void playMusic(int position) {
		String mediaUri = audioList.get(position).filePath;
		String mediaType = audioList.get(position).mimeType;
		Uri data = Uri.fromFile(new File(mediaUri));
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(data, mediaType);
		context.startActivity(intent);
	}

	public Dialog openDetailsDialog(int position) {
		// 创建builder
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("属性") // 标题
				.setIcon(R.drawable.ic_launcher) // icon
				.setCancelable(true) // 响应back按钮
				// 设置按钮
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// Toast.makeText(context, "点击了确定按钮",
						// Toast.LENGTH_SHORT).show();
					}
				});
		String name = "\t名称：" + audioList.get(position).title + "\n";
		String type = audioList.get(position).mimeType + "\n";
		type = "\t格式：" + type.substring(type.indexOf('/') + 1, type.length());
		String filePath = "\t位置：" + audioList.get(position).filePath;
		filePath = filePath.substring(0, filePath.lastIndexOf('/')) + '\n';
		String size = "\t大小："
				+ Tools.sizeFormat(audioList.get(position).size) + "\n";
		String date_modified = "\t修改日期："
				+ Tools.secondsToDate(audioList.get(position).dateModified);
		String str = "" + name + type + filePath + size + date_modified;
		System.out.print(str);
		builder.setMessage(str); // 对话框显示内容

		AlertDialog propertyDialog = builder.create();
		return propertyDialog;
	}
}
