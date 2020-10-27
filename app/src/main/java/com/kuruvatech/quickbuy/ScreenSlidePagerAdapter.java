package com.kuruvatech.quickbuy;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import com.kuruvatech.quickbuy.utils.SessionManager;

public class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
        private List<String> picList = new ArrayList<>();
    Context mContext;
    public ScreenSlidePagerAdapter(FragmentManager fm , Context context) {

        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int i) {
        SessionManager session = new SessionManager(mContext);
        return ScreenSlidePageFragment.newInstance(session.getSlider().get(i));
    }

    @Override
    public int getCount() {
        return picList.size();
    }

    public void addAll(List<String> picList) {
        this.picList = picList;
    }
}
