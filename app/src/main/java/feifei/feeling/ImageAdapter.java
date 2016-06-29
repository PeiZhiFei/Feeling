package feifei.feeling;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ItemViewHolder> {

    private List<ImageBean> mData;
    private Context         mContext;
    private int             mMaxWidth;
    private int             mMaxHeight;

    private OnItemClickListener mOnItemClickListener;

    public ImageAdapter(Context context) {
        this.mContext = context;
        mMaxWidth = ToolsUtil.getWidthInPx(mContext) - 20;
        mMaxHeight = ToolsUtil.getHeightInPx(mContext) - ToolsUtil.getStatusHeight(mContext) -
                ToolsUtil.dip2px(mContext, 96);
    }

    public void setmDate(List<ImageBean> data) {
        this.mData = data;
        this.notifyDataSetChanged();
    }

    @Override
    public ImageAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image, parent, false);
        ItemViewHolder vh = new ItemViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ImageAdapter.ItemViewHolder holder, int position) {
        ImageBean imageBean = mData.get(position);
        if (imageBean == null) {
            return;
        }
        holder.mTitle.setText(imageBean.getTitle());
//        float scale = (float) imageBean.getWidth() / (float) mMaxWidth;
//        int height = (int) (imageBean.getHeight() / scale);
//        if (height > mMaxHeight) {
//            height = mMaxHeight;
//        }
//        holder.mImage.setLayoutParams(new LinearLayout.LayoutParams(mMaxWidth, height));
        holder.mImage.setImageURI(Uri.parse(imageBean.getThumburl()));
    }

    @Override
    public int getItemCount() {
        if (mData == null) {
            return 0;
        }
        return mData.size();
    }

    public ImageBean getItem(int position) {
        return mData == null ? null : mData.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView         mTitle;
        public SimpleDraweeView mImage;

        public ItemViewHolder(View v) {
            super(v);
            mTitle = (TextView) v.findViewById(R.id.tvTitle);
            mImage = (SimpleDraweeView) v.findViewById(R.id.ivImage);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(view, this.getPosition());
            }
        }
    }

}
