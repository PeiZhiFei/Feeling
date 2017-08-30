package com.feifei.checkpackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;

import com.feifei.lifetools.R;
import com.feifei.util.ConfigUtil;

public class DatabaseUtil {

	// 用于写入配置文件 判断是否已经加载了数据库
	public static final String HASLOAD_DATABASE = "hasload_database";
	public static DatabaseHelper dbh;

	// 加载数据流程：第一次安装该应用的时候，就创建数据库
	// 并且解析数据插入数据库 SD卡 创建数据库目录
	// 第一次运行应用程序时，加载数据库到data/data/当前包的名称/database/<db_name>
	public static void loadDatabase(Context context) throws Exception {
		File dir = new File("data/data/" + context.getPackageName()
				+ "/databases");
		if (!dir.exists() || !dir.isDirectory()) {
			dir.mkdir();
		}
		File file = new File(dir, "mypackage");
		// file.delete() ;
		if (!file.exists()) {
			// 获得封装.db文件的InputStream对象
			InputStream is = context.getResources().openRawResource(
					R.raw.mypackage);
			FileOutputStream fos = new FileOutputStream("data/data/"
					+ context.getPackageName() + "/databases/mypackage");
			byte[] buffer = new byte[7168];
			int count = 0;
			// 开始复制.db文件
			while ((count = is.read(buffer)) > 0) {
				fos.write(buffer, 0, count);
			}
			fos.close();
			is.close();
		}
	}

	// 解析json数据插入数据库
	public static void readData2Db(Context context) {
		InputStream in = context.getResources().openRawResource(
				R.raw.companyinfos);
		try {
			// 将in读入reader 中
			BufferedReader br = new BufferedReader(new InputStreamReader(in,
					"GBK"));
			StringBuffer buffer = new StringBuffer("");
			String tem = "";
			while ((tem = br.readLine()) != null) {
				buffer.append(tem);
			}
			br.close();
			JSONObject myjson = new JSONObject(buffer.toString());
			JSONArray jsonarr = myjson.getJSONArray("companyinfos");
			CompanyInfo companyinfo = new CompanyInfo();
			for (int i = 0; i < jsonarr.length(); i++) {
				JSONObject njson = jsonarr.getJSONObject(i);
				String compName = njson.getString("name");
				String compId = njson.getString("id");
				String helpinfo = njson.getString("helpinfo");
				String phoneNumber = njson.getString("phonenumber");
				companyinfo.info_cd = companyinfo
						.getMaxIndexNo(DatabaseUtil.dbh);
				companyinfo.name = compName;
				companyinfo.id = compId;
				companyinfo.count = "0";
				companyinfo.helpInfo = helpinfo;
				companyinfo.phoneNumber = phoneNumber;
				companyinfo.addData(DatabaseUtil.dbh);
			}
			ConfigUtil.writeString(context, DatabaseUtil.HASLOAD_DATABASE, "1");
			// 加载数据库完成，写入成功标志位
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public static void setClassValueBycursor(Object obj, Cursor cursor) {
		int ColCount = cursor.getColumnCount();
		int i = 0;
		for (i = 0; i < ColCount; i++) {
			String ColName = cursor.getColumnName(i);

			try {
				Field f = obj.getClass().getField(ColName);
				String ret = cursor.getString(i);
				if (f == null) {
					continue;
				}
				if (ret == null) {
					ret = "";
				}
				f.set(obj, ret);
			} catch (SecurityException e) {
			} catch (NoSuchFieldException e) {
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			}
		}
	}

	public static Map<String, Object> getListObjectBycursor(Cursor cursor) {
		int ColCount = cursor.getColumnCount();
		int i = 0;
		Map<String, Object> map = new HashMap<String, Object>();

		for (i = 0; i < ColCount; i++) {
			String ColName = cursor.getColumnName(i);
			try {

				String ret = cursor.getString(i);
				if (ret == null) {
					ret = "";
				}
				map.put(ColName, ret);
			} catch (SecurityException e) {
			} catch (IllegalArgumentException e) {
			}
		}
		return map;
	}

}
