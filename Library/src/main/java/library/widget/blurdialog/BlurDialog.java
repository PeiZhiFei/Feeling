package library.widget.blurdialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import library.other.OnButtonClick;
import library.util.S;
import my.library.R;

public class BlurDialog extends SupportBlurDialogFragment {


    private String title;
    private boolean single;
    private int confirm;
    private int cancel;
    private boolean can;
    private boolean hasTitle;
    private int cancelColor;
    private int confirmColor;
    OnButtonClick onConfirmListener;
    OnButtonClick onCancelListener;

    public static BlurDialog newInstance(String title, boolean single, int confirm, int cancel, boolean can, boolean hasTitle, int cancelColor, int confirmColor) {
        BlurDialog fragment = new BlurDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putBoolean("single", single);
        args.putInt("confirm", confirm);
        args.putInt("cancel", cancel);
        args.putBoolean("can", can);
        args.putBoolean("hasTitle", hasTitle);
        args.putInt("cancelColor", cancelColor);
        args.putInt("confirmColor", confirmColor);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        title = args.getString("title");
        single = args.getBoolean("single");
        confirm = args.getInt("confirm");
        cancel = args.getInt("cancel");
        can = args.getBoolean("can");
        hasTitle = args.getBoolean("hasTitle");
        cancelColor = args.getInt("cancelColor");
        confirmColor = args.getInt("confirmColor");
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_button, null);

        LinearLayout layout1 = (LinearLayout) view.findViewById(R.id.title_layout);
        if (!hasTitle) {
            layout1.setVisibility(View.GONE);
        }

        //这其实是内容
        TextView titleTv = (TextView) view.findViewById(R.id.title);
        if (!TextUtils.isEmpty(title)) {
            titleTv.setText(title);
        }

//        //dialog定制
//        TextView dialogTitleTv = (TextView) view.findViewById(R.id.dialog_title);
//        SimpleDraweeView logoImage = (SimpleDraweeView) view.findViewById(R.id.logo_image_view);
//        String s = SPHelper.getLoginContent();
//        if (!TextUtils.isEmpty(s)) {
//            dialogTitleTv.setText(s + "提示您");
//        }
//        if (!TextUtils.isEmpty(SPHelper.getLogoUrl())) {
//            logoImage.setImageURI(UriUtil.getHttpUri(SPHelper.getLogoUrl()));
//        }

        // TODO: 2015/12/4 0004 之前只用了一个组件
//        Drawable drawable1 = context.getResources().getDrawable(R.drawable.ic_launcher);
//        drawable1.setBounds(0, 0, DensityUtil.dip2px(context, 25), DensityUtil.dip2px(context, 25));//第一0是距左边距离，第二0是距上边距离，40分别是长宽
//        dialogTitleTv.setCompoundDrawables(drawable1, null, null, null);//只放左边
//        dialogTitleTv.setCompoundPadding(0, 0, DensityUtil.dip2px(context, 25), DensityUtil.dip2px(context, 25));

        TextView confirmBtn = (TextView) view.findViewById(R.id.confirm);
        TextView cancelBtn = (TextView) view.findViewById(R.id.cancel);
        View deliver = view.findViewById(R.id.deliver);

        if (single) {
            deliver.setVisibility(View.GONE);
            cancelBtn.setVisibility(View.GONE);
        } else {
            if (cancel != 0) {
                cancelBtn.setText(getResources().getString(cancel));
            }
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onCancelListener != null) {
                        onCancelListener.buttonClick();
                    }
                    dismiss();
                }
            });
            if (cancelColor != -1) {
                cancelBtn.setTextColor(cancelColor);
            }

        }
        if (confirm != 0) {
            confirmBtn.setText(getResources().getString(confirm));
            if (confirmColor != -1) {
                confirmBtn.setTextColor(confirmColor);
            }
        }
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onConfirmListener != null) {
                    onConfirmListener.buttonClick();
                }
                dismiss();
            }
        });
        setCancelable(can);
        builder.setView(view);
        return builder.create();
    }

    @Override
    protected boolean isDebugEnable() {
        return false;
    }

    @Override
    protected boolean isDimmingEnable() {
        return true;
    }

    @Override
    protected boolean isActionBarBlurred() {
        return false;
    }

    @Override
    protected float getDownScaleFactor() {
        if (S.readBoolear(getActivity(), "3", false)) {
            return 6;
        } else {
            return 1.1f;
        }
    }

    @Override
    protected int getBlurRadius() {
        if (S.readBoolear(getActivity(), "3", false)) {
            return 11;
        } else {
            return 1;
        }
    }

    @Override
    protected boolean isRenderScriptEnable() {
        return false;
    }

    public void OnConfirmListener(OnButtonClick onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
    }

    public void OnCancelListener(OnButtonClick onCancelListener) {
        this.onCancelListener = onCancelListener;
    }
}
