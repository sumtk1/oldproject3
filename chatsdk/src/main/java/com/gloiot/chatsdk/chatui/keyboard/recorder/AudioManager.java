package com.gloiot.chatsdk.chatui.keyboard.recorder;

import android.media.MediaRecorder;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by hygo00 on 17/2/23.
 * 发送语音Manager
 */

public class AudioManager {

    private MediaRecorder mMediaRecorder;
    private String mDir; // 保存路径
    private String mCurrentFilePath; // 当前文件路径
    private boolean isPrepare; // Audio准备

    private static AudioManager mInstance;

    public AudioStateListener mAudioStateListener;

    private AudioManager(String dir) {
        mDir = dir;
    }

    public static AudioManager getInstance(String dir) {
        if (mInstance == null) {
            // 同步AudioManager
            synchronized (AudioManager.class) {
                if (mInstance == null) {
                    mInstance = new AudioManager(dir);
                }
            }
        }
        return mInstance;
    }

    // 准备录音
    public void prepareAudio() {
        try {
            File dir = new File(mDir);
            if (!dir.exists()) // 路径是否存在
                dir.mkdirs();

            String fileName = generateFileName();
            File file = new File(dir, fileName);

            mCurrentFilePath =file.getAbsolutePath();

            mMediaRecorder = new MediaRecorder();
            // 设置输出文件
            mMediaRecorder.setOutputFile(file.getAbsolutePath());
            // 设置MediaRecorder的音频源为麦克风
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            // 设置音频格式为amr
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            // 设置音频编码为amr
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            // 准备录音
            mMediaRecorder.prepare();
            // 开始
            mMediaRecorder.start();
            // 准备结束
            isPrepare = true;
            if (mAudioStateListener != null) {
                mAudioStateListener.wellPrepared();
            }

        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 随机生成文件的名称、amr格式
     */
    private String generateFileName() {
        return UUID.randomUUID().toString() + ".amr";
    }

    /**
     * 音量等级
     * @param maxlevel 音量级别
     * @return
     */
    public int getVoiceLevel(int maxlevel) {
        if (isPrepare) {
            try {
                // mMediaRecorder.getMaxAmplitude() 1~32767
                return maxlevel * mMediaRecorder.getMaxAmplitude() / 32768 + 1;
            } catch (Exception e) {
            }
        }
        return 1;
    }

    /**
     * 释放资源
     */
    public void release() {
        try {
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder = null;
        }catch (Exception e){

        }
    }

    /**
     * 取消录音
     */
    public void cancel() {
        release();
        if (mCurrentFilePath != null) {
            File file = new File(mCurrentFilePath);
            file.delete(); // 删除录音文件
            mCurrentFilePath = null;
        }

    }

    /**
     * 获取当前文件路径
     * @return
     */
    public String getCurrentFilePath() {
        return mCurrentFilePath;
    }

    /**
     * 使用接口 用于回调
     */
    public interface AudioStateListener {
        void wellPrepared();
    }

    /**
     * 回调方法
     */
    public void setOnAudioStateListener(AudioStateListener listener) {
        mAudioStateListener = listener;
    }

}
