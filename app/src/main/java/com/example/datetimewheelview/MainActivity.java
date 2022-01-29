package com.example.datetimewheelview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.datetimewheelview.CustomView.DateTimeWheelView;
import com.example.datetimewheelview.CustomView.DayView;
import com.example.datetimewheelview.Utils.DateCell;

public class MainActivity extends AppCompatActivity {
    private DayView calender;
    private ImageView expand_collapse_image;
    private RelativeLayout expand_collapse_rl;
    private boolean expand_image_flag=false;
    private PopupWindow select_time_pop;
    private View mRootView;
    private TextView tv_time;
    private WindowManager.LayoutParams mLayoutParams;
    private int current_year,current_month,current_day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DateCell dateCell = new DateCell();
        dateCell.setDate(2022,1);
        calender=findViewById(R.id.calender);
        calender.update(dateCell);
//        calender.setSelectDate(dateCell.getYear(),dateCell.getMonth(),10,0,0,0);
        calender.displaysSpecifiedWeek(0);
        calender.setOnClickDayListener(new DayView.OnClickDayListener() {
            @Override
            public void clickDay(int year, int month, int day) {
                calender.setSelectDate(year,month,day,0,0,0);
                calender.invalidate();
                current_year=year;
                current_month=month;
                current_day=day;
            }
        });


        expand_collapse_rl=findViewById(R.id.expand_collapse_rl);
        expand_collapse_image=findViewById(R.id.expand_collapse_image);
        expand_collapse_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(expand_image_flag){
                    expand_collapse_image.setImageResource(R.drawable.study_calendar_collapse_arrow);
                    calender.displaysSpecifiedWeek(1);
                    calender.requestLayout();
                    calender.invalidate();
                }else{
                    expand_collapse_image.setImageResource(R.drawable.study_calendar_expland_arrow);
                    calender.displaysSpecifiedWeek(-1);
                    calender.requestLayout();
                    calender.invalidate();
                }
                expand_image_flag=!expand_image_flag;
            }
        });

        mRootView = findViewById(android.R.id.content).getRootView();
        mLayoutParams = getWindow().getAttributes();
        select_time_pop=new PopupWindow(mRootView, LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT, true);
        View view = LayoutInflater.from(this).inflate(R.layout.study_calendar_pop, null);
        select_time_pop.setContentView(view);
        DateTimeWheelView data_picker=view.findViewById(R.id.data_picker);
        ImageView pop_titleBar_cancel=view.findViewById(R.id.pop_titleBar_cancel);
        pop_titleBar_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select_time_pop.dismiss();
            }
        });
        data_picker.setYearCount(10);
        data_picker.onTouchEventListener(new DateTimeWheelView.OnTouchCallBack() {
            @Override
            public void callBack(int year, int month) {
                current_year=year;
                current_month=month;
            }
        });

        TextView date_ensure;date_ensure=view.findViewById(R.id.date_ensure);
        date_ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateCell dateCell = new DateCell();
                dateCell.setDate(current_year,current_month);
                calender.update(dateCell);
                calender.invalidate();
                select_time_pop.dismiss();
                tv_time.setText(current_month+"月");
            }
        });


        tv_time=findViewById(R.id.tv_time);
        tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select_time_pop.showAtLocation(mRootView, Gravity.BOTTOM, 0, 0);
                mLayoutParams.alpha = 0.6f;
                getWindow().setAttributes(mLayoutParams);
            }
        });

        select_time_pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mLayoutParams.alpha = 1.0f;
                getWindow().setAttributes(mLayoutParams);
            }
        });
    }
}