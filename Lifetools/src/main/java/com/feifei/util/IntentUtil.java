package com.feifei.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import java.io.File;

/**
 * @author   裴智飞
 * @date       2014-7-28
 * @date       下午9:06:12
 * @file         IntentUtil.java
 * @content  Intent跳转类，含动画
 */
public class IntentUtil {

	/**
	 * @param context
	 * @param c
	 * @notice 无动画的跳转
	 */
	public static void intentNoAnim(Context context, Class<?> c) {
		intent(context, c);
	}

	/**
	 * @param context
	 * @param c：传入类名，如MainActivity.class
	 * @notice 跳转到下一级界面 ,当前activity不finish
	 */
	public static void intentTo(Context context, Class<?> c) {
		intent(context, c);
		AnimUtil.animToSlide((Activity) context);
	}

	/**
	 * @param context
	 * @param c：传入类名，如MainActivity.class
	 * @notice 返回到上一级界面 ,当前activity不finish
	 */
	public static void intentBack(Context context, Class<?> c) {
		intent(context, c);
		AnimUtil.animBackSlide((Activity) context);
	}

	/**
	 * @param context
	 * @param c：传入类名，如MainActivity.class
	 * @notice 跳转到下一级界面 ,当前activity会finish
	 */
	public static void intentToFinish(Context context, Class<?> c) {
		intent(context, c);
		AnimUtil.animToSlideFinish((Activity) context);
	}

	/**
	 * @param context
	 * @param c：传入类名，如MainActivity.class
	 * @notice 返回到上一级界面 ,当前activity会finish
	 */
	public static void intentBackFinish(Context context, Class<?> c) {
		intent(context, c);
		AnimUtil.animBackSlideFinish((Activity) context);
	}

	/**
	 * @param context
	 * @param c：传入类名，如MainActivity.class
	 * @param code：返回值
	 * @notice 带动画的有返回值的启动，不支持bundle
	 */
	public static void intentResult(Context context, Class<?> c, int code) {
		Intent intent = new Intent(context, c);
		((Activity) context).startActivityForResult(intent, code);
		AnimUtil.animTo((Activity) context);
	}

	/**
	 * @param context
	 * @param c：传入类名，如MainActivity.class
	 * @param bundle
	 * @notice 含有Bundle通过Class跳转界面
	 */
	public static void intentTo(Context context, Class<?> c, Bundle bundle) {
		Intent intent = new Intent();
		intent.setClass(context, c);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		context.startActivity(intent);
		AnimUtil.animTo((Activity) context);
	}

	/**
	 * @param context
	 * @param phone：电话号码
	 * @notice 跳到拨号界面
	 */
	public static void intentToDial(Context context, String phone) {
		intent(context, Intent.ACTION_DIAL, "tel:" + phone);
	}

	/**
	 * @param context
	 * @param phone：电话号码
	 * @notice 直接拨号，需要加权限
	 * @notice <uses-permission android:name="android.permission.CALL_PHONE" />
	 */
	public static void intentToCall(Context context, String phone) {
		intent(context, Intent.ACTION_CALL, "tel:" + phone);
	}

	/**
	 * @param context
	 * @param toPeople：接收者
	 * @param content：内容
	 * @notice 发送短信
	 */
	public static void intentToMessage(Context context, String toPeople,
			String content) {
		try {
			Uri smsUri = Uri.parse("smsto:" + toPeople);
			Intent intent = new Intent(Intent.ACTION_SENDTO, smsUri);
			intent.putExtra("sms_body", content);
			context.startActivity(intent);
			AnimUtil.animTo((Activity) context);
		} catch (Exception e) {
			exception(context, e);
		}
	}

	/**
	 * @param context
	 * @param string
	 * @notice 分享
	 */
	public static void intentToShare(Context context, String string) {
		Intent intent = new Intent("android.intent.action.SEND");
		intent.setType("image/*");
		intent.putExtra("sms_body", "分享");
		intent.putExtra("android.intent.extra.TEXT", "分享");
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(Intent.createChooser(intent, "分享到"));
	}

	/**
	 * @param context
	 * @param emaiAddress：邮件地址
	 * @param title：标题
	 * @param content：内容
	 * @notice 跳到发邮件界面
	 */
	public static void intentToEmail(Context context, String emaiAddress,
			String title, String content) {
		try {
			Uri uri = Uri.parse("mailto:" + emaiAddress);
			Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
			intent.putExtra(Intent.EXTRA_SUBJECT, title);
			intent.putExtra(Intent.EXTRA_TEXT, content);
			context.startActivity(intent);
			AnimUtil.animTo((Activity) context);
		} catch (Exception e) {
			exception(context, e);
		}
	}

