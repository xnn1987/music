package com.allen.music;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.allen.music.tools.AnimUtils;
import com.allen.music.views.view.UIHelper;
import com.allen.music.views.view.set.Set_login;
import com.allen.music.views.view.set.Set_welcome;

public class AppStart extends Activity {

	private boolean isconnect = false;

	private boolean isnew = false;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 取消状态栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.public_home_welcome);

		// 获取app版本
		new Appversion().start();

		// 设置图片动画效果
		View iv = this.findViewById(R.id.home_welcome_ImageView);
		AlphaAnimation alpAnimation = new AlphaAnimation(0.1f, 1.0f);
		alpAnimation.setDuration(1000);
		iv.startAnimation(alpAnimation);

		// 内部匿名类实现动画监听，重写三个事件，我们关心的时最后一个
		alpAnimation.setAnimationListener(new Animation.AnimationListener() {
			public void onAnimationStart(Animation animation) {
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				// 判断是否联网
				if (isconnect) {
					// 判断是否进入欢迎界面
					// if (isnew) {
					// Intent intent = new Intent(AppStart.this,
					// Set_welcome.class);
					// startActivity(intent);
					// AnimUtils.overridePendingTransition_Left(AppStart.this);
					// } else {
					Intent intent = new Intent(AppStart.this, Set_login.class);
					startActivity(intent);
					AnimUtils.overridePendingTransition_Left(AppStart.this);
					// }
					// 结束
					UIHelper.Back(AppStart.this);

				} else {
					// 跳转至-主页面
					AlertDialog.Builder builder = new AlertDialog.Builder(AppStart.this);
					builder.setTitle("提示");
					builder.setMessage("您的网络没有打开，是否打开?");
					// alertbBuilder.setIcon(R.drawable.yhzx_main_warn);
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// 跳转到网络设置页面
							startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
							// 销毁本页面
							UIHelper.Back(AppStart.this);
						}
					});
					builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();

							finish();
						}
					}).show();
				}
			}
		});
	}

	class Appversion extends Thread {
		public void run() {
			AppContext ac = (AppContext) getApplication();
			ac.getAppVerson();
			isconnect = ac.isNetworkConnected();
			isnew = ac.isWhatsNew();
		}
	}
}
