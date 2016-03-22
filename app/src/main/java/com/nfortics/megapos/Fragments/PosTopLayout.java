package com.nfortics.megapos.Fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.nfortics.megapos.Adapters.ItemsAdapter;
import com.nfortics.megapos.Application.MyApplication;
import com.nfortics.megapos.DataBase.DataSets;
import com.nfortics.megapos.Dialogs.AirTime;
import com.nfortics.megapos.Dialogs.BillPayment;
import com.nfortics.megapos.Dialogs.CashIn;
import com.nfortics.megapos.Dialogs.CashOut;
import com.nfortics.megapos.Dialogs.CashPaymentDialog;
import com.nfortics.megapos.Dialogs.ListOfItems;
import com.nfortics.megapos.Dialogs.MobilePayment_Dialog;
import com.nfortics.megapos.Dialogs.UpdateQuantity;
import com.nfortics.megapos.Helper.Message;
import com.nfortics.megapos.Helper.Typefacer;
import com.nfortics.megapos.Helper.Useful;
import com.nfortics.megapos.Models.Items;
import com.nfortics.megapos.Models.ItemsBought;
import com.nfortics.megapos.Models.OnTransactions;
import com.nfortics.megapos.Models.Transactions;
import com.nfortics.megapos.R;
import com.nfortics.megapos.utils.GsonMan;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.m2i.m835.api.PRINTER;
import org.m2i.m835.api.SECONDSCREEN;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;




/**
 * A simple {@link Fragment} subclass.
 */
public class PosTopLayout extends Fragment {
    private RecyclerView
            recyclerView;

     RelativeLayout itemsSummary;

    DataSets
            dataSets;

    Useful useful
            = new Useful();

    RelativeLayout
            relativeLayout;

    ListView
            foundlist;

    private ItemsAdapter
            itemsAdapter;

    private TextView
            subTotalLabel,
            subTotalValue,
            taxLabel,
            taxValue,
            discountLabel,
            discountValue,
            totalLabel,
            totalValue,
            paywithLabel,Seconscreeen;

    private Button
            butMobilemoneyPay,
            butCreditcr,
            butCashPay,
            butRemoveItem,
            butDiscount,
            upButton,
            butrmv,
            butFilter,
            butAirtime,
            butCashIn,
            butCashOut,
            butBillPayement;

    private static String receiptNumber;

    private String   userID;

