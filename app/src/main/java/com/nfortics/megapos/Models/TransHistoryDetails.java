package com.nfortics.megapos.Models;

/**
 * Created by bigifre on 7/30/2015.
 */
public class TransHistoryDetails {

    private String DeTailsID;
    private String Items;
    private String sale;

    public TransHistoryDetails() {
    }

    public TransHistoryDetails(String deTailsID, String items) {
        DeTailsID = deTailsID;
        Items = items;
    }

    public String getDeTailsID() {
        return DeTailsID;
    }

    public void setDeTailsID(String deTailsID) {
        DeTailsID = deTailsID;
    }

    public String getItems() {
        return Items;
    }

    public void setItems(String items) {
        Items = items;
    }

    public String getSale() {
        return sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }
}
