package com.nfortics.megapos.Models;

/**
 * Created by bigifre on 6/28/2015.
 */
public class Product {

   // public int icon;
    public  String name;
   // public int quantity;
    public double price;

    public Product(double price, String name) {
        this.price = price;
       // this.icon = icon;
        this.name = name;
      //  this.quantity = quantity;
    }

    public Product() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
