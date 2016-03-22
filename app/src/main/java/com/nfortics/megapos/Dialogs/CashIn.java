package com.nfortics.megapos.Dialogs;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.widget.ArrayAdapter;
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
import com.nfortics.megapos.Activities.Home;
import com.nfortics.megapos.Application.MyApplication;
import com.nfortics.megapos.DataBase.DataSets;
import com.nfortics.megapos.Domain.BankModel;
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
import com.nfortics.megapos.utils.BiometricUtils;
import com.nfortics.megapos.utils.GsonMan;

import org.json.JSONException;
import org.json.JSONObject;
import org.m2i.m835.api.BIO;
import org.m2i.m835.api.IBIOConnState;
import org.m2i.m835.api.PRINTER;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CashIn.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CashIn#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CashIn extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    Gson gson = new Gson();
    Typefacer typefacer;
    RelativeLayout
            relativeDownButtonsLayout,
            relativeActions,
            layoutFinger,
            mobileMoneyLayout,
            PrepaidCardLayout,
            confirmLayout,SapLayout;

    Button
            backAccount,
            prePaid,
            mobileMoney,
            backButton,
            submitButton,
            confirmYesButton,
            confirmNoButton,
            butFingerPrint,butconfirmSap;

    TextView selectBankLabel,
            identifyLabel,
            enterAmtLabel,
            enterBankAcLabel,
            dialogTitle,
            confirmLabel1,
            confirmLabel2,
            confirmLabel3,
            confirmLabel4,m_txtStatus;

    EditText enterAccountdetails,
            enterAmt,
            enterMobileAcc,
            enterMobileAmount,
            edtCardNumber,
            edtPrepAmount,
            edtFingerUserID;

    Spinner bankTypeSpinners,
            paymenIdentitySpinners,
            telcosSpinners,
            prepaidVendorsSpinner;

    ImageView fingerImageViewer;

    ControlHelpers controlHelpers;

    Useful useful;


    TransHistory transHistory;
    TransHistoryDetails transHistoryDetails;

    BiometricUtils biometrics;
    String newTitle;
    Bundle extras;

    String userName;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    ConnectionDetector cd ;
    RootAccess rootAccess;

    DataSets
            dataSets;
    String receiptNumber;
  String UserId;

    GsonMan gsonMan;
    JSONObject jsonObject;
   // Activity ctx;


    public String transactionType;
    String provider,phone,TotalAmount,accountN,type;
    ////////////

    // TODO: Rename and change types and number of parameters
    public static CashIn newInstance(String param1, String param2) {
        CashIn fragment = new CashIn();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public CashIn() {
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


        controlHelpers=new ControlHelpers();
        useful=new Useful();
        jsonObject = new JSONObject();
        dataSets = new DataSets(getActivity());
        UserId="900090";

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        return inflater.inflate(R.layout.fragment_cash_in, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        InitializeView(view);
        SetTypeFace();
        InitializeClickListeners();
        setDialogTitle("Cash In");



            populateSpinners();


        SetChangeListenersForSpinners();
        InitializeNetworkAdapters();
    }
    void BiometricEngine(){

       // biometrics=new BiometricUtils(getActivity(),fingerStatus,fingerImageViewer,edtFingerUserID);
    }
    void populateSpinners(){


        controlHelpers.makeListForBankSpinners(getActivity(), bankTypeSpinners, userName);
        controlHelpers.makeListForTelcosSpinners(getActivity(), telcosSpinners, userName);


        List<String> data2=new ArrayList<>();
        data2.add("Account Number");
        data2.add("Phone Number");
       // data2.add("Finger Print");
        data2.add("Card");
        controlHelpers.populateSpinner(paymenIdentitySpinners, data2, getActivity());


        List<String> data4 = new ArrayList<>();
        data4.add("EZSWITCH");
        data4.add("VISA");
        data4.add("MASTER");
        controlHelpers.populateSpinner(prepaidVendorsSpinner, data4, getActivity());


    }
    public void InitializeNetworkAdapters(){

        try{

            cd = new ConnectionDetector(getActivity());
            volleySingleton=VolleySingleton.getsInstance();
            requestQueue=VolleySingleton.getRequestQueue();
        }catch(Exception ex){

        }

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

        backAccount=(Button)view.findViewById(R.id.butBankAc);
        prePaid=(Button)view.findViewById(R.id.butPrepaidC);
        mobileMoney=(Button)view.findViewById(R.id.butMobileM);
        backButton=(Button)view.findViewById(R.id.butBack);
        submitButton=(Button)view.findViewById(R.id.butSubmit);

        confirmYesButton=(Button)view.findViewById(R.id.confirmYesButton);
        confirmNoButton=(Button)view.findViewById(R.id.confirmNoButton);


        butFingerPrint=(Button)view.findViewById(R.id.butFingerPrint);

        butFingerPrint.setText("Open");

        dialogTitle=(TextView)view.findViewById(R.id.txtDialogTitle);
        selectBankLabel=(TextView)view.findViewById(R.id.txtSelectBankLabel);
        identifyLabel=(TextView)view.findViewById(R.id.txtIdentifyLabel);
        enterAmtLabel=(TextView)view.findViewById(R.id.txtEnterAmtLabel);
        enterBankAcLabel=(TextView)view.findViewById(R.id.txtBackAcLabel);
        m_txtStatus=(TextView)view.findViewById(R.id.txtStatus);

        confirmLabel1=(TextView)view.findViewById(R.id.txtconfirm1);
        confirmLabel2=(TextView)view.findViewById(R.id.txtconfirm2);
        confirmLabel3=(TextView)view.findViewById(R.id.txtconfirm3);
        confirmLabel4=(TextView)view.findViewById(R.id.txtconfirm4);


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
        edtFingerUserID=(EditText)view.findViewById(R.id.edtFingerUserID);


        fingerImageViewer=(ImageView)view.findViewById(R.id.ivImageViewer);



    }
    public void InitializeClickListeners(){

        BankAccountButtonClickLister();
        BackButtonClickListener();
        SubmitButtonClickListener();
        MobileMoneyButtonClickLister();
        PrepaidButtonClickLister();
        confirmNoButtonClickListener();
        confirmYesButtonClickListener();
        butFingerPrintClickLister();
        //SAPconfirmClickLister();
    }
    void BankAccountButtonClickLister(){
        backAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideButtons(true);
                setCancelable(false);
                transactionType = "cashInBankAccount";
                relativeDownButtonsLayout.setVisibility(View.VISIBLE);
                relativeActions.setVisibility(View.VISIBLE);
                setDialogTitle("Cash In/Bank Account");
            }
        });

    }
    void MobileMoneyButtonClickLister(){
        mobileMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideButtons(true);
                setCancelable(false);
                transactionType="cashInMobileWallet";
                relativeDownButtonsLayout.setVisibility(View.VISIBLE);
                mobileMoneyLayout.setVisibility(View.VISIBLE);
                setDialogTitle("Cash In/Mobile Money");
            }
        });

    }
    void PrepaidButtonClickLister(){
        prePaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideButtons(true);
                setCancelable(false);
                transactionType="cashInprepaid";
                relativeDownButtonsLayout.setVisibility(View.VISIBLE);
                PrepaidCardLayout.setVisibility(View.VISIBLE);
                setDialogTitle("Cash In/Prepaid");
            }
        });

    }

    void BackButtonClickListener(){

       // mListener.CurrencyDetectOff();
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideButtons(false);
                setCancelable(true);
                relativeDownButtonsLayout.setVisibility(View.GONE);
                relativeActions.setVisibility(View.GONE);
                mobileMoneyLayout.setVisibility(View.GONE);
                PrepaidCardLayout.setVisibility(View.GONE);

                setDialogTitle("Cash In");
               // mListener.cashInCurrencyDetectOff();
            }
        });


    }
    void SubmitButtonClickListener(){
        //mListener.CurrencyDetectOn();
        try{
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                  //  mListener.cashInCurrencyDetectOn();
                    String titleToken = dialogTitle.getText().toString();
                  //  mListener.CurrencyDetectOn();

                    switch (titleToken) {

                        case "Cash In/Bank Account":
                            if (enterAccountdetails.getText().length() == 0 ||
                                    enterAmt.getText().length() == 0 || bankTypeSpinners.getSelectedItem().toString().length() == 0) {
                            } else {
                                ConfirmDialog(true, dialogTitle.getText().toString(), relativeActions);
                                SetValuesToConfirmLabels("Cash In",
                                        "Provider :" + bankTypeSpinners.getSelectedItem().toString(),
                                        enterAccountdetails.getText().toString(),
                                        useful.formatMoney(Double.parseDouble(enterAmt.getText().toString())));


                                //CURENCY DETECTOR
                                //mListener.cashInCurrencyDetectOn();


                                ///SECOND SCREEN
                                mListener.cashInSecondScreen(titleToken,
                                        "Provider :" + bankTypeSpinners.getSelectedItem().toString(),
                                        enterAccountdetails.getText().toString(),
                                        useful.formatMoney(Double.parseDouble(enterAmt.getText().toString())));
                              // mListener.CurrencyDetectOn();


                            }
                            break;
                        case "Cash In/Mobile Money":
                            if (enterMobileAcc.getText().length() == 0 ||
                                    enterMobileAmount.getText().length() == 0 || telcosSpinners.getSelectedItem().toString().length() == 0) {
                            } else {


                                ConfirmDialog(true, dialogTitle.getText().toString(), mobileMoneyLayout);
                                SetValuesToConfirmLabels("Cash In",
                                        "Provider :" + telcosSpinners.getSelectedItem().toString(),
                                        enterMobileAcc.getText().toString(),
                                        useful.formatMoney(Double.parseDouble(enterMobileAmount.getText().toString())));


                                mListener.cashInSecondScreen(titleToken,
                                        "Provider :" + telcosSpinners.getSelectedItem().toString(),
                                        enterMobileAcc.getText().toString(),
                                        useful.formatMoney(Double.parseDouble(enterMobileAmount.getText().toString())));


                            }
                          //  mListener.CurrencyDetectOn();
                            break;

                        case "Cash In/Prepaid":


                            if (edtCardNumber.getText().toString().length() == 0 ||
                                    edtPrepAmount.getText().toString().length() == 0 ||
                                    prepaidVendorsSpinner.getSelectedItem().toString().length() == 0) {

                            } else {

                                ConfirmDialog(true, dialogTitle.getText().toString(), PrepaidCardLayout);
                                SetValuesToConfirmLabels("Cash In",
                                        prepaidVendorsSpinner.getSelectedItem().toString(),
                                        edtCardNumber.getText().toString(),
                                        useful.formatMoney(Double.parseDouble(edtPrepAmount.getText().toString())));

                                mListener.cashInSecondScreen(titleToken,
                                        prepaidVendorsSpinner.getSelectedItem().toString(),
                                        edtCardNumber.getText().toString(),
                                        useful.formatMoney(Double.parseDouble(edtPrepAmount.getText().toString())));

                               // mListener.CurrencyDetectOn();
                            }
                            break;

                        case "Cash In/Confirm Sap":



                            break;
                    }


                }
            });

        }catch(Exception ex){



        }




    }
    void confirmNoButtonClickListener() {

        confirmNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmDialog(false, newTitle, setGetLayout(newTitle));
             mListener.cashInClearSecondScreen();
            //  mListener.CurrencyDetectOn();
            }
        });
    }
    void confirmYesButtonClickListener(){
        confirmYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                switch (transactionType) {


                    case "cashInBankAccount":


                        // String provider,phone,TotalAmount;
                        type="cashInBankAccount";
                        accountN = enterAccountdetails.getText().toString();
                        provider =  bankTypeSpinners.getSelectedItem().toString();
                        TotalAmount =  enterAmt.getText().toString();
                        Log.d(" bank Type ", bankTypeSpinners.getSelectedItem().toString() +
                                " Payment Type " + paymenIdentitySpinners.getSelectedItem().toString() +
                                " AccountDetails " + enterAccountdetails.getText().toString() + " Amount " + enterAmt.getText().toString()
                                + " Type " + " Cashin");

                        //SubmitCashInSubmitCashOutMobileMoney("callback","cashIn","mtn","20.0","nothing","ccml","02440088705","refrere","38sig","ozinbo","gh","true");
                        SubmitCashInBankAccount(
                                bankTypeSpinners.getSelectedItem().toString(),
                                paymenIdentitySpinners.getSelectedItem().toString(),
                                "002-000001-01",
                               // enterAccountdetails.getText().toString(),
                                enterAmt.getText().toString(), "cashIn");
                        break;


                    case "cashInMobileWallet":


                        type="cashInMobileWallet";
                        accountN =   enterMobileAcc.getText().toString();
                        provider =   telcosSpinners.getSelectedItem().toString();
                        TotalAmount =  enterMobileAmount.getText().toString();

                        Log.d(" bank Type ",provider +
                                " Payment Type " + "" +
                                " AccountDetails " + accountN+ " Amount " +TotalAmount
                                + " Type " + " CashinMobile");

                  receiptNumber=useful.generateReceipt(MyApplication.getShopname(),MyApplication.getShopBranch(),UserId);
                  Log.d("Values >> ",enterMobileAcc.getText().toString()+" : "+telcosSpinners.getSelectedItem().toString()+" : "+enterMobileAmount.getText().toString());
                        SubmitCashInMobileMoney(
                                "",
                                "cashIn",
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

                        Log.d("ReceiptN",receiptNumber);

                        break;
                    case "cashInprepaid":


                        // SubmitCashInMobile("",
                        //   "cashIn","mtn","20","nothing");

                        break;
                }


            }
        });
    }
    void butFingerPrintClickLister(){

        butFingerPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (butFingerPrint.getText() == "Open") {
                    //OnOpenDeviceBtn();

                } else if (butFingerPrint.getText() == "Opened") {

                    //OnEnrollBtn();
                  //  OnGetImageBtn();
                } else if (butFingerPrint.getText() == "good") {
                    //  OnEnrollBtn();
                }


            }
        });
    }

    /////////////////////////////////

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
           // printer.print("Provider :" + provider);

            if(type.length()!=0){
                if(type.equals("cashInBankAccount")){
                    printer.print("Bank :" + provider);
                    printer.print("Account.# :" + phone);


                }else if(type.equals("cashInMobileWallet")){
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
            CashInItems cashInItems= new CashInItems();
            cashInItems.type=type;
            cashInItems.accNumber=accountN;
            cashInItems.Amount=TotalAmount;
            cashInItems.Provider=provider;

            /**
             *  type="cashInBankAccount";
             accountN = enterAccountdetails.getText().toString();
             provider =  bankTypeSpinners.getSelectedItem().toString();
             TotalAmount =  enterAmt.getText().toString();
             * **/
            //cashInItems.accNumber=ac
            ////////

            String getItemsBought =gson.toJson(cashInItems);
            Transactions transactions=SetTransactions(
                    receiptNumber,
                    Double.parseDouble(useful.prepareString4double(Amount)),
                    getItemsBought,
                    useful.getDateWithFormatter(),
                    useful.getTimeWithFormatter(),
                    "Cash In",
                    "0",
                    "Offline",
                    "new",
                    "Ghs 0.0",
                    "Ghs 0.0",
                    "Ghs 0.0",
                    "Ghs 0.0"
            );
            Log.d(">Airtime Items", "  " + getItemsBought);
            // Log.d(">>>Transactions ", "  " + getTransactionItems);

            long lil=  dataSets.insertTransaction(transactions);
            Log.d(">>>Long this here :",""+lil);
            if(lil<1){

                Log.d(">>>Inserted Transaction",""+lil);
            }else{

                Log.d(">>>Inserted Transfailed",""+lil);
            }

            // String getTransactionItems =gson.toJson(transactions);
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

    ////////////////////////////
    TransHistory transHistory(){
        transHistory= new TransHistory();
        transHistory.setDate(useful.getDateWithFormatter());
        transHistory.setTime(useful.getTimeWithFormatter());
        transHistory.setTransID("0001");
        transHistory.setSellerID("Yaw Boafo");
        transHistory.setMerchantID("CCML");
        transHistory.setType("OffLine");
        transHistory.setDetailsID(useful.getTimeWithFormatter());
        transHistory.setModetype("Banker");
        transHistory.setTotalSale(confirmLabel4.getText().toString());
                return transHistory;
    }
    TransHistoryDetails transHistoryDetails(){

        transHistoryDetails = new TransHistoryDetails();
        transHistoryDetails.setDeTailsID(useful.getTimeWithFormatter());
        transHistoryDetails.setItems(BuildStringTopPRINT());
        transHistoryDetails.setSale("");


        return transHistoryDetails;
    }
    void SetValuesToConfirmLabels(String one,String two,String three,String four){
        confirmLabel1.setText(one);
        confirmLabel2.setText(two);
        confirmLabel3.setText("A/c# :" + three);
        confirmLabel4.setText("Total :" + four);
    }
    void SetTypeFace(){
        typefacer=new Typefacer();
        selectBankLabel.setTypeface(typefacer.getRoboCondensedRegular(getActivity().getAssets()));
        identifyLabel.setTypeface(typefacer.getRoboCondensedRegular(getActivity().getAssets()));
        enterAmtLabel.setTypeface(typefacer.getRoboCondensedRegular(getActivity().getAssets()));
        enterBankAcLabel.setTypeface(typefacer.getRoboCondensedRegular(getActivity().getAssets()));
    }
    void HideButtons(boolean token){

        if(token){
            backAccount.setVisibility(View.GONE);
            prePaid.setVisibility(View.GONE);
            mobileMoney.setVisibility(View.GONE);

        }else{

            backAccount.setVisibility(View.VISIBLE);
            prePaid.setVisibility(View.VISIBLE);
            mobileMoney.setVisibility(View.VISIBLE);

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
    String BuildStringTopPRINT(){

        StringBuilder sb = new StringBuilder();
        sb.append(confirmLabel2.getText().toString()+"\n"
                +confirmLabel3.getText().toString()+"\n"
                +confirmLabel4.getText().toString());


        return sb.toString();
    }
    String getSetDialogTitle(String value){

        if(value.contains("Cash In/Bank Account")){

            newTitle="Cash In/Bank Account";
        }else if(value.contains("Cash In/Mobile Money")){

            newTitle="Cash In/Mobile Money";

        }else if(value.contains("Cash In/Prepaid")){

            newTitle="Cash In/Prepaid";

        }else if(value.contains("Cash In/Confirm Sap")){

            newTitle="Cash In/Confirm Sap";
        }

        return newTitle;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        gsonMan= new GsonMan();

        try {
        extras = getActivity().getIntent().getExtras();



        if(extras == null) {

        }else {


            userName= extras.getString("UserDetails");
          //  UserId=gsonMan.getMerchantDetails(userName, activity).getId();
//            Log.d("UserID   > > > : ",UserId);
        }

            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {

        }
    }
    public void setDialogTitle(String title){

       dialogTitle.setText(title);
   }
    public RelativeLayout setGetLayout(String value){

        RelativeLayout relativeLayout=null;

        if(value.contains("Cash In/Bank Account")){

            relativeLayout= relativeActions;
        }else if(value.contains("Cash In/Mobile Money")){

            relativeLayout= mobileMoneyLayout;

        }else if(value.contains("Cash In/Prepaid")){

            relativeLayout= PrepaidCardLayout;



        }

        return relativeLayout;
    }
    public  interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String name, String price);

        public void cashInPrint(String name, String provider,String acc,String amount);

        public void cashInInsertHistory(TransHistory transHistory,TransHistoryDetails transHistoryDetails);

        public void cashInSecondScreen(String type,String provider,String Account, String amt);

        public void cashInClearSecondScreen();

        public void cashInPositiveSecondScreen();

        public void cashInCurrencyDetectOn();
        public void cashInCurrencyDetectOff();
    }
    public String TrimIdentitySelected(String value){
        String val="";

        switch(value){

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
    void Dismiss(){
        try{
            // mListener.cashInCurrencyDetectOn();
            dismiss();//accountN = enterAccountdetails.getText().toString();
           // provider =  bankTypeSpinners.getSelectedItem().toString();
           // TotalAmount
            new MyPrintTask("CashIn", provider,accountN,useful.formatMoney(Double.parseDouble(TotalAmount))).execute();
            // mListener.cashInInsertHistory(transHistory(), transHistoryDetails());
            // mListener.CurrencyDetectOff();
            mListener.cashInPositiveSecondScreen();
        }catch(Exception exx){

exx.printStackTrace();

        }
    }
    /**
     * /*
     * "callbackUrl": "",
     "type": "",
     "provider": "",
     "amount": 0,
     "remarks": "",
     "recipient": "",
     "customerMsisdn": "",
     "reference": "",
     "client": "",
     "application": "",
     "country": "",
     "test": false
     }
     *
     * */

    public void SubmitCashInMobileMoney(String callbackUrl,
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


                            Log.d("response",response.toString());

                            if(response.getString("success").toString().equals("true")){




                                Dismiss();

                            }else {
                                controlHelpers.AnAlertertDialog(getActivity(),response.getString("success").toString()+"" +
                                        ""+response.getString("message").toString(),"").show();
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
        int socketTimeout = 990000000;//4 minutes - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        requestQueue.add(request);




    }

    public void SubmitCashInBankAccount(String Bank,
                                        String Idtype,
                                        String Idvalue,
                                        String amount,
                                        String type){

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
                MyApplication.getPaymentUrl(),
                new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.hide();
                        try {

                            // booleaner=Boolean.getBoolean(response.getString("success").toString());
                        Log.d("Response ", response.toString());
                                      if(response.getString("status").equals("000")){

                                          Dismiss();
                                      }else{

                                          controlHelpers.AnAlertertDialog(getActivity(),
                                                  ""+response.getString("message").toString(),"").show();


                                      }



                        } catch (Exception e) {

                            e.printStackTrace();
                        }



                        //  Message.messageShort(MyApplication.getAppContext(),""+tokenValue+"\n"+response.toString()+"\n"+booleaner);
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


}

class CashInItems{
    String type; //by mobile or account etc ...
    String Provider;
    String accNumber;
    String Amount;
}