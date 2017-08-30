package com.feifei.lifetools;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;

import java.lang.reflect.Method;

/**
 * @author   裴智飞
 * @date       2014-7-24
 * @date       下午2:40:45
 * @file         ScreenObserver.java
 * @content  屏幕唤醒或锁屏会发送广播，实现接口中的2个方法
 */
public class ScreenObserver {

	private final Context mContext;
	private final ScreenBroadcastReceiver mScreenReceiver;
	private ScreenStateListener mScreenStateListener;
	private static Method mReflectScreenState;

	public ScreenObserver(Context context) {
		mContext = context;
		mScreenReceiver = new ScreenBroadcastReceiver();
		try {
			mReflectScreenState = PowerManager.class.getMethod("isScreenOn",
					new Class[] {});
		} catch (NoSuchMethodException nsme) {
		}
	}

	// screen状态广播接收者
	private class ScreenBroadcastReceiver extends BroadcastReceiver {
		private String action = null;

		public void onReceive(Context context, Intent intent) {
			action = intent.getAction();
			if (Intent.ACTION_SCREEN_ON.equals(action)) {
				mScreenStateListener.onScreenOn();
			} else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
				mScreenStateListener.onScreenOff();
			}
		}
	}

	// 请求screen状态更新
	public void requestScreenStateUpdate(ScreenStateListener listener) {
		mScreenStateListener = listener;
		startScreenBroadcastReceiver();
		firstGetScreenState();
	}

	// 第一次请求screen状态
	private void firstGetScreenState() {
		PowerManager manager = (PowerManager) mContext
				.getSystemService(Activity.POWER_SERVICE);
		if (isScreenOn(manager)) {
			if (mScreenStateListener != null) {
				mScreenStateListener.onScreenOn();
			}
		} else {
			if (mScreenStateListener != null) {
				mScreenStateListener.onScreenOff();
			}
		}
	}

	// 停止screen状态更新
	public void stopScreenStateUpdate() {
		mContext.unregisterReceiver(mScreenReceiver);
	}

	// 启动screen状态广播接收器
	private void startScreenBroadcastReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		mContext.registerReceiver(mScreenReceiver, filter);
	}

	// screen是否打开状态
	private static boolean isScreenOn(PowerManager pm) {
		boolean screenState;
		try {
			screenState = (Boolean) mReflectScreenState.invoke(pm);
		} catch (Exception e) {
			screenState = false;
		}
		return screenState;
	}

	public interface ScreenStateListener {

		public void onScreenOn();

		public void onScreenOff();
	}
}