    private boolean check=true;
    int counter=0;
    manipulateUIS
            aCommunicator;
    Typefacer typeface;
    Bundle extras;
    String userName;
    String listOfItemsBoughtTypeJson;
    GsonMan gsonMan=new GsonMan();
    Gson gson = new Gson();
    List<ItemsBought> itemsBoughtsList;
    public PosTopLayout() {
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

                try {
                     userID=gsonMan.getMerchantDetails(userName, activity).getId();
                    Log.d("UserID   > > > : ",userID);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            aCommunicator = (manipulateUIS) activity;
        } catch (ClassCastException e) {


        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         typeface = new Typefacer();
        dataSets = new DataSets(getActivity());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View layout = inflater.inflate(R.layout.fragment_pos_top_layout, container, false);


        return layout;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InitializeControls(view);
        InitializeClickListeners(view);
        setTypeFacer();
    }
    public void InitializeControls(View layout){


        recyclerView = (RecyclerView) layout.findViewById(R.id.preparedItems);

        List<Items> datatmp = new ArrayList<>();
        itemsAdapter = new ItemsAdapter(getActivity(), datatmp);
        recyclerView.setAdapter(itemsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //iNITAILIZE LABEL


        subTotalLabel = (TextView) layout.findViewById(R.id.txtSubTotalLabel);
        subTotalLabel.setTypeface(typeface.getRoboCondensedRegular(getActivity().getAssets()));


        subTotalValue = (TextView) layout.findViewById(R.id.txtSubTotalValue);
        subTotalValue.setTypeface(typeface.getRoboBold(getActivity().getAssets()));
        subTotalValue.setText("00.0");
        taxLabel = (TextView) layout.findViewById(R.id.txtTaxLabel);
        taxLabel.setTypeface(typeface.getRoboCondensedRegular(getActivity().getAssets()));


        taxValue = (TextView) layout.findViewById(R.id.txtTaxValue);
        taxValue.setTypeface(typeface.getRoboBold(getActivity().getAssets()));
        taxValue.setText("00.0");
        discountLabel = (TextView) layout.findViewById(R.id.txtDiscountLabel);
        discountLabel.setTypeface(typeface.getRoboCondensedRegular(getActivity().getAssets()));


        discountValue = (TextView) layout.findViewById(R.id.txtDiscountValue);
        discountValue.setTypeface(typeface.getRoboBold(getActivity().getAssets()));
        discountValue.setText("00.0");

        totalLabel = (TextView) layout.findViewById(R.id.txtTotalLabel);
        totalLabel.setTypeface(typeface.getRoboCondensedRegular(getActivity().getAssets()));

        totalValue = (TextView) layout.findViewById(R.id.txtTotalValue);
        totalValue.setTypeface(typeface.getRoboBold(getActivity().getAssets()));
        totalValue.setText("00.0");
        //Initialize buttons

        butrmv = (Button) layout.findViewById(R.id.butRemoveSelected);
     //   butrmv.setEnabled(false);
    // butrmv.setVisibility(View.INVISIBLE);
        butAirtime=(Button) layout.findViewById(R.id.butAirtime);

        butCashIn = (Button) layout.findViewById(R.id.butCashIn);
        butRemoveItem = (Button) layout.findViewById(R.id.butRemoveSelected);
        butCashPay = (Button) layout.findViewById(R.id.butCash);
        butDiscount = (Button) layout.findViewById(R.id.butDiscount);
        butMobilemoneyPay = (Button) layout.findViewById(R.id.butMobile);
        butCashOut= (Button) layout.findViewById(R.id.butCashOut);
        butBillPayement= (Button) layout.findViewById(R.id.butBillPay);


        itemsSummary= (RelativeLayout) layout.findViewById(R.id.itemsSummary);




        Seconscreeen= (TextView) layout.findViewById(R.id.Seconscreeen);
        Seconscreeen.setVisibility(View.GONE);
    }
    public void InitializeClickListeners(View layout){



        butRemoveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                   final int i=itemsAdapter.getselectedIemsIndata();

                    itemsAdapter.removechecked();
                    useful.InitializeSummaryBoard(subTotalValue, taxValue, discountValue, totalValue, itemsAdapter,Seconscreeen);
                   new  ItemRemovedSecondScreen(i+"",itemsAdapter.getSubTotal()+"").execute();
                    if (itemsAdapter.getselectedIemsIndata() <= 0) {

                        butrmv.setText("REMOVE");
                        butrmv.setEnabled(false);

                    } else {

                        butrmv.setText("REMOVE (" + itemsAdapter.getselectedIemsIndata() + ")");

                    }
                } catch (Exception ex) {


                }


            }
        });


        butCashPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CashPaymentDialog dialog = new CashPaymentDialog();
                showPaymentDialog(dialog, useful.formatMoney(0.0),
                        useful.formatMoney(0.0),
                        useful.formatMoney(itemsAdapter.getSubTotal()));


            }
        });
        //


        butDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //ListOfItems dialog=new ListOfItems();
                //  showPaymentDialog(dialog);
            }
        });



        butMobilemoneyPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobilePayment_Dialog dialog = new MobilePayment_Dialog();
                showPaymentDialog(dialog, useful.formatMoney(0.0), useful.formatMoney(0.0), useful.formatMoney(itemsAdapter.getSubTotal()));
                // ListOfItems dialog=new ListOfItems();
                // showPaymentDialog(dialog);

            }
        });
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                recyclerView, new ClickListner() {
            @Override
            public void onClick(View view, final int position) {


                TextView itemname = (TextView) view.findViewById(R.id.itemname);

               // final String itemNAME = itemname.getText().toString();

                Button quantity = (Button) view.findViewById(R.id.itemqty);
                final String quantityText = quantity.getText().toString();

               itemname.setOnClickListener(new View.OnClickListener() {
                    @Override
                  public void onClick(View v) {


                        try {

                            if (itemsAdapter.getData().get(position).name.contains("Item")) {


                            } else {



                                String itemName=itemsAdapter.returnItemNameBasedonPosition(position);
                         Log.d("oxinbo","got string name  >> "+itemName);
                                String tmpDoublePrice4Item;
                                 tmpDoublePrice4Item = dataSets.getItemPrice(itemName);

                              String preparedString=  useful.prepareString4double(tmpDoublePrice4Item);

                                double originalAmt = Double.parseDouble(preparedString);

                                itemsAdapter.decreaseQuantity(position, originalAmt);



                                useful.InitializeSummaryBoard(subTotalValue,
                                        taxValue, discountValue, totalValue, itemsAdapter,Seconscreeen);
                            }

                        } catch (Exception ex) {

                            ex.printStackTrace();
                        }


                  }
                });

                CheckBox tmpch = (CheckBox) view.findViewById(R.id.itemchk);

                tmpch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {

                            itemsAdapter.setItemSelect(itemsAdapter.getData().get(position), position);
                            if (itemsAdapter.getselectedIemsIndata() <= 0) {

                                butrmv.setText("REMOVE");
                                butrmv.setEnabled(false);

                            } else {
                                butrmv.setEnabled(true);
                                butrmv.setText("REMOVE (" + itemsAdapter.getselectedIemsIndata() + ")");

                            }


                        } catch (Exception ex) {


                        }


                    }
                });


            }

            @Override
            public void onLongClick(View view, int position) {


                String itemName = itemsAdapter.getData().get(position).name;
                String tmpvalue = itemsAdapter.getData().get(position).quatity;
                if (itemName.contains("Item") ||
                        itemName.contains("CashIn") ||
                        itemName.contains("CashOut") ||
                        itemName.contains("BillPaid") ||
                        itemName.contains("Airtime")) {

                    Message.messageShort(getActivity(),"Cannot Update Quantity");
                } else {
                    UpdateQuantity updateQuantity = new UpdateQuantity();
                    showPaymentDialog(updateQuantity, Integer.parseInt(tmpvalue), position);
                }


            }
        }));



        butAirtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AirTime dialog = new AirTime();
                showPaymentDialog(dialog);
            }
        });
        butCashIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CashIn dialog = new CashIn();
                //dialog.newInstance("","");
                showADialog(dialog.newInstance("", ""));
            }
        });
        butCashOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CashOut dialog = new CashOut();
                //dialog.newInstance("","");
                showADialog(dialog);
            }
        });

        butBillPayement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BillPayment dialog = new BillPayment();
                //dialog.newInstance("","");
                showADialog(dialog);
            }
        });

    }
    public void HideSummaryBoardAndItemsB(boolean val){





        if(val){
            Seconscreeen.setVisibility(View.VISIBLE);
            itemsSummary.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        }else{
            Seconscreeen.setVisibility(View.GONE);
            itemsSummary.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }


      //;
    }
    public void increaseItemQuantity(int qty, int position) {

        itemsAdapter.IncreaseQuantity(qty, position);
    }
    public void updateListView(String pname, String pprice) {

        Items itm = new Items();
        itm.name = pname;
        itm.amount = pprice;
        itm.select = false;
        itm.quatity = "1";
        itemsAdapter.addItem(itm);

    }
    public void InitiaLizeSummaryBoard() {
        useful.InitializeSummaryBoard(subTotalValue, taxValue, discountValue, totalValue, itemsAdapter, Seconscreeen);
    }
    public void setTypeFacer(){


        Seconscreeen.setTypeface(typeface.getRoboRealThin(getActivity().getAssets()));
        butAirtime.setTypeface(typeface.getRoboCondensedRegular(getActivity().getAssets()));
        butCashIn.setTypeface(typeface.getRoboCondensedRegular(getActivity().getAssets()));
        butRemoveItem .setTypeface(typeface.getRoboCondensedRegular(getActivity().getAssets()));
        butCashPay.setTypeface(typeface.getRoboCondensedRegular(getActivity().getAssets()));
        butDiscount.setTypeface(typeface.getRoboCondensedRegular(getActivity().getAssets()));
        butMobilemoneyPay.setTypeface(typeface.getRoboCondensedRegular(getActivity().getAssets()));
        butCashOut.setTypeface(typeface.getRoboCondensedRegular(getActivity().getAssets()));
        butBillPayement.setTypeface(typeface.getRoboCondensedRegular(getActivity().getAssets()));

    }
    public void showPaymentDialog(DialogFragment dialogFragment, String tax, String discount, String total) {

        FragmentManager manager = getActivity().getSupportFragmentManager();
        // CashPaymentDialog dialog= new CashPaymentDialog();
        DialogFragment dialog = dialogFragment;
        // dialog.difficultvalue="3000";
        dialog.show(manager, "");
        Bundle bundle = new Bundle();
        bundle.putString("Tax", tax);
        bundle.putString("Discount", discount);
        bundle.putString("Total", total);
        dialog.setArguments(bundle);

    }
    public void showADialog(DialogFragment dialogFragment){

        FragmentManager manager = getActivity().getSupportFragmentManager();
        // CashPaymentDialog dialog= new CashPaymentDialog();
        DialogFragment dialog = dialogFragment;
        dialog.show(manager, "");

    }
    public void showPaymentDialog(DialogFragment dialogFragment, int value, int position) {

        FragmentManager manager = getActivity().getSupportFragmentManager();

        DialogFragment dialog = dialogFragment;

        dialog.show(manager, "");
        Bundle bundle = new Bundle();
        bundle.putInt("value", value);
        bundle.putInt("position", position);
        dialog.setArguments(bundle);

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
    public void executePrintOnBackground(String paymentype,String amountPaid,String amtChange){
        new MyTask(paymentype,amountPaid,amtChange).execute();
         //Log.d("Json List > <<<",listOfItemsBoughtTypeJson);
    }
    public void executeGenericPrintOnBackground(String name,String provider,String phone,String Amount){
        new MyPrintTask(name,provider,phone,Amount).execute();
        Log.d("Messsage", "executePrintOnBackGroundWasCalled");
    }
    public void executeGenericPrintOnBackgroundBP(String name,String provider,String billreference,String Amount){
        new BillPaymentTask(name,provider,billreference,Amount).execute();
        Log.d("Messsage", "executePrintOnBackGroundWasCalled");
    }
    public void resetPOSScreens(){
        check=false;
        useful.ClearSummaryBoard(subTotalValue, taxValue, discountValue, totalValue,Seconscreeen);
        itemsAdapter.data.clear();
        itemsAdapter.notifyDataSetChanged();
        itemsAdapter.setCounter(2);
    }
    public String getItemsBought(){
        StringBuilder sb = new StringBuilder();
        String realprice,itemsName;
        itemsBoughtsList=new ArrayList<>();
        try{

            for (Items items : itemsAdapter.data) {
                ItemsBought itemsBought=new ItemsBought();
                //minVal = (a < b) ? a : b;
                realprice=(items.name.contains("Item")?  useful.cleanAstring(items.amount, 3) :useful.cleanAstring(dataSets.getItemPrice(items.name),3));
                if(items.name.length() > 8){

                    itemsName=   items.name.substring(0,6);
                }else{

                    itemsName=items.name;
                }
                //sb.append(itemsName=items.name + "\t" +"("+items.quatity+")" +  realprice   +"\t"+ useful.cleanAstring(items.amount,3));
                sb.append(itemsName+ "\t ("+items.quatity+")" +  realprice +"\t"+ useful.cleanAstring(items.amount,3));
                sb.append("\n");
                // sb.append(s);
                //sb.append("\t");
                itemsBought.setItemName(items.name);
                itemsBought.setQuantity(items.quatity);
                itemsBought.setRealPrice(realprice);
                itemsBought.setAmount(items.amount);
                itemsBoughtsList.add(itemsBought);


                Log.d("oxinbo", "real price " + realprice);

               // listOfItemsBoughtTypeJson =gson.toJson(itemsBoughtsList);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }

        return sb.toString();
    }
    public String getTotalValue(){

        return  totalValue.getText().toString();

    }
    public String getTaxValue(){

        return taxValue.getText().toString();

    }
    public String getDiscountValue(){

        return discountValue.getText().toString();

    }
    public void doPrint2(String amountPaid,
                        String amtChange,
                        String totalValue,
                        String taxValue,
                        String discountValue,
                        String getItemsB) {
        PRINTER printer = new PRINTER();

        // initialize/activate Device
        printer.activateDevice();

        // Ready the printer
        printer.init();




        try {

            printer.setFontMedium();
            printer.print("\t\t\tAmazing Grace" );
            printer.print("Tel :" + "0208173179");
            printer.setFontSmall();
            printer.print("R# " + "009871");
            printer.setFontMedium();
            printer.print("Item" + "\t\t\t" + "Quantity" + "\t" + "Price");
            printer.setFontSmall();
            printer.print(getItemsB);
            printer.printBlankLine();
            printer.print("TAX : " + "\t" + taxValue);
            printer.print("DISCOUNT : " +"\t" + discountValue);
            printer.print("TOTAL :  "+"\t"+totalValue);
            printer.print("PaymentType : "+"\t"+"Cash");
            printer.print("AmountPaid : "+"\t"+amountPaid);
            printer.print("Change : " + "\t"+amtChange);
            printer.printBlankLine();
            printer.setFontLarge();
            printer.print("\t\t\t\t\t\t\t\t\t" + "Powered By Ozinbo");
            printer.endPrint();




        } catch (Exception ex) {
           // System.out.println(ex.getMessage());
        }

        // Deactivate Device
        printer.deactivateDevice();
        printer = null;



}


    public void doPrint(String paymentype,
                        String amountPaid,
                        String amtChange,
                        String totalValue,
                        String taxValue,
                        String discountValue,
                        String getItemsB) {


        try{
            receiptNumber=useful.generateReceipt(MyApplication.getShopname(),MyApplication.getShopBranch(),userID);

        }catch (Exception e){


        }


        PRINTER printer=null;
try{


     printer = new PRINTER();

    // initialize/activate Device
    printer.activateDevice();

    // Ready the printer
    printer.init();
}catch (Exception e){


}





        try {

            printer.setFontMedium();
            printer.print(MyApplication.getShopname() + "-" + MyApplication.getShopBranch());
            printer.print("Tel :" + MyApplication.getShopPhoneNumber());
            printer.setFontSmall();

            printer.print("Receipt.# " + receiptNumber);
            printer.print("Date : " + useful.getDateWithFormatter());
            printer.print("Time : " + useful.getTimeWithFormatter());
            printer.setFontMedium();
            printer.print("Item" + "\t\t\t" + "Qty" + "\t" + "Price" + "\t" + "Total");
            printer.setFontSmall();
            printer.print(getItemsB);
            printer.printBlankLine();

            if(taxValue.equals("Ghs 0.00")){

            }else{
                printer.print("TAX : " + "\t" + taxValue);
            }


            if(discountValue.equals("Ghs 0.00")){

            }else{
                printer.print("DISCOUNT : " +"\t" + discountValue);
            }


            if(paymentype.equals("Mobile")){
                printer.print("PaymentType : "+paymentype);
                printer.print("TOTAL :  "+totalValue);
                printer.print("AmountPaid : " + " Full");
            }else{


                printer.print("PaymentType : "+paymentype);
                printer.print("TOTAL :  "+totalValue);
                printer.print("AmountPaid : " + amountPaid);
                if(amtChange.equals("Ghs 0.00")){

                }else{
                    printer.print("Change : " + "\t"+amtChange);
                }

            }


            printer.printBlankLine();
            printer.setFontLarge();
            printer.print("\t\t\t\t\t\t\t\t\t" + "Powered By Ozinbo");
            printer.endPrint();






        } catch (Exception ex) {
          Log.d("oxinbo ","Excp Pri"+ex.toString());
        }

        // Deactivate Device
        printer.deactivateDevice();
        printer = null;

        try {
            String getItemsBought = gson.toJson(itemsBoughtsList);
            Transactions transactions = SetTransactions(
                    receiptNumber,
                    Double.parseDouble(useful.prepareString4double(totalValue)),
                    getItemsBought,
                    useful.getDateWithFormatter(),
                    useful.getTimeWithFormatter(),
                    "Sales",
                    "0",
                    "Offline",
                    "new",
                    useful.prepareString4double(discountValue),
                    useful.prepareString4double(amountPaid),
                    useful.prepareString4double(taxValue),
                    useful.prepareString4double(amtChange)
            );



            Log.d(">>> Items Bougth", "  " + getItemsBought);

            long lil = dataSets.insertTransaction(transactions);

            if (lil < 1) {

                Log.d(">>>Inserted Transaction", "" + lil);
            } else {

                Log.d(">>>Inserted Transfailed", "" + lil);
            }
        }catch(Exception e){

            e.printStackTrace();
        }


    }
    public void InPrint(String name,String provider,String phone,String Amount){
        PRINTER printer = new PRINTER();

        // initialize/activate Device
        printer.activateDevice();

        // Ready the printer
        printer.init();



        try{
            receiptNumber=useful.generateReceipt(MyApplication.getShopname(),MyApplication.getShopBranch(),userID);
            printer.setFontMedium();
            printer.print(MyApplication.getShopname() + "-" + MyApplication.getShopBranch());
            printer.print("Tel :" + MyApplication.getShopPhoneNumber());
            printer.setFontSmall();
            printer.print("Date :" + useful.getDateWithFormatter() + "\n" + "Time : " + useful.getTimeWithFormatter());
            printer.print("Receipt.# :" + receiptNumber);
            printer.setFontMedium();
            printer.print(name);
            printer.setFontSmall();
            printer.print("Provider :" + provider);
            printer.print("Phone :" + phone);
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
            //AIRTIMR ITEMS BOUGHT TO JSON
            AirtimeItems airtimeItems=new AirtimeItems();
            airtimeItems.Amount=Amount;
            airtimeItems.Phone=phone;
            airtimeItems.Provider=provider;
            ////////

            String getItemsBought =gson.toJson(airtimeItems);
            Transactions transactions=SetTransactions(
                    receiptNumber,
                    Double.parseDouble(useful.prepareString4double(Amount)),
                    getItemsBought,
                    useful.getDateWithFormatter(),
                    useful.getTimeWithFormatter(),
                    "Airtime",
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
    public void InPrintBP(String name,String provider,String Billreference,String Amount){
        PRINTER printer = new PRINTER();

        // initialize/activate Device
        printer.activateDevice();

        // Ready the printer
        printer.init();



        try{
            receiptNumber=useful.generateReceipt(MyApplication.getShopname(),MyApplication.getShopBranch(),userID);
            printer.setFontMedium();
            printer.print(MyApplication.getShopname() +
                    "-" + MyApplication.getShopBranch());
            printer.print("Tel :" + MyApplication.getShopPhoneNumber());
            printer.setFontSmall();
            printer.print("Date :" + useful.getDateWithFormatter() + "\n" + "Time : " + useful.getTimeWithFormatter());
            printer.print("Receipt.# :" + receiptNumber);
            printer.setFontMedium();
            printer.print(name);
            printer.setFontSmall();
            printer.print("Provider :" + provider);
            printer.print("Bill Ref :" + Billreference);
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
            //AIRTIMR ITEMS BOUGHT TO JSON
            BillPaymentsItems biipay=new BillPaymentsItems();
            biipay.Amount=Amount;
            biipay.account=Billreference;
            biipay.Provider=provider;
            ////////

            String getItemsBought =gson.toJson(biipay);
            Transactions transactions=SetTransactions(
                    receiptNumber,
                    Double.parseDouble(useful.prepareString4double(Amount)),
                    getItemsBought,
                    useful.getDateWithFormatter(),
                    useful.getTimeWithFormatter(),
                    "BillPay",
                    "0",
                    "Offline",
                    "new",
                    "Ghs 0.0",
                    "Ghs 0.0",
                    "Ghs 0.0",
                    "Ghs 0.0"
            );
            Log.d(">BillPay Items", "  " + getItemsBought);
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

    public void SecondScreen(String item,String price){


      new CheckOutSecondScreen( item, price).execute();



    }
    void RealItems2ndScreen(String item,
                            String qty,
                            String price,
                            String total){

        SECONDSCREEN secondscreen = new SECONDSCREEN();
        secondscreen.activateDevice();
        if (secondscreen.init()) {
            try {



                secondscreen.printOnScreen("Item    Qty    Price", 0, 0);
                secondscreen.printOnScreen("****************************", 0, 16);
                secondscreen.printOnScreen(item+ "  ("+ qty +") x"+price, 0, 32);
                secondscreen.printOnScreen("--------------------------", 0, 48);
                secondscreen.printOnScreen( "Total "+total, 0, 64);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        secondscreen.deactivateDevice();
        secondscreen = null;



    }
    void ItemRemoved2ndScreen( String size,String total){

        final  String ITEMS_REMOVED_STATUS= (Integer.parseInt(size)<=1)? " Item removed":" Items removed";

        SECONDSCREEN secondscreen = new SECONDSCREEN();
        secondscreen.activateDevice();
        if (secondscreen.init()) {
            try {

                secondscreen.printOnScreen("Item Removed", 0, 0);
                secondscreen.printOnScreen("****************************", 0,16);
                secondscreen.printOnScreen( size+ ITEMS_REMOVED_STATUS, 0,32);
                secondscreen.printOnScreen("--------------------------", 0,48);
                secondscreen.printOnScreen( "Current Total :" + total, 0,64);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        secondscreen.deactivateDevice();
        secondscreen = null;


    }
    public static interface ClickListner {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
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
    public static interface manipulateUIS {

        public void setFilter();
    }
    class MyTask extends AsyncTask<String,String,String>{

        public String paymentype;
        public String amtPaid;
        public String amtChange;
        public String TotalValue;
        public String TaxValue;
        public String DiscountValue;
        public String ItemsBought;
     public MyTask(String paymentype,String amtpaid,String amtChange){


         this.paymentype=paymentype;
         this.amtPaid=amtpaid;
         this.amtChange=amtChange;
         this.TotalValue=getTotalValue();
         this.TaxValue=getTaxValue();
         this.DiscountValue=getDiscountValue();
         this.ItemsBought=getItemsBought();
   }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try{
                resetPOSScreens();
            }catch(Exception ex){


            }

           //
        }
//(String amountPaid,String amtChange,String totalValue,String taxValue,String discountValue)
        @Override
        protected String doInBackground(String... params) {
            doPrint(paymentype,amtPaid,amtChange,TotalValue,TaxValue,DiscountValue,ItemsBought);
            return null;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);


        }
    }
    class MyPrintTask extends  AsyncTask<Void,Void,Void>{
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
    class BillPaymentTask extends AsyncTask<Void,Void,Void>{
        public String name, provider, reference, Amount;
        public BillPaymentTask(String name,String provider,String reference,String Amount){

            this.name=name;
            this.provider=provider;
            this.reference=reference;
            this.Amount=Amount;
        }

        @Override
        protected Void doInBackground(Void... params) {

            InPrintBP(name, provider,reference,Amount);
            return null;
        }
    }
    class CheckOutSecondScreen extends  AsyncTask<Void,Void,Void>{

        public  String item;
        public  String price;
        public  String TotalValue;
        public String qty;

        public CheckOutSecondScreen(String item,String price){
            this.item=item;
            this.price=price;

            this.qty=(item.contains("Item")?"1" :itemsAdapter.getQty(item));
            //this.qty=itemsAdapter.getQty(item);
            this.TotalValue=useful.formatMoney(itemsAdapter.getSubTotal());
        }

        @Override
        protected Void doInBackground(Void... params) {

            RealItems2ndScreen(item, qty, useful.cleanAstring(price, 3), TotalValue);
            return null;
        }
    }
    class ItemRemovedSecondScreen extends  AsyncTask<Void,Void,Void>{

        public  String size;
        public  String TotalValue;


        public ItemRemovedSecondScreen(String size,String price){
            this.size=size;
            this.TotalValue=price;
        }

        @Override
        protected Void doInBackground(Void... params) {

            ItemRemoved2ndScreen(size,TotalValue);
            return null;
        }
    }
}
   class AirtimeItems{
    String Provider;
    String Phone;
    String Amount;
}
   class BillPaymentsItems{
    String Provider;
    String account;
    String Amount;

}