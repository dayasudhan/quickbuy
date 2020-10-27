package com.kuruvatech.quickbuy.model;

import java.util.ArrayList;

/**
 * Created by dganeshappa on 5/18/2016.
 */
public class HotelDetail {


    String phone;
    int deliverRange;
    String _id;
    ArrayList<String> deliverAreas;
    Address address;
    ArrayList<MenuItem> menu;
    Hotel hotel;
    String speciality;
    int rating;
    int deliveryTime;
    int deliverCharge;
    int minimumOrder;

    public int getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(int isOpen) {
        this.isOpen = isOpen;
    }

    int isOpen;

    public OrderAcceptTimings getOrderAcceptTimings() {
        return orderAcceptTimings;
    }

    public void setOrderAcceptTimings(OrderAcceptTimings orderAcceptTimings) {
        this.orderAcceptTimings = orderAcceptTimings;
    }

    public int getMinimumOrder() {
        return minimumOrder;
    }

    public void setMinimumOrder(int minimumOrder) {
        this.minimumOrder = minimumOrder;
    }

    public int getDeliverCharge() {
        return deliverCharge;
    }

    public void setDeliverCharge(int deliverCharge) {
        this.deliverCharge = deliverCharge;
    }

    OrderAcceptTimings orderAcceptTimings;
    public HotelDetail()
    {
        hotel = new Hotel();

        _id = new String();

        address = new Address();
        menu = new ArrayList<MenuItem>();
        speciality = new String();
        deliverAreas = new ArrayList<String>();
   //     deliverRange = 3;
        phone = new String();
        rating = 5;
      //  deliveryTime = 60;
    }

    public int getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(int deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
    public ArrayList<MenuItem> getMenu() {
        return menu;
    }

    public void setMenu(ArrayList<MenuItem> menu) {
        this.menu = menu;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public ArrayList<String> getDeliverAreas() {
        return deliverAreas;
    }

    public void setDeliverAreas(ArrayList<String> deliverAreas) {
        this.deliverAreas = deliverAreas;
    }




    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public int getDeliverRange() {
        return deliverRange;
    }

    public void setDeliverRange(int deliverRange) {
        this.deliverRange = deliverRange;
    }

}
