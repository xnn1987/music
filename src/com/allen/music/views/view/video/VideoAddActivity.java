package com.allen.music.views.view.video;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allen.music.R;
import com.allen.music.http.HttpResStrToAsync;
import com.allen.music.tools.Logger;
import com.allen.music.views.base.BaseActivity;
import com.allen.music.views.base.BaseAppException;
import com.allen.music.views.bean.Result;

public class VideoAddActivity extends BaseActivity {

	private static final int REQUEST_VIDEO_RECORDER = 0;
	
	private RelativeLayout mVideoLayout;
	private TextView mVideoLength;
	private ImageView mBtnVideoRecod;
	private EditText mTitleEdit;
	private EditText mContentEdit;
	private Button mSaveButton;
	private boolean isRecord=true;
	
	private String mVideoPath;
	
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentLayout(R.layout.video_add);

		//initial view and data
		initTitleView();
		initView();
	}


	// initial view
	private void initView() {
		mVideoLength=(TextView)findViewById(R.id.tv_videorecord_time);
		mVideoLayout=(RelativeLayout)findViewById(R.id.relative_videorecord_again);
		mBtnVideoRecod=(ImageView)findViewById(R.id.img_videorecorder);
		mTitleEdit=(EditText)findViewById(R.id.ed_videorecord_title);
		mContentEdit=(EditText)findViewById(R.id.ed_videorecord_content);
		mSaveButton=(Button)findViewById(R.id.btn_save);
		
		mVideoLayout.setVisibility(View.GONE);
		mBtnVideoRecod.setOnClickListener(listener);
		mSaveButton.setOnClickListener(listener);
	}
	
	

	// 初始化title
	private void initTitleView() {

		getbtn_left().setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onKeyDown(KeyEvent.KEYCODE_BACK, null);
			}
		});
	}
	
	private View.OnClickListener listener=new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.img_videorecorder:
				if(isRecord){
					Intent intent = new Intent();
					intent.setClass(VideoAddActivity.this, VideoRecorderActivity.class);
					startActivityForResult(intent, REQUEST_VIDEO_RECORDER);
				}else{
					Intent intent =new Intent(VideoAddActivity.this, VideoPlayerActivity.class);
					intent.putExtra("path", mVideoPath);
					intent.putExtra("title", "Test Video");
					startActivity(intent);
				}

				break;
			case R.id.btn_save:
				gotoActivity(VideoPlayerActivity.class);
				break;
			}
			
		}
	};
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

		switch (requestCode) {
		case REQUEST_VIDEO_RECORDER:
			if (resultCode == RESULT_OK) {
				mVideoPath=intent.getStringExtra("VideoPath");
				String videoLength=intent.getStringExtra("VideoLength");
				
				Logger.d(getClass().getName(), "videoPath="+mVideoPath+" videoLength"+videoLength);
				updateVideoView(videoLength);
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, intent);
	}

	private void updateVideoView(String videoLength){
		if(!TextUtils.isEmpty(mVideoPath)){
			mVideoLayout.setVisibility(View.VISIBLE);
			mBtnVideoRecod.setImageResource(R.drawable.bg_audioplayer_play);
			mVideoLength.setText(videoLength);
			
			isRecord=false;
		}

	}

	private void send(final Map<String, Object> param) throws BaseAppException {

		new HttpResStrToAsync() {
			protected void onPreExecute() {// 2
				showDialog();
			}

			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					Result ob = (Result) msg.obj;
					if (ob != null) {

					}
				} else if (msg.what == 0) {
					Result ob = (Result) msg.obj;
					ToastMessage(ob.Message);
				} else if (msg.what == -1) {
					((BaseAppException) msg.obj).makeToast(getApplicationContext());
				}
				closeDialog();
			}
		}.execute_Test(param);
	}
}
