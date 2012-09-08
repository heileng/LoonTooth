package net.mindlee.loontooth.adapter;

import java.io.File;
import java.util.List;

import net.mindlee.loontooth.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 用于BrowseActivity中文件列表ListView中的Adapter
 * @author 李伟
 *
 */
public class AllFilesAdapter extends BaseAdapter {
   // private LayoutInflater mInflater;
    private Bitmap floderBackDir;
    private Bitmap floderFloder;
    private Bitmap floderDoc;
    private Context context;
    private List<String> items;
    private List<String> paths;

    public AllFilesAdapter(Context context, List<String> items, List<String> paths) {
    	this.context = context;
        this.items = items;
        this.paths = paths;
        floderBackDir = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.folder_back_dir);
        floderFloder = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.folder_light);
        floderDoc = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.folder_doc);
    }

    public int getCount() {
        return items.size();
    }

    public Object getItem(int position) {
        return items.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.all_files_item, null);
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.filePath);
            holder.icon = (ImageView) convertView.findViewById(R.id.fileIcon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        File f = new File(paths.get(position).toString());

        if (items.get(position).toString().equals("rootPath")) {
            holder.text.setText("返回根目录");
            holder.icon.setImageBitmap(floderBackDir);
        } else if (items.get(position).toString().equals("parentDir")) {
            holder.text.setText("返回上一层");
            holder.icon.setImageBitmap(floderBackDir);
        } else {
            holder.text.setText(f.getName());
            if (f.isDirectory()) {
                holder.icon.setImageBitmap(floderFloder);
            } else {
                holder.icon.setImageBitmap(floderDoc);
            }
        }
        return convertView;
    }

    private class ViewHolder {
        TextView text;
        ImageView icon;
    }

}
