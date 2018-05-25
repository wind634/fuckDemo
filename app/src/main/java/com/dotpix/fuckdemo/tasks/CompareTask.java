package com.dotpix.fuckdemo.tasks;

import android.util.Log;

import com.dotpix.fuckdemo.activity.MainActivity;
import com.dotpix.fuckdemo.common.SysConfig;

import java.util.TimerTask;

/**
 * Created by wangjiang on 2018/5/25.
 */

public class CompareTask extends TimerTask  {
    private final String TAG = SysConfig.COMPARE_TAG;

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
        boolean hasCompare = false;
        float score =-1;
        while ((compareTime - currentTime)/1000 < SysConfig.CompareSustainedTime) {
            compareTime = System.currentTimeMillis();
            // 开始进行比对
            score = context.startToCompare();
            if(score>0){
                hasCompare = true;
                break;
            }
        }
        context.addRecord2Excel(score);


        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                Toast.makeText(context, "结束进行第" + (currentIndex + 1)+"张的人脸比对", Toast.LENGTH_SHORT).show();
                context.setBlueLogText(context.getDateTextView() + "   " + "结束进行第" + (currentIndex + 1)+"张的人脸比对");
            }
        });

    }


}
