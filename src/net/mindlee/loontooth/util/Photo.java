package net.mindlee.loontooth.util;

import java.io.File;
import java.util.List;

import net.mindlee.loontooth.R;
import net.mindlee.loontooth.adapter.PhotoAdapter.PhotoInfo;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

/**
 * 照片工具类，包括打开照片，开启照片属性对话框
 * @author 李伟
 *
 */
public class Photo {

    private Context context;
    private List<PhotoInfo> photoList;

    public Photo(Context context, List<PhotoInfo> photoList) {
        this.context = context;
        this.photoList = photoList;
    }

    /**
     * 打开photoList中的第position张照片
     * @param position photoList中的位置
     */
    public void playPhoto(int position) {
        String mediaUri = photoList.get(position).filePath;
        String mediaType = photoList.get(position).mimeType;
        Uri data = Uri.fromFile(new File(mediaUri));
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(data, mediaType);
        context.startActivity(intent);
    }
    
    /**
     * 弹出一个属性对话框，用于显示videoList中第position张照片详细信息
     * @param position videoList的item位置position
     * @return 返回一个“属性”对话框
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
        String name = "\t名称：" + photoList.get(position).title + "\n";
        String type = photoList.get(position).mimeType + "\n";
        type = "\t格式：" + type.substring(type.indexOf('/') + 1, type.length());
        
        String filePath = "\t位置：" + photoList.get(position).filePath;
        filePath = filePath.substring(0, filePath.lastIndexOf('/')) + '\n';
        String size = "\t大小："
                + Tools.sizeFormat(photoList.get(position).size) + "\n";
        String date_modified = "\t修改日期："
                + Tools.secondsToDate(photoList.get(position).dateModified);
        String str = "" + name + type + filePath + size + date_modified;
        System.out.print(str);
        builder.setMessage(str); // 对话框显示内容

        AlertDialog propertyDialog = builder.create();
        return propertyDialog;
    }
}
