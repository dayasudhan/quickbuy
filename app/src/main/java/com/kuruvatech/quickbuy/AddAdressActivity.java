package com.kuruvatech.quickbuy;

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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.splunk.mint.Mint;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.kuruvatech.quickbuy.model.Address;
import com.kuruvatech.quickbuy.model.FavouriteAddress;
import com.kuruvatech.quickbuy.utils.Constants;
import com.kuruvatech.quickbuy.utils.SessionManager;

/**
 * Created by dganeshappa on 7/26/2016.
 */
public class AddAdressActivity  extends AppCompatActivity {
    EditText editTagLabel,editCity,editHouseNo,editAreaName,editLandmark,editAddress;
    Button btnSave;
    Address address;
    String locationaddress;
    List<android.location.Address> mAddresses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mint.initAndStartSession(this, "49d903c2");
        setContentView(R.layout.add_address_layout);
        Intent intent = getIntent();
        Gson gson = new Gson();
        Type listType = new TypeToken<List<android.location.Address>>() {}.getType();
        mAddresses = gson.fromJson(intent.getStringExtra("locationaddress"), listType);
    //    String strAddress = intent.getStringExtra("address");

        editCity=(EditText)findViewById(R.id.orderDetailEmail);
        editHouseNo=(EditText)findViewById(R.id.orderDetailAddress_house_no);
        editAreaName=(EditText)findViewById(R.id.orderDetailAddress_areaname);
        editLandmark=(EditText)findViewById(R.id.orderDetailAddress_landmark);
        editAddress=(EditText)findViewById(R.id.orderDetailAddress_address);
        editTagLabel=(EditText)findViewById(R.id.tag_address_label);
        if(mAddresses != null) {
            editAddress.setText(mAddresses.get(0).getAddressLine(1));
            editCity.setText(mAddresses.get(0).getLocality());
            editAreaName.setText(mAddresses.get(0).getSubLocality());
        }
        btnSave= (Button) findViewById(R.id.saveAddressbutton);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String house =editHouseNo.getText().toString();
                String addresss=editAddress.getText().toString();
                String areaname =editAreaName.getText().toString();
                String landmark =editLandmark.getText().toString();
                String tagLabel =editTagLabel.getText().toString();
                if(house.trim().length() == 0){
                    //Toast.makeText(getApplicationContext(), "Enter House No or Flat No ", Toast.LENGTH_LONG).show();
                    editHouseNo.setFocusableInTouchMode(true);
                    editHouseNo.requestFocus();
                    alertMessage("Enter House or Flat No ");
                }
                else if(addresss.trim().length() <= 0){
                    editAddress.setFocusableInTouchMode(true);
                    editAddress.requestFocus();
                    alertMessage("Enter adress ");
                }
                else if(areaname.trim().length() <= 0){
                    editAreaName.setFocusableInTouchMode(true);
                    editAreaName.requestFocus();
                    alertMessage("Enter Areaname ");
                }
                else if(landmark.trim().length()<= 0){
                    editLandmark.setFocusableInTouchMode(true);
                    editLandmark.requestFocus();
                    alertMessage("Enter Landmark/locality ");
                }

                else if(tagLabel.trim().length() <= 0){
                    editTagLabel.setFocusableInTouchMode(true);
                    editTagLabel.requestFocus();
                    alertMessage("Enter lable for this address");
                }
                else
                {
                    address = new Address();
                    address.setAreaName(editAreaName.getText().toString().trim());
                    address.setLandMark(editLandmark.getText().toString().trim());
                    address.setAddressLine1(editHouseNo.getText().toString().trim());
                    address.setAddressLine2(editAddress.getText().toString().trim());
                    address.setCity(editCity.getText().toString().trim());
                    if(mAddresses != null) {
                        address.setZip(mAddresses.get(0).getPostalCode());
                        address.setLatitude(String.valueOf(mAddresses.get(0).getLatitude()));
                        address.setLongitude(String.valueOf(mAddresses.get(0).getLongitude()));
                    }
                    FavouriteAddress favouriteAddress = new FavouriteAddress();
                    favouriteAddress.setLabel(editTagLabel.getText().toString().trim());
                    favouriteAddress.setAddress(address);




                    SessionManager session = new SessionManager(getApplicationContext());
                    session.setFavoutrateAddress(favouriteAddress);
                    session.setHasAddress(true);
                    Gson gson = new Gson();
                    String strAddress = gson.toJson(favouriteAddress);
                    postAddressToServer(strAddress,session.getKeyPhone());
                    locationaddress = gson.toJson(address);

                }
            }
        });
        setToolBar("Add Address");
    }


    public void postAddressToServer(String address,String phone)
    {
        String url = Constants.CUSTOMER_ADDRESS_URL +  phone;
        new PostJSONAsyncTask().execute(url, address);
    }
    public  class PostJSONAsyncTask extends AsyncTask<String, Void, Boolean> {
        Dialog dialog;
        public  PostJSONAsyncTask()
        {
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new Dialog(AddAdressActivity.this,android.R.style.Theme_Translucent);
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
                request.setHeader("Accept", "pplication/json");
                request.setHeader("Content-type", "application/json");
                request.setHeader(Constants.SECUREKEY_KEY, Constants.SECUREKEY_VALUE);
                request.setHeader(Constants.VERSION_KEY, Constants.VERSION_VALUE);
                request.setHeader(Constants.CLIENT_KEY, Constants.CLIENT_VALUE);
                HttpResponse response = httpclient.execute(request);

                int status = response.getStatusLine().getStatusCode();
                if (status == 200) {
                    HttpEntity entity = response.getEntity();
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
            Intent intent = new Intent();
            intent.putExtra("locationaddress", locationaddress);
            setResult(RESULT_OK, intent);
            finish();
            if(result == true){
            }
            else if (result == false)
                Toast.makeText(getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();
        }
    }


    private void setToolBar(String title) {
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(tb);
        tb.setTitleTextColor(Color.rgb(Constants.TITLE_TEXT_COLOR_RED,
                Constants.TITLE_TEXT_COLOR_GREEN, Constants.TITLE_TEXT_COLOR_BLUE));
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_action_back);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(title);
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

        Intent start = new Intent(AddAdressActivity.this,SelectAddressActivity.class);
        start.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(start);
        finish();
    }
}
