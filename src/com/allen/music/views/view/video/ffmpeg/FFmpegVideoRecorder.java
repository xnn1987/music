package com.allen.music.views.view.video.ffmpeg;
/*
* Copyright (c) 2012,上海不言科技发展有限公司 http://www.buyantech.com
* All rights reserved.
*
* 文件名称:FFmpegVideoRecorder.java
* 摘   要:FFMPEG 音视频编码器封装类
*
* 当前版本:1.0
* 作   者:董亮
* 完成日期:2012年10月16日
*/


import java.io.File;

import android.text.TextUtils;

import com.allen.music.tools.Logger;

/**
Albert Dong
 */
public class FFmpegVideoRecorder {
	private VideoParam param = null;
	private File file;/**文件操作句柄*/

	
	/**
	 * 编码开始标识
	 */
	private boolean encoderStarted;
	/**
	 * 构造函数
	 */
	public FFmpegVideoRecorder() {

	}
	/**
	 * 构造函数
	 * @param file 文件读写句柄
	 * @param width 视频大小之宽度
	 * @param height 视频大小之高度
	 */
	public FFmpegVideoRecorder(File file, VideoParam param) {
		this.file = file;
		this.param = param;
	}
	/**
	 * 初始化编码器
	 * @param filename 要写入的文件名
	 * @param videoBitRate 视频码率
	 * @param frameRate 帧率
	 * @param audioRate 音频码率
	 * @param sampleRate 音频采样率
	 * @param audioChannel 音频声道数
	 * @param width 视频宽度
	 * @param height 视频高度
	 * @param mute 1-静音 2－只录音频 0-全录
	 * @return 编码器（未使用）
	 */
	public native long staticInitialize(String filename, int videoBitRate, int frameRate, 
			int audioRate, int sampleRate, int audioChannel, int width, int height, int mute);
	/**
	 * 压缩视频
	 * @param in 原始帧数据
	 * @param insize 数据大小
	 * @return 1：成功 -1：失败
	 */
	public native int CompressVideo(byte[] in, int insize);
	/**
	 * 编码音频
	 * @param in 原始音频数据
	 * @param insize 音频数据大小
	 * @return 1：成功 -1：失败
	 */
	public native int CompressAudio(short[] in, int insize);
	/**
	 * 编码音频
	 * @param in 原始音频数据
	 * @param insize 音频数据大小
	 * @return 1：成功 -1：失败
	 */
	public native int CompressAudio2(byte[] in, int length);
	/**
	 * 编码结束，写入编码格式到文件
	 * @return
	 */
	private native int CompressEnd();

	
	/** 
	 * 需要先设置参数再调用此方法
	 */
	public void start() {
		try {
			long ret = staticInitialize(this.file.getPath(), param.videoBitrate, param.frameRate, param.sampleRate, param.sampleRate, param.audioChannels, param.width, param.height, param.mute);
			Logger.d(getClass().getName(), String.format("start: result %d, %s", ret, param));
			encoderStarted = ret > 0;
		} catch (Exception e) {
			Logger.ex(getClass().getName(), e);
		}
	}

	/**
	 *  需要先设置参数再调用此方法
	 */
	public void start(String filePath) {
		try {
	        if (!TextUtils.isEmpty(filePath)) {
    			long ret = staticInitialize(filePath, param.videoBitrate, param.frameRate, param.sampleRate, param.sampleRate, param.audioChannels, param.width, param.height, param.mute);
                Logger.d(getClass().getName(), String.format("start: result %d, %s", ret, param));
    			encoderStarted = ret > 0;
	        }
		} catch (Exception e) {
            Logger.ex(getClass().getName(), e);
		}
	}
	/**
	 * 停止录制
	 */
	public void stop() {
		CompressEnd();
	}
	/**
	 * 音频编码
	 * @param data
	 */
	public void recordAudio(short[] data) {
		if(param.mute==1) {
			Logger.d(getClass().getName(), "Mute short Audio data");
		}
		else {
		    CompressAudio(data, data.length);
		}
	}
	/**
	 * 音频编码
	 * @param data
	 */
	public void recordAudio(byte[] data) {
		if(param.mute==1) {
			Logger.d(getClass().getName(), "Mute byte Audio");
		}
		else {
		    CompressAudio2(data, data.length);
		}
	}
	/**
	 * 编码图像
	 * @param 图像数据
	 */
	public void record(byte[] previewPicture) {
		if (null != previewPicture && 2 != param.mute) {
		    int result = CompressVideo(previewPicture, previewPicture.length);
		}
	}

	/**
	 * 加载jni库
	 */
	static {
		try {
			System.loadLibrary("ffmpeg");
			System.loadLibrary("ffmpegrecorder");
		} 
		catch (Exception ex) {
			Logger.ex(FFmpegVideoRecorder.class.getName(), ex);
		}
	}
}
