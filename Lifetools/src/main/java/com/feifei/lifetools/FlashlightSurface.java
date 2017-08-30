package com.feifei.lifetools;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class FlashlightSurface extends SurfaceView implements
		SurfaceHolder.Callback {

	private SurfaceHolder mHolder;
	private Camera mCameraDevices;
	private Camera.Parameters mParameters;

	public FlashlightSurface(Context context, AttributeSet attrs) {
		super(context, attrs);
		mHolder = this.getHolder();
		mHolder.addCallback(this);
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		try {
			mParameters = mCameraDevices.getParameters();
			mCameraDevices.setParameters(mParameters);
			mCameraDevices.startPreview();
		} catch (Exception e) {
		}
	}

	public void surfaceCreated(SurfaceHolder holder) {
		try {
			mCameraDevices = Camera.open();
			mCameraDevices.setPreviewDisplay(mHolder);
		} catch (Exception e) {
			if (mCameraDevices != null)
				mCameraDevices.release();
			mCameraDevices = null;
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		if (mCameraDevices == null)
			return;
		mCameraDevices.stopPreview();
		mCameraDevices.release();
		mCameraDevices = null;
	}

	public void setFlashlightSwitch(boolean on) {
		if (mCameraDevices == null)
			return;
		if (mParameters == null) {
			mParameters = mCameraDevices.getParameters();
		}
		if (on) {
			mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
		} else {
			mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
		}
		mCameraDevices.setParameters(mParameters);
	}

}
