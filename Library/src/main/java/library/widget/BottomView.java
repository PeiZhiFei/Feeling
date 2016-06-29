package library.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import my.library.R;


/*
    BottomView bottomView = new BottomView(this,R.layout.bottom_view);
    bottomView.showBottomView();

    当使用listview的时候点击每一项
    bottomView.dismissBottomView();
 */
public class BottomView {
    private final View convertView;
    private final Context context;
    private Dialog bv;
    private boolean isTop = false;

    public BottomView(Context c, View convertView) {
        this.context = c;
        this.convertView = convertView;
    }

    public BottomView(Context c, int resource) {
        this.context = c;
        this.convertView = View.inflate (c, resource, null);
    }

    /**
     * 点击外部是否可以消失
     */
    @SuppressWarnings("deprecation")
    public void showBottomView () {
        this.bv = new Dialog (this.context, R.style.dialog_bottom_style);
        // 点击外部可以消失
        this.bv.setCanceledOnTouchOutside (true);
        this.bv.getWindow ().requestFeature (1);
        this.bv.setContentView (this.convertView);
        Window wm = this.bv.getWindow ();
        WindowManager m = wm.getWindowManager ();
        Display d = m.getDefaultDisplay ();
        WindowManager.LayoutParams p = wm.getAttributes ();
        p.width = (d.getWidth () * 1);
        if (this.isTop) {
            p.gravity = 48;
        } else {
            p.gravity = 80;
        }
        wm.setAttributes (p);
        this.bv.show ();
    }

    public void setTopIfNecessary () {
        this.isTop = true;
    }

    public View getView () {
        return this.convertView;
    }

    public void dismissBottomView () {
        if (this.bv != null) {
            this.bv.dismiss ();
        }
    }
}
