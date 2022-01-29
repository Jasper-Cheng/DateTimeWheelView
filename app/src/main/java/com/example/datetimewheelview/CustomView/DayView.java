package com.example.datetimewheelview.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.example.datetimewheelview.R;
import com.example.datetimewheelview.Utils.DateCell;

import java.util.Calendar;
import java.util.HashMap;


public class DayView extends View {
    private static final String TAG = "DayView";
    DateCell dateCell;
    DateCell dateCellStart;
    DateCell dateCellEnd;
    int weeks;
    int sumDays;
    int firstDayOfWeek;

    float dayViewHeight;
    float dayViewWidth;
    float bottomLineWidth;

    float clickPositionX, clickPositionY;

    Paint curDayBgPaint, dayTextPaint, bottomLinePaint;

    int curDayTextColor, commonDayTextColor, bottomLineColor, curDayBackgroundColor;
    float curDayTextSize, commonDayTextSize;

    HashMap<String, float[]> map = new HashMap<>(31);
    OnClickDayListener listener;
    private int DisplaySpecifiedWeek=-1;
    private Boolean DisplaySpecifiedBoolean=false;

    public void update(DateCell dateCell) {
        this.dateCell = dateCell;
        weeks = dateCell.getSumWeeksOfMonth();
        sumDays = dateCell.getSumDays();
        firstDayOfWeek = dateCell.getFirstDayOfWeek();
        map.clear();
    }

    public void setSelectDate(int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay) {
        dateCellStart.setDate(startYear, startMonth);
        dateCellStart.setDay(startDay);
        dateCellEnd.setDate(endYear, endMonth);
        dateCellEnd.setDay(endDay);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        map.clear();
    }

    public DayView(Builder builder, Context context) {
        this(context);
        bottomLineColor = builder.bottomLineColor;
        bottomLineWidth = builder.bottomLineWidth;
        curDayTextColor = builder.curDayTextColor;
        curDayTextSize = builder.curDayTextSize;
        commonDayTextColor = builder.commonDayTextColor;
        commonDayTextSize = builder.commonDayTextSize;
        curDayBackgroundColor = builder.curDayBackgroundColor;

        curDayBgPaint.setColor(curDayBackgroundColor);
        dayTextPaint.setColor(commonDayTextColor);
        dayTextPaint.setTextSize(commonDayTextSize);
        bottomLinePaint.setColor(bottomLineColor);
        bottomLinePaint.setStrokeWidth(bottomLineWidth);
    }

    public DayView(Context context) {
        this(context, null);
    }

    public DayView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        dateCell = new DateCell();
        dateCell.toCurrentDate();
        weeks = dateCell.getSumWeeksOfMonth();
        sumDays = dateCell.getSumDays();
        firstDayOfWeek = dateCell.getFirstDayOfWeek();

        dateCellStart = new DateCell();
        dateCellStart.setDate(0, 0);
        dateCellStart.setDay(0);
        dateCellEnd = new DateCell();
        dateCellEnd.setDate(0, 0);
        dateCellEnd.setDay(0);

        dayViewWidth = getResources().getDimension(R.dimen.dayview_width);
        dayViewHeight = getResources().getDimension(R.dimen.dayview_height);

        curDayTextColor = Color.BLUE;
        commonDayTextColor = Color.BLACK;
        curDayTextSize = getResources().getDimension(R.dimen.day_text_size);
        commonDayTextSize = getResources().getDimension(R.dimen.day_text_size);

        curDayBgPaint = new Paint();
        curDayBackgroundColor = getContext().getResources().getColor(R.color.color_1B6CFF);
        curDayBgPaint.setColor(curDayBackgroundColor);
        curDayBgPaint.setStyle(Paint.Style.FILL);
        curDayBgPaint.setAntiAlias(true);

        dayTextPaint = new Paint();
        dayTextPaint.setColor(commonDayTextColor);
        dayTextPaint.setAntiAlias(true);
        dayTextPaint.setTextSize(commonDayTextSize);

        bottomLinePaint = new Paint();
        bottomLineColor = Color.WHITE;  //ta.getColor(R.styleable.CalendarView_lineColor, Color.parseColor("#DCDCDC"));
        bottomLinePaint.setColor(bottomLineColor);
        bottomLinePaint.setAntiAlias(true);
        bottomLinePaint.setStrokeCap(Paint.Cap.ROUND);
        bottomLineWidth =  getResources().getDimension(R.dimen.bottomLineWidth);
        bottomLinePaint.setStrokeWidth(bottomLineWidth);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            int minWidth = Math.round(dayViewWidth * 7);
            width = widthSize != 0 ? Math.min(minWidth, widthSize) : Math.round(dayViewWidth * 7);
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            int minHeight;
            if(!DisplaySpecifiedBoolean){
                minHeight = Math.round(dayViewHeight * (weeks+1));
            }else{
                minHeight = 280;
            }
            height = heightSize != 0 ? Math.min(minHeight, heightSize) : minHeight;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final int saveCount = canvas.getSaveCount();
        canvas.save();

