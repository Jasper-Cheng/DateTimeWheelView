package com.example.datetimewheelview.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


import androidx.annotation.Nullable;

import com.example.datetimewheelview.Utils.DensityUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class DateTimeWheelView extends View {
    private ArrayList<String> yearList=new ArrayList<>();
    private ArrayList<String> monthList=new ArrayList<>();
    private int all_width= DensityUtil.getScreenWidth(getContext());
    private int all_height=DensityUtil.dip2px(getContext(),210);
    private float began_draw_year_position=all_height/2;
    private float began_draw_month_position=all_height/2;
    private float allow_all_year_area_up;
    private float allow_all_year_area_down;
    private float allow_all_month_area_up;
    private float allow_all_month_area_down;
    private float space_year_item=all_height/3;
    private float space_month_item=all_height/3;
    private boolean scroll_year_area=false;
    private boolean scroll_month_area=false;
    private float began_year_y;
    private float began_month_y;
    private long last_time1_year=0;
    private long last_time1_month=0;
    private int year_move_count=0;
    private int month_move_count=0;
    private float[] year_move_7=new float[7];
    private float[] month_move_7=new float[7];
    private Timer timer = new Timer();
    private Timer timer2 = new Timer();
    private int slide_time=0;
    private int slide_time2=0;
    private float basic_distance=100;
    private ArrayList<float[]> year_coordinate=new ArrayList<>();
    private ArrayList<float[]> month_coordinate=new ArrayList<>();
    private TimerTask task= new TimerTask() {
        @Override
        public void run() {

        }
    };
    private TimerTask task2= new TimerTask() {
        @Override
        public void run() {

        }
    };
    private OnTouchCallBack onTouchCallBack;
    private int current_year;
    private int current_month;

    public DateTimeWheelView(Context context) {
        super(context);
    }

    public DateTimeWheelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DateTimeWheelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setYearCount(int countYear){
        yearList.clear();
        monthList.clear();
        year_coordinate.clear();
        month_coordinate.clear();
        Calendar cd = Calendar.getInstance();
        int curYear=cd.get(Calendar.YEAR);
        for(int i=0;i<countYear;i++){
            yearList.add(curYear+"");
            year_coordinate.add(new float[2]);
            curYear--;
        }
        for(int i=0;i<12;i++){
            monthList.add(i+1+"");
            month_coordinate.add(new float[2]);
        }
        allow_all_year_area_up= space_year_item * (yearList.size()-1);
        allow_all_month_area_up= space_month_item * (monthList.size()-1);
    }

    public interface OnTouchCallBack {
        void callBack(int year, int month);
    }
    public void onTouchEventListener(OnTouchCallBack callBack){
        this.onTouchCallBack=callBack;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
//
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
//        int width;
//        int height;
//        if (widthMode == MeasureSpec.EXACTLY) {
//            width = widthSize;
//        } else {
//            width = all_width;
//        }
//        if (heightMode == MeasureSpec.EXACTLY) {
//            height = heightSize;
//        } else {
//            height = all_height;
//        }
        setMeasuredDimension(all_width, all_height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                task.cancel();
                task2.cancel();
                float down_x=event.getX();
                began_year_y=event.getY();
                began_month_y=event.getY();
                if(down_x<all_width/2){
                    scroll_year_area=true;
                    scroll_month_area=false;
                }else{
                    scroll_year_area=false;
                    scroll_month_area=true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                last_time1_year=System.currentTimeMillis();
                last_time1_month=System.currentTimeMillis();
                if(scroll_year_area){
                    float scroll_distance=event.getY()-began_year_y;
                    if(scroll_distance>0){
                        float up_distance= (float) (allow_all_year_area_up-scroll_distance*0.1);
                        if(up_distance<0){
                            began_draw_year_position= began_draw_year_position+allow_all_year_area_up;
                            allow_all_year_area_up=0;
                            allow_all_year_area_down= space_year_item * (yearList.size()-1);
                        }else{
                            began_draw_year_position= (float) (began_draw_year_position+scroll_distance*0.1);
                            allow_all_year_area_up=up_distance;
                            allow_all_year_area_down= (float) (allow_all_year_area_down+scroll_distance*0.1);
                        }
                    }else{
                        float down_distance= (float) (allow_all_year_area_down+scroll_distance*0.1);
                        if(down_distance<0){
                            began_draw_year_position= began_draw_year_position-allow_all_year_area_down;
                            allow_all_year_area_down=0;
                            allow_all_year_area_up= space_year_item * (yearList.size()-1);
                        }else{
                            began_draw_year_position= (float) (began_draw_year_position+scroll_distance*0.1);
                            allow_all_year_area_down=down_distance;
                            allow_all_year_area_up= (float) (allow_all_year_area_up-scroll_distance*0.1);
                        }
                    }
                    began_year_y= (float) (began_year_y+scroll_distance*0.1);
                    invalidate();
                    year_move_7[year_move_count%7]=event.getY();
                    year_move_count++;
                }else{
                    float scroll_distance=event.getY()-began_month_y;
                    if(scroll_distance>0){
                        float up_distance= (float) (allow_all_month_area_up-scroll_distance*0.1);
                        if(up_distance<0){
                            began_draw_month_position= began_draw_month_position+allow_all_month_area_up;
                            allow_all_month_area_up=0;
                            allow_all_month_area_down= space_month_item * (monthList.size()-1);
                        }else{
                            began_draw_month_position= (float) (began_draw_month_position+scroll_distance*0.1);
                            allow_all_month_area_up=up_distance;
                            allow_all_month_area_down= (float) (allow_all_month_area_down+scroll_distance*0.1);
                        }
                    }else{
                        float down_distance= (float) (allow_all_month_area_down+scroll_distance*0.1);
                        if(down_distance<0){
                            began_draw_month_position= began_draw_month_position-allow_all_month_area_down;
                            allow_all_month_area_down=0;
                            allow_all_month_area_up= space_month_item * (monthList.size()-1);
                        }else{
                            began_draw_month_position= (float) (began_draw_month_position+scroll_distance*0.1);
                            allow_all_month_area_down=down_distance;
                            allow_all_month_area_up= (float) (allow_all_month_area_up-scroll_distance*0.1);
                        }
                    }
                    began_month_y= (float) (began_month_y+scroll_distance*0.1);
                    invalidate();
                    month_move_7[month_move_count%7]=event.getY();
                    month_move_count++;
                }
                break;
            case MotionEvent.ACTION_UP:
                slide_time=0;
                slide_time2=0;
                long last_time2_year=System.currentTimeMillis();
                long last_time2_month=System.currentTimeMillis();
                if(scroll_year_area){
                    if(Math.abs(event.getY()-began_year_y)<10){
                        caluNewClick(event.getY());
                        invalidate();
                        break;
                    }
                    float scroll_distance=event.getY()-year_move_7[2];
                    float a=scroll_distance/(last_time2_year-last_time1_year);
                    if(Float.isInfinite(a)||Float.isNaN(a)){
                        caluNewDistance();
                        invalidate();
                        break;
                    }
                    if(a>35||a<-35){
                        slide_time= (int) (Math.abs(a)*3);
                        task = new TimerTask() {
                            @Override
                            public void run() {
                                try {
                                    Message message=new Message();
                                    message.what=200;
                                    message.obj=scroll_distance;
                                    handler.sendMessage(message);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        timer.schedule(task,0,50);
                    }else{
                        caluNewDistance();
                        invalidate();
                    }
                }else{
                    if(Math.abs(event.getY()-began_month_y)<10){
                        caluNewClick2(event.getY());
                        invalidate();
                        break;
                    }
                    float scroll_distance=event.getY()-month_move_7[2];
                    float a=scroll_distance/(last_time2_month-last_time1_month);
                    if(Float.isInfinite(a)||Float.isNaN(a)){
                        caluNewDistance2();
                        invalidate();
                        break;
                    }
                    if(a>35||a<-35){
                        slide_time2= (int) (Math.abs(a)*3);
                        task2 = new TimerTask() {
                            @Override
                            public void run() {
                                try {
                                    Message message=new Message();
                                    message.what=300;
                                    message.obj=scroll_distance;
                                    handler.sendMessage(message);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        timer2.schedule(task2,0,50);
                    }else{
                        caluNewDistance2();
                        invalidate();
                    }
                }
                break;
            default:
                break;
        }

        return true;
    }

    public void caluNewDistance(){
        float line_y1=all_height/2-space_year_item/2;
        float line_y2=all_height/2+space_year_item/2;
        for(int i=0;i<year_coordinate.size();i++){
            if(line_y1<year_coordinate.get(i)[1]&&year_coordinate.get(i)[1]<line_y2){
                began_draw_year_position=all_height/2+i*space_year_item;
                allow_all_year_area_up=space_year_item * (yearList.size()-i-1);
                allow_all_year_area_down=space_year_item * i;
                return;
            }
        }
    }

    public void caluNewDistance2(){
        float line_y1=all_height/2-space_month_item/2;
        float line_y2=all_height/2+space_month_item/2;
        for(int i=0;i<month_coordinate.size();i++){
            if(line_y1<month_coordinate.get(i)[1]&&month_coordinate.get(i)[1]<line_y2){
                began_draw_month_position=all_height/2+i*space_month_item;
                allow_all_month_area_up=space_month_item * (monthList.size()-i-1);
                allow_all_month_area_down=space_month_item * i;
                return;
            }
        }
    }

    public void caluNewClick(float y){
        for(int i=0;i<year_coordinate.size();i++){
            float began=year_coordinate.get(i)[1]-space_year_item/2;
            float end=year_coordinate.get(i)[1]+space_year_item/2;
            if(y>=began&&end>=y){
                began_draw_year_position=all_height/2+i*space_year_item;
                allow_all_year_area_up=space_year_item * (yearList.size()-i-1);
                allow_all_year_area_down=space_year_item * i;
                return;
            }
        }
    }

    public void caluNewClick2(float y){
        for(int i=0;i<month_coordinate.size();i++){
            float began=month_coordinate.get(i)[1]-space_month_item/2;
            float end=month_coordinate.get(i)[1]+space_month_item/2;
            if(y>=began&&end>=y){
                began_draw_month_position=all_height/2+i*space_month_item;
                allow_all_month_area_up=space_month_item * (monthList.size()-i-1);
                allow_all_month_area_down=space_month_item * i;
                return;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画双横线
        Paint line_paint=new Paint();
        line_paint.setColor(Color.parseColor("#F3F3F5"));
        float line_x=getMeasuredWidth();
        float line_y1=all_height/2-space_year_item/2;
        float line_y2=all_height/2+space_year_item/2;
        canvas.drawLine(0,line_y1,line_x,line_y1,line_paint);
        canvas.drawLine(0,line_y2,line_x,line_y2,line_paint);

        //画年份
        Paint unYear_paint=new Paint();
        unYear_paint.setTextSize(50);
        Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
        Typeface font2 = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);
        float unYear_x=(float) (getMeasuredWidth()/2*0.5);
        float unYear_y;
        for(int i=0;i<yearList.size();i++){
            unYear_y=began_draw_year_position-i*space_year_item+(unYear_paint.getFontMetrics().descent - unYear_paint.getFontMetrics().ascent) / 2.0F;
            float[] a=new float[2];
            a[0]=unYear_x;
            a[1]=unYear_y;
            year_coordinate.set(i,a);
            if(line_y1<year_coordinate.get(i)[1]&&year_coordinate.get(i)[1]<line_y2){
                unYear_paint.setColor(Color.parseColor("#1B6CFF"));
                unYear_paint.setTypeface(font);
                current_year=Integer.parseInt(yearList.get(i));
            }else{
                unYear_paint.setColor(Color.parseColor("#858E9E"));
                unYear_paint.setTypeface(font2);
            }

            canvas.drawText(yearList.get(i),unYear_x,unYear_y,unYear_paint);
        }

        //画月份
        Paint unMonth_paint=new Paint();
        unMonth_paint.setTextSize(50);
        Typeface font3 = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
        Typeface font4 = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);
        float unMonth_x=(float) (getMeasuredWidth()*0.7);
        float unMonth_y;
        for(int i=0;i<monthList.size();i++){
            unMonth_y=began_draw_month_position-i*space_month_item+(unMonth_paint.getFontMetrics().descent - unMonth_paint.getFontMetrics().ascent) / 2.0F;
            float[] a=new float[2];
            a[0]=unMonth_x;
            a[1]=unMonth_y;
            month_coordinate.set(i,a);
            if(line_y1<month_coordinate.get(i)[1]&&month_coordinate.get(i)[1]<line_y2){
                unMonth_paint.setColor(Color.parseColor("#1B6CFF"));
                unMonth_paint.setTypeface(font3);
                current_month=Integer.parseInt(monthList.get(i));
            }else{
                unMonth_paint.setColor(Color.parseColor("#858E9E"));
                unMonth_paint.setTypeface(font4);
            }
            canvas.drawText(monthList.get(i),unMonth_x,unMonth_y,unMonth_paint);
        }
        onTouchCallBack.callBack(current_year,current_month);
    }

    Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch(msg.what){
                case 200:
                    slide_time=slide_time-50;
                    if(slide_time>0){
                        float scroll_distance= (float) msg.obj;
                        if(scroll_distance>0){
                            float up_distance= (float) (allow_all_year_area_up-basic_distance);
                            if(up_distance<0){
                                began_draw_year_position= began_draw_year_position+allow_all_year_area_up;
                                allow_all_year_area_up=0;
                                allow_all_year_area_down= space_year_item * (yearList.size()-1);
                            }else{
                                began_draw_year_position= (float) (began_draw_year_position+basic_distance);
                                allow_all_year_area_up=up_distance;
                                allow_all_year_area_down= (float) (allow_all_year_area_down+basic_distance);
                            }
                        }else{
                            float down_distance= (float) (allow_all_year_area_down-basic_distance);
                            if(down_distance<0){
                                began_draw_year_position= began_draw_year_position-allow_all_year_area_down;
                                allow_all_year_area_down=0;
                                allow_all_year_area_up= space_year_item * (yearList.size()-1);
                            }else{
                                began_draw_year_position= (float) (began_draw_year_position-basic_distance);
                                allow_all_year_area_down=down_distance;
                                allow_all_year_area_up= (float) (allow_all_year_area_up+basic_distance);
                            }
                        }
                    }else{
                        caluNewDistance();
                        task.cancel();
                    }
                    invalidate();
                    break;
                case 300:
                    slide_time2=slide_time2-50;
                    if(slide_time2>0){
                        float scroll_distance= (float) msg.obj;
                        if(scroll_distance>0){
                            float up_distance= (float) (allow_all_month_area_up-basic_distance);
                            if(up_distance<0){
                                began_draw_month_position= began_draw_month_position+allow_all_month_area_up;
                                allow_all_month_area_up=0;
                                allow_all_month_area_down= space_month_item * (monthList.size()-1);
                            }else{
                                began_draw_month_position= (float) (began_draw_month_position+basic_distance);
                                allow_all_month_area_up=up_distance;
                                allow_all_month_area_down= (float) (allow_all_month_area_down+basic_distance);
                            }
                        }else{
                            float down_distance= (float) (allow_all_month_area_down-basic_distance);
                            if(down_distance<0){
                                began_draw_month_position= began_draw_month_position-allow_all_month_area_down;
                                allow_all_month_area_down=0;
                                allow_all_month_area_up= space_month_item * (monthList.size()-1);
                            }else{
                                began_draw_month_position= (float) (began_draw_month_position-basic_distance);
                                allow_all_month_area_down=down_distance;
                                allow_all_month_area_up= (float) (allow_all_month_area_up+basic_distance);
                            }
                        }
                    }else{
                        caluNewDistance2();
                        task2.cancel();
                    }
                    invalidate();
                    break;
            }
        }
    };
}
