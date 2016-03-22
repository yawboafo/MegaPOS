package com.nfortics.megapos.Models;

/**
 * Created by bigifre on 8/13/2015.
 */
public class ItemsBought {
    //  sb.append(items.name + "\t" +"("+items.quatity+")" +  realprice +"\t"+ useful.cleanAstring(items.amount,3));
    public String ItemName;
    public String Quantity;
    public String RealPrice;
    public String Amount;


    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getRealPrice() {
        return RealPrice;
    }

    public void setRealPrice(String realPrice) {
        RealPrice = realPrice;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }
}
