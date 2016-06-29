package library.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * 都是在同一个文件里
 * write有返回值，apply没有
 */

public class S {

    //默认的存储文件，不是键，这里的键要加上版本号和包名
    public static String FILE_NAME = "PREFERENCE";
    private static String LOGIN_FILE_NAME = "LOGIN";
    private static String FIRST_FILE_NAME = "APPFIRST";

    public static void s(Context context, String key, String value) {
        SharedPreferences share = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        Editor editor = share.edit();
        editor.putString(key, value).apply();
    }

    public static void s(Context context, String key, boolean value) {
        SharedPreferences share = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        Editor editor = share.edit();
        editor.putBoolean(key, value).apply();
    }

    public static void s(Context context, String key, int value) {
        SharedPreferences share = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        Editor editor = share.edit();
        editor.putInt(key, value).apply();
    }

    public static void s(Context context, String key, long value) {
        SharedPreferences share = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        Editor editor = share.edit();
        editor.putLong(key, value).apply();
    }

    public static void s(Context context, String key, float value) {
        SharedPreferences share = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        Editor editor = share.edit();
        editor.putFloat(key, value).apply();
    }

    public static void s(Context context, String key, Set<String> set) {
        SharedPreferences share = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        Editor editor = share.edit();
        editor.putStringSet(key, set).apply();
    }

    public static int readInt(Context context, String key, int defaultValue) {
        SharedPreferences share = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return share.getInt(key, defaultValue);
    }

    public static String readString(Context context, String key,
                                    String defaultValue) {
        SharedPreferences share = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return share.getString(key, defaultValue);
    }

    public static boolean readBoolear(Context context, String key,
                                      boolean defaultValue) {
        SharedPreferences share = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return share.getBoolean(key, !defaultValue);
    }

    public static boolean readBoolean(Context context, String key,
                                      boolean defaultValue) {
        SharedPreferences share = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return share.getBoolean(key, defaultValue);
    }

    public static float readFloat(Context context, String key,
                                  float defaultValue) {
        SharedPreferences share = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return share.getFloat(key, defaultValue);
    }

    public static long readLong(Context context, String key, long defaultValue) {
        SharedPreferences share = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return share.getLong(key, defaultValue);
    }

    public static Set<String> readSet(Context context, String key,
                                      Set<String> defaultValue) {
        SharedPreferences share = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return share.getStringSet(key, defaultValue);
//        HashSet<String> set2 = null;
//        Iterator<String> it = set2.iterator();
//        while (it.hasNext()) {
//            LogUtil.log(it.next());
//        }
    }

    /**
     * 清除所有数据
     */
    public static void clearSP(Context context) {
        try {
            SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                    Context.MODE_PRIVATE);
            sp.edit().clear().apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 提交事务可以用这个兼容包
        // SharedPreferencesCompat.s(sp.edit());
    }

    /**
     * 清除指定文件的数据
     */
    public static void clearSP(Context context, String fileName) {
        try {
            SharedPreferences sp = context.getSharedPreferences(fileName,
                    Context.MODE_PRIVATE);
            sp.edit().clear().apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 提交事务可以用这个兼容包
        // SharedPreferencesCompat.s(sp.edit());
    }


    /**
     * 移除某个key值已经对应的值,指定文件
     */
    public static void removeSP(Context context, String fileName, String key) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key).apply();
        // 提交事务可以用这个兼容包
        // SharedPreferencesCompat.s(sp.edit());
    }

    /**
     * 移除某个key值已经对应的值
     */
    public static void removeSP(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key).apply();
        // 提交事务可以用这个兼容包
        // SharedPreferencesCompat.s(sp.edit());
    }
    /**
     * 查询某个key是否已经存在
     */
    public static boolean isContains(Context context, String fileName, String key) {
        SharedPreferences sp = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     * 这里暂时没有Set<String>
     */
    public static void putAll(Context context, String key, Object object) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     */
    public static Object get(Context context, String key, Object defaultObject) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }
        return null;
    }


    /**
     * 返回所有的键值对
     */
    public static Map<String, ?> getAllMap(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getAll();
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
            editor.commit();
        }
    }


    //-----------------------------------------以下是需要传文件名的-----------------------------------------

    public static void s(Context context, String filename, String key, String value) {
        SharedPreferences share = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        Editor editor = share.edit();
        editor.putString(key, value).apply();
    }

    public static void s(Context context, String filename, String key, boolean value) {
        SharedPreferences share = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        Editor editor = share.edit();
        editor.putBoolean(key, value).apply();
    }

    public static void s(Context context, String filename, String key, int value) {
        SharedPreferences share = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        Editor editor = share.edit();
        editor.putInt(key, value).apply();
    }

    public static void s(Context context, String filename, String key, long value) {
        SharedPreferences share = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        Editor editor = share.edit();
        editor.putLong(key, value).apply();
    }

    public static void s(Context context, String filename, String key, float value) {
        SharedPreferences share = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        Editor editor = share.edit();
        editor.putFloat(key, value).apply();
    }

    public static void s(Context context, String filename, String key, Set<String> set) {
        SharedPreferences share = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        Editor editor = share.edit();
        editor.putStringSet(key, set).apply();
    }

    public static int readInt(Context context, String filename, String key, int defaultValue) {
        SharedPreferences share = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        try {
            return share.getInt(key, defaultValue);
        } catch (ClassCastException e) {
            return defaultValue;
        }
    }

    public static String readString(Context context, String filename, String key, String defaultValue) {
        SharedPreferences share = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        String result;
        try {
            result = share.getString(key, defaultValue);
        } catch (ClassCastException e) {
            return defaultValue;
        }
        return result;
    }

    public static boolean readBoolean(Context context, String filename, String key, boolean defaultValue) {
        SharedPreferences share = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        return share.getBoolean(key, defaultValue);
    }

    public static float readFloat(Context context, String filename, String key, float defaultValue) {
        SharedPreferences share = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        return share.getFloat(key, defaultValue);
    }

    public static long readLong(Context context, String filename, String key, long defaultValue) {
        SharedPreferences share = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        return share.getLong(key, defaultValue);
    }

    public static Set<String> readSet(Context context, String filename, String key, Set<String> defaultValue) {
        SharedPreferences share = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        return share.getStringSet(key, defaultValue);
    }

    public static void writeMap(Context context, Map map) {
        SharedPreferences fac = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        // 对数据进行编辑
        Editor editor = fac.edit();
        for (int i = 0; i < map.size(); i++) {
//            editor.putString(map.get(""));
        }
        editor.apply();
    }


    //—————————————————————————————————需要返回值的，可忽略—————————————————————————————————————
    public static boolean write(Context context, String key, String value) {
        SharedPreferences share = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        Editor editor = share.edit();
        return editor.putString(key, value).commit();
    }

    public static boolean write(Context context, String key, boolean value) {
        SharedPreferences share = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        Editor editor = share.edit();
        return editor.putBoolean(key, value).commit();
    }

    public static boolean write(Context context, String key, int value) {
        SharedPreferences share = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        Editor editor = share.edit();
        return editor.putInt(key, value).commit();
    }

    public static boolean write(Context context, String key, long value) {
        SharedPreferences share = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        Editor editor = share.edit();
        return editor.putLong(key, value).commit();
    }

    public static boolean write(Context context, String key, float value) {
        SharedPreferences share = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        Editor editor = share.edit();
        return editor.putFloat(key, value).commit();
    }

    public static boolean write(Context context, String key, Set<String> set) {
        SharedPreferences share = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        Editor editor = share.edit();
        return editor.putStringSet(key, set).commit();
    }

}