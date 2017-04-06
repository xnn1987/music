package com.allen.music.tools;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

public class Logger {
    public static int MAX_MESSAGE_SIZE = Integer.MAX_VALUE; // MUST < 2048 
    
	private static String debug;

	public static String getDebug() {
		return debug;
	}

	public static void setDebug(String debug) {
		Logger.debug = debug;
	}

	public static boolean isDebug() {
		if ("1".equals(getDebug())) {
			return true;
		}
		return false;
	}

    public static void v(String tag, String msg) {
        if (null != tag && null != msg) { 
			int line =(new Throwable()).getStackTrace()[1].getLineNumber(); 
			String method =(new Throwable()).getStackTrace()[1].getMethodName(); 
            if (isDebug()) {   			
                Log.v(tag+" @Line"+line+":"+method+"()", msg);
            }
            Native.logV(tag+" @Line"+line+":"+method+"()", msg.length() > MAX_MESSAGE_SIZE ? msg.substring(0, MAX_MESSAGE_SIZE) : msg);   // @see log.c::MAX_MESSAGE_SIZE
        }
        else {
            Log.e(Logger.class.getName(), "Invalid parameters: " + tag + ", " + msg);
        }
    }

	public static void d(String tag, String msg) {
        if (null != tag && null != msg) { 
			int line =(new Throwable()).getStackTrace()[1].getLineNumber(); 
			String method =(new Throwable()).getStackTrace()[1].getMethodName(); 
    		if (isDebug()) {  			
                Log.d(tag+" @Line"+line+":"+method+"()", msg);
    		}
    		Native.logD(tag+" @Line"+line+":"+method+"()", msg.length() > MAX_MESSAGE_SIZE ? msg.substring(0, MAX_MESSAGE_SIZE) : msg);   // @see log.c::MAX_MESSAGE_SIZE
        }
        else {
            Log.e(Logger.class.getName(), "Invalid parameters: " + tag + ", " + msg);
        }
	}

    public static void i(String tag, String msg) {
        if (null != tag && null != msg) { 
			int line =(new Throwable()).getStackTrace()[1].getLineNumber(); 
			String method =(new Throwable()).getStackTrace()[1].getMethodName(); 
            if (isDebug()) { 			
                Log.i(tag+" @Line"+line+":"+method+"()", msg);
            }
            Native.logI(tag+" @Line"+line+":"+method+"()", msg.length() > MAX_MESSAGE_SIZE ? msg.substring(0, MAX_MESSAGE_SIZE) : msg);   // @see log.c::MAX_MESSAGE_SIZE
        }
        else {
            Log.e(Logger.class.getName(), "Invalid parameters: " + tag + ", " + msg);
        }
    }

    public static void w(String tag, String msg) {
        if (null != tag && null != msg) { 
			int line =(new Throwable()).getStackTrace()[1].getLineNumber(); 
			String method =(new Throwable()).getStackTrace()[1].getMethodName(); 
            if (isDebug()) {   			
                Log.w(tag+" @Line"+line+":"+method+"()", msg);
            }
            Native.logW(tag+" @Line"+line+":"+method+"()", msg.length() > MAX_MESSAGE_SIZE ? msg.substring(0, MAX_MESSAGE_SIZE) : msg);   // @see log.c::MAX_MESSAGE_SIZE
        }
        else {
            Log.e(Logger.class.getName(), "Invalid parameters: " + tag + ", " + msg);
        }
    }

	public static void e(String tag, String msg) {
        if (null != tag && null != msg) { 
			int line =(new Throwable()).getStackTrace()[1].getLineNumber(); 
			String method =(new Throwable()).getStackTrace()[1].getMethodName(); 
    		if (isDebug()) { 			
                Log.e(tag+" @Line"+line+":"+method+"()", msg);
    		}
    		Native.logE(tag+" @Line"+line+":"+method+"()", msg.length() > MAX_MESSAGE_SIZE ? msg.substring(0, MAX_MESSAGE_SIZE) : msg);    // @see log.c::MAX_MESSAGE_SIZE
        }
        else {
            Log.e(Logger.class.getName(), "Invalid parameters: " + tag + ", " + msg);
        }
	}
	
	public static void ex(String tag, Throwable e) {
	    if (null != tag && null != e) {
    	    StringWriter sw = new StringWriter();
    	    PrintWriter pw = new PrintWriter(sw);
    	    e.printStackTrace(pw); // Output to log file
    	    e.printStackTrace();   // Output to android log system.
    	    String s = sw.toString();
    	    
			int line =(new Throwable()).getStackTrace()[1].getLineNumber(); 
			String method =(new Throwable()).getStackTrace()[1].getMethodName(); 
    	    
    	    Native.logE(tag+" @Line"+line+":"+method+"()", s.length() > MAX_MESSAGE_SIZE ? s.substring(0, MAX_MESSAGE_SIZE) : s);    // @see log.c::MAX_MESSAGE_SIZE
	    }
        else {
            Log.e(Logger.class.getName(), "Invalid parameters: " + tag + ", " + e);
        }
	}

    public static void memo(Context context, String tag, Throwable ex) {
        if (null != context && null != tag) {
            Logger.i(tag, "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++" + System.currentTimeMillis());
            try { 
                Logger.i(tag, "Android " + Build.VERSION.RELEASE + " " + Build.VERSION.SDK_INT);
                PackageManager pm = context.getPackageManager();  
                PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);  
                if (pi != null) {  
                    String versionName = pi.versionName == null ? "null" : pi.versionName;  
                    Logger.i(tag, context.getPackageName() + " " + versionName);
                }  
                Field[] fields = Build.class.getDeclaredFields();  
                for (Field field : fields) {  
                    field.setAccessible(true);  
                    Logger.i(tag, field.getName() + " = " + field.get(null));  
                }  
            } 
            catch (Exception e) {  
                Logger.ex(tag, e);  
            }
            if (null != ex) {
                Logger.ex(tag, ex);
            }
            Logger.i(tag, "-----------------------------------------------------------" + System.currentTimeMillis());
        }
        else {
            Log.e(Logger.class.getName(), "Invalid parameters: " + context + ", " + tag + ", " + ex);
        }
    }
}
