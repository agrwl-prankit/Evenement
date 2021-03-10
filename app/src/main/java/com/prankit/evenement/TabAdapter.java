package com.prankit.evenement;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

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
//        switch (position){
//            case 0:
//                AdminChatFragment chatFragment = new AdminChatFragment();
//                return chatFragment;
//            case 1:
//                AdminShowChatFragment showChatFragment = new AdminShowChatFragment();
//                return showChatFragment;
//            default:
//                chatFragment = new AdminChatFragment();
//                return chatFragment;
//        }
        return null;
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
