package com.dotpix.fuckdemo.tasks;

import android.util.Log;
import android.widget.Toast;

import com.dotpix.fuckdemo.activity.MainActivity;

import java.util.TimerTask;

/**
 * Created by wangjiang on 2018/5/25.
 */

public class CompareTask extends TimerTask  {
    private final String TAG = "CompareTask";

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
                Toast.makeText(context, "开始进行第" + (currentIndex + 1)+"张的人脸比对", Toast.LENGTH_SHORT).show();
            }
        });

//        currentIndex

    }


}
