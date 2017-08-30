package com.feifei.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class MyViewHolder {
    //key为int的map，效率高，存放view
    public SparseArray<View> viewSparseArray;
    int position;

    public View getmContentView() {
        return mContentView;
    }

    public int getPosition() {
        return position;
    }

    public Context context;
    public View mContentView;

    //单例模式需要私有化构造否则getViewHolder没有任何意义
    private MyViewHolder(Context context, int layoutId, View convertView, ViewGroup parent, int position) {
        this.context = context;
        this.position = position;
        viewSparseArray = new SparseArray<>();
        mContentView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        mContentView.setTag(this);

    }

    public static MyViewHolder getViewHolder(Context context, int layoutId, View convertView, ViewGroup parent, int position) {
        if (convertView == null) {
            return new MyViewHolder(context, layoutId, convertView, parent, position);
        } else {
            return (MyViewHolder) convertView.getTag();
        }
    }

    public <T extends View> T getView(int viewId) {
        View view = viewSparseArray.get(viewId);
        if (view == null) {
            view = mContentView.findViewById(viewId);
            viewSparseArray.put(viewId, view);
        }
        return (T) view;

    }


    public void setBackground(int viewId, int resId) {
        View view = getView(viewId);
        view.setBackgroundResource(resId);
    }

    public void setText(int viewID, String text) {
        TextView textView = getView(viewID);
        textView.setText(text);
    }


    public void setImageResource(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);
    }

    public void setImageBitmap(int viewId, Bitmap bm) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bm);
    }

    /**
     * 设置图片来自url，可以使用volley里面的ImageLoader
     */
    public void setImageByUrl(int viewId, String url) {
        //        ImageLoader.getInstance(3, Type.LIFO).loadImage(url,
        //                (ImageView) getView(viewId));
    }

    public void setChecked(int viewId, boolean check) {
        CheckBox view = getView(viewId);
        view.setChecked(check);
    }


}
