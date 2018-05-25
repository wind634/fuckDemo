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
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * Created by wangjiang on 2018/5/25.
 */

public class ExcelHelper {
    public static final String TAG = "ExcelHelper";

    public static final int CLOUMN_SIZE = 3;

    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy_MM_dd_HHmmss");

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
            WritableSheet sheet = wwb.createSheet("人脸比对结果表格", 0);

//            for (int i = 0; i < 3; i++) {
//                // Label(x,y,z) 代表单元格的第x+1列，第y+1行, 内容z
//                // 在Label对象的子对象中指明单元格的位置和内容
            Label  label = new Label(0, 0, "id");
                // 将定义好的单元格添加到工作表中
            sheet.addCell(label);
//            }
            wwb.write();
        }catch (Exception e) {
            e.printStackTrace();
            Log.e(SysConfig.ERROR_TAG, e.toString());
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
        Workbook wwb=null;
        WritableWorkbook book =null;
        File file = null;
        try {
            file = new File(SysConfig.resultDestDir + "/" + excelName);
            wwb = Workbook.getWorkbook(file);
            book = Workbook.createWorkbook(file, wwb);
            WritableSheet sheet = book.getSheet(0);

            int size = sheet.getRows();
            // 先写入id
            Label  label = new Label(0, size, String.valueOf(size));
            sheet.addCell(label);

            book.write();
        }catch (Exception e) {
            e.printStackTrace();
            Log.e(SysConfig.ERROR_TAG, e.toString());
        }finally {
            try {
                if(wwb!=null) {
                    wwb.close();
                }
                if(book!=null) {
                    book.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
