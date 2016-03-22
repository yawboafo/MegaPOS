package com.nfortics.megapos.Activities;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nfortics.megapos.Adapters.ViewPagerAdapter;
import com.nfortics.megapos.DataBase.DataBaseSchema;
import com.nfortics.megapos.DataBase.DataSets;
import com.nfortics.megapos.Dialogs.AirTime;
import com.nfortics.megapos.Dialogs.BillPayment;
import com.nfortics.megapos.Dialogs.CashIn;
import com.nfortics.megapos.Dialogs.CashOut;
import com.nfortics.megapos.Dialogs.CashPaymentDialog;
import com.nfortics.megapos.Dialogs.CreateItemsDialog;
import com.nfortics.megapos.Dialogs.ListOfItems;
import com.nfortics.megapos.Dialogs.UpdateQuantity;
import com.nfortics.megapos.Domain.LoginModel;
import com.nfortics.megapos.Fragments.ActivityHistory;
import com.nfortics.megapos.Fragments.BillPayFragment;
import com.nfortics.megapos.Fragments.InnerFragments.MiddleFragment;
import com.nfortics.megapos.Fragments.InnerFragments.StartFragment;
import com.nfortics.megapos.Fragments.MfinanceFragment;
import com.nfortics.megapos.Fragments.MfinanceMenu;
import com.nfortics.megapos.Fragments.PosBottomLayout;
import com.nfortics.megapos.Fragments.NavigationDrawerFragment;
import com.nfortics.megapos.Fragments.PosTopLayout;
import com.nfortics.megapos.Fragments.Stocking;
import com.nfortics.megapos.Helper.Message;
import com.nfortics.megapos.Models.TransHistory;
import com.nfortics.megapos.Models.TransHistoryDetails;
import com.nfortics.megapos.R;
import com.nfortics.megapos.utils.GsonMan;

import org.m2i.m835.api.CURRENCYDETECTOR;
import org.m2i.m835.api.SECONDSCREEN;

import java.io.UnsupportedEncodingException;


