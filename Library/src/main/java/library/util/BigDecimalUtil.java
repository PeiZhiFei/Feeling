package library.util;

import java.math.BigDecimal;

public class BigDecimalUtil {
    private void test1() {
        double a = 1.0;
        double b = 0.8;
        L.l(a - b);
        //String正常一些
        BigDecimal bd1 = new BigDecimal(Double.toString(a));
        BigDecimal bd2 = new BigDecimal(Double.toString(b));
        L.l(bd1.subtract(bd2).doubleValue());
        L.l(sub(a, b));
    }

    private void test() {
        float a = 1.0f;
        float b = 0.8f;
        L.l(a - b);
        //String正常一些
        BigDecimal bd1 = new BigDecimal(Float.toString(a));
        BigDecimal bd2 = new BigDecimal(Float.toString(b));
        L.l(bd1.subtract(bd2).floatValue());
    }

    public static double add(double d1, double d2) {
        BigDecimal b1 = new BigDecimal(d1);
        BigDecimal b2 = new BigDecimal(d2);
        return b1.add(b2).doubleValue();
    }

    public static double sub(double d1, double d2) {
        BigDecimal b1 = new BigDecimal(d1);
        BigDecimal b2 = new BigDecimal(d2);
        return b1.subtract(b2).doubleValue();
    }

    public static double mul(double d1, double d2) {
        BigDecimal b1 = new BigDecimal(d1);
        BigDecimal b2 = new BigDecimal(d2);
        return b1.multiply(b2).doubleValue();
    }

    public static double div(double d1,
                             double d2, int len) {
        BigDecimal b1 = new BigDecimal(d1);
        BigDecimal b2 = new BigDecimal(d2);
        return b1.divide(b2, len, BigDecimal.
                ROUND_HALF_UP).doubleValue();
    }

    // 进行四舍五入
    public static double round(double d, int len) {
        BigDecimal b1 = new BigDecimal(d);
        BigDecimal b2 = new BigDecimal(1);
        // 任何一个数字除以1都是原数字
        // ROUND_HALF_UP是BigDecimal的一个常量,表示进行四舍五入的操作
        return b1.divide(b2, len, BigDecimal.
                ROUND_HALF_UP).doubleValue();
    }

}
