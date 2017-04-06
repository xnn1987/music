package com.allen.music.views.view.set;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.allen.music.R;
import com.allen.music.http.HttpResStrToAsync;
import com.allen.music.tools.StringUtils;
import com.allen.music.views.base.BaseActivity;
import com.allen.music.views.base.BaseAppException;
import com.allen.music.views.bean.Result;
import com.allen.music.views.model.Bean_member;
import com.allen.music.views.view.UIHelper;
import com.allen.music.views.view.activity.Activity_activity;

public class Set_login extends BaseActivity implements OnClickListener {
	/** Called when the activity is first created. */
	private EditText set_login_username; // 用户名
	private EditText set_login_password; // 密码
	private Button login_btn; // 登陆

	private CheckBox remember_pwd;

	private String loginName;
	private String loginPwd;

	private Button btn_find;
	private Button btn_reg;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.set_login);

		initView();

		initData();
	}

	// 查询
	private void select(final Map<String, Object> param) throws BaseAppException {

		new HttpResStrToAsync() {

			protected void onPreExecute() {
				showDialog();
			}

			public void handleMessage(Message msg) {

				if (msg.what == 1) {

					Result ob = (Result) msg.obj;

					if (ob != null) {
						//
						// // 保存登录信息
						// getAc().saveLoginInfo(Bean_member.parse(loginName,
						// loginPwd, isRememberMe));
						//
						// // 跳转
						// gotoActivity(Activity_activity.class);
						//
						// // 销毁本页面
						// UIHelper.Back(Set_login.this);
					}
				} else if (msg.what == 0) {

					Result ob = (Result) msg.obj;

					ToastMessage(ob.Message);

					// 清除登录信息
					getAc().cleanLoginInfo();

				} else if (msg.what == -1) {

					((BaseAppException) msg.obj).makeToast(getApplicationContext());
				}

				// 保存登录信息
				getAc().saveLoginInfo(Bean_member.parse(loginName, loginPwd));

				// 跳转
				gotoActivity(Activity_activity.class);

				// 销毁本页面
				UIHelper.Back(Set_login.this);

				closeDialog();
			}
		}.execute_select(param);
	}

	// 事件
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == login_btn) {
			loginName = getStr(set_login_username);
			loginPwd = getStr(set_login_password);
			// boolean isRememberMe = remember_pwd.isChecked();

			// 判断输入
			if (StringUtils.isEmpty(loginName) || StringUtils.isEmpty(loginPwd)) {
				ToastMessage("帐号/密码不能为空");
				return;
			}

			try {
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("tablename", "kf_member");
				param.put("field", "admin-password");
				param.put("val", loginName + "-" + loginPwd);
				param.put("pagenum", "");
				param.put("pagesize", "");
				select(param);

			} catch (BaseAppException e) {
				e.makeToast(getApplicationContext());
			} catch (Exception e) {
				ToastMessage(getStr(R.string.app_data_code_error));
			}
		} else if (v == btn_find) {
			gotoActivity(Set_pwd_updata.class);
		} else if (v == btn_reg) {
			gotoActivity(Set_reg.class);
		}
	}

	// 初始化Data
	private void initData() {
		Bean_member user = getAc().getLoginInfo();
		set_login_username.setText(user.username);
		set_login_password.setText(user.password);
		// remember_pwd.setChecked(user.isremember);
	}

	// 初始化View
	private void initView() {
		set_login_username = (EditText) findViewById(R.id.set_login_username);
		set_login_password = (EditText) findViewById(R.id.set_login_password);
		remember_pwd = (CheckBox) findViewById(R.id.remember_pwd);

		login_btn = (Button) findViewById(R.id.login_btn);
		login_btn.setOnClickListener(this);

		btn_find = (Button) findViewById(R.id.btn_find);
		btn_find.setOnClickListener(this);

		btn_reg = (Button) findViewById(R.id.btn_reg);
		btn_reg.setOnClickListener(this);
	}
}