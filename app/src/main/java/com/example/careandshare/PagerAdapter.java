package com.example.careandshare;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {
    private int numoftabs;
    public  PagerAdapter(FragmentManager fm, int numofTabs){
        super(fm);

        this.numoftabs  = numofTabs;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

switch (position){

    case 0: return new Chat_Fragment();
    case 1 : return new Status_Fragement();
    case 2: return new Calls_FRagment();
    default: return null;


          }

    }

    @Override
    public int getCount() {
        return numoftabs;
    }
}
