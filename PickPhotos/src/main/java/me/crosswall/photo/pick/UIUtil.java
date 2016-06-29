package me.crosswall.photo.pick;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.view.Window;
import android.view.WindowManager;


public class UIUtil {
    public static void setTranslucentStatusColor(Activity activity, @ColorRes int color) {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.KITKAT)
            return;
        setTranslucentStatus(activity, true);
        SystemBarTint tintManager = new SystemBarTint(activity);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setStatusBarTintResource(color);
        tintManager.setNavigationBarTintColor(color);
        tintManager.setTintColor(color);
    }

    /**
     * 设置窗口高度
     *
     * @param activity
     * @param on
     */
    private static void setTranslucentStatus(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}
