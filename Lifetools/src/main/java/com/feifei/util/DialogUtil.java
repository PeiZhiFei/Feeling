package com.feifei.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feifei.lifetools.R;
import com.feifei.util.DialogView.DialogViewListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author   裴智飞
 * @date       2014-7-28
 * @date       下午9:18:58
 * @file         DialogUtil.java
 * @content  各种样式的dialog大集合
 */
public class DialogUtil {

	/**
	 * 第一部分：普通dialog
	 */
	private static Dialog loadingDialog;
	private static boolean open = false;

	/**
	 * @notice 普通dialog，不显示文字
	 */
	public static void dialog(Context context) {
		dialogProgress(context, null);
	}

	/**
	 * @param string
	 * @notice 普通dialog，显示文字
	 */
	public static void dialog(Context context, String string) {
		dialogProgress(context, string);
	}

	/**
	 * @notice 取消普通dialog
	 */
	public synchronized static void dialogDismiss() {
		if (open) {
			loadingDialog.dismiss();
			open = false;
		}
	}

	private synchronized static void dialogProgress(final Context context, final String text) {
		((Activity) context).runOnUiThread(new Runnable() {
			public void run() {
				int dp20 = DeviceUtil.dp2px(context, 20);
				LinearLayout layout = new LinearLayout(context);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
				layout.setOrientation(LinearLayout.VERTICAL);
				layout.setGravity(Gravity.CENTER);
				layout.setLayoutParams(params);

				ImageView imageView = new ImageView(context);
				LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				params2.setMargins(dp20, dp20, dp20, dp20);
				imageView.setLayoutParams(params2);
				imageView.setImageResource(R.drawable.dialog_loading);

				TextView textView = new TextView(context);
				LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
				textView.setTextColor(context.getResources().getColor(R.color.gray));
				textView.setTextSize(18);
				textView.setGravity(Gravity.CENTER);
				textView.setLayoutParams(params3);
				layout.addView(imageView);
				layout.addView(textView);
				if (!open) {
					if (text != null) {
						textView.setText(text);
					}
					imageView.startAnimation(AnimUtil.getDialogRotateAnimation2());
					loadingDialog = new Dialog(context, R.style.dialog_loading_style);
					loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
					loadingDialog.setCancelable(false);
					// loadingDialog.setOnKeyListener(keylistener);
					loadingDialog.show();
					open = true;
				}
			}
		});
	}

	/**
	 * 第二部分：计数dialog，可完成多项操作
	 */
	private static Dialog loadingDialogPro;
	private static int dialogCount = 0;

	/**
	 * @notice 初始化计数dialog
	 */
	public static void dialogInit(Context context) {
		loadingDialogPro = new Dialog(context,  R.style.dialog_loading_style);
	}

	/**
	 * @param string
	 * @notice 显示计数dialog，传null则不显示文字
	 */
	public synchronized static void dialogPro(Context context, String string) {
		dialogCount++;
		dialogProProgress(context, string);
	}

	/**
	 * @notice 取消计数dialog
	 */
	public synchronized static void dialogProDismiss() {
		dialogCount--;
		if (dialogCount == 0) {
			loadingDialogPro.dismiss();
		}
		if (dialogCount < 0) {
			dialogCount = 0;
		}
	}

