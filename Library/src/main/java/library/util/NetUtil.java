package library.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author 谁谁谁
 * @date 2014-7-28
 * @date 下午9:13:46
 * @file NetUtil.java
 * @content 安卓网络相关判断和激活
 */
public class NetUtil {

    private static final int NETWORN_NONE = 0;
    private static final int NETWORN_WIFI = 1;
    private static final int NETWORN_MOBILE = 2;

    /**
     * @param context
     * @return 0：没有网络，1：WIFI，2：数据；
     * @notice 获取网络状态
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

    public static boolean isNetConnected(Context context) {
        boolean result = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager == null ? null
                : connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
            result = true;
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

    /**
     * 自动打开WIFI
     */
    public static void wifiOpen(Context context) {
        WifiManager wifimanager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        wifimanager.setWifiEnabled(true);
    }

    /**
     * 自动关闭WIFI
     */
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


    private static final int TIMEOUT_IN_MILLIONS = 5000;

    public interface CallBack {
        void onRequestComplete(String result);
    }


    /**
     * 异步的Get请求
     *
     * @param urlStr
     * @param callBack
     */
    public static void doGetAsyn(final String urlStr, final CallBack callBack) {
        new Thread() {
            public void run() {
                try {
                    String result = doGet(urlStr);
                    if (callBack != null) {
                        callBack.onRequestComplete(result);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            ;
        }.start();
    }

    /**
     * 异步的Post请求
     *
     * @param urlStr
     * @param params
     * @param callBack
     * @throws Exception
     */
    public static void doPostAsyn(final String urlStr, final String params,
                                  final CallBack callBack) throws Exception {
        new Thread() {
            public void run() {
                try {
                    String result = doPost(urlStr, params);
                    if (callBack != null) {
                        callBack.onRequestComplete(result);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            ;
        }.start();

    }

    /**
     * Get请求，获得返回数据
     *
     * @param urlStr
     * @return
     * @throws Exception
     */
    public static String doGet(String urlStr) {
        URL url = null;
        HttpURLConnection conn = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIMEOUT_IN_MILLIONS);
            conn.setConnectTimeout(TIMEOUT_IN_MILLIONS);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                baos = new ByteArrayOutputStream();
                int len = -1;
                byte[] buf = new byte[128];

                while ((len = is.read(buf)) != -1) {
                    baos.write(buf, 0, len);
                }
                baos.flush();
                return baos.toString();
            } else {
                throw new RuntimeException(" responseCode is not 200 ... ");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
            }
            try {
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
            }
            conn.disconnect();
        }

        return null;

    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     * @throws Exception
     */
    public static String doPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection conn = (HttpURLConnection) realUrl
                    .openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "utf-8");
            conn.setUseCaches(false);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setReadTimeout(TIMEOUT_IN_MILLIONS);
            conn.setConnectTimeout(TIMEOUT_IN_MILLIONS);

            if (param != null && !param.trim().equals("")) {
                // 获取URLConnection对象对应的输出流
                out = new PrintWriter(conn.getOutputStream());
                // 发送请求参数
                out.print(param);
                // flush输出流的缓冲
                out.flush();
            }
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

}
