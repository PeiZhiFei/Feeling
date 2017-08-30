package com.feifei.lifetools;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.MediaColumns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.feifei.lifetools.BaseActivity;
import com.feifei.lifetools.R;
import com.feifei.util.AnimUtil;
import com.feifei.util.ConfigUtil;
import com.feifei.util.DialogUtil;
import com.feifei.util.FileUtil;
import com.feifei.util.IntentUtil;
import com.feifei.util.MyWebView;
import com.feifei.util.PhotoUtil;
import com.feifei.view.ScaleButton;

public class SettingActivity extends BaseActivity implements ScaleButton.ScaleButtonListener {
    //    private Slider switch_auto;
    // public int mTheme = R.style.Holo;
    Uri outputFileUri;
    ImageView myimage;
    ScaleButton aboutUs, share, feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 在super之前
        // initTheme(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        FileUtil.isDirExistAndCreat(path);
        initImage();
        initView();
        initEvents();
        initAnim();
    }

    private void initImage() {
        myimage = (ImageView) findViewById(R.id.myimage);
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            myimage.setImageBitmap(PhotoUtil.getCircleImage(BitmapFactory.decodeFile(ConfigUtil.readString(activity,
                                    "photo", ""))));
                            AnimUtil.animFallDown(myimage);
                        } catch (Exception e) {
                            myimage.setImageResource(R.drawable.myimage);
                            AnimUtil.animFallDown(myimage);
                        }

                    }
                });
            }
        }).start();
    }

    private void initAnim() {
        ImageView logbackground = (ImageView) findViewById(R.id.log_background);
        Drawable mPicture1 = getResources().getDrawable(R.drawable.login_image1);
        Drawable mPicture2 = getResources().getDrawable(R.drawable.login_image2);
        Drawable mPicture3 = getResources().getDrawable(R.drawable.login_image3);
        AnimUtil.animImageScreen(logbackground, mPicture1, mPicture2, mPicture3);
    }

    // private void initTheme(Bundle savedInstanceState) {
    // if (savedInstanceState == null) {
    // mTheme = ConfigUtil.readInt(this, "theme", R.style.Holo);
    // } else {
    // mTheme = savedInstanceState.getInt("theme");
    // }
    // setTheme(mTheme);
    // }

    private void initView() {
/*        if (!hasFlashlight) {
            switch_auto.setEnabled(false);
            switch_auto.setChecked(false);
        } else {
            switch_auto.setChecked(auto);
        }*/
        feedback = (ScaleButton) findViewById(R.id.button_feedback);
        share = (ScaleButton) findViewById(R.id.button_share);
        aboutUs = (ScaleButton) findViewById(R.id.button_about);
        feedback.setScaleButtonListener(this);
        share.setScaleButtonListener(this);
        aboutUs.setScaleButtonListener(this);
    }

    // 进行设置
    private void initEvents() {
 /*       switch_auto.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ConfigUtil.writeBoolean(activity, "auto", true);
                    ToastUtil.toast(activity, "开启自动闪光灯");
                } else {
                    ConfigUtil.writeBoolean(activity, "auto", false);
                    ToastUtil.toast(activity, "关闭自动闪光灯");
                }
            }
        });*/
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.button_about:
                aboutUs(view);
                break;
            case R.id.button_feedback:
                feedback(view);
                break;
           /* case R.id.button_game:
                IntentUtil.startWebview(activity, MyWebView.class, "http://app.mi.com/detail/98281", true);
                break;*/
            case R.id.button_share:
                DialogUtil.dialogShare(activity, "给你推荐一个【生活小助手】，功能很实用哦，快去应用商店下载吧！http://app.mi.com/detail/65536", "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/01a10d42e0633488b2594f6f3e964a38a5a478a8e");
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // if (mTheme != ConfigUtil.readInt(this, "theme", R.style.Holo)) {
        // reload();
        // }
    }

    // 保存皮肤状态
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // outState.putInt("theme", mTheme);
    }

    // 换肤方法
    // private void reload() {
    // Intent intent = getIntent();
    // finish();
    // overridePendingTransition(R.anim.fade, R.anim.hold);
    // startActivity(intent);
    // }

    public void aboutUs(View v) {
        IntentUtil.startWebview(activity, MyWebView.class, "file:///android_asset/YuToo/index.html", true);
    }

    public void feedback(View v) {
        DialogUtil.dialogFeedback(activity, null);
    }

    public void changePhoto(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);

        // File file = new File(path, "myimage.jpg");
        // outputFileUri = Uri.fromFile(file);
        // Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        // startActivityForResult(intent, 2);

    }

    // private void changeTheme() {
    // ConfigUtil
    // .writeInt(activity, "theme",
    // mTheme == R.style.Holo ? R.style.AppTheme_Change
    // : R.style.Holo);
    // ConfigUtil.writeBoolean(activity, "th", !th);
    // TS.t(activity, "换个皮肤，换种心境", TS.TOAST_SUCCESS);
    // reload();
    // }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaColumns.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
            myimage.setImageBitmap(PhotoUtil.getCircleImage(bitmap));
            ConfigUtil.writeString(activity, "photo", picturePath);
        } else if ((requestCode == 2 && resultCode == RESULT_OK)) {
            Bitmap bitmap = BitmapFactory.decodeFile(outputFileUri.getPath());
            myimage.setImageBitmap(PhotoUtil.getCircleImage(bitmap));
            ConfigUtil.writeString(activity, "photo", outputFileUri.getPath());
        }
    }

}
