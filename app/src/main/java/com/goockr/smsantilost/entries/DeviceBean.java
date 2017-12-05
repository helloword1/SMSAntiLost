package com.goockr.smsantilost.entries;

/**
 * Created by tanzhihao on 2017/11/23.
 */

public class DeviceBean {
    private String deviceName;
    private int deviceIcon;
    private String deviceSignal;

    public DeviceBean(String deviceName, int deviceIcon, String deviceSignal) {
        this.deviceName = deviceName;
        this.deviceIcon = deviceIcon;
        this.deviceSignal = deviceSignal;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public int getDeviceIcon() {
        return deviceIcon;
    }

    public void setDeviceIcon(int deviceIcon) {
        this.deviceIcon = deviceIcon;
    }

    public String getDeviceSignal() {
        return deviceSignal;
    }

    public void setDeviceSignal(String deviceSignal) {
        this.deviceSignal = deviceSignal;
    }
}
