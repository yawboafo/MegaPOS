package com.nfortics.megapos.Network;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nfortics.megapos.Application.MyApplication;

/**
 * Created by bigifre on 7/28/2015.
 */
public class VolleySingleton {
    private static VolleySingleton sInstance=null;
    private static RequestQueue mRequestQueue;
    private VolleySingleton(){
        mRequestQueue= Volley.newRequestQueue(MyApplication.getAppContext());
    }


    public static VolleySingleton getsInstance(){

        if(sInstance==null){
            sInstance=new VolleySingleton();
        }
        return  sInstance;
    }

    public static RequestQueue getRequestQueue(){

        return mRequestQueue;

    }
}

