package com.dtw.fellinghouse.Bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/2 0002.
 */

public class LocationTranslatedBean {

    /**
     * status : 0
     * message : 转换成功
     * locations : [{"lng":116.835904,"lat":39.12088}]
     */

    private int status;
    private String message;
    private List<LocationsBean> locations;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<LocationsBean> getLocations() {
        return locations;
    }

    public void setLocations(List<LocationsBean> locations) {
        this.locations = locations;
    }
}
