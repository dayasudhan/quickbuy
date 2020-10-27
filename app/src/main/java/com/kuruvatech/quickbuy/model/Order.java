package com.kuruvatech.quickbuy.model;

import java.util.ArrayList;

/**
 * Created by dganeshappa on 5/19/2016.
 */
public class Order {
    public int getOrdertype() {
        return ordertype;
    }

    public void setOrdertype(int ordertype) {
        this.ordertype = ordertype;
    }

    private int ordertype;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public ArrayList<Menu> getMenuItems() {
        return menu;
    }

    public void setMenuItems(ArrayList<Menu> menuItems) {
        this.menu = menuItems;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
    public int getBill_value() {
        return bill_value;
    }

    public void setBill_value(int bill_value , int deliveryCharge) {
        this.bill_value = bill_value;
        this.totalCost = bill_value + deliveryCharge;
    }
    public int getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(int deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    public ArrayList<Menu> getMenu() {
        return menu;
    }

    public void setMenu(ArrayList<Menu> menu) {
        this.menu = menu;
    }
    Customer customer;
    Hotel hotel;
    ArrayList<Menu> menu;
    String _id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;


    int bill_value;


    int deliveryCharge;
    int totalCost;
    public Order()
    {
        _id = new String();
        id = new String();
        customer = new Customer();
        hotel = new Hotel();
        menu = new ArrayList<Menu>();
        bill_value = 0;
        deliveryCharge = 0;
        totalCost = 0;
        ordertype = 0;
    }

}
