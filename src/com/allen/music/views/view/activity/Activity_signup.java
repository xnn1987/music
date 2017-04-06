package com.allen.music.views.view.activity;

import java.util.Map;

import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.allen.music.R;
import com.allen.music.http.HttpResStrToAsync;
import com.allen.music.views.base.BaseActivity;
import com.allen.music.views.base.BaseAppException;
import com.allen.music.views.bean.Result;
import com.allen.music.views.view.video.Video_add;

public class Activity_signup extends BaseActivity {

	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);

		setContentLayout(R.layout.activity_signup);

		initTitleView();

		initView();
	}

	private void send(final Map<String, Object> param) throws BaseAppException {

		new HttpResStrToAsync() {
			protected void onPreExecute() {// 2
				showDialog();
			}

			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					Result ob = (Result) msg.obj;
					if (ob != null) {

					}
				} else if (msg.what == 0) {
					Result ob = (Result) msg.obj;
					ToastMessage(ob.Message);
				} else if (msg.what == -1) {
					((BaseAppException) msg.obj).makeToast(getApplicationContext());
				}
				closeDialog();
			}
		}.execute_Test(param);
	}

	// 初始化view
	private void initView() {
		Button btnOk = (Button) findViewById(R.id.btnOk);
		btnOk.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gotoActivity(Activity_signup_add.class);
			}
		});
	}

	// 初始化title
	private void initTitleView() {

		getbtn_left().setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onKeyDown(KeyEvent.KEYCODE_BACK, null);
			}
		});

		setTitle("钢琴之星");
	}
}
