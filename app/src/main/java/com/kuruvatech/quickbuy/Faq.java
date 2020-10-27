package com.kuruvatech.quickbuy;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.kuruvatech.quickbuy.adapter.ExpandableListAdapter;

/**
 * Created by Gagan on 9/14/2016.
 */
public class Faq extends AppCompatActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_faq);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("question1");
        listDataHeader.add("question2");
        listDataHeader.add("question3");

        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("Ansewr1");


        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("Answer2");


        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("answer3");


        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
    }
}
