package feifei.feeling;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import feifei.feeling.bean.Content;
import feifei.feeling.bean.User;
import feifei.feeling.widget.ImageChooser;
import feifei.feeling.widget.MultiImageEditGridView;
import library.util.BitmapUtil;
import library.util.FileUtil;
import library.util.TS;

public class PublishActivity extends BaseActivity {
    @Bind(R.id.title)
    EditText               title;
    @Bind(R.id.content)
    EditText               content;
    @Bind(R.id.image_grid_view)
    MultiImageEditGridView imageGridView;

    ImageChooser mImageChooser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_publish);
        ButterKnife.bind(this);
        imageGridView.setAddOnClickListener(view -> takeImage());
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
        if (mImageChooser == null) {
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
        mImageChooser.takeImage(getResources().getString(R.string.add_picture));
    }

    public void publish() {
        String titles = title.getText().toString().trim();
        String commitContent = content.getText().toString().trim();
        if (TextUtils.isEmpty(titles)) {
            TS.s(this, "请输入标题");
        } else if (TextUtils.isEmpty(commitContent)) {
            TS.s(this, "请输入内容");
        } else if (imageGridView.getImages().size() > 0) {
            Bitmap b = imageGridView.getImages().get(0);
            long x = System.currentTimeMillis();

//            if (b == null) {
//                L.l("########");
//            }

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
                    TS.s(PublishActivity.this, "上传文件失败" + arg0 + arg1);
                }
            });
        } else {
            publishWithoutFigure(titles, commitContent, null);
        }
    }

    private void publishWithoutFigure(final String titles, final String commitContent,
                                      final BmobFile figureFile) {
        User user = BmobUser.getCurrentUser(this, User.class);

        final Content qiangYu = new Content();
        qiangYu.setAuthor(user);
        qiangYu.setContent(commitContent);
        if (figureFile != null) {
            qiangYu.setContentfigureurl(figureFile);
        }
        qiangYu.setLove(0);
        qiangYu.setHate(0);
        qiangYu.setShare(0);
        qiangYu.setComment(0);
        qiangYu.setPass(true);
        qiangYu.save(this, new SaveListener() {

            @Override
            public void onSuccess() {
//                TS.s(PublishActivity.this, "发表成功");
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(int arg0, String arg1) {
                TS.s(PublishActivity.this, "发表失败");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mImageChooser.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

}
