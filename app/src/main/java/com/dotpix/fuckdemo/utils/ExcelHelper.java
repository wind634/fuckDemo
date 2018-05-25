package com.dotpix.fuckdemo.utils;

import android.util.Log;

import com.dotpix.fuckdemo.common.SysConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 * Created by wangjiang on 2018/5/25.
 */

public class ExcelHelper {
    public static final String TAG = "ExcelHelper";

    public static void  createExcel() {
        WritableWorkbook wwb=null;
        OutputStream os = null;
        try {
            File f = new File(SysConfig.resultDestDir);
            if(!f.exists()){
                f.mkdir();
            }

            os = new FileOutputStream(SysConfig.resultDestDir + "/" + SysConfig.resultDestFileName);
            wwb = Workbook.createWorkbook(os);
            WritableSheet sheet = wwb.createSheet("订单", 0);
//            sheet.

            wwb.write();
        }catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }finally {
            try {
                if(wwb!=null) {
                    wwb.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        }
    }
}
