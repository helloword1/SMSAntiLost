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
    @Property(nameInDb = "isLeft")
    public boolean isLeft;
    @Property(nameInDb = "isSucceed")
    public boolean isShowDate;
    @Property(nameInDb = "isShowDate")
    public boolean isSucceed=true;
    @Transient
    public boolean isSending=false;
    private Long mid;
    @Generated(hash = 1393834311)
    public ContentBean(Long id, String date, String msmStr, boolean isLeft,
            boolean isShowDate, boolean isSucceed, Long mid) {
        this.id = id;
        this.date = date;
        this.msmStr = msmStr;
        this.isLeft = isLeft;
        this.isShowDate = isShowDate;
        this.isSucceed = isSucceed;
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
    public boolean getIsSucceed() {
        return this.isSucceed;
    }
    public void setIsSucceed(boolean isSucceed) {
        this.isSucceed = isSucceed;
    }
    public Long getMid() {
        return this.mid;
    }
    public void setMid(Long mid) {
        this.mid = mid;
    }
    public boolean getIsShowDate() {
        return this.isShowDate;
    }
    public void setIsShowDate(boolean isShowDate) {
        this.isShowDate = isShowDate;
    }
    public boolean getIsSending() {
        return this.isSending;
    }
    public void setIsSending(boolean isSending) {
        this.isSending = isSending;
    }

}
