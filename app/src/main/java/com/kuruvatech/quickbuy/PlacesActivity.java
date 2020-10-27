package com.kuruvatech.quickbuy;

import android.app.Activity;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.splunk.mint.Mint;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.kuruvatech.quickbuy.adapter.LocationAdapter;
import com.kuruvatech.quickbuy.utils.Constants;
import com.kuruvatech.quickbuy.utils.SessionManager;
/**
 * Created by dganeshappa on 7/14/2016.
 */
public class PlacesActivity extends AppCompatActivity{

    private static final String TAG_SUBAREAS = "subAreas";
    private static final String TAG_SLIDERS = "sliders";
    private static final String TAG_LOGO1 = "logo1";
    private static final String TAG_LOGO2 = "logo2";
    private static final String TAG_LOGO3 = "logo3";
    private static final String TAG_LOGO4 = "logo4";
    private static final String TAG_NAME = "name";


    private ArrayList<String> mCityCoverage;
    ListView listView;
    LinearLayout navigation;
    //ListView addresslistview;
    SearchView search;
    LocationAdapter dataAdapter;
//    ArrayList<FavouriteAddress> mFavouriteAddressArrayList;
//    AddressListAdapater addressListAdapater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mint.initAndStartSession(this, "49d903c2");
        setContentView(R.layout.activity_place);
        navigation=(LinearLayout) findViewById(R.id.location);
        mCityCoverage =  new ArrayList<String>();
        listView = (ListView) findViewById(R.id.area_listView);
        listView.setAdapter(dataAdapter);
        listView.setTextFilterEnabled(true);



//        mFavouriteAddressArrayList = new ArrayList<FavouriteAddress>();
//        if(session.getFavoutrateAddress() !=null) {
//            mFavouriteAddressArrayList = session.getFavoutrateAddress();
//            //addressListAdapater.setmFavouriteAddressArrayList(mFavouriteAddressArrayList);
//        }
//        addressListAdapater = new AddressListAdapater(this,R.layout.address_list_item,mFavouriteAddressArrayList);
//        addresslistview = (ListView) findViewById(R.id.listView_address);
//        addresslistview.setAdapter(addressListAdapater);


//        addresslistview.setEmptyView(findViewById(R.id.emptyElement));
//        addresslistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//               String areaName =  mFavouriteAddressArrayList.get(position).getAddress().getAreaName();
//               goBackwithAreaName(areaName);
//            }
//        });
        getCityCoverage();
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);


        search = (SearchView)findViewById(R.id.searchView1);

        search.setSearchableInfo( searchManager.getSearchableInfo(getComponentName()) );


        search.setIconified(false);

        search.setQueryHint("Search Location");

        search.setIconified(false);
        setToolBar("Select Delivery Location");
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                if(dataAdapter!= null)
                {
                    dataAdapter.filter(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if(dataAdapter!=null)
                {
                //    dataAdapter.filter(query);
                    dataAdapter.filter(query.toString().trim());
                    listView.invalidate();
                    return true;
                }
                return false;
            }

        });
        search.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    search.setIconified(true);
                }
            }

        });

        navigation.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(PlacesActivity.this, MapsActivity.class);
                i.putExtra("Uniqid","From_PlacesActivity");
                startActivityForResult(i,1);


            }
        });
    }

    public void onReceiveCity()
    {
        dataAdapter = new LocationAdapter(this,
                R.layout.area_list,mCityCoverage);

        listView.setAdapter(dataAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goBackwithAreaName(mCityCoverage.get(position));
            }
        });

    }
    public void goBackwithLatitudeLongitude(String latitude, String longitude)
    {

        Intent intent = new Intent();
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        intent.putExtra("area", "knvl");
       // Toast.makeText(getApplicationContext(), longitude+" kdb "+latitude, Toast.LENGTH_LONG).show();
        setResult(RESULT_OK, intent);
        finish();
    }
    public void goBackwithAreaName(String areaname)
    {
        SessionManager session = new SessionManager(getApplicationContext());
        session.setlastareasearched(areaname);
        Intent intent = new Intent();
        intent.putExtra("area", areaname);
        setResult(RESULT_OK, intent);
        finish();
    }
    public void getCityCoverage()
    {
        mCityCoverage.clear();
        new JSONAsyncTask().execute(Constants.GET_COVERAGE_AREAS);
    }

    public  class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        Dialog dialog;

        public  JSONAsyncTask()
        {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new Dialog(PlacesActivity.this,android.R.style.Theme_Translucent);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom_progress_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.show();
            dialog.setCancelable(true);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {

                //------------------>>
                HttpGet request = new HttpGet(urls[0]);
                HttpClient httpclient = new DefaultHttpClient();
                request.addHeader(Constants.SECUREKEY_KEY, Constants.SECUREKEY_VALUE);
                request.addHeader(Constants.VERSION_KEY, Constants.VERSION_VALUE);
                request.addHeader(Constants.CLIENT_KEY, Constants.CLIENT_VALUE);
                HttpResponse response = httpclient.execute(request);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();

                    String data = EntityUtils.toString(entity);
                    JSONArray jarray = new JSONArray(data);

                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject object = jarray.getJSONObject(i);

                        if(object.has(TAG_SUBAREAS)) {
                            JSONArray subAreasArray = object.getJSONArray(TAG_SUBAREAS);
                            for (int j = 0; j < subAreasArray.length(); j++) {
                                JSONObject city_object = subAreasArray.getJSONObject(j);
                                if(city_object.has(TAG_NAME)) {
                                    mCityCoverage.add(city_object.get(TAG_NAME).toString());
                                }
                            }
                        }
                        if(object.has(TAG_SLIDERS)) {
                            JSONObject slider_object = object.getJSONObject(TAG_SLIDERS);
                            SessionManager  session = new SessionManager(getApplicationContext());
                            List<String> logos = new ArrayList<String>();
                            logos.clear();
                            logos.add(slider_object.getString("logo1"));
                            logos.add(slider_object.getString("logo2"));
                            logos.add(slider_object.getString("logo3"));
                            logos.add(slider_object.getString("logo4"));
                            session.setSlider(logos);

                        }
                    }
                    return true;
                }
            }  catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            dialog.cancel();
            if (result == false)
                Toast.makeText(getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();
            else
            {
                onReceiveCity();
            }

        }
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
                search.onActionViewCollapsed();
                onBackPressed();
                return true;

            default:

               return super.onOptionsItemSelected(item);


        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            String longitude=new String(intent.getStringExtra("longitude"));
            String latitude=new String(intent.getStringExtra("latitude"));
            goBackwithLatitudeLongitude(latitude,longitude);

        }
    }

}
