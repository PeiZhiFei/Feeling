package com.feifei.checkpackage;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.feifei.util.ToastUtil;

public class DatabaseHelper extends SQLiteOpenHelper {
	private SQLiteDatabase db;
	private final Context context;
	private static final int VERSION = 1;

	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		this.context = context;

	}

	public DatabaseHelper(Context context, String name) {
		this(context, name, null, VERSION);
		db = this.getReadableDatabase();
	}

	public void onCreate(SQLiteDatabase db) {

	}

	public void insertTmpData() {
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	// 数据库的查询函数
	public Cursor rawQuery(String sql) {
		return db.rawQuery(sql, null);
	}

	public boolean execSQL(String sql) {
		try {
			db.execSQL(sql);
		} catch (SQLException e) {
			ToastUtil.toast(context, "android.database.sqlite.SQLiteException");
			return false;
		}
		return true;
	}

	public boolean execSQL(String sql, boolean Throw) {
		try {
			db.execSQL(sql);
		} catch (SQLException e) {
			if (Throw) {
				throw e;
			}
			return false;
		}
		return true;
	}

	// 封装系统的执行sql语句的函数
	public boolean execSQL(String sql, Object[] object) {
		try {
			db.execSQL(sql, object);
		} catch (SQLException e) {
			e.printStackTrace();
			ToastUtil.toast(context, e.getMessage());
			return false;
		}
		return true;
	}

	public void beginTransaction() {
		db.beginTransaction();
	}

	public void setTransactionSuccessful() {
		db.setTransactionSuccessful();
	}

	public void endTransaction() {
		db.endTransaction();
	}

	public void close() {
		db.close();
	}

}
