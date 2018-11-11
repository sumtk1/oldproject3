package com.gloiot.chatsdk.chatui.keyboard.recorder;

import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.view.View;

import com.gloiot.chatsdk.R;

public class MediaManager {

	private static MediaManager instance;
	private MediaPlayer mMediaPlayer;
	private boolean isPause;

	/** 播放语音的动画相关属性 */
	private View animView;
	private boolean animType;
	private onInterruptAnimListener onInterruptAnimListener;

	private MediaManager() {

	}

	public static MediaManager getInstance() {
		if (instance == null) {
			instance = new MediaManager();
		}
		return instance;
	}

	/**
	 * 播放语音和动画时传入监听事件
	 * 当出现终止播放的语音时，触发回调
	 */
	public interface onInterruptAnimListener {
		void interruptAnim();
	}

	/**
	 * 播放语音和动画
	 * @param animView   播放语音的动画展示view
	 * @param animType   判断播放接收或者发送的动画， true为接收/左，false为发送/右
	 * @param voiceData  语音数据
	 * @param onCompletionListener  播放语音完成的回调
	 */
	public void playSound(View animView, boolean animType, String voiceData,
						  OnCompletionListener onCompletionListener, onInterruptAnimListener onInterruptAnimListener) {
		setAnimView(onInterruptAnimListener, animView, animType);

		if (mMediaPlayer == null) {
			mMediaPlayer = new MediaPlayer();
			
			//设置一个error监听器
			mMediaPlayer.setOnErrorListener(new OnErrorListener() {

				public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
					mMediaPlayer.reset();
					return false;
				}
			});
		} else {
			mMediaPlayer.reset();
		}

		try {
			mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mMediaPlayer.setOnCompletionListener(onCompletionListener);
			mMediaPlayer.setDataSource(voiceData);
			mMediaPlayer.prepare();
			mMediaPlayer.start();
		} catch (Exception e) {

		}
	}

	/**
	 * 打开动画
	 */
	private void setAnimView(onInterruptAnimListener onInterruptAnimListener, View animView, boolean animType) {
		setAnimViewBg();
		this.onInterruptAnimListener = onInterruptAnimListener;
        this.animView = animView;
		this.animType = animType;
		setAnimViewAnim();
	}
	// 设置动画View的背景为一张图片
	private void setAnimViewBg() {
		if (animView != null) { // 若有正在播放的语音，将正在播放的语音的背景设置为图片
			animView.setBackgroundResource(animType ? R.mipmap.voice_left : R.mipmap.voice_right);
			onInterruptAnimListener.interruptAnim();
			animView = null;
		}
	}
	// 设置动画View的背景播放动画（帧动画）
	private void setAnimViewAnim() {
		animView.setBackgroundResource(animType ? R.drawable.play_anim_left : R.drawable.play_anim_right);
		AnimationDrawable animation = (AnimationDrawable) animView.getBackground();
		animation.start();
	}

	/**
	 * 暂停播放
	 */
	public void pause() {
		if (mMediaPlayer != null && mMediaPlayer.isPlaying()) { //正在播放的时候
			setAnimViewBg();
			mMediaPlayer.pause();
			isPause = true;
		}
	}

	/**
	 * 当前是isPause状态
	 */
	public void resume() {
		if (mMediaPlayer != null && isPause) {
			setAnimViewAnim();
			mMediaPlayer.start();
			isPause = false;
		}
	}

	/**
	 * 当正在播放时，停止播放
	 */
	public void stop() {
		if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
			setAnimViewBg();
			mMediaPlayer.stop();
			mMediaPlayer.reset();
		}
	}

	/**
	 * 释放资源
	 */
	public void release() {
		if (mMediaPlayer != null) {
			instance = null;
			setAnimViewBg();
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}
}
