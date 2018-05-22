package com.dotpix.fuckdemo.listener;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.ImageReader;
import android.util.Log;

import com.dotpix.fuckdemo.activity.MainActivity;
import com.dotpix.fuckdemo.common.SysConfig;
import com.pixtalks.detect.DetectResult;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by wangjiang on 17/12/2.
 * 获取图像的监听器
 */

public class OnGetImageListener implements ImageReader.OnImageAvailableListener {

    private String TAG = "OnGetImageListener";
    private long getImageStartTime;

    private MainActivity activity;

    private Boolean hasFace = false;


    public void initialize(){

    }


    public void deInitialize(){

    }

    public OnGetImageListener(MainActivity activity){
        getImageStartTime = System.currentTimeMillis();
        this.activity = activity;
    }

    @Override
    public void onImageAvailable(ImageReader imageReader) {
//        Log.i(TAG, "fetch image *****");

        // 首先判断是否有人脸
        if(!hasFace){
            // 如果没有人脸
            long nowTimestamp = System.currentTimeMillis();
            Date nowTime = new Date(nowTimestamp);
            Date beforeTime = new Date(getImageStartTime);
            long interval = (nowTime.getTime() - beforeTime.getTime());
            // 大于间隔数才开始使用抓拍的图像
            if(interval >= SysConfig.WaitFacePictureInvertalTime){
//                Log.i(TAG, "fetch image by 3s");

                Image image =imageReader.acquireNextImage();
                if(image!=null) {
                    compare(image);
                }
            }else{
                // 否则抛弃图片
                Image image =imageReader.acquireNextImage();
                if(image!=null) {
                    image.close();
                }
                Log.i(TAG, "no fetch image");
            }

        }else{
            // 如果有人脸
            Image image =imageReader.acquireNextImage();
            if(image!=null) {
                compare(image);
            }
        }

    }

    private void compare(Image image){
        getImageStartTime = System.currentTimeMillis();

        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        image.close();

        // MainActivity2 专用
//        if(activity instanceof MainActivity2) {
//            long endRegtime = System.currentTimeMillis();
//            if ((endRegtime - ((MainActivity2) activity).endRegTime) / 1000 > SysConfig.twoCameraCompareCompleteWaitTime) {
//                ((MainActivity2) activity).clearAndRestart();
//            }
//        }
        // MainActivity2 专用


        final Bitmap mRGBframeBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        if (MainActivity.shouldRunCompare) {

            ArrayList<DetectResult> result = activity.judgeHasFaceImage(mRGBframeBitmap);
            if (result == null) {
                hasFace = false;
                Log.i(TAG, "hasFace:" + hasFace);
            } else {
                hasFace = true;
                Log.i(TAG, "hasFace:" + hasFace);
                // 如果检测到人脸, 则启动进行人脸比对

                activity.startThreadToCompare(mRGBframeBitmap, result);
            }
        } else {

        }

    }

}
