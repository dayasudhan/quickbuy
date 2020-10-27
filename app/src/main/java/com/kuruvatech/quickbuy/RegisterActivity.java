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
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.splunk.mint.Mint;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;

import com.kuruvatech.quickbuy.utils.Constants;

/**
 * Created by dganeshappa on 8/5/2016.
 */
public class RegisterActivity extends AppCompatActivity {
    Button btnSubmit;
    private EditText phoneNumber,editName,editemail;
    private String apiReponse;
    private String order ,hotelDetail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mint.initAndStartSession(this, "49d903c2");
        setContentView(R.layout.register_layout);
        Intent intent = getIntent();

        order = intent.getStringExtra("order");
        hotelDetail = intent.getStringExtra("HotelDetail");

        phoneNumber = (EditText)findViewById(R.id.phoneInput);
        editName=(EditText)findViewById(R.id.nameregisterinput);
        editemail = (EditText)findViewById(R.id.emailregisterinput);
        btnSubmit= (Button) findViewById(R.id.registerSubmitButton);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                String name=editName.getText().toString();
                String email=editemail.getText().toString();


                if (!validatePhoneNumber(phoneNumber.getText().toString())) {
                    phoneNumber.setFocusableInTouchMode(true);
                    phoneNumber.requestFocus();
                    alertMessage("Enter Valid Phone Number");
                }
                else if(name.trim().length() == 0){
                    editName.setFocusableInTouchMode(true);
                    editName.requestFocus();
                    alertMessage("Enter Name");
                }
//                else if(!validateEmail(editemail.getText().toString()))
//                {
//                    editemail.setFocusableInTouchMode(true);
//                    editemail.requestFocus();
//                    alertMessage("Enter email");
//                }
//                else
//                {
//                    registerUser(phoneNumber.getText().toString(),editName.getText().toString(),editemail.getText().toString(),"deviceid");
//                }
                else
                {
                    Intent i = new Intent(RegisterActivity.this, OtpVeirificationActivity.class);

                    i.putExtra("order", order);
                    i.putExtra("HotelDetail",hotelDetail);
                    i.putExtra("phoneNumber",phoneNumber.getText().toString());
                    i.putExtra("name",editName.getText().toString());
                    i.putExtra("email", editemail.getText().toString());

                    startActivity(i);
                }
               // postReview(hotelid,comment.getText().toString(),rating.getRating());
            }
        });

        setToolBar("Sign In or Register");
    }
    private static boolean validatePhoneNumber(String phoneNo)
    {
        if (phoneNo.matches("\\d{10}"))
            return true;
        else if(phoneNo.matches("\\+\\d{12}")) return true;
        else return false;
    }
    private boolean validateEmail(String email)
    {
        if(email.trim().length() <= 0)
        {
            return false;
        }
        if(email.matches("[a-zA-Z0-9\\.]+@[a-z]+\\.+[a-z]+"))
        {
            return true;
        }
        return false;
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
                .setIcon(R.drawable.ic_action_about).show();

    }

    public void registerUser(String phoneNumber,String name,String email,String deciveId)
    {
        new PostJSONAsyncTask().execute(Constants.OTP_REGISTER_URL, phoneNumber,name,email,deciveId);
    }
    public  class PostJSONAsyncTask extends AsyncTask<String, Void, Boolean> {
        Dialog dialog;
        public  PostJSONAsyncTask()
        {
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new Dialog(RegisterActivity.this,android.R.style.Theme_Translucent);
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
                postParameters.add(new BasicNameValuePair("phoneNumber", urls[1]));
                postParameters.add(new BasicNameValuePair("name", urls[2]));
                postParameters.add(new BasicNameValuePair("email", urls[3]));
                postParameters.add(new BasicNameValuePair("deciveId", urls[4]));

                HttpPost request = new HttpPost(urls[0]);
                request.addHeader(Constants.SECUREKEY_KEY, Constants.SECUREKEY_VALUE);
                request.addHeader(Constants.VERSION_KEY, Constants.VERSION_VALUE);
                request.addHeader(Constants.CLIENT_KEY, Constants.CLIENT_VALUE);
                HttpClient httpclient = new DefaultHttpClient();
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
                request.setEntity(formEntity);
                HttpResponse response = httpclient.execute(request);

                int status = response.getStatusLine().getStatusCode();
                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    apiReponse = EntityUtils.toString(entity);
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
                if(apiReponse.equals("Success"))
                {
                    Intent i = new Intent(RegisterActivity.this, OtpVeirificationActivity.class);

                    i.putExtra("order", order);
                    i.putExtra("HotelDetail",hotelDetail);
                    i.putExtra("phoneNumber",phoneNumber.getText().toString());
                    i.putExtra("name",editName.getText().toString());
                    i.putExtra("email", editemail.getText().toString());

                    startActivity(i);
                }
            }
            else if (result == false)
                Toast.makeText(getApplicationContext(), "Unable to push data to server try once again", Toast.LENGTH_LONG).show();
        }
    }
}

