package com.goockr.smsantilost.entries;

import java.util.List;

/**
 * Created by lijinning on 2018/1/12.
 */

public class AntiDisturbBean {

    /**
     * result : 0
     * data : [{"antiname":"我家","address":"佛山市南海区简平路圆方广场5栋南海华科高","latitude":"23.020319140469677","id":"b13e6912e6cb4a4e8df0c0bb144c7c55","radius":"288.0","longitude":"113.17022100090982"}]
     */

    private String result;
    private List<DataBean> data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * antiname : 我家
         * address : 佛山市南海区简平路圆方广场5栋南海华科高
         * latitude : 23.020319140469677
         * id : b13e6912e6cb4a4e8df0c0bb144c7c55
         * radius : 288.0
         * longitude : 113.17022100090982
         */

        private String antiname;
        private String address;
        private String latitude;
        private String id;
        private String radius;
        private String longitude;

        public String getAntiname() {
            return antiname;
        }

        public void setAntiname(String antiname) {
            this.antiname = antiname;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRadius() {
            return radius;
        }

        public void setRadius(String radius) {
            this.radius = radius;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }
    }
}
