package com.kuruvatech.quickbuy.model;

/**
 * Created by dganeshappa on 5/19/2016.
 */
public class Menu {
    String name;
    int  no_of_order;
    int  price;
    String itemDescription;
    String logo;

    public Menu()
    {
        name = new String();
        itemDescription=new String();
        no_of_order = 0;
        price  = 0;
        logo=new String();
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNo_of_order() {
        return no_of_order;
    }

    public void setNo_of_order(int no_of_order) {
        this.no_of_order = no_of_order;
    }
    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
