package com.dotpix.fuckdemo.utils;

import android.annotation.SuppressLint;
import android.util.Log;

import com.dotpix.fuckdemo.common.SysConfig;
import com.dotpix.fuckdemo.model.Record;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import jxl.Workbook;
import jxl.write.WritableWorkbook;

/**
 * Created by wangjiang on 2018/5/25.
 */

public class ExcelHelper {
    public static final String TAG = "ExcelHelper";

    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String  createExcelFileName() {
        String excelName = formatter1.format(new Date()) + "_" + SysConfig.resultDestFileName;
        return excelName;
    }

    public static void  createExcel(String excelName) {
        WritableWorkbook wwb=null;
        OutputStream os = null;
        try {
            File f = new File(SysConfig.resultDestDir);
            if(!f.exists()){
                f.mkdir();
            }

            os = new FileOutputStream(SysConfig.resultDestDir + "/" + excelName);
            wwb = Workbook.createWorkbook(os);
            wwb.createSheet("人脸比对结果表格", 0);
            wwb.write();
        }catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }finally {
            try {
                if(wwb!=null) {
                    wwb.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void  addRecord2Excel(String excelName, Record record) {

    }
}