	private static void dialogProProgress(final Context context, final String text) {
		((Activity) context).runOnUiThread(new Runnable() {
			public void run() {
				int dp20 = DeviceUtil.dp2px(context, 20);
				LinearLayout layout = new LinearLayout(context);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
				layout.setOrientation(LinearLayout.VERTICAL);
				layout.setGravity(Gravity.CENTER);
				layout.setLayoutParams(params);

				ImageView imageView = new ImageView(context);
				LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				params2.setMargins(dp20, dp20, dp20, dp20);
				imageView.setLayoutParams(params2);
				imageView.setImageResource(R.drawable.dialog_loading);

				TextView textView = new TextView(context);
				LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
				textView.setTextColor(context.getResources().getColor(R.color.gray));
				textView.setTextSize(18);
				textView.setGravity(Gravity.CENTER);
				textView.setLayoutParams(params3);
				layout.addView(imageView);
				layout.addView(textView);

				if (text != null) {
					textView.setText(text);
				}
				imageView.startAnimation(AnimUtil.getDialogRotateAnimation());
				loadingDialogPro.setContentView(layout, new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
				loadingDialogPro.setCancelable(false);
				loadingDialogPro.show();
			}
		});
	}

	/**
	 * 第三部分：含确定取消按钮的dialog
	 */
	/**
	 * @notice 退出程序专用dialog，少了finish
	 */
	public static void dialogShow(Context context) {
		dialogNormal(context, null, null, null, null, null, null);
	}

	/**
	 * @param context
	 * @param title：标题
	 * @param content：内容
	 * @notice 纯文字显示
	 */
	public static void dialogShow(Context context, String title, String content) {
		dialogNormal(context, title, content, null, null, null, null);
	}

	/**
	 * @param listener2：确定按钮的监听器
	 * @notice 传确定按钮的文字和监听器
	 */
	public static void dialogShow(Context context, String title, String content, String right,
			DialogConfirmListener listener2) {
		dialogNormal(context, title, content, null, right, null, listener2);
	}

	/**
	 * @param title：标题
	 * @param content：内容
	 * @param left：左侧按钮文字
	 * @param right：右侧按钮文字
	 * @param listener1：左侧按钮的监听器，DialogCancelListener
	 * @param listener2：右侧按钮的监听器，DialogConfirmListener
	 * @notice 自定义标题，内容，2个按钮的文字，2个监听器
	 */
	public static void dialogShow(Context context, String title, String content, String left, String right,
			DialogCancelListener listener1, DialogConfirmListener listener2) {
		dialogNormal(context, title, content, left, right, listener1, listener2);

	}

	public interface DialogCancelListener {
		public void dialogCancel();
	}

	public interface DialogConfirmListener {
		public void dialogConfirm();
	}

	private static void dialogNormal(final Context context, final String title, final String content,
			final String left, final String right, final DialogCancelListener listener1,
			final DialogConfirmListener listener2) {
		((Activity) context).runOnUiThread(new Runnable() {
			public void run() {
				final Dialog dialog = new Dialog(context,R.style.dialog_white_style);
				View layout = LayoutInflater.from(context).inflate(R.layout.yuntu_dialog_normal,
						null);
				TextView dialogTitle = (TextView) layout.findViewById(R.id.yuntu_dialog_normal_title);
				TextView dialogMessage = (TextView) layout.findViewById(R.id.yuntu_dialog_normal_message);
				Button cancel_btn = (Button) layout.findViewById(R.id.yuntu_dialog_normal_cancel);
				Button confirm_btn = (Button) layout.findViewById(R.id.yuntu_dialog_normal_confirm);
				dialogTitle.getPaint().setFakeBoldText(true);
				// 增加容错性
				if (title != null) {
					dialogTitle.setText("" + title);
				}
				if (content != null) {
					dialogMessage.setText("" + content);
				}
				if (left != null) {
					cancel_btn.setText("" + left);
				}
				if (right != null) {
					confirm_btn.setText("" + right);
				}

				cancel_btn.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						if (listener1 != null) {
							dialog.dismiss();
							listener1.dialogCancel();
						} else {
							dialog.dismiss();
							// AnimUtil.animBackFinish((Activity) context);
						}
					}
				});

				confirm_btn.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						if (listener2 != null) {
							dialog.dismiss();
							listener2.dialogConfirm();
						} else {
							dialog.dismiss();
						}
					}
				});
				if (DeviceUtil.isPad(context) && DeviceUtil.isLandscape(context)) {
					dialog.setContentView(layout, new LayoutParams((int) (DeviceUtil.getWidth(context) * 0.35),
							LayoutParams.WRAP_CONTENT));
				} else {
					dialog.setContentView(layout, new LayoutParams((int) (DeviceUtil.getWidth(context) * 0.9),
							LayoutParams.WRAP_CONTENT));
				}

				dialog.show();

			}
		});
	}

	/**
	 * 返回键也无法取消dialog，暂时没有用到
	 */
	@SuppressWarnings("unused")
	private static OnKeyListener keylistener = new DialogInterface.OnKeyListener() {
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
				return true;
			} else {
				return false;
			}
		}
	};

	/**
	 * 第四部分：含一个EditText的dialog
	 */
	/**
	 * @content  自定义回调listener，获取到文本后要做的，这里不进行空值判断
	 */
	public interface ObtainTextListener {
		public void obtainText(String string);
	}


	/**
	 * 第六部分：自定义日期选择器样式的dialog
	 */

	public interface ObtainDateListener {
		public void obtainDate(Date start, Date end);
	}

	/**
	 * @notice 获取日期选择器，从7天前到今天
	 */
