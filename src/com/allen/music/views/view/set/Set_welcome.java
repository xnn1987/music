package com.allen.music.views.view.set;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.allen.music.AppConfig;
import com.allen.music.AppContext;
import com.allen.music.R;
import com.allen.music.views.view.UIHelper;
import com.allen.music.views.widget.ScrollLayout;

public class Set_welcome extends Activity implements OnClickListener {
	/** Called when the activity is first created. */

	private ScrollLayout mScrollLayout;
	private ImageView[] mImageViews;
	private Button button;
	private int mViewCount;
	private int mCurSel;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.public_home_desc_main);

		init();
	}

	private void init() {
		mScrollLayout = (ScrollLayout) findViewById(R.id.ScrollLayout);
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.llayout);
		mViewCount = mScrollLayout.getChildCount();
		mImageViews = new ImageView[mViewCount];
		for (int i = 0; i < mViewCount; i++) {
			mImageViews[i] = (ImageView) linearLayout.getChildAt(i);
			mImageViews[i].setEnabled(true);
			mImageViews[i].setOnClickListener(this);
			mImageViews[i].setTag(i);
		}
		mCurSel = 0;
		mImageViews[mCurSel].setEnabled(false);
		mScrollLayout.SetOnViewChangeListener(new ScrollLayout.ScrollOnViewChangeListener() {
			public void OnViewChange(int view) {
				// TODO Auto-generated method stub
				setCurPoint(view);
			}
		});

		button = (Button) findViewById(R.id.enter);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 跳转
				Intent intent = new Intent(getApplicationContext(), Set_login.class);
				startActivity(intent);

				// 第一次进入后，WhatsNew 改为真，再次进入则不会进入欢迎界面
				// SharedPreferences p = getSharedPreferences("WhatsNew",
				// MODE_PRIVATE);
				// p.edit().putBoolean("WhatsNew", true).commit();

				AppContext ac = (AppContext) getApplication();
				ac.setAppConfig(AppConfig.CONF_NEW, false);

				// 结束
				UIHelper.Back(Set_welcome.this);
			}
		});
	}

	private void setCurPoint(int index) {
		if (index < 0 || index > mViewCount - 1 || mCurSel == index) {
			return;
		}
		mImageViews[mCurSel].setEnabled(true);
		mImageViews[index].setEnabled(false);
		mCurSel = index;
	}

	public void onClick(View v) {
		int pos = (Integer) (v.getTag());
		setCurPoint(pos);
		mScrollLayout.snapToScreen(pos);
	}
}