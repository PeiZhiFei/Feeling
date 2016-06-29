package library.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;

import my.library.R;


public class DownloadDialog extends Dialog {

    private final Context context;
    protected Dialog dialogs;
    private boolean open = false;
    private ArrowDownloadButton newtonCradleLoading;

    public DownloadDialog(Context context) {
        super(context, R.style.dialog_loading_style);
        this.context = context;
    }

    public void dialogInit(boolean can) {
        if (context != null) {
            dialogs = new Dialog(context, R.style.dialog_loading_style);
            dialogs.getWindow().getAttributes().gravity = Gravity.CENTER;
            dialogs.setContentView(R.layout.dialog_download);
//            dialogs.setCancelable(false);
            dialogs.setCancelable(can);
            newtonCradleLoading = (ArrowDownloadButton) dialogs.findViewById(R.id.arrow_download_button);
            newtonCradleLoading.startAnimating();
        }
    }

    public synchronized void dialogDismiss() {
        if (open && dialogs != null) {
            dialogs.dismiss();
            open = false;
        }
    }

    public synchronized void dialogProgress(float progress) {
        if (open && dialogs != null) {
            newtonCradleLoading.setProgress(progress);
        }
    }


    public synchronized void dialogShow() {
        if (!open && dialogs != null) {
            dialogs.show();
            open = true;
        }
    }
}
