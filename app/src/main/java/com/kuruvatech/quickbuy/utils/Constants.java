package com.kuruvatech.quickbuy.utils;


public class Constants {

    public static final String LOCALHOST = "http://192.168.1.104:3000";

    public static final String RELEASE_URL = "https://oota.herokuapp.com";
    public static final String DEBUG_URL = "https://kuruva.herokuapp.com";
    public static final String QKBY_URL = "https://qkby.herokuapp.com";
    public static final String QKB2_URL = "http://localhost:3000";
    public static final String MAIN_URL = QKBY_URL;

    public static final String ORDER_URL = MAIN_URL + "/v1/vendor/order";
    public static final String GET_HOTEL_BY_DELIVERY_AREAS =  MAIN_URL +"/v1/vendor/delieveryareas?areaName=";
    public static final String GET_HOTEL_BY_GPS =  MAIN_URL +"/v1/vendor/deliveryareasbygps?";
    public static final String GET_COVERAGE_AREAS = MAIN_URL +"/v1/admin/coverageArea";
    public static final String GET_STATUS_URL= MAIN_URL + "/v1/vendor/order_by_id/";
    public static final String REVIEW_URL = MAIN_URL + "/v1/vendor/review/";
    public static final String OTP_REGISTER_URL =MAIN_URL + "/v1/vendor/otp/register";
    public static final String OTP_CONFIRM_URL = MAIN_URL + "/v1/vendor/otp/confirm";
    public static final String CUSTOMER_ADDRESS_URL = MAIN_URL + "/v1/customer/address/";
    public static final String FIREBASE_APP = "https://project-8598805513533999178.firebaseio.com";
    //To store the firebase id in shared preferences
    public static final String UNIQUE_ID = "uniqueid";
    public static final String INVITE_TEXT = "\"All Groceries in reasonable prices at your doorstep from cheerful Youths of Thirthahalli \n" +
            "\n" + "A new Attempt of innovative company starting in order to build employment\n" +
            "\t\n" +
            " Download App @  https://play.google.com/store/apps/details?id=com.kuruvatech.quickbuy";
    public static final String INVITE_SUBJECT = "QuickBuy  ";


    public static final String SECUREKEY_KEY = "securekey";
    public static final String VERSION_KEY = "version";
    public static final String CLIENT_KEY = "client";

    public static final String SECUREKEY_VALUE = "EjR7tUPWx7WhsVs9FuVO6veFxFISIgIxhFZh6dM66rs";
    public static final String VERSION_VALUE = "1";
    public static final String CLIENT_VALUE = "bhoomika";
	


    public static final String NOTIFICATION_URL= MAIN_URL + "/images/logo/notification.jpg";
    public static final String ACCEPTED_URL= MAIN_URL + "/images/logo/accepted.png";
    public static final String REJECTED_URL= MAIN_URL + "/images/logo/rejected.png";
   // public static final String FAVOURITE_MENU_ITEM_URL = MAIN_URL + "/images/slider/slider4.jpg";


    public static final String ABOUT_US_URL= MAIN_URL+"/aboutus.html";
    public static final String FAQ_URL= MAIN_URL+"/faq.html";
    public static final String CONTACT_US_URL= MAIN_URL+"/contactus.html";
    public static final String TERMS_AND_CONDITION_URL= MAIN_URL+"/terms.html";

    public static final String DELETE_ADDRESS = MAIN_URL + "/v1/customer/address/";
    public static final String SLIDER_URL1= "https://devraj.s3.ap-south-1.amazonaws.com/images/scrol1.jpg";
    public static final String SLIDER_URL3="https://devraj.s3.ap-south-1.amazonaws.com/images/scrol2.jpg";
    public static final String SLIDER_URL2="https://devraj.s3.ap-south-1.amazonaws.com/images/scrol3.jpg";
    public static final String SLIDER_URL4=MAIN_URL + "/images/slider/slider4.jpg";
    public static final String DEFAULT_MENU_LOGO =MAIN_URL + "/images/menu/default.png";
    public static final String DEFAULT_HOTEL_LOGO =MAIN_URL + "/images/hotel/default.png";
    public static final int TITLE_TEXT_COLOR_RED = 00;
    public static final int TITLE_TEXT_COLOR_GREEN = 177;
    public static final int TITLE_TEXT_COLOR_BLUE = 106;

    public static final String AREATOBESEARCHED= "Soppugudde";

}
