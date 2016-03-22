package com.nfortics.megapos.Fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nfortics.megapos.Adapters.MenuActionsAdapter;
import com.nfortics.megapos.Models.MenuActions;
import com.nfortics.megapos.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MfinanceMenu extends Fragment {
    TextView titlevIEW;
    ImageButton backButton;
    RecyclerView recyclerView;
    MenuActionsAdapter menuActionsAdapter;
    String bankLabel;
    CallAViewFragment callAViewFragment;
    public MfinanceMenu() {
        // Required empty public constructor
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callAViewFragment =(CallAViewFragment)activity;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_mfinance_menu, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bankLabel=this.getArguments().getString("BankName");

        InitializeControls(view);
        InitializeRecycleViewComponent(view);
        InitializeListeners();
    }
    public void InitializeControls(View view){
        backButton=(ImageButton)view.findViewById(R.id.imageBckButt);
    }
    public void InitializeRecycleViewComponent(View view){

        RecyclerView.LayoutManager mlyout2=new GridLayoutManager(getActivity(),3,GridLayoutManager.VERTICAL,false);
        recyclerView=(RecyclerView)view.findViewById(R.id.menuActions);
        titlevIEW=(TextView)view.findViewById(R.id.titleLabel);
        titlevIEW.setText(bankLabel);

        menuActionsAdapter=new MenuActionsAdapter(getActivity(),getData2());
        recyclerView.setAdapter(menuActionsAdapter);
        recyclerView.setLayoutManager(mlyout2);
    }
    public void InitializeListeners(){

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAViewFragment.callfrag();
            }
        });

    }
    public static List<MenuActions> getData2(){

        List<MenuActions> data=new ArrayList<>();
        int icons[]={R.drawable.cash_out,
                R.drawable.redraw_cash,
                R.drawable.customers_ci,
                R.drawable.money_transfer,
                R.drawable.loan,
                R.drawable.balance,
                R.drawable.statement,
                R.drawable.mini_statement,
                R.drawable.request,
                android.R.color.transparent,
                R.drawable.bill_payment,
                android.R.color.transparent
        };




        String[] Titles={
                "Deposit",
                "Withdrawal",
                "New Customers","" +
                "Transfer",
                "Loan Application",
                "Balance Enquiry",
                "Full Statement",
                "Mini Statement",
                "Request","",
                "Bill Payment",""};

        for(int t=0;t<Titles.length && t<icons.length;t++){

            MenuActions current = new MenuActions();
            current.setTextLabel(Titles[t]);
            current.setIcon(icons[t]);


            data.add(current);
        }


        return data;
    }
    public static interface CallAViewFragment{

        public void callfrag();

    }

}
