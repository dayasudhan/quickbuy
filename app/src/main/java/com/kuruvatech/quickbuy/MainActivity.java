package com.kuruvatech.quickbuy;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.splunk.mint.Mint;
import com.squareup.picasso.Picasso;

import com.kuruvatech.quickbuy.utils.Constants;
import com.kuruvatech.quickbuy.utils.SessionManager;


public class MainActivity extends AppCompatActivity {

    RelativeLayout navHead;
    private DrawerLayout dLayout;
    TextView name,email,phno;
    private boolean ishotelFragmentOpen;
    private boolean isdrawerbackpressed;
    private boolean isFirst=true;
    Fragment fragment=null;
    String notification;
    SessionManager session;


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;



    public boolean isOnline(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            Toast.makeText(context, "No Internet connection!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session= new SessionManager(getApplicationContext());
        Mint.initAndStartSession(this, "49d903c2");
        setContentView(R.layout.activity_main_nav);

        notification=getIntent().getStringExtra("notificationFragment");
        final Dialog dialog1 =new Dialog(MainActivity.this);
        dialog1.setContentView(R.layout.notification);
        ImageView notif = (ImageView) dialog1.findViewById(R.id.notifimg);
        if(dialog1!=null) {
                if (notification!=null &&notification.equals("accepted")) {
                    Picasso.with(getApplicationContext()).load(Constants.ACCEPTED_URL).centerInside().into(notif);
                }else if (notification!=null &&notification.equals("rejected")){
                    Picasso.with(getApplicationContext()).load(Constants.REJECTED_URL).centerCrop().into(notif);
                }else if(notification!=null && notification.equals("notify")){
                    Picasso.with(getApplicationContext()).load(Constants.NOTIFICATION_URL).into(notif);
                }
            dialog1.show();
            if(notification==null){
                    dialog1.cancel();
                }

        }
        ishotelFragmentOpen = true;
        isdrawerbackpressed =  false;
        //gaganwelcome
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.rgb(Constants.TITLE_TEXT_COLOR_RED,
                Constants.TITLE_TEXT_COLOR_GREEN, Constants.TITLE_TEXT_COLOR_BLUE));;
        setSupportActionBar(toolbar);


        //gaganwelcome
        //gagan internet
        if (!isOnline(MainActivity.this))
        {
            try {
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();

                alertDialog.setTitle("Info");
                alertDialog.setMessage("Internet not available, Cross check your internet connectivity and try again");
                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();

                    }
                });

                alertDialog.show();
            }
            catch(Exception e)
            {

            }
        }


        //gagan internet


        setNavigationDrawer();
        setToolBar();
//        if (!checkNotificationListenerServiceRunning()) {
//            startService(new Intent(this, NotificationListener.class));
//        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
       // client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

//    public boolean checkNotificationListenerServiceRunning() {
//        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
//            if ("com.kuruvatech.fruity.NotificationListener"
//                    .equals(service.service.getClassName())) {
//                return true;
//            }
//        }
//        return false;
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            // extract data
            String areaClicked = new String(intent.getStringExtra("area"));
            HotelFragment fragment = null;
            try {
                fragment = (HotelFragment) getSupportFragmentManager().findFragmentById(R.id.frame);
                if(!areaClicked.equals("knvl")) {

                    fragment.getHotelList(areaClicked);
                    //getHotelList(areaClicked);
                }
                else {
                    String longitude, latitude;

                    longitude = new String(intent.getStringExtra("longitude"));

                    latitude = new String(intent.getStringExtra("latitude"));
                 //   Toast.makeText(getApplicationContext(), longitude+" kdb "+latitude, Toast.LENGTH_LONG).show();
                    fragment.getHotelListByGPS(latitude,longitude);
                }
            } catch (ClassCastException e){
                //don't need to crash at this point,
                //just let the user know that a wrong file has been passed.
            }


        }
    }

    private void setToolBar() {
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setNavigationDrawer() {
        dLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.navigation);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment frag = null;



        View hView =  navView.inflateHeaderView(R.layout.header);
        navHead = (RelativeLayout) hView.findViewById(R.id.profileinfo);
        name = (TextView) hView.findViewById(R.id.myNameHeader);
        phno = (TextView) hView.findViewById(R.id.phNoHeader);
        email = (TextView)hView.findViewById(R.id.eMailHeader);

        name.setText(session.getName());
        phno.setText(session.getKeyPhone());
        email.setText(session.getEmail());
        if(session.isLoggedIn()) {
            navHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   isdrawerbackpressed = false;
                    Fragment frag=null;
                    frag = new MyProfile();
                    ishotelFragmentOpen = false;
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                    transaction.replace(R.id.frame, frag);
                    transaction.commit();
                    dLayout.closeDrawers();
                }
            });
        }
        //    transaction.addToBackStack(null);
        transaction.replace(R.id.frame, new HotelFragment());
        ishotelFragmentOpen = true;
        transaction.commit();


        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                 Fragment frag = null;
                isdrawerbackpressed = false;
                int itemId = menuItem.getItemId();

                if (itemId == R.id.hotel) {
                    frag = new HotelFragment();

                    ((HotelFragment) frag).setBulk(false);
                    ishotelFragmentOpen = true;

                }else  if (itemId == R.id.notification) {
                    frag = new Notification();
                    ishotelFragmentOpen = false;
                }
//                else if (itemId == R.id.bulk_activity) {
//                    frag = new HotelFragment();
//                    ((HotelFragment) frag).setBulk(true);
//                    ishotelFragmentOpen = true;
//                }
//                else if (itemId == R.id.about_knvl) {
//
//                    frag = new AboutFruity();
//                    ishotelFragmentOpen = false;
//
//                }
                else if (itemId == R.id.status) {
                    frag = new StatusTrackerFragment();
                    ishotelFragmentOpen = false;
                } else /*if (itemId == R.id.profileinfo) {
                                if(isFirst==true) {
                        fragment=new MyProfile();
                        ishotelFragmentOpen = false;
                        isFirst=false;
                    }
                } else*/ if (itemId == R.id.invite) {
                    frag = new ShareAppFragment();
                    ishotelFragmentOpen = false;
                }
                if (frag != null ) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                        transaction.replace(R.id.frame, frag);

//
//      if(itemId != R.id.location) {
//                    transaction.addToBackStack(null);
//                }
                    transaction.commit();

                    dLayout.closeDrawers();
                    return true;
                }

                return false;
            }
        });
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        String btnName = null;

        switch (itemId) {
            case android.R.id.home: {
                dLayout.openDrawer(GravityCompat.START);
                return true;
            }
            case R.id.menu_search: {
                //  Toast.makeText(getApplicationContext(), "menu selected", Toast.LENGTH_LONG).show();
                Intent i = new Intent(this, PlacesActivity.class);
                startActivityForResult(i, 1);

                return true;
            }


        }
        return true;
    }

    //
//    @Override
//    public boolean onCreateOptionsMenu(android.view.Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.home_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (dLayout.isDrawerOpen(GravityCompat.START)) {
            dLayout.closeDrawer(GravityCompat.START);
        } else if (ishotelFragmentOpen == false) {
            if(!isdrawerbackpressed) {
                dLayout.openDrawer(GravityCompat.START);
                isdrawerbackpressed = true;
            }
            else {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, new HotelFragment());
                ishotelFragmentOpen = true;
                transaction.commit();
                isdrawerbackpressed = false;
            }
        } else {
            //super.onBackPressed();
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

}
