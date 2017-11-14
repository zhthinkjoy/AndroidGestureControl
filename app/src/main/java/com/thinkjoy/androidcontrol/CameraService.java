package com.thinkjoy.androidcontrol;

import android.app.Service;
import android.content.Intent;
import android.gesture.Gesture;
import android.hardware.Camera;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;

import android.support.annotation.Nullable;
import android.view.SurfaceHolder;

import com.thinkjoy.zhthinkjoygesturedetectlib.GestureInfo;
import com.thinkjoy.zhthinkjoygesturedetectlib.ZHThinkjoyGesture;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thinkjoy on 17-10-28.
 */

public class CameraService extends Service implements Camera.PreviewCallback{

    static {
        System.loadLibrary("gesture");
    }
    private Handler gestureHandler;
    private Camera camera;
    private ZHThinkjoyGesture zhThinkjoyGesture;
    private CameraWindow mCameraWindow;
    private boolean mIsGestureFinished = true;
    private GestureProcess mGestureProcess;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();

        zhThinkjoyGesture = ZHThinkjoyGesture.getInstance(this);
        zhThinkjoyGesture.init();
        mCameraWindow = CameraWindow.getInstance(this);
        final HandlerThread gestureDetectHandler = new HandlerThread("gestureDetect");
        mGestureProcess = new GestureProcess();
        gestureDetectHandler.start();
        gestureHandler = new Handler(gestureDetectHandler.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case GlobalInfo.MSG_GESTURE_DETECT:
                        GestureInfo gestureInfo = (GestureInfo)msg.obj;
                        GestureDetectInfo gestureDetectInfo = new GestureDetectInfo();
                        gestureDetectInfo.type = gestureInfo.type;
                        gestureDetectInfo.left = gestureInfo.gestureRectangle[0].x;
                        gestureDetectInfo.top = gestureInfo.gestureRectangle[0].y;
                        gestureDetectInfo.right = gestureInfo.gestureRectangle[1].x;
                        gestureDetectInfo.bottom = gestureInfo.gestureRectangle[1].y;

                        mGestureProcess.processGesture(gestureDetectInfo);

                        break;
                }
            }
        };
        setupCamera();
    }

    public void setupCamera() {
        camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);

        Camera.Parameters params = camera.getParameters();
        params.setPreviewSize(640, 480);
        params.setPictureSize(640, 480);
        camera.setParameters(params);
        SurfaceHolder holder = mCameraWindow.getView().getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        try {
            camera.setPreviewDisplay(holder);
            camera.setPreviewCallback(this);
            camera.startPreview() ;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
        Camera.Size previewSize = camera.getParameters().getPreviewSize();

        if (mIsGestureFinished == true && gestureHandler != null && bytes != null && bytes.length >= (previewSize.width * previewSize.height)) {
            mIsGestureFinished = false;
            List<GestureInfo> gestureInfoList = new ArrayList<>();
            zhThinkjoyGesture.gestureDetect(bytes, ZHThinkjoyGesture.IMAGE_FORMAT_NV21, previewSize.width, previewSize.height, gestureInfoList);
            if (gestureInfoList.size() > 0) {
                Message msg = new Message();
                msg.what = GlobalInfo.MSG_GESTURE_DETECT;
                msg.obj = gestureInfoList.get(0);
                gestureHandler.sendMessage(msg);
            }
            mIsGestureFinished = true;
        }

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (camera != null) {
            camera.release();
        }
    }
}
