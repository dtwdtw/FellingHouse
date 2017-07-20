package com.dtw.fellinghouse.Bean;

import java.util.List;

public  class ProductBean {
        /**
         * id : 0
         * name : 测试
         * descripe : 奢华测试
         * locationName : 天津
         * locationLatitude : 36.30556423523153
         * locationLongitude : 104.48060937499996
         * productImgNameList : ["FhieYtYGtSKnhmoFPItl-OqLdFLZ","Fqo6eh2C8fTLxDQoW3","factory-2389587_960_720.png","illustration-1546834_960_720.png"]
         */

        private int id;
        private String name;
        private String descripe;
        private String locationName;
        private double locationLatitude;
        private double locationLongitude;
        private List<String> productImgNameList;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescripe() {
            return descripe;
        }

        public void setDescripe(String descripe) {
            this.descripe = descripe;
        }

        public String getLocationName() {
            return locationName;
        }

        public void setLocationName(String locationName) {
            this.locationName = locationName;
        }

        public double getLocationLatitude() {
            return locationLatitude;
        }

        public void setLocationLatitude(double locationLatitude) {
            this.locationLatitude = locationLatitude;
        }

        public double getLocationLongitude() {
            return locationLongitude;
        }

        public void setLocationLongitude(double locationLongitude) {
            this.locationLongitude = locationLongitude;
        }

        public List<String> getProductImgNameList() {
            return productImgNameList;
        }

        public void setProductImgNameList(List<String> productImgNameList) {
            this.productImgNameList = productImgNameList;
        }
    }