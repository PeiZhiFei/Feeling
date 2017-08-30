package com.feifei.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

import com.feifei.lifetools.R;

/**
 * @author   裴智飞
 * @date       2014-7-15
 * @date       上午8:54:51
 * @file         SearchText.java
 * @content  一个自定义的View，叉号会删除内容
 */
public class SearchText extends EditText implements OnFocusChangeListener,
		TextWatcher {
	private Drawable mClearDrawable;

	public SearchText(Context context) {
		super(context);
		init(context);
	}

	public SearchText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public SearchText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		mClearDrawable = getCompoundDrawables()[2];
		if (mClearDrawable == null) {
			mClearDrawable = getResources().getDrawable(
					R.drawable.search_delete);
		}
		mClearDrawable.setBounds(0, 0, mClearDrawable.getMinimumWidth(),
				mClearDrawable.getMinimumHeight());
		setClearIconVisible(false);
		setOnFocusChangeListener(this);
		addTextChangedListener(this);
	}

	/**
	 *因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件
	 *当我们按下的位置在EditText的宽度 图标到控件右边的间距 - 图标的宽度
	 *和 EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向没有考虑
	 */
	public boolean onTouchEvent(MotionEvent event) {
		if (getCompoundDrawables()[2] != null) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				boolean touchable = event.getX() > (getWidth()
						- getPaddingRight() - mClearDrawable
							.getIntrinsicWidth())
						&& (event.getX() < ((getWidth() - getPaddingRight())));
				if (touchable) {
					this.setText("");
				}
			}
		}

		return super.onTouchEvent(event);
	}

	// 当ClearEditText焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			setClearIconVisible(getText().length() > 0);
		} else {
			setClearIconVisible(false);
		}
	}

	// 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
	protected void setClearIconVisible(boolean visible) {
		Drawable right = visible ? mClearDrawable : null;
		setCompoundDrawables(getCompoundDrawables()[0],
				getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
	}

	// 当输入框里面内容发生变化的时候回调的方法
	public void onTextChanged(CharSequence s, int start, int count, int after) {
		setClearIconVisible(s.length() > 0);
	}

	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	public void afterTextChanged(Editable s) {
	}

}
