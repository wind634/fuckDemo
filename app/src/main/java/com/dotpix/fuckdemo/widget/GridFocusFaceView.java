package com.dotpix.fuckdemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.dotpix.fuckdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjiang on 17/12/8.
 * 检测人脸的红色方框线的vie
 */

public class GridFocusFaceView extends View {

    Paint  paint;  //绘图

    Paint  paint2;
    private List<Point> fourPointList;

    private int edageLength = 50;

    private Context mContext;
//    private int gap = 50;

    // 自定义偏移量
    private int gap = 0;

    private  Boolean canSee=false;


    private int radioH = -1;
    private int radioW = -1;

    public GridFocusFaceView(Context context) {
        super(context);
        mContext = context;
        // TODO Auto-generated constructor stub
        initPaint();
    }

    public GridFocusFaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initPaint();
    }

    private void initPaint(){
        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        paint2 = new Paint();
        paint2.setColor(mContext.getResources().getColor(R.color.transColorGray1));
        paint2.setTextSize(25);
        paint2.setTextAlign(Paint.Align.CENTER);

        fourPointList = new ArrayList<Point>();

        // 左上
        fourPointList.add(new Point(10,10));
        // 右上
        fourPointList.add(new Point(100,10));
        // 左下
        fourPointList.add(new Point(10,300));
        // 右下
        fourPointList.add(new Point(100,300));
    }


    public void setCanSee(Boolean canSee){
        this.canSee = canSee;
    }

    public  Boolean getCanSee(){
        return this.canSee;
    }


    public void setPoint(int x1, int y1, int x2, int y2){
            // 左上
            Point point1 = fourPointList.get(0);
            point1.x = x1 + gap;
            point1.y = y1 + gap;
            // 右上
            Point point2 = fourPointList.get(1);
            point2.x = x2 + gap;
            point2.y = y1 + gap;
            // 左下
            Point point3 = fourPointList.get(2);
            point3.x = x1 + gap;
            point3.y = y2 + gap;
            // 右下
            Point point4 = fourPointList.get(3);
            point4.x = x2 + gap;
            point4.y = y2 + gap;

    }


    /**
     * 绘制网格线
     */
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        if(canSee) {

            // 左上
            Point point1 = fourPointList.get(0);
            // 右上
            Point point2 = fourPointList.get(1);
            // 左下
            Point point3 = fourPointList.get(2);
            // 右下
            Point point4 = fourPointList.get(3);

            int endX1 = point1.x + edageLength;
            int endY1 = point1.y + edageLength;
            canvas.drawLine(point1.x, point1.y, endX1, point1.y, paint);
            canvas.drawLine(point1.x, point1.y, point1.x, endY1, paint);

            int endX2 = point2.x - edageLength;
            int endY2 = point2.y + edageLength;
            canvas.drawLine(point2.x, point2.y, endX2, point2.y, paint);
            canvas.drawLine(point2.x, point2.y, point2.x, endY2, paint);


            int endX3 = point3.x + edageLength;
            int endY3 = point3.y - edageLength;
            canvas.drawLine(point3.x, point3.y, endX3, point3.y, paint);
            canvas.drawLine(point3.x, point3.y, point3.x, endY3, paint);

            int endX4 = point4.x - edageLength;
            int endY4 = point4.y - edageLength;
            canvas.drawLine(point4.x, point4.y, endX4, point4.y, paint);
            canvas.drawLine(point4.x, point4.y, point4.x, endY4, paint);

//            canvas.drawText("请刷身份证", (point2.x + point1.x) / 2, point1.y + 10, paint2);
            // 显示人脸分辨率信息
//            if(this.radioW!=-1 && this.radioH!=-1) {
//                canvas.drawText(this.radioW + " * " + this.radioH, (point2.x + point1.x) / 2, point3.y + 10, paint2);
//            }

        }
    }


    class Point{
        private int x;
        private int y;
        public Point(int x, int y){
            this.x = x;
            this.y = y;
        }
    }


    public void setRadio(int radioW, int radioH){
        this.radioW = radioW;
        this.radioH = radioH;
    }
}

