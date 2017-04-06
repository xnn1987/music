package com.allen.music.views.view.video.ffmpeg;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder.AudioSource;

import com.allen.music.tools.Logger;

public class AudioRecordThread implements Runnable {
    public AudioRecordThread(FFmpegVideoRecorder recorder, VideoParam param) {
        this.recorder = recorder;
        this.param = param;        
    }

    @Override
    public void run() {
        int bufferSize;
        byte[] audioData;
        int bufferReadResult;
        try {
            bufferSize = AudioRecord.getMinBufferSize(param.sampleRate, AudioFormat.CHANNEL_IN_DEFAULT, AudioFormat.ENCODING_PCM_16BIT);
            if (bufferSize <= 2048) {
                bufferLength = 2048;
            } else if (bufferSize <= 4096) {
                bufferLength = 4096;
            } else if(bufferSize<=8192) {
                bufferLength = 8192;
            }
            
            Logger.v(getClass().getName(), String.format("run: buffer size=%d, buffer length=%d", bufferSize, bufferLength));
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
            audioRecord = new AudioRecord(AudioSource.MIC, param.sampleRate, AudioFormat.CHANNEL_IN_DEFAULT, AudioFormat.ENCODING_PCM_16BIT, bufferLength );
            if(audioRecord ==null){
                Logger.e(getClass().getName(), "run: cant init audio record");
                return;
            }
            audioData = new byte[bufferLength];
            audioRecord.startRecording();
            Logger.v(getClass().getName(), "run: start recording bufferLength=" + bufferLength);

            isAudioRecording = true;

            /* ffmpeg_audio encoding loop */
            while (isAudioRecording) {
                if (isRecorderStart) {
                    bufferReadResult = audioRecord.read(audioData, 0, audioData.length);
                    Logger.v(getClass().getName(), "run: bufferReadResult= " + bufferReadResult);
                    if (bufferReadResult == 2048) {
                        recorder.recordAudio(audioData);
                    } else if (bufferReadResult == 4096) {
                        byte[] realAudioData2048_1 = new byte[2048];
                        byte[] realAudioData2048_2 = new byte[2048];

                        System.arraycopy(audioData, 0, realAudioData2048_1, 0, 2048);
                        System.arraycopy(audioData, 2048, realAudioData2048_2, 0, 2048);
                        for (int i = 0; i < 2; i++) {
                            if (i == 0) {
                                recorder.recordAudio(realAudioData2048_1);
                                Logger.v(getClass().getName(), "encoded first part of 4096");
                            } else if (i == 1) {
                                recorder.recordAudio(realAudioData2048_2);
                                Logger.v(getClass().getName(), "encoded second part of 4096");
                            }
                        }
                    }
                    else if (bufferReadResult == 8192) {
                        byte[] realAudioData2048_1 = new byte[2048];
                        byte[] realAudioData2048_2 = new byte[2048];
                        byte[] realAudioData2048_3 = new byte[2048];
                        byte[] realAudioData2048_4 = new byte[2048];

                        System.arraycopy(audioData, 0, realAudioData2048_1, 0, 2048);
                        System.arraycopy(audioData, 2048, realAudioData2048_2, 0, 2048);
                        System.arraycopy(audioData, 4096, realAudioData2048_3, 0, 2048);
                        System.arraycopy(audioData, 6144, realAudioData2048_4, 0, 2048);

                        
                        for (int i = 0; i < 4; i++) {
                            if (i == 0) {
                                recorder.recordAudio(realAudioData2048_1);
                            } else if (i == 1) {
                                recorder.recordAudio(realAudioData2048_2);
                            }else if(i==2)
                                recorder.recordAudio(realAudioData2048_3);
                            else if(i==3)
                                recorder.recordAudio(realAudioData2048_4);
                        }
                    }
                    // create second video clip and upload to server
//                    long fileLength = prRecordedFile.length();
//                    if (fileLength >= param.splitterSize) {
//                        recorder.stop();
//                        oldFullFileName = prRecordedFile.getAbsolutePath();
//
//                        String filename = prRecordedFile.getName();
//                        splitCounter++;
//                        String newFilename = filename.substring(0, filename.indexOf("_")) + "_" + splitCounter + suffix;
//                        prRecordedFile = new File(cVideoFilePath + newFilename);
//                        recorder.start(prRecordedFile.getAbsolutePath());
//
//                        toUploadPath.add(oldFullFileName);
//                        handler.sendEmptyMessage(0);
//                    }
                }
            }

            /* encoding finish, release recorder */
            if (audioRecord != null) {
                audioRecord.stop();
                audioRecord.release();
                audioRecord = null;
            }
            Logger.d(getClass().getName(), "begin stop");
            if (recorder != null) {
                try {
                    Logger.d(getClass().getName(), "do stop");
                    recorder.stop();
                } catch (Exception e) {
                    Logger.ex(getClass().getName(), e);
                }
                recorder = null;
            }

//            if (isRecorderStart == false) { // last video clip
//                oldFullFileName = prRecordedFile.getAbsolutePath();
//                toUploadPath.add(prRecordedFile.getAbsolutePath());
//                handler.sendEmptyMessage(0);
//
//            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e(getClass().getName(), "run: get audio data failed");
        }
    }
    
    public void startRecording() {
        isAudioRecording = true;
        isRecorderStart = true;
    }
    
    public void stopRecording() {
        isAudioRecording = false;
        isRecorderStart = false;
    }

    private VideoParam param = null;
    private int bufferLength = 0;
    private AudioRecord audioRecord;    // 音频采集
    private volatile boolean isRecorderStart = false;   // 编码控制信号量
    private volatile boolean isAudioRecording = false;   // 音频编吗控制变量
    private FFmpegVideoRecorder recorder = null;
}
