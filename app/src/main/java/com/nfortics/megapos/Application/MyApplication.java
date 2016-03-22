package com.nfortics.megapos.Application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.nfortics.megapos.Domain.LoginModel;
import com.nfortics.megapos.Helper.Message;
import com.nfortics.megapos.utils.GsonMan;

/**
 * Created by bigifre on 7/28/2015.
 */
public class MyApplication  extends Application{
    //http://174.142.15.209:8000/ozinbo/api/merchant/transaction


    private static String  shopPhoneNumber="0208173179";
    private static String  shopname="Koala";
    private static String  shopBranch="Tema";
    private static String  LOGIN_URL="http://174.142.15.209:8000/ozinbo/api/merchant/login";
    private static String  AIRTIME_URL="http://174.142.15.209:5000/nfsp/airtime";
    private static String  BILLPAY_URL="http://174.142.15.209:5000/nfsp/bill";
    private static String  PAYMENT_URL_Mobile=" http://174.142.15.209:5000/nfsp/payments";
    private static String  PAYMENT_URL="http://174.142.15.209:8000/ozinbo/api/merchant/transaction";
    private static String  PAYMENT_URL_CashOut_Mobile="http://174.142.15.209:5000/nfsp/payments";
    private static String  PAYMENT__CashOut_URL="http://174.142.15.209:8000/ozinbo/api/merchant/transaction";
    private static String  CONFIRM_SAP_URL="http://174.142.15.209:8000/ozinbo/api/merchant/withdrawal/verify";
    private static String  App_name="OzinboPOS";
    private static String  App_Country="Ghana";
    private static String  Payment_Provider="etranzact";
    private static String  ApplicationClient="CCML";
    private static Boolean testing=false;
    private static MyApplication sInstance;
    public static LoginModel merchant;
    Gson gson ;
    private static GsonMan gsonMan;
    private static Bundle extras;
    private static String userName,UserId;
    @Override
    public void onCreate() {
        super.onCreate();
        sInstance=this;
        gsonMan=new GsonMan();
        gson = new Gson();
        userName="yawboafo";
        UserId="900090";
    }
    public static MyApplication getsInstance(){

        return sInstance;
    }
    public static String getApp_name(){
        return App_name;
    }
    public static String getPAYMENT_URL_MobileURL(){

        return PAYMENT_URL_Mobile;
    }
    public static String getPAYMENT__CashOut_URL(){

        return PAYMENT__CashOut_URL;
    }
    public static String getPAYMENT_URL_CashOut_Mobile(){

        return  PAYMENT_URL_CashOut_Mobile;

    }

    public static String getShopPhoneNumber() {
        return shopPhoneNumber;
    }

    public static String getLoginURL(){

        return LOGIN_URL;
    }
    public static String getConfirmSapUrl(){

        return CONFIRM_SAP_URL;
    }
    public static String getAirtimeUrl(){

        return  AIRTIME_URL;

    }

    public static String getShopname() {
        return shopname;
    }

    public static String getShopBranch() {
        return shopBranch;
    }

    public static String getApp_Country() {
        return App_Country;
    }

    public static String getBillpayUrl(){return BILLPAY_URL;}
    public static String getPaymentUrl(){return PAYMENT_URL; }
   // public static String getPaymentUrlDummy(){return PAYMENT_DUMMY_URL; }
    public static String getPayment_Provider(){

        return Payment_Provider;
    }
    public static String getApplicationClient(){
        return  ApplicationClient;
    }
    public static Context getAppContext(){

        return  sInstance.getApplicationContext();

    }
    public static Boolean getApplicationSeriousness(){

        return testing;
    }
    public static LoginModel getMerchant() {
        return merchant;
    }
    public static void setMerchant(LoginModel merchant) {
        MyApplication.merchant = merchant;
    }
    public static LoginModel merchant(String val){

        merchant = gsonMan.merChantString(val);
       // Message.messageShort(getAppContext(),val.toString());
        //Message.messageShort(getAppContext(),merchant.getName().toString());
        return merchant;
    }
    public LoginModel merChantString(String json){

       // LoginModel loginModel=null;
        try{

            merchant=gson.fromJson(json,LoginModel.class);

        }catch(Exception ex){



        }



        return  merchant;
    }


   public static String getUserName(Activity activity){

       try{
          // gsonMan= new GsonMan();
           extras = activity.getIntent().getExtras();





           if(extras == null) {

           }else {


              // userName= extras.getString("UserDetails");
               //UserId=gsonMan.getMerchantDetails(userName, activity).getName();
               Log.d("UserID   > > > : ", UserId);


           }
       }catch (Exception e){



       }


       return userName;
    }
    public static String getUserID(Activity activity){

        try{
            // gsonMan= new GsonMan();
            extras = activity.getIntent().getExtras();





            if(extras == null) {

            }else {


               // userName= extras.getString("UserDetails");
              //  UserId=gsonMan.getMerchantDetails(userName, activity).getId();
                Log.d("UserID   > > > : ", UserId);


            }
        }catch (Exception e){



        }


        return UserId;
    }

}
