package com.allen.music.views.view.video.ffmpeg;
/*
* Copyright (c) 2012,上海不言科技发展有限公司 http://www.buyantech.com
* All rights reserved.
*
* 文件名称:FFmpeg.java
* 摘   要:FFMPEG 音视频解码器封装类
*
* 当前版本:1.0
* 作   者:董亮
* 完成日期:2012年10月16日
*/


import java.util.Vector;

import android.graphics.Bitmap;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;
/**
 * FFMpeg媒体文件播放jni封装类
 */
public class FFmpeg {
	
	private static final String Tag = "FFMPEG_TAG";
    int sampleRate=44100;

	/**
	 * 单件类对象实体
	 */
	private static FFmpeg singleton;
	/**
	 * 自定义类，用于存放帧类型
	 * 1-视频 2-音频
	 */
	public class FrameType{
		private int value;  
	    public FrameType() { this(0); }  
	    public FrameType(int value) { this.value = value; }  
	    public int getValue() { return value; }  
	    public void setValue(int value) { this.value = value; }  
	    public byte[] audioData;
	    public void setAudio(byte[] data){
	    	audioData = data;
	    }
	}
	/**
	 * 当前解码的帧类型
	 */
	public FrameType currentFrameType = new FrameType();
	/**
	 * 加载jni库
	 */
	static {
		System.loadLibrary("ffmpeg");
		System.loadLibrary("ffmpegrecorder");
    }
	
	/**
	 * 用于测试: hello world
	 * @return
	 */
	public native static String stringFromJNI();
	/**
	 * 用于测试: test if global variables shared value in different Java calls
	 */
	public native void nativeTest();
	/**
	 * 用于测试: test. //ok
	 * @return
	 */
	public native int[] jniIntArray();
	/**
	 * 用于测试:播放
	 * @param filePath 播放地址路径
	 * @return
	 */
	public native String play(String filePath);
	
	// ffmpeg api
	private native void avRegisterAll();
	private native boolean avOpenInputFile(String filePath);
	private native boolean avFindStreamInfo();
	private native boolean findVideoStream();
	private native boolean avcodecFindDecoder();
	private native boolean avcodecOpen();
	private native void avcodecAllocFrame();
	private native void avFree();	  
	private native void avcodecClose();	  
	private native void avCloseInputFile();
	
	// functional call
	public native String getCodecName();
	public native int getWidth();
	public native int getHeight();
	public native int getBitRate();
	public native boolean allocateBuffer();
	/**
	 * 设置解码后视频显示高度和宽度
	 * @param sWidth 宽度
	 * @param sHeight 高度
	 */
	public native void setScreenSize(int sWidth, int sHeight);
	/**
	 * 音视频解码
	 * @param type 音频或视频
	 * @param bitmap 视频解码结果
	 * @return
	 */
	public native byte[] getNextDecodedFrame(FrameType type,Bitmap bitmap);	

	/**
	 * 构造函数，注册编码器
	 */
	private FFmpeg() {
		avRegisterAll();
	}
	/**
	 * ffmpeg控制的单例类，用于全局访问
	 */
	public static FFmpeg getInstance() {
		if( singleton == null )
			singleton = new FFmpeg();
		
		return singleton;
	}
	
	/**
	 * 打开并初始化解码器
	 */
	private boolean init(String filePath) {
        if( avOpenInputFile(filePath) )
        	Log.i("ff", "success open");
        else {
        	Log.i("ff", "failed open");
        	return false;
        }
        
        if( avFindStreamInfo() )
        	Log.i("ff", "success find stream info");
        else {
        	Log.i("ff", "failed find stream info");
        	return false;
        }
        
        if( findVideoStream() )
        	Log.i("ff", "success find stream");
        else {
        	Log.i("ff", "failed find stream");
        	return false;
        }
        
        if( avcodecFindDecoder() )
        	Log.i("ff", "success find decoder");
        else {
        	Log.i("ff", "failed find decoder");
        	return false;
        }
        
    	if( avcodecOpen() )
    		Log.i("ff", "success codec open");
        else {
        	Log.i("ff", "failed codec open");
        	return false;
        }    	
    	Log.i("ff", getCodecName());
    	
    	return true;
	}
	/**
	 * 清理缓存及关闭解码器
	 */
	public void cleanUp() {
		Log.d(Tag, "begin clean up");
		avFree();	  
		avcodecClose();	  
		avCloseInputFile();
		if(track!=null){
			track.release();
			track =null;
		}
		Log.d(Tag, "end clean up");
	}

	/**
	 * 打开文件准备播放
	 */
	public boolean openFile(String filePath) {
		return init(filePath);
	}
	/**
	 * 通过读取媒体文件的编码器来识别音、视频文件
	 * 如果编码器为空，说明是纯音频文件
	 */
	public boolean isMediaFile(String filePath) {
		avRegisterAll();
        if( avOpenInputFile(filePath) ) {
        	avCloseInputFile();
        	return true;
        }
        else
        	return false;
	}

	/**
	 * 准备解码缓冲区
	 */
	public void prepareResources() {
		avcodecAllocFrame();
    	allocateBuffer();
	}
	private AudioTrack track = null;
	/*-----------------音频播放开始－－－－－－－－－－－－－－－－－－－－－－－－－－－*/
	/**音频数据缓存序列*/
	private Vector<byte[]> _Frames = new Vector<byte[]>();
	/**
	 * 将音频数据添加到播放缓冲中
	 * @param soundData
	 * @param dataLength
	 */
	public void playSound(byte[] soundData,int dataLength){		
		if(soundData!=null)
			_Frames.add(soundData);
	}
	/**
	 * 打开手机音频输出设备，等待音频数据写入
	*/
	public void startAudioPlay() {
		if(track==null){
			int minBufferSize = AudioTrack.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT);
		    Log.d(Tag, "audio track size:"+minBufferSize);
		    track = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, minBufferSize, AudioTrack.MODE_STREAM);
		    if(track.getPlayState()!=AudioTrack.PLAYSTATE_PLAYING)
	            track.play();
		}
	}
	/**
	 * 将缓冲中音频数据播出
	 */
	public void audioOut() {
		if(track!=null && _Frames.size()>0){
			byte[] soundData = _Frames.remove(0);
			track.write(soundData, 0, soundData.length);
		}
	}
	/**
	 * 直接音频输出(低效）
	 */
	public void audioOut(byte[] soundData){
		//Log.d(Tag, "native call play sound2:"+soundData.length+" frames:"+_Frames.size());
		if(track!=null && soundData.length>0){
			track.write(soundData, 0, soundData.length);
		}
		
	}
	/**
	 * 检查缓冲区数量，看是否可以播放
	 * @return
	 */
	public boolean audioAviable() {
		return _Frames.size()>0;
	}
	/*-----------------音频播放结束－－－－－－－－－－－－－－－－－－－－－－－－－－－*/

	
	/**-----------------回调接口开始－－－－－－－－－－－－－－－－－－－－－－－－－－－*/
	/**
	 * 播放完成的回调接口
	 * 文件解码结束后调用此接口，通知前台停止
	 */
	public interface FFmpegPlayInterface{
		public void playStopped();
	}
	private FFmpegPlayInterface playInterface;
	public void setPlayInterface(FFmpegPlayInterface interfac){
		playInterface = interfac;
	}
	/**
	 * 全部文件播放结否
	 */
	public void playComplete(){
//		Log.d(Tag, "play finished:");
		if(playInterface!=null)
			playInterface.playStopped();
	}
	/**-----------------回调接口结束－－－－－－－－－－－－－－－－－－－－－－－－－－－*/

}
