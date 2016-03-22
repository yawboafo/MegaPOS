package com.nfortics.megapos.Dialogs;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nfortics.megapos.Application.MyApplication;
import com.nfortics.megapos.Helper.ControlHelpers;
import com.nfortics.megapos.Helper.CurrencyTextWatcher;
import com.nfortics.megapos.Helper.Message;
import com.nfortics.megapos.Helper.Typefacer;
import com.nfortics.megapos.Helper.Useful;
import com.nfortics.megapos.Models.TransHistory;
import com.nfortics.megapos.Models.TransHistoryDetails;
import com.nfortics.megapos.Network.ConnectionDetector;
import com.nfortics.megapos.Network.VolleySingleton;
import com.nfortics.megapos.R;
import com.nfortics.megapos.ThirrtySevenSignals.RootAccess;
import com.nfortics.megapos.utils.GsonMan;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.m2i.m835.api.SECONDSCREEN;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class CashPaymentDialog extends DialogFragment {


    public   String tax,discount,total,amtPaid,amtChange;
    Button cash,mobile,creditCard,pay,back,printerbut,checkout,backButton;
    public TextView Tax,Discount,Total,TaxValue,DiscountValue,TotalValue,DialogTitle;
    EditText cashAmount;
    private boolean checkselectable;
    Spinner mobileMoneyTelcos;
    RelativeLayout relativeLayout,
            containerLayout,
            lessButContainerLayout;
    LinearLayout linearLayout;
    InputMethodManager IMM;
    CommunicatePrinter communicatePrinter;
    ControlHelpers controlHelpers;
    Useful useful;
    Typefacer typeface;

    public String paymentType;
    GsonMan  gsonMan;
    JSONObject jsonObject;
    private VolleySingleton
            volleySingleton;
    private RequestQueue
            requestQueue;
    ConnectionDetector cd ;
    RootAccess rootAccess;
    Bundle extras;
    String userName;
    String receiptNumber;
    public CashPaymentDialog() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        typeface = new Typefacer();
        useful =new Useful();
        controlHelpers=new ControlHelpers();
        IMM=(InputMethodManager)getActivity()
       .getSystemService(Context.INPUT_METHOD_SERVICE);
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

        try{
            tax=this.getArguments().getString("Tax");
            discount=this.getArguments().getString("Discount");
            total=this.getArguments().getString("Total");
            communicatePrinter=(CommunicatePrinter)activity;
            gsonMan= new GsonMan();


            extras = getActivity().getIntent().getExtras();



            if(extras == null) {

            }else {


                userName= extras.getString("UserDetails");

            }

        }catch(Exception e){


        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCancelable(false);

        View view= inflater.inflate(R.layout.fragment_cash_payment_dialog, null);

        return view;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        InitaiLiseViews(view);
        setOnClickListeners();
        PopulateSpinner();
    }
    void setDialogTitle(String val){

       DialogTitle.setText(val);



   }
    public void InitaiLiseViews(View view){


        containerLayout=(RelativeLayout)view.findViewById(R.id.containerfrag);
        lessButContainerLayout=(RelativeLayout)view.findViewById(R.id.lessbutcontain);
        cash=(Button)view.findViewById(R.id.butPayCash);
        mobile=(Button)view.findViewById(R.id.butPayMobile);
        creditCard=(Button)view.findViewById(R.id.butPayCard);


        cashAmount=(EditText)view.findViewById(R.id.editAmount);

        pay=(Button)view.findViewById(R.id.butPay);
        printerbut=(Button)view.findViewById(R.id.printbut);
        printerbut.setVisibility(View.GONE);
        checkout=(Button)view.findViewById(R.id.butcheckout);
        checkout.setVisibility(View.GONE);

        backButton=(Button)view.findViewById(R.id.butBack);

        Tax=(TextView)view.findViewById(R.id.txtTax);
        Discount=(TextView)view.findViewById(R.id.txtDiscount);
        Total=(TextView)view.findViewById(R.id.txtTotal);
        TaxValue=(TextView)view.findViewById(R.id.TaxValue);
        DiscountValue=(TextView)view.findViewById(R.id.DiscountValue);
        TotalValue=(TextView)view.findViewById(R.id.TotalValue);

        DialogTitle=(TextView)view.findViewById(R.id.txtDialogTitle);

        mobileMoneyTelcos=(Spinner)view.findViewById(R.id.mobileMoneyTelcos);

        TaxValue.setText("" + tax);
        DiscountValue.setText("" + discount);
        TotalValue.setText("" + total);




  // back.setTypeface(typeface.getRoboCondensedRegular(getActivity().getAssets()));


        setTypeFacer();

    }
    public void PopulateSpinner(){
        controlHelpers.makeListForTelcosSpinners(getActivity(), mobileMoneyTelcos, userName);
    }
    public void setTypeFacer(){

        TaxValue.setTypeface(typeface.getRoboCondensedRegular(getActivity().getAssets()));
        DiscountValue.setTypeface(typeface.getRoboCondensedRegular(getActivity().getAssets()));
        TotalValue.setTypeface(typeface.getRoboCondensedRegular(getActivity().getAssets()));
        Tax.setTypeface(typeface.getRoboCondensedRegular(getActivity().getAssets()));
        Discount.setTypeface(typeface.getRoboCondensedRegular(getActivity().getAssets()));
        Total.setTypeface(typeface.getRoboCondensedRegular(getActivity().getAssets()));
        pay.setTypeface(typeface.getRoboCondensedRegular(getActivity().getAssets()));
    }
    public void setOnClickListeners(){
        try{
            payClickListener();
            payModeCashClickListener();
            payModeMobileClickListener();
            backButtonClickListener();
            printerbutClickListener();
            printerbutClickListener();
            butCheckOutClickListener();

        }catch(Exception ex){


        }


    }
    void confirm2bdScreen(String tax,String discount,String total){

        SECONDSCREEN secondscreen = new SECONDSCREEN();
        secondscreen.activateDevice();
        if (secondscreen.init()) {
            try {


                secondscreen.printOnScreen("** Confirm ***", 0, 0);
                secondscreen.printOnScreen("Tax : "+tax, 0, 16);
                secondscreen.printOnScreen("Discount : "+discount, 0, 32);
                secondscreen.printOnScreen("-----------------------------", 0, 48);
                secondscreen.printOnScreen("Total :"+total, 0, 64);


            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
        }
        secondscreen.deactivateDevice();
        secondscreen = null;



    }
    void confirm2bdScreenMobile(String tax,String discount,String total,String phonenumber){

          SECONDSCREEN secondscreen = new SECONDSCREEN();
          secondscreen.activateDevice();
          if (secondscreen.init()) {
              try {


                  secondscreen.printOnScreen("** Confirm ***", 0, 0);
                  secondscreen.printOnScreen("Tax : "+tax, 0, 16);
                  secondscreen.printOnScreen("Discount : "+discount, 0, 32);
                  secondscreen.printOnScreen("-----------------------------", 0, 48);
                  secondscreen.printOnScreen("Total :"+total, 0, 64);
                  secondscreen.printOnScreen("Phone Number ?", 0, 80);
                  secondscreen.printOnScreen(""+phonenumber, 0, 96);

              } catch (UnsupportedEncodingException ex) {
                  ex.printStackTrace();
              }
          }
          secondscreen.deactivateDevice();
          secondscreen = null;



      }
    void clear2ndScreen(){

        SECONDSCREEN secondscreen = new SECONDSCREEN();
        secondscreen.activateDevice();
        if (secondscreen.init()) {
            try {


                secondscreen.printOnScreen("** Thank you  ***", 0, 0);



            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
        }
        secondscreen.deactivateDevice();
        secondscreen = null;


    }
    void PaymentMainScreen(boolean val,String Title){


        if(val){

            cash.setVisibility(View.GONE);
            mobile.setVisibility(View.GONE);
            creditCard.setVisibility(View.GONE);

            containerLayout.setVisibility(View.VISIBLE);
            lessButContainerLayout.setVisibility(View.VISIBLE);
            pay.setVisibility(View.VISIBLE);
            cashAmount.setVisibility(View.VISIBLE);
            cashAmount.requestFocus();
            cashAmount.setHint("Enter Phone Number ");
            IMM.showSoftInput(cashAmount, InputMethodManager.SHOW_IMPLICIT);

            backButton.setVisibility(View.VISIBLE);

            setDialogTitle(Title);
        }else {

            setDialogTitle("CHECK OUT");
            IMM.hideSoftInputFromWindow(cashAmount.getWindowToken(),0);
            containerLayout.setVisibility(View.GONE);
            lessButContainerLayout.setVisibility(View.GONE);
            pay.setVisibility(View.GONE);
            cashAmount.setVisibility(View.GONE);
            backButton.setVisibility(View.GONE);

            cash.setVisibility(View.VISIBLE);
            mobile.setVisibility(View.VISIBLE);
            creditCard.setVisibility(View.VISIBLE);
        }

    }
    void PaymentMainScreen(boolean val){


        if(val){

            cash.setVisibility(View.GONE);
            mobile.setVisibility(View.GONE);
            creditCard.setVisibility(View.GONE);

            containerLayout.setVisibility(View.VISIBLE);
            lessButContainerLayout.setVisibility(View.VISIBLE);
            pay.setVisibility(View.VISIBLE);
            cashAmount.setVisibility(View.VISIBLE);
            cashAmount.requestFocus();
            cashAmount.setHint("Enter Amount");
            IMM.showSoftInput(cashAmount, InputMethodManager.SHOW_IMPLICIT);

            backButton.setVisibility(View.VISIBLE);

            setDialogTitle("CHECK OUT/CASH");
        }else {

            setDialogTitle("CHECK OUT");
            IMM.hideSoftInputFromWindow(cashAmount.getWindowToken(),0);
            containerLayout.setVisibility(View.GONE);
            lessButContainerLayout.setVisibility(View.GONE);
            pay.setVisibility(View.GONE);
            cashAmount.setVisibility(View.GONE);
            backButton.setVisibility(View.GONE);

            cash.setVisibility(View.VISIBLE);
            mobile.setVisibility(View.VISIBLE);
            creditCard.setVisibility(View.VISIBLE);
        }

    }
    void payClickListener(){
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Pay ButtonClicked >> ", "Pay Type " + DialogTitle.getText().toString());

                try {



                    switch (DialogTitle.getText().toString()){


                        case "CHECK OUT/CASH":
                            //PaymentMainScreen(true);
                            if (cashAmount.length() <= 0) {


                            } else {


                                Double val = Double.parseDouble(cashAmount.getText().toString());

                                Double totalValue = Double.parseDouble(useful.prepareString4double(total));
                                Double balance = totalValue - val;

                                Tax.setText("Total Amount : ");
                                TaxValue.setText(total);

                                Discount.setText("Amount Paid : ");
                                DiscountValue.setText("" + useful.formatMoney(Double.parseDouble(val.toString())));


                                if (balance > 0) {
                                    Total.setText("InSufficient Funds : ");
                                  //  TotalValue.setVisibility(View.GONE);
                                   // TaxValue.setVisibility(View.GONE);
                                    TotalValue.setText("-" + useful.formatMoney(Double.parseDouble(balance.toString())));
                                    printerbut.setVisibility(View.GONE);
                                    //checkout.setVisibility(View.GONE);

                                } else {
                                    TotalValue.setVisibility(View.VISIBLE);
                                    Total.setText("Change : ");
                                    TotalValue.setText("" + useful.formatMoney(Double.parseDouble(balance.toString())));

                                    printerbut.setVisibility(View.VISIBLE);
                                    //  checkout.setVisibility(View.VISIBLE);

                                }
                            }


                            break;
                        case "CHECK OUT/MOBILE":



                            if (cashAmount.length() <= 0) {


                            } else {

                                printerbut.setVisibility(View.VISIBLE);
                    new ConfirmSecondScreenMobile(tax,discount,total,cashAmount.getText().toString()).execute();



                            }

                            break;
                    }




                } catch (Exception ex) {


                    Log.e("Error",ex.toString());

                }

            }
        });


    }
    void printerbutClickListener(){


        printerbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("Printer >> ", "was clicked");

                switch (DialogTitle.getText().toString()){

                    case "CHECK OUT/CASH":

                        new ClearSecondScreen().execute();
                        amtPaid = DiscountValue.getText().toString();
                        amtChange=TotalValue.getText().toString();
                        dismiss();
                        communicatePrinter.doAprint(paymentType, amtPaid, amtChange);
                        break;

                    case "CHECK OUT/MOBILE":

                        Double totalValue = Double.parseDouble(useful.prepareString4double(total));
                        receiptNumber=useful.generateReceipt(MyApplication.getShopname(),MyApplication.getShopBranch(),MyApplication.getUserID(getActivity()));

                        amtPaid = DiscountValue.getText().toString();
                        amtChange = TotalValue.getText().toString();

                        VolleySubmitCashOutMobileMoney(""
                                ,
                                "cashout",
                                mobileMoneyTelcos.getSelectedItem().toString(),
                                totalValue + "",
                                "nothing",
                                MyApplication.getApplicationClient(),
                                cashAmount.getText().toString(),
                                receiptNumber,
                                MyApplication.getApplicationClient(),
                                MyApplication.getApp_name(),
                                MyApplication.getApp_Country(),
                                MyApplication.getApplicationSeriousness().toString()
                        );
                        Log.d("receiptNumber  ", receiptNumber+"  provider   " + mobileMoneyTelcos.getSelectedItem().toString());



                        Log.d("checking out mobile ", cashAmount.getText().toString());
                        break;


                }



            }
        });



    }
    void butCheckOutClickListener(){

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("Check Out >> ","was clicked");
                new ClearSecondScreen().execute();

                amtPaid = DiscountValue.getText().toString();
                amtChange = TotalValue.getText().toString();
                dismiss();
                communicatePrinter.checkout(amtPaid, amtChange);
            }
        });

    }
    void Dismiss(){

       Log.d("dis>> ", "Dismiss was called ");

        communicatePrinter.doAprint(paymentType, amtPaid, amtChange);
        new ClearSecondScreen().execute();
        dismiss();
    }
    void payModeCashClickListener(){

         cash.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 try{


                     ///INITIALIZE SCREEN
                     TaxValue.setText("" + tax);
                     DiscountValue.setText("" + discount);
                     TotalValue.setText("" + total);


                     //HIDE THIS SPINNER
                     mobileMoneyTelcos.setVisibility(View.GONE);

                     ///HIDE BUTTONS///
                     printerbut.setVisibility(View.GONE);
                     cashAmount.setText("");
                     getDialog().setCancelable(false);
                     paymentType="Cash";
                     PaymentMainScreen(true);
                     new ConfirmSecondScreen(tax,discount,total).execute();
                 }catch(Exception e){

                 }


             }
         });





  }
    void payModeMobileClickListener(){
        mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{

                    ///INITIALIZE
                    TaxValue.setText("" + tax);
                    DiscountValue.setText("" + discount);
                    TotalValue.setText("" + total);


                    //SHow spiiner containing Telcos
                    mobileMoneyTelcos.setVisibility(View.VISIBLE);

                    //HIDE BUTTONS
                    printerbut.setVisibility(View.GONE);

                    cashAmount.setText("");
                    getDialog().setCancelable(false);
                    paymentType="Mobile";
                    PaymentMainScreen(true,"CHECK OUT/MOBILE");
                    new ConfirmSecondScreen(tax,discount,total).execute();
                }catch(Exception e){

                }


            }
        });

    }
    void backButtonClickListener(){

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().setCancelable(true);
                PaymentMainScreen(false);
                new emptySecondScreen().execute();
            }
        });
    }
    void VolleySubmitCashOutMobileMoney(String callbackUrl,
                                        String type,
                                        String provider,
                                        String amount,
                                        String remarks,
                                        String recipient,
                                        String customerMsisdn,
                                        String reference,
                                        String client,
                                        String application,
                                        String country,
                                        String test){





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
                MyApplication.getPAYMENT_URL_MobileURL(),
                new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.hide();
                        try {
                            Log.d("Volley Response ",response.toString());

                            if(response.getString("success").toString().equals("true")){

                                Dismiss();



                            }else if(response.getString("success").toString().equals("false")){
                                controlHelpers.AnAlertertDialog(getActivity(),
                                          response.getString("message"),"").show();
                            }

                        } catch (Exception e) {

                            e.printStackTrace();
                        }

                          Log.d("Ozinbo","Response"+response.toString());

                        //  Message.messageShort(MyApplication.getAppContext(),""+tokenValue+"\n"+response.toString()+"\n"+booleaner);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                String ErrorMessage="Connection Failed !";
                String successMessage="";
                try {
                VolleyError earror = new VolleyError(new String(error.networkResponse.data));


                    JSONObject jobj = new JSONObject(earror.getMessage().toString());

                    ErrorMessage=jobj.getString("message");
                    successMessage=jobj.getString("success");


                    Log.e("errer",earror.getMessage().toString());
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
        int socketTimeout = 999999999;//> more than 4 minutes - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        requestQueue.add(request);


        Log.d("Params ",params.toString());


    }


    void VolleySubmitCashOutMobileMoney1(String callbackUrl,
                                        String type,
                                        String provider,
                                        String amount,
                                        String remarks,
                                        String recipient,
                                        String customerMsisdn,
                                        String reference,
                                        String client,
                                        String application,
                                        String country,
                                        String test){

            Log.d("warning","Volley 2 was called");

        AsyncHttpClient mobileMoneyRedraw = new AsyncHttpClient();


        final ProgressDialog pDialog  = new ProgressDialog(getActivity());
        pDialog.setMessage("Please  wait...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
        pDialog.show();

        RequestParams params = new RequestParams();

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



        mobileMoneyRedraw.post(MyApplication.getPAYMENT_URL_MobileURL(), params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject response) {
                        pDialog.hide();
                        try {
                           // pDialog.hide();
                            //process JSONObject obj
                            Log.w("here", "success status code..." + statusCode+"  "+response.toString());

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          Throwable throwable, JSONObject errorResponse) {
                        pDialog.hide();
                        Log.w("myapp", "failure status code..." + statusCode);


                        try {
                            //process JSONObject obj
                            Log.w("myapp", "httperror ..." + errorResponse.getString("success").toString());
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });

    }
    public interface CommunicatePrinter{


    public void doAprint(String paymentmode,String amtPaid,String amtChange);
    public void checkout(String amtPaid,String amtChange);
}
    class ConfirmSecondScreen extends AsyncTask<Void,Void,Void>{

        public String tax,discount,total;


        public ConfirmSecondScreen(String tax,String discount,String total){
            this.tax=tax;
            this.discount=discount;
            this.total=total;


        }

        @Override
        protected Void doInBackground(Void... params) {

            confirm2bdScreen(tax,discount,total);
            return null;
        }
    }
    class ConfirmSecondScreenMobile extends AsyncTask<Void,Void,Void>{

        public String tax,discount,total,phone;


        public ConfirmSecondScreenMobile(String tax,String discount,String total,String phone){
            this.tax=tax;
            this.discount=discount;
            this.total=total;
            this.phone=phone;


        }

        @Override
        protected Void doInBackground(Void... params) {

            confirm2bdScreenMobile(tax,discount,total,phone);
            return null;
        }
    }
    class ClearSecondScreen extends   AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {

            clear2ndScreen();
            return null;
        }
    }
    private class emptySecondScreen extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {

            SECONDSCREEN secondscreen = new SECONDSCREEN();

            secondscreen.activateDevice();
            if (secondscreen.init()) {
                try {

                    secondscreen.printOnScreen(  "", 0, 0);


                } catch (UnsupportedEncodingException ex) {
                    ex.printStackTrace();
                }
            }
            secondscreen.deactivateDevice();
            secondscreen = null;
            return null;
        }
    }
}
