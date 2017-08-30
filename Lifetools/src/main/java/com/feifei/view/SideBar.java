package com.feifei.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.feifei.lifetools.R;
import com.feifei.util.DeviceUtil;

/**
 * @author   裴智飞
 * @date       2014-7-15
 * @date       上午9:05:23
 * @file         SideBar.java
 * @content  右侧的字母索引表
 */
public class SideBar extends View {
	private final Context context;
	private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
	public static String[] b = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
			"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
			"W", "X", "Y", "Z", "#" };
	private int choose = -1;// 选中
	private final Paint paint = new Paint();

	private TextView mTextDialog;

	public void setTextView(TextView mTextDialog) {
		this.mTextDialog = mTextDialog;
	}

	public SideBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	public SideBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public SideBar(Context context) {
		super(context);
		this.context = context;
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int height = getHeight();
		int width = getWidth();
		// 获取每一个字母的高度
		int singleHeight = height / b.length;
		for (int i = 0; i < b.length; i++) {
			// 深黑色字体颜色
			paint.setColor(Color.parseColor("#878c92"));
			// paint.setTypeface(Typeface.DEFAULT_BOLD);
			paint.setAntiAlias(true);
			int wid = DeviceUtil.getWidth(context);
			if (wid >= 720 && wid < 1080 && !DeviceUtil.isPad(context)) {
				paint.setTextSize(30);
			} else {
				if (wid >= 1080 && !DeviceUtil.isPad(context)) {
					paint.setTextSize(45);
				} else {
					paint.setTextSize(20);
				}
			}

			// 选中的状态
			if (i == choose) {
				// 蓝色
				paint.setColor(Color.parseColor("#0b6aff"));
				// paint.setFakeBoldText(true);
			}
			// x坐标等于中间-字符串宽度的一半.
			float xPos = width / 2 - paint.measureText(b[i]) / 2;
			float yPos = singleHeight * i + singleHeight;
			canvas.drawText(b[i], xPos, yPos, paint);
			paint.reset();// 重置画笔
		}

	}

	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		// 点击y坐标
		final float y = event.getY();
		final int oldChoose = choose;
		final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
		// 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数
		final int c = (int) (y / getHeight() * b.length);
		switch (action) {
		case MotionEvent.ACTION_UP:
			try {
				setBackground(new ColorDrawable(0x00000000));
			} catch (Error e) {
			}
			choose = -1;
			invalidate();
			if (mTextDialog != null) {
				mTextDialog.setVisibility(View.INVISIBLE);
			}
			break;

		default:
			setBackgroundResource(R.drawable.sidebar_round);
			if (oldChoose != c) {
				if (c >= 0 && c < b.length) {
					if (listener != null) {
						listener.onTouchingLetterChanged(b[c]);
					}
					if (mTextDialog != null) {
						mTextDialog.setText(b[c]);
						mTextDialog.setVisibility(View.VISIBLE);
					}

					choose = c;
					invalidate();
				}
			}

			break;
		}
		return true;
	}

	public void setOnTouchingLetterChangedListener(
			OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
		this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}

	public interface OnTouchingLetterChangedListener {
		public void onTouchingLetterChanged(String s);
	}

}