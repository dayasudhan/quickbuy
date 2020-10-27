package com.kuruvatech.quickbuy;

/*
 * Copyright (C) 2015, Francesco Azzola
 *
 *(http://www.survivingwithandroid.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * 20/10/15
 */

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.kuruvatech.quickbuy.model.Tracker;
import com.kuruvatech.quickbuy.utils.Constants;
import com.kuruvatech.quickbuy.utils.SessionManager;

public class StatusTrackerFragment extends Fragment {

    Button btnStatus,btnOrderDetail;
    TextView txtViewTracker;
    EditText ed;
    private static final String TAG_TRACKER = "tracker";
    private static final String TAG_CUSTOMER = "customer";
    private static final String TAG_ID = "id";
    private static final String TAG_ID2 = "_id";
    private static final String TAG_NAME = "name";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_PHONE = "phone";
    private static final String TAG_CURRENT_STATUS = "current_status";
    private static final String TAG_MENU = "menu";
    private static final String TAG_BILL_VALUE = "bill_value";
    private static final String TAG_DELIVERY_CHARGE = "deliveryCharge";
    private static final String TAG_TOTAL_COST = "totalCost";
    ArrayList<Tracker> trackerDetails;
    SessionManager session;
    ListView listView ;
    String responseOrder;
    boolean isHistoryClicked;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        View v = inflater.inflate(R.layout.statuslist_layout, container, false);
        ed = (EditText)v.findViewById(R.id.editText);
        txtViewTracker = (TextView) v.findViewById(R.id.statusText);

        isHistoryClicked = false;
        session = new SessionManager(getActivity().getApplicationContext());
        ed.setText(session.getCurrentOrderId());
        ed.setKeyListener(null);
        btnStatus = (Button)v.findViewById(R.id.status_button);
        btnOrderDetail = (Button)v.findViewById(R.id.order_detail);
        trackerDetails = new ArrayList<Tracker>();
        ((MainActivity) getActivity())
                .setActionBarTitle("Order Tracker");
        btnStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                isHistoryClicked = false;
                if(ed.getText().length() > 0 && !(ed.getText().toString().contains(" ")))
                {
                    getStatus(ed.getText().toString());
                }
                else
                {
                    alertMessage("Please enter valid Order id");
                }
            }
        });
        btnOrderDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {


                if(ed.getText().length() > 0 && !(ed.getText().toString().contains(" ")))
                {
                    isHistoryClicked = true;
                    getStatus(ed.getText().toString().trim());
                }
                else
                {
                    alertMessage("Please enter valid Order Id");
                }
            }
        });
        final List<String> orderList = session.getOrderIdList();
        if(orderList != null) {
            listView = (ListView) v.findViewById(R.id.listView_status);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                    R.layout.area_list, R.id.location_name, orderList);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ed.setText(orderList.get(position));
                }
            });

        }
        return v;
    }

    public void getStatus(String orderId )
    {
       String order_url = Constants.GET_STATUS_URL;
       order_url= order_url.concat(orderId);
       new JSONAsyncTask().execute(order_url);
    }
public void updateStatus()
{
     if(!trackerDetails.isEmpty()) {
        String trackerItemStr = "";
        for (int j = 0; j < trackerDetails.size(); j++) {
            SimpleDateFormat existingUTCFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat requiredFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date getDate = null;
            try {
                getDate = existingUTCFormat.parse(trackerDetails.get(j).getTime());
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(getDate);
            String newTime = requiredFormat.format(cal.getTime());
            trackerItemStr += trackerDetails.get(j).getStatus() + " (" + newTime + ")" + '\n';
        }
        txtViewTracker.setText(trackerItemStr);
        if(isHistoryClicked ==  true)
        {

                Intent i = new Intent(getActivity(), OrderHistory.class);
                i.putExtra("order", responseOrder);
                i.putExtra("txtViewTracker",trackerItemStr);
                startActivity(i);
            //    finish();

        }
    }
    else
     {
         alertMessage("Order empty for this OrderId");
     }

}
    public  class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        Dialog dialog;
        public  JSONAsyncTask()
        {

        }

        @Override
        protected void onPreExecute() {
            dialog = new Dialog(getActivity(),android.R.style.Theme_Translucent);
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
                    responseOrder = data;
                    JSONArray jarray = new JSONArray(data);
                    trackerDetails.clear();
                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject object = jarray.getJSONObject(i);
                        responseOrder = object.toString();
                        if(object.has(TAG_TRACKER))
                        {
                            trackerDetails.clear();
                            trackerDetails = new ArrayList<Tracker>();
                            JSONArray trackerarr =  object.getJSONArray(TAG_TRACKER);
                            for (int j = 0; j < trackerarr.length(); j++) {
                                JSONObject trackerobject = trackerarr.getJSONObject(j);
                                Tracker tracker = new Tracker();
                                tracker.setStatus(trackerobject.getString("status"));
                                tracker.setTime(trackerobject.getString("time"));
                                if(trackerobject.has("reason"))
                                {
                                    tracker.setReason(trackerobject.getString("reason"));
                                }
                                if(j ==0)
                                {
                                    Date getDate = null;
                                    SimpleDateFormat existingUTCFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                    try {
                                        getDate = existingUTCFormat.parse(tracker.getTime());
                                    } catch (java.text.ParseException e) {
                                        e.printStackTrace();
                                    }
                                  }
                                trackerDetails.add(tracker);
                            }

                        }

                    }
                    return true;
                }
            } catch (ParseException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            dialog.cancel();
            updateStatus();
            if (result == false)
                Toast.makeText(getActivity().getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();

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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Fruity");
        builder.setMessage(message).setNeutralButton("Ok", dialogClickListeneryesno)
                .setIcon(R.drawable.ic_action_about).show();

    }
}
