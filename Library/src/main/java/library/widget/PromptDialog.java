package library.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import library.util.Tools;
import my.library.R;

public class PromptDialog extends Dialog {
    public static final int TYPE_CONFIRM = 0;
    public static final int TYPE_CONFIRM_CANCEL = 1;
    private Context context;

    TextView titleTV;
    TextView contentTV;
    View btnDivider;
    Button cancelBtn;
    Button confirmBtn;

    int type = TYPE_CONFIRM_CANCEL;
    String content = "";

    public PromptDialog(Context context) {
        super(context, R.style.dialog_loading_style);
        this.context = context;
        init();
    }

    public PromptDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
        init();
    }

    public void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_prompt, null);
        setContentView(view, new ViewGroup.LayoutParams((int) (Tools.getWidth(context) * 0.8),
                ViewGroup.LayoutParams.WRAP_CONTENT));

        titleTV = (TextView) findViewById(R.id.prompt_title);
        contentTV = (TextView) findViewById(R.id.prompt_content);
        btnDivider = findViewById(R.id.prompt_vertical_divider);
        cancelBtn = (Button) findViewById(R.id.prompt_cancel);
        confirmBtn = (Button) findViewById(R.id.prompt_confirm);

        setType(type);
        setContent(content);
    }

    public PromptDialog setConfirmButton(String text,
                                         View.OnClickListener onClickListener) {
        confirmBtn.setText(TextUtils.isEmpty(text)?"确定":text);
        confirmBtn.setOnClickListener(onClickListener);
        return this;
    }

    public PromptDialog setLeft() {
        contentTV.setGravity(Gravity.LEFT);
        return this;
    }

    public PromptDialog setCenter() {
        contentTV.setGravity(Gravity.CENTER);
        return this;
    }

    public PromptDialog setCancelButton(String text,
                                        View.OnClickListener onClickListener) {
        cancelBtn.setText(TextUtils.isEmpty(text)?"取消":text);
        cancelBtn.setOnClickListener(onClickListener);
        return this;
    }

    public PromptDialog setType(int type) {
        if (TYPE_CONFIRM == type) {
            btnDivider.setVisibility(View.GONE);
            cancelBtn.setVisibility(View.GONE);
            confirmBtn.setBackgroundResource(R.drawable.bt_bottom);
        } else {
            btnDivider.setVisibility(View.VISIBLE);
            cancelBtn.setVisibility(View.VISIBLE);
            confirmBtn.setBackgroundResource(R.drawable.bt_right);
            cancelBtn.setBackgroundResource(R.drawable.bt_left);
        }
        return this;
    }

    public PromptDialog setTitle(String title) {
        titleTV.setText(title);
        return this;
    }

    public PromptDialog setContent(String content) {
        contentTV.setText(content);
        return this;
    }

    @Override
    public void show() {
        if (contentTV.getText().length() > 12) {
            contentTV.setGravity(Gravity.LEFT);
        }
        super.show();
    }

}