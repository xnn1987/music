package com.allen.music;

/**
 * 接口URL实体类
 * 
 * @version 1.0
 * @created 2012-3-21
 */
public class AppUrls {

	// http://musicplay.kmusick.com/doSoap.php?wsdl

	// 命名空间
	public static final String nameSpace = "http://musicplay.kmusick.com/";
	// EndPoint
	public static final String endPoint = "http://musicplay.kmusick.com/server.php";
	
	public static final String service = nameSpace;
	// SD卡
	public static final String sdcardPath = "/mnt/sdcard/anka/";
	// 图片缓存
	public static final String Crash_Pictrue_file = sdcardPath + "cache/pictrue/";
	// 拍照缓存
	public static final String Crash_Photo_file = sdcardPath + "cache/photo/";
	// 日志缓存
	public static final String Crash_Log_file = sdcardPath + "cache/log/";
}
