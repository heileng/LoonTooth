package net.mindlee.loontooth.gui;

import net.mindlee.loontooth.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class HistoryActivity  extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
	}
}