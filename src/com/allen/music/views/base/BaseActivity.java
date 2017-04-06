package com.allen.music.views.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allen.music.R;
import com.allen.music.tools.AnimUtils;
import com.allen.music.tools.Logger;

/**
 * 应用程序Activity的基类
 * 
 * @version 1.0
 * @created 2012-10-18
 */
public class BaseActivity extends Activity {

	// 通用Title
	private View titleView;
	private View contentView;
	private LinearLayout ly_content;

	private TextView tv_title;
	private Button btn_left;
	private Button btn_right;

	// 通用资源缩写
	private Resources res;

	// 通用加载
	private Dialog narmal;
	private Dialog warning;

	private BaseAppContext ac;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Logger.d(getClass().getName(), "onCreate");

		setContentView(R.layout.public_title_content);

		ac = (BaseAppContext) getApplication();

		TitleView();

		// 添加Activity到堆栈
		BaseAppManager.getAppManager().addActivity(this);

		// 通用资源缩写
		res = getResources();

		// 优化输入法模式
		int inputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;
		getWindow().setSoftInputMode(inputMode);
	}

	protected void onResume() {
		super.onResume();
		Logger.d(getClass().getName(), "onResume");
	}

	protected void onStart() {
		super.onStart();
		Logger.d(getClass().getName(), "onStart");
	}

	protected void onPause() {
		super.onPause();
		Logger.d(getClass().getName(), "onPause");
	}

	protected void onStop() {
		super.onStop();
		Logger.d(getClass().getName(), "onStop");
	}

	protected void onDestroy() {
		super.onDestroy();
		Logger.d(getClass().getName(), "onCreate");

		// 结束Activity&从堆栈中移除
		BaseAppManager.getAppManager().finishActivity(this);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			finish();

			AnimUtils.overridePendingTransition_Right(this);

			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	// --------------------------------------------------------

	public void TitleView() {

		titleView = findViewById(R.id.title_bar);

		tv_title = (TextView) titleView.findViewById(R.id.title_text);
		btn_left = (Button) titleView.findViewById(R.id.left_btn);
		btn_right = (Button) titleView.findViewById(R.id.right_btn);

		ly_content = (LinearLayout) findViewById(R.id.content);
	}

	/***
	 * 设置内容区域
	 * 
	 * @param resId
	 *            资源文件ID
	 */
	public void setContentLayout(int resId) {

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		contentView = inflater.inflate(resId, null);
		LayoutParams layoutParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		contentView.setLayoutParams(layoutParams);
		contentView.setBackgroundDrawable(null);
		if (null != ly_content) {
			ly_content.addView(contentView);
		}

	}

	/***
	 * 设置内容区域
	 * 
	 * @param view
	 *            View对象
	 */
	public void setContentLayout(View view) {
		if (null != ly_content) {
			ly_content.addView(view);
		}
	}

	/**
	 * 得到内容的View
	 * 
	 * @return
	 */
	public View getLyContentView() {

		return contentView;
	}

	/**
	 * 得到左边的按钮
	 * 
	 * @return
	 */
	public Button getbtn_left() {
		return btn_left;
	}

	/**
	 * 得到右边的按钮
	 * 
	 * @return
	 */
	public Button getbtn_right() {
		return btn_right;
	}

	/**
	 * 设置标题
	 * 
	 * @param title
	 */
	public void setTitle(String title) {

		if (null != tv_title) {
			tv_title.setText(title);
		}

	}

	/**
	 * 设置标题
	 * 
	 * @param resId
	 */
	public void setTitle(int resId) {
		tv_title.setText(getString(resId));
	}

	/**
	 * 隐藏上方的标题栏
	 */
	public void hideTitleView() {

		if (null != titleView) {
			titleView.setVisibility(View.GONE);
		}
	}

	/**
	 * 显示上方的标题栏
	 */
	public void showTitleView() {

		if (null != titleView) {
			titleView.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 隐藏左边的按钮
	 */
	public void hidebtn_left() {

		if (null != btn_left) {
			btn_left.setVisibility(View.GONE);
		}

	}

	/***
	 * 隐藏右边的按钮
	 */
	public void hidebtn_right() {
		if (null != btn_right) {
			btn_right.setVisibility(View.GONE);
		}

	}

	// --------------------------------------------------------
	/**
	 * 从当前activity跳转到目标activity,<br>
	 * 如果目标activity曾经打开过,就重新展现,<br>
	 * 如果从来没打开过,就新建一个打开
	 * 
	 * FLAG_ACTIVITY_REORDER_TO_FRONT a-b-c-d d启动b 则 a-b FLAG_ACTIVITY_CLEAR_TOP
	 * a-b-c-d d启动b 则 a-c-d-b
	 * 
	 * @param cls
	 */
	public void gotoExistActivity(Class<?> cls) {
		Intent intent = new Intent(this, cls);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);

		AnimUtils.overridePendingTransition_Left(this);
	}

	/**
	 * 新建一个activity打开
	 * 
	 * @param cls
	 */
	public void gotoActivity(Class<?> cls) {
		Intent intent = new Intent(this, cls);
		startActivity(intent);
		AnimUtils.overridePendingTransition_Left(this);
	}

	public void gotoActivity(Class<?> cls, Bundle bundle) {
		Intent intent = new Intent(this, cls);
		intent.putExtras(bundle);
		startActivity(intent);

		AnimUtils.overridePendingTransition_Left(this);
	}

	public void gotoActivityForResult(Class<?> cls, int requestCode) {
		Intent intent = new Intent(this, cls);
		startActivityForResult(intent, requestCode);

		AnimUtils.overridePendingTransition_Left(this);
	}

	public void gotoActivityForResult(Class<?> cls, int requestCode, Bundle bundle) {
		Intent intent = new Intent(this, cls);
		intent.putExtras(bundle);
		startActivityForResult(intent, requestCode);

		AnimUtils.overridePendingTransition_Left(this);
	}

	// --------------------------------------------------------
	/**
	 * 弹出Toast消息
	 * 
	 * @param msg
	 */
	public void ToastMessage(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	public void ToastMessage(int msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	public void ToastMessage(String msg, int time) {
		Toast.makeText(this, msg, time).show();
	}

	/**
	 * 开启、关闭加载框
	 * 
	 */
	public boolean showDialog() {

		// 是否联网网络
		ConnectivityManager manger = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manger.getActiveNetworkInfo();
		if (info != null) {
			narmal = new Dialog(this, R.style.dialog);
			narmal.setContentView(R.layout.public_show_dialog_loading);
			narmal.show();
			return true;
		} else {
			warning = new Dialog(this, R.layout.public_show_dialog_loading);
			warning.show();
			Window window = warning.getWindow();
			window.setContentView(R.layout.public_show_dialog_warning);
			Button button01 = (Button) window.findViewById(R.id.show_dialog_button1);
			Button button02 = (Button) window.findViewById(R.id.show_dialog_button2);
			button01.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
				}
			});
			button02.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					warning.dismiss();
				}
			});
			return true;
		}

	}

	public void closeDialog() {
		if (narmal != null) {
			narmal.dismiss();
		}
	}

	static class AlixOnCancelListener implements DialogInterface.OnCancelListener {
		Activity mcontext;

		AlixOnCancelListener(Activity context) {
			mcontext = context;
		}

		public void onCancel(DialogInterface dialog) {
			mcontext.onKeyDown(KeyEvent.KEYCODE_BACK, null);
		}
	}

	// --------------------------------------------------------
	/**
	 * 获取appcontent
	 * 
	 * @param resId
	 * @return
	 */
	public BaseAppContext getAc() {
		return ac;
	}

	/**
	 * 从资源获取字符串
	 * 
	 * @param resId
	 * @return
	 */
	public String getStr(int resId) {
		return res.getString(resId);
	}

	/**
	 * 从EditText 获取字符串
	 * 
	 * @param editText
	 * @return
	 */
	public String getStr(EditText editText) {
		return editText.getText().toString();
	}

	/**
	 * 从Button 获取字符串
	 * 
	 * @param editText
	 * @return
	 */
	public String getStr(Button button) {
		return button.getText().toString();
	}

	// --------------------------------------------------------
}
