package com.feifei.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.util.Log;

/**
 * @author   裴智飞
 * @date       2014-7-28
 * @date       下午9:13:21
 * @file         LogUtil.java
 * @content  增强型的Log，可以输出各种类型，不用考空指针
 */
public class LogUtil {

	/**
	 * @param msg
	 * @notice 数字输出
	 */
	public static void log(int msg) {
		Log.v("LOG", "" + msg);
	}

	/**
	 * @param msg
	 * @notice 浮点数输出
	 */
	public static void log(double msg) {
		Log.v("LOG", "" + msg);
	}

	/**
	 * @param msg
	 * @notice byte数组输出
	 */
	public static void log(byte[] msg) {
		for (byte element : msg) {
			Log.v("LOG", "" + element);
		}
	}

	/**
	 * @param msg
	 * @notice 布尔值输出
	 */
	public static void log(boolean msg) {
		if (msg) {
			Log.v("LOG", "现在变量是true");
		} else {
			Log.v("LOG", "现在变量是false");
		}
	}

	/**
	 * @param msg
	 * @notice 字符串输出
	 */
	public static void log(String msg) {
		Log.v("LOG", "" + msg);
	}

	/**
	 * @param date
	 * @notice 日期输出
	 */
	public static void log(Date date) {
		Log.v("LOG", "" + date);
	}

	/**
	 * @param msg
	 * @notice String[]数组输出
	 */
	public static void log(String[] msg) {
		String result = "";
		for (String string : msg) {
			result += string + "\n";
		}
		Log.v("LOG", "" + result);
	}

	/**
	 * @param msg
	 * @notice ArrayList输出
	 */
	public static void log(ArrayList<String> msg) {
		String result = "";
		for (String string : msg) {
			result += string + "\n";
		}
		Log.v("LOG", "" + result);
	}

	/**
	 * @param msg
	 * @notice List输出
	 */
	public static void log(List<String> msg) {
		String result = "";
		for (String string : msg) {
			result += string + "\n";
		}
		Log.v("LOG", "" + result);
	}

	/**
	 * @param msg
	 * @notice Error输出
	 */
	public static void logError(String msg) {
		Log.e("LOG", "" + msg);
	}

	/**
	 * @param tag
	 * @param msg
	 * @notice 自定义tag
	 */
	public static void logTag(String tag, String msg) {
		Log.v(tag, "" + msg);
	}

}
