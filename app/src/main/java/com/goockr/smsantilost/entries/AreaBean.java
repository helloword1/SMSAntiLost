package com.goockr.smsantilost.entries;

/**
 * Created by yuyouming on 2017/11/27.
 */

public class AreaBean {
    private String areaName;
    private String areaRemark;

    public AreaBean(String areaName, String areaRemark) {
        this.areaName = areaName;
        this.areaRemark = areaRemark;

    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaRemark() {
        return areaRemark;
    }

    public void setAreaRemark(String areaRemark) {
        this.areaRemark = areaRemark;
    }
}
