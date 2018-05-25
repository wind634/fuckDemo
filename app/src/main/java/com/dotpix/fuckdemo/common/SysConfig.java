package com.dotpix.fuckdemo.common;

import android.os.Environment;

/**
 * Created by wangjiang on 2018/5/22.
 */

public class SysConfig {

    public static String COMPARE_TAG = "FuckLog";

    public static String ERROR_TAG = "ErrorLog";

    // 空闲时图像抓拍间隔 1 ms 毫秒为单位
    public static int WaitFacePictureInvertalTime = 300;


    // 每隔几秒切换一张图片
    public static int SwitchImageInvertalTime = 5;


    // 周期内的第几秒开始进行比对
    public static float CompareImageDelayTime = 2f;

    // 比对持续时长
    public static float CompareSustainedTime = 1f;

    // 待比对图片存放的路径
    public static String compareImageDir = Environment.getExternalStorageDirectory() +  "/compareDir";

    // 比对结果路径
    public static String resultDestDir = Environment.getExternalStorageDirectory() +  "/destDir";
    public static String resultDestFileName= "result.xls";

    // 抓拍的图片处理路径
    public static String caputerFaceImageDir = Environment.getExternalStorageDirectory() +  "/faceDir";
}
