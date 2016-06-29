package library.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

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
import java.util.Locale;

//我的
public class FileUtil {

    public static String getSizeName(long kb) {
        if (kb >= 1024) {
            return Tools.scale1(kb / 1024f) + " MB";
        } else if (kb >= 0 && kb < 1024) {
            return kb + " KB";
        } else {
            return "0 KB";
        }
    }

/*    public static String getSizeName2(long kb) {
        if (kb >= 100) {
            return Tools.scale(kb / 1024f) + " MB";
        } else if (kb >= 0 && kb < 100) {
            return kb + " KB";
        } else {
            return "0 KB";
        }
    }*/

    /**
     * @param context
     * @param fileName
     * @param text
     * @notice 写文件
     */
    public static void writeFile(Context context, String fileName, String text) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = context.openFileOutput(fileName,
                    Context.MODE_APPEND);
            fileOutputStream.write(text.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (IOException e) {
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
                        if ((attrNameString != null)
                                && attrNameString.equals("name")) {
                            nameString = attrNameValue;
                        } else if ((attrNameString != null)
                                && attrNameString.equals("age")) {
                            ageString = attrNameValue;

                        } else if ((attrNameString != null)
                                && attrNameString.equals("widht")) {
                            widthString = attrNameValue;

                        }
                    }

                    if ((nameString != null) && (ageString != null)
                            && (widthString != null)) {
                        tString += "姓名：" + nameString + "，年龄：" + ageString
                                + "，身高：" + widthString + "\n";
                    }

                }

            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tString;
    }

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
     * @param filepath ：文件路径全名
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
     * @param filepath ：文件夹路径名
     * @notice 判断文件夹是否存在，不存在则创建路径
     */
    public static void isDirExistAndCreat(String filepath) {
        if (isSdcardAvailable()) {
            File file = new File(filepath);
            if (!file.exists()) {
                file.mkdirs();
            }
        } else {
            L.l("没有存储卡");
        }
    }

    //

    /**
     * @param file ：File
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
     * @param path     ：路径
     * @param filename ：文件名
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
     * @param path     ：路径
     * @param filename ：文件名，不包含后缀如.jpg
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
     * @param path      ：文件路径
     * @param filename  ：文件名
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
     * 获取SD卡的剩余容量 单位byte
     *
     * @return
     */
    public static long getSDCardAllSize() {
        if (isSdcardAvailable()) {
            StatFs stat = new StatFs(getRootPath());
            // 获取空闲的数据块的数量
            long availableBlocks = (long) stat.getAvailableBlocks() - 4;
            // 获取单个数据块的大小（byte）
            long freeBlocks = stat.getAvailableBlocks();
            return freeBlocks * availableBlocks;
        }
        return 0;
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
        if (!targetFile.exists()) {
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
    }


    //单位未知
    public static long getFileSize(String path) {
        if (StringUtil.isEmpty(path)) {
            return -1;
        }

        File file = new File(path);
        return (file.exists() && file.isFile() ? file.length() : -1);
    }

    /**
     * @param file
     * @return 多少M
     * @throws Exception
     * @notice 获取文件夹的大小，传入file对象
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

    /**
     * get suffix of file from path
     * <p>
     * <pre>
     *      getFileExtension(null)               =   ""
     *      getFileExtension("")                 =   ""
     *      getFileExtension("   ")              =   "   "
     *      getFileExtension("a.mp3")            =   "mp3"
     *      getFileExtension("a.b.rmvb")         =   "rmvb"
     *      getFileExtension("abc")              =   ""
     *      getFileExtension("c:\\")              =   ""
     *      getFileExtension("c:\\a")             =   ""
     *      getFileExtension("c:\\a.b")           =   "b"
     *      getFileExtension("c:a.txt\\a")        =   ""
     *      getFileExtension("/home/admin")      =   ""
     *      getFileExtension("/home/admin/a.txt/b")  =   ""
     *      getFileExtension("/home/admin/a.txt/b.mp3")  =   "mp3"
     * </pre>
     *
     * @param filePath
     * @return
     */
    public static String getFileExtension(String filePath) {
        int extenPosi = filePath.lastIndexOf(".");
        int filePosi = filePath.lastIndexOf(File.separator);
        if (extenPosi == -1) {
            return "";
        }
        return (filePosi >= extenPosi) ? "" : filePath.substring(extenPosi + 1);
    }

    /**
     * get file name from path, include suffix
     * <p>
     * <pre>
     *      getFileName(null)               =   null
     *      getFileName("")                 =   ""
     *      getFileName("   ")              =   "   "
     *      getFileName("a.mp3")            =   "a.mp3"
     *      getFileName("a.b.rmvb")         =   "a.b.rmvb"
     *      getFileName("abc")              =   "abc"
     *      getFileName("c:\\")              =   ""
     *      getFileName("c:\\a")             =   "a"
     *      getFileName("c:\\a.b")           =   "a.b"
     *      getFileName("c:a.txt\\a")        =   "a"
     *      getFileName("/home/admin")      =   "admin"
     *      getFileName("/home/admin/a.txt/b.mp3")  =   "b.mp3"
     * </pre>
     *
     * @param filePath
     * @return 全部的文件名
     */
    public static String getFileName(String filePath) {
        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? filePath : filePath.substring(filePosi + 1);
    }

    //获取文件名不包含后缀
    public static String getFileName2(String filePath) {
        int filePosi = filePath.lastIndexOf(File.separator);
        int filePosi2 = filePath.lastIndexOf(".");
//        L.l((filePosi == -1 || filePosi2 == -1) ? filePath : filePath.substring(filePosi + 1, filePosi2));
//        L.l((filePosi == -1 || filePosi2 == -1) ? filePath : filePath.substring(filePosi + 1, filePosi2 + 1));
//        L.l((filePosi == -1 || filePosi2 == -1) ? filePath : filePath.substring(filePosi + 1, filePosi2 + 2));
        return (filePosi == -1 || filePosi2 == -1) ? filePath : filePath.substring(filePosi + 1, filePosi2);
    }


    private static String getSuffix(File file) {
        if (file == null || !file.exists() || file.isDirectory()) {
            return null;
        }
        String fileName = file.getName();
        if (fileName.equals("") || fileName.endsWith(".")) {
            return null;
        }
        int index = fileName.lastIndexOf(".");
        if (index != -1) {
            return fileName.substring(index + 1).toLowerCase(Locale.US);
        } else {
            return null;
        }
    }

    public static String getMimeType(File file) {
        String suffix = getSuffix(file);
        if (suffix == null) {
            return "file/*";
        }
        String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix);
        if (!TextUtils.isEmpty(type)) {
            return type;
        }
        return "file/*";
    }
}
