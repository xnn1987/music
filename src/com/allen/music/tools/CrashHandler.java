package com.allen.music.tools;

import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import com.allen.music.R;

/**
 * Singleton crash handler class.
 * It outputs exception to log file and display customized crash message dialog.
 */
public class CrashHandler implements UncaughtExceptionHandler {
    public static CrashHandler getInstance(Context context) {
        if (null == instance) {
            instance = new CrashHandler();
            instance.context = context;
            instance.systemHandler = Thread.getDefaultUncaughtExceptionHandler();
            Thread.setDefaultUncaughtExceptionHandler(instance);
        }
        return instance;        
    }

    
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        ex.printStackTrace();
        
        if (!handle(ex) && null != systemHandler) {
            systemHandler.uncaughtException(thread, ex);
        }
        else {
            try {
                Thread.sleep(3000);                
            }
            catch (InterruptedException e) {
                Logger.ex(getClass().getName(), e);
            }
            
            boolean killSafely = true;
            if (killSafely) {
                // Notify the system to finalize and collect all objects of the app on exit so that the virtual machine running the app can be killed
                // by the system without causing issues. NOTE: If this is set to true then the virtual machine will not be killed until all of its threads have closed.
                System.runFinalizersOnExit(true);

                // Force the system to close the app down completely instead of retaining it in the background. The virtual machine that runs the
                // app will be killed. The app will be completely created as a new app in a new virtual machine running in a new process if the user
                // starts the app again.
                System.exit(0);
            } 
            else {
                 // Alternatively the process that runs the virtual machine could be abruptly killed. This is the quickest way to remove the app from
                 // the device but it could cause problems since resources will not be finalized first. For example, all threads running under the
                 // process will be abruptly killed when the process is abruptly killed. If one of those threads was making multiple related
                 // changes to the database, then it may have committed some of those changes but not all of those changes when it was abruptly killed.
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }
    }

    private CrashHandler() {
    }
    
    private boolean handle(Throwable ex) {
        boolean retVal = false;
        if (null != ex) {
            new Thread() {
                @Override
                public void run() {
                    Looper.prepare();
                    Toast.makeText(context, R.string.text_crash, Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
                
            }.start();
            
            Logger.memo(context, getClass().getName(), ex);
        }
        return retVal;        
    }

    
    private static CrashHandler instance = null;    // Singleton instance.
    private Context context = null;                 // Application context.
    private Thread.UncaughtExceptionHandler systemHandler = null;   // Backup android system handler.
}
