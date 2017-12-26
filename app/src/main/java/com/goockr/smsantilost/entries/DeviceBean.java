package com.goockr.smsantilost.entries;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by ning on 2017/11/23.
 */
@Entity
public class DeviceBean {
    @Id
    private Long id;
    @Property(nameInDb = "deviceType")
    private String deviceType;
    @Property(nameInDb = "name")
    private String name;
    @Property(nameInDb = "battery")
    private String battery;
    @Property(nameInDb = "mac")
    private String mac;
    @Property(nameInDb = "distance")
    private String distance;
    @Property(nameInDb = "date")
    private String date;
    @Property(nameInDb = "longitude")
    private double longitude ;
    @Property(nameInDb = "latitude")
    private double latitude ;
    @Property(nameInDb = "address")
    private String address ;
    @Generated(hash = 1976272697)
    public DeviceBean(Long id, String deviceType, String name, String battery,
            String mac, String distance, String date, double longitude,
            double latitude, String address) {
        this.id = id;
        this.deviceType = deviceType;
        this.name = name;
        this.battery = battery;
        this.mac = mac;
        this.distance = distance;
        this.date = date;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
    }
    @Generated(hash = 74682814)
    public DeviceBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDeviceType() {
        return this.deviceType;
    }
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getBattery() {
        return this.battery;
    }
    public void setBattery(String battery) {
        this.battery = battery;
    }
    public String getMac() {
        return this.mac;
    }
    public void setMac(String mac) {
        this.mac = mac;
    }
    public String getDistance() {
        return this.distance;
    }
    public void setDistance(String distance) {
        this.distance = distance;
    }
    public String getDate() {
        return this.date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public double getLongitude() {
        return this.longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public double getLatitude() {
        return this.latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public String getAddress() {
        return this.address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
}
