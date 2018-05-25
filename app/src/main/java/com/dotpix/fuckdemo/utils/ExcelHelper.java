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


            Label  label1 = new Label(0, 0, "序号");
            // 将定义好的单元格添加到工作表中
            sheet.addCell(label1);

            Label  label2 = new Label(1, 0, "compare比对的图片名称");
            sheet.addCell(label2);

            Label  label3 = new Label(2, 0, "compare比对的图片路径");
            sheet.addCell(label3);

            Label  label4 = new Label(3, 0, "抓拍的人脸图像路径");
            sheet.addCell(label4);

            Label  label5 = new Label(4, 0, "比对得分");
            sheet.addCell(label5);

            Label  label6 = new Label(5, 0, "比对时间");
            sheet.addCell(label6);

            Label  label7 = new Label(6, 0, "备注");
            sheet.addCell(label7);

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
            Label  label1 = new Label(0, size, String.valueOf(size));
            sheet.addCell(label1);

            Label  label2 = new Label(1, size, record.getCompareImageName());
            sheet.addCell(label2);

            Label  label3 = new Label(2, size, record.getCompareImagePath());
            sheet.addCell(label3);

            Label  label4 = new Label(3, size, record.getFaceImagePath());
            sheet.addCell(label4);

            Label  label5 = new Label(4, size, String.valueOf(record.getScore()));
            sheet.addCell(label5);

            Label  label6 = new Label(5, size, record.getCompareTime());
            sheet.addCell(label6);

            Label  label7 = new Label(6, size, record.getExt());
            sheet.addCell(label7);

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
