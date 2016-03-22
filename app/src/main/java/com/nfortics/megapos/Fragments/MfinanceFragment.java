package com.nfortics.megapos.Fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nfortics.megapos.Adapters.RegisteredFirmsAdapter;
import com.nfortics.megapos.Application.MyApplication;
import com.nfortics.megapos.Domain.BankModel;
import com.nfortics.megapos.Helper.Message;
import com.nfortics.megapos.R;
import com.nfortics.megapos.utils.GsonMan;

import java.util.ArrayList;
import java.util.List;


public class MfinanceFragment extends Fragment {
    InilializeMenus inilializeMenus;
    RecyclerView listOfFirmsViews;
    View tmpView;
    GsonMan gsonMan=new GsonMan();
    RegisteredFirmsAdapter registeredFirmsAdapter;
    String userName;
    Bundle extras;
    public MfinanceFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            extras = getActivity().getIntent().getExtras();



            if(extras == null) {

            }else {


                userName= extras.getString("UserDetails");

            }

            inilializeMenus=(InilializeMenus)activity;
        } catch (ClassCastException e) {

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View layout=inflater.inflate(R.layout.fragment_mfinance, container, false);
        tmpView=layout;
        listOfFirmsViews=(RecyclerView)tmpView.findViewById(R.id.listOfFirms);
        return tmpView;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        InitializeAdapters();
        InitializeRecylceClickListener();

    }



    public void IniializeFirmViews(){


        new BackgroundTask().execute();
    }
    public void InitializeAdapters(){

     /**   String items[]={"Fidelity","UNIBANK","ECOBANK","GT BANK"};
        List<String> itemsList=new ArrayList<>();

        for(String i:items){
            itemsList.add(i);
        }**/
        List<String> itemsList=new ArrayList<>();

        for (BankModel bankModel : gsonMan.getMerchantDetails(userName, MyApplication.getAppContext()).getBanks()) {
            itemsList.add(bankModel.getName());
        }

        registeredFirmsAdapter=new RegisteredFirmsAdapter(getActivity(),itemsList);
        listOfFirmsViews.setAdapter(registeredFirmsAdapter);
        listOfFirmsViews.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
    public void InitializeRecylceClickListener(){

       /** listOfFirmsViews.addOnItemTouchListener(new RecyclerView.OnItemTouchListener(getActivity(),
                listOfFirmsViews,new ClickListner()) {
        });**/

        listOfFirmsViews.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), listOfFirmsViews, new ClickListner() {
            @Override
            public void onClick(View view, int position) {
                Button itemname = (Button) view.findViewById(R.id.butAfirm);

              final String butTextLabel=itemname.getText().toString();
                itemname.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // String name = itemname.getText().toString();
                        inilializeMenus.CallMenus("MFINANCE  : " + butTextLabel);
                       // Message.message(getActivity(),"somethign was clicked");
                    }
                });


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));




    }
    public static interface InilializeMenus{

        public void CallMenus(String val);
    }
    public static interface ClickListner {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    class BackgroundTask extends AsyncTask<String,String,String> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();


          //  registeredFirmsAdapter=(RegisteredFirmsAdapter)listOfFirmsViews.getAdapter();

            /*
            *  itemsAdapter = new ItemsAdapter(getActivity(), datatmp);
               recyclerView.setAdapter(itemsAdapter);
               recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
               recyclerView.setItemAnimator(new DefaultItemAnimator());
            *
            *
            * */
        }

        @Override
        protected String doInBackground(String... params) {

            InitializeAdapters();
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListner clickListner;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListner clickListner) {

            this.clickListner = clickListner;

            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent e) {

                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());

                    if (child != null & clickListner != null) {


                        clickListner.onClick(child, recyclerView.getChildPosition(child));

                    }

                    return super.onSingleTapUp(e);
                }

                @Override
                public void onLongPress(MotionEvent e) {


                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());

                    if (child != null & clickListner != null) {


                        clickListner.onLongClick(child, recyclerView.getChildPosition(child));

                    }
                    // super.onLongPress(e);
                }
            });


        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());

            if (child != null & clickListner != null && gestureDetector.onTouchEvent(e)) {


                clickListner.onClick(child, rv.getChildPosition(child));

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
}
