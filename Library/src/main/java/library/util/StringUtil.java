package library.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//第一部分是正则表达式
//第二部分是中文字符的处理
public class StringUtil {


    /**
     * @param string
     * @return boolean
     * @notice 手机号码的验证
     */
    public static boolean checkPhone(String string) {
        return match("^((1[0-9][0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$", string);
    }

    /**
     * @param string
     * @return boolean
     * @notice 邮箱格式的验证
     */
    public static boolean checkEmail(String string) {
        return match(
                "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$",
                string);
    }

    /**
     * @param string
     * @return boolean
     * @notice 快递单号格式的验证
     */
    public static boolean checkPackage(String string) {
        return match("[\\da-zA-Z]+?", string);
    }

    /**
     * @param string
     * @return boolean
     * @notice 验证字符串是否为网址
     */
    public static boolean checkHttp(String string) {
        return match(
                "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]",
                string);
    }

    private static boolean match(String rule, String string) {
        Pattern pattern = Pattern.compile(rule);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }

    /**
     * @param idCard 居民身份证号码15位或18位，最后一位可能是数字或字母
     * @return 验证身份证号码
     */
    public static boolean checkIdCard(String idCard) {
        String regex = "[1-9]\\d{13,16}[a-zA-Z0-9]{1}";
        return Pattern.matches(regex, idCard);
    }

    /**
     * @param mobile：手机号
     * @return 验证手机号码
     */
    public static boolean checkMobile2(String mobile) {
        String regex = "(\\+\\d+)?1[3458]\\d{9}$";
        return Pattern.matches(regex, mobile);
    }

    /**
     * @param phone：格式：+8602085588447
     * @return 验证固定电话号码
     */
    public static boolean checkPhone2(String phone) {
        String regex = "(\\+\\d+)?(\\d{3,4}\\-?)?\\d{7,8}$";
        return Pattern.matches(regex, phone);
    }

    /**
     * @param blankSpace 空白字符，包括：空格、\t、\n、\r、\f、\x0B
     * @return 验证空白字符
     */
    public static boolean checkBlankSpace(String blankSpace) {
        String regex = "\\s+";
        return Pattern.matches(regex, blankSpace);
    }

    /**
     * @param chinese：中文字符
     * @return 验证中文
     */
    public static boolean checkChinese(String chinese) {
        String regex = "^[\u4E00-\u9FA5]+$";
        return Pattern.matches(regex, chinese);
    }

    /**
     * @param birthday：格式：1992-09-03，或1992.09.03
     * @return 验证日期（年月日）
     */
    public static boolean checkBirthday(String birthday) {
        String regex = "[1-9]{4}([-./])\\d{1,2}\\1\\d{1,2}";
        return Pattern.matches(regex, birthday);
    }

    /**
     * @param postcode 邮政编码
     * @return 验证邮政编码
     */
    public static boolean checkPostcode(String postcode) {
        String regex = "[1-9]\\d{5}";
        return Pattern.matches(regex, postcode);
    }

    /**
     * @param ipAddress IPv4标准地址
     * @return 验证IP地址
     */
    public static boolean checkIpAddress(String ipAddress) {
        String regex = "[1-9](\\d{1,2})?\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))";
        return Pattern.matches(regex, ipAddress);
    }

    public static boolean isEmpty(String s) {
        return TextUtils.isEmpty(s) || s.equals("") || s.length() == 0;
    }

    public static boolean isEmptyTrim(String s) {
        return TextUtils.isEmpty(s) || s.trim().equals("");
    }

    //空格也算
    public static boolean isEmptyEdit(EditText s) {
        return TextUtils.isEmpty(s.getText().toString()) || s.getText().toString().equals(" ");
    }

    public static String getStr(Context context, int str) {
        return context != null ? context.getString(str) : "111";
    }


    public static String formatMysqlTimestamp(String timestamp, String pattern) {
        Date date = new Date(Long.valueOf(timestamp) * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern,
                Locale.getDefault());
        return sdf.format(date);
    }


    /**
     * @param obj
     * @return
     * @notice 对象转为String
     */
    public static String string(Object obj) {
        String str = "";
        if (obj != null) {
            str = String.valueOf(obj);
        }
        return str;
    }

    /**
     * @param text
     * @param defaultText
     * @return
     * @notice 获取字符串中需要的内容，此处为括号
     */
    public static String getCountryCodeBracketsInfo(String text,
                                                    String defaultText) {
        if (text.contains("(") && text.contains(")")) {
            int leftBrackets = text.indexOf("(");
            int rightBrackets = text.lastIndexOf(")");
            if (leftBrackets < rightBrackets) {
                return "+" + text.substring(leftBrackets + 1, rightBrackets);
            }
        }
        if (defaultText != null) {
            return defaultText;
        } else {
            return text;
        }
    }

    /**
     * 判断map是否为空
     * is null or its size is 0
     * isEmpty(null)   =   true;
     * isEmpty({})     =   true;
     * isEmpty({1, 2})    =   false;
     *
     * @return if map is null or its size is 0, return true, else return false.
     */
    public static <K, V> boolean isEmpty(Map<K, V> sourceMap) {
        return (sourceMap == null || sourceMap.size() == 0);
    }

    /**
     * @param map
     * @return map转json
     */
    public static String toJson(Map<String, String> map) {
        if (map == null || map.size() == 0) {
            return null;
        }
        StringBuilder paras = new StringBuilder();
        paras.append("{");
        Iterator<Map.Entry<String, String>> ite = map.entrySet().iterator();
        while (ite.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) ite.next();
            paras.append("\"").append(entry.getKey()).append("\":\"").append(entry.getValue()).append("\"");
            if (ite.hasNext()) {
                paras.append(",");
            }
        }
        paras.append("}");
        return paras.toString();
    }


}
