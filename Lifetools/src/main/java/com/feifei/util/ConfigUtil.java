package com.feifei.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @author   裴智飞
 * @date       2014-7-28
 * @date       下午9:01:11
 * @file           ConfigUtil.java
 * @content  SharePreference类的读写方法和文件的读写
 */
public class ConfigUtil {

	/**
	 * @param context
	 * @param key 键
	 * @param value 值
	 * @notice 写入字符串
	 */
	public static void writeString(Context context, String key, String value) {
		SharedPreferences share = context.getSharedPreferences("perference", Context.MODE_PRIVATE);
		Editor editor = share.edit();
		editor.putString(key, value).commit();
	}

	/**
	 * @param context
	 * @param key 键
	 * @param defaultValue 默认值
	 * @return String
	 * @notice 读取字符串
	 */
	public static String readString(Context context, String key, String defaultValue) {
		SharedPreferences share = context.getSharedPreferences("perference", Context.MODE_PRIVATE);
		return share.getString(key, defaultValue);
	}

	/**
	 * @param context
	 * @param key 键
	 * @param value 值
	 * @notice 写入整型
	 */
	public static void writeInt(Context context, String key, int value) {
		SharedPreferences share = context.getSharedPreferences("perference", Context.MODE_PRIVATE);
		Editor editor = share.edit();
		editor.putInt(key, value).commit();
	}

	/**
	 * @param context
	 * @param key 键
	 * @param defaultValue 默认值
	 * @return int
	 * @notice 读取整型
	 */
	public static int readInt(Context context, String key, int defaultValue) {
		SharedPreferences share = context.getSharedPreferences("perference", Context.MODE_PRIVATE);
		return share.getInt(key, defaultValue);
	}

	/**
	 * @param context
	 * @param key 键
	 * @param value 值
	 * @notice 写入布尔值
	 */
	public static void writeBoolean(Context context, String key, boolean value) {
		SharedPreferences share = context.getSharedPreferences("perference", Context.MODE_PRIVATE);
		Editor editor = share.edit();
		editor.putBoolean(key, value).commit();
	}

	/**
	 * @param context
	 * @param key 键
	 * @param defaultValue 默认值
	 * @return boolean
	 * @notice 读取布尔值
	 */
	public static boolean readBoolean(Context context, String key, boolean defaultValue) {
		SharedPreferences share = context.getSharedPreferences("perference", Context.MODE_PRIVATE);
		return share.getBoolean(key, defaultValue);
	}

	/**
	 * @param context
	 * @param key 键
	 * @param value 值
	 * @notice 写入小数
	 */
	public static void writeFloat(Context context, String key, float value) {
		SharedPreferences share = context.getSharedPreferences("perference", Context.MODE_PRIVATE);
		Editor editor = share.edit();
		editor.putFloat(key, value).commit();
	}

	/**
	 * @param context
	 * @param key 键
	 * @param defaultValue 默认值
	 * @return float
	 * @notice 读取小数
	 */
	public static float readFloat(Context context, String key, float defaultValue) {
		SharedPreferences share = context.getSharedPreferences("perference", Context.MODE_PRIVATE);
		return share.getFloat(key, defaultValue);
	}

	/**
	 * @param context
	 * @param key 键
	 * @param value
	 * @notice 写入长整数
	 */
	public static void writeLong(Context context, String key, long value) {
		SharedPreferences share = context.getSharedPreferences("perference", Context.MODE_PRIVATE);
		Editor editor = share.edit();
		editor.putLong(key, value).commit();
	}

	/**
	 * @param context
	 * @param key 键
	 * @param defaultValue 默认值
	 * @return
	 * @notice 读取长整数
	 */
	public static long readLong(Context context, String key, long defaultValue) {
		SharedPreferences share = context.getSharedPreferences("perference", Context.MODE_PRIVATE);
		return share.getLong(key, defaultValue);
	}

	/**
	 * @param context
	 * @param fileName
	 * @param text
	 * @notice 写文件
	 */
	public static void writeFile(Context context, String fileName, String text) {
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = context.openFileOutput(fileName, Context.MODE_APPEND);
			fileOutputStream.write(text.getBytes());
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (fileOutputStream != null) {
				try {
					fileOutputStream.flush();
					fileOutputStream.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	}

	/**
	 * @param context
	 * @param string
	 * @return
	 * @notice 读文件
	 */
	public static String readFile(Context context, String string) {
		FileInputStream fileInputStream = null;
		String textString = "";
		try {
			fileInputStream = context.openFileInput(string);
			if (fileInputStream.available() == 0) {
				return null;
			}

			byte[] bytes = new byte[fileInputStream.available()];
			while (fileInputStream.read(bytes) != -1) {
			}
			textString = new String(bytes);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return textString;
	}

	/**
	 * @param context
	 * @param xml
	 * @return
	 * @notice 解析XML的样例
	 */
	public String readXml(Context context, int xml) {
		XmlPullParser parser = context.getResources().getXml(xml);
		String tString = "";
		try {
			while (parser.next() != XmlPullParser.END_DOCUMENT) {
				String peopleString = parser.getName();
				String nameString = null;
				String ageString = null;
				String widthString = null;

				if ((peopleString != null) && peopleString.equals("View")) {
					int count = parser.getAttributeCount();
					for (int i = 0; i < count; i++) {
						String attrNameString = parser.getAttributeName(i);
						String attrNameValue = parser.getAttributeValue(i);
						if ((attrNameString != null) && attrNameString.equals("name")) {
							nameString = attrNameValue;
						}
						else if ((attrNameString != null) && attrNameString.equals("age")) {
							ageString = attrNameValue;

						}
						else if ((attrNameString != null) && attrNameString.equals("widht")) {
							widthString = attrNameValue;

						}
					}

					if ((nameString != null) && (ageString != null) && (widthString != null)) {
						tString += "姓名：" + nameString + "，年龄：" + ageString + "，身高：" + widthString + "\n";
					}

				}

			}

		}
		catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return tString;
	}
}
