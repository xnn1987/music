package com.allen.music.views.view.activity;

import java.util.Map;

import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.allen.music.R;
import com.allen.music.http.HttpResStrToAsync;
import com.allen.music.views.base.BaseActivity;
import com.allen.music.views.base.BaseAppException;
import com.allen.music.views.bean.Result;
import com.allen.music.views.view.UIHelper;
import com.allen.music.views.view.user.User_info;

public class Activity_activity extends BaseActivity implements OnClickListener {

	Button tab_btn_type_1;
	Button tab_btn_type_2;

	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);

		setContentLayout(R.layout.activity_activity);

		initTitleView();

		initView();
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == tab_btn_type_1) {
			gotoActivity(Activity_watch.class);
		} else if (v == tab_btn_type_2) {
			gotoActivity(Activity_signup.class);
		}
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

		tab_btn_type_1 = (Button) findViewById(R.id.tab_btn_type_1);
		tab_btn_type_1.setOnClickListener(this);

		tab_btn_type_2 = (Button) findViewById(R.id.tab_btn_type_2);
		tab_btn_type_2.setOnClickListener(this);
	}

	// 初始化title
	private void initTitleView() {

		hidebtn_left();

		getbtn_right().setText("SopHie");
		getbtn_right().setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				gotoActivity(User_info.class);
			};
		});

		setTitle("钢琴之星");
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			UIHelper.Exit(this);
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
}
