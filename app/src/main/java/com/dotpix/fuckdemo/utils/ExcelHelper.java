package com.dotpix.fuckdemo.utils;

import com.dotpix.fuckdemo.common.SysConfig;

import java.io.FileNotFoundException;
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

    public static void  createExcel() {
        WritableWorkbook wwb=null;
        OutputStream os = null;
        try {
            os = new FileOutputStream(SysConfig.resultDestFile);
            wwb = Workbook.createWorkbook(os);
            WritableSheet sheet = wwb.createSheet("订单", 0);
//            sheet.

            wwb.write();
        }catch (FileNotFoundException e) {
                e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
