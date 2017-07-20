package com.dtw.fellinghouse.Bean;

/**
 * Created by Administrator on 2017/7/19 0019.
 */

public class QiNiuResultBean {
    private String key;
    private String hash;
    private String bucket;
    private String fsize;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getFsize() {
        return fsize;
    }

    public void setFsize(String fsize) {
        this.fsize = fsize;
    }
}
