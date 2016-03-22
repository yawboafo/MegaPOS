package com.nfortics.megapos.Dialogs;


import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.nfortics.megapos.Application.MyApplication;
import com.nfortics.megapos.DataBase.DataSets;
import com.nfortics.megapos.Helper.ControlHelpers;
import com.nfortics.megapos.Helper.Message;
import com.nfortics.megapos.Helper.Typefacer;
import com.nfortics.megapos.Helper.Useful;
import com.nfortics.megapos.Models.TransHistory;
import com.nfortics.megapos.Models.TransHistoryDetails;
import com.nfortics.megapos.Models.Transactions;
import com.nfortics.megapos.Network.ConnectionDetector;
import com.nfortics.megapos.Network.VolleySingleton;
import com.nfortics.megapos.R;
import com.nfortics.megapos.ThirrtySevenSignals.RootAccess;
import com.nfortics.megapos.utils.GsonMan;

import org.json.JSONObject;
import org.m2i.m835.api.BIO;
import org.m2i.m835.api.IBIOConnState;
import org.m2i.m835.api.PRINTER;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class CashOut extends DialogFragment {
    private OnFragmentInteractionListener mListener;
    Typefacer typefacer;
    RelativeLayout
            relativeDownButtonsLayout,
            relativeActions,
            layoutFinger,
            mobileMoneyLayout,
            PrepaidCardLayout
            ,confirmLayout,
            SapLayout;

    Button backAccount,
            prePaid,
            mobileMoney,
            backButton,
            submitButton,
            confirmYesButton,
            confirmNoButton,
            butconfirmSap;

    TextView selectBankLabel,
            identifyLabel,
            enterAmtLabel,
            enterBankAcLabel,
            dialogTitle,
            confirmLabel1,
            confirmLabel2,
            confirmLabel3,
            confirmLabel4;

    EditText enterAccountdetails,
            enterAmt,
            enterMobileAcc,
            enterMobileAmount,
            edtCardNumber,
            edtPrepAmount,edtSapCode;
    Spinner bankTypeSpinners,
            paymenIdentitySpinners,
            telcosSpinners,
            prepaidVendorsSpinner;


    ControlHelpers
            controlHelpers;

    Useful useful;

    String newTitle;
    Gson gson = new Gson();

    Bundle extras;
    String userName;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    ConnectionDetector cd ;
    RootAccess rootAccess;
    public static String receiptNumber;

    TransHistory transHistory;
    TransHistoryDetails transHistoryDetails;
    String UserId;

    GsonMan gsonMan;
    JSONObject jsonObject;
    // Activity ctx;
DataSets dataSets;


   public String provider,phone,TotalAmount,accountN,type;
    public String transactionType;
    public CashOut() {
        // Required empty public constructor
    }
    @Override
    public void show(FragmentManager manager, String tag) {
        if(manager.findFragmentByTag(tag)==null){
            super.show(manager, tag);
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        controlHelpers=new ControlHelpers();
        useful=new Useful();
        jsonObject = new JSONObject();
        dataSets = new DataSets(getActivity());
        UserId="9009";
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        return inflater.inflate(R.layout.fragment_cash_out, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        InitializeView(view);
        SetTypeFace();
        InitializeClickListeners();
        setDialogTitle("Cash Out");


        populateSpinners();

        SetChangeListenersForSpinners();
    }
    void populateSpinners(){
        controlHelpers.makeListForBankSpinners(getActivity(), bankTypeSpinners, userName);


        controlHelpers.makeListForTelcosSpinners(getActivity(), telcosSpinners, userName);



        List<String> data2=new ArrayList<>();
        data2.add("Account Number");
        data2.add("Phone Number");
        data2.add("Finger Print");
        data2.add("Card");
        controlHelpers.populateSpinner(paymenIdentitySpinners, data2, getActivity());


        List<String> data4=new ArrayList<>();
        data4.add("EZSWITCH");
        data4.add("VISA");
        data4.add("MASTER");
        controlHelpers.populateSpinner(prepaidVendorsSpinner, data4, getActivity());
    }
    public void SetChangeListenersForSpinners(){

        bankTypeSpinners.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        paymenIdentitySpinners.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (parent.getItemAtPosition(position).toString().equals("Finger Print")) {

                    InitializeLayoutFingerPrint(true);
                    try {
                        //InitWidget();
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                } else {

                    InitializeLayoutFingerPrint(false);
                    enterBankAcLabel.setText("Enter " + parent.getItemAtPosition(position).toString() + " :");
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                enterBankAcLabel.setText("");
            }
        });


    }
    public void InitializeLayoutFingerPrint(boolean val){

        if(val){

            if(layoutFinger.getVisibility()==View.GONE){

                layoutFinger.setVisibility(View.VISIBLE);
                InitializeControlsBelowFingerPrint(false);
            }


        }else if(!val){

            if(layoutFinger.getVisibility()==View.VISIBLE){

                layoutFinger.setVisibility(View.GONE);
                InitializeControlsBelowFingerPrint(true);
            }

        }
    }
    public void InitializeControlsBelowFingerPrint(boolean val){

        if(val){

            if(enterAccountdetails.getVisibility()==View.GONE
                    & enterAmt.getVisibility()==View.GONE
                    & enterBankAcLabel.getVisibility()==View.GONE
                    & enterAmtLabel.getVisibility()==View.GONE
                    ){
                enterAmtLabel.setVisibility(View.VISIBLE);
                enterBankAcLabel.setVisibility(View.VISIBLE);
                enterAmt.setVisibility(View.VISIBLE);
                enterAccountdetails.setVisibility(View.VISIBLE);
            }


        }else if(!val){

            if(enterAccountdetails.getVisibility()==View.VISIBLE
                    & enterAmt.getVisibility()==View.VISIBLE
                    & enterBankAcLabel.getVisibility()==View.VISIBLE
                    & enterAmtLabel.getVisibility()==View.VISIBLE
                    ){
                enterAmtLabel.setVisibility(View.GONE);
                enterBankAcLabel.setVisibility(View.GONE);
                enterAmt.setVisibility(View.GONE);
                enterAccountdetails.setVisibility(View.GONE);
            }

        }


    }
    public void InitializeView(View view){

        confirmLayout=(RelativeLayout)view.findViewById(R.id.confirmLayout);
        mobileMoneyLayout=(RelativeLayout)view.findViewById(R.id.MobileMoneyLayout);
        relativeDownButtonsLayout=(RelativeLayout)view.findViewById(R.id.buttonHolder);
        relativeDownButtonsLayout.setVisibility(View.GONE);
        relativeActions=(RelativeLayout)view.findViewById(R.id.relativeActions);
        layoutFinger=(RelativeLayout)view.findViewById(R.id.layoutFinger);
        PrepaidCardLayout=(RelativeLayout)view.findViewById(R.id.PrepaidCardLayout);
        SapLayout=(RelativeLayout)view.findViewById(R.id.SapLayout);



        backAccount=(Button)view.findViewById(R.id.butBankAc);
        prePaid=(Button)view.findViewById(R.id.butPrepaidC);
        mobileMoney=(Button)view.findViewById(R.id.butMobileM);
        backButton=(Button)view.findViewById(R.id.butBack);
        submitButton=(Button)view.findViewById(R.id.butSubmit);

        confirmYesButton=(Button)view.findViewById(R.id.confirmYesButtonCH);
        confirmNoButton=(Button)view.findViewById(R.id.confirmNoButtonCH);
        butconfirmSap=(Button)view.findViewById(R.id.butSap);

        dialogTitle=(TextView)view.findViewById(R.id.txtDialogTitle);
        selectBankLabel=(TextView)view.findViewById(R.id.txtSelectBankLabel);
        identifyLabel=(TextView)view.findViewById(R.id.txtIdentifyLabel);
        enterAmtLabel=(TextView)view.findViewById(R.id.txtEnterAmtLabel);
        enterBankAcLabel=(TextView)view.findViewById(R.id.txtBackAcLabel);


        confirmLabel1=(TextView)view.findViewById(R.id.txtconfirmCH1);
        confirmLabel2=(TextView)view.findViewById(R.id.txtconfirmCH2);
        confirmLabel3=(TextView)view.findViewById(R.id.txtconfirmCH3);
        confirmLabel4=(TextView)view.findViewById(R.id.txtconfirmCH4);



        bankTypeSpinners=(Spinner)view.findViewById(R.id.spinnerBanks);
        paymenIdentitySpinners=(Spinner)view.findViewById(R.id.spinnerAcpType);
        telcosSpinners=(Spinner)view.findViewById(R.id.spinnerTelcos);
        prepaidVendorsSpinner=(Spinner)view.findViewById(R.id.prepaidVendorsSpinner);



        enterAccountdetails=(EditText)view.findViewById(R.id.edtBankAcValue);
        enterAmt=(EditText)view.findViewById(R.id.edtBankAmount);
        enterMobileAcc =(EditText)view.findViewById(R.id.edtMobileAcc);
        enterMobileAmount =(EditText)view.findViewById(R.id.editEnterAmount);
        edtCardNumber=(EditText)view.findViewById(R.id.edtCardNumber);
        edtPrepAmount=(EditText)view.findViewById(R.id.edtPrepAmount);

        edtSapCode=(EditText)view.findViewById(R.id.edtSapCode);





    }
    public void InitializeClickListeners(){

        BankAccountButtonClickLister();
        BackButtonClickListener();
        SubmitButtonClickListener();
        MobileMoneyButtonClickLister();
        PrepaidButtonClickLister();
        confirmNoButtonClickListener();
        confirmYesButtonClickListener();
        SAPconfirmClickLister();
        butFingerPrintClickLister();
    }
    void BankAccountButtonClickLister(){
        backAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideButtons(true);
                setCancelable(false);
                transactionType = "cashOutBankAccount";
                relativeDownButtonsLayout.setVisibility(View.VISIBLE);
                relativeActions.setVisibility(View.VISIBLE);
                setDialogTitle("Cash Out/Bank Account");
            }
        });

    }
    void MobileMoneyButtonClickLister(){
        mobileMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideButtons(true);
                setCancelable(false);
                transactionType = "cashOutMobileWallet";
                relativeDownButtonsLayout.setVisibility(View.VISIBLE);
                mobileMoneyLayout.setVisibility(View.VISIBLE);
                setDialogTitle("Cash Out/Mobile Money");
            }
        });

    }
    void PrepaidButtonClickLister(){
        prePaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideButtons(true);
                setCancelable(false);
                transactionType="cashOutPrepaid";
                relativeDownButtonsLayout.setVisibility(View.VISIBLE);
                PrepaidCardLayout.setVisibility(View.VISIBLE);
                setDialogTitle("Cash Out/Prepaid");
            }
        });

    }
    void SAPconfirmClickLister(){
        butconfirmSap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideButtons(true);
                setCancelable(false);
                transactionType="cashOutSap";
                relativeDownButtonsLayout.setVisibility(View.VISIBLE);
                SapLayout.setVisibility(View.VISIBLE);
                setDialogTitle("Cash Out/Confirm Sap");
            }
        });


    }
    void BackButtonClickListener(){
        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Log.d("Debuged", "new title : " + newTitle + "transType  :  " + transactionType);

                if(transactionType.equals("cashOutSap")){
                    dismiss();
                 /**   setDialogTitle("CONFIRM");
                    confirmLayout.setVisibility(View.VISIBLE);
                    SapLayout.setVisibility(View.GONE);
                    relativeDownButtonsLayout.setVisibility(View.GONE);
                    transactionType = "cashOutBankAccount";**/
                }else{

                    HideButtons(false);
                    setCancelable(true);
                    relativeDownButtonsLayout.setVisibility(View.GONE);
                    relativeActions.setVisibility(View.GONE);
                    mobileMoneyLayout.setVisibility(View.GONE);
                    PrepaidCardLayout.setVisibility(View.GONE);
                    SapLayout.setVisibility(View.GONE);
                    setDialogTitle("Cash Out");
                }



               // mListener.cashOutCurrencyDetectOff();
            }
        });


    }
    void SubmitButtonClickListener(){


        try{

            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    String titleToken = dialogTitle.getText().toString();


                    switch (titleToken) {

                        case "Cash Out/Bank Account":
                            if (enterAccountdetails.getText().length() == 0 ||
                                    enterAmt.getText().length() == 0) {
                            } else {


                                ConfirmDialog(true, dialogTitle.getText().toString(), relativeActions);
                                SetValuesToConfirmLabels("Cash Out",
                                        bankTypeSpinners.getSelectedItem().toString(),
                                        enterAccountdetails.getText().toString(),
                                        useful.formatMoney(Double.parseDouble(enterAmt.getText().toString())));

                                ///SECOND SCREEN
                                mListener.cashOutSecondScreen(titleToken,
                                        "Provider :" + bankTypeSpinners.getSelectedItem().toString(),
                                        enterAccountdetails.getText().toString(),
                                        useful.formatMoney(Double.parseDouble(enterAmt.getText().toString())));
                                // mListener.CurrencyDetectOn();
                            }
                            break;
                        case "Cash Out/Mobile Money":
                            if (enterMobileAcc.getText().length() == 0 ||
                                    enterMobileAmount.getText().length() == 0) {
                            } else {

                                ConfirmDialog(true, dialogTitle.getText().toString(), mobileMoneyLayout);
                                SetValuesToConfirmLabels("Cash Out",
                                        telcosSpinners.getSelectedItem().toString(),
                                        enterMobileAcc.getText().toString(),
                                        useful.formatMoney(Double.parseDouble(enterMobileAmount.getText().toString())));


                                mListener.cashOutSecondScreen(titleToken,
                                        "Provider :" + telcosSpinners.getSelectedItem().toString(),
                                        enterMobileAcc.getText().toString(),
                                        useful.formatMoney(Double.parseDouble(enterMobileAmount.getText().toString())));

                            }

                            break;

                        case "Cash Out/Prepaid":



                            if (edtCardNumber.getText().toString().length() == 0 ||
                                    edtPrepAmount.getText().toString().length() == 0) {

                            } else {

                                ConfirmDialog(true, dialogTitle.getText().toString(), PrepaidCardLayout);
                                SetValuesToConfirmLabels("Cash Out",
                                        prepaidVendorsSpinner.getSelectedItem().toString(),
                                        edtCardNumber.getText().toString(),
                                        useful.formatMoney(Double.parseDouble(edtPrepAmount.getText().toString())));


                                mListener.cashOutSecondScreen(titleToken,
                                        prepaidVendorsSpinner.getSelectedItem().toString(),
                                        edtCardNumber.getText().toString(),
                                        useful.formatMoney(Double.parseDouble(edtPrepAmount.getText().toString())));


                            }
                            break;

                        case "Cash Out/Confirm Sap":

                            ConfirmSap(edtSapCode.getText().toString(),"Mint");
                            Log.d("Sap Code ", edtSapCode.getText().toString());
                           // Message.messageShort(getActivity(),"SAP CALLED");
                            break;
                    }


                }
            });

        }catch(Exception ex){


        }



    }
    void SetTypeFace(){
        typefacer=new Typefacer();
        selectBankLabel.setTypeface(typefacer.getRoboCondensedRegular(getActivity().getAssets()));
        identifyLabel.setTypeface(typefacer.getRoboCondensedRegular(getActivity().getAssets()));
        enterAmtLabel.setTypeface(typefacer.getRoboCondensedRegular(getActivity().getAssets()));
        enterBankAcLabel.setTypeface(typefacer.getRoboCondensedRegular(getActivity().getAssets()));
    }
    public void HideButtons(boolean token){

        if(token){
            backAccount.setVisibility(View.GONE);
            prePaid.setVisibility(View.GONE);
            mobileMoney.setVisibility(View.GONE);
            butconfirmSap.setVisibility(View.GONE);
        }else{

            backAccount.setVisibility(View.VISIBLE);
            prePaid.setVisibility(View.VISIBLE);
            mobileMoney.setVisibility(View.VISIBLE);
            butconfirmSap.setVisibility(View.VISIBLE);
        }



    }
    void ConfirmDialog(boolean value,String type,RelativeLayout layout){

        getSetDialogTitle(type);

        if(value){
            layout.setVisibility(View.GONE);
            relativeDownButtonsLayout.setVisibility(View.GONE);
            confirmLayout.setVisibility(View.VISIBLE);

            setDialogTitle("CONFIRM");
        }else{
            confirmLayout.setVisibility(View.GONE);
            relativeDownButtonsLayout.setVisibility(View.VISIBLE);
            layout.setVisibility(View.VISIBLE);
            setDialogTitle(getSetDialogTitle(type));
        }

    }
    String getSetDialogTitle(String value){

        if(value.contains("Cash Out/Bank Account")){

            newTitle="Cash Out/Bank Account";
        }else if(value.contains("Cash Out/Mobile Money")){

            newTitle="Cash Out/Mobile Money";

        }else if(value.contains("Cash Out/Prepaid")){

            newTitle="Cash Out/Prepaid";

        }else if(value.contains("Cash Out/Confirm Sap")){

            newTitle="Cash Out/Confirm Sap";}

        return newTitle;
    }
    void butFingerPrintClickLister(){


    }

    void confirmNoButtonClickListener() {

        confirmNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Debuged","new title : "+newTitle+"transType  :  " + transactionType);
                try {
                    ConfirmDialog(false, newTitle, setGetLayout(newTitle));

                   // Log.d("Debuged","new title : "+newTitle+"transType  :  "+transactionType);
                    mListener.cashOutClearSecondScreen();
                }catch(Exception ex){
                    dismiss();
                  ex.printStackTrace();
                }

            }
        });
    }
    void confirmYesButtonClickListener(){
        confirmYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (transactionType) {


                    case "cashOutBankAccount":


                        type = "cashOutBankAccount";
                        accountN = enterAccountdetails.getText().toString();
                        provider = bankTypeSpinners.getSelectedItem().toString();
                        TotalAmount = enterAmt.getText().toString();


                        Log.d(" bank Type ", bankTypeSpinners.getSelectedItem().toString() +
                                " Payment Type " + paymenIdentitySpinners.getSelectedItem().toString() +
                                " AccountDetails" + enterAccountdetails.getText().toString() +
                                " Amount " + enterAmt.getText().toString()
                                + " Type " + " Cashout");

//"002-000001-01"
                        SubmitCashOutBankAccount(
                                bankTypeSpinners.getSelectedItem().toString(),
                                paymenIdentitySpinners.getSelectedItem().toString(),
                                enterAccountdetails.getText().toString(),
                                enterAmt.getText().toString(), "cashout");
                        break;


                    case "cashOutMobileWallet":


                        type = "cashOutMobileWallet";
                        accountN = enterMobileAcc.getText().toString();
                        provider = telcosSpinners.getSelectedItem().toString();
                        TotalAmount = enterMobileAmount.getText().toString();

                        Log.d(" provider ", provider +
                                " Payment Type " + "" +
                                " AccountDetails " + accountN + " Amount " + TotalAmount
                                + " Type " + " CashinMobile");


                        receiptNumber = useful.generateReceipt(MyApplication.getShopname(),
                                MyApplication.getShopBranch(), UserId);

                        SubmitCashOutMobileMoney(
                                "",
                                "cashout",
                                telcosSpinners.getSelectedItem().toString(),
                                enterMobileAmount.getText().toString(),
                                "Items",
                                MyApplication.getApplicationClient(),
                                enterMobileAcc.getText().toString(),
                                receiptNumber,
                                MyApplication.getApplicationClient(),
                                MyApplication.getApp_name(),
                                MyApplication.getApp_Country(),
                                MyApplication.getApplicationSeriousness() + "");


                        Log.d("ReferenceNumber -- >", receiptNumber);
                        break;
                    case "cashOutPrepaid":


                        break;
                }

            }
        });
    }

    TransHistoryDetails transHistoryDetails(){

        transHistoryDetails = new TransHistoryDetails();
        transHistoryDetails.setDeTailsID(useful.getTimeWithFormatter());
        transHistoryDetails.setItems(BuildStringTopPRINT());
        transHistoryDetails.setSale("");


        return transHistoryDetails;
    }
    String BuildStringTopPRINT(){

        StringBuilder sb = new StringBuilder();
        sb.append(confirmLabel2.getText().toString() + "\n"
                + confirmLabel3.getText().toString() + "\n"
                + confirmLabel4.getText().toString());


        return sb.toString();
    }
    void SetValuesToConfirmLabels(String one,String two,String three,String four){
        confirmLabel1.setText( one);
        confirmLabel2.setText("From :" + two);
        confirmLabel3.setText("A/c# :" + three);
        confirmLabel4.setText("Total :" + four);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            gsonMan= new GsonMan();
            extras = getActivity().getIntent().getExtras();





            if(extras == null) {

            }else {

             try{
                 userName= extras.getString("UserDetails");

             }catch (Exception e){
                 e.printStackTrace();

             }

              //  UserId=gsonMan.getMerchantDetails(userName, activity).getId();
//                Log.d("UserID   > > > : ",UserId);


        }
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {

        }
    }
    public void setDialogTitle(String title){

        dialogTitle.setText(title);
    }
    public  interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String name, String price);
        public void cashOutPrint(String type ,String item);


        public void cashOutInsertHistory(TransHistory transHistory,TransHistoryDetails transHistoryDetails);

        public void cashOutSecondScreen(String type,String provider,String Account, String amt);

        public void cashOutClearSecondScreen();

        public void cashOutPositiveSecondScreen();

        public void cashOutCurrencyDetectOn();
        public void cashOutCurrencyDetectOff();
    }
    public String TrimIdentitySelected(String value){
        String val="";

        switch(value){
            /*
            *      data2.add("Account Number");
        data2.add("Phone Number");
        data2.add("Finger Print");
            *
            * **/
            case "Account Number":
                val="AC";
                break;
            case "Phone Number":
                val="PhN";
                break;
            case "Finger Print":
                val="Bio";
                break;

        }



        return val;
    }
    public RelativeLayout setGetLayout(String value){
        Log.d("Value >> ", value);
        RelativeLayout relativeLayout=null;

        if(value.contains("Cash Out/Bank Account")){

            relativeLayout= relativeActions;
        }else if(value.contains("Cash Out/Mobile Money")){

            relativeLayout= mobileMoneyLayout;

        }else if(value.contains("Cash Out/Prepaid")){

            relativeLayout= PrepaidCardLayout;

        }else if(value.contains("Cash Out/Confirm Sap")){
            relativeLayout=SapLayout;

        }else if(value==null){
            dismiss();
        }

        return relativeLayout;
    }
    void Dismiss(){
        try{
           dismiss();
           // mListener.cashOutPrint(confirmLabel1.getText().toString(), BuildStringTopPRINT());
            new MyPrintTask("CashOut", provider,accountN,useful.formatMoney(Double.parseDouble(TotalAmount))).execute();
            //mListener.cashOutInsertHistory(transHistory(), transHistoryDetails());
            mListener.cashOutPositiveSecondScreen();
        }catch(Exception exx){



        }
    }



    /***public void SubmitCashOutMobileMoney(String callbackUrl,
                             String type,
                             String provider,
                             String amount,
                             String remarks,
                             String recipient,
                             String customerMsisdn,
                             String reference,
                             String client,
                             String application,
                             String country,String test){





        final ProgressDialog pDialog  = new ProgressDialog(getActivity());
        pDialog.setMessage("Please  wait...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
        pDialog.show();

        HashMap<String, String> params = new HashMap<String, String>();

        params.put("callbackUrl", callbackUrl);
        params.put("type", type);
        params.put("provider", provider);
        params.put("amount", amount);
        params.put("remarks", remarks);
        params.put("recipient", recipient);
        params.put("customerMsisdn", customerMsisdn);
        params.put("reference", reference);
        params.put("client", client);
        params.put("application", application);
        params.put("country", country);
        params.put("test", test);
        // Message.messageShort(MyApplication.getAppContext(), params.toString());
        volleySingleton= VolleySingleton.getsInstance();
        requestQueue=VolleySingleton.getRequestQueue();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                MyApplication.getPaymentUrl(),
                new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.hide();
                        try {

                            // booleaner=Boolean.getBoolean(response.getString("success").toString());


                            if(response.getString("success").toString().equals("true")){

                                /// Message.messageShort(MyApplication.getAppContext(),params.toString());

                                Dismiss();

                            }else if(response.getString("success").toString().equals("false")){
                                confirmLabel1.setText(response.getString("message").toString());
                            }

                        } catch (Exception e) {

                            e.printStackTrace();
                        }



                        //  Message.messageShort(MyApplication.getAppContext(),""+tokenValue+"\n"+response.toString()+"\n"+booleaner);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                pDialog.hide();
                Message.messageShort(MyApplication.getAppContext(), "\t\tSorry Try Again \n" + error.toString());

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type","application/json; charset=utf-8");
                headers.put("User-agent", "Ozinbo");
                return headers;
            }
        };
        int socketTimeout = 240000000;//4 minutes - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        requestQueue.add(request);




    }**/



    final void SAP(){

        HideButtons(true);
        setCancelable(false);
        transactionType="cashOutSap";
        confirmLayout.setVisibility(View.GONE);
        relativeDownButtonsLayout.setVisibility(View.VISIBLE);
        SapLayout.setVisibility(View.VISIBLE);
        setDialogTitle("Cash Out/Confirm Sap");
    }

    public void SubmitCashOutBankAccount(String Bank,
                                        final String Idtype,
                                        String Idvalue,
                                        String amount,
                                        String type)
    {

        final ProgressDialog pDialog  = new ProgressDialog(getActivity());
        pDialog.setMessage("Please  wait...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
        pDialog.show();

        HashMap<String, String> params = new HashMap<String, String>();

        params.put("Bank", Bank);
        params.put("Idtype", Idtype);
        params.put("Idvalue", Idvalue);
        params.put("Amount", amount);
        params.put("Type",type);

        // Message.messageShort(MyApplication.getAppContext(), params.toString());
        volleySingleton=VolleySingleton.getsInstance();
        requestQueue=VolleySingleton.getRequestQueue();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                MyApplication.getPAYMENT__CashOut_URL(),
                new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.hide();
                        try {


                           Log.d("response ",response.toString());

                            if(response.getString("status").equals("000")){


                                if (Idtype.equals("Account Number") || Idtype.equals("Phone Number")) {

                                    //show Sapform
                                    SAP();

                                }
                            }else{

                                controlHelpers.AnAlertertDialog(getActivity(),
                                        response.getString("status")+"" +
                                        ""+response.getString("message"),"").show();
                            }



                        } catch (Exception e) {



                           // dismiss();
                            e.printStackTrace();
                        }



                        //  Message.messageShort(MyApplication.getAppContext(),""+tokenValue+"\n"+response.toString()+"\n"+booleaner);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String ErrorMessage="Connection Failed !";
                String successMessage="Error Message";


                try {
                    VolleyError earror = new VolleyError(new String(error.networkResponse.data));
                    JSONObject jobj = new JSONObject(earror.getMessage().toString());

                    ErrorMessage=jobj.getString("message");
                    successMessage=jobj.getString("success");
                    Log.e("erre", earror.getMessage().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                pDialog.hide();

                controlHelpers.AnAlertertDialog(getActivity(),
                       ErrorMessage,"Sorry Try Again").show();

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type","application/json; charset=utf-8");
                headers.put("User-agent", "Ozinbo");
                return headers;
            }
        };
        int socketTimeout = 240000000;//4 minutes - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        requestQueue.add(request);




    }

    public void SubmitCashOutMobileMoney(String callbackUrl,
                                                     String type,
                                                     String provider,
                                                     String amount,
                                                     String remarks,
                                                     String recipient,
                                                     String customerMsisdn,
                                                     String reference,
                                                     String client,
                                                     String application,
                                                     String country,String test){





        final ProgressDialog pDialog  = new ProgressDialog(getActivity());
        pDialog.setMessage("Please  wait...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
        pDialog.show();

        HashMap<String, String> params = new HashMap<String, String>();

        params.put("callbackUrl", callbackUrl);
        params.put("type", type);
        params.put("provider", provider);
        params.put("amount", amount);
        params.put("remarks", remarks);
        params.put("recipient", recipient);
        params.put("customerMsisdn", customerMsisdn);
        params.put("reference", reference);
        params.put("client", client);
        params.put("application", application);
        params.put("country", country);
        params.put("test", test);
        // Message.messageShort(MyApplication.getAppContext(), params.toString());
        volleySingleton=VolleySingleton.getsInstance();
        requestQueue=VolleySingleton.getRequestQueue();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                MyApplication.getPAYMENT_URL_MobileURL(),
                new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.hide();
                        try {

                            // booleaner=Boolean.getBoolean(response.getString("success").toString());
                            Log.d("response",response.toString());

                            if(response.getString("success").toString().equals("true")){



                                //  Message.messageShort(MyApplication.getAppContext(),params.toString());

                                Dismiss();

                            }else {
                                controlHelpers.AnAlertertDialog(getActivity(),response.getString("success")+
                                        ""+response.getString("message"),"").show();
                            }

                        } catch (Exception e) {

                            e.printStackTrace();
                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                String ErrorMessage="Connection Failed !";
                String successMessage="ErrorMessage";


                try {
                    VolleyError earror = new VolleyError(new String(error.networkResponse.data));
                    JSONObject jobj = new JSONObject(earror.getMessage().toString());

                    ErrorMessage=jobj.getString("message");
                    successMessage=jobj.getString("success");

                    Log.e("erre", earror.getMessage().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                pDialog.hide();

                controlHelpers.AnAlertertDialog(getActivity(),
                        ErrorMessage,"Sorry Try Again").show();

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type","application/json; charset=utf-8");
                headers.put("User-agent", "Ozinbo");
                return headers;
            }
        };
        int socketTimeout = 990000000;//4 minutes - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        requestQueue.add(request);




    }


    public void ConfirmSap(String Sapecode,String Bank){

        final ProgressDialog pDialog  = new ProgressDialog(getActivity());
        pDialog.setMessage("Please  wait...Confirming Sap");
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
        pDialog.show();

        HashMap<String, String> params = new HashMap<String, String>();

        params.put("Bank", Bank);
        params.put("Sapcode", Sapecode);

        // Message.messageShort(MyApplication.getAppContext(), params.toString());
        volleySingleton=VolleySingleton.getsInstance();
        requestQueue=VolleySingleton.getRequestQueue();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                MyApplication.getConfirmSapUrl(),
                new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.hide();
                        try {

                            Log.d("Sap response ",response.toString());

                            if(response.getString("status").equals("000")){

                                Dismiss();

                            }else{

                                controlHelpers.AnAlertertDialog(getActivity(),"ErrorCode "+response.getString("status")+" " +
                                        ""+" Unsuccessful","").show();
                            }

                        } catch (Exception e) {

                            e.printStackTrace();
                        }




                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                String ErrorMessage="Connection Failed !";
                String successMessage="Error Message";


                try {
                    VolleyError earror = new VolleyError(new String(error.networkResponse.data));
                    JSONObject jobj = new JSONObject(earror.getMessage().toString());

                    ErrorMessage=jobj.getString("message");
                    successMessage=jobj.getString("success");

                    Log.e("erre",earror.getMessage().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                pDialog.hide();

                controlHelpers.AnAlertertDialog(getActivity(),ErrorMessage,"Sorry Try Again").show();

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type","application/json; charset=utf-8");
                headers.put("User-agent", "Ozinbo");
                return headers;
            }
        };
      int socketTimeout = 240000000;//4 minutes - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        requestQueue.add(request);




    }




    public void InPrint(String name,String provider,String phone,String Amount){
        PRINTER printer = new PRINTER();

        // initialize/activate Device
        printer.activateDevice();

        // Ready the printer
        printer.init();



        try{
            receiptNumber=useful.generateReceipt(MyApplication.getShopname(),MyApplication.getShopBranch(),UserId);
            printer.setFontMedium();
            printer.print(MyApplication.getShopname() + "-" + MyApplication.getShopBranch());
            printer.print("Tel :" + MyApplication.getShopPhoneNumber());
            printer.setFontSmall();
            printer.print("Date :" + useful.getDateWithFormatter() + "\n" + "Time : " + useful.getTimeWithFormatter());
            printer.print("Receipt.# :" + receiptNumber);
            printer.setFontMedium();
            printer.print(name);
            printer.setFontSmall();


            if(type.length()!=0){
                if(type.equals("cashOutBankAccount")){
                    printer.print("Bank :" + provider);
                    printer.print("Account.# :" + phone);


                }else if(type.equals("cashOutMobileWallet")){
                    printer.print("Provider :" + provider);
                    printer.print("Phone.# :" + phone);

                }


            }



            printer.print("Amount :" + Amount);
            printer.printBlankLine();
            printer.setFontLarge();
            printer.print("\t\t\t\t\t\t\t\t\t" + "Powered By Ozinbo");
            printer.endPrint();




        }catch(Exception ex){

            ex.printStackTrace();

        }
        // Deactivate Device
        printer.deactivateDevice();
        printer = null;


        try{
            //cash itemts ITEMS BOUGHT TO JSON
            //cash itemts ITEMS BOUGHT TO JSON
            CashOutItems cashInItems= new CashOutItems();
            cashInItems.type=type;
            cashInItems.accNumber=accountN;
            cashInItems.Amount=TotalAmount;
            cashInItems.Provider=provider;


            String getItemsBought =gson.toJson(cashInItems);
            Transactions transactions=SetTransactions(
                    receiptNumber,
                    Double.parseDouble(useful.prepareString4double(Amount)),
                    getItemsBought,
                    useful.getDateWithFormatter(),
                    useful.getTimeWithFormatter(),
                    "Cash Out",
                    "0",
                    "Offline",
                    "new",
                    "Ghs 0.0",
                    "Ghs 0.0",
                    "Ghs 0.0",
                    "Ghs 0.0"
            );
            Log.d(">CashOutItemsB, " , getItemsBought);
            // Log.d(">>>Transactions ", "  " + getTransactionItems);

            long lil=  dataSets.insertTransaction(transactions);
            Log.d(">>>Long this here :",""+lil);
            if(lil<1){

                Log.d(">>>Inserted Transaction",""+lil);
            }else{

                Log.d(">>>Inserted Transfailed",""+lil);
            }


        }catch(Exception e){

            e.printStackTrace();

        }

    }

    Transactions SetTransactions(String referenceNumber,
                                 double TranstotalAmount,
                                 String TransItems,
                                 String TransDate,
                                 String TransTime,
                                 String TransType,
                                 String TransStatus,
                                 String TransMode,
                                 String Transtoken,
                                 String TransDiscount,
                                 String TransAmountPaid,
                                 String TransTax,
                                 String TransChange ){

        Transactions transactions=new Transactions();
        // OnTransactions onTransactions = new OnTransactions();

        transactions.setReferenceNumber(referenceNumber);
        //  onTransactions.setReferenceNumber(referenceNumber);

        transactions.setTranstotalAmount(TranstotalAmount);
        // onTransactions.setTranstotalAmount(""+TranstotalAmount);




        transactions.setTransDate(TransDate);
        // onTransactions.setTransDate(useful.getDateWithFormatter());

        transactions.setTransTime(TransTime);
        // onTransactions.setTransTime(useful.getTimeWithFormatter());

        transactions.setTransType(TransType);
        // onTransactions.setTransType("Sales");


        transactions.setTransStatus(TransStatus);
        // onTransactions.setTransStatus(0 + "");

        transactions.setTransMode(TransMode);
        //  onTransactions.setTransMode("Offline");


        transactions.setTranstoken(Transtoken);
        // onTransactions.setTranstoken("new");


        transactions.setTransDiscount(useful.prepareString4double(TransDiscount));
        //  onTransactions.setTransDiscount(useful.prepareString4double(TransDiscount));

        transactions.setTransAmountPaid(useful.prepareString4double(TransAmountPaid));
        //  onTransactions.setTransAmountPaid(useful.prepareString4double(TransAmountPaid));

        transactions.setTransTax(useful.prepareString4double(TransTax));
        //  onTransactions.setTransTax(useful.prepareString4double(TransTax));

        transactions.setTransChange(useful.prepareString4double(TransChange));
        //  onTransactions.setTransChange(useful.prepareString4double(TransChange));

        String getItemsBought =gson.toJson(TransItems);
        transactions.setTransItems(getItemsBought);
        // transactions.setTransItems(getItemsB);
        // onTransactions.setTransItems(itemsBoughtsList);





        return transactions;


    }



    class MyPrintTask extends AsyncTask<Void,Void,Void> {
        //public String transtype;
        //public String things;
        public String name, provider, phone, Amount;
        public MyPrintTask(String name,String provider,String phone,String Amount){

            this.name=name;
            this.provider=provider;
            this.phone=phone;
            this.Amount=Amount;
        }


        @Override
        protected Void doInBackground(Void... params) {
            InPrint(name, provider,phone,Amount);
            return null;
        }
    }






class CashOutItems{
    String type; //by mobile or account etc ...
    String Provider;
    String accNumber;
    String Amount;
}}