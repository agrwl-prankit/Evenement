package com.prankit.evenement;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.prankit.evenement.fragments.EventsFragment;
import com.prankit.evenement.fragments.MyEventFragment;

public class TabAdapter extends FragmentPagerAdapter {

    Context context;
    int totalTabs;

    public TabAdapter(Context c, FragmentManager fm, int totalTabs){
        super(fm);
        context = c;
        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            return new MyEventFragment();
        }
        return new EventsFragment();
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
