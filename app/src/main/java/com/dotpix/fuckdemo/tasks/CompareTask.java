package com.dotpix.fuckdemo.tasks;

import com.dotpix.fuckdemo.activity.MainActivity;

import java.util.TimerTask;

/**
 * Created by wangjiang on 2018/5/25.
 */

public class CompareTask extends TimerTask{

    private final String TAG = "CompareTask";

    private MainActivity context;
    private int cnt = 0;
    public CompareTask(MainActivity context){
        this.context = context;
    }


    @Override
    public void run() {
        context.runOnUiThread(new Runnable() {
             @Override
            public void run() {
                 cnt++;
                 // 1小时 3600
                 int hours = cnt / 3600;
                 int minutes = (cnt - hours * 3600)/ 60;
                 int seconds = cnt - hours * 3600 - minutes * 60;
                 String hoursStr = String.format("%02d", hours);
                 String minutesStr = String.format("%02d", minutes);
                 String secondsStr = String.format("%02d", seconds);
                 context.setDateTextView(hoursStr + ":" + minutesStr + ":" + secondsStr);
//                 textView.setText(getStringTime(cnt++));
             }
        });
    }
}