public class Home extends ActionBarActivity implements
        NavigationDrawerFragment.getNavigationDrawerClicks,
        MiddleFragment.Communicator,
        StartFragment.InsertItemsToListView,
        PosTopLayout.manipulateUIS,
        ListOfItems.MessageSender,
        UpdateQuantity.Communicate,
        MfinanceFragment.InilializeMenus,
        MfinanceMenu.CallAViewFragment,
        CashIn.OnFragmentInteractionListener,
        CashPaymentDialog.CommunicatePrinter,
        CashOut.OnFragmentInteractionListener,
        AirTime.OnbuttonClickListener,
        BillPayment.OnbuttonClickListener,
        Stocking.CommunicateWithMainFragment,
        CreateItemsDialog.OnFragmentInteractionListener,
        BillPayFragment.OnFragmentInteractionListener{



    private NavigationDrawerFragment drawerFragment;
    private Toolbar toolbar;
    DataBaseSchema dataBaseSchema;
    FrameLayout TopFrameLayout;
    ViewPager viewPager;
    FragmentManager fragmentManager;
    int tokenSetView;
    Bundle extras;
    DataSets dataSets;
    String userName;
    GsonMan gsonMan;
    EditText searchEdit;
    RelativeLayout Downlayout;
    Boolean currencyOn = false;
    //Intent intent;


     //*********HOME ACTIVITY******************************///


    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        extras = getIntent().getExtras();
        dataSets=new DataSets(getApplicationContext());
        gsonMan=new GsonMan();
        if(extras == null) {
            tokenSetView= 0;
        }else {

            tokenSetView= extras.getInt("DetectView");
            userName= extras.getString("UserDetails");

        }



        try{

            TopFrameLayout=(FrameLayout)findViewById(R.id.TopLayout);
            Downlayout=(RelativeLayout)findViewById(R.id.DownLayout);
            dataBaseSchema =new DataBaseSchema(this);
          //  TopFrameLayout.getLayoutParams().height = 100;
           // Downlayout.getLayoutParams().height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
            InitializeToolBar();
            InitializeNavigationDrawer();



            if(tokenSetView==1){
                InitializeViewMFINANCEview();
            }else if(tokenSetView==0){
                InitializeViewPOSview();

            }



        }catch(Exception ex){



        }

    }
    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {



        return super.onCreateView(parent, name, context, attrs);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    boolean gridview=true;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
           // CurrencyDetectOff();
            return true;

        }else if(id==R.id.action_currency){

            if(currencyOn){
                cashInCurrencyDetectOff();
               // currencyOn=;
            }else{
                cashInCurrencyDetectOn();
                //currencyOn=true;

            }


            return true;
        }else if(id==R.id.action_gridview){






            if(gridview){


                //TopFrameLayout.getLayoutParams().height= 200;
                TopFrameLayout.getLayoutParams().height = 100;
                Downlayout.getLayoutParams().height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
                PosTopHideThings(true);
                gridview = false;
            }else {
                PosTopHideThings(false);

                //TopFrameLayout.getLayoutParams().height= 200;
                TopFrameLayout.getLayoutParams().height =  ViewGroup.LayoutParams.WRAP_CONTENT;
                Downlayout.getLayoutParams().height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
                gridview = true;
            }









        }

        return super.onOptionsItemSelected(item);
    }



    public void setGridV(){
        if(gridview){


            //TopFrameLayout.getLayoutParams().height= 200;
            TopFrameLayout.getLayoutParams().height = 150;
            Downlayout.getLayoutParams().height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
            PosTopHideThings(true);
            gridview = false;
        }else {
            PosTopHideThings(false);

            //TopFrameLayout.getLayoutParams().height= 200;
            TopFrameLayout.getLayoutParams().height =  ViewGroup.LayoutParams.WRAP_CONTENT;
            Downlayout.getLayoutParams().height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
            gridview = true;
        }


    }
    ///**********************************************************//


    //////********************OVER RIDE METHODS*******////////
    @Override
    public void populateListView() {

        PosTopLayout posTopLayout=(PosTopLayout)getSupportFragmentManager().findFragmentByTag("postoplayer");
       // posTopLayout.updateListView();

    }
    @Override
    public void addToPreparedItems(String name, String price) {


        PosTopLayout posTopLayout=(PosTopLayout)getSupportFragmentManager().findFragmentByTag("postoplayer");
        posTopLayout.updateListView(name,price);
        posTopLayout.SecondScreen(name,price);
        posTopLayout.InitiaLizeSummaryBoard();





    }

    @Override
    public void changeView() {
        setGridV();
    }

    @Override
    public void setFilter() {

    }
    @Override
    public void sendMessage() {





       // StartFragment startFragment=(StartFragment)getSupportFragmentManager().findFragmentById(R.id.startFragment);
      FragmentManager manager=getSupportFragmentManager();

        StartFragment startFragment  = (StartFragment) manager.findFragmentById(R.id.startFragment);
       // startFragment.hideItemSort();
       // posTopLayout.updateListView(name,price);
       // posTopLayout.InitiaLizeSummaryBoard();
    }
    @Override
    public void updateQuantity(int quantity,int position) {
        PosTopLayout posTopLayout=(PosTopLayout)getSupportFragmentManager().findFragmentByTag("postoplayer");
        posTopLayout.increaseItemQuantity(quantity, position);
        posTopLayout.InitiaLizeSummaryBoard();
    }
    @Override
    public void doAprint(String paymentType,String amtPaid,String amtChange) {
        PosTopLayout posTopLayout=(PosTopLayout)getSupportFragmentManager().findFragmentByTag("postoplayer");

        posTopLayout.executePrintOnBackground(paymentType, amtPaid, amtChange);
        Log.d("Messsage", "doPrint Was Called ");
    }
    @Override
    public void checkout(String amtPaid,String amtChange) {
        PosTopLayout posTopLayout=(PosTopLayout)getSupportFragmentManager().findFragmentByTag("postoplayer");

       posTopLayout.resetPOSScreens();
    }
    @Override
    public void navigate(int position) {

        try{
            switch (position){

                case  0:
                    //getSupportActionBar().setTitle("Ozinbo-POS");
                    InitializeViewPOSview();
                    break;
                case 1:
                   //  getSupportActionBar().setTitle("MFINANCE");

                  //  Intent intent = new Intent(getBaseContext(), mFinance.class);
                  //  intent.putExtra("DetectView", 1);
                  //  startActivity(intent);
                    //startActivity(new Intent(this, mFinance.class));

                    if(viewPager.getVisibility()==View.VISIBLE){

                        viewPager.setVisibility(View.GONE);

                    }

                 //   InitializeViewMFINANCEview();

                    break;
                case 2:
                    InitializeBillPaymentView();
                    break;

            }

        }catch(Exception ex){


        }


    }
    @Override
    public void navigate1(int position){
        try{
            switch (position){

                case  0:
                    if(viewPager.getVisibility()==View.VISIBLE){

                        viewPager.setVisibility(View.GONE);

                    }
                    InitializeDailyActivities();
                    break;
                case 1:


                    break;




                case 4:
                    InitializeStockinView();
                    break;
            }

        }catch(Exception ex){


        }


    }


    /////INITILAIILIZE VIEWS/////////////////
    public void InitializeDailyActivities(){
        getSupportActionBar().setTitle("All Activity");
        TopFrameLayout.getLayoutParams().height= android.view.ViewGroup.LayoutParams.MATCH_PARENT;
        FragmentManager fragmentManager1;
        FragmentTransaction fragmentTransaction1;
        fragmentManager1 = getSupportFragmentManager();

        Fragment fragment1 = new ActivityHistory();

        fragmentTransaction1 = fragmentManager1.beginTransaction();

        fragmentTransaction1.replace(R.id.TopLayout, fragment1, "AllActivist");

        fragmentTransaction1.commit();

    }
    public void InitializeViewPOSview(){
        getSupportActionBar().setTitle("OzinbO");
        TopFrameLayout.getLayoutParams().height =  ViewGroup.LayoutParams.WRAP_CONTENT;
        FragmentManager fragmentManager1,fragmentManager2;
        FragmentTransaction fragmentTransaction1,fragmentTransaction2;

        fragmentManager1 = getSupportFragmentManager();
        fragmentManager2 = getSupportFragmentManager();

        Fragment fragment = new PosTopLayout();
        fragmentTransaction1 = fragmentManager1.beginTransaction();
        fragmentTransaction1.replace(R.id.TopLayout, fragment,"postoplayer");
        fragmentTransaction1.commit();

        InitializeViewPager();

    }
    public void InitializeViewMFINANCEview(){
       getSupportActionBar().setTitle("Mfinance");
        TopFrameLayout.getLayoutParams().height= android.view.ViewGroup.LayoutParams.MATCH_PARENT;
        FragmentManager fragmentManager1;
        FragmentTransaction fragmentTransaction1;
        fragmentManager1 = getSupportFragmentManager();

        Fragment fragment1 = new MfinanceFragment();

        fragmentTransaction1 = fragmentManager1.beginTransaction();

        fragmentTransaction1.replace(R.id.TopLayout, fragment1, "Mfinace");

        fragmentTransaction1.commit();

    }
