package com.nfortics.megapos.Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.nfortics.megapos.Adapters.TransHistoryAdapter;
import com.nfortics.megapos.Adapters.TransactionsAdapter;
import com.nfortics.megapos.Application.MyApplication;
import com.nfortics.megapos.DataBase.DataSets;
import com.nfortics.megapos.Dialogs.CashPaymentDialog;
import com.nfortics.megapos.Dialogs.HistoryDialog;
import com.nfortics.megapos.Helper.Message;
import com.nfortics.megapos.Helper.Typefacer;
import com.nfortics.megapos.Helper.Useful;
import com.nfortics.megapos.Models.TransHistory;
import com.nfortics.megapos.Models.Transactions;
import com.nfortics.megapos.R;

import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityHistory extends Fragment {

    RecyclerView histories;

    EditText searchText;

    TransHistoryAdapter
            transHistoryAdapter;

    TransactionsAdapter
            transactionsAdapter;

    DataSets dataSets;

    TextView reference,txtTransType;

    TextView txtSalesValue,
            txtBillPayValue,
            txtAirtimeValue,
            txtCashOutValue,
            txtCashInValue,
            txtMfinanceValue;

    TextView txtSalesLabel,
            txtBillPayLabel,
            txtAirtimeLabel,
            txtCashOutLabel,
            txtCashInLabel,
            txtMfinanceLabel;

Useful useful;
    public List<TransHistory> data= Collections.emptyList();
    public ActivityHistory() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataSets= new DataSets(getActivity());
        useful=new Useful();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_activity_history, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InitiliaLizeView(view);


        histories.setAdapter(transactionsAdapter);
        histories.setLayoutManager(new LinearLayoutManager(getActivity()));
        histories.setLayoutManager(new LinearLayoutManager(getActivity()));



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // llayout.setVisibility(View.GONE);
             InitializeHistoryRecycleView();
                SetValuesToLabelValues();

            }
        }, 1000);

        InitializeRecycleView();
    }

    public void InitiliaLizeView(View view){
        Typefacer typeface= new Typefacer();


         //RecycleViewer
        histories=(RecyclerView)view.findViewById(R.id.listOfActivities);


        //Search History
        searchText=(EditText)view.findViewById(R.id.searchMe);


        //LABELS
                txtSalesLabel=(TextView)view.findViewById(R.id.txtSalesLabel);
                txtSalesLabel.setTypeface(typeface.getRoboCondensedRegular(getActivity().getAssets()));



                txtBillPayLabel=(TextView)view.findViewById(R.id.txtBillpayLabel);
        txtBillPayLabel.setTypeface(typeface.getRoboCondensedRegular(getActivity().getAssets()));
                txtAirtimeLabel=(TextView)view.findViewById(R.id.txtAirTimeLabel);
        txtAirtimeLabel.setTypeface(typeface.getRoboCondensedRegular(getActivity().getAssets()));
                txtCashOutLabel=(TextView)view.findViewById(R.id.txtCashOutLabel);
        txtCashOutLabel.setTypeface(typeface.getRoboCondensedRegular(getActivity().getAssets()));
                txtCashInLabel=(TextView)view.findViewById(R.id.txtCashInLabel);
        txtCashInLabel.setTypeface(typeface.getRoboCondensedRegular(getActivity().getAssets()));
                txtMfinanceLabel=(TextView)view.findViewById(R.id.txtMfinanceLabel);
        txtMfinanceLabel.setTypeface(typeface.getRoboCondensedRegular(getActivity().getAssets()));





        ///VALUES
        txtMfinanceValue=(TextView)view.findViewById(R.id.txtMfinanceValue);
        txtMfinanceValue.setTypeface(typeface.getRoboCondensedRegular(getActivity().getAssets()));
        txtMfinanceValue.setText("Ghs 0.0");

        txtSalesValue=(TextView)view.findViewById(R.id.txtSalesValue);
        txtSalesValue.setTypeface(typeface.getRoboCondensedRegular(getActivity().getAssets()));
        txtSalesValue.setText("Ghs 0.0");


        txtBillPayValue=(TextView)view.findViewById(R.id.txtBillPayValue);
        txtBillPayValue.setTypeface(typeface.getRoboCondensedRegular(getActivity().getAssets()));
        txtBillPayValue.setText("Ghs 0.0");


        txtAirtimeValue=(TextView)view.findViewById(R.id.txtAirtimeValue);
        txtAirtimeValue .setTypeface(typeface.getRoboCondensedRegular(getActivity().getAssets()));
        txtAirtimeValue.setText("Ghs 0.0");

        txtCashOutValue=(TextView)view.findViewById(R.id.txtCashOutValue);
        txtCashOutValue.setTypeface(typeface.getRoboCondensedRegular(getActivity().getAssets()));
        txtCashOutValue.setText("Ghs 0.0");

        txtCashInValue=(TextView)view.findViewById(R.id.txtCashInValue);
        txtCashInValue.setTypeface(typeface.getRoboCondensedRegular(getActivity().getAssets()));
        txtCashInValue.setText("Ghs 0.0");
        SearchTextWatcher();
    }

    void InitializeRecycleView(){
        histories.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                histories, new ClickListener() {
            @Override
            public void onClick(View view, int position) {


                  String value,transType;
                reference=(TextView)view.findViewById(R.id.txtRefenerence);
                txtTransType=(TextView)view.findViewById(R.id.txtTransType);
                transType=txtTransType.getText().toString();
                value=reference.getText().toString();
                String formated=value.substring(5);
                String referenceValue=formated.replaceAll("\\s+","");




                        new getTransaction(transType,referenceValue).execute();
                        Log.d("oxinbo","Trans Type  : "+transType);

                //transactions=dataSets.getAllTransactionsByRefrence(getActivity(),referenceValue);



            //  Message.messageShort(getActivity(),dataSets.getAllTransactionsByRefrence(getActivity(),value.substring(4));



                Log.d(" ozinbo ", "formatted Value ="+ formated+"  raw value ="+value+"spaces rem="+referenceValue);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));



    }

    void SetValuesToLabelValues(){

        try{

            String salevalue=  dataSets.getAllSales(getActivity(),useful.getDateWithFormatter(),"Sales");
            txtSalesValue.setText("Ghs "+salevalue);

            String Airtimvalue=  dataSets.getAllSales(getActivity(),useful.getDateWithFormatter(),"Airtime");
            txtAirtimeValue.setText("Ghs "+Airtimvalue);

            String Billpayvalue=  dataSets.getAllSales(getActivity(),useful.getDateWithFormatter(),"BillPay");

            txtBillPayValue.setText("Ghs "+Billpayvalue);


            String cashOut=  dataSets.getAllSales(getActivity(),useful.getDateWithFormatter(),"Cash Out");

            txtCashOutValue.setText("Ghs "+cashOut);



            String cashIn=  dataSets.getAllSales(getActivity(),useful.getDateWithFormatter(),"Cash In");

            txtCashInValue.setText("Ghs "+cashIn);


            String mfinance=  dataSets.getAllSales(getActivity(),useful.getDateWithFormatter(),"mFinance");

            txtMfinanceValue.setText("Ghs "+mfinance);


        }catch (Exception er){


            er.printStackTrace();

        }



    }



    void SearchTextWatcher(){

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                     new InitializeTransactionsReference(s.toString()).execute();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    public void showPaymentDialog(DialogFragment dialogFragment) {

        FragmentManager manager = getActivity().getSupportFragmentManager();

        DialogFragment dialog = dialogFragment;

        dialog.show(manager, "");
        //Bundle bundle = new Bundle();
        //  bundle.putInt("value", value);
        // bundle.putInt("position", position);
        //dialog.setArguments(bundle);
    }
    void InitializeHistoryRecycleView(){
       // transactionsAdapter=new TransactionsAdapter(getActivity(),dataSets.getAllTransactions(getActivity()));
        //histories.setAdapter(transactionsAdapter);
        //histories.setLayoutManager(new LinearLayoutManager(getActivity()));

        new InitializeTransactions().execute();
    }

    Transactions transactions;


    class getTransaction extends AsyncTask<Void,Void,Void>{


        public String referenceNumber,type;

        public  getTransaction(String type,String referenceNumber){
            this.referenceNumber=referenceNumber;
            this.type=type;
        }
        @Override
        protected Void doInBackground(Void... params) {

            transactions=dataSets.getAllTransactionsByRefrence(getActivity(),referenceNumber);

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            HistoryDialog historyDialog=new HistoryDialog();
            showPaymentDialog(historyDialog.newCategoryInstance(type,"",referenceNumber));

        }
    }

    class InitializeTransactionsReference extends AsyncTask<Void,Void,Void>{
        ProgressDialog pDialog;

        String reference;

        public InitializeTransactionsReference(String reference ){
            this.reference=reference;


        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(Void... params) {
            transactionsAdapter=new TransactionsAdapter(getActivity(),
                    dataSets.getAllTransactionsByReference(getActivity(), reference));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            histories.setAdapter(transactionsAdapter);
            histories.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }
    class InitializeTransactions extends AsyncTask<Void,Void,Void>{
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog  = new ProgressDialog(getActivity());
            pDialog.setMessage("Please  wait...");
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            transactionsAdapter=new TransactionsAdapter(getActivity(),
                    dataSets.getAllTransactionsByDate(getActivity(), useful.getDateWithFormatter()));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pDialog.hide();
            histories.setAdapter(transactionsAdapter);
            histories.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
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

}
