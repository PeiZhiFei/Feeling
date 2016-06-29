package library.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import library.widget.snackbar.Snackbar;
import my.library.R;

/**
 * 自定义布局的toast，纯代码写布局，线程里面显示需要自己加runOnUiThread()，高级功能请用ToastCustom
 * toast的时间只有2.5和3秒两种，所以时间没必要作为一个选项去定义，想改系统toast时间需要反射
 * http://blog.csdn.net/jiangwei0910410003/article/details/25540037
 * toast是使用了队列的，使用WindowManager是可以独立于Activity显示的
 * 自己写的toast可以自定义动画：http://blog.csdn.net/zhangweiwtmdbf/article/details/30031015
 * todo 自定义的样式定义一个全局静态变量，每个项目的样式都是统一的
 */
public class TS {
    public static void t(final Context context, final int string) {
        if (context != null) {
            toast(context, context.getResources().getString(string));
        }
    }

    public static void t(final Context context, final CharSequence string) {
        if (context != null) {
            toast(context, string);
        }
    }

    public static void s(Context context, CharSequence message) {
        if (context != null) {
            snack(context, message, 1);
        }

    }

    public static void s(Context context, int message) {
        if (context != null) {
            snack(context, context.getResources().getString(message), 1);
        }
    }

    public static void s(Context context, CharSequence message, int lev) {
        if (context != null) {
            snack(context, message, lev);
        }
    }


    public static void s(Context context, int message, int lev) {
        if (context != null) {
            snack(context, context.getResources().getString(message), lev);
        }
    }

    //mActivity.getWindow().getDecorView().getRootView()是在状态栏上面
    //mActivity.getWindow().getDecorView().getRootView().findViewById(android.R.id.content)在状态栏下面
    private static void snack(Context mActivity, CharSequence message, int lev) {
        if (S.readBoolear(mActivity, "3", false) && (mActivity instanceof Activity) && ((Activity) mActivity).findViewById(android.R.id.content) != null) {
            View view = ((Activity) mActivity).findViewById(android.R.id.content);
            Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
            View snackbarView = snackbar.getView();
            switch (lev) {
                case 1:
                    snackbarView.setBackgroundColor(mActivity.getResources().getColor(R.color.glb_blue));
                    break;
                case 2:
                    snackbarView.setBackgroundColor(mActivity.getResources().getColor(R.color.glb_blue));
//                    snackbarView.setBackgroundColor(mActivity.getResources().getColor(R.color.glb_yellow));
                    break;
                case 3:
                    snackbarView.setBackgroundColor(mActivity.getResources().getColor(R.color.glb_red));
                    break;
            }

            TextView textView = (TextView) snackbarView.findViewById(R.id.snackbar_text);
            textView.setTextSize(16);
            textView.setGravity(Gravity.CENTER);
            snackbar.show();
        } else {
            t(mActivity, message);
        }

    }


