package com.allen.music.views.view.user;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.allen.music.R;
import com.allen.music.views.base.BaseActivity;
import com.allen.music.views.model.Bean_member;

public class User_info extends BaseActivity implements OnClickListener {

	private TextView tv_fullname;
	private TextView tv_nickname;
	private Button tv_sex;
	private TextView tv_birthday;
	private TextView tv_phonenum;
	private TextView tv_email;

	private Button bt_xiugai;

	private Bean_member user;

	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);

		setContentLayout(R.layout.user_info);

		initTitleView();

		initView();

		initData();
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == bt_xiugai) {
		}
	}

	// 初始化数据
	private void initData() {

		// Bean_member user = getAc().getLoginInfo();
		//
		// tv_fullname.setText(user.yhxx_fullname);
		// tv_nickname.setText(user.yhxx_nickname);
		// if (StringUtils.toInt(user.yhxx_sex) == 1) {
		// tv_sex.setText("男");
		// } else {
		// tv_sex.setText("女");
		// }
		//
		// tv_birthday.setText(user.yhxx_birth);
		// tv_email.setText(user.yhxx_email);
		// tv_phonenum.setText(user.username);
	}

	// 初始化view
	private void initView() {

		//tv_fullname = (TextView) findViewById(R.id.tv_fullname);
		tv_nickname = (TextView) findViewById(R.id.tv_nickname);
		tv_sex = (Button) findViewById(R.id.tv_sex);

		tv_birthday = (TextView) findViewById(R.id.tv_birthday);
		tv_email = (TextView) findViewById(R.id.tv_email);
		tv_phonenum = (TextView) findViewById(R.id.tv_phonenum);

		bt_xiugai = (Button) findViewById(R.id.bt_xiugai);
		bt_xiugai.setOnClickListener(this);

	}

	// 初始化title
	private void initTitleView() {
		getbtn_left().setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onKeyDown(KeyEvent.KEYCODE_BACK, null);
			}
		});

		hidebtn_right();

		setTitle("个人资料");
	}
}
