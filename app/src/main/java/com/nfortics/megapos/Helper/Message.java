package com.nfortics.megapos.Helper;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by bigifre on 7/10/2015.
 */
public class Message {
    public static void message(Context context,String message){

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

    }

    public static void messageShort(Context context,String message){

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

    }
}