    private static void toast(Context context, CharSequence string) {
        if (S.readBoolear(context, "3", false)) {

            Toast toast = new Toast(context);
            View view = LayoutInflater.from(context).inflate(R.layout.toast_layout, null);

            //这样设置的全屏
            View view2 = view.findViewById(R.id.layout);
            view2.getLayoutParams().width = Tools.getWidth(context);
            view2.getLayoutParams().height = Tools.dp2px(context,50);

            toast.setView(view);
            TextView textView = (TextView) toast.getView().findViewById(R.id.toast_text);
            ImageView imageView = (ImageView) toast.getView().findViewById(R.id.toast_image);
            textView.setText(TextUtils.isEmpty(string) ? "" : string);

            textView.setTextColor(Color.WHITE);

            imageView.setVisibility(View.GONE);
            toast.setDuration(Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.BOTTOM, 0, 200);
            toast.setGravity(Gravity.TOP, 0, 0);

//            toast.setGravity(Gravity.CENTER, 0, 200);
//            ToastLayout toastLayout = ToastLayout.toastlayout(context);
//            RelativeLayout layout = toastLayout.getLayout();
//            toast.setView(layout);
//            TextView textView = toastLayout.getTextView();
//            ImageView imageView = toastLayout.getImageView();

           /*修改这里显示image
              imageView.setVisibility(View.VISIBLE);
           switch (imageType) {
                case IMAGE_NORMAL:
                    imageView.setImageResource(R.drawable.icon_notice);
                    break;
                case IMAGE_SUCCESS:
                    imageView.setImageResource(R.drawable.icon_notice);
                    break;
                case IMAGE_ERROR:
                    imageView.setImageResource(R.drawable.icon_notice);
                    break;
                case IMAGE_NOIMAGE:
                    imageView.setVisibility(View.GONE);
                    break;
                default:
                    // 数字超出范围则不显示，容错性
                    imageView.setVisibility(View.GONE);
                    break;
            }*/

            // 关闭toast的方法，这是加强版
            if ((context instanceof Activity)) {
                if (!((Activity) context).isFinishing()) {
                    toast.show();
                }
            } else {
                toast.show();
            }
        } else {
            Toast toast = Toast.makeText(context, string, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.show();
        }
    }


    //这里没有公开是为了方便
    private final static int IMAGE_NOIMAGE = 0;
    private final static int IMAGE_NORMAL  = 1;
    private final static int IMAGE_SUCCESS = 2;
    private final static int IMAGE_ERROR   = 3;

    //标准snackbar的使用
    public static void tip(Activity activity, CharSequence string) {
        if (activity != null) {
            View view = activity.getWindow().getDecorView().getRootView();
//            activity.findViewById(R.id.activity_base_container)
            if (activity.getWindow().peekDecorView() != null) {
                InputMethodManager inputmanger = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            Snackbar sb = Snackbar.make(view, string, Snackbar.LENGTH_SHORT);
            sb.setText(string);
            Snackbar.SnackbarLayout sbView = (Snackbar.SnackbarLayout) sb.getView();
//            sbView.setBackgroundColor(view.getResources().getColor(R.color.colorPrimary));
            TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
//            textView.setTextColor(Color.WHITE);
            textView.setTextSize(18);
            textView.setGravity(Gravity.CENTER);
            sbView.setPadding(0, 60, 0, 0);
            sb.show();
        }
    }


}
/*

//之前两个Toast的布局一样，写一个通用类可以重用，但涉及到静态
class ToastLayout {
    Context context;
    RelativeLayout layout;
    ImageView imageView;
    TextView textView;

    public RelativeLayout getLayout() {
        return layout;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public TextView getTextView() {
        return textView;
    }

    public ToastLayout(Context context) {
        this.context = context;
        // xml中的dp要转为px
        int dp5 = Tools.dp2px(context, 3);
        int dp12 = Tools.dp2px(context, 8);
        //相对布局需要的id，最好使用xml中的id
        final int IMAGEID = 0x000012;

        //设置布局
        layout = new RelativeLayout(context);
        RelativeLayout.LayoutParams layoutParam = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParam.addRule(RelativeLayout.CENTER_VERTICAL);
        layoutParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(dp12, dp12, dp12, dp12);
        layout.setBackgroundResource(R.drawable.white_blue_line);
//        layout.setBackgroundResource(R.drawable.toast_dark);
        layout.setLayoutParams(layoutParam);

        //设置图片
        imageView = new ImageView(context);
        RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        imageParams.setMargins(dp5, 0, 0, 0);
        imageParams.addRule(RelativeLayout.CENTER_VERTICAL);
        imageView.setLayoutParams(imageParams);
        //noinspection ResourceType
        imageView.setId(IMAGEID);

        //设置文本
        textView = new TextView(context);
        RelativeLayout.LayoutParams textParam = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        textParam.setMargins(dp5, 0, 0, 0);
        textParam.addRule(RelativeLayout.CENTER_VERTICAL);
        textParam.addRule(RelativeLayout.RIGHT_OF, IMAGEID);
        textView.setTextColor(context.getResources().getColor(R.color.glb_blue));
        textView.setTextSize(16);
        textView.setPadding(0, 0, dp5, 0);
        textView.setMaxLines(3);
        textView.setLayoutParams(textParam);
        layout.addView(imageView);
        layout.addView(textView);
    }

    //静态类也是可以用的
    public static ToastLayout toastlayout(Context context) {
        ToastLayout toastLayout = new ToastLayout(context);
        return toastLayout;
    }

}

*/
