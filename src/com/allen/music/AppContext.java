package com.allen.music;

import java.io.File;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;

import com.allen.music.tools.CrashHandler;
import com.allen.music.tools.Logger;
import com.allen.music.tools.Native;
import com.allen.music.tools.StringUtils;
import com.allen.music.views.base.BaseAppContext;

/**
 * 全局应用程序类：用于保存和调用全局应用配置
 * 
 * @version 1.0
 * @created 2012-3-21
 * @modify: 2013-11-11
 * @modifyContent: 增加log输出到sdcard中MusicCloud文件夹下
 */
public class AppContext extends BaseAppContext {

	public void onCreate() {
		super.onCreate();
		
        // Initialize logger.
        Native.logInitialize(getLogPath(), File.separatorChar);
        Logger.setDebug("1");
        Logger.memo(this, getClass().getName(), null);
        Logger.i(getClass().getName(), "onCreate");
        CrashHandler.getInstance(this); // Initialize and enable crash handler.
	}
	
	@Override
	public void onTerminate() {
		Logger.i(getClass().getName(), "onTerminate");
		
		Native.logDestroy();
		super.onTerminate();		
	}
	/**
	 * 获取应用程序根路径
	 * **/
	public static String getProductFolder() {
        File sd = Environment.getExternalStorageDirectory();
        String sdcard = sd.getAbsolutePath();
        String retVal = sdcard + File.separatorChar + "MusicCloud";
        File folder = new File(retVal); // Create the folder if not exists.
        if (!folder.exists()) {
            folder.mkdirs();           
        }
        return retVal;
	}
	/**
	 * 获取log文件保存路径
	 * **/
	private String getLogPath() {
	    String retVal = getProductFolder() + File.separatorChar + "music.log"; 
        return retVal;
	}

	/**
	 * 获取App安装包信息
	 * 
	 * @return
	 */
	public PackageInfo getPackageInfo() {
		PackageInfo info = null;
		try {
			info = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace(System.err);
		}
		if (info == null)
			info = new PackageInfo();
		return info;
	}

	/**
	 * 获取App版本编号
	 * 
	 * @return
	 */
	public void getAppVerson() {
		String version = getProperty(AppConfig.CONF_APP_VERSION);
		if (StringUtils.isEmpty(version)) {
			PackageInfo info = getPackageInfo();
			setProperty(AppConfig.CONF_APP_VERSION, info.versionName);
			appversion = info.versionName;
		} else {
			appversion = version;
		}
	}

	/**
	 * 是否进入欢迎界面
	 * 
	 * @return
	 */
	public boolean isWhatsNew() {
		String perf_whatsnew = getProperty(AppConfig.CONF_NEW);
		// 默认是进入
		if (StringUtils.isEmpty(perf_whatsnew))
			return true;
		else
			return StringUtils.toBool(perf_whatsnew);
	}

}
