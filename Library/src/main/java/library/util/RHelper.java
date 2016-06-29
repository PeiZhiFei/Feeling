package library.util;

import android.content.Context;
import android.content.res.Resources;

public class RHelper {
    private static Resources resources;

    public static void init(Context application) {
        resources = application.getResources();
    }

    public static String getString(int resId) {
        return resources.getString(resId);
    }
}
