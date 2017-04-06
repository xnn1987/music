package com.allen.music.views.base;

import java.io.EOFException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.ksoap2.transport.HttpResponseException;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.allen.music.R;

/**
 * 应用程序异常类：用于捕获异常和提示错误信息
 * 
 * @version 1.0
 * @created 2012-3-21
 */
public class BaseAppException extends Exception implements UncaughtExceptionHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String TAG = "AppException";

	/** 定义异常类型 */
	public final static byte TYPE_NETWORK = 0x01;
	public final static byte TYPE_SOCKET = 0x02;
	public final static byte TYPE_HTTP_CODE = 0x03;
	public final static byte TYPE_HTTP_ERROR = 0x04;
	public final static byte TYPE_JSON = 0x05;
	public final static byte TYPE_IO = 0x06;
	public final static byte TYPE_REQUEST = 0x07;
	public final static byte TYPE_RUN = 0x08;

	private byte type;
	private int code;

	/** 系统默认的UncaughtException处理类 */
	private Thread.UncaughtExceptionHandler mDefaultHandler;

	private BaseAppException() {
		this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
	}

	private BaseAppException(byte type, int code, Exception excp) {
		super(excp);
		this.type = type;
		this.code = code;
	}

	/**
	 * 获取APP异常崩溃处理对象
	 * 
	 * @param context
	 * @return
	 */
	public static BaseAppException getAppExceptionHandler() {
		return new BaseAppException();
	}

	public int getCode() {
		return this.code;
	}

	public int getType() {
		return this.type;
	}

	public static BaseAppException network(Exception e) {
		if (e instanceof UnknownHostException || e instanceof ConnectException) {
			return new BaseAppException(TYPE_NETWORK, 0, e);
		} else if (e instanceof SocketException) {
			return socket(e);
		}
		return http(e);
	}

	public static BaseAppException http(int code) {
		return new BaseAppException(TYPE_HTTP_CODE, code, null);
	}

	public static BaseAppException http(Exception e) {
		return new BaseAppException(TYPE_HTTP_ERROR, 0, e);
	}

	public static BaseAppException socket(Exception e) {
		return new BaseAppException(TYPE_SOCKET, 0, e);
	}

	public static BaseAppException io(Exception e) {
		if (e instanceof UnknownHostException || e instanceof ConnectException) {
			return new BaseAppException(TYPE_NETWORK, 0, e);
		} else if (e instanceof IOException) {
			return new BaseAppException(TYPE_IO, 0, e);
		} else if (e instanceof EOFException || e instanceof HttpResponseException) {
			return new BaseAppException(TYPE_SOCKET, 0, e);
		} else if (e instanceof XmlPullParserException) {
			return new BaseAppException(TYPE_JSON, 0, e);
		}
		return run(e);
	}

	public static BaseAppException json(Exception e) {
		return new BaseAppException(TYPE_JSON, 0, e);
	}

	public static BaseAppException request(Exception e) {
		return new BaseAppException(TYPE_REQUEST, 0, e);
	}

	public static BaseAppException run(Exception e) {
		return new BaseAppException(TYPE_RUN, 0, e);
	}

	/**
	 * 提示友好的错误信息
	 * 
	 * @param ctx
	 */
	public void makeToast(Context ctx) {
		switch (this.getType()) {
		case TYPE_NETWORK:
			Toast.makeText(ctx, R.string.network_not_connected, Toast.LENGTH_SHORT).show();
			break;
		case TYPE_HTTP_ERROR:
			Toast.makeText(ctx, R.string.http_exception_error, Toast.LENGTH_SHORT).show();
			break;
		case TYPE_HTTP_CODE:
			String err = ctx.getString(R.string.http_status_code_error, this.getCode());
			Toast.makeText(ctx, err, Toast.LENGTH_SHORT).show();
			break;
		case TYPE_SOCKET:
			Toast.makeText(ctx, R.string.socket_exception_error, Toast.LENGTH_SHORT).show();
			break;
		case TYPE_JSON:
			Toast.makeText(ctx, R.string.json_parser_failed, Toast.LENGTH_SHORT).show();
			break;
		case TYPE_IO:
			Toast.makeText(ctx, R.string.io_exception_error, Toast.LENGTH_SHORT).show();
			break;
		case TYPE_RUN:
			Toast.makeText(ctx, R.string.app_run_code_error, Toast.LENGTH_SHORT).show();
			break;
		}
	}

	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 * */
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				Log.e(TAG, "error : ", e);
			}
			// 退出程序
			final Context context = BaseAppManager.getAppManager().currentActivity();
			BaseAppManager.getAppManager().AppExit(context);
		}

	}

	/**
	 * 自定义异常处理:收集错误信息&发送错误报告
	 * 
	 * @param ex
	 * @return true:处理了该异常信息;否则返回false
	 */
	private boolean handleException(final Throwable ex) {
		if (ex == null) {
			return false;
		}
		final Context context = BaseAppManager.getAppManager().currentActivity();
		if (context == null) {
			return false;
		}
		// 显示异常信息
		new Thread() {
			public void run() {
				Looper.prepare();
				// Toast.makeText(context, "很抱歉,未知异常错误,即将关闭",
				// Toast.LENGTH_SHORT).show();
				getAppCrashReport(context, ex);
				Looper.loop();
			}
		}.start();

		sendAppCrashReport(context, ex);

		return true;
	}

	/**
	 * 发送App异常崩溃报告
	 * 
	 * @param cont
	 * @param crashReport
	 */
	public static void getAppCrashReport(final Context cont, final Throwable ex) {
		AlertDialog.Builder builder = new AlertDialog.Builder(cont);
		builder.setTitle("程序异常");
		builder.setMessage("亲，很抱歉，未知异常。请将问题报告发送给我们，协助我们优化产品和服务!");
		builder.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// 发送信息
				sendAppCrashReport(cont, ex);

				// 重启
				Intent i = ((Activity) cont).getBaseContext().getPackageManager().getLaunchIntentForPackage(((Activity) cont).getBaseContext().getPackageName());
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				cont.startActivity(i);

				// 退出
				BaseAppManager.getAppManager().AppExit(cont);
			}
		});
		builder.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// 退出
				BaseAppManager.getAppManager().AppExit(cont);
			}
		});
		builder.show();
	}

	/**
	 * 获取设备编号-以及异常信息
	 * 
	 * @param ex
	 * @return
	 */
	public static void sendAppCrashReport(Context context, Throwable ex) {
		PackageInfo pinfo = ((BaseAppContext) context.getApplicationContext()).getPackageInfo();
		StringBuffer sb = new StringBuffer();
		// 常规信息
		sb.append("软件版本:" + pinfo.versionName + "(" + pinfo.versionCode + ")");
		sb.append("安卓版本:" + android.os.Build.VERSION.RELEASE + "(" + android.os.Build.MODEL + ")");

		// 异常时间
		String time = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
		sb.append("--------------------" + time + "---------------------" + "\n");

		// 异常内容
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		sb.append("错误日志:" + result);

		System.out.println(sb.toString());
	}
}
