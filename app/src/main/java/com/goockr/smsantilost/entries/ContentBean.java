package com.goockr.smsantilost.entries;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;

/**
 * Created by LJN on 2017/12/19.
 */
@Entity
public class ContentBean implements Serializable{
    private static final long serialVersionUID=-12L;
    @Id(autoincrement=true)
    private Long id;
    @Property(nameInDb = "date")
    public String date;
    @Property(nameInDb = "msmStr")
    public String msmStr;
    @Transient
    public int sumTime;
    @Property(nameInDb = "isLeft")
    public boolean isLeft;
    @Property(nameInDb = "isShowDate")
    public boolean isShowDate;
    @Property(nameInDb = "isSucceed")
    public int isSucceed=-1;
    @Property(nameInDb = "timeIndex")
    public String timeIndex;
    private Long mid;
    @Generated(hash = 194834548)
    public ContentBean(Long id, String date, String msmStr, boolean isLeft,
            boolean isShowDate, int isSucceed, String timeIndex, Long mid) {
        this.id = id;
        this.date = date;
        this.msmStr = msmStr;
        this.isLeft = isLeft;
        this.isShowDate = isShowDate;
        this.isSucceed = isSucceed;
        this.timeIndex = timeIndex;
        this.mid = mid;
    }
    @Generated(hash = 1643641106)
    public ContentBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDate() {
        return this.date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getMsmStr() {
        return this.msmStr;
    }
    public void setMsmStr(String msmStr) {
        this.msmStr = msmStr;
    }
    public boolean getIsLeft() {
        return this.isLeft;
    }
    public void setIsLeft(boolean isLeft) {
        this.isLeft = isLeft;
    }
    public boolean getIsShowDate() {
        return this.isShowDate;
    }
    public void setIsShowDate(boolean isShowDate) {
        this.isShowDate = isShowDate;
    }
    public int getIsSucceed() {
        return this.isSucceed;
    }
    public void setIsSucceed(int isSucceed) {
        this.isSucceed = isSucceed;
    }
    public String getTimeIndex() {
        return this.timeIndex;
    }
    public void setTimeIndex(String timeIndex) {
        this.timeIndex = timeIndex;
    }
    public Long getMid() {
        return this.mid;
    }
    public void setMid(Long mid) {
        this.mid = mid;
    }


}
