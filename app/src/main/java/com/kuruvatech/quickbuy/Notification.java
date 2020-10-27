package com.kuruvatech.quickbuy;


import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import com.kuruvatech.quickbuy.utils.Constants;

/**
 * Created by Gagan on 8/30/2016.
 */
public class Notification extends Fragment {

    View rootview;
    ImageView notifImage;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.notification, container, false);
        ((MainActivity) getActivity())
                .setActionBarTitle("Notifications");
        final Dialog dialog1 =new Dialog(getContext());
        dialog1.setContentView(R.layout.notification);
        notifImage=(ImageView)dialog1.findViewById(R.id.notifimg);
        Picasso.with(getContext()).load(Constants.NOTIFICATION_URL).into(notifImage);
        dialog1.show();

        return rootview;
    }




}
