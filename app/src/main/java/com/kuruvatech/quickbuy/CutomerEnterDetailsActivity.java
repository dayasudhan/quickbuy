package com.kuruvatech.quickbuy;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.splunk.mint.Mint;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;

import com.kuruvatech.quickbuy.model.Address;
import com.kuruvatech.quickbuy.model.HotelDetail;
import com.kuruvatech.quickbuy.model.Order;
import com.kuruvatech.quickbuy.utils.Constants;
import com.kuruvatech.quickbuy.utils.GPSTracker;
import com.kuruvatech.quickbuy.utils.SessionManager;


public class CutomerEnterDetailsActivity extends AppCompatActivity {

    Order order;
    String responseOrder;
    Button btnShowLocation;
    EditText editName,editPhone,editCity,editHouseNo,editAreaName,editLandmark,editAddress;
    GPSTracker gps;
    HotelDetail hotelDetail;
    String strHotelDetail;
    SessionManager session;
    boolean isCartActivityStarted;
    TextView orderTotalCharge,billvalue,deliveryCharge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mint.initAndStartSession(this, "49d903c2");
        setContentView(R.layout.activity_cutomer_enter_details);
        Intent intent = getIntent();
        Gson gson = new Gson();
        order = gson.fromJson(intent.getStringExtra("order"), Order.class);
        strHotelDetail = intent.getStringExtra("HotelDetail");
        hotelDetail = gson.fromJson(intent.getStringExtra("HotelDetail"), HotelDetail.class);
        if(intent.getExtras().getString("Uniqid").equals("From_CartActivity") )
        {
            isCartActivityStarted = true;
        }
        else
        {
            isCartActivityStarted = false;
        }
        responseOrder = new String();
        session = new SessionManager(getApplicationContext());
        Button btn= (Button) findViewById(R.id.placeOrderButton);
        btnShowLocation= (Button) findViewById(R.id.locationButton);
        editName=(EditText)findViewById(R.id.orderDetailName);

        editPhone=(EditText)findViewById(R.id.orderDetailPhone);
        editPhone.setKeyListener(null);

        editCity=(EditText)findViewById(R.id.orderDetailEmail);
        editHouseNo=(EditText)findViewById(R.id.orderDetailAddress_house_no);
        editAreaName=(EditText)findViewById(R.id.orderDetailAddress_areaname);
        editLandmark=(EditText)findViewById(R.id.orderDetailAddress_landmark);
        editAddress=(EditText)findViewById(R.id.orderDetailAddress_address);
        orderTotalCharge = (TextView) findViewById(R.id.textView);
        billvalue = (TextView) findViewById(R.id.orderbilltotaltextrupees);
        deliveryCharge = (TextView) findViewById(R.id.orderDetailDeliveryRupees);
        orderTotalCharge.setText(String.valueOf(order.getTotalCost()));
        billvalue.setText(String.valueOf(order.getBill_value()));
        deliveryCharge.setText(String.valueOf(order.getDeliveryCharge()));
        if(session.isLoggedIn()) {
            try {
                editName.setText(session.getName());
                editPhone.setText(session.getKeyPhone());
                editAreaName.setText(session.getAddress().getAreaName());
                editLandmark.setText(session.getAddress().getLandMark());
                editHouseNo.setText(session.getAddress().getAddressLine1());
                editAddress.setText(session.getAddress().getAddressLine2());
                editCity.setText(session.getAddress().getCity());
            }
            catch(Exception e)
            {

            }
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validatePhoneNumber(editPhone.getText().toString())) {
                    alertMessage(false,"Enter Valid Phone Number");
                }
                String name =editName.getText().toString();
                String house=editHouseNo.getText().toString();
                String addr =editAddress.getText().toString();
                String area =editAreaName.getText().toString();
                String land =editLandmark.getText().toString();

