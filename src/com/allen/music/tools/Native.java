package com.allen.music.tools;

/**
 * Native methods delegate, which is implemented in C library.
 */

public class Native {
    
	public static native void logInitialize(String path, char separator);
	
	public static native void logDestroy();
	
    public static native void logV(String tag, String message);

    public static native void logD(String tag, String message);
	
	public static native void logI(String tag, String message);

	public static native void logW(String tag, String message);
	
	public static native void logE(String tag, String message);

    public static native void logSetMaxFilesize(int size);
    public static native void logSetLogLevel(int level);
	
	static {
        System.loadLibrary("music");
	}
}
