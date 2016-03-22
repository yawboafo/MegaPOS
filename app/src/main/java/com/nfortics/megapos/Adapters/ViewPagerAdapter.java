package com.nfortics.megapos.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nfortics.megapos.Fragments.InnerFragments.LastFragment;
import com.nfortics.megapos.Fragments.InnerFragments.MiddleFragment;
import com.nfortics.megapos.Fragments.InnerFragments.StartFragment;
import com.nfortics.megapos.Helper.Message;

/**
 * Created by bigifre on 7/20/2015.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    Fragment fragment=null;
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
       // Message.message(ctx, "view pager called");
        //fragment=new LastFragment();
    }

    @Override
    public Fragment getItem(int i) {

        if(i==0)
        {

            // actionButton.setVisibility(View.GONE);
            fragment=new StartFragment();


        }     if(i==1)
        {
            // actionButton.setVisibility(View.VISIBLE);
            fragment=new LastFragment();

        }if(i==2)
        {
            // actionButton.setVisibility(View.GONE);
           // fragment=new MiddleFragment();
            //  new connectOptician().execute();

        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
