package com.allen.music.views.view.video;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder.AudioSource;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.allen.music.AppConfig;
import com.allen.music.R;
import com.allen.music.tools.DateUtils;
import com.allen.music.tools.Logger;
import com.allen.music.views.base.BaseActivity;
import com.allen.music.views.view.video.ffmpeg.FFmpegVideoRecorder;
/**
 * 音视频录制Activity
 * @author Terence
 *
 */
public class VideoRecorderActivity1 extends BaseActivity implements SurfaceHolder.Callback{
//	private SurfaceView prSurfaceView;
//	private Button prStartBtn;
//	private Button prSettingsBtn;
//	private boolean prRecordInProcess;
//	private SurfaceHolder prSurfaceHolder;
//	private Camera prCamera;
////	private final String cVideoFilePath = VideoConfig.PHOTO_CAPTURE_FOLDER;
//
//	private final String Tag = "VideoRecorderActivity";
//	private Context prContext;
//
//	private volatile ArrayList<String> toUploadPath = new ArrayList<String>();
//	private int splitCounter = 0;
//	/**
//	 * 帧率
//	 */
//	private final int cFrameRate = 10;
//	/**
//	 * 码率
//	 */
//	private final int cBitrate = 512 * 1000; 
//	/**
//	 * 文件控制句柄
//	 */
//	private File prRecordedFile;
//	/**
//	 * 每个分片文件大小
//	 */
//	private int splitterSize = 10 *1024 * 1024; //file max length is 10M
//	/**
//	 * 实际音视频录制封装类
//	 */
//	private FFmpegVideoRecorder recorder;
//	/**
//	 * 分辨率宽度
//	 */
//	private int imageWidth  = 320;//
//	/**
//	 * 分辨率长度
//	 */
//	private int imageHeight = 240;//
//	/**
//	 * 音频采集
//	 */
//	private AudioRecord audioRecord;
//	/**
//	 * 音频编码线程
//	 */
//	private Thread audioView;
//	/**
//	 * 视频编号线程
//	 */
//	private Thread videoEncodeThread;
//	/**
//	 * 编码控制信号量
//	 */
//	private volatile boolean isRecorderStart = false;
//	/**
//	 * 音频采样率
//	 */
//	private int sampleAudioRateInHz = 22050;//
//	/**
//	 * 音频比特率
//	 */
//	private int sampleAudioBitRate = 96000;
//	/**
//	 * 声道数
//	 */
//	private int audioChannels = 1;
//	/**
//	 * 静音 0-no mute 1-mute sound 2-mute video
//	 */
//	private int mute = 0;
//	/**
//	 *视频录制时长 1分钟， 5分钟，10分钟；
//	 */
//	private int settingTime = 60;
//	/**
//	 * 音频编吗控制变量
//	 */
//	private static volatile boolean isAudioRecording = false;
//	/**
//	 * 缓存需要编码的视频图片变量
//	 */
//	private byte[] previewPicture;
//	/**
//	 * 录制的文件后缀名
//	 */
//	private String suffix = ".flv";
//	/**
//	 * 文件上传类
//	 */
//	private UploadImageTask uploaderTask;
//	/**
//	 * 视频录制计时器
//	 * **/
//	private Timer mVideoRecorderTimer;
//    private TimerTask mVideoRecorderTimerTask = null;  
//    private Handler mVideoRecorderHandler = null;      
//    private static int count = 0;  
//    private boolean isPause = false;  
//    private boolean isStop = true;  
//    private static int delay = 1000;  //1s  
//    private static int period = 1000;  //1s  
//    
//    private static final int UPDATE_TIMER = 0;  
//    private static final int STOP_RECORD = 1;  
//    
//    private TextView mTimerText;
//
	@Override
	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		prContext = this.getApplicationContext();
//		setContentView(R.layout.video_recorder);
//		Utils.createDirIfNotExist(cVideoFilePath);
//		prSurfaceView = (SurfaceView) findViewById(R.id.surface_camera);
//
//		mTimerText=(TextView)findViewById(R.id.btn_videorecord_timer);
//		prStartBtn = (Button) findViewById(R.id.main_btn1);
//		prSettingsBtn = (Button) findViewById(R.id.main_btn2);
//		prRecordInProcess = false;
//		prStartBtn.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//				if (prRecordInProcess == false) {
//					startVideoTimer();
//					startRecording();
//					// 用于测试
//					VideoConfig.PLAY_URL = "";
//				} else {
//					stopVideoTimer();
//					stopRecording();
//					Utils.muteSound(VideoRecorderActivity1.this, false);
//				}
//			}
//		});
//		prSettingsBtn.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//				
//				Intent intent = new Intent();
//				intent.setClass(prContext, SettingsDialog.class);
//				startActivityForResult(intent, REQUEST_DECODING_OPTIONS);
//			}
//		});
//		prSurfaceHolder = prSurfaceView.getHolder();
//		prSurfaceHolder.addCallback(this);
//		prSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//		
//		//Time Handler
//		mVideoRecorderHandler=new Handler(){
//
//			@Override
//			public void handleMessage(Message msg) {
//				 switch (msg.what) {  
//	                case UPDATE_TIMER:  
//	                	mTimerText.setText(getTimeString(count));
//	                    break;  
//	                case STOP_RECORD:  
//	                	stopVideoTimer();//stop video timer
//	                	stopRecording();//stop record video
//	                    break;  
//	                default:  
//	                    break;  
//	                }  
//			}			
//		};
//		
	}
