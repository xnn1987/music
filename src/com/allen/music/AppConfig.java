package com.allen.music;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * 应用程序配置类：用于保存用户相关信息及设置
 * 
 * @version 1.0
 * @created 2012-3-21
 */
public class AppConfig {

	public final static String APP_CONFIG = "app_config";

	// 页号
	public final static int PAGE_No = 1;
	// 每页条数
	public final static int PAGE_Size = 5;
	// 是否有下一页
	public final static int PAGE_Next = 0;

	// 添加
	public final static int MENU_ADD = 1;
	// 修改
	public final static int MENU_UPDATE = 2;
	// 删除
	public final static int MENU_DELETE = 3;
	// 详情
	public final static int MENU_INFO = 4;

	// 获取App唯一标识
	public final static String CONF_APP_UNIQUEID = "APP_UNIQUEID";
	// 获取App版本
	public final static String CONF_APP_VERSION = "APP_VERSION";
	// 检查App新内容
	public final static String CONF_NEW = "App_whatsnew";

	// App推送
	public final static String CONF_PUSH = "App_push";
	// App下载
	public final static String CONF_PIC = "App_pic";
	// App支付
	public final static String CONF_PAY = "App_pay";

	// 缓存------------------------------------------------------------

	private Context mContext;

	private static AppConfig appConfig;

	public static AppConfig getAppConfig(Context context) {
		if (appConfig == null) {
			appConfig = new AppConfig();
			appConfig.mContext = context;
		}
		return appConfig;
	}

	/**
	 * 获取Preference设置
	 */
	public static SharedPreferences getSharedPreferences(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}

	public String get(String key) {
		Properties props = get();
		return (props != null) ? props.getProperty(key) : null;
	}

	public Properties get() {
		FileInputStream fis = null;
		Properties props = new Properties();
		try {
			// 读取files目录下的config
			// fis = activity.openFileInput(APP_CONFIG);

			// 读取app_config目录下的config
			File dirConf = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
			fis = new FileInputStream(dirConf.getPath() + File.separator + APP_CONFIG);

			props.load(fis);
		} catch (Exception e) {
		} finally {
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
		return props;
	}

	private void setProps(Properties p) {
		FileOutputStream fos = null;
		try {
			// 把config建在files目录下
			// fos = activity.openFileOutput(APP_CONFIG, Context.MODE_PRIVATE);

			// 把config建在(自定义)app_config的目录下
			File dirConf = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
			File conf = new File(dirConf, APP_CONFIG);
			fos = new FileOutputStream(conf);

			p.store(fos, null);
			fos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (Exception e) {
			}
		}
	}

	public void set(Properties ps) {
		Properties props = get();
		props.putAll(ps);
		setProps(props);
	}

	public void set(String key, String value) {
		Properties props = get();
		props.setProperty(key, value);
		setProps(props);
	}

	public void remove(String... key) {
		Properties props = get();
		for (String k : key)
			props.remove(k);
		setProps(props);
	}
}
