package com.allen.music.views.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.text.ParseException;
import java.util.Properties;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.widget.Toast;

import com.allen.music.tools.StringUtils;
import com.allen.music.views.model.Bean_member;

/**
 * 全局应用程序类：用于保存和调用全局应用配置
 * 
 * @version 1.0
 * @created 2012-3-21
 */
public class BaseAppContext extends Application {

	private static final int CACHE_TIME = 60 * 1000;// 缓存失效时间,1分钟

	private static int login_user_id = 0; // 登录用户的id

	public static final boolean DEVELOPER_MODE = false;

	public static String appversion = "";

	public void onCreate() {
		// 开发者模式
		isDeveloperMode();

		super.onCreate();

		// 注册App异常崩溃处理器
		Thread.setDefaultUncaughtExceptionHandler(BaseAppException.getAppExceptionHandler());
	}

	public void isDeveloperMode() {
		if (DEVELOPER_MODE) {
			// 当我们发现问题。
			// 检测指定我们应该寻找什么样的问题。.detectDiskReads()、、、
			// 处罚规定我们应该做的，.penaltyLog()/.penaltyDialog()
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()// .detectCustomSlowCalls()
					.detectAll()// 全部
					.detectDiskReads() // 磁盘读
					.detectDiskWrites()// 磁盘写
					.detectNetwork()// 网络访问
					.penaltyLog() // 打印logcat
					.penaltyDialog()// 弹出框打印logcat
					.build());

			// 策略应用到虚拟机的过程中的所有线程。
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
			// VM方面的策略重点关注如下几类：
					.detectLeakedSqlLiteObjects() // 内存泄露的SQLite对象
					.detectLeakedClosableObjects()// 内存泄露的释放的对象
					.detectActivityLeaks()// 内存Activity的释放的对象
					.penaltyLog() // 打印logcat
					.penaltyDeath().build());
		}
	}

	/**
	 * 检测网络是否可用
	 * 
	 * @return
	 */
	public boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}

	/**
	 * 检查代理，是否cnwap接入
	 */
	public Proxy detectProxy() {
		Proxy mProxy = null;
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni != null && ni.isAvailable() && ni.getType() == ConnectivityManager.TYPE_MOBILE) {
			String proxyHost = android.net.Proxy.getDefaultHost();
			int port = android.net.Proxy.getDefaultPort();
			if (proxyHost != null) {
				final InetSocketAddress sa = new InetSocketAddress(proxyHost, port);
				mProxy = new Proxy(Proxy.Type.HTTP, sa);
			}
		}
		return mProxy;
	}

	/**
	 * 判断当前版本是否兼容目标版本的方法
	 * 
	 * @param VersionCode
	 * @return
	 */
	public static boolean isMethodsCompat(int VersionCode) {
		int currentVersion = android.os.Build.VERSION.SDK_INT;
		return currentVersion >= VersionCode;
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

	// AppConfig--------------------------------------------------------------------开始-

	/**
	 * 获取配置
	 * 
	 * @return
	 */
	public String getAppConfig(String key) {
		String config = getProperty(key);
		if (StringUtils.isEmpty(config))
			return "";
		else
			return config;
	}

	/**
	 * 设置配置1
	 * 
	 * @return
	 */
	public void setAppConfig(String key, String value) {
		setProperty(key, String.valueOf(value));
	}

	/**
	 * 设置配置2
	 * 
	 * @return
	 */
	public void setAppConfig(String key, boolean value) {
		setProperty(key, String.valueOf(value));
	}

	// AppConfig--------------------------------------------------------------------结束-

	// 获取用户信息-------------------------------------------------------------------开始-
	/**
	 * 用户注销
	 */
	public void Logout() {
		login_user_id = 0;
	}

	/**
	 * 获取登录用户id
	 * 
	 * @return
	 */
	public static int getLoginUid() {
		return login_user_id;
	}

	/**
	 * 未登录或修改密码后的处理
	 */
	public Handler getUnLoginHandler() {
		return this.unLoginHandler;
	}

	public Handler unLoginHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				Toast.makeText(BaseAppContext.this, "检测到您是未登录状态，请重新登录", Toast.LENGTH_SHORT).show();
			}
		}
	};

	/**
	 * 初始化用户登录信息
	 */
	public void initLoginInfo() {
		Bean_member loginUser = getLoginInfo();
		if (loginUser != null && loginUser.uid > 0) {
			login_user_id = loginUser.uid;
		} else {
			Logout();
		}
	}

	/**
	 * 保存登录信息
	 * 
	 * @param username
	 * @param pwd
	 */
	public void saveLoginInfo(final Bean_member user) {
		login_user_id = user.uid;
		Properties ps = new Properties();
		ps.setProperty("user.username", String.valueOf(user.username));
		ps.setProperty("user.password", getProperty("user.password"));
		setProperties(ps);
	}

	/**
	 * 清除登录信息
	 */
	public void cleanLoginInfo() {
		login_user_id = 0;
		removeProperty("user.username", "user.password", "user.isremember");
	}

	/**
	 * 获取登录信息
	 * 
	 * @return
	 */
	public Bean_member getLoginInfo() {
		Bean_member lu = new Bean_member();
		lu.username = getProperty("user.username");
		lu.password = getProperty("user.password");
		return lu;
	}

	// 获取用户信息----------------------------------------------------------------------结束-

	// 缓存相关-------------------------------------------------------------------------开始-
	/**
	 * 获取属性文件
	 */
	public Properties getProperties() {
		return BaseAppConfig.getAppConfig(this).get();
	}

	/**
	 * 查找属性
	 */
	public String getProperty(String key) {
		return BaseAppConfig.getAppConfig(this).get(key);
	}

	/**
	 * 设置属性 ps
	 */
	public void setProperties(Properties ps) {
		BaseAppConfig.getAppConfig(this).set(ps);
	}

	/**
	 * 设置属性 key-value
	 */
	public void setProperty(String key, String value) {
		BaseAppConfig.getAppConfig(this).set(key, value);
	}

	/**
	 * 删除属性 key
	 */
	public void removeProperty(String... key) {
		BaseAppConfig.getAppConfig(this).remove(key);
	}

	/**
	 * 判断key有没有 value值
	 */
	public boolean containsProperty(String key) {
		Properties props = getProperties();
		return props.containsKey(key);
	}

	// --------

	/**
	 * 获取对于action的缓存时间
	 * 
	 * @return
	 */
	public long getActionCacheTime(String action) {
		String cache_time = getProperty(action);
		return StringUtils.toLong(cache_time);
	}

	/**
	 * 设置对于action的缓存时间
	 * 
	 * @param b
	 */
	public void setActionCacheTime(String action, String b) {
		setProperty(action, String.valueOf(b));
	}

	/**
	 * 判断缓存数据是否可读
	 * 
	 * @param cachefile
	 * @return
	 * @throws ParseException
	 */
	public boolean isReadDataCache(String cachefile) throws ParseException {
		if (readObject(cachefile) == null) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 判断缓存是否失效
	 * 
	 * @param cachefile
	 * @return
	 * @throws ParseException
	 */
	public boolean isCacheDataFailure(String cachefile) throws ParseException {
		boolean failure = false;
		// 读取当前缓存的创建时间，和系统时间
		File data = getFileStreamPath(cachefile);

		long sys_time = System.currentTimeMillis();
		long file_time = data.lastModified();
		// System.out.println("当前系统时间：----------" + sys_time);
		// System.out.println("文件创建时间：----------" + file_time);
		if (data.exists() && (sys_time - file_time) > CACHE_TIME)
			failure = true;
		else if (!data.exists())
			failure = true;
		return failure;
	}

	/**
	 * 判断缓存是否存在
	 * 
	 * @param cachefile
	 * @return
	 */
	public boolean isExistDataCache(String cachefile) {
		boolean exist = false;
		File data = getFileStreamPath(cachefile);
		if (data.exists())
			exist = true;
		return exist;
	}

	/**
	 * 清除缓存目录
	 * 
	 * @param dir
	 *            目录
	 * @param numDays
	 *            当前系统时间
	 * @return
	 */
	public int clearCacheFolder(File dir, long curTime) {
		int deletedFiles = 0;
		if (dir != null && dir.isDirectory()) {
			try {
				for (File child : dir.listFiles()) {
					if (child.isDirectory()) {
						deletedFiles += clearCacheFolder(child, curTime);
					}
					if (child.lastModified() < curTime) {
						if (child.delete()) {
							deletedFiles++;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return deletedFiles;
	}

	/**
	 * 保存对象
	 * 
	 * @param ser
	 * @param file
	 * @throws IOException
	 */
	public boolean saveObject(Serializable ser, String file) {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = openFileOutput(file, MODE_PRIVATE);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(ser);
			oos.flush();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				oos.close();
			} catch (Exception e) {
			}
			try {
				fos.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 读取对象
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public Serializable readObject(String file) {
		if (!isExistDataCache(file))
			return null;
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = openFileInput(file);
			ois = new ObjectInputStream(fis);
			return (Serializable) ois.readObject();
		} catch (FileNotFoundException e) {
		} catch (Exception e) {
			e.printStackTrace();
			// 反序列化失败 - 删除缓存文件
			if (e instanceof InvalidClassException) {
				File data = getFileStreamPath(file);
				data.delete();
			}
		} finally {
			try {
				ois.close();
			} catch (Exception e) {
			}
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
		return null;
	}

	// 缓存相关-------------------------------------------------------------------------结束-
}
