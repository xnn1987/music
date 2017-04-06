package com.allen.music.views.view.video.ffmpeg;

import java.io.File;

public class VideoParam {
    
    @Override
    public String toString() {
        return super.toString() + String.format("(frame=%d,bitrate=%d,audioHzRate=%d,audioBitrate=%d,channels=%d,width=%d,height=%d,mute=%d)", 
                frameRate, videoBitrate, sampleRate, audioBitrate, audioChannels, width, height, mute);
    }
    
    
    public final int frameRate = 10;  // 帧率
    public final int videoBitrate = 512 * 1000;    // 码率 
    public int sampleRate = 44100;//22050;//  音频采样率
    public int audioBitrate = 96000; // 音频比特率
    public int audioChannels = 1;  // 声道数
    public int splitterSize = 400 * 1024;  // 每个分片文件大小
    public int width  = 320;  // 分辨率宽度
    public int height = 240;  // 分辨率长度
    public int mute = 0;   // 静音 : 0-no mute 1-mute sound 2-mute video
}
