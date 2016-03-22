package com.nfortics.megapos.Dialogs;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nfortics.megapos.Helper.CurrencyTextWatcher;
import com.nfortics.megapos.Helper.Typefacer;
import com.nfortics.megapos.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MobilePayment_Dialog extends DialogFragment implements View.OnClickListener {


    public   String tax,discount,total;
    Button cash,mobile,credit,pay,back;
    public TextView Tax,Discount,Total,TaxValue,DiscountValue,TotalValue;
    EditText cashAmount;
    private boolean checkselectable;
    RelativeLayout relativeLayout;
    LinearLayout linearLayout;
    InputMethodManager IMM;

    public MobilePayment_Dialog() {
        // Required empty public constructor
    }
    @Override
    public void show(FragmentManager manager, String tag) {
        if(manager.findFragmentByTag(tag)==null){
            super.show(manager, tag);
        }
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        tax=this.getArguments().getString("Tax");
        discount=this.getArguments().getString("Discount");
        total=this.getArguments().getString("Total");
        //communicator=(Communicator)activity;

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCancelable(false);

        View view= inflater.inflate(R.layout.fragment_mobile_payment__dialog, null);
        Typefacer typeface = new Typefacer();
      //  IMM=(InputMethodManager)getActivity()
               // .getSystemService(Context.INPUT_METHOD_SERVICE);





        cashAmount=(EditText)view.findViewById(R.id.meditAmount);
        cashAmount.addTextChangedListener(new CurrencyTextWatcher(cashAmount));
        //cashAmount.setVisibility(View.GONE);

        pay=(Button)view.findViewById(R.id.mbutPay);
        pay.setOnClickListener(this);
        // pay.setVisibility(View.GONE);
        //  back=(Button)view.findViewById(R.id.butBack);

//        back.setOnClickListener(this);
        //back.setVisibility(View.GONE);




        Tax=(TextView)view.findViewById(R.id.mtxtTax);
        Discount=(TextView)view.findViewById(R.id.mtxtDiscount);
        Total=(TextView)view.findViewById(R.id.mtxtTotal);

        // DiscountValue,TotalValue;
        TaxValue=(TextView)view.findViewById(R.id.mTaxValue);
        DiscountValue=(TextView)view.findViewById(R.id.mDiscountValue);
        TotalValue=(TextView)view.findViewById(R.id.mTotalValue);


        TaxValue.setTypeface(typeface.getRoboCondensedRegular(getActivity().getAssets()));
        DiscountValue.setTypeface(typeface.getRoboCondensedRegular(getActivity().getAssets()));
        TotalValue.setTypeface(typeface.getRoboCondensedRegular(getActivity().getAssets()));

        TaxValue.setText("" + tax);
        DiscountValue.setText(""+discount);
        TotalValue.setText(""+total);

        Tax.setTypeface(typeface.getRoboCondensedRegular(getActivity().getAssets()));
        Discount.setTypeface(typeface.getRoboCondensedRegular(getActivity().getAssets()));
        Total.setTypeface(typeface.getRoboCondensedRegular(getActivity().getAssets()));

        pay.setTypeface(typeface.getRoboCondensedRegular(getActivity().getAssets()));
        // back.setTypeface(typeface.getRoboCondensedRegular(getActivity().getAssets()));

       // IMM.showSoftInput(cashAmount, InputMethodManager.SHOW_IMPLICIT);

        cashAmount.requestFocus();
        return view;
    }


    @Override
    public void onClick(View v) {

    }



}
