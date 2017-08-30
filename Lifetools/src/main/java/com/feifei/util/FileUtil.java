package com.feifei.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class FileUtil {

	/**
	 * @return
	 * @notice 获取SDCard根目录
	 */
	public static String getRootPath() {
		if (isSdcardAvailable()) {
			return Environment.getExternalStorageDirectory().toString() + "/";
		} else {
			return "";
		}
	}

	/**
	 * @param filepath
	 * @return
	 * @notice 判断文件是否存在
	 */
	public static boolean isFileExist(String filepath) {
		if (isSdcardAvailable()) {
			File file = new File(filepath);
			file.isFile();
			return file.exists();
		} else {
			return false;
		}
	}

	/**
	 * @param filepath
	 *            ：文件路径全名
	 * @notice 判断文件是否存在，不存在则创建
	 */
	public static void isFileExistAndCreat(String filepath) {
		if (isSdcardAvailable()) {
			File file = new File(filepath);
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @param filepath
	 *            ：文件夹路径名
	 * @notice 判断文件夹是否存在，不存在则创建路径
	 */
	public static void isDirExistAndCreat(String filepath) {
		if (isSdcardAvailable()) {
			File file = new File(filepath);
			if (!file.exists()) {
				file.mkdirs();
			}
		} else {
			LogUtil.log("没有存储卡");
		}
	}

	//
	/**
	 * @param file
	 *            ：File
	 * @notice 判断文件夹是否存在，不存在则创建路径
	 */
	public static void isDirExistAndCreat(File file) {
		if (isSdcardAvailable()) {
			if (!file.exists()) {
				file.mkdirs();
			}
		}
	}

	/**
	 * @param filepath
	 * @return
	 * @throws IOException
	 * @notice File转为String
	 */
	public static String file2String(String filepath) {
		if (isFileExist(filepath)) {
			String text = null;
			InputStream is = null;
			try {
				is = new FileInputStream(new File(filepath));
				text = ConvertUtil.stream2String(is);
			} catch (Exception e) {
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
					}
				}
			}
			return text;
		} else {
			return filepath + "文件不存在";
		}
	}

	/**
	 * @param path
	 * @return
	 * @notice File转为Bitmap
	 */
	public static Bitmap file2Bitmap(String path) {
		Bitmap bitmap = null;
		File file = new File(path);
		if (file.exists()) {
			bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
		}
		return bitmap;
	}

	/**
	 * @param path
	 *            ：路径
	 * @param filename
	 *            ：文件名
	 * @param string
	 * @notice 保存文本文件
	 */
	public static void saveText(String path, String filename, String string) {
		// 创建目录
		isFileExistAndCreat(path);
		File targetFile = new File(path + filename);
		OutputStreamWriter osw;
		try {
			// 创建文件
			if (!targetFile.exists()) {
				targetFile.createNewFile();
				osw = new OutputStreamWriter(new FileOutputStream(targetFile), "UTF-8");
				osw.write(string);
				osw.close();
			} else {
				// 再次写入时不采用拼接的方法,而是重新写
				osw = new OutputStreamWriter(new FileOutputStream(targetFile, false), "UTF-8");
				osw.write(string);
				osw.flush();
				osw.close();
			}
		} catch (Exception e) {
		}
	}

	/**
	 * @param bm
	 * @param path
	 *            ：路径
	 * @param filename
	 *            ：文件名，不包含后缀如.jpg
	 * @notice 保存图片
	 */
	public static void saveBitmap(Bitmap bm, String path, String filename) {
		try {
			File f = new File(path + filename + ".JPEG");
			if (f.exists()) {
				f.delete();
			}
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param filePath
	 * @return byte数组
	 * @notice 文件转为byte
	 */
	public static byte[] file2Byte(String filePath) {
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}

	/**
	 * @param byteArray
	 * @param path
	 *            ：文件路径
	 * @param filename
	 *            ：文件名
	 * @notice 根据byte[]生成文件
	 */
	public static void saveByte(byte[] byteArray, String path, String filename) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		isDirExistAndCreat(path);
		isFileExistAndCreat(filename);
		try {
			fos = new FileOutputStream(path + filename);
			bos = new BufferedOutputStream(fos);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			bos.write(byteArray);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e1) {
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e1) {
				}
			}
		}
	}

	/**
	 * @return
	 * @notice 检查SD卡是否可用
	 */
	public static boolean isSdcardAvailable() {
		return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
	}

	/**
	 * @param filepath
	 * @notice 删除文件
	 */
	public static void deleteFile(String filepath) {
		File file = new File(filepath);
		if (file.isFile()) {
			file.delete();
		}
		file.exists();
	}

	/**
	 * @param path
	 * @notice 删除目录
	 */
	public static void deleteDir(String path) {
		File dir = new File(path);
		if (dir == null || !dir.exists() || !dir.isDirectory()) {
			return;
		}
		for (File file : dir.listFiles()) {
			if (file.isFile()) {
				file.delete();
			} else if (file.isDirectory()) {
				// 递规的方式删除文件夹
				deleteDir(path);
			}
		}
		dir.delete();
	}

	/**
	 * @param sourceFile
	 * @param targetFile
	 * @throws IOException
	 * @notice 复制文件
	 */
	public static void copyFile(File sourceFile, File targetFile) throws IOException {
		final int BUFFER = 8192;
		BufferedInputStream inBuff = null;
		BufferedOutputStream outBuff = null;
		try {
			inBuff = new BufferedInputStream(new FileInputStream(sourceFile));
			outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));
			byte[] buffer = new byte[BUFFER];
			int length;
			while ((length = inBuff.read(buffer)) != -1) {
				outBuff.write(buffer, 0, length);
			}
			outBuff.flush();
		} finally {
			if (inBuff != null) {
				inBuff.close();
			}
			if (outBuff != null) {
				outBuff.close();
			}
		}
	}

	/**
	 * @param   path
	 * @return
	 * @throws  Exception
	 * @notice    获取文件夹的大小，传入path路径
	 */
	public static float getFileSize(String path) {
		File file = new File(path);
		float size = ((float) file.length()) / 1048576;
		return size;
	}

	/**
	 * @param   file
	 * @return   多少M
	 * @throws  Exception
	 * @notice   获取文件夹的大小，传入file对象
	 */
	public static long getFolderSize(File file) throws Exception {
		long size = 0;
		File[] fileList = file.listFiles();
		for (File element : fileList) {
			if (element.isDirectory()) {
				size = size + getFolderSize(element);
			} else {
				size = size + element.length();
			}
		}
		return size / 1048576;
	}

	/**
	 * @param string
	 * @return
	 * @notice 获取文件类型
	 */
	public static String getFileType(String string) {
		int index = string.lastIndexOf(".");
		String type = string.substring(index, string.length());
		return type;
	}

}
