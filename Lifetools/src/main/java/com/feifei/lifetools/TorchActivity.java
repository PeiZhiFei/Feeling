package com.feifei.lifetools;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.feifei.util.AnimUtil;
import com.feifei.util.ConfigUtil;
import com.feifei.util.ToastCustom;
import com.feifei.util.ToastUtil;

// 需要手电筒和屏幕光一起
public class TorchActivity extends BaseActivity {
	private FlashlightSurface mSurface;
	private ImageView image_torch, image_screen;
	private boolean isFlashlightOn = false;
	private boolean isPlaying = false;

	private MediaPlayer mediaPlayer;
	private MediaPlayer alarmPlayer;
	private AudioManager audioManager;
	// 获取当前音量值，亮度值
	private int screenBrightness;
	private int currentVolume;
	private Button button_torch;
	// 锁屏监听事件
	private ScreenObserver mScreenObserver;
	// 屏幕亮度标识
	private int screen_count = 1;
	// 锁屏标识
	private int power_count = 0;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_torch);
		initAudio();
		initView();
		try {
			initEvents();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!hasFlashlight) {
			button_torch.setEnabled(false);
		}

		// 首次打开
		if (torchFirst) {
			ConfigUtil.writeBoolean(this, "torchFirst", false);
			ToastUtil.toast(TorchActivity.this, "屏幕上长按即可报警\n屏幕上点击即可开启屏幕光", 2,
					true);
			if (!hasFlashlight) {
				ToastCustom.toast(TorchActivity.this, "您的设备不支持闪光灯\n但您可以使用屏幕光哦" );
			}
		}
		// 自动打开
		if (auto) {
			new Handler().postDelayed(new Runnable() {
				public void run() {
					turnOn();
				}
			}, 10);
		}
	}

	private void initView() {
		button_torch = (Button) findViewById(R.id.button1);
		image_screen = (ImageView) findViewById(R.id.wallper);
		mSurface = (FlashlightSurface) findViewById(R.id.surfaceview);
		image_torch = (ImageView) findViewById(R.id.open);
	}

	private void initEvents() {
		mScreenObserver = new ScreenObserver(this);
		mScreenObserver
				.requestScreenStateUpdate(new ScreenObserver.ScreenStateListener() {
					public void onScreenOn() {
						if (isFlashlightOn) {
							turnOn();
						}
					}

					public void onScreenOff() {
						if (isFlashlightOn) {
							power_count = 1;
						}
					}
				});
		// 按钮声音
		button_torch.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					mediaPlayer.start();
				} catch (Exception e) {
				}
				if (isFlashlightOn) {
					turnOff();
				} else {
					turnOn();
				}
			}
		});
		image_torch.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (screen_count == 1) {
					try {
						screenBrightness = Settings.System.getInt(
								getContentResolver(),
								Settings.System.SCREEN_BRIGHTNESS);
					} catch (Exception e) {
					}
					Settings.System.putInt(getContentResolver(),
							Settings.System.SCREEN_BRIGHTNESS, 200);
					image_screen.setBackgroundResource(R.color.white);
					image_screen.setVisibility(View.VISIBLE);
					screen_count = 2;
				} else {
					Settings.System
							.putInt(getContentResolver(),
									Settings.System.SCREEN_BRIGHTNESS,
									screenBrightness);
					image_screen.setVisibility(View.GONE);
					screen_count = 1;
				}
			}
		});

		image_torch.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View v) {
				// 长按报警，长按取消
				int max = audioManager
						.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, max, 0);
				if (!isPlaying) {
					try {
						alarmPlayer.setVolume(1, 1);
						alarmPlayer.seekTo(0);
						alarmPlayer.start();
						alarmPlayer.setLooping(true);
						isPlaying = true;
					} catch (Exception e) {
					}
				} else {
					// 音乐停止的实现，用pause配合seekTo
					alarmPlayer.pause();
					isPlaying = false;
				}
				// 屏蔽掉点击事件
				return true;
			}
		});

	}

	private void initAudio() {
		mediaPlayer = MediaPlayer.create(this, R.raw.sound);
		alarmPlayer = MediaPlayer.create(this, R.raw.alarm);
		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
	}

	private void turnOn() {
		mSurface.setFlashlightSwitch(true);
		isFlashlightOn = true;
		image_torch.setBackgroundResource(R.drawable.torch_on);
	}

	private void turnOff() {
		mSurface.setFlashlightSwitch(false);
		isFlashlightOn = false;
		image_torch.setBackgroundResource(R.drawable.torch_off);
	}

	// 保存当前的状态，临时状态，主页，屏幕旋转，关屏
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	protected void onPause() {
		super.onPause();
	}

	// 释放资源
	protected void onDestroy() {
		super.onDestroy();
		image_torch.setBackgroundResource(R.drawable.torch_off);
		// 恢复音量
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume,
				0);
		// 停止监听screen状态
		mScreenObserver.stopScreenStateUpdate();
		// 停止报警
		if (isPlaying) {
			alarmPlayer.stop();
			isPlaying = false;
		}
		// 恢复屏幕亮度
		if (screen_count == 2) {
			Settings.System.putInt(getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS, screenBrightness);
		}

	}

	// 恢复之前的状态
	protected void onResume() {
		super.onResume();
		// 读取设置，会有更改
		if (auto) {
			new Handler().postDelayed(new Runnable() {
				public void run() {
					turnOn();
				}
			}, 10);
		}
		if (power_count != 1) {
			image_torch.setBackgroundResource(R.drawable.torch_off);
			isFlashlightOn = false;
		}

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AnimUtil.animBackSlideFinish(this);
			return true;
		}
		return true;
	}

}