        float t_l=0;
        float t_r=0;
        Paint t_p=new Paint();
        t_p.setColor(getContext().getResources().getColor(R.color.color_858e9e));
        t_p.setTextSize(40);
        for(int i=0;i<7;i++){
            String w="日";
            switch (i){
                case 0:
                    w="日";
                    break;
                case 1:
                    w="一";
                    break;
                case 2:
                    w="二";
                    break;
                case 3:
                    w="三";
                    break;
                case 4:
                    w="四";
                    break;
                case 5:
                    w="五";
                    break;
                case 6:
                    w="六";
                    break;
            }
            t_l=getPaddingLeft() + i * (getMeasuredWidth() / 7.0F);
            t_r=getMeasuredWidth() / 7.0F;
            canvas.drawText(w,t_l+ t_r/2 - dayTextPaint.measureText(w) / 2 ,t_r-50,t_p);
        }

        canvas.translate(getPaddingLeft(), getPaddingTop()+getMeasuredHeight() / (float) (weeks+1));
        if(!DisplaySpecifiedBoolean){
            for (int week = 0; week < weeks; week++) {
                int count = (week == 0) ? 7 - firstDayOfWeek + 1 : ((week == weeks - 1) ? dateCell.getSumDays() - ((weeks - 2) * 7 + 7 - firstDayOfWeek + 1) : 7);
                for (int index = 0; index < count; index++) {
                    String day = String.valueOf(((week == 0) ? index : (week > 1 ? (week - 1) * 7 + 7 - firstDayOfWeek + 1 + index : 7 - firstDayOfWeek + 1 + index)) + 1);
                    float l;
                    if (week == 0 && firstDayOfWeek > 1) {
                        l = getPaddingLeft() + (firstDayOfWeek - 1 + index) * (getMeasuredWidth() / 7.0F/*day_view_width*/);
                    } else {
                        l = getPaddingLeft() + index * (getMeasuredWidth() / 7.0F);
                    }
                    float t = getPaddingTop() + week * (getMeasuredHeight() / (float) (weeks+1));
                    float r = l + getMeasuredWidth() / 7.0F;
                    float b = t + getMeasuredHeight() / (float) (weeks+1);

                /*if (dateCell.isCurMonth() && Integer.parseInt(day) == dateCell.getCurDay()) {
                    //canvas.drawCircle(l + (r - l) / 2.0F, t + (b - t) / 2.0F, (r - l) / 4.0F, curDayBgPaint);
                    dayTextPaint.setColor(curDayTextColor);
                    dayTextPaint.setTextSize(curDayTextSize);
                } else */{
                        int dayOfWeek = dateCell.getDayOfWeek(Integer.parseInt(day));
                        if (dateCell.getYear() > dateCell.curYear) {
                            dayTextPaint.setColor(Color.parseColor("#B7B7B7"));
//                        dayTextPaint.setColor(Color.parseColor("#333333"));
                        } else if (dateCell.getYear() < dateCell.curYear){
                            if (dayOfWeek  == Calendar.SUNDAY || dayOfWeek  == Calendar.SATURDAY) {
                                dayTextPaint.setColor(Color.parseColor("#333333"));
                            } else {
                                dayTextPaint.setColor(commonDayTextColor);
                            }
                        } else {
                            if (dateCell.getMonth() > dateCell.curMonth) {
                                dayTextPaint.setColor(Color.parseColor("#B7B7B7"));
//                            dayTextPaint.setColor(Color.parseColor("#333333"));
                            } else if (dateCell.getMonth() < dateCell.curMonth){
                                if (dayOfWeek  == Calendar.SUNDAY || dayOfWeek  == Calendar.SATURDAY) {
                                    dayTextPaint.setColor(Color.parseColor("#333333"));
                                } else {
                                    dayTextPaint.setColor(commonDayTextColor);
                                }
                            } else {
                                if (Integer.parseInt(day) > dateCell.getCurDay()) {
                                    dayTextPaint.setColor(Color.parseColor("#B7B7B7"));
//                                dayTextPaint.setColor(Color.parseColor("#333333"));
                                } else {
                                    if (dayOfWeek  == Calendar.SUNDAY || dayOfWeek  == Calendar.SATURDAY) {
                                        dayTextPaint.setColor(Color.parseColor("#333333"));
                                    } else {
                                        dayTextPaint.setColor(commonDayTextColor);
                                    }
                                }
                            }
                        }
                        dayTextPaint.setTextSize(commonDayTextSize);
                    }

                    if (dateCell.getYear() == dateCellStart.getYear()
                            && dateCell.getMonth() == dateCellStart.getMonth()
                            && Integer.parseInt(day) == dateCellStart.getDay()) {
                        RectF rectF = new RectF(l, t, r, b);
//                    canvas.drawRoundRect(rectF, 5, 5, curDayBgPaint);
                        canvas.drawCircle((l+r)/2,(t+b)/2,50,curDayBgPaint);
                        dayTextPaint.setColor(Color.WHITE);


                        dayTextPaint.setTextSize(curDayTextSize/2);
//                    canvas.drawText(getContext().getString(R.string.calendar_start_time), l + (r - l) / 2 - dayTextPaint.measureText(getContext().getString(R.string.calendar_end_time)) / 2,
//                            t + 30,
//                            dayTextPaint);

                        dayTextPaint.setTextSize(curDayTextSize);
                    }

                    if (dateCell.getYear() == dateCellEnd.getYear()
                            && dateCell.getMonth() == dateCellEnd.getMonth()
                            && Integer.parseInt(day) == dateCellEnd.getDay()) {
                        RectF rectF = new RectF(l, t, r, b);
                        canvas.drawRoundRect(rectF, 5, 5, curDayBgPaint);
                        dayTextPaint.setColor(Color.WHITE);

                        dayTextPaint.setTextSize(curDayTextSize/2);
                        canvas.drawText(getContext().getString(R.string.calendar_end_time), l + (r - l) / 2 - dayTextPaint.measureText(getContext().getString(R.string.calendar_end_time)) / 2,
                                t + 30,
                                dayTextPaint);

                        dayTextPaint.setTextSize(curDayTextSize);
                    }

                    if (week < weeks - 1) {
                        //canvas.drawLine(l, b, r, b, bottomLinePaint);
                    }
                    canvas.drawText(day, l + (r - l) / 2 - dayTextPaint.measureText(day) / 2, t + (b - t) / 2.0F + (dayTextPaint.getFontMetrics().descent - dayTextPaint.getFontMetrics().ascent) / 2.0F - dayTextPaint.getFontMetrics().descent, dayTextPaint);
                    map.put(day, new float[]{l, r, t+getMeasuredHeight() / (float) (weeks+1), b+getMeasuredHeight() / (float) (weeks+1)});
                }
            }
        }else{
            int count = (DisplaySpecifiedWeek == 0) ? 7 - firstDayOfWeek + 1 : ((DisplaySpecifiedWeek == weeks - 1) ? dateCell.getSumDays() - ((weeks - 2) * 7 + 7 - firstDayOfWeek + 1) : 7);
            for (int index = 0; index < count; index++) {
                String day = String.valueOf(((DisplaySpecifiedWeek == 0) ? index : (DisplaySpecifiedWeek > 1 ? (DisplaySpecifiedWeek - 1) * 7 + 7 - firstDayOfWeek + 1 + index : 7 - firstDayOfWeek + 1 + index)) + 1);
                float l;
                if (DisplaySpecifiedWeek == 0 && firstDayOfWeek > 1) {
                    l = getPaddingLeft() + (firstDayOfWeek - 1 + index) * (getMeasuredWidth() / 7.0F/*day_view_width*/);
                } else {
                    l = getPaddingLeft() + index * (getMeasuredWidth() / 7.0F);
                }
                float t = getPaddingTop() + 120;
                float r = l + getMeasuredWidth() / 7.0F;
                float b = t + 120;

                /*if (dateCell.isCurMonth() && Integer.parseInt(day) == dateCell.getCurDay()) {
                    //canvas.drawCircle(l + (r - l) / 2.0F, t + (b - t) / 2.0F, (r - l) / 4.0F, curDayBgPaint);
                    dayTextPaint.setColor(curDayTextColor);
                    dayTextPaint.setTextSize(curDayTextSize);
                } else */{
                    int dayOfWeek = dateCell.getDayOfWeek(Integer.parseInt(day));
                    if (dateCell.getYear() > dateCell.curYear) {
                        dayTextPaint.setColor(Color.parseColor("#B7B7B7"));
//                        dayTextPaint.setColor(Color.parseColor("#333333"));
                    } else if (dateCell.getYear() < dateCell.curYear){
                        if (dayOfWeek  == Calendar.SUNDAY || dayOfWeek  == Calendar.SATURDAY) {
                            dayTextPaint.setColor(Color.parseColor("#333333"));
                        } else {
                            dayTextPaint.setColor(commonDayTextColor);
                        }
                    } else {
                        if (dateCell.getMonth() > dateCell.curMonth) {
                            dayTextPaint.setColor(Color.parseColor("#B7B7B7"));
//                            dayTextPaint.setColor(Color.parseColor("#333333"));
                        } else if (dateCell.getMonth() < dateCell.curMonth){
                            if (dayOfWeek  == Calendar.SUNDAY || dayOfWeek  == Calendar.SATURDAY) {
                                dayTextPaint.setColor(Color.parseColor("#333333"));
                            } else {
                                dayTextPaint.setColor(commonDayTextColor);
                            }
                        } else {
                            if (Integer.parseInt(day) > dateCell.getCurDay()) {
                                dayTextPaint.setColor(Color.parseColor("#B7B7B7"));
//                                dayTextPaint.setColor(Color.parseColor("#333333"));
                            } else {
                                if (dayOfWeek  == Calendar.SUNDAY || dayOfWeek  == Calendar.SATURDAY) {
                                    dayTextPaint.setColor(Color.parseColor("#333333"));
                                } else {
                                    dayTextPaint.setColor(commonDayTextColor);
                                }
                            }
                        }
                    }
                    dayTextPaint.setTextSize(commonDayTextSize);
                }

                if (dateCell.getYear() == dateCellStart.getYear()
                        && dateCell.getMonth() == dateCellStart.getMonth()
                        && Integer.parseInt(day) == dateCellStart.getDay()) {
                    RectF rectF = new RectF(l, t, r, b);
//                    canvas.drawRoundRect(rectF, 5, 5, curDayBgPaint);
                    canvas.drawCircle((l+r)/2,(t+b)/2,50,curDayBgPaint);
                    dayTextPaint.setColor(Color.WHITE);


                    dayTextPaint.setTextSize(curDayTextSize/2);
//                    canvas.drawText(getContext().getString(R.string.calendar_start_time), l + (r - l) / 2 - dayTextPaint.measureText(getContext().getString(R.string.calendar_end_time)) / 2,
//                            t + 30,
//                            dayTextPaint);

                    dayTextPaint.setTextSize(curDayTextSize);
                }

                canvas.drawText(day, l + (r - l) / 2 - dayTextPaint.measureText(day) / 2, t + (b - t) / 2.0F + (dayTextPaint.getFontMetrics().descent - dayTextPaint.getFontMetrics().ascent) / 2.0F - dayTextPaint.getFontMetrics().descent, dayTextPaint);
                map.put(day, new float[]{l, r, t+getMeasuredHeight() / (float) (weeks+1), b+getMeasuredHeight() / (float) (weeks+1)});
            }
        }

