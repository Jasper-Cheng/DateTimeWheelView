package com.example.datetimewheelview.Utils;

import android.text.format.DateFormat;

import java.util.Calendar;

public class DateCell {
    int year;
    int month;
    int sumDays;
    int status = 0;     //0:none/1:start/2:end
    int day = 0;
    public static int curYear,curMonth,curSumdays,curDay;

    static{
        curYear = Integer.parseInt(DateFormat.format("yyyy", System.currentTimeMillis()).toString());
        curMonth = Integer.parseInt(DateFormat.format("MM", System.currentTimeMillis()).toString());
        curDay = Integer.parseInt(DateFormat.format("dd", System.currentTimeMillis()).toString());
        curSumdays = getDaysOfMonth(curYear, curMonth);
    }

    public boolean isCurMonth(){
        return this.year == curYear && this.month == curMonth;
    }

    public int getCurDay(){
        return curDay;
    }

    public int getDay(){
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getStatus(){
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public DateCell() {

    }

    public DateCell toCurrentDate(){
        year = Integer.parseInt(DateFormat.format("yyyy", System.currentTimeMillis()).toString());
        month = Integer.parseInt(DateFormat.format("MM", System.currentTimeMillis()).toString());
        sumDays = getDaysOfMonth(year, month);
        return this;
    }

    public void setDate(int year, int month) {
        this.year = year;
        this.month = month;
        sumDays = getDaysOfMonth(year, month);
    }

    public int getSumDays() {
        return sumDays;
    }


    public int getSumWeeksOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, sumDays);
        return calendar.get(Calendar.WEEK_OF_MONTH);
    }

	public int getDayOfWeek(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public int getFirstDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static int getLastDayOfLastMonth(int year,int moth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, moth, 0);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    private static int getDaysOfMonth(int year, int moth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, moth - 1);
        c.set(Calendar.DATE, 1);
        c.roll(Calendar.DATE, -1);
        return c.get(Calendar.DATE);
    }


    @Override
    public String toString() {
        return "DateCell{" +
                "year=" + year +
                ", month=" + month +
                ", sumDays=" + sumDays +
                ", day =" + day +
                ", status=" + status +
                '}';
    }
}
