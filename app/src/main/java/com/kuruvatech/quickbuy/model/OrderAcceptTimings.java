package com.kuruvatech.quickbuy.model;

/**
 * Created by dganeshappa on 6/23/2016.
 */
public class OrderAcceptTimings {
    public TimeInDay getMorning() {
        return Morning;
    }

    public void setMorning(TimeInDay morning) {
        Morning = morning;
    }

    public TimeInDay getLunch() {
        return Lunch;
    }

    public void setLunch(TimeInDay lunch) {
        Lunch = lunch;
    }

    public TimeInDay getDinner() {
        return Dinner;
    }

    public void setDinner(TimeInDay dinner) {
        Dinner = dinner;
    }

    TimeInDay Morning;
    TimeInDay Lunch;
    TimeInDay Dinner;

}
