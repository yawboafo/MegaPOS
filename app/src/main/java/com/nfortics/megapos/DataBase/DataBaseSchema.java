package com.nfortics.megapos.DataBase;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.security.SecureRandom;

/**
 * Created by bigifre on 7/9/2015.
 */
public class DataBaseSchema extends SQLiteOpenHelper {

    private Context context;

    private static final int DATABASE_VERSION=2;

    private static final String DATABASE_NAME="Ozinbo";
    private static final String ITEMS_TABLE="ITEMS";
    private static final String PRODUCTS_TABLE="PRODUCT";
    private static final String TRANS_HISTORY="TRANSACTIONS";
    private  static final String CATEGORY_TABLE="CATEGORY";
    private static final String TRANS_HISTORY_DETAILS="TRANSACDETAILS";
    private static final String SALES_HISTORY_TABLE="SALESHISTORY";
    private  static final String FAVPRODUCT="FAVPRODUCT";

    private static final String CREATE_ITEMS_TABLE = "CREATE TABLE ITEMS ( " +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "ISBN  VARCHAR(255), "+
            "Name VARCHAR(255)," +
            "Price  VARCHAR(45),Category VARCHAR(255) )";

/**
 *  public String Transid;
 public String referenceNumber;
 public double TranstotalAmount;
 public String TransItems;
 public String TransDate;
 public String TransTime;
 public String TransType;  //sale/cash out/cash in eetc
 public String TransStatus;   ///closed not closed
 public String TransMode;   //offline online
 public String Transtoken;
 *
 * public String TransDiscount;
 public String TransAmountPaid;
 public String TransTax;
 public String TransChange;
 * **/


private static final String CREATE_SALES_ACTIVITIES_TABLE ="CREATE TABLE SALESHISTORY (" +
        "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
        "TransreferenceNumber VARCHAR(255)," +
        "TranstotalAmount VARCHAR(255)," +
        "TransItems TEXT," +
        "TransDate VARCHAR(20)," +
        "TransTime VARCHAR(10)," +
        "TransType VARCHAR(10)," +
        "TransStatus VARCHAR(10)," +
        "TransMode VARCHAR(10)," +
        "Transtoken VARCHAR(10)," +
        "TransDiscount  VARCHAR(10)," +
        "TransAmountPaid  VARCHAR(10)," +
        "TransTax  VARCHAR(10), " +
        "TransChange VARCHAR(10)  )  ";

    private static final String CREATE_CATEGORY_TABLE="CREATE TABLE CATEGORY ( " +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "Name VARCHAR(255) )";

    private static final String CREATE_PRODUCTS_TABLE = "CREATE TABLE PRODUCT ( " +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "ISBN  VARCHAR(255), "+
            "Name VARCHAR(255)," +
            "Category VARCHAR(255),"+
            "Price  VARCHAR(45) )";

    private static final String CREATE_FAVPRODUCT_TABLE= "CREATE TABLE FAVPRODUCT ( " +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "ISBN  VARCHAR(255), "+
            "Name VARCHAR(255)," +
            "Category VARCHAR(255),"+
            "Price  VARCHAR(45) )";

    private static final String CREATE_TRANSACTIONS_TABLE="CREATE TABLE TRANSACTIONS(" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "Date VARCHAR(255)," +
            "Time VARCHAR(255)," +
            "TransID VARCHAR(255)," +
            "SellerID VARCHAR(255)," +
            "MerchantID VARCHAR(255)," +
            "Type VARCHAR(255)," +
            "DetailsID VARCHAR(255)," +
            "Modetype VARCHAR(255)," +
            "Totalsale VARCHAR(225) )";

    private static final String CREATE_TRANSACTIONS_DETAILS_TABLE="CREATE TABLE TRANSACDETAILS(" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "DeTailsID VARCHAR(255)," +
            "Items VARCHAR(500)," +
            "Sale VARCHAR(255) )";

    //create Items Table...
    private static String ItemsCreateQuery=
            "CREATE TABLE "+ITEMS_TABLE+"" +
            "(ISBN VARCHAR(255)" +
            ",Name VARCHAR(255)," +
            "Price VARCHAR(45)," +
            "Category VARCHAR(255));";

    private static final String Create_Items_Table=CREATE_ITEMS_TABLE;
    private static final String Create_product_Table=CREATE_PRODUCTS_TABLE;
    private static final String DROP_CATEGORY_TABLE="DROP "+CATEGORY_TABLE+" IF EXIST";
    private static final String DROP_FAVPRODUCT_TABLE="DROP "+FAVPRODUCT+" IF EXIST";
    private static final String DROP_ITEMS_TABLE="DROP "+ITEMS_TABLE+" IF EXIST";
    private static final String DROP_PRODUCT_TABLE="DROP "+PRODUCTS_TABLE+" IF EXIST";
    private static final String DROP_HISTORY="DROP "+TRANS_HISTORY+" IF EXIST";
    private static final String DROP_HISTORY_TABLE="DROP "+TRANS_HISTORY_DETAILS+" IF EXIST";
    private static final String DROP_SALES_HISTORY_TABLE="DROP"+SALES_HISTORY_TABLE+"IF EXIST";
    public DataBaseSchema(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        this.context=context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        try{

            db.execSQL(CREATE_FAVPRODUCT_TABLE);
            db.execSQL(Create_product_Table);
            db.execSQL(CREATE_CATEGORY_TABLE);
            db.execSQL(CREATE_SALES_ACTIVITIES_TABLE);
           // db.execSQL(CREATE_TRANSACTIONS_TABLE);
            //db.execSQL(CREATE_TRANSACTIONS_DETAILS_TABLE);
            Toast.makeText(context,"New DataBase Created",Toast.LENGTH_SHORT).show();
        }catch(SQLException ex){
            Log.e("ERROR",ex.toString());
           // Toast.makeText(context,"Failed To Create Database\n"+ex.toString(),Toast.LENGTH_SHORT).show();
            ex.printStackTrace();

        }



    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        try{
            db.execSQL(DROP_CATEGORY_TABLE);
            //db.execSQL(DROP_ITEMS_TABLE);
          db.execSQL(FAVPRODUCT);
           db.execSQL(DROP_SALES_HISTORY_TABLE);
            db.execSQL(DROP_PRODUCT_TABLE);
           // db.execSQL(DROP_HISTORY);
            onCreate(db);
        }catch(SQLException ex){
            ex.printStackTrace();

        }


    }
}
