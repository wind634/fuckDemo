package com.dotpix.fuckdemo.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by wangjiang on 2018/1/15.
 */

public class LightHelper {

    public static void openLight(){
        //开
        doWriteFile_led("/sys/class/rk29-keypad/gpio2", "1");
    }

    public static void closeLight(){
        //关
        doWriteFile_led("/sys/class/rk29-keypad/gpio2", "0");
    }

    private static void doWriteFile_led(String filename, String str) {
        try {
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(filename));
            osw.write(str);//, 0, TEST_STRING.length());
            osw.flush();
            osw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