//	public static void dialogDate(final Context context, final ObtainDateListener obtainDateListener) {
//		final Calendar startcalendar = Calendar.getInstance();
//		final Calendar endcalendar = Calendar.getInstance();
//		final Dialog dialog_search = new Dialog(context, R.style.dialog_white_style);
//		View dialogSearch = LayoutInflater.from(context).inflate(R.layout.yuntu_dialog_date, null);
//		final Button timeStart = (Button) dialogSearch.findViewById(R.id.dialog_search_start);
//		final Button timeEnd = (Button) dialogSearch.findViewById(R.id.dialog_search_end);
//		startcalendar.set(TimeUtil.getYear(), TimeUtil.getMonth() - 1, TimeUtil.getDay() - 7);
//		// endcalendar.set(TimeUtil.getYear(), TimeUtil.getMonth() - 1,
//		// TimeUtil.getDay());
//		timeStart.setText(startcalendar.get(Calendar.YEAR) + "." + (startcalendar.get(Calendar.MONTH) + 1) + "."
//				+ (startcalendar.get(Calendar.DAY_OF_MONTH)));
//		timeEnd.setText(endcalendar.get(Calendar.YEAR) + "." + (endcalendar.get(Calendar.MONTH) + 1) + "."
//				+ (endcalendar.get(Calendar.DAY_OF_MONTH)));
//
//		final Button searchButton = (Button) dialogSearch.findViewById(R.id.dialog_search_confirm);
//		// dialog_search.setCanceledOnTouchOutside(false);
//		if (DeviceUtil.isPad(context) && DeviceUtil.isLandscape(context)) {
//			dialog_search.setContentView(dialogSearch, new ViewGroup.LayoutParams(
//					(int) (DeviceUtil.getWidth(context) * 0.5), LayoutParams.WRAP_CONTENT));
//		} else {
//			dialog_search.setContentView(dialogSearch, new ViewGroup.LayoutParams(
//					(int) (DeviceUtil.getWidth(context) * 0.8), LayoutParams.WRAP_CONTENT));
//		}
//
//		timeStart.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(new OnDateSetListener() {
//					public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
//						timeStart.setText(year + "." + (month + 1) + "." + day);
//						startcalendar.set(year, month, day);
//					}
//				}, startcalendar.get(Calendar.YEAR), startcalendar.get(Calendar.MONTH),
//						startcalendar.get(Calendar.DAY_OF_MONTH), true);
//
//				datePickerDialog.setYearRange(1985, 2028);
//				datePickerDialog.show(((Activity) context).getFragmentManager(), "datepicker");
//			}
//		});
//
//		timeEnd.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				final DatePickerDialog datePickerDialog2 = DatePickerDialog.newInstance(new OnDateSetListener() {
//					public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
//						timeEnd.setText(year + "." + (month + 1) + "." + day);
//						endcalendar.set(year, month, day);
//					}
//				}, endcalendar.get(Calendar.YEAR), endcalendar.get(Calendar.MONTH),
//						endcalendar.get(Calendar.DAY_OF_MONTH), true);
//
//				datePickerDialog2.setYearRange(1985, 2028);
//				datePickerDialog2.show(((Activity) context).getFragmentManager(), "datepicker");
//			}
//		});
//
//		searchButton.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				try {
//					if (startcalendar.getTime().before(endcalendar.getTime())) {
//						dialog_search.dismiss();
//						if (obtainDateListener != null) {
//							obtainDateListener.obtainDate(startcalendar.getTime(), endcalendar.getTime());
//
//						}
//					} else {
//						TS.toast(context, "日期顺序错误，请重试");
//					}
//				} catch (Exception e) {
//					TS.toast(context, "请选择日期");
//				}
//			}
//		});
//		dialog_search.show();
//	}

	public interface ObtainCityListener {
		public void obtainCity(String city);
	}

	/**
	 * @param context
	 * @param obtainCityListener：获取城市的listener
	 * @notice 城市选择器
	 */
