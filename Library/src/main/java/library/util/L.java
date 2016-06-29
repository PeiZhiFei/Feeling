package library.util;

import android.util.Log;

import java.util.Date;
import java.util.List;

/**
 * @author 谁谁谁
 * @date 2014-7-28
 * @date 下午9:13:21
 * @file LogUtil.java
 * @content 增强型的Log，可以输出各种类型，不用考空指针,默认tag为log，都是以error输出的
 * i是没用的信息，d可以控制在配置文件里面
 */
public class L {

    private final static String TAG = "log";

    public static void setDEBUG(boolean DEBUG) {
        L.DEBUG = DEBUG;
    }

    private static boolean DEBUG = true;

    public static void l(int msg) {
        if (DEBUG) {
            Log.e(TAG, "" + msg);
        }
    }

    public static void l(double msg) {
        if (DEBUG) {
            Log.e(TAG, "" + msg);
        }
    }

    public static void l(long msg) {
        if (DEBUG) {
            Log.e(TAG, "" + msg);
        }
    }

    public static void l(byte[] msg) {
        if (DEBUG) {
            for (byte element : msg) {
                Log.e(TAG, "" + element);
            }
        }
    }

    public static void l(boolean msg) {
        if (DEBUG) {
            Log.e(TAG, "" + msg);
        }

    }

    public static void l(String msg) {
        if (DEBUG) {
            Log.e(TAG, "" + msg);
        }
    }

    public static void l(Date date) {
        if (DEBUG) {
            Log.e(TAG, "" + date.toString());
        }
    }

    public static void l(String[] msg) {
        if (DEBUG) {
            String result = "";
            for (String string : msg) {
                result += string + "\n";
            }
            Log.e(TAG, "" + result);
        }
    }

    public static void l(List<String> msg) {
        if (DEBUG) {
            String result = "";
            for (String string : msg) {
                result += string + "\n";
            }
            Log.e(TAG, "" + result);
        }
    }

    public static void l(String tag, String msg) {
        if (DEBUG) {
            Log.e(tag, "" + msg);
        }
    }

}
