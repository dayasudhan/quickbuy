package com.kuruvatech.quickbuy;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.splunk.mint.Mint;

import java.util.ArrayList;

import com.kuruvatech.quickbuy.adapter.PlusMinusButtonListener;
import com.kuruvatech.quickbuy.adapter.ProductAdapter;
import com.kuruvatech.quickbuy.model.HotelDetail;
import com.kuruvatech.quickbuy.model.Menu;
import com.kuruvatech.quickbuy.model.MenuAdapter;
import com.kuruvatech.quickbuy.model.Order;
import com.kuruvatech.quickbuy.utils.Constants;
import com.kuruvatech.quickbuy.utils.SessionManager;

public class CartActivity extends AppCompatActivity implements PlusMinusButtonListener {


    Order order;
    HotelDetail hotelDetail;
    ArrayList<MenuAdapter> mMenulist;
    ProductAdapter dataAdapter;
    TextView orderTotalCharge,billvalue;
    SessionManager session;
    ImageView itemImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mint.initAndStartSession(this, "49d903c2");
        setContentView(R.layout.activity_cart);
        session = new SessionManager(getApplicationContext());
        Intent intent = getIntent();
        Gson gson = new Gson();
        order = gson.fromJson(intent.getStringExtra("order"), Order.class);
        hotelDetail = gson.fromJson(intent.getStringExtra("HotelDetail"), HotelDetail.class);

        int totalCost = 0;
        mMenulist = new ArrayList<MenuAdapter>();
        for(int i = 0; i< order.getMenuItems().size();i++)
        {
            MenuAdapter menuAdapter = new MenuAdapter( order.getMenuItems().get(i));
            mMenulist.add(menuAdapter);
            totalCost +=  menuAdapter.getNo_of_order() * menuAdapter.getPrice();
        }
        order.setBill_value(totalCost,hotelDetail.getDeliverCharge());
        order.setDeliveryCharge(hotelDetail.getDeliverCharge());

        dataAdapter = new ProductAdapter(CartActivity.this,
                R.layout.product_detail_list_layout,mMenulist);
        dataAdapter.setListener(this);
        ListView listView = (ListView) findViewById(R.id.listView_cart);


        TextView deliveryCharge = (TextView) findViewById(R.id.orderDetailDeliveryRupees);
        orderTotalCharge = (TextView) findViewById(R.id.order_total_charge);

        listView.setAdapter(dataAdapter);
        Button btn= (Button) findViewById(R.id.orderDetailButton_next);

        //TextView vendor_name = (TextView) findViewById(R.id.vendor_add_cart_name);
        //vendor_name.setText(order.getHotel().getName());

        deliveryCharge.setText(String.valueOf(order.getDeliveryCharge()));
        orderTotalCharge.setText(String.valueOf(order.getTotalCost()));

        billvalue = (TextView) findViewById(R.id.orderbilltotaltextrupees);
        billvalue.setText(String.valueOf(totalCost));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(order.getBill_value() < hotelDetail.getMinimumOrder())
                {
                    String text  = "Minimum Order for this Order is Rs." +  Integer.toString(hotelDetail.getMinimumOrder()) + " Kindly add more items";
                    //Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                    alertMessage(text);
                }
                else {
                    //Intent i = new Intent(CartActivity.this, ReviewDetailsActivity.class);
                    Gson gson = new Gson();
                    order.getMenuItems().clear();
                    for (int j = 0; j < dataAdapter.getmMenulist().size(); j++) {
                        if (dataAdapter.getmMenulist().get(j).getMenuOrder().getNo_of_order() > 0) {
                            Menu menu = dataAdapter.getmMenulist().get(j).getMenuOrder();
                            order.getMenuItems().add(menu);
                        }
                    }
                    String strOrder = gson.toJson(order);
                    String strHotelDetail = gson.toJson(hotelDetail);
                    if(session.isLoggedIn())
                    {
                        Intent i = new Intent(CartActivity.this, CutomerEnterDetailsActivity.class);
                        i.putExtra("order", strOrder);
                        i.putExtra("HotelDetail",strHotelDetail);
                        i.putExtra("Uniqid","From_CartActivity");
                        startActivity(i);
                    }
                    else
                    {
                        Intent i = new Intent(CartActivity.this, RegisterActivity.class);
                        i.putExtra("order", strOrder);
                        i.putExtra("HotelDetail",strHotelDetail);
                        startActivity(i);
                    }

                }
            }
        });
        setToolBar(order.getHotel().getName());
    }

    private void setToolBar(String title) {
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(tb);

        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_action_back);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(title);
        tb.setTitleTextColor(Color.rgb(Constants.TITLE_TEXT_COLOR_RED,
                        Constants.TITLE_TEXT_COLOR_GREEN, Constants.TITLE_TEXT_COLOR_BLUE));
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent();
        Gson gson = new Gson();
        order.getMenuItems().clear();
        for (int j = 0; j < dataAdapter.getmMenulist().size(); j++) {
            if (dataAdapter.getmMenulist().get(j).getMenuOrder().getNo_of_order() > 0) {
                Menu menu = dataAdapter.getmMenulist().get(j).getMenuOrder();
                order.getMenuItems().add(menu);
            }
        }
        String strOrder = gson.toJson(order);
        intent.putExtra("order", strOrder);
        setResult(RESULT_OK, intent);
        finish();
    }
    @Override
    public void buttonClicked(int position, int value) {
        ArrayList<MenuAdapter> menulist = dataAdapter.getmMenulist();
        int total =0;
        for(int i = 0; i< menulist.size();i++)
        {
            if(menulist.get(i).getNo_of_order() > 0) {
                total += menulist.get(i).getNo_of_order() * menulist.get(i).getPrice();
            }
        }

        order.setBill_value(total,hotelDetail.getDeliverCharge());
        orderTotalCharge.setText(String.valueOf(order.getTotalCost()));
        billvalue.setText(String.valueOf(order.getBill_value()));
    }
    public void alertMessage(String message) {
        DialogInterface.OnClickListener dialogClickListeneryesno = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {

                    case DialogInterface.BUTTON_NEUTRAL:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Fruity");
        builder.setMessage(message).setNeutralButton("Ok", dialogClickListeneryesno)
                .setIcon(R.drawable.ic_action_about).show();

    }
}
