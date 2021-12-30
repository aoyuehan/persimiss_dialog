package com.aoyuehan.permission.bean;

import java.io.Serializable;

public class CacheElement implements Serializable {

    /**
     * 缓存的内容
     */
    private String value;

    /**
     * 失效时间，单位：秒，-1表示永久有效
     */
    private int expireTime;

    /**
     * 当前时间
     */
    private long currentTime;

    public CacheElement() {
        super();
    }

    public CacheElement(String value, int expireTime) {
        this.value = value;
        this.expireTime = expireTime;
        this.currentTime = System.currentTimeMillis();
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setExpireTime(int expireTime) {
        this.expireTime = expireTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public String getValue() {
        return value;
    }

    public int getExpireTime() {
        return expireTime;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    /**
     * 是否已经过期
     * @return
     */
    public boolean hasExpired() {
        if(expireTime == -1) {
            return false;
        }
        return System.currentTimeMillis() > currentTime + expireTime * 1000L;
    }
}
