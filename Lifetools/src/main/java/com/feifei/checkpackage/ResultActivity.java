package com.feifei.checkpackage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.feifei.lifetools.BaseActivity;
import com.feifei.lifetools.R;
import com.feifei.util.ConfigUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ResultActivity extends BaseActivity {

	private final HashMap<String, String> detailMap = new HashMap<String, String>();
	private ListView resultListView;
	private MyAdaper myAdaper;
	private final HashMap<String, String> infoMap = new HashMap<String, String>();
	private ArrayList<String> timeList;
	private ArrayList<String> infoList;
	private TextView companyTextView, orderTextView;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.package_detail);
		initActionbar();
		setTitle("我的快递");
		setGone(actionbar_right_layout);
		setLogoImage(R.drawable.circle_package);
		companyTextView = (TextView) findViewById(R.id.company);
		orderTextView = (TextView) findViewById(R.id.order);
		companyTextView.setText(ConfigUtil.readString(activity, "company", ""));
		orderTextView.setText("运单号:"
				+ ConfigUtil.readString(activity, "order", ""));
		Intent intent = getIntent();
		timeList = intent.getStringArrayListExtra("timeList");
		infoList = intent.getStringArrayListExtra("infoList");
		Bundle bundle = intent.getExtras();
		infoMap.put("companyName", bundle.getString("companyName"));
		infoMap.put("companyId", bundle.getString("companyId"));
		infoMap.put("order_num", bundle.getString("order_num"));
		resultListView = (ListView) findViewById(R.id.resultlistview);
		myAdaper = new MyAdaper(infoList, timeList, this);
		resultListView.setAdapter(myAdaper);
	}

	class Holder {
		TextView timePoint;
		TextView timeText;
		TextView infoText;
		ImageView imageView;
		LinearLayout linearLayout;
	}

	public class MyAdaper extends BaseAdapter {
		LayoutInflater layoutInflater = null;
		List<String> timeList = null;
		List<String> infoList = null;
		Holder holder = null;
		Context mContext;
		String[] strArr = null;

		public MyAdaper(List<String> infoList, List<String> timeList,
				Context context) {

			mContext = context;
			layoutInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.timeList = new ArrayList<String>();
			this.infoList = new ArrayList<String>();
			this.timeList = timeList;
			this.infoList = infoList;
			this.strArr = new String[2];

		}

		public int getCount() {
			return infoList.size();
		}

		public Object getItem(int position) {
			strArr[0] = timeList.get(position);
			strArr[1] = infoList.get(position);
			return strArr;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = layoutInflater.inflate(
						R.layout.package_detail_item, null);
				holder = new Holder();
				holder.timeText = (TextView) convertView
						.findViewById(R.id.timetxt);
				holder.infoText = (TextView) convertView
						.findViewById(R.id.infotxt);
				holder.timePoint = (TextView) convertView
						.findViewById(R.id.time);
				holder.imageView = (ImageView) convertView
						.findViewById(R.id.image);
				holder.linearLayout = (LinearLayout) convertView
						.findViewById(R.id.package_bg);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			switch (position % 2) {
			case 0:
				holder.linearLayout
						.setBackgroundResource(R.drawable.listitem_selector);
				break;
			case 1:
				holder.linearLayout
						.setBackgroundResource(R.drawable.listitem_selector_change);
				break;
			}
			holder.timePoint
					.setTextColor(getResources().getColor(R.color.blue));
			holder.timeText.setTextColor(getResources().getColor(R.color.blue));
			holder.infoText
					.setTextColor(getResources().getColor(R.color.black));
			holder.imageView.setImageDrawable(getResources().getDrawable(
					R.drawable.package_ditail_middle));
			String[] str = (String[]) getItem(position);
			if ((position == 0) && str[1].contains("签收")) {
				// 如果是最后一个并且包含已经签收三个字就显示绿色
				holder.timePoint.setTextColor(getResources().getColor(
						R.color.green));
				holder.timeText.setTextColor(getResources().getColor(
						R.color.green));
				holder.infoText.setTextColor(getResources().getColor(
						R.color.green));
				holder.imageView.setImageDrawable(getResources().getDrawable(
						R.drawable.package_ditail_last));
			}
			if (position == timeList.size() - 1) {
				holder.imageView.setImageDrawable(getResources().getDrawable(
						R.drawable.package_ditail_first));
			}
			holder.timeText.setText(timeFormat(str[0].substring(0, 10)));
			holder.timePoint.setText(str[0].substring(11, 16));
			holder.infoText.setText(contentFormat(str[1]));
			return convertView;
		}

	}

	/**
	 * 转换数据
	 * 
	 * @param resultList
	 */
	public void changeData(ArrayList<String> resultList) {
		int size = resultList.size();
		int n = 0;
		while (n < size) {
			if (n == 0) {
				detailMap.put("aboutResult", resultList.get(n));
				continue;
			} else if (n == 1) {
				detailMap.put("title", resultList.get(n));
				continue;
			}
			String key = resultList.get(n).substring(0, 19);// 2013-07-01
			String value = resultList.get(n).substring(20);
			detailMap.put(key, value);
		}
	}

	public static String timeFormat(String string) {
		return string.replace('-', '.').trim();
	}

	public static String contentFormat(String string) {
		return string.replace("【", "").replace("】", "").replace("|", "--")
				.trim();
	}

}
