package com.feifei.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;

import java.lang.reflect.Method;

public class NetworkUtil {

	private static final int NETWORN_NONE = 0;
	private static final int NETWORN_WIFI = 1;
	private static final int NETWORN_MOBILE = 2;

	/**
	 * @param context
	 * @return 0：没有网络，1：WIFI，2：数据；
	 */
	public static int getNetworkState(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		if (state == State.CONNECTED || state == State.CONNECTING) {
			return NETWORN_WIFI;
		}
		state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();
		if (state == State.CONNECTED || state == State.CONNECTING) {
			return NETWORN_MOBILE;
		}
		return NETWORN_NONE;
	}

	public static boolean isNetworkAvailable(Context context) {
		boolean result = false;
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager == null ? null
				: connectivityManager.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isAvailable()) {
			result = true;
			return result;
		}
		return result;
	}

	public static boolean isWifi(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null
				&& activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}

	public static boolean is3G(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null
				&& activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
			return true;
		}
		return false;
	}

	public static void wifiOpen(Context context) {
		WifiManager wifimanager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		wifimanager.setWifiEnabled(true);
	}

	public static void wifiClose(Context context) {
		WifiManager wifimanager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		wifimanager.setWifiEnabled(false);
	}

	/**
	 * @param context
	 * @return boolean
	 * @notice 自动打开数据网络
	 * @notice <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
	 * @notice <uses-permission android:name="android.permission.CHANGE_NETWORK_STATE"/>
	 */
	@SuppressWarnings("rawtypes")
	public static boolean gprsOpen(Context context) {
		ConnectivityManager mCM = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		Class<? extends ConnectivityManager> cmClass = mCM.getClass();
		Class[] argClasses = null;
		Object[] argObject = null;
		Boolean isOpen = false;
		try {
			Method method = cmClass.getMethod("getMobileDataEnabled",
					argClasses);
			isOpen = (Boolean) method.invoke(mCM, argObject);
		} catch (Exception e) {
		}
		if (isOpen == false) {
			Class<? extends ConnectivityManager> cmClass2 = mCM.getClass();
			Class[] argClasses2 = new Class[1];
			argClasses2[0] = boolean.class;
			try {
				Method method = cmClass2.getMethod("setMobileDataEnabled",
						argClasses2);
				method.invoke(mCM, true);
			} catch (Exception e) {
			}
		}
		return isOpen;
	}

	/**
	 * @param context
	 * @return boolean
	 * @notice 自动关闭数据网络
	 */
	@SuppressWarnings("rawtypes")
	public static boolean gprsClose(Context context) {
		ConnectivityManager mCM = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		Class<? extends ConnectivityManager> cmClass = mCM.getClass();
		Class[] argClasses = null;
		Object[] argObject = null;
		Boolean isOpen = false;
		try {
			Method method = cmClass.getMethod("getMobileDataEnabled",
					argClasses);
			isOpen = (Boolean) method.invoke(mCM, argObject);
		} catch (Exception e) {
		}
		if (isOpen == true) {
			Class<? extends ConnectivityManager> cmClass2 = mCM.getClass();
			Class[] argClasses2 = new Class[1];
			argClasses2[0] = boolean.class;
			try {
				Method method = cmClass2.getMethod("setMobileDataEnabled",
						argClasses2);
				method.invoke(mCM, false);
			} catch (Exception e) {
			}
		}
		return isOpen;
	}

}
