package com.dotpix.fuckdemo.model;

/**
 * Created by wangjiang on 2018/5/25.
 */

public class Record {

    private String compareImageName="";
    private String compareImagePath="";
    private String faceImagePath="";
    private float score=0.0f;
    private String compareTime="";
    private String ext="";


    public Record(){

    }

    public String getCompareImageName() {
        return compareImageName;
    }

    public void setCompareImageName(String compareImageName) {
        this.compareImageName = compareImageName;
    }

    public String getCompareImagePath() {
        return compareImagePath;
    }

    public void setCompareImagePath(String compareImagePath) {
        this.compareImagePath = compareImagePath;
    }

    public String getFaceImagePath() {
        return faceImagePath;
    }

    public void setFaceImagePath(String faceImagePath) {
        this.faceImagePath = faceImagePath;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getCompareTime() {
        return compareTime;
    }

    public void setCompareTime(String compareTime) {
        this.compareTime = compareTime;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }
}
