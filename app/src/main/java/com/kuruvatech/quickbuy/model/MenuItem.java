package com.kuruvatech.quickbuy.model;

import com.kuruvatech.quickbuy.utils.Constants;

/**
 * Created by dganeshappa on 5/18/2016.
 */
public class MenuItem {
    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    int price;
    String name;
    String id,itemDescription;
    int availability;
    int  no_of_order;
    String logo;
    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }



    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public int isAvailable() {
        return availability;
    }

    public void setAvailable(int available) {
        this.availability = available;
    }
    public int getNo_of_order() {
        return no_of_order;
    }
    public void setNo_of_order(int no_of_order) {
        this.no_of_order = no_of_order;
    }
    public MenuItem()
    {
        name = new String();
        id = new String();
       // available = ;
        logo = Constants.DEFAULT_MENU_LOGO;
        no_of_order = 0;

    }
}
