package com.allen.music.views.view.set;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.allen.music.R;
import com.allen.music.http.HttpResStrToAsync;
import com.allen.music.tools.StringUtils;
import com.allen.music.views.base.BaseActivity;
import com.allen.music.views.base.BaseAppException;
import com.allen.music.views.bean.Result;

public class Set_reg extends BaseActivity implements OnClickListener {
	/** Called when the activity is first created. */

	private EditText set_password_e1;
	private EditText set_password_e2;
	private EditText set_password_e3;

	private Button set_password_button;

	private String moblie;
	private String new_password;
	private String cof_password;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentLayout(R.layout.set_reg);

		initTitleView();

		initView();
	}

	public void onClick(View v) {
		if (v == set_password_button) {
			try {
				moblie = getStr(set_password_e1);
				new_password = getStr(set_password_e2);
				cof_password = getStr(set_password_e3);

				if (StringUtils.isEmpty(moblie)) {
					ToastMessage("手机号码不能为空");
					return;
				}
				if (StringUtils.isEmpty(new_password)) {
					ToastMessage("密码不能为空");
					return;
				}
				if (StringUtils.isEmpty(cof_password)) {
					ToastMessage("确认密码不能为空");
					return;
				}
				if (cof_password.equals(new_password)) {
					ToastMessage("新密码与确认密码不一致");
					return;
				}

				Map<String, Object> param = new HashMap<String, Object>();
				param.put("tablename", "kf_member");
				param.put("field", "admin-password");
				param.put("val", moblie + "-" + cof_password);

				add(param);

			} catch (BaseAppException e) {
				e.makeToast(getApplicationContext());
			} catch (Exception e) {
				ToastMessage(getStr(R.string.app_run_code_error));
			}
		}
	}

	// 添加
	private void add(final Map<String, Object> param) throws BaseAppException {

		new HttpResStrToAsync() {

			protected void onPreExecute() {
				showDialog();
			}

			public void handleMessage(Message msg) {
				if (msg.what == 1) {

					Result ob = (Result) msg.obj;

					if (ob != null) {

						try {
							Map<String, Object> param = new HashMap<String, Object>();
							param.put("tablename", "kf_member");
							param.put("field", "admin-password");
							param.put("val", moblie + "-" + cof_password);
							param.put("pagenum", "");
							param.put("pagesize", "");
							select(param);

						} catch (BaseAppException e) {
							e.makeToast(getApplicationContext());
						} catch (Exception e) {
							ToastMessage(getStr(R.string.app_run_code_error));
						}

					}
				} else if (msg.what == 0) {

					Result ob = (Result) msg.obj;
					ToastMessage(ob.Message);

				} else if (msg.what == -1) {

					((BaseAppException) msg.obj).makeToast(getApplicationContext());
				}
				closeDialog();
			}
		}.execute_add(param);
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

						Map<String, Object> param = new HashMap<String, Object>();
						param.put("tablename", "kf_type");
						param.put("field", "uid");
						param.put("val", moblie + "-" + cof_password);

						// add(param);
					}
				} else if (msg.what == 0) {

					Result ob = (Result) msg.obj;
					ToastMessage(ob.Message);

				} else if (msg.what == -1) {

					((BaseAppException) msg.obj).makeToast(getApplicationContext());
				}
				closeDialog();
			}
		}.execute_select(param);
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

		setTitle("用户注册");
	}
}