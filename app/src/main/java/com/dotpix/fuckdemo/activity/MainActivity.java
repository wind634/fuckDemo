package com.dotpix.fuckdemo.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dotpix.fuckdemo.R;
import com.dotpix.fuckdemo.common.SysConfig;
import com.dotpix.fuckdemo.fragment.Camera2BasicFragment;
import com.dotpix.fuckdemo.model.Record;
import com.dotpix.fuckdemo.tasks.CompareTask;
import com.dotpix.fuckdemo.tasks.SwitchImageTask;
import com.dotpix.fuckdemo.utils.ExcelHelper;
import com.dotpix.fuckdemo.utils.ImageHelper;
import com.dotpix.fuckdemo.utils.LightHelper;
import com.dotpix.fuckdemo.widget.GridFocusFaceView;
import com.dyhdyh.widget.loading.dialog.LoadingDialog;
import com.pixtalks.detect.DetectResult;
import com.pixtalks.facekitsdk.FaceKit;
import com.pixtalks.facekitsdk.PConfig;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy_MM_dd_HHmmss");

    public static boolean shouldRunCompare=true;
    public FaceKit faceKit=null;
    private GridFocusFaceView redFace;
    private RelativeLayout layout_frame;
    private Dialog initDialog;
    private Button startCompareBtn;
    private TextView dateText;
    private ImageView showImage;
    private TextView logText;
    private TextView descText;

    public static List<String> imagePathList;

    // 是否正在识别
    private boolean isReg=false;
    private SwitchImageTask switchImageTask;
    private CompareTask compareTask;
    private Timer timer;

    public static int currentCompareImageIndex = 0;
    private String currentExcelName = "";

    private Bitmap currentFaceBitmap = null;
    private float[]  currentFaceBitmapFeature = null;

    private float[] currentComparedBitmapFeature = null;

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

        showImage = (ImageView)findViewById(R.id.showImage);

        logText = (TextView) findViewById(R.id.logText);
        descText = (TextView) findViewById(R.id.descText);

    }

    public void startReg(){
        startCompareBtn.setText("停止识别");
        imagePathList = ImageHelper.getImagePathFromSD();
        setImage(0);
        setRedLogText(getDateTextView() + "   " + "切换到第" + 0 +"张图片");

        compareTask = new CompareTask(MainActivity.this, 0);
        timer.schedule(compareTask, (long) (SysConfig.CompareImageDelayTime * 1000));

        isReg = true;

        currentExcelName = ExcelHelper.createExcelFileName();
        ExcelHelper.createExcel(currentExcelName);

        switchImageTask = new SwitchImageTask(MainActivity.this);
        timer.schedule(switchImageTask,0,1000);



    }

    public void endReg(){
        startCompareBtn.setText("开始识别");
        isReg = false;
        if (!switchImageTask.cancel()){
            switchImageTask.cancel();
            timer.cancel();
        }
        setDateTextView("00:00:00");
        showImage.setImageDrawable(null);
        logText.setText("未开始比对");
        descText.setText("处理完成");

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


    public void setCurrentFaceData(final Bitmap cameraBitmap, final ArrayList<DetectResult> cameraBitmapResult) {
        Log.i(TAG, "setCurrentFaceData....");
        Bitmap cropBitmap = ImageHelper.cropBitmapFunc(cameraBitmap, cameraBitmapResult.get(0).getBox());
        if(cropBitmap!=null){
            this.currentFaceBitmap = cropBitmap;
        } else {
            this.currentFaceBitmap = cameraBitmap;
        }

        this.currentFaceBitmapFeature = faceKit.getFeatureByDetectResult(cameraBitmap, cameraBitmapResult.get(0));

    }

    public float startToCompare( ) {
        // 说明底库图片无人脸
        if(currentComparedBitmapFeature==null) {
            Log.i(TAG, "底库图片无人脸 endToCompare....");
            Log.e(SysConfig.COMPARE_TAG, "compare image no face....");
            return  -1;
        }else{
            // 说明照相机未抓拍到人脸
            if(currentFaceBitmapFeature==null){
                Log.e(SysConfig.COMPARE_TAG, "camrea no find face....");
                return -2;
            }else {
                Log.e(SysConfig.COMPARE_TAG, "start to  compareScore....");
                Float score = faceKit.compareScore(currentFaceBitmapFeature, currentComparedBitmapFeature);
                Log.e(SysConfig.COMPARE_TAG, "score:" + score);
                return score;
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

    public String getDateTextView(){
        return dateText.getText().toString();
    }

    public void  setImage(int index){
        currentCompareImageIndex = index;
        Glide.with(MainActivity.this).
                load(imagePathList.get(index)).into(showImage);
        descText.setText("总共要处理"+imagePathList.size()+"张图片, 现在处理到第"+(index+1)+"张");

        Bitmap compareImageBitmap = BitmapFactory.decodeFile(imagePathList.get(index));
        ArrayList<DetectResult>  compareImageResult = faceKit.detectFace(compareImageBitmap);
        if(compareImageResult!=null){
            currentComparedBitmapFeature = faceKit.getFeatureByDetectResult(compareImageBitmap, compareImageResult.get(0));
        }else {
            currentComparedBitmapFeature = null;
        }

    }


    public void  setRedLogText(String content){
        logText.setTextColor(getResources().getColor(R.color.red));
        logText.setText(content);
    }

    public void  setBlueLogText(String content){
        logText.setTextColor(getResources().getColor(R.color.colorBlue));
        logText.setText(content);
    }


    public void  addRecord2Excel(Record record){
        ExcelHelper.addRecord2Excel(currentExcelName, record);
    }

    public String  saveFaceBitmap(){
        String[] pathList = imagePathList.get(currentCompareImageIndex).split("/");
        String imageName =  pathList[pathList.length-1];
        String faceImageName = "cameraFace_" + formatter2.format(new Date()) + "_____" + imageName;
        return ImageHelper.saveBitmapToPath(currentFaceBitmap, faceImageName);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LightHelper.closeLight();
    }
}


