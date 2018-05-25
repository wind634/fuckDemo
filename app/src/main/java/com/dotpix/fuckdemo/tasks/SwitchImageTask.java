package com.dotpix.fuckdemo.tasks;

import android.widget.Toast;

import com.dotpix.fuckdemo.activity.MainActivity;
import com.dotpix.fuckdemo.common.SysConfig;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wangjiang on 2018/5/25.
 */

public class SwitchImageTask extends TimerTask{

    private final String TAG = "SwitchImageTask";

    private MainActivity context;

    private int cnt = 0;
    private int imageCount = 0;

    private Timer timer;
    private CompareTask compareTask;

    public SwitchImageTask(MainActivity context){
        this.context = context;
        timer = new Timer();
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

                         compareTask = new CompareTask(context, imageCount);
                         timer.schedule(compareTask, (long) (SysConfig.CompareImageDelayTime * 1000));


                         Toast.makeText(context, "切换到第" + (imageCount+1) +"张图片", Toast.LENGTH_SHORT).show();
                         context.setImage(imageCount);
                     }else{
                         Toast.makeText(context, "图片比对完成", Toast.LENGTH_SHORT).show();

                         if (!compareTask.cancel()){
                             compareTask.cancel();
                             timer.cancel();
                         }
                         context.endReg();
                     }

                 }

             }
        });
    }
}




