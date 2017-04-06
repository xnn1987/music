package com.allen.music.views.view.video;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.allen.music.R;
import com.allen.music.tools.Logger;
import com.allen.music.views.base.BaseActivity;
import com.allen.music.views.view.video.ffmpeg.Utils;
import com.allen.music.views.view.video.ffmpeg.VideoRecorder;

public class VideoRecorderActivity extends BaseActivity {
	private SurfaceHolder surfaceHolder = null;
	private VideoRecorder recorder = null;

	/**
	 * 视频录制计时器
	 * **/
	private Timer mVideoRecorderTimer;
    private TimerTask mVideoRecorderTimerTask = null;  
    private Handler mVideoRecorderHandler = null;      
    private static int count = 0;  
    private boolean isPause = false;  
    private boolean isStop = true;  
    private static int delay = 1000;  //1s  
    private static int period = 1000;  //1s  
    
	/**
	 *视频录制时长 1分钟， 5分钟，10分钟；
	 */
	private int settingTime = 60;
    private static final int UPDATE_TIMER = 0;  
    private static final int STOP_RECORD = 1;  
    
    private TextView mTimerText;
	private Spinner prSpinnerTime;
	
	@Override
	protected void onCreate(Bundle icie) {
		 super.onCreate(icie); // Do not call
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		this.setContentView(R.layout.video_recorder);
		
		mTimerText=(TextView)findViewById(R.id.btn_videorecord_timer);
		
		prSpinnerTime = (Spinner) findViewById(R.id.spinner_videorecorder_time);
		ArrayAdapter<CharSequence> adapterSplitter = ArrayAdapter.createFromResource(this, R.array.dialog_settings_time_array, android.R.layout.simple_spinner_item);
		adapterSplitter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		prSpinnerTime.setAdapter(adapterSplitter);	
		prSpinnerTime.setSelection(Utils.puTime);
		prSpinnerTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				int rowSelected = (int)arg3;
				switch(rowSelected){
				case 0:
					settingTime=60;
					break;
				case 1:
					settingTime=5*60;
					break;
				case 2:
					settingTime=10*60;
					break;
				}
			}
			public void onNothingSelected(AdapterView<?> arg0) {
				//do nothing
			}
		});
		
		recorder = new VideoRecorder(this);
		SurfaceView sv = (SurfaceView) findViewById(R.id.surface_camera);
		surfaceHolder = sv.getHolder();
		surfaceHolder.addCallback(callback);
		
		startVideoTimer();//start timer

		findViewById(R.id.main_btn1).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Logger.d(getClass().getName(), "onClick: " + view);
				stopVideoTimer(); //stop timer
				recorder.stopRecording();
				findViewById(R.id.main_btn1).setBackgroundResource(R.drawable.bg_video_recorder);
				
				ActivityResult(); //call back 
				finish();
			}
		});
		
		//Time Handler
		mVideoRecorderHandler=new Handler(){

			@Override
			public void handleMessage(Message msg) {
				 switch (msg.what) {  
	                case UPDATE_TIMER:  
	                	mTimerText.setText(getTimeString(count));
	                    break;  
	                case STOP_RECORD:  
	                	stopVideoTimer();//stop video timer
	                	recorder.stopRecording();//stop record video
	                	ActivityResult(); //call back 
	                	finish();
	                    break;  
	                default:  
	                    break;  
	                }  
			}			
		};
	}

	private void ActivityResult(){
		Intent intent = new Intent("VideoRecorder");
		intent.putExtra("VideoPath", recorder.getVideoPath());
		intent.putExtra("VideoLength", String.valueOf(count));
		setResult(RESULT_OK,intent);
	}
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	private SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
			Logger.d(getClass().getName(), "surfaceChanged");
			if (null != recorder) {
				recorder.attachDispaly(surfaceHolder);
				recorder.startRecording();
			}
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			Logger.d(getClass().getName(), "surfaceCreated");
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			Logger.d(getClass().getName(), "surfaceDestroyed");
			if (null != recorder) {
				recorder.detachDisplay();
				recorder.stopRecording();
			}
		}
	};
/////////////////////////////////////////////////////////////////////////
///Timer
	private void startVideoTimer() {
		if (mVideoRecorderTimer == null) {
			mVideoRecorderTimer = new Timer();
		}

		if (mVideoRecorderTimerTask == null) {
			mVideoRecorderTimerTask = new TimerTask() {
				@Override
				public void run() {
					Logger.i(getClass().getName(),"count: " + String.valueOf(count));
					if (count >= settingTime) {
						sendTimerMessage(STOP_RECORD);
						isPause = false;
					} else {
						sendTimerMessage(UPDATE_TIMER);
					}
					do {
						try {
							Logger.i(getClass().getName(), "sleep(1000)...");
							Thread.sleep(1000);
						} catch (InterruptedException e) {
						}
					} while (isPause);

					count++;
				}
			};
		}

		if (mVideoRecorderTimer != null && mVideoRecorderTimerTask != null)
			mVideoRecorderTimer.schedule(mVideoRecorderTimerTask, delay, period);

	}

	private void stopVideoTimer() {

		if (mVideoRecorderTimer != null) {
			mVideoRecorderTimer.cancel();
			mVideoRecorderTimer = null;
		}

		if (mVideoRecorderTimerTask != null) {
			mVideoRecorderTimerTask.cancel();
			mVideoRecorderTimerTask = null;
		}

		count = 0;
	}

	public void sendTimerMessage(int id) {
		if (mVideoRecorderHandler != null) {
			Message message = Message.obtain(mVideoRecorderHandler, id);
			mVideoRecorderHandler.sendMessage(message);
		}
	}

	private String getTimeString(int count) {
		String reVal = "";
		if (count > 0) {
			int minutes = count / 60;
			if (minutes == 0) {
				int seconds = count % 60;
				if (seconds > 9) {
					reVal = "00:00:" + seconds;
				} else {
					reVal = "00:00:0" + seconds;
				}

			} else {
				if (minutes > 9) {
					reVal = "00:" + minutes;
				} else {
					reVal = "00:0" + minutes;
				}
				int seconds = count % 60;
				if (seconds > 9) {
					reVal = reVal + seconds;
				} else {
					reVal = reVal + ":0" + seconds;
				}
			}
		} else {
			reVal = "00:00:00";
		}
		return reVal;
	}
}

