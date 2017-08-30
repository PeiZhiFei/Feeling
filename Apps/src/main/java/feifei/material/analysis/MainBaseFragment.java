package feifei.material.analysis;

import android.app.Activity;
import android.app.Fragment;

import java.util.Arrays;


public class MainBaseFragment extends Fragment

{
    protected String[] orderTime7 = new String[7];
    protected String[] orderTime30 = new String[30];
    protected float[] orderCount7 = new float[7];
    protected float[] orderMoney7 = new float[7];
    protected float[] orderCount30 = new float[30];
    protected float[] orderMoney30 = new float[30];

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof AnalysisActivity) {
            orderTime7 = ((AnalysisActivity) activity).orderTime7;
            orderTime30 = ((AnalysisActivity) activity).orderTime30;
            orderCount7 = ((AnalysisActivity) activity).orderCount7;
            orderMoney7 = ((AnalysisActivity) activity).orderMoney7;
            orderCount30 = ((AnalysisActivity) activity).orderCount30;
            orderMoney30 = ((AnalysisActivity) activity).orderMoney30;
        } else if (activity instanceof SearchActivity) {
            orderTime7 = ((SearchActivity) activity).orderTime7;
            orderTime30 = ((SearchActivity) activity).orderTime30;
            orderCount7 = ((SearchActivity) activity).orderCount7;
            orderMoney7 = ((SearchActivity) activity).orderMoney7;
            orderCount30 = ((SearchActivity) activity).orderCount30;
            orderMoney30 = ((SearchActivity) activity).orderMoney30;
        } else if (activity instanceof StoreInfoActivity) {
            orderTime7 = ((StoreInfoActivity) activity).orderTime7;
            orderTime30 = ((StoreInfoActivity) activity).orderTime30;
            orderCount7 = ((StoreInfoActivity) activity).orderCount7;
            orderMoney7 = ((StoreInfoActivity) activity).orderMoney7;
            orderCount30 = ((StoreInfoActivity) activity).orderCount30;
            orderMoney30 = ((StoreInfoActivity) activity).orderMoney30;
        } else {
            for (int i = 0; i < orderCount7.length; i++) {
                orderCount7[i] = 5 + i * i * i;
                orderMoney7[i] = 1000 + i * i * i + 3;
                orderTime7[i] = "090" + i;
            }
            for (int i = 0; i < orderCount30.length; i++) {
                orderCount30[i] = 5 + i * i * i;
                orderMoney30[i] = 1000 + i * i * i + 3;
                orderTime30[i] = "090" + i;
            }
        }

    }


    public static int getMax(int[] arr) {
        int max = arr[0];
        for (int x = 1; x < arr.length; x++) {
            if (arr[x] > max)
                max = arr[x];
        }
        return max;
    }

    public static float getMax(float[] arr) {
        float max = arr[0];
        for (int x = 1; x < arr.length; x++) {
            if (arr[x] > max)
                max = arr[x];
        }
        return max;
    }

    public int[] sortN(int[] a) {
        int[] b = new int[a.length];
        Arrays.sort(a);
        for (int i = 0; i < a.length; i++) {
            b[i] = a[a.length - i - 1];
        }
        return b;
    }

//    public float[] sortN(float[] a)
//    {
//        float[] b = new float[a.length];
//        Arrays.sort(a);
//        for (int i = 0; i < a.length; i++)
//        {
//            b[i] = a[a.length - i - 1];
//        }
//        return b;
//    }
//
//    public String[] sortN(String[] a)
//    {
//        String[] b = new String[a.length];
//        Arrays.sort(a);
//        for (int i = 0; i < a.length; i++)
//        {
//            b[i] = a[a.length - i - 1];
//        }
//        return b;
//    }

    public float[] sortN(float[] a) {
        if (a != null) {
            float[] b = new float[a.length];
            for (int i = 0; i < a.length; i++) {
                b[i] = a[a.length - i - 1];
            }
            return b;
        }
        return null;
    }

    public String[] sortN(String[] a) {
        if (a != null) {
            String[] b = new String[a.length];
            for (int i = 0; i < a.length; i++) {
                b[i] = a[a.length - i - 1];
            }
            return b;
        }
        return null;
    }


}