public void InitializeBillPaymentView(){

    getSupportActionBar().setTitle("Bill Pay");
    TopFrameLayout.getLayoutParams().height= android.view.ViewGroup.LayoutParams.MATCH_PARENT;
    FragmentManager fragmentManager1;
    FragmentTransaction fragmentTransaction1;
    fragmentManager1 = getSupportFragmentManager();

    Fragment fragment1 = new BillPayFragment();

    fragmentTransaction1 = fragmentManager1.beginTransaction();

    fragmentTransaction1.replace(R.id.TopLayout, fragment1, "Billpay");

    fragmentTransaction1.commit();

}

    void PosTopHideThings(boolean val){
        PosTopLayout posTopLayout=(PosTopLayout)getSupportFragmentManager().findFragmentByTag("postoplayer");
        posTopLayout.HideSummaryBoardAndItemsB(val);

    }
    public void InitializeToolBar(){
        toolbar=(Toolbar)findViewById(R.id.app_bar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
       // ((ActionBarActivity) this).setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setTitle("Ozinbo-POS");
    }
    public void InitializeNavigationDrawer(){


        drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        //drawerFragment.setDrawerListener(this);

    }
    public void InitializeViewPager(){
        viewPager=(ViewPager)findViewById(R.id.bottomViewpager);
        if(viewPager.getVisibility()==View.GONE){

            viewPager.setVisibility(View.VISIBLE);
        }


    fragmentManager = this.getSupportFragmentManager();

    viewPager.setAdapter(new ViewPagerAdapter(fragmentManager));
}
    public void InitializeMfinanceMenus(String bkname){

        FragmentManager mfinanceMenuManaager;
        FragmentTransaction fragmentTransaction1;

        mfinanceMenuManaager = getSupportFragmentManager();
/***/
        Bundle bundle = new Bundle();
        bundle.putString("BankName", bkname);

        Fragment fragment = new MfinanceMenu();

        fragment.setArguments(bundle);
        fragmentTransaction1 = mfinanceMenuManaager.beginTransaction();
        fragmentTransaction1.replace(R.id.TopLayout, fragment, "postoplayer");
        fragmentTransaction1.commit();

    }
    public void InitializeStockinView(){
        getSupportActionBar().setTitle("Stock/All Items");
        TopFrameLayout.getLayoutParams().height= android.view.ViewGroup.LayoutParams.MATCH_PARENT;
        FragmentManager fragmentManager1;
        FragmentTransaction fragmentTransaction1;
        fragmentManager1 = getSupportFragmentManager();

        Fragment fragment1 = new Stocking();

        fragmentTransaction1 = fragmentManager1.beginTransaction();

        fragmentTransaction1.replace(R.id.TopLayout, fragment1, "stock");

        fragmentTransaction1.commit();








    }
    ///////***********************************?////////


    @Override
    public void CallMenus(String val) {
        InitializeMfinanceMenus(val);
    }
    @Override
    public void callfrag() {
        InitializeViewMFINANCEview();
    }
    @Override
    public void onFragmentInteraction(String name, String price) {
        PosTopLayout posTopLayout=(PosTopLayout)getSupportFragmentManager().findFragmentByTag("postoplayer");
        posTopLayout.updateListView(name, price);
        posTopLayout.InitiaLizeSummaryBoard();

       // posTopLayout.executePrintOnBackground(name,price);
    }


    /////////////////////////CASH IN///////////////////////
    @Override
    public void cashOutPrint(String type, String item) {
        PosTopLayout posTopLayout=(PosTopLayout)getSupportFragmentManager().findFragmentByTag("postoplayer");

       // posTopLayout.executeGenericPrintOnBackground(type, item);
    }
    @Override
    public void cashInPrint(String name, String provider,String acc,String amount) {
        PosTopLayout posTopLayout=(PosTopLayout)getSupportFragmentManager().findFragmentByTag("postoplayer");
         posTopLayout.executeGenericPrintOnBackground(name, provider,acc,amount);
       // posTopLayout.executeGenericPrintOnBackground(item, things);
    }
    @Override
    public void cashInInsertHistory(TransHistory transHistory, TransHistoryDetails transHistoryDetails) {


        try{

            //if( dataSets.insertHistory(transHistory))

            dataSets.insertHistory(transHistory);
            dataSets.insertHistoryDetails(transHistoryDetails);
        }catch (Exception x){


            Message.messageShort(getApplicationContext(),x.toString());
        }


    }
    @Override
    public void cashInSecondScreen(final String type,
                             final String provider,
                             final String Account,
                             final String amt) {


       new SecondScreen(type,provider,Account,amt).execute();


    }
    @Override
    public void cashInClearSecondScreen() {


        new ClearSecondScreen().execute();

    }
    @Override
    public void cashInPositiveSecondScreen() {
        new ThankyouSecondScreen().execute();


    }

    @Override
    public void cashInCurrencyDetectOn() {
     //new  CurrencyDetectorOn().execute();

        CURRENCYDETECTOR detect = new CURRENCYDETECTOR();
        detect.activateDevice();

        try{


            detect.On();

        }catch(Exception ex){



        }


        currencyOn=true;

        detect.deactivateDevice();
        detect = null;
    }
    @Override
    public void cashInCurrencyDetectOff() {
        //new  CurrencyDetectorOff().execute();
        CURRENCYDETECTOR detect = new CURRENCYDETECTOR();
        detect.activateDevice();

        try{


            detect.Off();

        }catch(Exception ex){



        }

        currencyOn=false;


        detect.deactivateDevice();
        detect = null;
    }
    public void cashInBadScreenPrintHack(){

       // new ThankyouSecondScreen().execute();
        CURRENCYDETECTOR detect = new CURRENCYDETECTOR();
        detect.activateDevice();

        try{


            detect.Off();

        }catch(Exception ex){



        }




        detect.deactivateDevice();
        detect = null;


    }


    ///////////////////CASH OUT//////////////////////

    @Override
    public void cashOutInsertHistory(TransHistory transHistory,
                                     TransHistoryDetails transHistoryDetails) {


        try{

            //if( dataSets.insertHistory(transHistory))

            dataSets.insertHistory(transHistory);
            dataSets.insertHistoryDetails(transHistoryDetails);
        }catch (Exception x){


            Message.messageShort(getApplicationContext(),x.toString());
        }


    }
    @Override
    public void cashOutSecondScreen(final String type,
                                   final String provider,
                                   final String Account,
                                   final String amt) {


        new SecondScreen(type,provider,Account,amt).execute();


    }
    @Override
    public void cashOutClearSecondScreen() {


        new ClearSecondScreen().execute();

    }
    @Override
    public void cashOutPositiveSecondScreen() {
        new ThankyouSecondScreen().execute();


    }
    @Override
    public void cashOutCurrencyDetectOn() {
        new  CurrencyDetectorOn().execute();
    }
    @Override
    public void cashOutCurrencyDetectOff() {
        new  CurrencyDetectorOff().execute();
    }
    public void cashOutBadScreenPrintHack(){

        new ThankyouSecondScreen().execute();



    }

    ////////////////////AIRTIME///////////////////////
    @Override
    public void AirtimeButtonClickListener(String name, String price) {
        PosTopLayout posTopLayout=(PosTopLayout)getSupportFragmentManager().findFragmentByTag("postoplayer");
        posTopLayout.updateListView(name, price);
        posTopLayout.InitiaLizeSummaryBoard();
    }
    @Override
    public void AirtimePrint(String name,String provider,String phone,String Amount) {
        PosTopLayout posTopLayout=(PosTopLayout)getSupportFragmentManager().findFragmentByTag("postoplayer");

        posTopLayout.executeGenericPrintOnBackground(name, provider, phone, Amount);
    }
    @Override
    public void AirtimeInsertHistory(TransHistory transHistory, TransHistoryDetails transHistoryDetails) {


        try{

            //if( dataSets.insertHistory(transHistory))

            dataSets.insertHistory(transHistory);
            dataSets.insertHistoryDetails(transHistoryDetails);
        }catch (Exception x){


            Message.messageShort(getApplicationContext(),x.toString());
        }


    }
    @Override
    public void AirtimeSecondScreen(final String type,
                                    final String provider,
                                    final String Account,
                                    final String amt) {


        new SecondScreen(type,provider,Account,amt).execute();


    }
    @Override
    public void AirtimeClearSecondScreen() {


        new ClearSecondScreen().execute();

    }
    @Override
    public void AirtimePositiveSecondScreen() {
        new ThankyouSecondScreen().execute();


    }

    @Override
    public void AirtimeCurrencyDetectOn() {
        new  CurrencyDetectorOn().execute();
    }
    @Override
    public void AirtimeCurrencyDetectOff() {
        new  CurrencyDetectorOff().execute();
    }
    public void AirtimeBadScreenPrintHack(){

        new ThankyouSecondScreen().execute();



    }

    /////////////////////BILL PAYMENTS////////////////////////

    @Override
    public void BillPayPrint(String name, String provider, String reference, String Amount) {
        PosTopLayout posTopLayout=(PosTopLayout)getSupportFragmentManager().findFragmentByTag("postoplayer");
        posTopLayout.executeGenericPrintOnBackgroundBP(name, provider, reference, Amount);
    }

    @Override
    public void BillPayementButtonClickListener(String name, String price) {
        PosTopLayout posTopLayout=(PosTopLayout)getSupportFragmentManager().findFragmentByTag("postoplayer");
        posTopLayout.updateListView(name, price);
        posTopLayout.InitiaLizeSummaryBoard();
    }
    @Override
    public void BillPaymentPrint(String type, String things) {
        PosTopLayout posTopLayout=(PosTopLayout)getSupportFragmentManager().findFragmentByTag("postoplayer");

       // posTopLayout.executeGenericPrintOnBackground(type, things);
    }
    @Override
    public void BillPayInsertHistory(TransHistory transHistory, TransHistoryDetails transHistoryDetails) {


        try{

            //if( dataSets.insertHistory(transHistory))

            dataSets.insertHistory(transHistory);
            dataSets.insertHistoryDetails(transHistoryDetails);
        }catch (Exception x){


            Message.messageShort(getApplicationContext(),x.toString());
        }


    }
    @Override
    public void BillPaySecondScreen(final String type,
                                    final String provider,
                                    final String Account,
                                    final String amt) {


        new SecondScreen(type,provider,Account,amt).execute();


    }
    @Override
    public void BillPayClearSecondScreen() {


        new ClearSecondScreen().execute();

    }
    @Override
    public void BillPayPositiveSecondScreen() {
        new ThankyouSecondScreen().execute();


    }

    @Override
    public void BillPayCurrencyDetectOn() {
        new  CurrencyDetectorOn().execute();
    }
    @Override
    public void BillPayCurrencyDetectOff() {
        new  CurrencyDetectorOff().execute();
    }

    ////*******STOCKING****//////

    @Override
    public void changeTitle(String title) {
        getSupportActionBar().setTitle("Stock/"+title);
    }

    @Override
    public void UpdateRecycleView(String val) {



        Stocking stocking=(Stocking)getSupportFragmentManager().findFragmentByTag("stock");

        if(val.equals("Items")){

            stocking.InitializeRecycleViewer();
        }else if(val.equals("Category")){
            stocking.InitializeRecycleViewerCatgory();
        }


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    ///ASYNTASK......///////////////
    private class SecondScreen extends AsyncTask<Void,Void,Void>{

          private String type;
          private String provider;
          private String Account;
          private String amt;

          public SecondScreen( String type,
                               String provider,
                               String Account,
                               String amt){


              this.type=type;
              this.provider=provider;
              this.Account=Account;
              this.amt=amt;

          }


          @Override
          protected Void doInBackground(Void... params) {

              SECONDSCREEN secondscreen = new SECONDSCREEN();

              secondscreen.activateDevice();
              if (secondscreen.init()) {
                  try {

                      secondscreen.printOnScreen(  type, 0, 0);
                      secondscreen.printOnScreen("**********", 0, 16);
                      secondscreen.printOnScreen(provider, 0, 32);
                      secondscreen.printOnScreen("To : "+ Account, 0, 48);
                      secondscreen.printOnScreen("Amount : "+   amt, 0, 64);
                      secondscreen.printOnScreen("****Confirm*****", 0, 80);
                      secondscreen.printOnScreen("Yes  /  No", 0, 96);
                  } catch (UnsupportedEncodingException ex) {
                      ex.printStackTrace();
                  }
              }
              secondscreen.deactivateDevice();
              secondscreen = null;

              return null;
          }
      }
    private class ClearSecondScreen extends AsyncTask<Void,Void,Void>{
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
    private class ThankyouSecondScreen extends  AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {

            SECONDSCREEN secondscreen = new SECONDSCREEN();
            secondscreen.activateDevice();
            if (secondscreen.init()) {


                try{


                    secondscreen.printOnScreen("Thank You ", 0, 0);



                }catch(Exception ex){



                }
                secondscreen.deactivateDevice();
                secondscreen = null;

            }
            return null;
        }
    }
    private class CurrencyDetectorOn extends  AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {

            CURRENCYDETECTOR detect = new CURRENCYDETECTOR();
            detect.activateDevice();

            try{


                detect.On();

            }catch(Exception ex){



            }




            detect.deactivateDevice();
            detect = null;
            return null;
        }
    }
    private class CurrencyDetectorOff extends  AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {

            CURRENCYDETECTOR detect = new CURRENCYDETECTOR();
            detect.activateDevice();

            try{


                detect.Off();

            }catch(Exception ex){



            }




            detect.deactivateDevice();
            detect = null;
            return null;
        }
    }
}
