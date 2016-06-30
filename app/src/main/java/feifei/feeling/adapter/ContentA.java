package feifei.feeling.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import feifei.feeling.R;
import feifei.feeling.bean.Content;
import feifei.feeling.bean.User;
import feifei.feeling.widget.LabelView;
import library.base.MyBaseRecyclerAdapter;
import library.util.Tools;


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
//                holder.bottom.setBackgroundColor(mContext.getResources().getColor(R.color.glb_green));
                holder.bottom.setBackgroundColor(Color.parseColor("#99" + Tools.scale6(Math.random() * 111111)));
            } else if (!TextUtils.isEmpty(s)) {
                holder.contentImage.setVisibility(View.VISIBLE);
                holder.wrapper.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Tools.dp2px(mContext, 220)));
                Picasso.with((holder).contentImage.getContext()).load(s).resize(400, 600).centerInside().into((holder).contentImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap bitmap = ((BitmapDrawable) holder.contentImage.getDrawable()).getBitmap();
                        Palette.from(bitmap).generate(palette -> {
                            if (palette != null) {
                                holder.bottom.setBackgroundColor(palette.getMutedColor(holder.itemView.getResources().getColor(R.color.primary)));
                            }
                        });
                    }

                    @Override
                    public void onError() {
                    }

                });
//                holder.contentImage.setImageURI(Uri.parse(s));
//                holder.bottom.setBackgroundColor(Color.parseColor("#30ffffff"));
            }
        }

        if (content.getContent().length() >= 60) {
            holder.wrapper.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Tools.dp2px(mContext, 220)));
        }
        if (position % 3 == 0) {
            holder.label.setVisibility(View.VISIBLE);
        } else {
            holder.label.setVisibility(View.GONE);

        }

        if (content.getMyFav()) {
            holder.item_action_fav.setImageResource(R.drawable.rating_bar_small_checked);
        } else {
            holder.item_action_fav.setImageResource(R.drawable.rating_bar_small_unchecked);
        }

        holder.setIsRecyclable(false);
        holder.itemActionComment.setText("" + content.getComment());
//        holder.itemActionShare.setText("" + content.getShare());
        holder.itemActionHate.setText("" + content.getHate());
        holder.itemActionLove.setText("" + content.getLove());
        holder.parentView.setTag(content.getObjectId());
//        holder.parentView.setTag(position);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.user_logo)
        SimpleDraweeView userLogo;
        @Bind(R.id.user_name)
        TextView         userName;
        @Bind(R.id.content_text)
        TextView         contentText;
        @Bind(R.id.content_image)
        ImageView        contentImage;
        @Bind(R.id.item_action_comment)
        TextView         itemActionComment;
        @Bind(R.id.item_action_fav)
        ImageView        item_action_fav;
        @Bind(R.id.item_action_hate)
        TextView         itemActionHate;
        @Bind(R.id.item_action_love)
        TextView         itemActionLove;
        @Bind(R.id.wrapper)
        RelativeLayout   wrapper;
        @Bind(R.id.bottom)
        RelativeLayout   bottom;
        @Bind(R.id.label)
        LabelView        label;
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
