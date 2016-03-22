package com.nfortics.megapos.Helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.nfortics.megapos.Domain.BankModel;
import com.nfortics.megapos.Domain.BillerModel;
import com.nfortics.megapos.Domain.TelcoModel;
import com.nfortics.megapos.utils.GsonMan;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bigifre on 7/24/2015.
 */
public class ControlHelpers {
    GsonMan  gsonMan=new GsonMan();
    public void populateSpinner(Spinner spinner,List<String> lables,Activity activity){
        // spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(activity,
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }


    public void makeListForBankSpinners( final Activity activity,
                                     final Spinner spinner,
                                     final String  userName){

        final List<String> data1=new ArrayList<>();

        try{

            activity. runOnUiThread(new Runnable() {
                public void run() {
                    // change text

                    for (BankModel bankModel : gsonMan.getMerchantDetails(userName, activity).getBanks()) {
                        data1.add(bankModel.getName());
                    }


                    populateSpinner(spinner, data1, activity);


                }
            });

        }catch (Exception ex){


        }



    }


int value;
    public int OptionDialog(final Activity context,
                                    String message){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        alertDialogBuilder
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                            Message.messageShort(context,""+id);


                        value=id;
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Message.messageShort(context,""+which);
                value=which;
                dialog.cancel();

            }
        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.show();

       return  value;

    }

    public AlertDialog AnAlertertDialog(Activity context,String message,String title){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        // alertDialogBuilder.
        // alertDialogBuilder.setTitle(title);

        // set dialog message


        alertDialogBuilder
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        //
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


        return  alertDialog;

    }

    public void makeListForTelcosSpinners( final Activity activity,
                                         final Spinner spinner,
                                         final String  userName){

        final List<String> data1=new ArrayList<>();


        try{
            activity. runOnUiThread (new Runnable() {
                public void run() {
                    // change text

                    for(TelcoModel bankModel:gsonMan.getMerchantDetails(userName,activity).getTelcos()){
                        data1.add(bankModel.getName());
                    }



                    populateSpinner(spinner,data1,activity);


                }
            });
        }catch(Exception ex){


        }


    }

    public void makeListForBillersSpinners( final Activity activity,
                                           final Spinner spinner,
                                           final String  userName){
        final List<String> data1=new ArrayList<>();

        try{

            activity. runOnUiThread (new Runnable() {
                public void run() {
                    // change text

                    for(BillerModel bankModel:gsonMan.getMerchantDetails(userName,activity).getBillers()){
                        data1.add(bankModel.getName());
                    }



                    populateSpinner(spinner,data1,activity);


                }
            });

            Log.d("oxinbo", "username from controlhelper"+userName);

        }catch(Exception ex){



            ex.printStackTrace();

        }




    }

}
