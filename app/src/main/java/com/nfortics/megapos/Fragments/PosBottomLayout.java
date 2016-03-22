package com.nfortics.megapos.Fragments;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nfortics.megapos.Adapters.ViewPagerAdapter;
import com.nfortics.megapos.Fragments.InnerFragments.LastFragment;
import com.nfortics.megapos.Fragments.InnerFragments.MiddleFragment;
import com.nfortics.megapos.Fragments.InnerFragments.StartFragment;
import com.nfortics.megapos.R;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class PosBottomLayout extends Fragment {

    ViewPager viewPager;
    FragmentManager fragmentManager;
    public PosBottomLayout() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //viewPager=(ViewPager)layout.findViewById(R.id.bottomViewpager);
        //setUpTabs();
        //  FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
        //  viewPager.setAdapter(new ViewPagerAdapter(fragmentManager));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentManager = getActivity().getSupportFragmentManager();

        viewPager.setAdapter(new ViewPagerAdapter(fragmentManager));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

            View layout=inflater.inflate(R.layout.fragment_pos_bottom_layout, container, false);


        viewPager=(ViewPager)layout.findViewById(R.id.bottomViewpager);



       return layout;
    }

  /**
    class ViewPagerAdapter extends FragmentPagerAdapter {

        Fragment fragment=null;
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {

            if(i==0)
            {

               // actionButton.setVisibility(View.GONE);
                fragment=new LastFragment();

            }     if(i==1)
            {
               // actionButton.setVisibility(View.VISIBLE);
                fragment=new StartFragment();

            }if(i==2)
            {
               // actionButton.setVisibility(View.GONE);
                fragment=new MiddleFragment();
                //  new connectOptician().execute();

            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
**/
}
