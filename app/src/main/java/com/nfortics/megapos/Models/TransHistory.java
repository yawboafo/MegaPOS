package com.nfortics.megapos.Models;

/**
 * Created by bigifre on 7/30/2015.
 */
public class TransHistory {

    private String date;
    private String time;
    private String TransID;
    private String SellerID;
    private String MerchantID;
    private String Type;
    private String DetailsID;
    private String Modetype;
    private String TotalSale;
    public TransHistory() {
    }

    public TransHistory(String date, String time, String transID, String sellerID, String merchantID, String type, String detailsID, String modetype) {
        this.date = date;
        this.time = time;
        TransID = transID;
        SellerID = sellerID;
        MerchantID = merchantID;
        Type = type;
        DetailsID = detailsID;
        Modetype = modetype;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMerchantID() {
        return MerchantID;
    }

    public void setMerchantID(String merchantID) {
        MerchantID = merchantID;
    }

    public String getSellerID() {
        return SellerID;
    }

    public void setSellerID(String sellerID) {
        SellerID = sellerID;
    }

    public String getTransID() {
        return TransID;
    }

    public void setTransID(String transID) {
        TransID = transID;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getDetailsID() {
        return DetailsID;
    }

    public void setDetailsID(String detailsID) {
        DetailsID = detailsID;
    }

    public String getModetype() {
        return Modetype;
    }

    public void setModetype(String modetype) {
        Modetype = modetype;
    }

    public String getTotalSale() {
        return TotalSale;
    }

    public void setTotalSale(String totalSale) {
        TotalSale = totalSale;
    }
}


