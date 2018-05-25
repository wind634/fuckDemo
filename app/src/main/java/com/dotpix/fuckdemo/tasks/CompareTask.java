package com.dotpix.fuckdemo.tasks;

import android.widget.Toast;

import com.dotpix.fuckdemo.activity.MainActivity;
import com.dotpix.fuckdemo.common.SysConfig;

import java.util.TimerTask;

/**
 * Created by wangjiang on 2018/5/25.
 */

public class CompareTask extends TimerTask{

    private final String TAG = "CompareTask";

    private MainActivity context;

    private int cnt = 0;
    private int imageCount = 0;

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


                 if(cnt % SysConfig.SwitchImageInvertalTime ==0){
                     if(imageCount != MainActivity.imagePathList.size()-1) {
                         imageCount++;
                         Toast.makeText(context, "切换到第" + (imageCount+1) +"张图片", Toast.LENGTH_SHORT).show();
                         context.setImage(imageCount);
                     }else{
                         Toast.makeText(context, "图片比对完成", Toast.LENGTH_SHORT).show();
                         context.endReg();
                     }

                 }

             }
        });
    }
}




