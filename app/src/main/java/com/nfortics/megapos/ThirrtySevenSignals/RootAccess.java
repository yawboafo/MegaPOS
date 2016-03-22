package com.nfortics.megapos.ThirrtySevenSignals;

/**
 * Created by bigifre on 7/28/2015.
 */
public class RootAccess {


    public boolean formFillsCheck(String domain,
                                  String username,
                                  String password){

        if(domain.equals("root")
                & username.equals("root")
                & password.equals("root")){


            return true;
        }

        return false;

    }


}
