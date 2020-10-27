package com.kuruvatech.quickbuy.model;

/**
 * Created by dganeshappa on 5/18/2016.
 */
public class Hotel {
    public Hotel()
    {
        name = new String();
        email = new String();
        id = new String();
        phone = new String();
        logo =  new String();
        //deliveryCharges = 0;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    String name;
    String email;
    String phone;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    String logo;
//    public int getDeliveryCharges() {
//        return deliveryCharges;
//    }
//
//    public void setDeliveryCharges(int deliveryCharges) {
//        this.deliveryCharges = deliveryCharges;
//    }
//
//    int deliveryCharges;
}
