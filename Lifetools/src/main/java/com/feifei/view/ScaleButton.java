/**
 * 
 */
package com.feifei.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.feifei.util.AnimUtil;

/**
 * @author   裴智飞
 * @date       2014-7-25
 * @date       上午9:50:38
 * @file         ScaleButton.java
 * @content  仿去哪网的button，别忘了拷贝字体，需要设置宽高，不能在XML里面写监听，
 * 还没有加入指纹和抬起手指
 */
public class ScaleButton extends TextView {

	private ScaleButtonListener listener = null;

	public ScaleButton(Context context) {
		super(context);
//		init(context);
	}

	public ScaleButton(Context context, AttributeSet attrs) {
		super(context, attrs);
//		init(context);
	}

	public ScaleButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
//		init(context);
	}

//	// 设置字体为Text-Roboto.ttf
//	private void init(Context context) {
//		Typeface typeface = Typeface.createFromAsset(context.getAssets(),
//				"yuntu-font/Text-Roboto.ttf");
//		setTypeface(typeface);
//	}

	public boolean onTouchEvent(MotionEvent event) {
		Animation scaleAnimation = AnimUtil.getScaleAnimation(1, 0.7f, 100);
		Animation endAnimation = AnimUtil.getScaleAnimation(0.7f, 1, 100);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startAnimation(scaleAnimation);
			break;
		case MotionEvent.ACTION_UP:
			startAnimation(endAnimation);
			if (listener != null) {
				listener.onClick(this);
			}
			break;
		// 滑动出去不会调用action_up,调用action_cancel
		case MotionEvent.ACTION_CANCEL:
			startAnimation(endAnimation);
			break;
		// case MotionEvent.ACTION_MOVE:
		// startAnimation(endAnimation);
		// break;
		}
		return true;
	}

	/**
	 * 加入响应事件
	 * @param clickListener
	 */
	public void setScaleButtonListener(ScaleButtonListener clickListener) {
		this.listener = clickListener;
	}

	public interface ScaleButtonListener {

		public void onClick(View view);
	}

}
