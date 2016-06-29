package library.widget.snackbar;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.WeakHashMap;

import my.library.R;


/**
 * 谁谁谁 2014-7-28 下午8:17:38
 * 自己写的toast组件，默认图标，默认显示在底部，白色，右进左出
 * 可以把自己的view传进来，还有番茄土豆的toast的效果
 */
public class ToastCustom {

    public static int LENGTH = 2000;
    private final Context context;
    private View mView;
    private LayoutParams mLayoutParams;
    private boolean mFloating;

    private static boolean supertToast;

    public ToastCustom(Context context) {
        this.context = context;
    }

    /**
     * 带动画的toast，默认在底部，短时间，右进左出
     */
    public static void toast(Context context, CharSequence text) {
        toastShow(context, text, Gravity.BOTTOM, false, false, null).show();
    }


    /**
     * grivaty：用Gravity类的参数
     * time：true为长时间4秒，false为短时间2秒
     * 带动画的toast，自定义位置，自定义时间长短
     */
    public static void toast(Context context, CharSequence text, int grivaty,
                             boolean time) {
        toastShow(context, text, grivaty, time, false, null).show();
    }


    /**
     * supertoast是指番茄土豆中的toast效果，底部弹一个短暂的提示
     */
    public static void superToast(Context context, CharSequence text) {
        toastShow(context, text, Gravity.BOTTOM, false, true, null).show();
    }

    /**
     * grivaty:用Gravity类的参数
     * time:true为长时间4秒，false为短时间2秒
     * supertoast:是否使用supertoast样式，暗黑色无圆角
     * customLayout:可以传一个自定义的view过来，用则supertoast无效
     */
    public static void toast(Context context, CharSequence text, int grivaty,
                             boolean time, boolean supertoast, View customLayout) {
        toastShow(context, text, grivaty, time, supertoast, customLayout).show();
    }

    @SuppressWarnings("ResourceType")
    private static ToastCustom toastShow(Context context, CharSequence text,
                                         int gravity, boolean time, boolean supertoast, View customLayout) {
        supertToast = supertoast;
        //这里的layout相对布局支持了RTL布局，从右向左阅读：http://article.yeeyan.org/view/37503/335086
        RelativeLayout layout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.toast_layout, null);
        ImageView imageView = (ImageView) layout.findViewById(R.id.toast_image);
        TextView textView = (TextView) layout.findViewById(R.id.toast_text);

        if (supertoast) {
            layout.setBackgroundColor(Color.parseColor("#90000000"));
            textView.setTextColor(context.getResources().getColor(R.color.lightgray));
            imageView.setVisibility(View.GONE);
        }

        ToastCustom result = new ToastCustom(context);
        textView.setText("" + text);
        if (time) {
            LENGTH = 4000;
        } else {
            LENGTH = 2000;
        }
        result.mView = customLayout != null ? customLayout : layout;
        result.mFloating = true;
        result.setLayoutGravity(gravity);
        return result;
    }

    //这是两个类关联的地方
    public void show() {
        ToastManager manager = ToastManager.obtain((Activity) context);
        manager.add(this, supertToast);
    }

    public boolean isShowing() {
        if (mFloating) {
            return mView != null && mView.getParent() != null;
        } else {
            return mView.getVisibility() == View.VISIBLE;
        }
    }

    public void cancel() {
        ToastManager.obtain((Activity) context).clearMsg(this);

    }

    public static void cancelAll() {
        ToastManager.clearAll();
    }

    public static void cancelAll(Context context) {
        ToastManager.release((Activity) context);
    }

    public Activity getActivity() {
        return (Activity) context;
    }

    public void setView(View view) {
        mView = view;
    }

    public View getView() {
        return mView;
    }

    public LayoutParams getLayoutParams() {
        if (mLayoutParams == null) {
            mLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT);
        }
        return mLayoutParams;
    }

    public ToastCustom setLayoutGravity(int gravity) {
        mLayoutParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT, gravity);
        return this;
    }

    public boolean isFloating() {
        return mFloating;
    }

    public void setFloating(boolean mFloating) {
        this.mFloating = mFloating;
    }

}

/**
 * ToastCustom的控制类
 */
class ToastManager extends Handler {

    private static final int MESSAGE_DISPLAY = 0xc2007;
    private static final int MESSAGE_ADD_VIEW = 0xc20074dd;
    private static final int MESSAGE_REMOVE = 0xc2007de1;

    private static WeakHashMap<Activity, ToastManager> toastManager;
    private static ReleaseCallbacks releaseCallbacks;
    //消息队列
    private final Queue<ToastCustom> msgQueue;
    //进出动画和时间参数
    private Animation inAnimation, outAnimation;

    private ToastManager() {
        //实现了一个队列LinkedList
        msgQueue = new LinkedList<ToastCustom>();
    }

    public static synchronized ToastManager obtain(Activity activity) {
        if (toastManager == null) {
            toastManager = new WeakHashMap<Activity, ToastManager>(1);
        }
        ToastManager manager = toastManager.get(activity);
        if (manager == null) {
            manager = new ToastManager();
            ensureReleaseOnDestroy(activity);
            toastManager.put(activity, manager);
        }

        return manager;
    }

    static void ensureReleaseOnDestroy(Activity activity) {
        if (releaseCallbacks == null) {
            releaseCallbacks = new ReleaseCallbacksIcs();
        }
        releaseCallbacks.register(activity.getApplication());

    }

    public static synchronized void release(Activity activity) {
        if (toastManager != null) {
            final ToastManager manager = toastManager.remove(activity);
            if (manager != null) {
                manager.clearAllMsg();
            }
        }
    }

