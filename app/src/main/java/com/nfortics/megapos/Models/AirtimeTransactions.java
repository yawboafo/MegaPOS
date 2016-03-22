package com.nfortics.megapos.Models;

import java.security.PublicKey;

/**
 * Created by bigifre on 8/13/2015.
 */
public class AirtimeTransactions {

    public String Transid;
    public String referenceNumber;
    public String TransDate;
    public String TransTime;
    public String TransType;  //sale/cash out/cash in eetc
    public String TransStatus;   ///closed not closed  if not losed> 0 else 1
    public String TransMode;   //offline online
    public String Transtoken;
    public String TransItems;
   public AirtimeItems airtimeItems;

    public String getTransid() {
        return Transid;
    }

    public void setTransid(String transid) {
        Transid = transid;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getTransDate() {
        return TransDate;
    }

    public void setTransDate(String transDate) {
        TransDate = transDate;
    }

    public String getTransTime() {
        return TransTime;
    }

    public void setTransTime(String transTime) {
        TransTime = transTime;
    }

    public String getTransType() {
        return TransType;
    }

    public void setTransType(String transType) {
        TransType = transType;
    }

    public String getTransStatus() {
        return TransStatus;
    }

    public void setTransStatus(String transStatus) {
        TransStatus = transStatus;
    }

    public String getTransMode() {
        return TransMode;
    }

    public void setTransMode(String transMode) {
        TransMode = transMode;
    }

    public String getTranstoken() {
        return Transtoken;
    }

    public void setTranstoken(String transtoken) {
        Transtoken = transtoken;
    }

    public String getTransItems() {
        return TransItems;
    }

    public void setTransItems(String transItems) {
        TransItems = transItems;
    }

    public AirtimeItems getAirtimeItems() {
        return airtimeItems;
    }

    public void setAirtimeItems(AirtimeItems airtimeItems) {
        this.airtimeItems = airtimeItems;
    }
}


class AirtimeItems{

    public String Provider;
    public String Phone;
    public String Total;

}