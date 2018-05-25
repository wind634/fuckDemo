package com.dotpix.fuckdemo.tasks;

import android.util.Log;

import java.util.TimerTask;

/**
 * Created by wangjiang on 2018/5/25.
 */

public class CompareTask extends TimerTask  {
    private final String TAG = "CompareTask";

    @Override
    public void run() {
        Log.e(TAG, "CompareTask run.............");
    }


}
