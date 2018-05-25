package com.dotpix.fuckdemo.activity;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dotpix.fuckdemo.R;
import com.dotpix.fuckdemo.fragment.Camera2BasicFragment;
import com.dotpix.fuckdemo.tasks.CompareTask;
import com.dotpix.fuckdemo.utils.LightHelper;
import com.dotpix.fuckdemo.widget.GridFocusFaceView;
import com.dyhdyh.widget.loading.dialog.LoadingDialog;
import com.pixtalks.detect.DetectResult;
import com.pixtalks.facekitsdk.FaceKit;
import com.pixtalks.facekitsdk.PConfig;

import java.util.ArrayList;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public static boolean shouldRunCompare=true;
    public FaceKit faceKit=null;
    private GridFocusFaceView redFace;
    private RelativeLayout layout_frame;
    private Dialog initDialog;
    private Button startCompareBtn;
    private TextView dateText;

    // 是否正在识别
    private boolean isReg=false;
    private  CompareTask compareTask;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (null == savedInstanceState) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, Camera2BasicFragment.newInstance())
                    .commit();
        }

        timer = new Timer();
        initView();

       initFaceKit();

    }

    private void initView(){
        redFace = (GridFocusFaceView) findViewById(R.id.redFace);
        layout_frame = (RelativeLayout) findViewById(R.id.layout_frame);
        initDialog = LoadingDialog.make(MainActivity.this)
                .setMessage("正在初始化中")//提示消息
                .setCancelable(true)
                .create();
        initDialog.show();
        startCompareBtn = (Button)findViewById(R.id.startCompare);
        dateText = (TextView) findViewById(R.id.dateText);

        startCompareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isReg) {
                    startReg();
                }else {
                    endReg();
                }
            }
        });


    }
    private void startReg(){
        startCompareBtn.setText("停止识别");
        isReg = true;
        compareTask = new CompareTask(MainActivity.this);
        timer.schedule(compareTask,0,1000);

    }

    private void endReg(){
        startCompareBtn.setText("开始识别");
        isReg = false;
        if (!compareTask.cancel()){
            compareTask.cancel();
            timer.cancel();
        }
        setDateTextView("00:00:00");
    }

    private void initFaceKit(){
        /**
         * 示例代码
         */

        new Thread(new Runnable() {
            @Override
            public void run() {
                faceKit = new FaceKit(MainActivity.this);
                PConfig.detectInSizeLevel = 2;
                faceKit.setFaceArgs(1.5f, 1.7f);
                faceKit.setAuth("ShangHai_Inner_Test_74543234", "T20_73452342344343");
                // 初始化模型
                long begin = System.currentTimeMillis();
                int ret = faceKit.initModel();
                Log.e(PConfig.projectLogTag, "Load model use time " + (System.currentTimeMillis() - begin));
                if (ret != PConfig.okCode) {
                    Log.e(PConfig.projectLogTag, "Fail to load model with " + ret);
                    return;
                }
                if(initDialog!=null) {
                    initDialog.cancel();
                }
            }
        }).start();

    }


    /**
     *    获取头像是否有人脸
     * @param bitmap
     * @return
     */
    public ArrayList<DetectResult> judgeHasFaceImage(final Bitmap bitmap) {
        if (faceKit == null) {
            return null;
        }

        long begin = System.currentTimeMillis();
        ArrayList<DetectResult> faceDetectResults = faceKit.detectFace(bitmap);
        long end = System.currentTimeMillis();
        Log.e("judgeHasFaceImage", "judgeHasFaceImage ues time: " + (end - begin));
        if(faceDetectResults!=null) {

            LightHelper.openLight();

            final int[] box = faceDetectResults.get(0).getBox();
            int h = bitmap.getHeight();
            int w = bitmap.getWidth();
            // 坐标点需要进行翻转
//           右上  左下
            for(int j=0; j<box.length;j++){
                Log.i(TAG, "faceDetectResults final:" + j + "box:" + box[j]);
            }

            final int x1;
            final int x2;
            final int y1;
            final int y2;
            // 如果是纷翔的设备

            int reversal_x1 = w - box[0];
//
            int reversal_x2 = w - box[2];

//                Log.i(TAG, "faceDetectResults:height===" + bitmap.getHeight());
//                Log.i(TAG, "faceDetectResults:width===" + bitmap.getWidth());
//                Log.i(TAG, "faceDetectResults:layout_frame height===" + layout_frame.getHeight());
//                Log.i(TAG, "faceDetectResults:layout_frame width===" + layout_frame.getWidth());

            float xRate = layout_frame.getWidth() * 1.0f / bitmap.getWidth();
            float yRate = layout_frame.getHeight() * 1.0f / bitmap.getHeight();

            Log.i(TAG, "faceDetectResults:xRate " + xRate);
            Log.i(TAG, "faceDetectResults:yRate " + yRate);
            Log.i(TAG, "faceDetectResults:process x" + box[0] * xRate);
            for (int j = 0; j < box.length; j++) {
                Log.i(TAG, "faceDetectResults:" + j + "box:" + box[j]);
            }

            // x1 y1 左上角
            // x2 y2 右下角

            // reversal_x1  y1 是右上角
            // reversal_x2  y2 是左下角
            // 要做转换
            x1 = (int) (reversal_x2 * xRate);
            x2 = (int) (reversal_x1 * xRate);

            y1 = (int) (box[1] * yRate);
            y2 = (int) (box[3] * yRate);

            Log.i(TAG, "faceDetectResults x1:" + String.valueOf(x1));
            Log.i(TAG, "faceDetectResults y1:" + String.valueOf(y1));
            Log.i(TAG, "faceDetectResults x2:" + String.valueOf(x2));
            Log.i(TAG, "faceDetectResults y2:" + String.valueOf(y2));

            final int radioW = x2 - x1;
            final int radioH = y2 - y1;


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    redFace.setRadio(radioW, radioH);
                    redFace.setPoint(x1, y1, x2, y2);
                    redFace.setCanSee(true);
                    // 重新绘制
                    redFace.invalidate();
                }
            });
        }else{
            LightHelper.closeLight();
            if(redFace.getCanSee()){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        redFace.setCanSee(false);
                        // 重新绘制
                        redFace.invalidate();
                    }
                });
            }
        }
        return faceDetectResults;
    }

    /**
     * 开始线程
     * @return
     */
    public void startThreadToCompare(final Bitmap cameraBitmap, final ArrayList<DetectResult> cameraBitmapResult) {
        // 如果没有身份证信息, 则不比对
//        Log.i(TAG, "startThreadToCompare....");

        // 锁住 currentIdCardInfo 对象
        // 此处要锁定currentIdCardInfo变量，在读取的时候不能被另个一个线程清理了就尴尬了
        synchronized (this) {
            if (faceKit == null) {
                return;
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    public void setDateTextView(String content){
        dateText.setText(content);
    }
}

