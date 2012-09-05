package net.mindlee.loontooth.util;

import net.mindlee.loontooth.R;
import net.mindlee.loontooth.adapter.MenuAdapter;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.GridView;

public class Dialog {
	private Context context;
	private GridView menuGrid;
	private MenuAdapter menuAdapter;

	public Dialog(Context context) {
		this.context = context;
		menuAdapter = new MenuAdapter(context);
	}

	public void createNoAccessDialog() {
		new AlertDialog.Builder(context).setTitle("Message")
				.setMessage("权限不足!")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();
	}

	public void createMenuDialog() {
        View menuView = View.inflate(context, R.layout.menu_gridview, null);
        // 创建AlertDialog
        final AlertDialog menuDialog = new AlertDialog.Builder(context).create();
        menuDialog.setView(menuView);
        menuGrid = (GridView) menuView.findViewById(R.id.gridview);
        menuGrid.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                 return MotionEvent.ACTION_MOVE == event.getAction() ? true
                           : false;
            }
       });

        menuGrid.setAdapter(menuAdapter);
        
        menuDialog.show();
	}
}
