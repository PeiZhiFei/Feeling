package feifei.feeling;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.listener.UpdateListener;
import feifei.feeling.bean.Content;
import library.util.L;
import library.util.TS;

public class DetailUI extends BaseActivity {
    @Bind(R.id.content_image)
    ImageView            contentImage;
    @Bind(R.id.title)
    TextView             title;
    @Bind(R.id.content)
    TextView             contents;
    @Bind(R.id.toolbar)
    Toolbar              toolbar;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    Content content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_detail);
        ButterKnife.bind(this);

        content = (Content) getIntent().getSerializableExtra("content");
        if (content != null) {
            if (content.getContentfigureurl() != null) {
                Picasso.with(this).load(content.getContentfigureurl().getFileUrl(this)).resize(400, 600).centerInside().into(contentImage);
//                contentImage.setImageURI(Uri.parse(content.getContentfigureurl().getFileUrl(this)));
            } else {
//                contentImage.setVisibility(View.GONE);
//                contentImage.setImageURI(Uri.parse("res:///" + R.drawable.place));
                contentImage.setImageResource(R.drawable.place);
            }
            title.setText(content.getAuthor().getUsername());
            contents.setText(content.getContent());
            toolbar.setTitle(content.getTitle());
            if (content.getMyFav()) {
                fab.setImageResource(R.drawable.ic_action_fav_choose);
            } else {
                fab.setImageResource(R.drawable.ic_action_fav_choose2);
            }
            fab.setOnClickListener(v -> {
                if (content.getMyFav()) {
                    content.setMyFav(false);
                    fab.setImageResource(R.drawable.ic_action_fav_choose2);
                } else {
                    content.setMyFav(true);
                    fab.setImageResource(R.drawable.ic_action_fav_choose);
                }
                //序列化的问题
                L.l(content.getObjectId());
                content.update(this, content.getObjectId(), new UpdateListener() {

                    @Override
                    public void onSuccess() {
//                        TS.s(DetailUI.this, "收藏成功:");
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        TS.s(DetailUI.this, "收藏失败:" + s);
                    }

                });
            });
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("result", content);
        setResult(Activity.RESULT_OK, intent);
        super.onBackPressed();
    }
}
