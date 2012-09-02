package net.mindlee.loontooth.util;

import net.mindlee.loontooth.R;
import net.mindlee.loontooth.adapter.DownMenuAdapter;
import net.mindlee.loontooth.gui.MainActivity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;

public class PopWindow {
	private ListView downMenuListView;
	private PopupWindow downMenuPopWindow;
	private Context context;

	public PopWindow(Context context) {
		this.context = context;
	}

	public PopupWindow createDownMenu() {
		View downMenuLayout = View.inflate(context, R.layout.downmenu, null);
		downMenuPopWindow = new PopupWindow(downMenuLayout,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		downMenuPopWindow.setTouchable(true);
		downMenuPopWindow.setOutsideTouchable(true);
		downMenuPopWindow.setWidth(MainActivity.SCREEN_WIDTH / 3);
		downMenuPopWindow.setBackgroundDrawable(new ColorDrawable(0));

		Log.w("popWindow是否聚焦", "" + downMenuPopWindow.isFocusable());
		Log.w("popWindow是否可点", "" + downMenuPopWindow.isTouchable());
		Log.w("popWindow外部是否可点", "" + downMenuPopWindow.isOutsideTouchable());

		downMenuListView = (ListView) downMenuLayout
				.findViewById(R.id.downmenu_listView);
		DownMenuAdapter downMenuAdapter = new DownMenuAdapter(context);
		downMenuListView.setAdapter(downMenuAdapter);
		return downMenuPopWindow;
	}

	public ListView getDownMenuListView() {
		return downMenuListView;
	}
}
