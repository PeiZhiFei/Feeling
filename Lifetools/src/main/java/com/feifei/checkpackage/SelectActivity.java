package com.feifei.checkpackage;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.feifei.lifetools.BaseActivity;
import com.feifei.lifetools.R;
import com.feifei.view.SideBar;
import com.feifei.util.AnimUtil;
import com.feifei.util.DeviceUtil;
import com.feifei.util.LogUtil;
import com.feifei.util.CharacterParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SelectActivity extends BaseActivity {
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private SortAdapter adapter;

//	private int mCurrentTransitionEffect = JazzyHelper.GROW;
	int scrol = 0;
	boolean select = true;

	// 汉字转换成拼音的类
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;
	// 根据拼音来排列ListView里面的数据类
	private PinyinComparator pinyinComparator;
	List<CompanyInfo> companyInfoList = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.package_select);
		companyInfoList = new ArrayList<CompanyInfo>();
		actionbarStyle0("请选择", R.drawable.circle_package);
		DeviceUtil.keyboardClose(activity);
		initViews();
	}

	private void initViews() {
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();
		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);
		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
			public void onTouchingLetterChanged(String s) {
				scrol = 1;
				if (s != null) {
					// 该字母首次出现的位置
					int position = adapter.getPositionForSection(s.charAt(0));
					if (position != -1) {
						sortListView.setSelection(position);
					}
				}
			}
		});

		sortListView = (ListView) findViewById(R.id.country_lvcountry);
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 这里要利用adapter.getItem(position)来获取当前position所对应的对象
				// TS.t(SelectActivity.this,
				// ((SortModel) adapter.getItem(position)).getName());
				SortModel model = (SortModel) adapter.getItem(position);
				Bundle bundle = new Bundle();
				bundle.putString("companyName", model.getName());
				bundle.putString("companyId", model.getCompanyId());
				Intent intent = new Intent();
				intent.putExtras(bundle);
				setResult(8, intent);
				AnimUtil.animBackFinish(SelectActivity.this);
			}
		});

//		setupJazziness(mCurrentTransitionEffect);

		mClearEditText.setLayoutParams(new LinearLayout.LayoutParams(
				(int) (DeviceUtil.getWidth(this) * 0.82),
				LayoutParams.MATCH_PARENT));

		// 根据输入框输入值的改变来过滤搜索
		mClearEditText.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				// setGone(actionbar_second_image);
				filterData(s.toString());
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable s) {
			}
		});
		// 变回车键为搜索键
		DeviceUtil.enterKeySearch(mClearEditText, activity);

		companyInfoList = CompanyInfo.getCompanyInfoList(DatabaseUtil.dbh);
		SourceDateList = filledData(companyInfoList);// 数据源

		// 根据a-z进行排序源数据
		Collections.sort(SourceDateList, pinyinComparator);
		adapter = new SortAdapter(SelectActivity.this, SourceDateList);
		sortListView.setAdapter(adapter);

//		sortListView.setOnScrollListener(new OnScrollListener() {
//			public void onScroll(AbsListView view, int arg1, int arg2, int arg3) {
//				if (scrol == 2) {
//					sideBar.setVisibility(View.INVISIBLE);
//				}
//			}
//
//			public void onScrollStateChanged(AbsListView view, int scrollState) {
//				sideBar.setVisibility(View.VISIBLE);
//				scrol = 2;
//			}
//		});

	}

//	private void setupJazziness(int effect) {
//		mCurrentTransitionEffect = effect;
//		sortListView.setTransitionEffect(mCurrentTransitionEffect);
//	}

	// 为ListView填充数据
	private List<SortModel> filledData(List<CompanyInfo> list) {
		List<SortModel> mSortList = new ArrayList<SortModel>();

		int size = list.size();
		for (int i = 0; i < size; i++) {
			SortModel sortModel = new SortModel();
			sortModel.setName(list.get(i).name);
			LogUtil.log(list.get(i).name);
			sortModel.setCompanyId(list.get(i).id);
			// 汉字转换成拼音
			String pinyin = characterParser.getSelling(list.get(i).name);
			String sortString = pinyin.substring(0, 1).toUpperCase();

			// 正则表达式，判断首字母是否是英文字母
			if (sortString.matches("[A-Z]")) {
				sortModel.setSortLetters(sortString.toUpperCase());
			} else {
				sortModel.setSortLetters("#");
			}

			mSortList.add(sortModel);
		}
		return mSortList;

	}

	// 根据输入框中的值来过滤数据并更新ListView
	private void filterData(String filterStr) {
		List<SortModel> filterDateList = new ArrayList<SortModel>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = SourceDateList;
		} else {
			filterDateList.clear();
			for (SortModel sortModel : SourceDateList) {
				String name = sortModel.getName();
				if (name.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(name).startsWith(
								filterStr.toString())) {
					filterDateList.add(sortModel);
				}
			}
		}

		// 根据a-z进行排序
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}

	public void setOnclickCallBack(itemOnclickCallBack onclickCallBack) {
	}

	public interface itemOnclickCallBack {
		void itemOnClick(HashMap<String, String> map);
	}

}
