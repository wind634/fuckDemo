package com.dotpix.fuckdemo.tasks;

import android.annotation.SuppressLint;
import android.util.Log;

import com.dotpix.fuckdemo.activity.MainActivity;
import com.dotpix.fuckdemo.common.SysConfig;
import com.dotpix.fuckdemo.model.Record;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

/**
 * Created by wangjiang on 2018/5/25.
 */

public class CompareTask extends TimerTask  {
    private final String TAG = SysConfig.COMPARE_TAG;

    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private MainActivity context;
    private int currentIndex = 0;


    public CompareTask(MainActivity context, int index){
        this.context = context;
        this.currentIndex = index;
    }

    @Override
    public void run() {
        Log.e(TAG, "CompareTask run.............");
        Log.e(TAG, "start to begin 【" + (currentIndex + 1)+"】 image compare......");

        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                Toast.makeText(context, "开始进行第" + (currentIndex + 1)+"张的人脸比对", Toast.LENGTH_SHORT).show();
                context.setBlueLogText(context.getDateTextView() + "   " + "开始进行第" + (currentIndex + 1)+"张的人脸比对");
            }
        });
        long currentTime = System.currentTimeMillis();
        long compareTime = System.currentTimeMillis();
        String compareDate = "未比对";
        boolean hasCompare = false;
        float score =-1;
        while ((compareTime - currentTime)/1000 < SysConfig.CompareSustainedTime) {
            compareTime = System.currentTimeMillis();
            // 开始进行比对
            score = context.startToCompare();
            if(score>0){
                compareDate = formatter1.format(new Date());
                hasCompare = true;
                break;
            }
        }


        Record record = new Record();
        String[] pathList = MainActivity.imagePathList.get(MainActivity.currentCompareImageIndex).split("/");
        String imageName =  pathList[pathList.length-1];
        record.setCompareImageName(imageName);
        record.setCompareImagePath(MainActivity.imagePathList.get(MainActivity.currentCompareImageIndex));
        record.setFaceImagePath(context.saveFaceBitmap());
        if(score>0) {
            record.setScore(score);
        }
        record.setCompareTime(compareDate);
        if(hasCompare==false && score == -1) {
            record.setExt("比对图片没有人脸");
        }
        if(hasCompare==false && score == -2) {
            record.setExt("摄像头未抓拍到人脸");
        }

        context.addRecord2Excel(record);


        final float finalScore = score;
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                Toast.makeText(context, "结束进行第" + (currentIndex + 1)+"张的人脸比对", Toast.LENGTH_SHORT).show();
                if(finalScore >0) {
                    context.setBlueLogText(context.getDateTextView() + "   " + "结束进行第" + (currentIndex + 1) + "张的人脸比对, 比对得分:" + finalScore);
                }else{
                    context.setBlueLogText(context.getDateTextView() + "   " + "结束进行第" + (currentIndex + 1) + "张的人脸比对");
                }
            }
        });

    }


}
