package com.gloiot.chatsdk.chatui.keyboard.recorder;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

import com.gloiot.chatsdk.R;


/**
 * Created by hygo00 on 17/2/23.
 * 发送语音按钮
 */

public class AudioRecorderButton extends Button {

    private static final int DISTANCE_Y_CANCEL = 100; //
    private static final int STATE_NORMAL = 1; // 默认的状态
    private static final int STATE_RECORDING = 2; // 正在录音
    private static final int STATE_WANT_TO_CANCEL = 3; // 希望取消
    private static final float MAX_VOICE_TIME = 60f; // 录音最大时长

    private int mCurState = STATE_NORMAL; // 当前点击状态
    private boolean isRecording = false; // 已经开始录音

    private DialogManager mDialogManager; // 发语音对话框
    private AudioManager mAudioManager; // 发送语音的管理器

    private float mTime; // 计时
    private boolean mReady; // 是否触发点击
    private long lastTime; // 点击按钮的最后时间

    private static final int MSG_AUDIO_PREPARED = 0x110; // 开始录音
    private static final int MSG_VOICE_CHANGED = 0x111; // 改变录音状态
    private static final int MSG_DIALOG_DIMISS = 0x112; // 结束录音

    private AudioFinishRecorderListener audioFinishRecorderListener; // 完成录音回调

    /**
     * 录音完成后的回调
     */
    public interface AudioFinishRecorderListener {
        void onFinish(float seconds, String filePath);
    }

    public void setAudioFinishRecorderListener(AudioFinishRecorderListener listener) {
        audioFinishRecorderListener = listener;
    }


    /**
     * 获取音量大小的线程
     */
    private Runnable mGetVoiceLevelRunnable = new Runnable() {

        public void run() {
            while (isRecording) {
                try {
                    // 每1毫秒更新当前录音状态
                    Thread.sleep(100);
                    mTime += 0.1f;
                    mHandler.sendEmptyMessage(MSG_VOICE_CHANGED);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_AUDIO_PREPARED:
                    // 开启一个线程
                    new Thread(mGetVoiceLevelRunnable).start();
                    break;
                case MSG_VOICE_CHANGED:
                    mDialogManager.updateVoiceLevel(mAudioManager.getVoiceLevel(7));
                    // 超过录音最大时长发送语音
                    if (mTime > MAX_VOICE_TIME) {
                        mDialogManager.dimissDialog();
                        mAudioManager.release();
                        if (audioFinishRecorderListener != null) {
                            audioFinishRecorderListener.onFinish(mTime, mAudioManager.getCurrentFilePath());
                        }
                        reset();
                    }
                    break;

                case MSG_DIALOG_DIMISS:
                    mDialogManager.dimissDialog();
                    break;

            }
        }
    };

    public AudioRecorderButton(Context context) {
        super(context);
    }

    public AudioRecorderButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        mDialogManager = new DialogManager(context);
//        String dir = "/storage/sdcard0/my_weixin";
        String dir = Environment.getExternalStorageDirectory() + "/my_recorder"; // 录音文件保存路径，后期需具体优化异常

        mAudioManager = AudioManager.getInstance(dir);
        mAudioManager.setOnAudioStateListener(new AudioManager.AudioStateListener() {

            public void wellPrepared() {
                // 录音准备完成回调
                isRecording = true;
                mHandler.sendEmptyMessage(MSG_AUDIO_PREPARED);
            }
        });
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN: // 按下监听
                if (!onMoreClick()) {
                    // 开始录音后显示对话框
                    mDialogManager.showRecordingDialog();
                    changeState(STATE_RECORDING);
                    mReady = true;
                    mAudioManager.prepareAudio();
                    lastTime = System.currentTimeMillis();
                }
                break;
            case MotionEvent.ACTION_MOVE: // 移动监听
                if (isRecording) {
                    // 根据x,y的坐标，判断是否想要取消
                    if (wantToCancel(x, y)) {
                        changeState(STATE_WANT_TO_CANCEL);
                    } else {
                        changeState(STATE_RECORDING);
                    }
                }
                break;
            case MotionEvent.ACTION_UP: // 释放按下监听
                if (!mReady) {
                    reset();
                    return super.onTouchEvent(event);
                }
                if (mCurState == STATE_WANT_TO_CANCEL) {
                    mDialogManager.dimissDialog();
                    mAudioManager.cancel();
                    reset();
                    return super.onTouchEvent(event);
                }
                if (!isRecording || mTime < 1f) {
                    mDialogManager.tooShort();
                    mAudioManager.cancel();
                    mHandler.sendEmptyMessageDelayed(MSG_DIALOG_DIMISS, 1000); // 延迟显示对话框
                } else if (mCurState == STATE_RECORDING) { // 正常录制结束
                    mDialogManager.dimissDialog();
                    mAudioManager.release();
                    if (audioFinishRecorderListener != null) {
                        audioFinishRecorderListener.onFinish(mTime, mAudioManager.getCurrentFilePath());
                    }
                }
                reset();
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 限制按钮1秒内多次触发
     *
     * @return
     */
    private boolean onMoreClick() {
        long time = System.currentTimeMillis() - lastTime;
        if (time < 1000) {
            return true;
        }
        return false;
    }

    /**
     * 恢复状态及标志位
     */
    private void reset() {
        isRecording = false;
        mTime = 0;
        mReady = false;
        changeState(STATE_NORMAL);
    }

    private boolean wantToCancel(int x, int y) {
//        // 判断手指横坐标是否超出按钮范围
//        if (x < 0 || x > getWidth()) {
//            return true;
//        }
        // 判断手指纵坐标是否超出按钮范围
        if (y < -DISTANCE_Y_CANCEL || y > getHeight() + DISTANCE_Y_CANCEL) {
            return true;
        }
        return false;
    }

    private void changeState(int state) {
        if (mCurState != state) {
            mCurState = state;
            switch (state) {
                case STATE_NORMAL:
                    setBackgroundResource(R.drawable.btn_recorder_normal);
                    setText(R.string.str_recorder_normal);
                    break;
                case STATE_RECORDING:
                    setBackgroundResource(R.drawable.btn_recordering);
                    setText(R.string.str_recorder_recording);
                    if (isRecording) {
                        mDialogManager.recording();
                    }
                    break;
                case STATE_WANT_TO_CANCEL:
                    setBackgroundResource(R.drawable.btn_recordering);
                    setText(R.string.str_recorder_want_cancel);
                    mDialogManager.wantToCancel();
                    break;
            }
        }
    }

}
