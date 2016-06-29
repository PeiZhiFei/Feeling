package library.other;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import library.widget.BadgeView;

public class OtherUtil {

    public static boolean isSuccess(JSONObject result) {
        try {
            return 200 == result.getInt("result");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }


    protected static final String PREFS_FILE = "get_device_id.xml";
    protected static final String PREFS_DEVICE_ID = "get_device_id";
    protected static String uuid;

    public static String getUDID(Context context) {
        if (uuid == null) {
            final SharedPreferences prefs = context.getSharedPreferences(
                    PREFS_FILE, 0);
            final String id = prefs.getString(PREFS_DEVICE_ID, null);
            if (id != null) {
                uuid = id;
            } else {
                final String androidId = Settings.Secure.getString(context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                try {
                    if (!"9774d56d682e549c".equals(androidId)) {
                        uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8")).toString();
                    } else {
                        final String deviceId = ((TelephonyManager) context.getSystemService(
                                Context.TELEPHONY_SERVICE)).getDeviceId();
                        uuid = deviceId != null ?
                                UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")).toString() :
                                UUID.randomUUID().toString();
                    }
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
                prefs.edit().putString(PREFS_DEVICE_ID, uuid).apply();
            }
        }
        return uuid;
    }


    public static void badgeView(final Context context, final View view, final int number) {
        ((Activity) context).runOnUiThread(new Runnable() {
            public void run() {
                BadgeView badgeView = new BadgeView(context, view);
                if (number != 0) {
                    badgeView.setText(String.valueOf(number));
                    badgeView.show(true);
                }
                if (number == 0) {
                    badgeView.hide(true);
                }
            }
        });
    }

    /**
     * 把短信插入数据库，显示在信息里面
     */
    public static void createSMS(Context context, float time, String address, String body) {
        ContentValues values = new ContentValues();
        values.put("address", address);
        values.put("type", "1");
        values.put("read", "0");
        values.put("body", body);
        values.put("date", String.valueOf(time));
        context.getContentResolver().insert(Uri.parse("content://sms/inbox"), values);
    }

    /**
     * 分享的dialog
     */
    public static void dialogShare(Context context, String string) {
        AndroidShare as = new AndroidShare(context, string, null);
        as.show();
    }

    /**
     * 图片路径
     * 分享的dialog
     */
    public static void dialogShare(Context context, String string, String imagePath) {
        AndroidShare as = new AndroidShare(context, string, imagePath);
        as.show();
    }
}
