package com.goockr.smsantilost.graphics.calendarview;


public class CalendarBean {

    public int year;
    public int moth;
    public int day;
    public int week;
    public boolean isHavePosition() {
        return isHavePosition;
    }

    public void setHavePosition(boolean havePosition) {
        isHavePosition = havePosition;
    }

    private boolean isHavePosition;

    //-1,0,1
    public int mothFlag;

    public CalendarBean(int year, int moth, int day) {
        this.year = year;
        this.moth = moth;
        this.day = day;
    }
    @Override
    public String toString() {
        String s=year+"/"+moth+"/"+day;
        return s;
    }
}