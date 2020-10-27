package com.kuruvatech.quickbuy.model;

/**
 * Created by dganeshappa on 6/23/2016.
 */
public class TimeInDay {


    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    String startTime;
    String endTime;
    String available;
}
