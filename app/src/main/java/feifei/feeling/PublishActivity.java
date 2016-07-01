package feifei.feeling;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.roger.catloadinglibrary.CatLoadingView;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import feifei.feeling.bean.Content;
import feifei.feeling.bean.User;
import feifei.feeling.widget.MultiImageEditGridView;
import io.github.lijunguan.imgselector.ImageSelector;
import library.util.FileUtil;
import library.util.PhotoUtil;
import library.util.TS;
import library.util.Tools;
import library.widget.RevealBackgroundView;

public class PublishActivity extends BaseActivity implements RevealBackgroundView.OnStateChangeListener {
    @Bind(R.id.title)
    EditText               title;
    @Bind(R.id.content)
    EditText               content;
    @Bind(R.id.anim_layout)
    LinearLayout           anim_layout;
    @Bind(R.id.image_grid_view)
    MultiImageEditGridView imageGridView;
    @Bind(R.id.vRevealBackground)
    RevealBackgroundView   vRevealBackground;

    CatLoadingView loadingView;

    public static final  String       ARG_REVEAL_START_LOCATION = "reveal_start_location";
    private static final Interpolator ACCELERATE_INTERPOLATOR   = new AccelerateInterpolator();
    private static final Interpolator DECELERATE_INTERPOLATOR   = new DecelerateInterpolator();

//    ImageChooser mImageChooser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_publish);
        ButterKnife.bind(this);
        imageGridView.setmMaxImage(5);
        imageGridView.setAddOnClickListener(view -> takeImage());
        setupRevealBackground(savedInstanceState);
        loadingView = new CatLoadingView();
    }

    public static void startCameraFromLocation(int[] startingLocation, Activity startingActivity) {
        Intent intent = new Intent(startingActivity, PublishActivity.class);
        intent.putExtra(ARG_REVEAL_START_LOCATION, startingLocation);
        startingActivity.startActivityForResult(intent, 5);
    }


    private void setupRevealBackground(Bundle savedInstanceState) {
        vRevealBackground.setFillPaintColor(getResources().getColor(R.color.glb_blue));
        vRevealBackground.setOnStateChangeListener(this);
        if (savedInstanceState == null) {
            final int[] startingLocation = getIntent().getIntArrayExtra(ARG_REVEAL_START_LOCATION);
            vRevealBackground.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    vRevealBackground.getViewTreeObserver().removeOnPreDrawListener(this);
                    vRevealBackground.startFromLocation(startingLocation);
                    return true;
                }
            });
        } else {
            vRevealBackground.setToFinishedFrame();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        publish();
        return super.onOptionsItemSelected(item);
    }

    public void takeImage() {
        ImageSelector imageSelector = ImageSelector.getInstance();
        loadConfig(imageSelector).startSelect(this);
    }

    private ImageSelector loadConfig(ImageSelector imageSelector) {
        imageSelector.setSelectModel(ImageSelector.MULTI_MODE);
        //头像
//        imageSelector.setSelectModel(ImageSelector.AVATOR_MODE);
        imageSelector.setToolbarColor(ContextCompat.getColor(this, R.color.blue));
        imageSelector.setShowCamera(true);
        imageSelector.setMaxCount(4);
        imageSelector.setGridColumns(3);
        return imageSelector;
    }




      /*  if (mImageChooser == null) {
            mImageChooser = new ImageChooser(this, true, true, false, true, 1);
            mImageChooser.setOnImageChosenListener((bitmap, type) ->
                    {
                        List<Bitmap> bitmapList = imageGridView.getImages();
                        if (bitmapList != null && bitmapList.size() >= imageGridView.getMaxImageCount()) {
                            TS.s(this, "已达上限", 2);
                            BitmapUtil.recycleBitmap(bitmap);
                        } else {
                            imageGridView.addImage(bitmap);
//                            imageGridView.setImage(0, bitmap);
                        }
                    }
            );
            mImageChooser.setOnOperationClickListener(type -> {
                if (type == ImageChooser.TYPE_IMAGE && imageGridView.isMax()) {
                    TS.s(this, "已达上限", 2);
                }
                return true;
            });
        } else {
            mImageChooser.setMax(imageGridView.getMaxImageCount() - imageGridView.getCount() + 1);
        }
        mImageChooser.takeImage(getResources().getString(R.string.add_picture));*/

    public void publish() {
        String titles = title.getText().toString().trim();
        String commitContent = content.getText().toString().trim();
        if (TextUtils.isEmpty(titles)) {
//            TSnackbar.make(vRevealBackground, "请输入标题", Prompt.WARNING).show();
            TS.tip(this, "请输入标题");
        } else if (TextUtils.isEmpty(commitContent)) {
//            TSnackbar.make(vRevealBackground, "请输入内容", Prompt.WARNING).show();
            TS.tip(this, "请输入内容");
        } else if (imageGridView.getImages().size() > 0) {
            show();

            Bitmap b = imageGridView.getImages().get(0);
            long x = System.currentTimeMillis();

            FileUtil.saveBitmap(b, FileUtil.getRootPath(), x + "");
//            L.l(FileUtil.getRootPath() + x + ".JPEG");
            final BmobFile figureFile = new BmobFile(new File(FileUtil.getRootPath() + x + ".JPEG"));
            figureFile.upload(this, new UploadFileListener() {

                @Override
                public void onSuccess() {
//                    TS.s(PublishActivity.this, "上传文件成功");
                    publishWithoutFigure(titles, commitContent, figureFile);
                }

                @Override
                public void onFailure(int arg0, String arg1) {
                    TS.tip(PublishActivity.this, "上传文件失败" + arg0 + arg1);
//                    TSnackbar.make(vRevealBackground, "上传文件失败" + arg0 + arg1, Prompt.ERROR).show();
                    hide();
                }
            });
        } else {
            publishWithoutFigure(titles, commitContent, null);
        }
    }

    boolean show = false;

    private void show() {
        loadingView.show(getSupportFragmentManager(), "");
        show = true;
    }

    private void hide() {
        try {
            loadingView.dismiss();
        } catch (Exception e) {
        }
        show = false;
    }

    private void publishWithoutFigure(final String titles, final String commitContent,
                                      final BmobFile figureFile) {
        if (!show) {
            show();
        }
        User user = BmobUser.getCurrentUser(this, User.class);

        final Content qiangYu = new Content();
        qiangYu.setAuthor(user);
        qiangYu.setContent(commitContent);
        if (figureFile != null) {
            qiangYu.setContentfigureurl(figureFile);
        }
        qiangYu.setLove(0);
        qiangYu.setHate(0);
        qiangYu.setTitle(titles);
//        qiangYu.setShare(0);
        qiangYu.setComment(0);
        qiangYu.setPass(true);
        qiangYu.save(this, new SaveListener() {

            @Override
            public void onSuccess() {
//                TS.s(PublishActivity.this, "发表成功");
                hide();
                setResult(Activity.RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(int arg0, String arg1) {
                TS.tip(PublishActivity.this, "发表失败");
//                TSnackbar.make(vRevealBackground, "发表失败" + arg0 + arg1, Prompt.ERROR).show();
                hide();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        mImageChooser.onActivityResult(requestCode, resultCode, data);
//        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ImageSelector.REQUEST_SELECT_IMAGE) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> imagesPath = data.getStringArrayListExtra(ImageSelector.SELECTED_RESULT);
                if (imagesPath != null) {
                    for (int i = 0; i < imagesPath.size(); i++) {
                        Bitmap bitmap = PhotoUtil.getBitmapFromFile(imagesPath.get(i));
                        imageGridView.addImage(bitmap);
                    }
                }

            }
        }
    }


    @Override
    public void onStateChange(int state) {
        if (RevealBackgroundView.STATE_FINISHED == state) {
//            title.setVisibility(View.VISIBLE);
//            new android.os.Handler().postDelayed(() -> {
//                content.setVisibility(View.VISIBLE);
//            }, 80);
//            new android.os.Handler().postDelayed(() -> {
//                anim_layout.setVisibility(View.VISIBLE);
//            }, 160);
            int x = -Tools.dp2px(this, 100);
            title.animate().translationY(x).alpha(1).setDuration(250).setInterpolator(DECELERATE_INTERPOLATOR).start();
            content.animate().translationY(x).alpha(1).setDuration(250).setStartDelay(100).setInterpolator(DECELERATE_INTERPOLATOR).start();
            anim_layout.animate().translationY(x).alpha(1).setDuration(250).setStartDelay(200).setInterpolator(DECELERATE_INTERPOLATOR).start();

            new android.os.Handler().postDelayed(() -> {
                content.setFocusable(true);
                content.setFocusableInTouchMode(true);
                title.setFocusable(true);
                title.setFocusableInTouchMode(true);
                title.requestFocus();
                InputMethodManager inputManager = (InputMethodManager) title.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(title, 0);
            }, 600);
//            vRevealBackground.setFillPaintColor(getResources().getColor(R.color.transparent));
        }
    }

}
