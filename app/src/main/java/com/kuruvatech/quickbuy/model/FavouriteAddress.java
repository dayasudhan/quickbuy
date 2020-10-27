package com.kuruvatech.quickbuy.model;

/**
 * Created by dganeshappa on 7/22/2016.
 */
public class FavouriteAddress {
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    String label;
    Address address;

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    String address2;
}
