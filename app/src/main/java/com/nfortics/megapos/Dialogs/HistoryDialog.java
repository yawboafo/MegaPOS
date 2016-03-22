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
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.google.gson.JsonObject;
import com.nfortics.megapos.Adapters.SalesItemsBoughtAdapter;
import com.nfortics.megapos.Application.MyApplication;
import com.nfortics.megapos.DataBase.DataSets;
import com.nfortics.megapos.Helper.ControlHelpers;
import com.nfortics.megapos.Helper.CurrencyTextWatcher;
import com.nfortics.megapos.Helper.Message;
import com.nfortics.megapos.Helper.Typefacer;
import com.nfortics.megapos.Helper.Useful;
import com.nfortics.megapos.Models.SalesItemsRow;
import com.nfortics.megapos.Models.TransHistory;
import com.nfortics.megapos.Models.TransHistoryDetails;
import com.nfortics.megapos.Models.Transactions;
import com.nfortics.megapos.Network.ConnectionDetector;
import com.nfortics.megapos.Network.VolleySingleton;
import com.nfortics.megapos.R;
import com.nfortics.megapos.ThirrtySevenSignals.RootAccess;
import com.nfortics.megapos.utils.GsonMan;
import com.nfortics.megapos.R;

import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryDialog extends DialogFragment {

private String type,date,reference;
    DataSets dataSets;
    SalesItemsBoughtAdapter salesItemsBoughtAdapter;
    GsonMan gsonMan;
    Typefacer typefacer;

    RelativeLayout regulasrSales,ListedSales;
    RecyclerView salesItemsRview;

    Button issueRefund,sendReceipt;

    TextView LableTitle,cashLable,referenceLabel,cashValue,receiptValue,txtTitles;

    TextView txt1,txt2,txt3,txt4;
    public HistoryDialog() {
        // Required empty public constructor
    }

    public static HistoryDialog newCategoryInstance(String type,String date,String ref){

        HistoryDialog fragment = new HistoryDialog();
        Bundle args = new Bundle();
        args.putString("Type", type);
        args.putString("date", date);
        args.putString("reference", ref);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getString("Type");
            date = getArguments().getString("date");
            reference= getArguments().getString("reference");

        }

    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if(manager.findFragmentByTag(tag)==null){
        super.show(manager, tag);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        return inflater.inflate(R.layout.fragment_history_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dataSets = new DataSets(getActivity());
        gsonMan=new GsonMan();
        typefacer=new Typefacer();
        InitializeView(view);
        ClickListeners();
           if(type.equals("Sales")){

        new LoadView("Sales","",reference).execute();

        }else if(type.equals("BillPay")){

               new LoadViewUnSales("BillPay","",reference).execute();


              }else if(type.equals("Cash Out")){

               new LoadViewUnSales("Cash Out","",reference).execute();

           }else if(type.equals("Cash In")){

               new LoadViewUnSales("Cash In","",reference).execute();

           }else if(type.equals("Airtime")){

               new LoadViewUnSales("Airtime","",reference).execute();


           }
        Log.d("oxinbo", "Its not sales");

    }

    void InitializeView(View view){



        issueRefund=(Button)view.findViewById(R.id.butRefund);
        issueRefund.setTypeface(typefacer.getRoboRegular(getActivity().getAssets()));

        sendReceipt=(Button)view.findViewById(R.id.butSendR);
        sendReceipt.setTypeface(typefacer.getRoboRegular(getActivity().getAssets()));


        LableTitle=(TextView)view.findViewById(R.id.LableTitle);
        LableTitle.setTypeface(typefacer.getRoboMediumitalic(getActivity().getAssets()));

        txtTitles=(TextView)view.findViewById(R.id.txtTitles);
        txtTitles.setTypeface(typefacer.getRoboRegular(getActivity().getAssets()));
        cashLable=(TextView)view.findViewById(R.id.cashLable);
        cashLable.setText("Total : ");
        cashLable.setTypeface(typefacer.getRoboCondensedRegular(getActivity().getAssets()));

        referenceLabel=(TextView)view.findViewById(R.id.referenceLabel);
        referenceLabel.setText("Receipt.# ");
        referenceLabel.setTypeface(typefacer.getRoboCondensedRegular(getActivity().getAssets()));

        cashValue=(TextView)view.findViewById(R.id.cashValue);
        cashValue.setTypeface(typefacer.getRoboCondensedLghtItalic(getActivity().getAssets()));

        receiptValue=(TextView)view.findViewById(R.id.receiptValue);
        receiptValue.setTypeface(typefacer.getRoboCondensedLghtItalic(getActivity().getAssets()));

        salesItemsRview=(RecyclerView)view.findViewById(R.id.ItemsBought);


        regulasrSales=(RelativeLayout)view.findViewById(R.id.regularSales);
        ListedSales=(RelativeLayout)view.findViewById(R.id.ListedSales);

                txt1=(TextView)view.findViewById(R.id.txt1);
                txt2=(TextView)view.findViewById(R.id.txt2);
                txt3=(TextView)view.findViewById(R.id.txt3);
                txt4=(TextView)view.findViewById(R.id.txt4);

        txt1.setText("");
        txt2.setText("");
        txt3.setText("");
        txt4.setText("");

        txt1.setTypeface(typefacer.getRoboCondensedLghtItalic(getActivity().getAssets()));
        txt2.setTypeface(typefacer.getRoboCondensedLghtItalic(getActivity().getAssets()));
        txt3.setTypeface(typefacer.getRoboCondensedLghtItalic(getActivity().getAssets()));
        txt4.setTypeface(typefacer.getRoboCondensedLghtItalic(getActivity().getAssets()));


    }
     void ClickListeners(){

         issueRefund.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 regulasrSales.setVisibility(View.VISIBLE);
                 ListedSales.setVisibility(View.GONE);
             }
         });
     }

    class LoadViewUnSales extends AsyncTask<Void,Void,Void>{


        BillPay billPay;
  Cashing cashing;
        public String transItems;
        public String type;
        public String date;
        public String reference;
        Transactions transactions;
        public String toTalSales;
        List<SalesItemsRow> salesItemsRows;
        public LoadViewUnSales(String type,String date,String reference){

            this.type=type;
            this.date=date;
            this.reference=reference;

        }


        @Override
        protected Void doInBackground(Void... params) {

          //  = Collections.EMPTY_LIST;



            //GsonArraySalesItems

                    try{
                        transactions=dataSets.getAllTransactionsByRefrence(getActivity(), reference);


                        Log.d("oxinbo","BK"+transactions.getTransItems());


                        transItems =transactions.getTransItems();
                        transItems = transItems.substring(1, transItems.length() - 1);

                        date=transactions.getTransDate()+" - "+ transactions.getTransTime()   ;
                        toTalSales=""+transactions.getTranstotalAmount();
                        type=transactions.getTransType();


                        if(type.equals("BillPay")){

                            JSONObject obj = new JSONObject(transItems);
                            billPay=new BillPay();
                            billPay.Provider=obj.getString("Provider").toString();
                            billPay.Account=obj.getString("account").toString();
                            billPay.Amount=obj.getString("Amount").toString();
                        }else if(type.equals("Airtime")){


                            JSONObject obj = new JSONObject(transItems);
                            billPay=new BillPay();
                            billPay.Provider=obj.getString("Provider").toString();
                            billPay.Account=obj.getString("Phone").toString();
                            billPay.Amount=obj.getString("Amount").toString();

                        }else if(type.equals("Cash Out")){

                            JSONObject obj = new JSONObject(transItems);
                            cashing =new Cashing();
                            cashing.AccNumber=obj.getString("accNumber").toString();
                            cashing.type=obj.getString("type").toString();
                            cashing.Provider=obj.getString("Provider").toString();
                            cashing.Amount=obj.getString("Amount").toString();

                           // Amount,Provider,AccNumber,type;

                        }else if(type.equals("Cash In")){

                            JSONObject obj = new JSONObject(transItems);
                            cashing =new Cashing();
                            cashing.AccNumber=obj.getString("accNumber").toString();
                            cashing.type=obj.getString("type").toString();
                            cashing.Provider=obj.getString("Provider").toString();
                            cashing.Amount=obj.getString("Amount").toString();

                            // Amount,Provider,AccNumber,type;

                        }



                         Log.d("oxinbo","im the Background Type"+type +"\n   trans Items "+transItems);



                     //   salesItemsRows=gsonMan.GsonArraySalesItems(transItems);
                      //  salesItemsBoughtAdapter= new SalesItemsBoughtAdapter(getActivity(),salesItemsRows);
                      // Log.d("oxinbo","sales Item sie "+salesItemsRows.size());




                    }catch (Exception e){
                        e.printStackTrace();

                    }



            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            try {
                LableTitle.setText(type + " " + date);
                receiptValue.setText(reference);
                cashValue.setText("Ghs " + toTalSales);

                ListedSales.setVisibility(View.GONE);
                regulasrSales.setVisibility(View.VISIBLE);





                if(type.equals("BillPay")){


                    txt1.setText("Bill Pay   ");
                    txt2.setText("Provider  " + billPay.Provider);
                    txt3.setText("Reference #." + billPay.Account);
                }else if(type.equals("Airtime")){

                    txt1.setText("Airtime");
                    txt2.setText("Provider  " + billPay.Provider);
                    txt3.setText("Account #."+billPay.Account);
                }else if(type.equals("Cash Out")){

                    txt1.setText(cashing.type);
                    txt2.setText("Provider  " + cashing.Provider);
                    txt3.setText("Account #."+cashing.AccNumber);


                }else if(type.equals("Cash In")){

                txt1.setText(cashing.type);
                txt2.setText("Provider  " + cashing.Provider);
                txt3.setText("Account #."+cashing.AccNumber);


            }






            }catch(Exception e){

                e.printStackTrace();

            }



        }
    }
    class LoadView extends AsyncTask<Void,Void,Void>{


        public String transItems;
        public String type;
        public String date;
        public String reference;
        Transactions transactions;
        public String toTalSales;
        List<SalesItemsRow> salesItemsRows;
        public LoadView(String type,String date,String reference){

            this.type=type;
            this.date=date;
            this.reference=reference;

        }


        @Override
        protected Void doInBackground(Void... params) {

            //  = Collections.EMPTY_LIST;



            //GsonArraySalesItems

            try{
                transactions=dataSets.getAllTransactionsByRefrence(getActivity(), reference);


                Log.d("oxinbo",transactions.getTransItems());


                transItems =transactions.getTransItems();
                transItems = transItems.substring(1, transItems.length() - 1);

                date=transactions.getTransDate()+" - "+ transactions.getTransTime()   ;
                toTalSales=""+transactions.getTranstotalAmount();
                type=transactions.getTransType();

                Log.d("oxinbo","im the Type"+type);



                salesItemsRows=gsonMan.GsonArraySalesItems(transItems);
                salesItemsBoughtAdapter= new SalesItemsBoughtAdapter(getActivity(),salesItemsRows);
                Log.d("oxinbo","sales Item sie "+salesItemsRows.size());




            }catch (Exception e){
                e.printStackTrace();

            }



            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            try {
                LableTitle.setText(type + " " + date);
                receiptValue.setText(reference);
                cashValue.setText("Ghs "+ toTalSales);



                salesItemsRview.setAdapter(salesItemsBoughtAdapter);
                salesItemsRview.setLayoutManager(new LinearLayoutManager(getActivity()));



            }catch(Exception e){

                e.printStackTrace();

            }



        }
    }

    class BillPay{

        //"Amount":"Ghs 45.00","Provider":"DSTV","account":"bbb"
        public  String Amount,Provider,Account;
    }

    class Cashing{

        //Amount":"25","Provider":"MTN","accNumber":"0240088708","type":"cashOutMobileWallet"
        public String Amount,Provider,AccNumber,type;
    }
}
