package com.allen.music.views.view.video.ffmpeg;

import java.io.File;

import com.allen.music.AppContext;
import com.allen.music.tools.DateUtils;
import com.allen.music.tools.Logger;

import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.WindowManager;


/**
 * Video Recorder
 * @author Terence
 * @date:2013-11-15
 */
public class VideoRecorder {
	public String videoPath;
	private String suffix = ".flv";
	
    public String getVideoPath() {
		return this.videoPath;
	}

	public void setVideoPath(String videoPath) {
		this.videoPath = videoPath;
	}

	public VideoRecorder(Context context) {
        this.context = context;
    }

    public void attachDispaly(SurfaceHolder holder) {
        Logger.d(getClass().getName(), "attachDispaly: " + holder);
        this.holder = holder;
        startCameraPreview(false);        
    }
    
    public void detachDisplay() {
        Logger.d(getClass().getName(), "detachDisplay");
        this.holder = null;
        stopCameraPreview(false);
    }
    
    public void startRecording() {
        Logger.d(getClass().getName(), "onRecorderStart");
        recording = true;
        
        startCameraPreview(true);
        
        String fileName=DateUtils.getFileNameByDateTime()+suffix;
        final String folder = AppContext.getProductFolder();
        Utils.createDirIfNotExist(folder);        
        videoPath=folder + File.separatorChar + fileName;
        File prRecordedFile = new File(videoPath);
        if (prRecordedFile.isFile()) {  // Delete old file.
            Logger.d(getClass().getName(), "onRecorderStart: delete old " + prRecordedFile.getPath());
            prRecordedFile.delete();
        }

        if (recorder == null) {
            Logger.d(getClass().getName(), "onRecorderStart: Initialize ffmpeg " + param);
            recorder = new FFmpegVideoRecorder(prRecordedFile, param);
        }
        recorder.start();

        Utils.muteSound(getContext(), true);        
        audioThread = new AudioRecordThread(recorder, param);
        new Thread(audioThread).start();
        new Thread(new VideoRecordThread()).start();
        
        audioThread.startRecording();   // Start recording audio after starting video recording thread.
    }

    public void stopRecording() {
        Logger.d(getClass().getName(), "stopRecording: " + isRecording());
        if (isRecording()) {
            recording = false;
            try {
                if (null != audioThread) {
                    audioThread.stopRecording();
                }                
                stopCameraPreview(false);
            } 
            catch (Exception e) {
                Logger.ex(getClass().getName(), e);
            }
            Utils.muteSound(getContext(), false);
        }
    }
    
    protected int getCameraDisplayOrientation(int cameraId, Camera camera) {
        int retVal = 0;
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int rotation = display.getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD) {   // API 9, November 2010: Android 2.3 
            android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
            android.hardware.Camera.getCameraInfo(cameraId, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                retVal = (info.orientation + degrees) % 360;
                retVal = (360 - retVal) % 360;  // compensate the mirror
            } 
            else {  // back-facing
                retVal = (info.orientation - degrees + 360) % 360;
            }
        }
        else {
            retVal = (360 - degrees) % 360;
        }
        return retVal;
    }
    
    public void startCameraPreview(boolean mandatory) {
        Logger.d(getClass().getName(), "startCameraPreview");
        if (mandatory && null != prCamera) {
            stopCameraPreview(mandatory);    // Always try to release the camera to avoid resource leaking.
        }
        
        try {
            if (mandatory || null == prCamera) {
                Logger.d(getClass().getName(), "startCameraPreview: start preview");
                prCamera = Camera.open();
                int orientation = getCameraDisplayOrientation(0, prCamera);
                Camera.Parameters parameters = prCamera.getParameters();
                parameters.setPreviewFrameRate(param.frameRate);
                parameters.setPreviewSize(param.width, param.height);
                parameters.setRotation(orientation);
                prCamera.setParameters(parameters);
                prCamera.setPreviewDisplay(holder);
                Logger.v(getClass().getName(), "startCameraPreview: PreviewFrameRate = " + prCamera.getParameters().getPreviewFrameRate());
                Size previewSize = prCamera.getParameters().getPreviewSize();
                param.width = previewSize.width;
                param.height = previewSize.height;
    
                int dataBufferSize = (int) (previewSize.height * previewSize.width * (ImageFormat.getBitsPerPixel(prCamera.getParameters().getPreviewFormat()) / 8.0));
                prCamera.addCallbackBuffer(new byte[dataBufferSize]);
                prCamera.addCallbackBuffer(new byte[dataBufferSize]);
                prCamera.addCallbackBuffer(new byte[dataBufferSize]);
                prCamera.setPreviewCallbackWithBuffer(previewCallback);
                prCamera.startPreview();
            }
            else if (null != holder) {  // Just attach display.   
//                prCamera.stopPreview();
                prCamera.reconnect();
                prCamera.setPreviewDisplay(holder);
                prCamera.startPreview();
            }            
        }
        catch (Exception e) {
            Logger.e(getClass().getName(), "startCameraPreview: filed to open camera");
            if (null != prCamera) {
                prCamera.release();
                prCamera = null;
            }
        }
    }
    
    private void stopCameraPreview(boolean mandatory) {
        Logger.d(getClass().getName(), "stopCameraPreview");
        try {
            if (null != prCamera && (mandatory || null == holder)) {
                Logger.d(getClass().getName(), "stopCameraPreview: stop preview");
                prCamera.setPreviewDisplay(null);
                prCamera.setPreviewCallbackWithBuffer(null);
                prCamera.stopPreview();
            }
        } 
        catch (Exception e) {
            Logger.ex(getClass().getName(), e);
        }
        finally {
            if (null != prCamera && (mandatory || null == holder)) {
                Logger.d(getClass().getName(), "stopCameraPreview: release camera");
                prCamera.release();
                prCamera = null;
            }
        }
    }

    /**
     * Preview the camera image.
     */
    private PreviewCallback previewCallback = new PreviewCallback() {
        public synchronized void onPreviewFrame(byte[] data, Camera camera) {
            previewPicture = data;
            camera.addCallbackBuffer(data);
        }
    };

    private class VideoRecordThread implements Runnable {
        public void run() {
            while (isRecording()) {
                if (null != recorder) {
                    if (previewPicture != null) {
                        Logger.v(getClass().getName(), "run: record picture");
                        recorder.record(previewPicture);
                    }                    
                }
                else {
                    break;
                }
            }
        }
    }
    
    
    public boolean isRecording() { return recording; }    
    private Context getContext() { return context; }

    private Context context = null;
    private volatile boolean recording = false;    
    private Camera prCamera = null;
    private byte[] previewPicture = null;  // 缓存需要编码的视频图片变量
    private VideoParam param = new VideoParam();
    private FFmpegVideoRecorder recorder = null;   // 实际音视频录制封装类
    private AudioRecordThread audioThread = null;   // 音频编码线程

    SurfaceHolder holder = null;
}