                if(name.trim().length() == 0){
                     editName.setFocusableInTouchMode(true);
                    editName.requestFocus();
                    alertMessage("Enter Name");
                }
                else if(house.trim().length() <= 0){
                    editHouseNo.setFocusableInTouchMode(true);
                    editHouseNo.requestFocus();
                    alertMessage("Enter house no ");
                }
                else if(addr.trim().length() <= 0){
                    editAddress.setFocusableInTouchMode(true);
                    editAddress.requestFocus();
                    alertMessage("Enter address ");
                }
                else if(area.trim().length()<= 0){
                    editAreaName.setFocusableInTouchMode(true);
                    editAreaName.requestFocus();
                    alertMessage("Enter area name ");
                }
                else if(land.trim().length()<=0){
                    editLandmark.setFocusableInTouchMode(true);
                    editLandmark.requestFocus();
                    alertMessage("enter landmark");
                }
                else if(hotelDetail.getMinimumOrder() > order.getTotalCost())
                {
                    String text  = "Minimum Order for this Hotel is Rs." +  Integer.toString(hotelDetail.getMinimumOrder()) + " Kindly add more items";
                    alertMessage(false,text);
                }
                else {
                    alertMessage(true,"Are you sure to place this order");
                }
            }
        });

        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(CutomerEnterDetailsActivity.this, SelectAddressActivity.class);
                startActivityForResult(i,1);

            }
        });
        setToolBar("Delivery Details");
    }
    private void setToolBar(String title) {
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(tb);
        tb.setTitleTextColor(Color.rgb(Constants.TITLE_TEXT_COLOR_RED,
                Constants.TITLE_TEXT_COLOR_GREEN, Constants.TITLE_TEXT_COLOR_BLUE));;
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_action_back);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(title);
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
                .setIcon(R.drawable.ic_action_about);

        final AlertDialog dialog = builder.create();
        dialog.show(); //show() should be called before dialog.getButton().


        final Button neutralButton = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) neutralButton.getLayoutParams();
        positiveButtonLL.gravity = Gravity.CENTER;
        neutralButton.setLayoutParams(positiveButtonLL);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK) {

            Gson gson = new Gson();

            Address address = gson.fromJson(intent.getStringExtra("locationaddress"), Address.class);

            editAreaName.setText(address.getAreaName());
            editLandmark.setText(address.getLandMark());
            editHouseNo.setText(address.getAddressLine1());
            editAddress.setText(address.getAddressLine2());
            editCity.setText(address.getCity());
        }
    }
    public void alertMessage(boolean yesnotype ,String message)
    {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE: // Yes button clicked
                    {
                        order.getCustomer().setPhone(editPhone.getText().toString());
                        order.getCustomer().setName(editName.getText().toString());
                        order.getCustomer().setEmail(session.getEmail());
                        order.getCustomer().getAddress().setAreaName(editAreaName.getText().toString());
                        order.getCustomer().getAddress().setLandMark(editLandmark.getText().toString());
                        order.getCustomer().getAddress().setAddressLine1(editHouseNo.getText().toString());
                        order.getCustomer().getAddress().setAddressLine2(editAddress.getText().toString());
                        order.getCustomer().getAddress().setCity(editCity.getText().toString());
                        Gson gson = new Gson();
                        String strOrder = gson.toJson(order);
                        postOrder(strOrder);
                    }
                    break;
                    case DialogInterface.BUTTON_NEGATIVE:
//                        Toast.makeText(getApplicationContext(), "Correct the Information", Toast.LENGTH_LONG).show();
                        break;
                    case DialogInterface.BUTTON_NEUTRAL:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Fruity");
        if (yesnotype) {

            builder.setMessage(message).setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        } else {
            builder.setMessage(message).setNeutralButton("Ok", dialogClickListener)
                    .setIcon(R.drawable.ic_action_about).show();

        }
    }
    private static boolean validatePhoneNumber(String phoneNo)
    {
        if (phoneNo.matches("\\d{10}"))
            return true;
        else if(phoneNo.matches("\\+\\d{12}")) return true;
        else return false;
    }
    public void postOrder(String order)
    {
        new PostJSONAsyncTask().execute(Constants.ORDER_URL, order);
    }
    public  class PostJSONAsyncTask extends AsyncTask<String, Void, Boolean> {
        Dialog dialog;
        public  PostJSONAsyncTask()
        {
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new Dialog(CutomerEnterDetailsActivity.this,android.R.style.Theme_Translucent);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom_progress_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.show();
            dialog.setCancelable(true);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {
                ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                HttpPost request = new HttpPost(urls[0]);
                HttpClient httpclient = new DefaultHttpClient();
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
                StringEntity se = new StringEntity(urls[1]);
                request.setEntity(se);
                request.setHeader("Accept", "application/json");
                request.setHeader("Content-type", "application/json");
                request.setHeader(Constants.SECUREKEY_KEY, Constants.SECUREKEY_VALUE);
                request.setHeader(Constants.VERSION_KEY, Constants.VERSION_VALUE);
                request.setHeader(Constants.CLIENT_KEY, Constants.CLIENT_VALUE);
                HttpResponse response = httpclient.execute(request);

                int status = response.getStatusLine().getStatusCode();
                if (status == 200) {
                    HttpEntity entity = response.getEntity();

                    responseOrder = EntityUtils.toString(entity);
                    return true;
                }
            } catch (ParseException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
        protected void onPostExecute(Boolean result) {

            dialog.cancel();
            if(result == true){
                Intent i = new Intent(CutomerEnterDetailsActivity.this, FinishActivity.class);
                i.putExtra("order", responseOrder);
                i.putExtra("HotelDetail",strHotelDetail);
                startActivity(i);
                finish();
            }
            else if (result == false)
                Toast.makeText(getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(isCartActivityStarted == false) {
            Intent start = new Intent(CutomerEnterDetailsActivity.this, MainActivity.class);
            start.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(start);
            finish();
        }
    }
}