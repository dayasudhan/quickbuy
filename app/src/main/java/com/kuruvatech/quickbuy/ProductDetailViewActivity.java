package com.kuruvatech.quickbuy;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.splunk.mint.Mint;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.kuruvatech.quickbuy.adapter.PlusMinusButtonListener;
import com.kuruvatech.quickbuy.adapter.ProductAdapter;
import com.kuruvatech.quickbuy.model.HotelDetail;
import com.kuruvatech.quickbuy.model.Menu;
import com.kuruvatech.quickbuy.model.MenuAdapter;
import com.kuruvatech.quickbuy.model.Order;
import com.kuruvatech.quickbuy.utils.Constants;


public class ProductDetailViewActivity extends AppCompatActivity implements PlusMinusButtonListener {

    HotelDetail hotelDetail;
    Order order;
    ImageView background;
    ArrayList<MenuAdapter> mMenulist;
    ProductAdapter mDataAdapter;
    TextView counttxt,priceTxt;
    String logo;

    TextView vendorRating,speciality,deliveryTime,minimumOrder,deliverycharge,orderTimings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mint.initAndStartSession(this, "49d903c2");
        setContentView(R.layout.activity_product_detail_view);

        order = new Order();
        Intent intent = getIntent();
        Gson gson = new Gson();
        hotelDetail = gson.fromJson(intent.getStringExtra("hotel"), HotelDetail.class);
         if(intent.getStringExtra("isBulk").equals("true"))
        {
            order.setOrdertype(1);
        }
        else
        {
            order.setOrdertype(0);
        }
        order.setHotel(hotelDetail.getHotel());
        mMenulist = new ArrayList<MenuAdapter>();
        for(int i = 0; i< hotelDetail.getMenu().size(); i++)
        {
            MenuAdapter menuAdapter = new MenuAdapter( hotelDetail.getMenu().get(i));
            mMenulist.add(menuAdapter);
        }
        if(order.getOrdertype()==1){
            mDataAdapter = new ProductAdapter(ProductDetailViewActivity.this,
                    R.layout.product_detail_list_layout,mMenulist,1);

        }else {
            mDataAdapter = new ProductAdapter(ProductDetailViewActivity.this,
                    R.layout.product_detail_list_layout, mMenulist);
        }mDataAdapter.setListener(this);
        ListView listView = (ListView) findViewById(R.id.listView_product_detail);
        listView.setAdapter(mDataAdapter);
        vendorRating  = (TextView)findViewById(R.id.vendor_name_info);
        vendorRating.setText(hotelDetail.getHotel().getName());

        vendorRating  = (TextView)findViewById(R.id.vendor_name_info);
        String rating = "";
        for(int i = 0 ; i < hotelDetail.getRating() ; i++)
        {
            rating = rating  + "*";
        }
        vendorRating.setText(rating);
        speciality  = (TextView)findViewById(R.id.vendor_speciality_info);
        speciality.setText(hotelDetail.getSpeciality());
        deliverycharge  = (TextView)findViewById(R.id.delievercharge);
        deliverycharge.setText("Deliver Charge: ₹"+Integer.toString(hotelDetail.getDeliverCharge()));
        deliveryTime  = (TextView)findViewById(R.id.vendor_delivery_time_info);
        deliveryTime.setText("Deliver Time: " + Integer.toString(hotelDetail.getDeliveryTime()) + " mins");
        minimumOrder  = (TextView)findViewById(R.id.vendor_rating_info);
        minimumOrder.setText("Min Order: ₹ "+Integer.toString(hotelDetail.getMinimumOrder()));
//        phone    = (TextView)findViewById(R.id.phone);
//        phone.setText("Phone :"+Integer.toString(hotelDetail.getPhone()));
        orderTimings = (TextView)findViewById(R.id.ordertimings);

