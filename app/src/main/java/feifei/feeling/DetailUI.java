package feifei.feeling;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import butterknife.ButterKnife;
import feifei.feeling.bean.Content;

public class DetailUI extends BaseActivity {
    @Bind(R.id.content_image)
    SimpleDraweeView contentImage;
    @Bind(R.id.title)
    TextView         title;
    @Bind(R.id.content)
    TextView         contents;
    @Bind(R.id.toolbar)
    Toolbar          toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_detail);
        ButterKnife.bind(this);

        Content content = getIntent().getParcelableExtra("content");
        if (content != null) {
            if (content.getContentfigureurl() != null) {
                contentImage.setImageURI(Uri.parse(content.getContentfigureurl().getFileUrl(this)));
            } else {
                contentImage.setVisibility(View.GONE);
            }
            title.setText(content.getAuthor().getUsername());
            contents.setText(content.getContent());
            toolbar.setTitle(content.getTitle());
        }
    }
}