//	
//	/**
//	 * Back to home when press BACK key.
//	 */
//	@Override
//	public boolean dispatchKeyEvent(KeyEvent event) {
//		if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {		
//			Intent intent = new Intent("VideoRecorder");
//			intent.putExtra("VideoPath", recordFilePath);
//			intent.putExtra("VideoLength", String.valueOf(count));
//			setResult(RESULT_OK,intent);
//		}
//		return super.dispatchKeyEvent(event);
//	}
//	
//	/** 视频预览回调
//	 * 
//	 */
//	private PreviewCallback previewCallback = new PreviewCallback() {
//		public synchronized void onPreviewFrame(byte[] data, Camera camera) {
//			previewPicture = data;
//			camera.addCallbackBuffer(data);
//		}
//	};
//
//	/**
//	 * 设置编码器参数并启动编码器
//	 */
//	private void startNativeRecorder() {
//		Logger.w(Tag, "init recorder");
//		if (recorder == null) {
//			recorder = new FFmpegVideoRecorder(prRecordedFile, imageWidth, imageHeight);
//			recorder.setAudioBitrate(sampleAudioBitRate);
//			recorder.setAudioChannels(audioChannels);
//			recorder.setAudioSampleRate(sampleAudioRateInHz);
//			recorder.setFrameRate(cFrameRate);
//			recorder.setVideoBitrate(cBitrate);
//			recorder.setMute(mute);
//		}
//		recorder.start();
//
//	}
//
//	// @Override
//	public void surfaceChanged(SurfaceHolder _holder, int _format, int _width, int _height) {
//
//	}
//	/**
//	 * 预览窗口创建
//	 */
//	public void surfaceCreated(SurfaceHolder arg0) {
//		try {
//			prCamera = Camera.open();
//		} catch (RuntimeException e) {
//			Logger.e("CameraTest", "Camera Open filed");
//			return;
//		}
//
//		Camera.Parameters parameters = prCamera.getParameters();
//		parameters.setPreviewFrameRate(cFrameRate);
//		parameters.setPreviewSize(imageWidth, imageHeight);
//		prCamera.setParameters(parameters);
//
//		try {
//			prCamera.setPreviewDisplay(prSurfaceHolder);
//		} catch (Throwable t) {
//			t.printStackTrace();
//		}
//		Logger.v("CameraTest", "Camera PreviewFrameRate = " + prCamera.getParameters().getPreviewFrameRate());
//		Size previewSize = prCamera.getParameters().getPreviewSize();
//		imageWidth = previewSize.width;
//		imageHeight = previewSize.height;
//
//		int dataBufferSize = (int) (previewSize.height * previewSize.width * (ImageFormat.getBitsPerPixel(prCamera.getParameters().getPreviewFormat()) / 8.0));
//		prCamera.addCallbackBuffer(new byte[dataBufferSize]);
//		prCamera.addCallbackBuffer(new byte[dataBufferSize]);
//		prCamera.addCallbackBuffer(new byte[dataBufferSize]);
//		prCamera.setPreviewCallbackWithBuffer(previewCallback);
//		try {
//			prCamera.startPreview();
//		} catch (Throwable e) {
//			e.printStackTrace();
//			prCamera.release();
//			prCamera = null;
//			return;
//		}
//	}
//	/**
//	 * 预览窗口销毁
//	 */
//	public void surfaceDestroyed(SurfaceHolder arg0) {
//		Logger.d(Tag, "surfaceDestroyed");
//		if (prRecordInProcess) {
//			stopRecording();
//		}
//		stopCameraPreview();
//		Utils.muteSound(VideoRecorderActivity1.this, false);
//	}
//	/**
//	 * 停止预览
//	 */
//	private void stopCameraPreview() {
//		Logger.d(Tag, "stopCameraPreview");
//		if (null == prCamera)
//			return;
//
//		try {
//			prCamera.stopPreview();
//			prCamera.setPreviewDisplay(null);
//			prCamera.setPreviewCallbackWithBuffer(null);
//			prCamera.release();
//		} catch (IOException e) {
//			e.printStackTrace();
//			return;
//		}
//		prCamera = null;
//	}
//	/**
//	 * 更新编码配置信息
//	 */
//	private void updateEncodingOptions() {
////		if(Utils.puMute == SettingsDialog.cpuMuteVideo)
////			mute = 2;
////		else if(Utils.puMute == SettingsDialog.cpuMuteAudio)
////			mute = 1;
////		else if(Utils.puMute == SettingsDialog.cpuMuteNo)
////			mute = 0;
////		
////		Logger.d(Tag, "setting mute:"+mute);
////		if(Utils.puSplitter==SettingsDialog.cpuSplitter500KB)
////			this.splitterSize = 500*1024;
////		else if(Utils.puSplitter==SettingsDialog.cpuSplitter200KB)
////			this.splitterSize = 200*1024;
////		else if(Utils.puSplitter==SettingsDialog.cpuSplitter100KB)
////			this.splitterSize = 100*1024;
////		else if(Utils.puSplitter==SettingsDialog.cpuSplitter50KB)
////			this.splitterSize = 50*1024;
//		
//		if(Utils.puTime == SettingsDialog.cpuTime1)
//			settingTime = 60;
//		else if(Utils.puTime == SettingsDialog.cpuTime5)
//			settingTime =5*60;
//		else if(Utils.puTime == SettingsDialog.cpuTime10)
//			settingTime = 10*60;
//		
//		Logger.d(Tag, "video setting Time:"+settingTime);
//		
//	}
//	/**
//	 * 开始录制
//	 * @return
//	 */
//	private String recordFilePath;
//	private boolean startRecording() {
//		Logger.d(Tag, "start recoding....");
//
////		final String randomName = String.valueOf(System.currentTimeMillis());
//		String fileName=DateUtils.getFileNameByDateTime();
//		recordFilePath=cVideoFilePath + fileName + suffix;
//		prRecordedFile = new File(recordFilePath);
//		this.prSettingsBtn.setEnabled(false);
//
//		startNativeRecorder();
//
//		audioView = new Thread(new AudioRecordThread());
//		audioView.start();
//
//		prRecordInProcess = true;
//		isRecorderStart = true;
//
//		videoEncodeThread = new Thread(new VideoRecordThread());
//		videoEncodeThread.start();
//
//		Utils.muteSound(this, true);
//		prStartBtn.setText("停止");
//
//		return true;
//	}
//	/**
//	 * 停止录制
//	 */
//	private void stopRecording() {
//		isRecorderStart = false;
//		isAudioRecording = false;
//		try {
//			if (prCamera != null) {
//				prCamera.reconnect();
//				prCamera.startPreview();
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		prStartBtn.setText("Start");
//		prRecordInProcess = false;
//		this.prSettingsBtn.setEnabled(true);
//
//	}
//	/**
//	 * 视频录制线程
//	 * @author albert
//	 *
//	 */
//	class VideoRecordThread implements Runnable {
//		public void run() {
//			while (isRecorderStart) {
//				if (recorder != null && previewPicture != null && isRecorderStart) {
//					Logger.v(Tag, "-----Call back of preview---");
//					recorder.record(previewPicture);
//				}
//			}
//		}
//
//	}
//	/**
//	 * 音频缓冲区大小
//	 */
//	int bufferLength = 0;
//
//	/**
// 	* 音频读取并编码线程
//	*/
//	class AudioRecordThread implements Runnable {
//		public void run() {
//			int bufferSize;
//			byte[] audioData;
//			int bufferReadResult;
//			try {
//				bufferSize = AudioRecord.getMinBufferSize(sampleAudioRateInHz, AudioFormat.CHANNEL_IN_DEFAULT, AudioFormat.ENCODING_PCM_16BIT);
//				Logger.d(Tag, "buffer size:"+bufferSize);
//				if (bufferSize <= 2048) {
//					bufferLength = 2048;
//				} else if (bufferSize <= 4096) {
//					bufferLength = 4096;
//				} else if(bufferSize<=8192){
//					bufferLength = 8192;
//				}
//				
//				Logger.d(Tag, "buffer length:"+bufferLength);
//				android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
//				audioRecord = new AudioRecord(AudioSource.MIC, sampleAudioRateInHz,AudioFormat.CHANNEL_IN_DEFAULT, AudioFormat.ENCODING_PCM_16BIT, bufferLength );
//				if(audioRecord ==null){
//					Logger.e(Tag, "cant init audio record");
//					return;
//				}
//				audioData = new byte[bufferLength];
//				audioRecord.startRecording();
//				Logger.d(Tag, "audioRecord.startRecording() bufferLength:" + bufferLength);
//
//				isAudioRecording = true;
//
//				/* ffmpeg_audio encoding loop */
//				while (isAudioRecording) {
//					if (isRecorderStart) {
//						bufferReadResult = audioRecord.read(audioData, 0, audioData.length);
//						Logger.v(Tag, "--------------bufferReadResult= " + bufferReadResult);
//						if (bufferReadResult == 2048 && prRecordInProcess) {
//							Logger.v(Tag, "bufferReadResult=2048");
//							recorder.recordAudio(audioData);
//						} else if (bufferReadResult == 4096 && prRecordInProcess) {
//							Logger.v(Tag, "bufferReadResult=4096");
//							byte[] realAudioData2048_1 = new byte[2048];
//							byte[] realAudioData2048_2 = new byte[2048];
//
//							System.arraycopy(audioData, 0, realAudioData2048_1, 0, 2048);
//							System.arraycopy(audioData, 2048, realAudioData2048_2, 0, 2048);
//							for (int i = 0; i < 2; i++) {
//								if (i == 0) {
//									recorder.recordAudio(realAudioData2048_1);
//									Logger.d(Tag, "encoded first part of 4096");
//								} else if (i == 1) {
//									recorder.recordAudio(realAudioData2048_2);
//									Logger.d(Tag, "encoded second part of 4096");
//								}
//							}
//						}
//						else if (bufferReadResult == 8192 && prRecordInProcess) {
//							Logger.v(Tag, "bufferReadResult=8192");
//							byte[] realAudioData2048_1 = new byte[2048];
//							byte[] realAudioData2048_2 = new byte[2048];
//							byte[] realAudioData2048_3 = new byte[2048];
//							byte[] realAudioData2048_4 = new byte[2048];
//
//							System.arraycopy(audioData, 0, realAudioData2048_1, 0, 2048);
//							System.arraycopy(audioData, 2048, realAudioData2048_2, 0, 2048);
//							System.arraycopy(audioData, 2048, realAudioData2048_3, 0, 2048);
//							System.arraycopy(audioData, 2048, realAudioData2048_4, 0, 2048);
//
//							
//							for (int i = 0; i < 4; i++) {
//								if (i == 0) {
//									recorder.recordAudio(realAudioData2048_1);
//								} else if (i == 1) {
//									recorder.recordAudio(realAudioData2048_2);
//								}else if(i==2)
//									recorder.recordAudio(realAudioData2048_3);
//								else if(i==3)
//									recorder.recordAudio(realAudioData2048_4);
//							}
//						}
//						
//						Logger.d(Tag, "");
//						// create second video clip and upload to server
//						long fileLength = prRecordedFile.length();
//						if (fileLength >= splitterSize) {
//							recorder.stop();
//							oldFullFileName = prRecordedFile.getAbsolutePath();
//
//							String filename = prRecordedFile.getName();
//							splitCounter++;
//							String newFilename = filename.substring(0, filename.indexOf("_")) + "_" + splitCounter + suffix;
//							prRecordedFile = new File(cVideoFilePath + newFilename);
//							recorder.start(prRecordedFile.getAbsolutePath());
//
//							toUploadPath.add(oldFullFileName);
//							handler.sendEmptyMessage(0);
//						}
//					}
//				}
//
//				/* encoding finish, release recorder */
//				if (audioRecord != null) {
//					audioRecord.stop();
//					audioRecord.release();
//					audioRecord = null;
//				}
//				Logger.d(Tag, "begin stop");
//				if (recorder != null) {
//					try {
//						Logger.d(Tag, "do stop");
//						recorder.stop();
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//					recorder = null;
//				}
//				if (isRecorderStart == false) { // last video clip
//					oldFullFileName = prRecordedFile.getAbsolutePath();
//					toUploadPath.add(prRecordedFile.getAbsolutePath());
//					handler.sendEmptyMessage(0);
//
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//				Logger.e(Tag, "get audio data failed");
//			}
//
//		}
//	}
//	/**
//	 * 上个录制文件名,用于文件分片
//	 */
//	private String oldFullFileName;
//	/**
//	 * 文件上传消息处理
//	 */
//	private Handler handler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			uploaderTask = new UploadImageTask(VideoRecorderActivity1.this);
//			uploaderTask.execute(oldFullFileName, VideoConfig.UPLOAD_URL, "");
//			// 修改播放地址，作为测试用
////			if (VideoConfig.PLAY_URL.contains(".m3u8") == false) {
////				String[] fs = oldFullFileName.split("/");
////				String filename = fs[fs.length - 1];
////
////				String nameWithoutExt = filename.substring(0, filename.indexOf("_"));
////				String remoteFolder = VideoConfig.DEFAULT_PLAY_URL.substring(0, VideoConfig.DEFAULT_PLAY_URL.lastIndexOf("/") + 1);
////				VideoConfig.PLAY_URL = remoteFolder + nameWithoutExt + ".m3u8";
////			}
//			Logger.d(Tag, "VideoConfig_PLAYURL:" + VideoConfig.PLAY_URL);
//
//		}
//	};
//	/**
//	 * 文件上传成功后回调通知
//	 */
//	public void uploadSuccess(String fileName) {
//		Logger.d(Tag, "uploaded file:" + fileName);
//		this.toUploadPath.remove(fileName);
//	}
//
//	public static double rootMeanSquared(short[] nums) {
//		double ms = 0;
//		for (int i = 0; i < nums.length; i++) {
//			ms += nums[i] * nums[i];
//		}
//		ms /= nums.length;
//		return Math.sqrt(ms);
//	}
//
//	void calc1(short[] lin, int off, int len) {
//		int i, j;
//		for (i = 0; i < len; i++) {
//			j = lin[i + off];
//			lin[i + off] = (short) (j >> 2);
//		}
//	}
//
//	private static final int REQUEST_DECODING_OPTIONS = 0;
//	
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
//		super.onActivityResult(requestCode, resultCode, intent);
//		switch (requestCode) {
//		case REQUEST_DECODING_OPTIONS:
//			if (resultCode == RESULT_OK) {
//				updateEncodingOptions();
//			}
//			break;
//		}
//	}
//	
///////////////////////////////////////////////////////////////////////////
/////Timer
//    private void startVideoTimer(){  
//        if (mVideoRecorderTimer == null) {  
//        	mVideoRecorderTimer = new Timer();  
//        }  
//  
//        if (mVideoRecorderTimerTask == null) {  
//        	mVideoRecorderTimerTask = new TimerTask() {  
//                @Override  
//                public void run() {  
//                    Logger.i(getClass().getName(), "count: "+String.valueOf(count));                
//                    if(count>=settingTime){
//                    	 sendTimerMessage(STOP_RECORD);  
//                    	 isPause=false;
//                    }else{
//                        sendTimerMessage(UPDATE_TIMER);  
//                    }                     
//                    do {  
//                        try {  
//                        	Logger.i(getClass().getName(), "sleep(1000)...");  
//                            Thread.sleep(1000);  
//                        } catch (InterruptedException e) {  
//                        }     
//                    } while (isPause);  
//                      
//                    count ++;    
//                }  
//            };  
//        }  
//  
//        if(mVideoRecorderTimer != null && mVideoRecorderTimerTask != null )  
//        	mVideoRecorderTimer.schedule(mVideoRecorderTimerTask, delay, period);  
//  
//    }  
//  
//    private void stopVideoTimer(){  
//          
//        if (mVideoRecorderTimer != null) {  
//        	mVideoRecorderTimer.cancel();  
//        	mVideoRecorderTimer = null;  
//        }  
//  
//        if (mVideoRecorderTimerTask != null) {  
//        	mVideoRecorderTimerTask.cancel();  
//        	mVideoRecorderTimerTask = null;  
//        }     
//  
//        count = 0;  
//    }
//    
//    public void sendTimerMessage(int id){  
//        if (mVideoRecorderHandler != null) {  
//            Message message = Message.obtain(mVideoRecorderHandler, id);     
//            mVideoRecorderHandler.sendMessage(message);   
//        }  
//    }
//    
//    private String getTimeString(int count){
//    	String reVal="";
//    	if(count>0){
//    		int minutes=count/60;
//    		if(minutes==0){
//    			int seconds=count%60;
//    			if(seconds>9){
//        			reVal="00:00:"+seconds;
//    			}else{
//    				reVal="00:00:0"+seconds;
//    			}
//
//    		}else{
//    			if(minutes>9){
//    				reVal="00:"+minutes;
//    			}else{
//    				reVal="00:0"+minutes;
//    			}
//    			int seconds=count%60;
//    			if(seconds>9){
//    				reVal=reVal+seconds;
//    			}else{
//    				reVal=reVal+":0"+seconds;
//    			}
//    		}
//    	}else{
//    		reVal="00:00:00";
//    	}
//    	return reVal;
//    }

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}
}
