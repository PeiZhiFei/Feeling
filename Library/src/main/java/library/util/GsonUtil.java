package library.util;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GsonUtil {
    //json转list  new TypeToken<ArrayList<Bean>>{}
    public <T> List<T> jsonToList(String jsonString, TypeToken<T> tt) {
        List<T> list = gson.fromJson(jsonString, tt.getType());
        return list;
    }

    //json转bean
    public static <T extends Object> T jsonToBean(String gsonString, Class cls) {
        Object t = gson.fromJson(gsonString, cls);
        return (T) t;
    }

    public static List<Map<String, Object>> jsonToMap(String jsonString) {
        List<Map<String, Object>> list = gson.fromJson(jsonString, new TypeToken<List<Map<String, Object>>>() {
        }.getType());
        return list;
    }

    private static final Gson gson = new Gson();


    public static <T> T fromJson(String jsonString, Class<T> className) {
        return gson.fromJson(jsonString, className);
    }

    //对象转json
    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    // 作为一个辅助型的方法，一般情况可以不用
    public static String toJson(Object object, Type type) {
        return gson.toJson(object, type);
    }

    public static Object fromJson(String jsonString, Type type) {
        return gson.fromJson(jsonString, type);
    }

    public static <T> List<T> fromLinedTreeMap(List<LinkedTreeMap> list, Class<T> className) {
        List<T> newList = new ArrayList<>();
        for (LinkedTreeMap tmp : list) {
            newList.add(fromJson(toJson(tmp), className));
        }
        return newList;
    }

    public static <T> List<T> fromJsonList(String jsonString, Class<T> className) {
        List<LinkedTreeMap> list = gson.fromJson(jsonString, new TypeToken<List<LinkedTreeMap>>() {
        }.getType());
        return fromLinedTreeMap(list, className);
    }


 /*
    //fastjson
    public static String createJsonString(Object object) {
        String jsonString = JSON.toJSONString(object);
        return jsonString;
    }

    public static <T extends Object> T createJsonBean(String jsonString, Class cls) {
        Object t = JSON.parseObject(jsonString, cls);
        return (T) t;
    }

    public static List createJsonToListBean(String jsonString,
                                            Class cls) {
        List list = null;
        list = JSON.parseArray(jsonString, cls);
        return list;
    }
    */
}
