package feifei.feeling.fold;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.ramotion.foldingcell.FoldingCell;

import java.util.HashSet;

import butterknife.Bind;
import butterknife.ButterKnife;
import feifei.feeling.R;
import feifei.feeling.bean.Content;
import library.base.MyBaseRecyclerAdapter;

public class FoldingCellListAdapter extends MyBaseRecyclerAdapter<Content, FoldingCellListAdapter.MyViewHolder> {

    View.OnClickListener mItemClickListener;

    public FoldingCellListAdapter(Context context, View.OnClickListener onClickListener) {
        super(context);
        mItemClickListener = onClickListener;
    }

    @Override
    public int getItemCount() {
        return getListCount() > 25 ? 25 : getListCount();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.cell, parent, false);
//        AnimUtil.animRightToLeft(view);
        MyViewHolder holder = new MyViewHolder(view);
        if (mItemClickListener != null) {
            holder.parentView.setOnClickListener(mItemClickListener);

        }
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        Content item = getItem(position);
        // if cell is exists - reuse it, if not - create the new one from resource
        FoldingCell cell = (FoldingCell) viewHolder.parentView;
        if (unfoldedIndexes.contains(position)) {
            cell.unfold(true);
        } else {
            cell.fold(true);
        }

        viewHolder.titleAuthor.setText(item.getAuthor().getUsername());
        viewHolder.titleContent.setText(item.getContent());
        viewHolder.titleTitle.setText(item.getTitle());
        viewHolder.contentTitle.setText(String.valueOf(item.getTitle()));
        viewHolder.contentContent.setText(item.getContent());

//        if (item.getContentfigureurl() != null && item.getContentfigureurl().getFileUrl(context) != null) {
//            viewHolder.titleImage.setImageURI(Uri.parse(item.getContentfigureurl().getFileUrl(context)));
//            viewHolder.contentImage.setImageURI(Uri.parse(item.getContentfigureurl().getFileUrl(context)));
//        }
//        if (item.getAuthor().getAvatar() != null && item.getAuthor().getAvatar().getFileUrl(context) != null) {
//            viewHolder.titleUserImage.setImageURI(Uri.parse(item.getAuthor().getAvatar().getFileUrl(context)));
//        }

        viewHolder.setIsRecyclable(false);
        viewHolder.parentView.setTag(item.getObjectId());
//        holder.parentView.setTag(position);
    }

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();

    public Context context;

    // simple methods for register cell state changes
    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.content_image)
        SimpleDraweeView contentImage;
        @Bind(R.id.content_title)
        TextView         contentTitle;
        @Bind(R.id.content_content)
        TextView         contentContent;
        @Bind(R.id.title_user_image)
        SimpleDraweeView titleUserImage;
        @Bind(R.id.title_author)
        TextView         titleAuthor;
        @Bind(R.id.title_image)
        SimpleDraweeView titleImage;
        @Bind(R.id.title_title)
        TextView         titleTitle;
        @Bind(R.id.title_content)
        TextView         titleContent;
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
