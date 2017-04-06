/*
* Copyright (c) 2012,TerenceYang
* All rights reserved.
*
* AppConfig.java
* Summary: application common configuration
*
*Version:1.0
*@author: TerenceYang
* @Date: 2012-10-10
*/
package com.allen.music.views.view.video.ffmpeg;

import java.io.File;

import com.allen.music.AppContext;

/**
 * 通用设置静态变量
 * @author Terence
 *
 */
public class VideoConfig {
	/**
	 * 远程服务器地址
	 */
	public static final String HOST = "http://172.16.16.54/";
	/**
	 * 文件录制的手机缓存路径
	 */
	public static final String PHOTO_CAPTURE_FOLDER = AppContext.getProductFolder() + File.separatorChar ;
	/**
	 * 文件上传地址
	 */
	public static final String UPLOAD_URL =  HOST+"/VideoTransfer/Default.aspx";
	/**
	 * 媒体缓存在sdcard卡的路径位置
	 */
	public static final String PLAY_CACHE =  AppContext.getProductFolder() + File.separatorChar+ "playcache/";

	/**
	 * 默认播放文件地址(方便测试）
	 */
	public static final String DEFAULT_PLAY_URL = HOST+"/VideoTransfer/uploads/1350444111942.m3u8";
	/**
	 * 默认播放文件地址
	 */
	public static String PLAY_URL = "";
	
	//临时播放文件地址 
	public static String TMPVIDEO_URL="";

	
}
