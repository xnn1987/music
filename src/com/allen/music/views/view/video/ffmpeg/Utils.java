/*
* Copyright (c) 2012,上海不言科技发展有限公司 http://www.buyantech.com
* All rights reserved.
*
* 文件名称:Utils.java
* 摘   要:通用操作的工具类
*
* 当前版本:1.0
* 作   者:董亮
* 完成日期:2012年10月16日
*/
package com.allen.music.views.view.video.ffmpeg;

import java.io.File;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaRecorder.AudioSource;
import android.util.Log;
/**
 * 通用工具类
 * @author albert
 *
 */
public class Utils {
	private static final String Tag = "Utils";
	public static int puEncodingFormat = 1;
	public static int puContainerFormat = 1;
	public static int puResolutionChoice = 1;
	public static int puMute = 0;
	public static int puSplitter =0;
	public static int puTime=0;
	
	/**
	 * 手机静音
	 * @param context
	 * @param mute
	 */
	public static void muteSound(Context context,boolean mute){
		 AudioManager mgr = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
		    mgr.setStreamMute(AudioManager.STREAM_SYSTEM, mute);
		}
	/**
	 * 检查文件夹是否存在，不存在则创建
	 * @param _path
	 */
	public static void createDirIfNotExist(String _path) {
		File lf = new File(_path);
		try {
			if (lf.exists()) {
				//directory already exists
			} else {
				if (lf.mkdirs()) {
					Log.v(Tag, "createDirIfNotExist created " + _path);
				} else {
					Log.v(Tag, "createDirIfNotExist failed to create " + _path);
				}
			}
		} catch (Exception e) {
			//create directory failed
			Log.v(Tag, "createDirIfNotExist failed to create " + _path);
		}
	}
	/**
	 * 从url中获取文件名
	 * @param fileUrl
	 * @return
	 */
	public static String getFileName2(String fileUrl) {
		if(fileUrl.endsWith("/"))
		{
			fileUrl = fileUrl.substring(0,fileUrl.length()-1);
		}
		int lastIndex = fileUrl.lastIndexOf("/");
		String fileName = fileUrl.substring(lastIndex+1);
		Log.d(Tag, "getFilename:"+fileName);
		return fileName;
	}
	
	
	private static int[] mSampleRates = new int[] { 8000, 11025,16000,24050, 22050, 44100 };
	/**
	 * 测试：检查手机是否支持相应AudioRecord格式
	 * @return
	 */
	public static AudioRecord findAudioRecord() {
	    for (int rate : mSampleRates) {
	        for (short audioFormat : new short[] { AudioFormat.ENCODING_PCM_16BIT }) {
	            for (short channelConfig : new short[] {AudioFormat.CHANNEL_CONFIGURATION_STEREO , AudioFormat.CHANNEL_CONFIGURATION_MONO ,AudioFormat.CHANNEL_IN_MONO }) {
	                try {
	                    Log.d(Tag, "Attempting rate " + rate + "Hz, bits: " + audioFormat + ", channel: "
	                            + channelConfig);
	                    int bufferSize = AudioRecord.getMinBufferSize(rate, channelConfig, audioFormat);
	                    Log.d(Tag, "buffer size:"+bufferSize);
	                    if (bufferSize != AudioRecord.ERROR_BAD_VALUE) {
	                        // check if we can instantiate and have a success
	                        AudioRecord recorder = new AudioRecord(AudioSource.MIC, rate, channelConfig, audioFormat, bufferSize);
	                        if (recorder.getState() == AudioRecord.STATE_INITIALIZED){
		                        Log.d(Tag, "------->Success!");
	                            return recorder;
	                        }
	                    }
	                } catch (Exception e) {
	                    Log.e(Tag, rate + "Exception, keep trying.",e);
	                }
	            }
	        }
	    }
	    return null;
	}
}