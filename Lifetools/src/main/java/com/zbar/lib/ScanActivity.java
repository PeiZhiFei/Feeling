package com.zbar.lib;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feifei.lifetools.BaseActivity;
import com.feifei.lifetools.R;
import com.feifei.util.AnimUtil;
import com.feifei.util.IntentUtil;
import com.feifei.util.NetworkUtil;
import com.feifei.util.ToastCustom;
import com.feifei.util.ToastUtil;
import com.zbar.lib.camera.CameraManager;
import com.zbar.lib.decode.CaptureActivityHandler;
import com.zbar.lib.decode.InactivityTimer;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class ScanActivity extends BaseActivity implements Callback {

	private CaptureActivityHandler handler;
	private boolean hasSurface;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.50f;
	private boolean vibrate;
	private int x = 0;
	private int y = 0;
	private int cropWidth = 0;
	private int cropHeight = 0;
	private RelativeLayout mContainer = null;
	private RelativeLayout mCropLayout = null;
	private boolean isNeedCapture = false;
	// 动态设置顶部遮罩的高度
	ImageView topMask;
	Button scan_light;
	Button scan_book;
	Button scan_pic;
	TextView text_wenzi;
	public static boolean isOpen = false;
	private boolean scanBook = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan);
		topMask = (ImageView) findViewById(R.id.top_mask);
		actionbarStyle0("扫一扫", R.drawable.circle_scan);
		CameraManager.init(getApplication());
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
		mContainer = (RelativeLayout) findViewById(R.id.capture_containter);
		mCropLayout = (RelativeLayout) findViewById(R.id.capture_crop_layout);
		ImageView mQrLineView = (ImageView) findViewById(R.id.capture_scan_line);
		AnimUtil.animScan(mQrLineView);
		initBars();
		actionbarFirstAnim(scanFIrst, "scanFIrst", "新增加扫图书的功能哦");
	}

	private void initBars() {
		scan_book = (Button) findViewById(R.id.scan_book);
		scan_light = (Button) findViewById(R.id.btn_deng);
		text_wenzi = (TextView) findViewById(R.id.text_wenzi);
		scan_light.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (!isOpen) {
					CameraManager.get().openLight();
					scan_light
							.setBackgroundResource(R.drawable.scan_button_flash_pressed);
					text_wenzi.setText("关灯");
				} else {
					CameraManager.get().offLight();
					scan_light.setBackgroundResource(R.drawable.scan_light);
					text_wenzi.setText("开灯");
				}
				isOpen = !isOpen;

			}
		});
		scan_book.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (!scanBook) {
					scanBook = true;
					scan_book
							.setBackgroundResource(R.drawable.scan_button_myqrcode_pressed);
					ToastUtil.toast(activity, "请扫描图书条码");
				} else {
					scanBook = false;
					scan_book
							.setBackgroundResource(R.drawable.scan_button_myqrcode_normal);
				}
			}
		});
		scan_pic = (Button) findViewById(R.id.scan_pic);
		scan_pic.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, 1);
			}
		});

	}

	boolean flag = true;

	protected void light() {
		if (flag == true) {
			flag = false;
			// 开闪光灯
			CameraManager.get().openLight();
		} else {
			flag = true;
			// 关闪光灯
			CameraManager.get().offLight();
		}

	}

	@SuppressWarnings("deprecation")
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.capture_preview);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}

	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	public void handleDecode(String result) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		if (!NetworkUtil.isNetworkAvailable(activity)) {
			ToastCustom.toast(this, "网络异常，请检查你的网络连接");
			IntentUtil.intentToWifi(this);
		} else {
			if (TextUtils.isEmpty(result)) {
				ToastUtil.toast(this, "未获取到数据，再试一次吧");
			} else {
				// 优先扫描图书
				if (scanBook) {
					String urlstr = "https://api.douban.com/v2/book/isbn/"
							+ result;
					// 扫到ISBN后，启动下载线程下载图书信息
					ToastCustom.toast(this, "正在查询中，请稍后……");
				} else {
					if ((result.startsWith("http://"))) {
						if ((result.startsWith("http://weixin."))) {
							actionbarUpAnim();
							ToastCustom.toast(this, "请用微信的扫一扫来扫描并关注",
									Gravity.TOP,false);
							Timer timer = new Timer();
							timer.schedule(new Task(), 2000);
						} else {
							IntentUtil.intentToInternet(this, result);
						}
					} else {
						ToastUtil.toast(this, "未获取到二维码数据，再试一次吧");
					}
				}
			}
		}
		// 是否开启连续扫描
		// handler.sendEmptyMessage(R.id.restart_preview);
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
			Point point = CameraManager.get().getCameraResolution();
			int width = point.y;
			int height = point.x;
			int x = mCropLayout.getLeft() * width / mContainer.getWidth();
			int y = mCropLayout.getTop() * height / mContainer.getHeight();
			int cropWidth = mCropLayout.getWidth() * width
					/ mContainer.getWidth();
			int cropHeight = mCropLayout.getHeight() * height
					/ mContainer.getHeight();
			setX(x);
			setY(y);
			setCropWidth(cropWidth);
			setCropHeight(cropHeight);
			// 设置是否需要截图
			setNeedCapture(true);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(ScanActivity.this);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public Handler getHandler() {
		return handler;
	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

	class Task extends TimerTask {
		public void run() {
			runOnUiThread(new Runnable() {
				public void run() {
					actionbarDownAnim();
				}
			});

		}
	}

	public boolean isNeedCapture() {
		return isNeedCapture;
	}

	public void setNeedCapture(boolean isNeedCapture) {
		this.isNeedCapture = isNeedCapture;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getCropWidth() {
		return cropWidth;
	}

	public void setCropWidth(int cropWidth) {
		this.cropWidth = cropWidth;
	}

	public int getCropHeight() {
		return cropHeight;
	}

	public void setCropHeight(int cropHeight) {
		this.cropHeight = cropHeight;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();
			Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
			scanningImage(bitmap);
		}

	}

	// 解析QR图片
	private void scanningImage(Bitmap bitmap) {
	}
}