        if(!DisplaySpecifiedBoolean||DisplaySpecifiedWeek==0){
            int lastMonthLastDay=DateCell.getLastDayOfLastMonth(dateCell.getYear(),dateCell.getMonth()-1);
            float last_l=0;
            float last_r=0;
            dayTextPaint.setColor(Color.parseColor("#B7B7B7"));
            for(int i=firstDayOfWeek-1;i>0;i--){
                last_l=getPaddingLeft() + (i-1) * (getMeasuredWidth() / 7.0F)+getMeasuredWidth() / 7.0F/2.0F - dayTextPaint.measureText(lastMonthLastDay--+"") / 2 ;
//                last_r=getMeasuredWidth() / 7.0F;
                if(DisplaySpecifiedBoolean){
                    last_r=getPaddingTop() + 120 + 120 / 2.0F + (dayTextPaint.getFontMetrics().descent - dayTextPaint.getFontMetrics().ascent) / 2.0F - dayTextPaint.getFontMetrics().descent;
                }else{
                    last_r=getPaddingTop() + getMeasuredHeight() / (float) (weeks+1)/2+(dayTextPaint.getFontMetrics().descent - dayTextPaint.getFontMetrics().ascent) / 2.0F - dayTextPaint.getFontMetrics().descent;
                }
                canvas.drawText(lastMonthLastDay--+"",last_l ,last_r,dayTextPaint);
            }
        }
        canvas.restoreToCount(saveCount);
    }

    public void displaysSpecifiedWeek(int week){
        if(week>weeks||week<0){
            DisplaySpecifiedBoolean=false;
            return;
        }
        DisplaySpecifiedWeek=week;
        DisplaySpecifiedBoolean=true;
        map.clear();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                clickPositionX = event.getX();
                clickPositionY = event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                /*if (clickPositionX == event.getX() && clickPositionY == event.getY()) */{
                    int day;
                    if(!DisplaySpecifiedBoolean){
                        day = getDayByPosition(clickPositionX, clickPositionY);
                    }else{
                        day = getDayByPosition2(clickPositionX, clickPositionY);
                    }


                    if (dateCell.getYear() > dateCell.curYear) {
                        return true;
                    } else if (dateCell.getYear() == dateCell.curYear){
                        if (dateCell.getMonth() > dateCell.curMonth) {
                            return true;
                        } else if (dateCell.getMonth() == dateCell.curMonth){
                            if (day > dateCell.getCurDay()) {
                                return true;
                            }
                        }
                    }

                    if (day != 0) {
                        if (listener != null) {
                            listener.clickDay(dateCell.getYear(), dateCell.getMonth(), day);
                        } else {
                            //Log.w("zzg", "dateCellStart="+dateCellStart+", dateCellEnd="+dateCellEnd);
                            //Toast.makeText(getContext(), dateCell.getYear() + "年" + dateCell.getMonth() + "月" + day + "日", Toast.LENGTH_SHORT).show();
                            //invalidate();
                        }
                        return true;
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private int getDayByPosition(float x, float y) {
        if (map.size() == 0) {
			return 0;
        }
        float[] rectInfo;//[left,right,top,bottom]
        for (int day = 1; day < 8; day++) {//先确定点击的点在周几
            rectInfo = map.get(String.valueOf(day));
            if(rectInfo==null)continue;
            if (x >= rectInfo[0] && x <= rectInfo[1]) {
                while (rectInfo != null && (y < rectInfo[2] || y > rectInfo[3])) {
                    rectInfo = map.get(String.valueOf(day += 7));
                }
                return day > sumDays ? 0 : day;
            }
        }
        return 0;
    }
    private int getDayByPosition2(float x, float y) {
        if (map.size() == 0) {
            return 0;
        }
        float[] rectInfo;//[left,right,top,bottom]
        String[] keySet = map.keySet().toArray(new String[0]);
        for (int day = 0; day < keySet.length; day++) {//先确定点击的点在周几
            rectInfo = map.get(keySet[day]);
            if(rectInfo==null)continue;
            if (x >= rectInfo[0] && x <= rectInfo[1]) {
                if(y >= rectInfo[2] && y <= rectInfo[3]){
                    return Integer.parseInt(keySet[day])> sumDays ? 0 : Integer.parseInt(keySet[day]);
                }
            }
        }
        return 0;
    }

    public void setOnClickDayListener(OnClickDayListener listener) {
        this.listener = listener;
    }

    public interface OnClickDayListener {
        void clickDay(int year, int month, int day);
    }

    public static class Builder {
        float bottomLineWidth;
        int curDayTextColor = Color.BLUE;
        int commonDayTextColor = Color.BLACK;
        float curDayTextSize;
        float commonDayTextSize;
        float startEndTextSize;
        int bottomLineColor = Color.parseColor("#DCDCDC");
        int curDayBackgroundColor = Color.WHITE;
        Context context;

        public Builder(Context context) {
            this.context = context;
            bottomLineWidth = context.getResources().getDimension(R.dimen.bottomLineWidth);
            curDayTextSize = context.getResources().getDimension(R.dimen.day_text_size);
            commonDayTextSize = curDayTextSize;
        }

        public Builder bottomLineColor(int color) {
            bottomLineColor = color;
            return this;
        }

        public Builder bottomLineWidth(float width) {
            bottomLineWidth = width;
            return this;
        }

        public Builder curDayTextColor(int corlor) {
            curDayTextColor = corlor;
            return this;
        }

        public Builder commonDayTextColor(int corlor) {
            commonDayTextColor = corlor;
            return this;
        }

        public Builder curDayTextSize(float size) {
            curDayTextSize = size;
            return this;
        }

        public Builder commonDayTextSize(float size) {
            commonDayTextSize = size;
            return this;
        }

        public Builder curDayBackgroundColor(int color) {
            curDayBackgroundColor = color;
            return this;
        }
    }

}
