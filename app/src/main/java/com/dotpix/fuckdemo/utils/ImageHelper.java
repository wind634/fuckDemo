package com.dotpix.fuckdemo.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Environment;

import com.dotpix.fuckdemo.common.SysConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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


    public static String saveBitmapToPath(Bitmap bitmap, String fileName) {
        if (bitmap == null) {
            return "";
        }
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) // 判断是否可以对SDcard进行操作
        {
            // 获取SDCard指定目录下
            String sdCardDir = SysConfig.caputerFaceImageDir;
            File dirFile = new File(sdCardDir);  //目录转化成文件夹
            if (!dirFile.exists()) {              //如果不存在，那就建立这个文件夹
                dirFile.mkdirs();
            }                          //文件夹有啦，就可以保存图片啦

            String filePath = sdCardDir + "/" + fileName;
            File file = new File(sdCardDir, filePath);// 在SDcard的目录下创建图片文,以当前时间为其命名
            if(!file.exists()){
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                System.out.println("_________保存到____sd______指定目录文件夹下____________________");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return filePath;
        }else {
            return "";
        }
    }


    public static Bitmap cropBitmapFunc(Bitmap cameraBitmap, int[] box){
        int centerx = box[0] + (box[2] - box[0]) / 2;
        int centery = box[1] + (box[3] - box[1]) / 2;

        int x1 = (int) (centerx - (centerx - box[0]) * SysConfig.CROP_FACE_IMAGE_TAP);
        int y1 = (int) (centery - (centery - box[1]) * SysConfig.CROP_FACE_IMAGE_TAP);
        if (x1 <= 0) {
            x1 = 0;
        }
        if (y1 <= 0) {
            y1 = 0;
        }

        int x2 = (int) (centerx + (centerx - box[0]) * SysConfig.CROP_FACE_IMAGE_TAP);
        int y2 = (int) (centery + (centery - box[1]) * SysConfig.CROP_FACE_IMAGE_TAP);
        if (x2 >= cameraBitmap.getWidth()) {
            x2 = cameraBitmap.getWidth();
        }
        if (y2 >= cameraBitmap.getHeight()) {
            y2 = cameraBitmap.getHeight();
        }

        Bitmap cropCameraBitmap = Bitmap.createBitmap(cameraBitmap,
                x1,
                y1,
                x2 - x1,
                y2 - y1, null, false);
        return cropCameraBitmap;
    }

}
