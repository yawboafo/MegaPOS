package com.nfortics.megapos.Models;

/**
 * Created by bigifre on 8/11/2015.
 */;

public class Transactions {
    public String Transid;
    public String referenceNumber;
    public double TranstotalAmount;
    public String TransItems;
    public String TransDate;
    public String TransTime;
    public String TransType;  //sale/cash out/cash in eetc
    public String TransStatus;   ///closed not closed  if not losed> 0 else 1
    public String TransMode;   //offline online
    public String Transtoken;
    public String TransDiscount;
    public String TransAmountPaid;
    public String TransTax;
    public String TransChange;
    //Transmode==offline/onlinie



    public String getTransDiscount() {
        return TransDiscount;
    }

    public void setTransDiscount(String transDiscount) {
        TransDiscount = transDiscount;
    }

    public String getTransAmountPaid() {
        return TransAmountPaid;
    }

    public void setTransAmountPaid(String transAmountPaid) {
        TransAmountPaid = transAmountPaid;
    }

    public String getTransTax() {
        return TransTax;
    }

    public void setTransTax(String transTax) {
        TransTax = transTax;
    }

    public String getTransChange() {
        return TransChange;
    }

    public void setTransChange(String transChange) {
        TransChange = transChange;
    }

    public String getTranstoken() {
        return Transtoken;
    }

    public void setTranstoken(String transtoken) {
        Transtoken = transtoken;
    }

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

    public double getTranstotalAmount() {
        return TranstotalAmount;
    }

    public void setTranstotalAmount(double transtotalAmount) {
        TranstotalAmount = transtotalAmount;
    }

    public String getTransItems() {
        return TransItems;
    }

    public void setTransItems(String transItems) {
        TransItems = transItems;
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
}
