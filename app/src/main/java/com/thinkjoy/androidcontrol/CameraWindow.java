package com.thinkjoy.androidcontrol;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

/**
 * Created by thinkjoy on 17-10-28.
 */

public class CameraWindow {
    private static Context mContext;
    private static CameraWindow mCameraWindow;
    private WindowManager mWindowManager;
    private SurfaceView mHideCameraView;
    private CameraWindow(Context context){
        mWindowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
    }

    public static CameraWindow getInstance(Context context) {
        mContext = context;
        if (mCameraWindow == null) {
            mCameraWindow = new CameraWindow(context);
            mCameraWindow.setupView();
        }
        return mCameraWindow;
    }


    public void setupView() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.height = 1;
        layoutParams.width = 1;
        layoutParams.x = 1;
        layoutParams.y = 1;
//        layoutParams.alpha = 0;
        layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
//        layoutParams.gravity = Gravity.LEFT;
//        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_hidecamera, null);
        mHideCameraView = new SurfaceView(mContext.getApplicationContext());
        mWindowManager.addView(mHideCameraView, layoutParams);
    }
    public SurfaceView getView() {
        return mHideCameraView;
    }
}
