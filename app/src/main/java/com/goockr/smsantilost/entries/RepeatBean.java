package com.goockr.smsantilost.entries;

/**
 * Created by yuyouming on 2017/11/28.
 */

public class RepeatBean {
    private String date;
    private boolean isSelected;

    public RepeatBean(String date, boolean isSelected) {
        this.date = date;
        this.isSelected = isSelected;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
