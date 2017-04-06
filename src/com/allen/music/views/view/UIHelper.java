package com.allen.music.views.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;

import com.allen.music.R;
import com.allen.music.tools.AnimUtils;
import com.allen.music.views.base.BaseAppContext;
import com.allen.music.views.base.BaseAppManager;
import com.allen.music.views.widget.CustomAlertDialog;

/**
 * 应用程序UI工具包：封装UI相关的一些操作
 * 
 */
public class UIHelper {

	/**
	 * 注销登录
	 * 
	 * @param cont
	 */
	public static void Logout(final Context cont) {
		CustomAlertDialog.Builder builder = new CustomAlertDialog.Builder(cont);
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setTitle("注销登录");
		builder.setMessage("注销后需要重新输入帐号、密码，确定要退出登录吗？");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// 清除登录信息
				BaseAppContext ac = (BaseAppContext) cont.getApplicationContext();
				ac.cleanLoginInfo();

				BaseAppManager.getAppManager().AppExit(cont);
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
			}
		});
		builder.show();
	}

	/**
	 * 退出程序
	 * 
	 * @param cont
	 */
	public static void Exit(final Context cont) {
		CustomAlertDialog.Builder builder = new CustomAlertDialog.Builder(cont);
		// builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setTitle("提示");
		builder.setMessage(R.string.app_menu_surelogout);
		builder.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// 退出
				BaseAppManager.getAppManager().AppExit(cont);
			}
		});
		builder.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
	}

	/**
	 * 撤销返回
	 * 
	 * @param cont
	 */
	public static void Revocation(final Context cont) {
		CustomAlertDialog.Builder builder = new CustomAlertDialog.Builder(cont);
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setTitle("安卡提示：");
		builder.setMessage("撤销后您当前的操作将不做保存\n确认是否返回？");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

				dialog.dismiss();

				((Activity) cont).finish();
				AnimUtils.overridePendingTransition_Right(cont);
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
			}
		});
		builder.show();
	}

	/**
	 * 返回
	 * 
	 * @param cont
	 */
	public static void Back(final Context cont) {

		((Activity) cont).finish();
		AnimUtils.overridePendingTransition_Right(cont);
	}
}
