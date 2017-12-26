package com.goockr.smsantilost.entries;

import java.io.Serializable;

/**
 * Created by tanzhihao on 2017/11/20.
 */

public class AntilostBean implements Serializable{
    // 图标
    private int mDrawableId;
    // 钥匙
    private String name;
    // 距离最近
    private String lastDate;
    // 连接状态
    private boolean connectState;
    // 连接
    private String connect;
    private String mac;

    private double longitude ;
    private double latitude ;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    private String Address ;
    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    private String deviceName;

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    private String distance;

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public AntilostBean(int drawable, String name, String lastDate, boolean connectState, String connect) {
        mDrawableId = drawable;
        this.name = name;
        this.lastDate = lastDate;
        this.connectState = connectState;
        this.connect = connect;
    }

    public int getDrawableId() {
        return mDrawableId;
    }

    public void setDrawableId(int drawableId) {
        mDrawableId = drawableId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public boolean isConnectState() {
        return connectState;
    }

    public void setConnectState(boolean connectState) {
        this.connectState = connectState;
    }

    public String getConnect() {
        return connect;
    }

    public void setConnect(String connect) {
        this.connect = connect;
    }
}
