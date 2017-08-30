package com.feifei.checkpackage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.feifei.lifetools.R;

import java.util.List;

public class SortAdapter extends BaseAdapter implements SectionIndexer {
	private List<SortModel> list = null;
	private final Context mContext;
	int section;

	public SortAdapter(Context mContext, List<SortModel> list) {
		this.mContext = mContext;
		this.list = list;
	}

	// 当ListView数据发生变化时,调用此方法来更新ListView
	public void updateListView(List<SortModel> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public int getCount() {
		return this.list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final SortModel mContent = list.get(position);
		// 这就是缓存视图，为null时才重新加载
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(
					R.layout.package_select_item, null);
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
			viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
			viewHolder.photo = (ImageView) view.findViewById(R.id.imagePhoto);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		// 根据position获取分类的首字母的Char ascii值
		section = getSectionForPosition(position);
		// 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if (position == getPositionForSection(section)) {
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(mContent.getSortLetters());
		} else {
			viewHolder.tvLetter.setVisibility(View.GONE);
		}
		viewHolder.tvTitle.setText(list.get(position).getName());
		viewHolder.photo.setBackgroundResource(getImage(list.get(position)
				.getName()));
		return view;
	}

	final static class ViewHolder {
		TextView tvLetter;
		TextView tvTitle;
		TextView tvPhone;
		ImageView photo;
	}

	// 根据ListView的当前位置获取分类的首字母的Char ascii值
	public int getSectionForPosition(int position) {
		return list.get(position).getSortLetters().charAt(0);
	}

	// 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		return -1;
	}

	// 提取英文的首字母，非英文字母用#代替。
	@SuppressWarnings("unused")
	private String getAlpha(String str) {
		String sortStr = str.trim().substring(0, 1).toUpperCase();
		// 正则表达式，判断首字母是否是英文字母
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	public Object[] getSections() {
		return null;
	}

	public static int getImage(String companyName) {
		if ("EMS".equals(companyName)) {
			return R.drawable.ems;
		} else if ("德邦物流".equals(companyName)) {
			return R.drawable.debangwuliu;
		} else if ("圆通速递".equals(companyName)) {
			return R.drawable.yuantong;
		} else if ("中通速递".equals(companyName)) {
			return R.drawable.zhongtong;
		} else if ("汇通快运".equals(companyName)) {
			return R.drawable.huitongkuaidi;
		} else if ("顺丰速运".equals(companyName)) {
			return R.drawable.shunfeng;
		} else if ("宅急送".equals(companyName)) {
			return R.drawable.zhaijisong;
		} else if ("中铁快运".equals(companyName)) {
			return R.drawable.zhongtiewuliu;
		} else if ("韵达快递".equals(companyName)) {
			return R.drawable.yunda;
		} else if ("申通快递".equals(companyName)) {
			return R.drawable.shentong;
		} else if ("佳怡物流".equals(companyName)) {
			return R.drawable.jiayiwuliu;
		} else if ("京广速递".equals(companyName)) {
			return R.drawable.jinguangsudikuaijian;
		} else if ("快捷速递".equals(companyName)) {
			return R.drawable.kuaijiesudi;
		} else if ("天地华宇".equals(companyName)) {
			return R.drawable.tiandihuayu;
		} else if ("龙邦速递".equals(companyName)) {
			return R.drawable.longbanwuliu;
		} else if ("UPS".equals(companyName)) {
			return R.drawable.ups;
		} else if ("天天快递".equals(companyName)) {
			return R.drawable.tiantian;
		} else if ("新邦物流".equals(companyName)) {
			return R.drawable.xinbangwuliu;
		} else if ("信丰物流".equals(companyName)) {
			return R.drawable.xinfengwuliu;
		} else if ("优速物流".equals(companyName)) {
			return R.drawable.youshuwuliu;
		} else if ("联邦快递".equals(companyName)) {
			return R.drawable.lianbangkuaidi;
		} else if ("FedEx-国际件".equals(companyName)) {
			return R.drawable.fedex;
		} else if ("运通中港".equals(companyName)) {
			return R.drawable.yuntongkuaidi;
		} else {
			return R.drawable.circle_package;
		}

	}
}