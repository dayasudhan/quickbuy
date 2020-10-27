package com.kuruvatech.quickbuy;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

import com.kuruvatech.quickbuy.utils.Constants;


/**
 * Created by dganeshappa on 6/4/2016.
 */

public class AboutKhaanavali extends Fragment {

    View rootview;
    Button faq,aboutus,contactUs,tandC;
    WebView popUpFaq;
    // Session Manager Class


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.about_khaanavali, container, false);
        ((MainActivity) getActivity())
                .setActionBarTitle("About Fruity");
        faq=(Button) rootview.findViewById(R.id.faq);
        aboutus=(Button) rootview.findViewById(R.id.aboutus);
        tandC=(Button) rootview.findViewById(R.id.tandc);
        contactUs=(Button) rootview.findViewById(R.id.contactus);
        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog1 =new Dialog(getContext());
                dialog1.setContentView(R.layout.aboutuspopup);
                popUpFaq =(WebView) dialog1.findViewById(R.id.popup);
                popUpFaq.loadUrl(Constants.ABOUT_US_URL);

                dialog1.show();

            }

            });
        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog1 =new Dialog(getContext());
                dialog1.setContentView(R.layout.aboutuspopup);

                popUpFaq =(WebView) dialog1.findViewById(R.id.popup);
                popUpFaq.loadUrl(Constants.FAQ_URL);

                dialog1.show();
            }

        });
        contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog1 =new Dialog(getContext());
                dialog1.setContentView(R.layout.aboutuspopup);

                popUpFaq =(WebView) dialog1.findViewById(R.id.popup);
                popUpFaq.loadUrl(Constants.CONTACT_US_URL);

                dialog1.show();
            }

        });
        tandC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog1 =new Dialog(getContext());
                dialog1.setContentView(R.layout.aboutuspopup);

                popUpFaq =(WebView) dialog1.findViewById(R.id.popup);
                popUpFaq.loadUrl(Constants.TERMS_AND_CONDITION_URL);

                dialog1.show();
            }

        });





        return rootview;
    }



}