    public static synchronized void clearAll() {
        if (toastManager != null) {
            final Iterator<ToastManager> iterator = toastManager.values()
                    .iterator();
            while (iterator.hasNext()) {
                final ToastManager manager = iterator.next();
                if (manager != null) {
                    manager.clearAllMsg();
                }
                iterator.remove();
            }
            toastManager.clear();
        }
    }


    public void add(ToastCustom toastCustom, boolean superToast) {
        // 加入队列
        msgQueue.add(toastCustom);
        // 如果想统一动画，则需要加上不等于null，全局的一个实例变量
        // if (inAnimation == null) {
        if (!superToast) {
            inAnimation = AnimationUtils.loadAnimation(
                    toastCustom.getActivity(), R.anim.slide_in_from_right);
        } else {
            inAnimation = AnimationUtils.loadAnimation(
                    toastCustom.getActivity(), R.anim.slide_in_from_bottom);
        }
        // }
        // if (outAnimation == null) {
        if (!superToast) {
            outAnimation = AnimationUtils.loadAnimation(
                    toastCustom.getActivity(), R.anim.slide_out_to_left);
        } else {
            outAnimation = AnimationUtils.loadAnimation(
                    toastCustom.getActivity(), R.anim.slide_out_to_bottom);
        }
        // }
        displayMsg();
    }

    public void clearMsg(ToastCustom toastCustom) {
        if (msgQueue.contains(toastCustom)) {
            removeMessages(MESSAGE_DISPLAY, toastCustom);
            removeMessages(MESSAGE_ADD_VIEW, toastCustom);
            removeMessages(MESSAGE_REMOVE, toastCustom);
            msgQueue.remove(toastCustom);
            removeMsg(toastCustom);
        }
    }

    void clearAllMsg() {
        if (msgQueue != null) {
            msgQueue.clear();
        }
        removeMessages(MESSAGE_DISPLAY);
        removeMessages(MESSAGE_ADD_VIEW);
        removeMessages(MESSAGE_REMOVE);
    }

    private void displayMsg() {
        if (msgQueue.isEmpty()) {
            return;
        }
        final ToastCustom toastCustom = msgQueue.peek();
        final Message msg;
        if (!toastCustom.isShowing()) {
            msg = obtainMessage(MESSAGE_ADD_VIEW);
            msg.obj = toastCustom;
            sendMessage(msg);
        } else {
            msg = obtainMessage(MESSAGE_DISPLAY);
            // 时间的长度在这里
            sendMessageDelayed(msg,
                    ToastCustom.LENGTH + inAnimation.getDuration()
                            + outAnimation.getDuration());
        }
    }

    private void removeMsg(final ToastCustom toastCustom) {
        clearMsg(toastCustom);
        final View view = toastCustom.getView();
        ViewGroup parent = ((ViewGroup) view.getParent());
        if (parent != null) {
            outAnimation.setAnimationListener(new OutAnimationListener(
                    toastCustom));
            view.startAnimation(outAnimation);
            if (toastCustom.isFloating()) {
                parent.removeView(view);
            } else {
                toastCustom.getView().setVisibility(View.INVISIBLE);
            }
        }

        Message msg = obtainMessage(MESSAGE_DISPLAY);
        sendMessage(msg);
    }

    private void addMsgToView(ToastCustom toastCustom) {
        View view = toastCustom.getView();
        if (view.getParent() == null) {
            toastCustom.getActivity().addContentView(view,
                    toastCustom.getLayoutParams());
        }
        view.startAnimation(inAnimation);
        if (view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
        }
        final Message msg = obtainMessage(MESSAGE_REMOVE);
        msg.obj = toastCustom;
        sendMessageDelayed(msg, ToastCustom.LENGTH);
    }

    @Override
    public void handleMessage(Message msg) {
        final ToastCustom toastCustom;
        switch (msg.what) {
            case MESSAGE_DISPLAY:
                displayMsg();
                break;
            case MESSAGE_ADD_VIEW:
                toastCustom = (ToastCustom) msg.obj;
                addMsgToView(toastCustom);
                break;
            case MESSAGE_REMOVE:
                toastCustom = (ToastCustom) msg.obj;
                removeMsg(toastCustom);
                break;
            default:
                super.handleMessage(msg);
                break;
        }
    }

    private static class OutAnimationListener implements
            Animation.AnimationListener {
        private final ToastCustom toastCustom;

        private OutAnimationListener(ToastCustom toastCustom) {
            this.toastCustom = toastCustom;
        }

        public void onAnimationStart(Animation animation) {

        }

        public void onAnimationEnd(Animation animation) {
            if (!toastCustom.isFloating()) {
                toastCustom.getView().setVisibility(View.GONE);
            }
        }

        public void onAnimationRepeat(Animation animation) {

        }
    }

    interface ReleaseCallbacks {
        void register(Application application);
    }

    static class ReleaseCallbacksIcs implements Application.ActivityLifecycleCallbacks,
            ReleaseCallbacks {
        private WeakReference<Application> mLastApp;

        public void register(Application app) {
            if (mLastApp != null && mLastApp.get() == app) {
                return;
            } else {
                mLastApp = new WeakReference<Application>(app);
            }
            app.registerActivityLifecycleCallbacks(this);
        }

        public void onActivityDestroyed(Activity activity) {
            release(activity);
        }

        public void onActivityCreated(Activity activity,
                                      Bundle savedInstanceState) {
        }

        public void onActivityStarted(Activity activity) {
        }

        public void onActivityResumed(Activity activity) {
        }

        public void onActivityPaused(Activity activity) {
        }

        public void onActivityStopped(Activity activity) {
        }

        public void onActivitySaveInstanceState(Activity activity,
                                                Bundle outState) {
        }
    }
}
