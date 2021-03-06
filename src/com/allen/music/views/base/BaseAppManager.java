package com.allen.music.views.base;

import java.util.Stack;

import android.app.Activity;
import android.content.Context;

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 * 
 * @version 1.0
 * @created 2012-3-21
 */
public class BaseAppManager {

	private static Stack<Activity> activityStack;
	private static BaseAppManager instance;

	private BaseAppManager() {
	}

	/**
	 * 单一实例
	 */
	public static BaseAppManager getAppManager() {
		if (instance == null) {
			instance = new BaseAppManager();
		}
		return instance;
	}

	/**
	 * 添加Activity到堆栈
	 */
	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	/**
	 * 获取当前Activity（堆栈中最后一个压入的）
	 */
	public Activity currentActivity() {
		Activity activity = activityStack.lastElement();
		return activity;
	}

	/**
	 * 结束当前Activity（堆栈中最后一个压入的）
	 */
	public void finishActivity() {
		Activity activity = activityStack.lastElement();
		finishActivity(activity);
	}

	/**
	 * 结束指定的Activity
	 */
	public void finishActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 结束指定类名的Activity
	 */
	public void finishActivity(Class<?> cls) {
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				finishActivity(activity);
			}
		}
	}

	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity() {
		final int count = activityStack.size();
		for (int i = 0, size = count; i < size; i++) {
			if (null != activityStack.get(i)) {
				activityStack.get(i).finish();
			}
		}
		activityStack.clear();
	}

	/**
	 * 退出应用程序
	 */
	public void AppExit(Context context) {
		try {
			finishAllActivity();
			// ActivityManager activityMgr = (ActivityManager)
			// context.getSystemService(Context.ACTIVITY_SERVICE);
			// activityMgr.killBackgroundProcesses(context.getPackageName());
			// activityMgr.restartPackage(context.getPackageName());
			android.os.Process.killProcess(android.os.Process.myPid());
		} catch (Exception e) {
		} finally {
			System.exit(0);
		}
	}
}