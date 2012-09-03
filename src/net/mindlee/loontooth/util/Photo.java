package net.mindlee.loontooth.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.mindlee.loontooth.R;
import net.mindlee.loontooth.adapter.PhotoAdapter;
import net.mindlee.loontooth.adapter.PhotoAdapter.PhotoInfo;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

public class Photo {

    private Context context;
    private PhotoAdapter photoAdapter;
    private List<PhotoInfo> photoList = new ArrayList<PhotoInfo>();

    public Photo(Context context, PhotoAdapter photoAdapter) {
        this.context = context;
        this.photoAdapter = photoAdapter;
        this.photoList = photoAdapter.getPhotoList();
    }

    public void playPhoto(int position) {
        String mediaUri = photoList.get(position).filePath;
        String mediaType = photoList.get(position).mimeType;
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