//	public static void dialogCity(final Context context, final ObtainCityListener obtainCityListener) {
//		((Activity) context).runOnUiThread(new Runnable() {
//			public void run() {
//				final Dialog dialog = new Dialog(context, Helper.dialog_white_style(context));
//				View pickerCity = LayoutInflater.from(context).inflate(Helper.getLayout(context, "yuntu_dialog_city"),
//						null);
//				final CityPicker cityPicker = (CityPicker) pickerCity.findViewById(Helper.getId(context,
//						"yuntu_dialog_city_include"));
//				TextView textView = (TextView) pickerCity.findViewById(Helper
//						.getId(context, "yuntu_dialog_city_button"));
//				textView.setOnClickListener(new OnClickListener() {
//					public void onClick(View arg0) {
//						dialog.dismiss();
//						if (obtainCityListener != null) {
//							obtainCityListener.obtainCity(cityPicker.getCity());
//						}
//					}
//				});
//				dialog.setContentView(pickerCity, new LinearLayout.LayoutParams(
//						(int) (DeviceUtil.getWidth(context) * 0.8), LinearLayout.LayoutParams.WRAP_CONTENT));
//				dialog.show();
//			}
//		});
//	}

//	public static void dialogPicker(final Context context, final ObtainCityListener obtainCityListener) {
//		((Activity) context).runOnUiThread(new Runnable() {
//			public void run() {
//				final Dialog dialog = new Dialog(context, Helper.dialog_white_style(context));
//				View pickerCity = LayoutInflater.from(context).inflate(
//						Helper.getLayout(context, "yuntu_dialog_picker"), null);
//				final CityPicker2 cityPicker2 = (CityPicker2) pickerCity.findViewById(Helper.getId(context,
//						"yuntu_dialog_city_include"));
//				TextView textView = (TextView) pickerCity.findViewById(Helper
//						.getId(context, "yuntu_dialog_city_button"));
//				textView.setOnClickListener(new OnClickListener() {
//					public void onClick(View arg0) {
//						dialog.dismiss();
//						if (obtainCityListener != null) {
//							obtainCityListener.obtainCity(cityPicker2.getCity());
//						}
//					}
//				});
//				dialog.setContentView(pickerCity, new LinearLayout.LayoutParams(
//						(int) (DeviceUtil.getWidth(context) * 0.8), LinearLayout.LayoutParams.WRAP_CONTENT));
//				dialog.show();
//			}
//		});
//	}

	// 设置默认选中的日期 格式为 “2014-04-05” 标准DATE格式
	private static String date = null;

