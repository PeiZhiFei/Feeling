package com.feifei.checkpackage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feifei.lifetools.R;
import com.feifei.lifetools.BaseActivity;
import com.feifei.util.AnimUtil;
import com.feifei.util.ConfigUtil;
import com.feifei.util.DeviceUtil;
import com.feifei.util.DialogUtil;
import com.feifei.util.LogUtil;
import com.feifei.util.MatchUtil;
import com.feifei.util.NetworkUtil;
import com.feifei.util.ToastCustom;
import com.feifei.util.ToastUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PackageActivity extends BaseActivity implements TextWatcher {
    boolean yuyin = true;
    private LinearLayout selectCompanyLayout;
    private Button searchBtn;
    private EditText order_numTxt;
    private TextView companynameTxt, hiddenText;
    private ImageView voice_search;
    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            try {
                DatabaseUtil.loadDatabase(activity);
            } catch (Exception e) {
            }
            String hasLoadDB = ConfigUtil.readString(activity, DatabaseUtil.HASLOAD_DATABASE, "0");
            DatabaseUtil.dbh = new DatabaseHelper(activity, "mypackage");
            if (hasLoadDB.equals("0")) {
                DatabaseUtil.readData2Db(activity);
                ConfigUtil.writeString(activity, DatabaseUtil.HASLOAD_DATABASE, "1");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.package_activity);
        new Thread(task).start();
        actionbarStyle0("查快递", R.drawable.circle_package);
        initView();
        DeviceUtil.keyboardClose(activity);
        actionbarFirstAnim(packageFirst, "packageFirst", "语音输入会自动清除前一次订单哦");
    }

    public void initView() {
        companynameTxt = (TextView) findViewById(R.id.companynametxt);
        companynameTxt.setText(ConfigUtil.readString(activity, "company", ""));
        selectCompanyLayout = (LinearLayout) findViewById(R.id.companyname_layout);
        searchBtn = (Button) findViewById(R.id.searchbtn);
        order_numTxt = (EditText) findViewById(R.id.order_numtxt);
        order_numTxt.setText(ConfigUtil.readString(activity, "order", ""));
        hiddenText = (TextView) findViewById(R.id.hidden_text);
        voice_search = (ImageView) findViewById(R.id.voice_search);
        voice_search.setOnClickListener(this);
        selectCompanyLayout.setOnClickListener(this);
        searchBtn.setOnClickListener(this);
        order_numTxt.addTextChangedListener(this);
        if (!NetworkUtil.isNetworkAvailable(activity)) {
            ToastUtil.toast(activity, "非联网状态不能查询订单信息哦");
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.searchbtn:
                if (TextUtils.isEmpty(companynameTxt.getText())) {
                    AnimUtil.animShakeText(companynameTxt);
                    ToastCustom.toast(activity, "您还未选择快递公司");
                    return;
                } else if (TextUtils.isEmpty(order_numTxt.getText())) {
                    AnimUtil.animShakeText(order_numTxt);
                    ToastCustom.toast(activity, "您还未输入订单号");
                    return;
                }
                String order_num = order_numTxt.getText().toString().trim();
                ConfigUtil.writeString(activity, "order", order_num);
                if (!MatchUtil.checkPackage(order_num)) {
                    ToastCustom.toast(activity, "订单号格式错误");
                    return;
                }
                infoMap.put("companyId", ConfigUtil.readString(activity, "id", ""));
                infoMap.put("order_num", order_num);
                new MyThread(infoMap).start();
                break;

            case R.id.companyname_layout:
                Intent intent = new Intent();
                intent.setClass(activity, SelectActivity.class);
                startActivityForResult(intent, 6);
                AnimUtil.animTo(activity);
                break;
            case R.id.voice_search:
                break;
        }

    }

    protected void getResult(String result) {
        order_numTxt.setText(result);
        order_numTxt.setSelection(order_numTxt.length());
    }


    HashMap<String, String> infoMap = new HashMap<String, String>();
    private static final int WAITTING = 0;
    private static final int FINISHED = 1;
    private static final int ERROR = 2;
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WAITTING:
                    DialogUtil.dialog(activity);
                    break;
                case ERROR:
                    DialogUtil.dialogDismiss();
                    break;
                case FINISHED:
                    DialogUtil.dialogDismiss();
                    DeviceUtil.keyboardClose(activity);
                    break;
                case 10:
                    DialogUtil.dialogDismiss();
                    ToastCustom.toast(activity, "未获取到相关信息，请检查");
                    break;
            }
            super.handleMessage(msg);
        }

    };

    // 子线程联网获取数据
    class MyThread extends Thread {
        private HashMap<String, String> threadMap = null;

        public MyThread(HashMap<String, String> map) {
            threadMap = new HashMap<String, String>();
            this.threadMap = map;

        }

        @Override
        public void run() {
            int rand = (int) Math.random() * 1000 + 1000;
            String strurl = "http://wap.kuaidi100.com/q.jsp?rand=" + rand + "&id=" + threadMap.get("companyId")
                    + "&postid=" + threadMap.get("order_num") + "&fromWeb=null";
            Message msg = new Message();
            msg.what = WAITTING;
            mHandler.sendMessage(msg);// http://wap.kuaidi100.com/q.jsp?rand=1000&id=aae&postid=987654321&fromWeb=null
            String result = getData(strurl);// http://m.kuaidi100.com/index_all.html?type=yuantong&postid=7409440775
            if (result.equals("")) {
                msg = new Message();
                msg.what = 10;
                mHandler.sendMessage(msg);
                return;
            }
            Document doc = Jsoup.parse(result);// 7409440775
            Elements element = doc.body().getElementsByTag("p");
            Object[] objArr = element.toArray();
            Pattern p = Pattern.compile("<.+?>|\\&gt;|\\&middot;", Pattern.DOTALL);
            String tempStr = null;
            ArrayList<String> timeList = new ArrayList<String>();// 存放快递的时间
            ArrayList<String> infoList = new ArrayList<String>();// 存放快递的对应时间点的信息
            for (int i = 3; i < objArr.length - 2; i++) {
                Matcher m = p.matcher(objArr[i].toString());
                tempStr = m.replaceAll("");
                if (TextUtils.isEmpty(tempStr)) {
                    continue;
                } else if (tempStr.startsWith("建议操作")) {
                    msg = new Message();
                    msg.what = 10;
                    mHandler.sendMessage(msg);
                    return;
                }
                // 2013-11-17 00:25:57 福建省泉州市石狮市公司 已收件 操作员：包叶明
                timeList.add(new String(tempStr.substring(0, 19)));
                infoList.add(new String(tempStr.substring(20)));
            }

            LogUtil.log(timeList.toString());
            LogUtil.log(infoList.toString());
            msg = new Message();
            msg.what = FINISHED;
            mHandler.sendMessage(msg);
            if (timeList.isEmpty() || infoList.isEmpty()) {
                msg = new Message();
                msg.what = 10;
                mHandler.sendMessage(msg);
                return;
            }
            Collections.reverse(timeList);
            Collections.reverse(infoList);
            Intent intent = new Intent();
            intent.setClass(activity, ResultActivity.class);
            intent.putStringArrayListExtra("timeList", timeList);
            intent.putStringArrayListExtra("infoList", infoList);
            Bundle bundle = new Bundle();
            bundle.putString("companyName", infoMap.get("companyName"));// 公司名称
            bundle.putString("companyId", infoMap.get("companyId"));
            bundle.putString("order_num", infoMap.get("order_num"));// 运单号
            intent.putExtras(bundle);
            startActivity(intent);
            AnimUtil.animTo(activity);
        }
    }

    public String getData(String strurl) {
        String output = "";
        URL url;
        try {
            url = new URL(strurl);
            InputStream is = url.openStream();
            BufferedReader buffer = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String tem = "";
            while ((tem = buffer.readLine()) != null) {
                output += tem;
            }
            return output;

        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        return "";
    }

    // 捕捉点击事件，点击空白处 软键盘消失
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        DeviceUtil.keyboardClose(activity);
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 8 && requestCode == 6) {
            Bundle bundle = data.getExtras();
            if (bundle == null) {
                ToastUtil.toast(activity, "获取数据失败");
                return;
            }
            infoMap = new HashMap<String, String>();
            String name = bundle.getString("companyName");
            String str = bundle.getString("companyId");
            String id = TextUtils.isEmpty(str) ? "" : str;
            infoMap.put("companyName", name);
            infoMap.put("companyId", id);
            ConfigUtil.writeString(activity, "company", name);
            ConfigUtil.writeString(activity, "id", id);
            companynameTxt.setText(TextUtils.isEmpty(name) ? "" : name);
            List<String> resultList = CompanyInfo.getCompanyName(DatabaseUtil.dbh, id);
            if (!resultList.get(0).equals("")) {
                ToastCustom.toast(activity, "温馨提示" + resultList.get(0));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        if (arg0.toString().length() > 0) {
            char[] chars = arg0.toString().toCharArray();
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < chars.length; i++) {
                if (i % 4 == 2) {
                    buffer.append(chars[i] + "  ");
                } else {
                    buffer.append(chars[i]);
                }
            }
            hiddenText.setText(buffer.toString());
        } else {
            hiddenText.setText("");
        }
    }

    @Override
    public void afterTextChanged(Editable arg0) {
    }

    @Override
    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
    }

}
