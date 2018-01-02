package com.goockr.smsantilost.entries;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 *
 * @author LJN
 * @date 2017/12/27
 */
@Entity
public class AntiAddressBean {
    @Id
    private Long id;
    @Property(nameInDb = "longitude")
    private String longitude="0";
    @Property(nameInDb = "Latitude")
    private String Latitude="0";
    @Property(nameInDb = "radius")
    private String radius;
    @Property(nameInDb = "name")
    private String name;
    @Property(nameInDb = "remark")
    private String remark;
    @Generated(hash = 1030151301)
    public AntiAddressBean(Long id, String longitude, String Latitude,
            String radius, String name, String remark) {
        this.id = id;
        this.longitude = longitude;
        this.Latitude = Latitude;
        this.radius = radius;
        this.name = name;
        this.remark = remark;
    }
    @Generated(hash = 1894258945)
    public AntiAddressBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getLongitude() {
        return this.longitude;
    }
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    public String getLatitude() {
        return this.Latitude;
    }
    public void setLatitude(String Latitude) {
        this.Latitude = Latitude;
    }
    public String getRadius() {
        return this.radius;
    }
    public void setRadius(String radius) {
        this.radius = radius;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getRemark() {
        return this.remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
}
