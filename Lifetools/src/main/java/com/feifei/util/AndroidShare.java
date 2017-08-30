/**
 *
 */
package com.feifei.util;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feifei.lifetools.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AndroidShare extends Dialog implements AdapterView.OnItemClickListener {
    LinearLayout layout;
    GridView gv;
    float density;
    String msgText = "我都懒得打分享的文字了";
    String imgPath;
    List<ShareItem> listData;
    Handler mHandler = new Handler();

    Runnable work = new Runnable() {
        @Override
        public void run() {
            if (!DeviceUtil.isLandscape(getContext())) {
                gv.setNumColumns(4);
            } else {
                gv.setNumColumns(6);
            }
            ((AndroidShare.MyAdapter) gv.getAdapter()).notifyDataSetChanged();
            mHandler.postDelayed(this, 1000L);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getContext();
        init(context);
        setContentView(layout);
        getWindow().setGravity(80);
        if (!DeviceUtil.isLandscape(getContext())) {
            gv.setNumColumns(4);
        } else {
            gv.setNumColumns(6);
        }
        gv.setAdapter(new MyAdapter());
        gv.setOnItemClickListener(this);
        mHandler.postDelayed(work, 1000L);
        setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mHandler.removeCallbacks(work);
            }
        });
    }

    public AndroidShare(Context context, String msgText, final String imgUri) {
        super(context, R.style.dialog_bottom_style);
        this.msgText = msgText;
        if (imgUri != null) {
            if (Patterns.WEB_URL.matcher(imgUri).matches()) {
                new Thread(new Runnable() {
                    private final String imgUri2 = imgUri;

                    @Override
                    public void run() {
                        try {
                            imgPath = getImagePath(imgUri2, getFileCache());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            } else {
                imgPath = imgUri;
            }
        }
    }

    private void init(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        density = dm.density;
        listData = new ArrayList<ShareItem>();
        listData.add(new ShareItem("微信", R.drawable.logo_wechat, "com.tencent.mm.ui.tools.ShareImgUI", "com.tencent.mm"));
//		listData.add(new ShareItem("朋友圈", R.drawable.logo_wechatmoments, "com.tencent.mm.ui.tools.ShareToTimeLineUI",
//				"com.tencent.mm"));
        listData.add(new ShareItem("qq", R.drawable.logo_qq, "com.tencent.mobileqq.activity.JumpActivity",
                "com.tencent.mobileqq"));
        listData.add(new ShareItem("新浪微博", R.drawable.logo_sinaweibo, "com.sina.weibo.EditActivity", "com.sina.weibo"));

        layout = new LinearLayout(context);
        layout.setOrientation(1);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
        params.leftMargin = (int) (10.0F * density);
        params.rightMargin = (int) (10.0F * density);
        layout.setLayoutParams(params);
        layout.setBackgroundColor(Color.parseColor("#D9DEDF"));

        gv = new GridView(context);
        gv.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
        gv.setGravity(17);
        gv.setHorizontalSpacing((int) (10.0F * density));
        gv.setVerticalSpacing((int) (10.0F * density));
        gv.setStretchMode(1);
        gv.setColumnWidth((int) (90.0F * density));
        gv.setHorizontalScrollBarEnabled(false);
        gv.setVerticalScrollBarEnabled(false);
        layout.addView(gv);
    }

    public List<ComponentName> queryPackage() {
        List<ComponentName> cns = new ArrayList<ComponentName>();
        Intent i = new Intent("android.intent.action.SEND");
        i.setType("image/*");
        List<ResolveInfo> resolveInfo = getContext().getPackageManager().queryIntentActivities(i, 0);
        for (ResolveInfo info : resolveInfo) {
            ActivityInfo ac = info.activityInfo;
            ComponentName cn = new ComponentName(ac.packageName, ac.name);
            cns.add(cn);
        }
        return cns;
    }

    public boolean isAvilible(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        List<?> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); ++i) {
            if (((PackageInfo) pinfo.get(i)).packageName.equalsIgnoreCase(packageName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ShareItem share = listData.get(position);
        shareMsg(getContext(), "分享到...", msgText, imgPath, share);
        dismiss();
    }

    private void shareMsg(Context context, String msgTitle, String msgText, String imgPath, ShareItem share) {
        if (!isAvilible(getContext(), share.packageName)) {
            ToastUtil.toast(getContext(), "请先安装" + share.title);
            return;
        }
        Intent intent = new Intent("android.intent.action.SEND");
        if ((imgPath == null) || (imgPath.equals(""))) {
            intent.setType("text/plain");
        } else {
            File f = new File(imgPath);
            if ((f != null) && (f.exists()) && (f.isFile())) {
                intent.setType("image/png");
                Uri u = Uri.fromFile(f);
                intent.putExtra("android.intent.extra.STREAM", u);
            }
        }
        intent.putExtra("android.intent.extra.SUBJECT", msgTitle);
        intent.putExtra("android.intent.extra.TEXT", msgText);
        intent.setFlags(268435456);
        intent.setComponent(new ComponentName(share.packageName, share.activityName));
        try {

            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            ToastUtil.toast(context, "没有安装相关软件");
        }
    }

    private File getFileCache() {
        File cache = null;
        if (Environment.getExternalStorageState().equals("mounted")) {
            cache = new File(Environment.getExternalStorageDirectory() + "/." + getContext().getPackageName());
        } else {
            cache = new File(getContext().getCacheDir().getAbsolutePath() + "/." + getContext().getPackageName());
        }
        if ((cache != null) && (!cache.exists())) {
            cache.mkdirs();
        }
        return cache;
    }

    public String getImagePath(String imageUrl, File cache) throws Exception {
        String name = imageUrl.hashCode() + imageUrl.substring(imageUrl.lastIndexOf("."));
        File file = new File(cache, name);
        if (file.exists()) {
            return file.getAbsolutePath();
        }
        URL url = new URL(imageUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        if (conn.getResponseCode() == 200) {
            InputStream is = conn.getInputStream();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            is.close();
            fos.close();

            return file.getAbsolutePath();
        }

        return null;
    }

    class MyAdapter extends BaseAdapter {
        static final int image_id = 256;
        static final int tv_id = 512;

        MyAdapter() {
        }

        @Override
        public int getCount() {
            return listData.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0L;
        }

        View getItemView() {
            LinearLayout item = new LinearLayout(getContext());
            item.setOrientation(1);
            int padding = (int) (10.0F * density);
            item.setPadding(padding, padding, padding, padding);
            item.setGravity(17);
            ImageView iv = new ImageView(getContext());
            item.addView(iv);
            iv.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
            iv.setId(256);
            TextView tv = new TextView(getContext());
            item.addView(tv);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
            layoutParams.topMargin = (int) (5.0F * density);
            tv.setLayoutParams(layoutParams);
            tv.setTextColor(Color.parseColor("#212121"));
            tv.setTextSize(14.0F);
            tv.setId(512);
            return item;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getItemView();
            }
            ImageView iv = (ImageView) convertView.findViewById(256);
            TextView tv = (TextView) convertView.findViewById(512);
            AndroidShare.ShareItem item = listData.get(position);
            iv.setImageResource(item.logo);
            tv.setText(item.title);
            return convertView;
        }
    }

    private class ShareItem {
        String title;
        int logo;
        String activityName;
        String packageName;

        public ShareItem(String title, int logo, String activityName, String packageName) {
            this.title = title;
            this.logo = logo;
            this.activityName = activityName;
            this.packageName = packageName;
        }
    }
}