        String strorderTimings = new String();
        if(hotelDetail.getOrderAcceptTimings()!=null) {
            if (hotelDetail.getOrderAcceptTimings().getMorning()!=null && hotelDetail.getOrderAcceptTimings().getMorning().getAvailable() !=null&& hotelDetail.getOrderAcceptTimings().getMorning().getAvailable().equals("Yes")) {
                strorderTimings = "Morning: " + hotelDetail.getOrderAcceptTimings().getMorning().getStartTime() + "-" + hotelDetail.getOrderAcceptTimings().getMorning().getEndTime();
            }
            if (hotelDetail.getOrderAcceptTimings().getLunch()!=null && hotelDetail.getOrderAcceptTimings().getLunch().getAvailable() !=null &&hotelDetail.getOrderAcceptTimings().getLunch().getAvailable().equals("Yes")) {
                strorderTimings = strorderTimings + "\n" + "Afternoon   : " + hotelDetail.getOrderAcceptTimings().getLunch().getStartTime() + "-" + hotelDetail.getOrderAcceptTimings().getLunch().getEndTime();
            }
            if (hotelDetail.getOrderAcceptTimings().getDinner()!=null &&  hotelDetail.getOrderAcceptTimings().getDinner().getAvailable() !=null && hotelDetail.getOrderAcceptTimings().getDinner().getAvailable().equals("Yes")) {
                strorderTimings = strorderTimings + "\n" + "Night  : " + hotelDetail.getOrderAcceptTimings().getDinner().getStartTime() + "-" + hotelDetail.getOrderAcceptTimings().getDinner().getEndTime();
            }
        }
        orderTimings.setText(strorderTimings);
        Button nextButton = (Button) findViewById(R.id.button);
        nextButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                moveNext();
            }
        });
        setToolBar(hotelDetail.getHotel().getName());
    }

    private void setToolBar(String areaClicked) {
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(tb);

        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_action_back);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(areaClicked);
        tb.setTitleTextColor(Color.rgb(Constants.TITLE_TEXT_COLOR_RED,
                Constants.TITLE_TEXT_COLOR_GREEN, Constants.TITLE_TEXT_COLOR_BLUE));
    }

    public void moveNext()
    {
        if(!checkTimeAllowedForOrder())
        {
            alertMessage("This time no delivery for this Order Kindly Check Order timings");
        }
        else if(mDataAdapter.totalCount <= 0)
        {
            alertMessage("Cart Empty -please select Some Items");
        }
        else if(mDataAdapter.totalCost < hotelDetail.getMinimumOrder())
        {
            String text  = "Minimum Order for this Order is Rs." +  Integer.toString(hotelDetail.getMinimumOrder()) + " Kindly add more items";
            alertMessage(text);
        }

        else {
            Intent i = new Intent(ProductDetailViewActivity.this, CartActivity.class);
            Gson gson = new Gson();
            order.getMenuItems().clear();
            for (int j = 0; j < mDataAdapter.getmMenulist().size(); j++) {
                if (mDataAdapter.getmMenulist().get(j).getMenuOrder().getNo_of_order() > 0) {
                    Menu menu = mDataAdapter.getmMenulist().get(j).getMenuOrder();
                    order.getMenuItems().add(menu);
                }
            }
            String strOrder = gson.toJson(order);
            i.putExtra("order", strOrder);
            String strHotelDetail = gson.toJson(hotelDetail);
            i.putExtra("HotelDetail",strHotelDetail);
            startActivityForResult(i, 1);
        }
    }
    @Override
    public void onResume()
    {
        super.onResume();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 1) {
            Gson gson = new Gson();
            order = gson.fromJson(intent.getStringExtra("order"), Order.class);
            for (int j = 0; j < mDataAdapter.getmMenulist().size(); j++) {
                boolean hasSelected = false;

                for (int i = 0; i < order.getMenuItems().size(); i++) {
                    MenuAdapter menuAdapter = new MenuAdapter(order.getMenuItems().get(i));
                    if (mDataAdapter.getmMenulist().get(j).getName().equals(menuAdapter.getName())) {
                        mDataAdapter.getmMenulist().get(j).setNo_of_order(menuAdapter.getNo_of_order());

                        hasSelected = true;
                    }
                }
                if(hasSelected == false && mDataAdapter.getmMenulist().get(j).getNo_of_order()>0) {
                    mDataAdapter.getmMenulist().get(j).setNo_of_order(0);
                }

            }
            mDataAdapter.totalCost = 0;
            mDataAdapter.totalCount = 0;
            for (int j = 0; j < mDataAdapter.getmMenulist().size(); j++) {
                mDataAdapter.totalCost +=  mDataAdapter.getmMenulist().get(j).getNo_of_order() * mDataAdapter.getmMenulist().get(j).getPrice();
                mDataAdapter.totalCount += mDataAdapter.getmMenulist().get(j).getNo_of_order();
            }
        }

        buttonClicked(0,0);

        mDataAdapter.notifyDataSetInvalidated();
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
    @Override
    public void buttonClicked(int postion,int value) {
        if(counttxt != null) {
            counttxt.setText(String.valueOf(mDataAdapter.totalCount));
        }
        if(priceTxt != null) {
            priceTxt.setText("₹ " + String.valueOf(mDataAdapter.totalCost));
        }
    }
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.menu_product_detail_list, menu);
        //  menu_hotlist = menu.findItem(R.id.menu_hotlist).getActionView();
        // counttxt= (TextView) menu_hotlist.findViewById(R.id.hotlist_hot);
        RelativeLayout badgeLayout = (RelativeLayout) menu.findItem(R.id.menu_hotlist).getActionView();
        //badgeLayout.set
        counttxt = (TextView) badgeLayout.findViewById(R.id.count_indicator);
        priceTxt = (TextView) badgeLayout.findViewById(R.id.checkoutprice);
        //counttxt.setVisibility(View.INVISIBLE);
        // updateHotCount(0);
        counttxt.setText("0");
        badgeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveNext();
            }
        });
        /*new MyMenuItemStuffListener(menu_hotlist, "Show hot message") {
            @Override
            public void onClick(View v) {
            }
            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                return false;
            }
        };*/
        return super.onCreateOptionsMenu(menu);
    }
    public boolean checkTimeAllowedForOrder()
    {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            Date orderTime  = dateFormat.parse( dateFormat.format(new Date()));
            if (hotelDetail.getOrderAcceptTimings().getMorning()!=null &&
                    hotelDetail.getOrderAcceptTimings().getMorning().getAvailable() !=null &&
                    hotelDetail.getOrderAcceptTimings().getMorning().getAvailable().equals("Yes")) {
                Date starttime = dateFormat.parse(hotelDetail.getOrderAcceptTimings().getMorning().getStartTime()   );
                starttime.setMinutes( starttime.getMinutes() -  hotelDetail.getDeliveryTime());
                Date endtime = dateFormat.parse(hotelDetail.getOrderAcceptTimings().getMorning().getEndTime());
                if ((orderTime.after(starttime) && orderTime.before(endtime))) {
                    return true;
                }
            }
            if (hotelDetail.getOrderAcceptTimings().getLunch()!=null &&
                    hotelDetail.getOrderAcceptTimings().getLunch().getAvailable() !=null &&
                    hotelDetail.getOrderAcceptTimings().getLunch().getAvailable().equals("Yes") ) {
                Date starttime = dateFormat.parse(hotelDetail.getOrderAcceptTimings().getLunch().getStartTime());
                starttime.setMinutes( starttime.getMinutes() -  hotelDetail.getDeliveryTime());
                Date endtime = dateFormat.parse(hotelDetail.getOrderAcceptTimings().getLunch().getEndTime());
                if ((orderTime.after(starttime) && orderTime.before(endtime))) {
                    return true;
                }
            }
            if (hotelDetail.getOrderAcceptTimings().getDinner()!=null &&
                    hotelDetail.getOrderAcceptTimings().getDinner().getAvailable() !=null &&
                    hotelDetail.getOrderAcceptTimings().getDinner().getAvailable().equals("Yes")) {
                Date starttime = dateFormat.parse(hotelDetail.getOrderAcceptTimings().getDinner().getStartTime());
                starttime.setMinutes( starttime.getMinutes() -  hotelDetail.getDeliveryTime());
                Date endtime = dateFormat.parse(hotelDetail.getOrderAcceptTimings().getDinner().getEndTime());
                if ((orderTime.after(starttime) && orderTime.before(endtime))) {
                    return true;
                }
            }
        }
        catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return false;
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
