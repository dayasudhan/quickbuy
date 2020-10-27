package com.kuruvatech.quickbuy;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.splunk.mint.Mint;

import java.util.ArrayList;

import com.kuruvatech.quickbuy.model.HotelDetail;
import com.kuruvatech.quickbuy.model.Menu;
import com.kuruvatech.quickbuy.model.Order;
import com.kuruvatech.quickbuy.utils.Constants;
import com.kuruvatech.quickbuy.utils.SessionManager;

public class FinishActivity extends AppCompatActivity {

    Order order;
    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mint.initAndStartSession(this, "49d903c2");
        setContentView(R.layout.activity_finish);
        Intent intent = getIntent();

        Gson gson = new Gson();
        order = gson.fromJson(intent.getStringExtra("order"), Order.class);
        HotelDetail hotelDetail = gson.fromJson(intent.getStringExtra("HotelDetail"), HotelDetail.class);
        TextView txtViewName = (TextView) findViewById(R.id.vendor_name_value);
        TextView txtViewPhone = (TextView) findViewById(R.id.vendor_phone_value);
        TextView txtViewAddress = (TextView) findViewById(R.id.address_value);
        TextView txtViewMenu = (TextView) findViewById(R.id.items_value);
        TextView txtViewOrderId = (TextView) findViewById(R.id.order_id_value);
        TextView txtViewBillValue = (TextView) findViewById(R.id.bill_value_value);
        TextView txtViewdeliveryTime = (TextView) findViewById(R.id.vendor_delivery_time_value);
      // TextView txtVieworderTime = (TextView) findViewById(R.id.order_time_value);

        txtViewName.setText(order.getHotel().getName());
        txtViewPhone.setText(String.valueOf(order.getHotel().getPhone()));
        txtViewOrderId.setText(order.getId());
        txtViewdeliveryTime.setText(Integer.toString(hotelDetail.getDeliveryTime()) + " mins");
        txtViewBillValue.setText(String.valueOf(order.getTotalCost()));
        ArrayList<Menu> items = order.getMenuItems();
        String MenuItemStr = "";
        for(int j = 0 ; j < items.size() ; j++)
        {
            MenuItemStr += items.get(j).getName() + " (" + items.get(j).getNo_of_order() + ")" + '\n';
        }
        txtViewMenu.setText(MenuItemStr);
        String CustomerAddress = order.getCustomer().getAddress().toString();
        session = new SessionManager(getApplicationContext());
        session.setCurrentOrderId(order.getId());
//        if(!session.isLoggedIn())
//            alertMessage();
//        else if(session.isLoggedIn())
//        {
//            saveAddress();
//        }
        saveAddress();
        txtViewAddress.setText(CustomerAddress);
        setToolBar();
    }

    private void saveAddress() {
        session.setKeyPhone(order.getCustomer().getPhone());
        session.setAddress(order.getCustomer().getAddress().getAreaName(),
                order.getCustomer().getAddress().getLandMark(),
                order.getCustomer().getAddress().getAddressLine1(),
                order.getCustomer().getAddress().getAddressLine2(),
                order.getCustomer().getAddress().getCity());
        //session.setName(order.getCustomer().getName());
    }


    private void setToolBar() {
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(tb);
        tb.setTitleTextColor(Color.rgb(Constants.TITLE_TEXT_COLOR_RED,
                Constants.TITLE_TEXT_COLOR_GREEN, Constants.TITLE_TEXT_COLOR_BLUE));;
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_action_back);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Order Summary");
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void onBackPressed() {

        Intent start = new Intent(FinishActivity.this,MainActivity.class);
        start.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(start);
        finish();
    }
}
