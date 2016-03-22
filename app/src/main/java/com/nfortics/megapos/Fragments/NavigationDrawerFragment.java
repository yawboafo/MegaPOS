package com.nfortics.megapos.Fragments;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nfortics.megapos.Adapters.NavigationItemsAdapter;
import com.nfortics.megapos.Domain.LoginModel;
import com.nfortics.megapos.Helper.Message;
import com.nfortics.megapos.Helper.Typefacer;
import com.nfortics.megapos.Models.NavigationItems;
import com.nfortics.megapos.R;
import com.nfortics.megapos.utils.GsonMan;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment {
    private RecyclerView recyclerView,recyclerView1;
    public static final String PREF_FILE_NAME="testpref";
    public static final String KEY_USER_LEARNED_DRAWER="user_learned_drawer";
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private View containerview;
    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    private NavigationItemsAdapter adapter,adapter1;
    Bundle extras;
    getNavigationDrawerClicks getnavigationDrawerClicks;
    TextView
            logoutTxtBox,
            listOneTitle,
            listTwoTitle,
            listSettingsTtitle,
            TxtUsername,
            TxtUserid;

    LoginModel loginModel;
    GsonMan gsonMan;
    String userName;
    public NavigationDrawerFragment() {
        // Required empty public constructor
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        extras = getActivity().getIntent().getExtras();


        if(extras == null) {

        }else {


            userName= extras.getString("UserDetails");

        }
        getnavigationDrawerClicks=(getNavigationDrawerClicks)activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        gsonMan=new GsonMan();


        View layout=inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        recyclerView=(RecyclerView)layout.findViewById(R.id.drawerlistOne);
        recyclerView1=(RecyclerView)layout.findViewById(R.id.drawerlistTwo);


        Typefacer typefacer =new Typefacer();

        logoutTxtBox=(TextView)layout.findViewById(R.id.txtLogout);
        logoutTxtBox.setTypeface(typefacer.getRoboRealThin(getActivity().getAssets()));

        listOneTitle=(TextView)layout.findViewById(R.id.ListOneTitle);
        listTwoTitle=(TextView)layout.findViewById(R.id.ListTwoTitle);
        // listSettingsTtitle=(TextView)layout.findViewById(R.id.ListSettingsTitle);
        TxtUsername=(TextView)layout.findViewById(R.id.txtUsername);
        TxtUserid=(TextView)layout.findViewById(R.id.txtUserid);




        try{


            String name=  gsonMan.getMerchantDetails(userName,getActivity()).getName();
            String ID=  gsonMan.getMerchantDetails(userName,getActivity()).getId();
            TxtUsername.setText( "Welcome, "+ name);
            TxtUserid.setText( "User ID :"+ID);
           /// Message.message(getActivity(),name);

        }catch (Exception ex){



        }










        listOneTitle.setTypeface(typefacer.getRoboRegular(getActivity().getAssets()));
        listTwoTitle.setTypeface(typefacer.getRoboRegular(getActivity().getAssets()));
        //listSettingsTtitle.setTypeface(typefacer.getRoboRegular(getActivity().getAssets()));
        TxtUserid.setTypeface(typefacer.getRoboCondensedLight(getActivity().getAssets()));
        TxtUsername.setTypeface(typefacer.getRoboCondensedBold(getActivity().getAssets()));
        listOneTitle.setTypeface(typefacer.getRoboCondensedBold(getActivity().getAssets()));

        listOneTitle.setTypeface(typefacer.getRoboCondensedRegular(getActivity().getAssets()));

        adapter=new NavigationItemsAdapter(getActivity(),getData());
        adapter1=new NavigationItemsAdapter(getActivity(),getData2());

        recyclerView.setAdapter(adapter);
        recyclerView1.setAdapter(adapter1);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {



                getnavigationDrawerClicks.navigate(position);
                mDrawerLayout.closeDrawer(containerview);
                //Message.message(getActivity(),""+position);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        recyclerView1.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView1, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                getnavigationDrawerClicks.navigate1(position);
                mDrawerLayout.closeDrawer(containerview);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        return layout;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mUserLearnedDrawer=Boolean.valueOf(readFromPreferences(getActivity(),KEY_USER_LEARNED_DRAWER,"false"));

        if(savedInstanceState!=null){

            mFromSavedInstanceState=true;
        }
    }
    public void setUp(int fag,DrawerLayout drawerlayout, final Toolbar toolbar) {

        containerview=getActivity().findViewById(fag);
        mDrawerLayout=drawerlayout;
        mDrawerToggle= new ActionBarDrawerToggle(getActivity(),drawerlayout,toolbar,R.string.drawer_open,R.string.drawer_close){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();


            /**    if(!mUserLearnedDrawer){

                    mUserLearnedDrawer=true;
                    saveToPreferences(getActivity(),KEY_USER_LEARNED_DRAWER,mUserLearnedDrawer+"Close");

                }**/

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
               // if(slideOffset<0.6){

                    toolbar.setAlpha(1-slideOffset/2);

               // }

                // toolbar.setAlpha(slideOffset);
            }
        };
      //  if(!mUserLearnedDrawer && !mFromSavedInstanceState){

          //  mDrawerLayout.openDrawer(containerview);
       // }


        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {

                mDrawerToggle.syncState();
            }
        });

    }
    public static void saveToPreferences(Context context,String preferenceName,String preferenceValue){

        SharedPreferences sharedpreference=context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedpreference.edit();
        editor.putString(preferenceName,preferenceValue);
        editor.apply();

    }

    public static String readFromPreferences(Context context,String preferenceName,String defaultValue){

        SharedPreferences sharedpreference=context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedpreference.getString(preferenceName,defaultValue);

    }
    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
       private GestureDetector gestureDetector;
        private  ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener){
            this.clickListener=clickListener;
            gestureDetector=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                 View child= recyclerView.findChildViewUnder(e.getX(),e.getY());
                    if(child!=null && clickListener!=null){


                        clickListener.onLongClick(child,recyclerView.getChildPosition(child));
                    }

                }
            });
        }
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child= rv.findChildViewUnder(e.getX(),e.getY());
            if(child!=null && clickListener!=null && gestureDetector.onTouchEvent(e)){


                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public static interface ClickListener{

        public void onClick(View view,int position);
        public void onLongClick(View view,int position);

    }

    public static interface getNavigationDrawerClicks{

        public void navigate(int position);
        public void navigate1(int position);
    }

    public static List<NavigationItems> getData(){

        List<NavigationItems> data=new ArrayList<>();
        int icons[]={R.drawable.pos,R.drawable.network,R.drawable.payment,R.drawable.mobilemoney,R.drawable.financecard};
        String[] Titles={"OZINBO-POS","M-FINANCE","BILL PAYMENT","AIRTIME & MOBILE MONEY","VOUCHERS & PREPAID CARDS"};

        for(int t=0;t<Titles.length;t++){

            NavigationItems current = new NavigationItems();
            current.setTitle(Titles[t]);
            current.setIconid(icons[t]);
            data.add(current);
        }


        return data;
    }
    public static  List<NavigationItems> getData2(){

        List<NavigationItems> data=new ArrayList<>();
        int icons[]={R.drawable.activity,R.drawable.moneyrefund,R.drawable.vreceipr,R.drawable.till,R.drawable.stock};
        String[] Titles={"VIEW DAILY ACTIVITIES","REFUNDS","VOID RECIEPTS","CLOSE TILL","STOCK CONTROL"};

        for(int t=0;t<Titles.length && t<icons.length;t++){

            NavigationItems current = new NavigationItems();
            current.setTitle(Titles[t]);
            current.setIconid(icons[t]);


            data.add(current);
        }


        return data;
    }
}
