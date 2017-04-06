/*
* Copyright (c) 2012,TerenceYang
* All rights reserved.
*
* SettingDialog
* Summary:配置信息设置界面
*
*Version:1.0
*@author: TerenceYang
* @Date: 2012-10-10
*/

package com.allen.music.views.view.video;

import com.allen.music.R;
import com.allen.music.views.view.video.ffmpeg.Utils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

/**
 * 配置设置类
 * @author Terence
 *
 */
public class SettingsDialog extends Activity {
	private Spinner prSpinnerMute;
	private Spinner prSpinnerSplitter;
	private Spinner prSpinnerTime;
	private Spinner prSpinnerResolution;
	
	private Button prBtnOk;
	private Button prBtnCancel;

	//for folderSpinner: 1. original folder, 2. browse for a folder
	public static final int cpuH263 = 0;
	public static final int cpuMP4_SP = 1;
	public static final int cpuH264 = 2;
	
	public static final int cpu3GP = 0;
	public static final int cpuMP4 = 1;
	
	public static final int cpuRes176 = 0;
	public static final int cpuRes320 = 1;
	public static final int cpuRes720 = 2;
	public static final int cpuRes800 = 3;
	
	public static final int cpuSplitter50KB = 0;
	public static final int cpuSplitter100KB = 1;
	public static final int cpuSplitter200KB = 2;
	public static final int cpuSplitter500KB = 3;
	
	public static final int cpuMuteNo = 0;
	public static final int cpuMuteVideo = 1;
	public static final int cpuMuteAudio = 2;
	
	public static final int cpuTime1 = 0;
	public static final int cpuTime5= 5;
	public static final int cpuTime10 =10;
	
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.video_dialog_settings);	
		
//		prSpinnerMute = (Spinner) findViewById(R.id.dialog_settings_mute_spinner);
//		ArrayAdapter<CharSequence> adapterMute = ArrayAdapter.createFromResource(this, R.array.dialog_settings_mute_array, android.R.layout.simple_spinner_item);
//		adapterMute.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		prSpinnerMute.setAdapter(adapterMute);	
//		prSpinnerMute.setSelection(Utils.puMute);
//		prSpinnerMute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
//				int rowSelected = (int)arg3;
//				Utils.puMute = rowSelected;
//			}
//			public void onNothingSelected(AdapterView<?> arg0) {
//				//do nothing
//			}
//		});
//		
//		prSpinnerSplitter = (Spinner) findViewById(R.id.dialog_settings_splitter_spinner);
//		ArrayAdapter<CharSequence> adapterSplitter = ArrayAdapter.createFromResource(this, R.array.dialog_settings_splitter_array, android.R.layout.simple_spinner_item);
//		adapterSplitter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		prSpinnerSplitter.setAdapter(adapterSplitter);	
//		prSpinnerSplitter.setSelection(Utils.puSplitter);
//		prSpinnerSplitter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
//				int rowSelected = (int)arg3;
//				Utils.puSplitter = rowSelected;
//			}
//			public void onNothingSelected(AdapterView<?> arg0) {
//				//do nothing
//			}
//		});
		
		prSpinnerTime = (Spinner) findViewById(R.id.dialog_settings_time_spinner);
		ArrayAdapter<CharSequence> adapterSplitter = ArrayAdapter.createFromResource(this, R.array.dialog_settings_time_array, android.R.layout.simple_spinner_item);
		adapterSplitter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		prSpinnerTime.setAdapter(adapterSplitter);	
		prSpinnerTime.setSelection(Utils.puTime);
		prSpinnerTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				int rowSelected = (int)arg3;
				Utils.puTime = rowSelected;
			}
			public void onNothingSelected(AdapterView<?> arg0) {
				//do nothing
			}
		});
		
		prSpinnerResolution = (Spinner) findViewById(R.id.dialog_settings_resolution_spinner);
		ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.dialog_settings_resolution_array, android.R.layout.simple_spinner_item);
		adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		prSpinnerResolution.setAdapter(adapter3);	
		prSpinnerResolution.setSelection(Utils.puResolutionChoice);
		prSpinnerResolution.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				int rowSelected = (int)arg3;
				Utils.puResolutionChoice = rowSelected;
			}
			public void onNothingSelected(AdapterView<?> arg0) {
				//do nothing
			}
		});
		
		prBtnOk = (Button) findViewById(R.id.dialog_settings_btn1);
		prBtnOk.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//export picture to specified location with specified name
				setResult(RESULT_OK);
				finish();
			}
		});
		prBtnCancel = (Button) findViewById(R.id.dialog_settings_btn2);
		prBtnCancel.setOnClickListener(new View.OnClickListener() {	
			public void onClick(View v) {
				//cancel export, just finish the activity
				SettingsDialog.this.finish();
			}
		});
		getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}
}

