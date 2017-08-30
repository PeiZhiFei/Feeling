package com.feifei.lifetools;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.os.Looper;

import com.feifei.util.DialogUtil;
import com.feifei.util.DialogView.DialogViewListener;
import com.feifei.util.FileUtil;
import com.feifei.util.LogUtil;
import com.feifei.util.SendUtil;
import com.feifei.util.ToastUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MyException implements UncaughtExceptionHandler {
	private static MyException myException;
	private Context context;
	private final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
	// 用来存储设备信息和异常信息
	private final Map<String, String> infos = new HashMap<String, String>();
	String path = FileUtil.getRootPath() + "log/";

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	private MyException() {
	}

	/**
	 * @return
	 * @notice 同步方法，以免单例多线程环境下出现异常
	 */
	public synchronized static MyException getInstance() {
		if (myException == null) {
			myException = new MyException();
		}
		return myException;
	}

	public void init() {
		Thread.setDefaultUncaughtExceptionHandler(myException);
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		saveCrashInfo2File(ex);
		LogUtil.log("uncaughtException thread : " + thread + "||name=" + thread.getName() + "||id=" + thread.getId() + "||exception=" + ex);
		showDialog();
	}

	private void showDialog() {
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				ToastUtil.toast(context, "123");
				ArrayList<String> list = new ArrayList<String>();
				list.add("很抱歉，生活小助手崩溃了");
				DialogUtil.dialogStyle1(context, "提示", "我知道了!", true, list, new DialogViewListener() {
					@Override
					public void onRightClick() {
					}

					@Override
					public void onListItemClick(int position, String string) {
					}

					@Override
					public void onButtonClick() {
						((Activity) context).finish();
					}

					@Override
					public void onLeftClick() {

					}
				});
				Looper.loop();
			}
		}.start();
	}

	/**
	 * @param ex
	 * @return
	 * @notice 保存错误信息到文件中，返回文件名称,便于将文件传送到服务器
	 */
	private String saveCrashInfo2File(Throwable ex) {
		StringBuffer sb = new StringBuffer();
		String time = formatter.format(new Date());
		sb.append("\n" + time + "----");
		for (Map.Entry<String, String> entry : infos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\n");
		}

		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();

		String result = writer.toString();
		sb.append(result);
		SendUtil.sendMail(result);
		try {
			String fileName = "exception.log";
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				File dir = new File(path);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				FileOutputStream fos = new FileOutputStream(path + fileName, true);
				fos.write(sb.toString().getBytes());
				fos.close();
			}
			return fileName;
		}
		catch (Exception e) {
			LogUtil.log("写文件的时候发生了错误" + e);
		}
		return null;
	}
}
