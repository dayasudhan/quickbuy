package com.kuruvatech.quickbuy;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.splunk.mint.Mint;

import java.util.ArrayList;

import com.kuruvatech.quickbuy.adapter.AddressListAdapater;
import com.kuruvatech.quickbuy.model.Address;
import com.kuruvatech.quickbuy.model.FavouriteAddress;
import com.kuruvatech.quickbuy.utils.Constants;
import com.kuruvatech.quickbuy.utils.SessionManager;


/**
 * Created by dganeshappa on 7/14/2016.
 */
public class SelectAddressActivity extends AppCompatActivity{

    private ArrayList<String> mCityCoverage;
    ListView addresslistview;
    Button btnAddNewAddress;
//    LocationAdapter dataAdapter;
    ArrayList<FavouriteAddress> mFavouriteAddressArrayList;
    AddressListAdapater addressListAdapater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mint.initAndStartSession(this, "49d903c2");
        setContentView(R.layout.select_address);
        if(addressListAdapater!=null)
                addressListAdapater.notifyDataSetChanged();

        btnAddNewAddress = (Button) findViewById(R.id.addnewaddress);
        final SessionManager  session = new SessionManager(getApplicationContext());
        mFavouriteAddressArrayList = new ArrayList<FavouriteAddress>();
        if(session.getFavoutrateAddress() !=null) {
            mFavouriteAddressArrayList = session.getFavoutrateAddress();
        }
        final Handler handler = new Handler();
        handler.postDelayed( new Runnable() {

            @Override
            public void run() {
                addressListAdapater.notifyDataSetChanged();
                handler.postDelayed( this, 1 * 1000 );
            }
        }, 1 );

        addressListAdapater = new AddressListAdapater(this,R.layout.address_list_item,mFavouriteAddressArrayList);
        addresslistview = (ListView) findViewById(R.id.listView_address);
        addresslistview.setAdapter(addressListAdapater);
        addresslistview.setEmptyView(findViewById(R.id.emptyElement));
        addresslistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Address locaddress =  mFavouriteAddressArrayList.get(position).getAddress();
                goBackwithAddress(locaddress);
            }
        });
        btnAddNewAddress.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if(session.isHasAddress()) {
                                                        if (session.getFavoutrateAddress().size() < 5) {
                                                            Intent i = new Intent(SelectAddressActivity.this, MapsActivity.class);
                                                            i.putExtra("Uniqid","From_SelectAddressActivity");
                                                            startActivityForResult(i, 1);


                                                        }else{
                                                            Toast.makeText(getApplicationContext(), "cant add more than 5", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                    else{
                                                        Intent i = new Intent(SelectAddressActivity.this, MapsActivity.class);
                                                        i.putExtra("Uniqid","From_SelectAddressActivity");
                                                        startActivityForResult(i, 1);

                                                    }
                                                }
        });
        setToolBar("Select address");
    }

    public void goBackwithAddress(Address locaddress)
    {

        Intent intent = new Intent();
        Gson gson = new Gson();
        String locationaddress = gson.toJson(locaddress);
        intent.putExtra("locationaddress", locationaddress);
        setResult(RESULT_OK, intent);
        finish();
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
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK) {
            intent.putExtra("locationaddress", intent.getStringExtra("locationaddress"));
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
