package library.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import my.library.R;


/*
    EmptyView helper = new EmptyView();
    View view2 = helper.getEmptyView(getActivity(), 0);
    ((ViewGroup) shopList.getParent()).addView(view2);
    emptyTextView = helper.getEmptyText();
    emptyTextView.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {}
         });
    shopList.setEmptyView(view2);
 */
public class EmptyView {

    //这里是为了更改文字
    protected TextView emptyText;

    public TextView getEmptyText() {
        return emptyText;
    }


    /**
     * @param context
     * @param dr      传入文字上方的图片
     */
    public View getEmptyView(Context context, int dr) {
        LinearLayout layout = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParamsss = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(layoutParamsss);
        emptyText = new TextView(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER);
        // layoutParams.gravity = Gravity.CENTER;
        layoutParams.weight = 1;
        layoutParams.setMargins(5, Tools.dp2px(context, 150), 5, 5);
        emptyText.setLayoutParams(layoutParams);
        emptyText.setText("这里是空的");
        emptyText.setTextSize(20);
        emptyText.setTextColor(context.getResources().getColor(R.color.color_text_second));
        emptyText.setGravity(Gravity.CENTER_HORIZONTAL);
        Drawable drawable;
        if (dr != 0) {
            drawable = context.getResources().getDrawable(dr);
        } else {
            drawable = context.getResources().getDrawable(R.drawable.nodata);
        }
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight());
        emptyText.setCompoundDrawables(null, drawable, null, null);
        emptyText.setCompoundDrawablePadding(30);
        layout.setVisibility(View.GONE);
        layout.addView(emptyText, layoutParams);
        //只是为了把文字顶上去
        View zhanwei = new View(context);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, 200);
        p.weight = 1;
        layout.addView(zhanwei, p);
        return layout;
    }

}
