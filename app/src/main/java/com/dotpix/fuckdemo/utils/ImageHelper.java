package com.dotpix.fuckdemo.utils;

import android.annotation.SuppressLint;

import com.dotpix.fuckdemo.common.SysConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjiang on 2018/5/25.
 */

public class ImageHelper {

    /**
     * 从sd卡获取图片资源
     * @return
     */
    public static List<String> getImagePathFromSD() {
        // 图片列表
        List<String> imagePathList = new ArrayList<String>();
        // 得到sd卡内image文件夹的路径   File.separator(/)

        // 得到该路径文件夹下所有的文件
        File fileAll = new File(SysConfig.compareImageDir);
        if(!fileAll.exists()){
            fileAll.mkdir();
        }

        File[] files = fileAll.listFiles();
        // 将所有的文件存入ArrayList中,并过滤所有图片格式的文件
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (checkIsImageFile(file.getPath())) {
                imagePathList.add(file.getPath());
            }
        }
        // 返回得到的图片列表
        return imagePathList;
    }

    /**
     * 检查扩展名，得到图片格式的文件
     * @param fName  文件名
     * @return
     */
    @SuppressLint("DefaultLocale")
    public static boolean checkIsImageFile(String fName) {
        boolean isImageFile = false;
        // 获取扩展名
        String FileEnd = fName.substring(fName.lastIndexOf(".") + 1,
                fName.length()).toLowerCase();
        if (FileEnd.equals("jpg") || FileEnd.equals("png") || FileEnd.equals("gif")
                || FileEnd.equals("jpeg")|| FileEnd.equals("bmp") ) {
            isImageFile = true;
        } else {
            isImageFile = false;
        }
        return isImageFile;
    }

}
