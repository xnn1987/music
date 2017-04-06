package com.allen.music.views.view.set;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.allen.music.AppContext;
import com.allen.music.R;
import com.allen.music.http.HttpResStrToAsync;
import com.allen.music.tools.StringUtils;
import com.allen.music.views.base.BaseActivity;
import com.allen.music.views.base.BaseAppException;
import com.allen.music.views.base.BaseAppManager;
import com.allen.music.views.bean.Result;
import com.allen.music.views.model.Bean_member;

public class Set_pwd_updata extends BaseActivity implements OnClickListener {
	/** Called when the activity is first created. */

	private EditText set_password_e1;
	private EditText set_password_e2;
	private EditText set_password_e3;

	private Button set_password_button;

	private String old_password;
	private String new_password;
	private String cof_password;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentLayout(R.layout.set_pwd_updata);

		initTitleView();

		initView();
	}

	public void onClick(View v) {
		if (v == set_password_button) {
			try {
				old_password = getStr(set_password_e1);
				new_password = getStr(set_password_e2);
				cof_password = getStr(set_password_e3);

				if (StringUtils.isEmpty(old_password)) {
					ToastMessage("原密码不能为空");
					return;
				}

				// 新密码----
				if (StringUtils.isEmpty(new_password)) {
					ToastMessage("新密码不能为空");
					return;
				}
				if (new_password.length() < 6) {
					ToastMessage("新密码长度不能小于6位");
					return;
				}

				// 确认密码----
				if (StringUtils.isEmpty(cof_password)) {
					ToastMessage("确认密码不能为空");
					return;
				}
				if (cof_password.length() < 6) {
					ToastMessage("确认密码长度不能小于6位");
					return;
				}

				// 判断新密码是否，和原密码相等
				Bean_member user = getAc().getLoginInfo();

				if (!user.password.equals(old_password)) {
					ToastMessage("原密码输入错误");
					return;
				}

				// 判断新密码是否，和原密码相等
				if (!new_password.equals(cof_password)) {
					ToastMessage("新密码与确认密码不相符");
					return;
				}

				Map<String, Object> param = new HashMap<String, Object>();
				param.put("yhxx_id", AppContext.getLoginUid());
				param.put("yhxx_new_pwd", new_password);
				param.put("yhxx_old_pwd", old_password);

				update(param);

			} catch (BaseAppException e) {
				e.makeToast(getApplicationContext());
			} catch (Exception e) {
				ToastMessage(getStr(R.string.app_run_code_error));
			}
		}
	}

	// 修改
	private void update(final Map<String, Object> param) throws BaseAppException {

		new HttpResStrToAsync() {

			protected void onPreExecute() {
				showDialog();
			}

			public void handleMessage(Message msg) {
				if (msg.what == -1) {
					((BaseAppException) msg.obj).makeToast(Set_pwd_updata.this);
				} else {
					// 提示
					Result ob = (Result) msg.obj;
					ToastMessage(ob.Message);

					// 注销账户
					getAc().cleanLoginInfo();

					// 清除所有activity
					BaseAppManager.getAppManager().finishAllActivity();

					// 重启应用
					Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
				}
				closeDialog();
			}
		}.execute_Test(param);
	}

	// 初始化View
	private void initView() {
		set_password_e1 = (EditText) findViewById(R.id.set_password_e1);
		set_password_e2 = (EditText) findViewById(R.id.set_password_e2);
		set_password_e3 = (EditText) findViewById(R.id.set_password_e3);

		set_password_button = (Button) findViewById(R.id.set_password_button);
		set_password_button.setOnClickListener(this);
	}

	protected void initTitleView() {
		getbtn_left().setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onKeyDown(KeyEvent.KEYCODE_BACK, null);
			}
		});

		hidebtn_right();

		setTitle("修改密码");
	}
}