	/**
	 * @param context
	 * @param weidu：经度
	 * @param jingdu：维度
	 * @notice 跳转到地图
	 */
	public static void intentToMap(Context context, String weidu, String jingdu) {
		try {
			Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
					Uri.parse("geo:" + weidu + "," + jingdu));
			context.startActivity(intent);
			AnimUtil.animTo((Activity) context);
		} catch (Exception e) {
			exception(context, e);
		}
	}

	/**
	 * @param context
	 * @param string
	 * @notice 跳到浏览器界面
	 */
	public static void intentToInternet(Context context, String string) {
		intent(context, Intent.ACTION_VIEW, string);
	}

	/**
	 * @param context
	 * @notice 跳转到设置界面
	 */
	public static void intentToSetting(Context context) {
		intent(context, Settings.ACTION_SETTINGS, null);
	}

	/**
	 * @param context
	 * @notice 跳转到WIFI设置界面
	 */
	public static void intentToWifi(Context context) {
		intent(context, Settings.ACTION_WIFI_SETTINGS, null);
	}

	/**
	 * @param context
	 * @notice 跳转到数据网络设置界面
	 */
	public static void intentToGprs(Context context) {
		intent(context, Settings.ACTION_DATA_ROAMING_SETTINGS, null);
	}

	/**
	 * @param context
	 * @notice 跳转到GPS界面
	 */
	public static void intentToGPS(Context context) {
		intent(context, Settings.ACTION_LOCATION_SOURCE_SETTINGS, null);
	}

	/**
	 * @param context
	 * @param string：媒体的路径
	 * @notice 播放多媒体
	 */
	public static void intentToPlay(Context context, String string) {
		intent(context, Intent.ACTION_VIEW, string);
	}

	/**
	 * @param context
	 * @param string：应用包名
	 * @notice 去应用商店评分
	 */
	public static void intentToMarket(Context context, String string) {
		intent(context, Intent.ACTION_VIEW, "market://details?id=" + string);
	}

	/**
	 * @param context
	 * @param string：应用包名或者文字
	 * @notice 去应用商店搜索应用
	 */
	public static void intentToMarketSearch(Context context, String string) {
		intent(context, Intent.ACTION_VIEW, "market://search?q=" + string);
	}

	/**
	 * @param context
	 * @param filepath：文件路径
	 * @return boolean：是否成功
	 * @notice 自动安装APK
	 */
	public static boolean install(Context context, String filepath) {
		File apkfile = new File(filepath);
		if (!apkfile.exists()) {
			LogUtil.log(apkfile + "不存在");
			return false;
		}
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
		return true;
	}

	/**
	 * @param context
	 * @param c：传入类名，如MyWebView.class
	 * @param string：intent存放的string值，给WebView用的
	 * @notice 通过Class跳转界面 ,传一个String过去，WebView使用
	 */
	public static void startWebview(Context context, Class<?> c, String string,
			boolean dialog) {
		Intent intent = new Intent();
		intent.putExtra("url", string);
		intent.putExtra("dialog", dialog);
		intent.setClass(context, c);
		context.startActivity(intent);
		AnimUtil.animTo((Activity) context);
	}

	/**
	 * @param context
	 * @param packageName：包名
	 * @notice 打开指定包名的程序
	 */
	public static void intentToApp(Context context, String packageName) {
		Intent intent = context.getPackageManager().getLaunchIntentForPackage(
				packageName);
		context.startActivity(intent);
		AnimUtil.animToSlide((Activity) context);
	}

	/**
	 * @param context
	 * @param packageName：包名
	 * @param activityName：完整类名，含包名
	 * @notice 打开指定包名的指定activity
	 */
	public static void intentToAppActivity(Context context, String packageName,
			String activityName) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setClassName(packageName, activityName);
		context.startActivity(intent);
		AnimUtil.animToSlide((Activity) context);
	}

	private static void intent(Context context, String action, String string) {
		try {
			Intent intent = new Intent(action, Uri.parse(string));
			context.startActivity(intent);
			AnimUtil.animTo((Activity) context);
		} catch (Exception e) {
			exception(context, e);
		}
	}

	private static void intent(Context context, Class<?> c) {
		Intent intent = new Intent(context, c);
		context.startActivity(intent);
	}

	private static void exception(Context context, Exception e) {
		e.printStackTrace();
		LogUtil.log(e.getMessage());
		ToastUtil.toast(context, "没有找到相关的应用");
	}

}
