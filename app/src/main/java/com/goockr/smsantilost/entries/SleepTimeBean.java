package com.goockr.smsantilost.entries;

import android.os.Parcel;

import java.io.Serializable;

/**
 * Created by yuyouming on 2017/11/29.
 */

public class SleepTimeBean implements Serializable {
    private String sleepTimeName;
    private String sleepTimeDuration;
    private String sleepTimeRepeat;


    public SleepTimeBean(String sleepTimeName, String sleepTimeDuration, String sleepTimeRepeat) {
        this.sleepTimeName = sleepTimeName;
        this.sleepTimeDuration = sleepTimeDuration;
        this.sleepTimeRepeat = sleepTimeRepeat;
    }

    protected SleepTimeBean(Parcel in) {
        sleepTimeName = in.readString();
        sleepTimeDuration = in.readString();
        sleepTimeRepeat = in.readString();
    }


    public String getSleepTimeName() {
        return sleepTimeName;
    }

    public void setSleepTimeName(String sleepTimeName) {
        this.sleepTimeName = sleepTimeName;
    }

    public String getSleepTimeDuration() {
        return sleepTimeDuration;
    }

    public void setSleepTimeDuration(String sleepTimeDuration) {
        this.sleepTimeDuration = sleepTimeDuration;
    }

    public String getSleepTimeRepeat() {
        return sleepTimeRepeat;
    }

    public void setSleepTimeRepeat(String sleepTimeRepeat) {
        this.sleepTimeRepeat = sleepTimeRepeat;
    }

}