//	/**
//	 * @param context
//	 * @notice 显示日历组件
//	 */
//	public static void dialogCalendar(Context context) {
//		final Dialog dialog = new Dialog(context, R.style.dialog_white_style);
//		View view = View.inflate(context, R.layout.yuntu_dialog_calendar, null);
//		final TextView popupwindow_calendar_month = (TextView) view.findViewById(R.id.popupwindow_calendar_month);
//		final CalendarView calendar = (CalendarView) view.findViewById(R.id.popupwindow_calendar);
//		Button popupwindow_calendar_bt_enter = (Button) view.findViewById(R.id.popupwindow_calendar_bt_enter);
//		popupwindow_calendar_month.setText(calendar.getCalendarYear() + "年" + calendar.getCalendarMonth() + "月");
//		if (null != date) {
//			int years = Integer.parseInt(date.substring(0, date.indexOf("-")));
//			int month = Integer.parseInt(date.substring(date.indexOf("-") + 1, date.lastIndexOf("-")));
//			popupwindow_calendar_month.setText(years + "年" + month + "月");
//			calendar.showCalendar(years, month);
//			calendar.setCalendarDayBgColor(date, R.drawable.calendar_date_focused);
//		}
//		List<String> list = new ArrayList<String>(); // 设置标记列表
//		list.add("2014-04-01");
//		list.add("2014-04-02");
//		calendar.addMarks(list, 0);
//		// 监听所选中的日期
//		calendar.setOnCalendarClickListener(new OnCalendarClickListener() {
//			public void onCalendarClick(int row, int col, String dateFormat) {
//				int month = Integer.parseInt(dateFormat.substring(dateFormat.indexOf("-") + 1,
//						dateFormat.lastIndexOf("-")));
//				if (calendar.getCalendarMonth() - month == 1// 跨年跳转
//						|| calendar.getCalendarMonth() - month == -11) {
//					calendar.lastMonth();
//				} else if (month - calendar.getCalendarMonth() == 1 // 跨年跳转
//						|| month - calendar.getCalendarMonth() == -11) {
//					calendar.nextMonth();
//				} else {
//					calendar.removeAllBgColor();
//					calendar.setCalendarDayBgColor(dateFormat, R.drawable.calendar_date_focused);
//					date = dateFormat;// 最后返回给全局 date
//				}
//			}
//		});
//		// 监听当前月份
//		calendar.setOnCalendarDateChangedListener(new OnCalendarDateChangedListener() {
//			public void onCalendarDateChanged(int year, int month) {
//				popupwindow_calendar_month.setText(year + "年" + month + "月");
//			}
//		});
//
//		// 上月监听按钮
//		RelativeLayout popupwindow_calendar_last_month = (RelativeLayout) view
//				.findViewById(R.id.popupwindow_calendar_last_month);
//		popupwindow_calendar_last_month.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				calendar.lastMonth();
//			}
//		});
//		// 下月监听按钮
//		RelativeLayout popupwindow_calendar_next_month = (RelativeLayout) view
//				.findViewById(R.id.popupwindow_calendar_next_month);
//		popupwindow_calendar_next_month.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				calendar.nextMonth();
//			}
//		});
//		// 关闭窗口
//		popupwindow_calendar_bt_enter.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				dialog.dismiss();
//			}
//		});
//		dialog.setContentView(view);
//		dialog.show();
//	}

	public static void dialogTime() {

	}

	public static void dialogColor() {
	}

	/**
	 * 第七部分：通用的含listview的dialog
	 */
	/**
	 * @param context
	 * @param title：标题
	 * @param gravity：列表文字内容是否居中显示
	 * @param list：数据
	 * @param dialogListener：监听器，DialogViewListener，只需要onListItemClick
	 * @notice 没有按钮的dialog，需要一个列表item的监听器
	 */
	public static void dialogStyle0(Context context, String title, boolean gravity, ArrayList<String> list,
			DialogViewListener dialogListener) {
		dialogView(context, title, gravity, list, dialogListener, true);
	}

	/**
	 * @param context
	 * @param title：标题
	 * @param gravity：列表文字内容是否居中显示
	 * @param list：数据
	 * @param dialogListener：监听器，DialogViewListener，只需要onListItemClick
	 * @param wrapContent：内容高度是否为wrap_content
	 * @notice 没有按钮的dialog，需要一个列表item的监听器
	 */
	public static void dialogStyle0(Context context, String title, boolean gravity, ArrayList<String> list,
			DialogViewListener dialogListener, boolean wrapContent) {
		dialogView(context, title, gravity, list, dialogListener, wrapContent);
	}

	/**
	 * @param context
	 * @param title：标题
	 * @param button：底部按钮文字
	 * @param gravity：列表文字内容是否居中显示
	 * @param list：数据
	 * @param dialogListener：监听器，DialogViewListener，需要onButtonClick
	 * @notice 1个按钮的dialog，需要监听器
	 */
	public static void dialogStyle1(Context context, String title, String button, boolean gravity,
			ArrayList<String> list, DialogViewListener dialogListener) {
		dialogView(context, title, button, gravity, list, dialogListener, false, true);
	}

	/**
	 * @param context
	 * @param title：标题
	 * @param button：底部按钮文字
	 * @param gravity：列表文字内容是否居中显示
	 * @param list：数据
	 * @notice 1个按钮的dialog，不需要监听器，没有假进度，高度自适应
	 */
	public static void dialogStyle1(Context context, String title, String button, boolean gravity,
			ArrayList<String> list) {
		dialogView(context, title, button, gravity, list, null, false, true);
	}

	/**
	 * @param context
	 * @param title：标题
	 * @param button：底部按钮文字
	 * @param gravity：列表文字内容是否居中显示
	 * @param list：数据
	 * @param progressbar：是否需要假进度
	 * @param wrapContent：是否需要高度为wrap_content
	 * @notice 1个按钮的dialog，不需要监听器，自定义假进度，自定义高度
	 */
	public static void dialogStyle1(Context context, String title, String button, boolean gravity,
			ArrayList<String> list, boolean progressbar, boolean wrapContent) {
		dialogView(context, title, button, gravity, list, null, progressbar, wrapContent);
	}

	/**
	 * @param context
	 * @param title：标题
	 * @param left：底部左侧文字
	 * @param right：底部右侧文字
	 * @param gravity：列表文字内容是否居中显示
	 * @param list：数据
	 * @param dialogListener：监听器，DialogViewListener，酌情处理
	 * @notice 2个按钮的dialog，需要监听器
	 */
	public static void dialogStyle2(Context context, String title, String left, String right, boolean gravity,
			ArrayList<String> list, DialogViewListener dialogListener) {
		dialogView(context, title, left, right, gravity, list, dialogListener);
	}

	// noButton模式
	private static void dialogView(Context context, String title, boolean gravity, ArrayList<String> list,
			DialogViewListener dialogListener, boolean wrapContent) {
		final DialogView dialog = new DialogView(context, title, gravity, list, dialogListener, wrapContent);
		dialog.show();
		dialog.postShow();
	}

	// oneButton模式
	private static void dialogView(Context context, String title, String button, boolean gravity,
			ArrayList<String> list, DialogViewListener dialogListener, boolean progress, boolean wrapContent) {
		final DialogView dialog = new DialogView(context, title, button, gravity, list, dialogListener, wrapContent);
		dialog.show();
		if (!progress) {
			dialog.postShow();
		} else {
			new Handler().postDelayed(new Runnable() {
				public void run() {
					dialog.postShow();
				}
			}, 800);
		}

	}

	// twoButton模式
	private static void dialogView(Context context, String title, String left, String right, boolean gravity,
			ArrayList<String> list, DialogViewListener dialogListener) {
		final DialogView dialog = new DialogView(context, title, left, right, gravity, list, dialogListener);
		dialog.show();
		dialog.postShow();
	}

	/**
	 * 第八部分：设置相关的dialog
	 */
	public interface FeedbackListener {
		public void feedback(String string);
	}

	/**
	 * @param context
	 * @param feedbackListener，需要异步执行
	 * @notice 意见反馈的dialog，不需要dialog，传null则发送邮件
	 */
	public static void dialogFeedback(final Context context, final FeedbackListener feedbackListener) {
		final Dialog dialog_feedback = new Dialog(context, R.style.dialog_white_style);
		View feedbackDialog = LayoutInflater.from(context).inflate(R.layout.yuntu_dialog_feedback,
				null);
		TextView title = (TextView) feedbackDialog.findViewById(R.id.yuntu_dialog_feedback_title);
		title.setText("意见反馈");
		final EditText edit_feedback = (EditText) feedbackDialog.findViewById(R.id.yuntu_dialog_feedback_edittext);
		Button send = (Button) feedbackDialog.findViewById(R.id.yuntu_dialog_feedback_button);
		dialog_feedback.setContentView(feedbackDialog, new ViewGroup.LayoutParams(
				(int) (DeviceUtil.getWidth(context) * 0.8), LayoutParams.WRAP_CONTENT));
		dialog_feedback.setCanceledOnTouchOutside(false);
		send.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// 比较的时候可以用trim()，但传参的时候不可以trim()
				if (!edit_feedback.getText().toString().trim().equals("")) {
					if (!NetworkUtil.isNetworkAvailable(context)) {
						dialog_feedback.dismiss();
						ToastUtil.toast(context, "当前网络不可用，请打开WIFI");
						IntentUtil.intentToWifi(context);
					} else {
						dialog_feedback.dismiss();
						if (feedbackListener != null) {
							feedbackListener.feedback(edit_feedback.getText().toString());
						} else {
							SendUtil.sendMail(edit_feedback.getText().toString());
						}
						ToastUtil.toast(context, "已收到您的反馈，谢谢");
					}
				} else {
					ToastUtil.toast(context, "请输入点内容吧^_^");
				}
			}
		});
		dialog_feedback.show();
	}




	/**
	 * @param context
	 * @param string
	 * @notice 分享的dialog
	 */
	public static void dialogShare(Context context, String string) {
		AndroidShare as = new AndroidShare(context, string, null);
		as.show();
	}

	/**
	 * @param context
	 * @param string
	 * @param imagePath：图片路径
	 * @notice 分享的dialog
	 */
	public static void dialogShare(Context context, String string, String imagePath) {
		AndroidShare as = new AndroidShare(context, string, imagePath);
		as.show();
	}

	private static Dialog dialogYuntu;

	public static void dialogYuntu(Context context) {
		dialogYuntu = new Dialog(context, R.style.dialog_loading_style);
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_yuntu, null);
		TextView[] TextViews = new TextView[7];
		int time = 150;
		TextViews[0] = (TextView) view.findViewById(R.id.image0);
		TextViews[1] = (TextView) view.findViewById(R.id.image1);
		TextViews[2] = (TextView) view.findViewById(R.id.image2);
		TextViews[3] = (TextView) view.findViewById(R.id.image3);
		TextViews[4] = (TextView) view.findViewById(R.id.image4);
		TextViews[5] = (TextView) view.findViewById(R.id.image5);
		TextViews[6] = (TextView) view.findViewById(R.id.image6);

		if (DeviceUtil.isPad(context) && DeviceUtil.isLandscape(context)) {
			dialogYuntu.setContentView(view, new LayoutParams((int) (DeviceUtil.getWidth(context) * 0.35), LayoutParams.MATCH_PARENT));
		} else {
			dialogYuntu.setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		}

		dialogYuntu.show();
		Timer timer = new Timer();
		for (int i = 0; i < TextViews.length; i++) {
			timer.schedule(new Task(context, TextViews[i]), time * (i + 1));
		}
	}

	public static void dialogYuntuDismiss() {
		dialogYuntu.dismiss();
	}

	static class Task extends TimerTask {
		Context context;
		TextView textView;

		public Task(Context context, TextView textView) {
			this.textView = textView;
			this.context = context;
		}

		public void run() {
			((Activity) context).runOnUiThread(new Runnable() {
				public void run() {
					final TranslateAnimation translateAnimation3 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0.5f);
					translateAnimation3.setDuration(500);
					translateAnimation3.setInterpolator(new AccelerateInterpolator());
					translateAnimation3.setFillAfter(true);

					final TranslateAnimation translateAnimation4 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0);
					translateAnimation4.setInterpolator(new AccelerateDecelerateInterpolator());
					translateAnimation4.setDuration(500);
					translateAnimation4.setFillAfter(true);

					translateAnimation3.setAnimationListener(new AnimationListener() {
						public void onAnimationStart(Animation arg0) {
						}

						public void onAnimationRepeat(Animation arg0) {
						}

						public void onAnimationEnd(Animation arg0) {
							textView.startAnimation(translateAnimation4);
						}
					});

					translateAnimation4.setAnimationListener(new AnimationListener() {
						public void onAnimationStart(Animation arg0) {
						}

						public void onAnimationRepeat(Animation arg0) {
						}

						public void onAnimationEnd(Animation arg0) {
							textView.startAnimation(translateAnimation3);
						}
					});

					textView.startAnimation(translateAnimation3);
				}
			});

		}
	}

}
