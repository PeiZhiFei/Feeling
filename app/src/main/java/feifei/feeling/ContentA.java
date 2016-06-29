package feifei.feeling;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import butterknife.ButterKnife;
import feifei.feeling.bean.Content;
import feifei.feeling.bean.User;
import library.base.MyBaseRecyclerAdapter;
import library.util.Tools;

//import library.widget.LevelTextView;

public class ContentA extends MyBaseRecyclerAdapter<Content, ContentA.MyViewHolder> {

    View.OnClickListener mItemClickListener;

    public ContentA(Context context, View.OnClickListener onClickListener) {
        super(context);
        mItemClickListener = onClickListener;
    }

    @Override
    public int getItemCount() {
        return getListCount() > 25 ? 25 : getListCount();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.ai_item, parent, false);
//        AnimUtil.animRightToLeft(view);
        MyViewHolder holder = new MyViewHolder(view);
        if (mItemClickListener != null) {
            holder.parentView.setOnClickListener(mItemClickListener);

        }
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Content content = getItem(position);
        User user = content.getAuthor();
        if (user.getAvatar() != null) {
            holder.userLogo.setImageURI(Uri.parse(user.getAvatar().getFileUrl(mContext)));
        }
        holder.userName.setText(user.getUsername());
        holder.contentText.setText(content.getContent());
        if (content.getContentfigureurl() != null) {
            String s = content.getContentfigureurl().getFileUrl(mContext);
            if (TextUtils.isEmpty(s)) {
                holder.contentImage.setVisibility(View.GONE);
                holder.wrapper.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                holder.bottom.setBackgroundColor(mContext.getResources().getColor(R.color.glb_green));
            } else if (!TextUtils.isEmpty(s)) {
                holder.contentImage.setVisibility(View.VISIBLE);
                holder.contentImage.setImageURI(Uri.parse(s));
                holder.wrapper.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Tools.dp2px(mContext, 240)));
                holder.bottom.setBackgroundColor(Color.parseColor("#30ffffff"));
            }
        }

        if (content.getContent().length() >= 60) {
            holder.wrapper.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Tools.dp2px(mContext, 240)));
        }

        holder.setIsRecyclable(false);
        holder.itemActionComment.setText("" + content.getComment());
        holder.itemActionShare.setText("" + content.getShare());
        holder.itemActionHate.setText("" + content.getHate());
        holder.itemActionLove.setText("" + content.getLove());
        holder.parentView.setTag(content.getObjectId());
//        holder.parentView.setTag(position);
    }

    protected Animator[] getAnimators(View view) {
        if (view.getMeasuredHeight() <= 0) {
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.05f, 1.0f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.05f, 1.0f);
            return new ObjectAnimator[]{scaleX, scaleY};
        }
        return new Animator[]{
                ObjectAnimator.ofFloat(view, "scaleX", 1.05f, 1.0f),
                ObjectAnimator.ofFloat(view, "scaleY", 1.05f, 1.0f),
                //ObjectAnimator.ofFloat(view, "translationY", upDownFactor * 1.05f * view.getMeasuredHeight(), 0)
        };
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.user_logo)
        SimpleDraweeView userLogo;
        @Bind(R.id.user_name)
        TextView         userName;
        @Bind(R.id.content_text)
        TextView         contentText;
        @Bind(R.id.content_image)
        SimpleDraweeView contentImage;
        @Bind(R.id.item_action_comment)
        TextView         itemActionComment;
        @Bind(R.id.item_action_share)
        TextView         itemActionShare;
        @Bind(R.id.item_action_hate)
        TextView         itemActionHate;
        @Bind(R.id.item_action_love)
        TextView         itemActionLove;
        @Bind(R.id.wrapper)
        RelativeLayout   wrapper;
        @Bind(R.id.bottom)
        RelativeLayout   bottom;
        View parentView;

        MyViewHolder(View view) {
            super(view);
            parentView = view;
            ButterKnife.bind(this, view);
        }
    }

    public int getItemPosById(String id) {
        if (mItemList == null) {
            return -1;
        }
        for (int i = 0; i < mItemList.size(); i++) {
            if (TextUtils.equals(mItemList.get(i).getObjectId(), id)) {
                return i;
            }
        }
        return -1;
    }


}
