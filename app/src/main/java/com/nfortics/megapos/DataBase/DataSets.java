package com.nfortics.megapos.DataBase;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.nfortics.megapos.Helper.Useful;
import com.nfortics.megapos.Models.CategoryValues;
import com.nfortics.megapos.Models.Product;
import com.nfortics.megapos.Models.Products;
import com.nfortics.megapos.Models.TransHistory;
import com.nfortics.megapos.Models.TransHistoryDetails;
import com.nfortics.megapos.Models.Transactions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bigifre on 7/10/2015.
 */
public class DataSets extends  DataBaseSchema {

    private Context context;
    DataBaseSchema dataBaseSchema;
    Useful useful= new Useful();


    public DataSets(Context context) {
        super(context);

        this.context=context;
        dataBaseSchema= new DataBaseSchema(context);
    }
    public long insertITEMS(String isbn,String name,String price,String category){

        SQLiteDatabase db=dataBaseSchema.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("ISBN",isbn);
        contentValues.put("Name",useful.capitalize(name) );
        contentValues.put("Price", price);
        contentValues.put("Category", category);
        long id=db.insert("ITEMS",null,contentValues);

        return  id;
    }
    public long insertPRODUCT(String isbn,String name,String price,String category){

        SQLiteDatabase db=dataBaseSchema.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("ISBN",isbn);
        contentValues.put("Name",useful.capitalize(name) );
        contentValues.put("Price", price);
        contentValues.put("Category", category);
        long id=db.insert("PRODUCT",null,contentValues);

        return  id;
    }
    //String isbn,String name,String price,String category
    public long insertFavoritePRODUCT(Products product){

        SQLiteDatabase db=dataBaseSchema.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("ISBN",product.getIsbn());
        contentValues.put("Name",product.getName() );
        contentValues.put("Price", product.getPrice());
        contentValues.put("Category", product.getCategory());
        long id=db.insert("FAVPRODUCT",null,contentValues);

        return  id;
    }
    public boolean insertFavoriteProduct(Activity context,String name){







        SQLiteDatabase db=dataBaseSchema.getWritableDatabase();
        boolean mark=false;
        List<String> data=new ArrayList<>();
        String [] colums={"Name"};
        Cursor cursor=db.query("FAVPRODUCT",colums,null,null,null,null,null);
        context.startManagingCursor(cursor);
        Log.d("oxinbo", name + "  tjen mane ");
        while (cursor.moveToNext()){

            String Cname=cursor.getString(0);
            //product.setName(name);
            Log.d("oxinbo",Cname+"Inside Cursor ");

            data.add(Cname);
            //buffer.append(name +  "" +price);

        }



        for(String x :data){

            if(x.equals(name)){
               deleteFavorite(context,name);
                mark=true;
                Log.d("oxinbo",x+"equals to "+name);
            }else {
                mark=false;
                Log.d("oxinbo",x+"In not equals to "+name);

            }


        }






        return mark;

    }

    /**
     * getAllFavoritesString
     * */


    public long insertHistory(TransHistory transHistory){

        SQLiteDatabase db=dataBaseSchema.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("Date",transHistory.getDate());
        contentValues.put("Time",transHistory.getTime());
        contentValues.put("TransID", transHistory.getTransID());
        contentValues.put("SellerID",transHistory.getSellerID());
        contentValues.put("MerchantID",transHistory.getMerchantID());
        contentValues.put("Type",transHistory.getType() );
        contentValues.put("DetailsID", transHistory.getDetailsID());
        contentValues.put("Modetype", transHistory.getModetype());
        contentValues.put("Totalsale", transHistory.getTotalSale());
        long id=db.insert("TRANSACTIONS",null,contentValues);

        return  id;





    }
    public long insertCategory(String name ){
        SQLiteDatabase db=dataBaseSchema.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("Name", name);
        long id=db.insert("CATEGORY",null,contentValues);
        return  id;
    }
    public long insertHistoryDetails(TransHistoryDetails transHistoryDetails){

        SQLiteDatabase db=dataBaseSchema.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("DeTailsID",transHistoryDetails.getDeTailsID());
        contentValues.put("Items",transHistoryDetails.getItems());
        contentValues.put("Sale", transHistoryDetails.getSale());

        long id=db.insert("TRANSACDETAILS",null,contentValues);

        return  id;


    }
    public long insertTransaction(Transactions transactions){
/**public String TransDiscount;
 public String TransAmountPaid;
 public String TransTax;
 public String TransChange;*
 * **/
        SQLiteDatabase db=dataBaseSchema.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("TransreferenceNumber",transactions.getReferenceNumber());
        contentValues.put("TranstotalAmount",transactions.getTranstotalAmount());
        contentValues.put("TransItems", transactions.getTransItems());
        contentValues.put("TransDate",transactions.getTransDate());
        contentValues.put("TransTime",transactions.getTransTime());
        contentValues.put("TransType",transactions.getTransType() );
        contentValues.put("TransStatus",transactions.getTransStatus());
        contentValues.put("TransMode",transactions.getTransMode());
        contentValues.put("Transtoken",transactions.getTranstoken());


        contentValues.put("TransDiscount",transactions.getTransDiscount() );
        contentValues.put("TransAmountPaid",transactions.getTransAmountPaid());
        contentValues.put("TransTax",transactions.getTransTax());
        contentValues.put("TransChange", transactions.getTransTax());


        long id=db.insert("SALESHISTORY",null,contentValues);




        return id;
    }
    public List<CategoryValues>getCategoryValues(Activity context){
        SQLiteDatabase db=dataBaseSchema.getWritableDatabase();

        List<CategoryValues> data=new ArrayList<>();
        String [] colums={"_id","Name"};
        Cursor cursor=db.query("CATEGORY", colums, null, null, null, null, null);
        context.startManagingCursor(cursor);
        while (cursor.moveToNext()){
          CategoryValues categoryValues=new CategoryValues();
            String id=cursor.getString(0);
            categoryValues.id=id;
            String name=cursor.getString(1);
            categoryValues.name=name;
          categoryValues.selected=false;

            data.add(categoryValues);
        }
        return data;

    }
    public List<CategoryValues>getCategoryValuesLike(Activity context,String value){
        SQLiteDatabase db=dataBaseSchema.getWritableDatabase();

        List<CategoryValues> data=new ArrayList<>();
        String [] colums={"_id","Name"};
        Cursor cursor=db.query("CATEGORY", colums, "Name"+" LIKE '%"+value+"%'", null, null, null, null);
        context.startManagingCursor(cursor);
        while (cursor.moveToNext()){
            CategoryValues categoryValues=new CategoryValues();
            String id=cursor.getString(0);
            categoryValues.id=id;
            String name=cursor.getString(1);
            categoryValues.name=name;
            categoryValues.selected=false;

            data.add(categoryValues);
        }
        return data;

    }
    public List<String>getAllFavoritesString(Activity context){

        SQLiteDatabase db=dataBaseSchema.getWritableDatabase();

        List<String> data=new ArrayList<>();
        String [] colums={"Name"};
        Cursor cursor=db.query("FAVPRODUCT",colums,null,null,null,null,null);
        context.startManagingCursor(cursor);
        while (cursor.moveToNext()){
           /// Product product=new Product();
            String name=cursor.getString(0);
            Log.d("oxinbo",name);
         //   product.setName(name);

           // String price=cursor.getString(1);
           // product.setPrice(Double.parseDouble(price));





            data.add(name);
            //buffer.append(name +  "" +price);

        }



        return  data;


    }
    public List<Product>getAllFavorites(Activity context){

        SQLiteDatabase db=dataBaseSchema.getWritableDatabase();

        List<Product> data=new ArrayList<>();
        String [] colums={"Name","Price"};
        Cursor cursor=db.query("FAVPRODUCT",colums,null,null,null,null,null);
        context.startManagingCursor(cursor);
        while (cursor.moveToNext()){
            Product product=new Product();
            String name=cursor.getString(0);
            product.setName(name);

            String price=cursor.getString(1);
            product.setPrice(Double.parseDouble(price));





            data.add(product);
            //buffer.append(name +  "" +price);

        }



        return  data;


    }
    public List<Products>getAllProducts(Activity context){

        SQLiteDatabase db=dataBaseSchema.getWritableDatabase();

        List<Products> data=new ArrayList<>();
        String [] colums={"Name","Price","Category","ISBN","_id"};
        Cursor cursor=db.query("PRODUCT",colums,null,null,null,null,null);
        context.startManagingCursor(cursor);
        while (cursor.moveToNext()){
            Products product=new Products();
            String name=cursor.getString(0);
            product.setName(name);

            String price=cursor.getString(1);
            product.setPrice(Double.parseDouble(price));

            String category=cursor.getString(2);
            product.setCategory(category);


            String isbn=cursor.getString(3);
            product.setIsbn(isbn);

            String id=cursor.getString(4);
            product.setId(id);


            data.add(product);
            //buffer.append(name +  "" +price);

        }



        return  data;


    }
    public List<Product>getAllProduct(Activity context){


        SQLiteDatabase db=dataBaseSchema.getWritableDatabase();

        List<Product> data=new ArrayList<>();

        String [] colums={"Name","Price"};
        Cursor cursor = db.query("PRODUCT",colums,null,null,null,null,null);
        context.startManagingCursor(cursor);
        while (cursor.moveToNext()){
            Product product=new Product();
            String name=cursor.getString(0);
            product.setName(name);
            String price=cursor.getString(1);
            product.setPrice(Double.parseDouble(price));
            data.add(product);
            //buffer.append(name +  "" +price);

        }
        return data;
    }
    public Transactions getAllTransactionsByRefrence(Activity context,String reference){

        Transactions  transactions=null;
        try {
            SQLiteDatabase db=dataBaseSchema.getWritableDatabase();


            String [] colums={"_id",
                    "TransreferenceNumber",
                    "TranstotalAmount",
                    "TransItems",
                    "TransDate",
                    "TransTime",
                    "TransType",
                    "TransStatus",
                    "TransMode",
                    "Transtoken",
                    "TransDiscount",
                    "TransAmountPaid",
                    "TransTax",
                    "TransChange"};
            Cursor cursor=db.query("SALESHISTORY",colums,"TransreferenceNumber = '"+reference+"'",null,null,null,null);
            context.startManagingCursor(cursor);
            while (cursor.moveToNext()) {
                transactions = new Transactions();
                String id = cursor.getString(0);
                transactions.setTransid(id);




                String TransreferenceNumber = cursor.getString(1);
                transactions.setReferenceNumber(TransreferenceNumber);

                String TranstotalAmount = cursor.getString(2);
                transactions.setTranstotalAmount(Double.valueOf(TranstotalAmount));


                String TransItems = cursor.getString(3);
               // Log.d("Ozinbo ", " transItems from datasets Raw" + TransItems);
                transactions.setTransItems(TransItems.replaceAll("\\\\", ""));



                String TransDate = cursor.getString(4);
                transactions.setTransDate(TransDate);

                String TransTime = cursor.getString(5);
                transactions.setTransTime(TransTime);

                String TransType = cursor.getString(6);
                transactions.setTransType(TransType);

                String TransStatus = cursor.getString(7);
                transactions.setTransStatus(TransStatus);

                String TransMode = cursor.getString(8);
                transactions.setTransMode(TransMode);

                String Transtoken = cursor.getString(9);
                transactions.setTranstoken(Transtoken);

                String TransDiscount = cursor.getString(10);
                transactions.setTransDiscount(TransDiscount);

                String TransAmountPaid = cursor.getString(11);
                transactions.setTransAmountPaid(TransAmountPaid);

                String TransTax = cursor.getString(12);
                transactions.setTransTax(TransTax);

                String TransChange = cursor.getString(13);
                transactions.setTransChange(TransChange);


            }



        }catch (Exception e){

            e.printStackTrace();
        }



        String ttransac=transactions.getTransItems();

        //String loginToken = getName().toString();
        ttransac = ttransac.substring(1, ttransac.length() - 1);

       Log.d("Ozinbo ", " transItems from datasets" + transactions.getTransItems() + "\n  modifired " +
               "" + ttransac);



        return  transactions;


    }
    public List<Transactions>getAllTransactions(Activity context,String date){

        SQLiteDatabase db=dataBaseSchema.getWritableDatabase();

        List<Transactions> data=new ArrayList<>();
        String [] colums={"_id",
                "TransreferenceNumber",
                "TranstotalAmount",
                "TransItems",
                "TransDate",
                "TransTime",
                "TransType",
                "TransStatus",
                "TransMode",
                "Transtoken","TransDiscount","TransAmountPaid","TransTax","TransChange"};
        Cursor cursor=db.query("SALESHISTORY",colums,null,null,null,null,null);
        context.startManagingCursor(cursor);
        while (cursor.moveToNext()){
            Transactions transactions=new Transactions();
            String id=cursor.getString(0);
            transactions.setTransid(id);

            String TransreferenceNumber=cursor.getString(1);
            transactions.setReferenceNumber(TransreferenceNumber);

            String TranstotalAmount=cursor.getString(2);
            transactions.setTranstotalAmount(Double.valueOf(TranstotalAmount));


            String TransItems=cursor.getString(3);
            transactions.setTransItems(TransItems);

            String TransDate=cursor.getString(4);
            transactions.setTransDate(TransDate);

            String TransTime=cursor.getString(5);
            transactions.setTransTime(TransTime);

            String TransType=cursor.getString(6);
            transactions.setTransType(TransType);

            String TransStatus=cursor.getString(7);
            transactions.setTransStatus(TransStatus);

            String TransMode=cursor.getString(8);
            transactions.setTransMode(TransMode);

            String Transtoken=cursor.getString(9);
            transactions.setTranstoken(Transtoken);

            String TransDiscount=cursor.getString(10);
            transactions.setTransDiscount(TransDiscount);

            String TransAmountPaid=cursor.getString(11);
            transactions.setTransAmountPaid(TransAmountPaid);

            String TransTax=cursor.getString(12);
            transactions.setTransTax(TransTax);

            String TransChange=cursor.getString(13);
            transactions.setTransChange(TransChange);
            data.add(transactions);
            //buffer.append(name +  "" +price);

        }



        return  data;


    }
    public List<Transactions>getAllTransactionsByDate(Activity context,String date){

        SQLiteDatabase db=dataBaseSchema.getWritableDatabase();

        List<Transactions> data=new ArrayList<>();
        String [] colums={"_id",
                "TransreferenceNumber",
                "TranstotalAmount",
                "TransItems",
                "TransDate",
                "TransTime",
                "TransType",
                "TransStatus",
                "TransMode",
                "Transtoken","TransDiscount","TransAmountPaid","TransTax","TransChange"};
        Cursor cursor=db.query("SALESHISTORY",colums,"TransDate = '"+date+"'",null,null,null,null);
        context.startManagingCursor(cursor);
        while (cursor.moveToNext()){
            Transactions transactions=new Transactions();
            String id=cursor.getString(0);
            transactions.setTransid(id);

            String TransreferenceNumber=cursor.getString(1);
            transactions.setReferenceNumber(TransreferenceNumber);

            String TranstotalAmount=cursor.getString(2);
            transactions.setTranstotalAmount(Double.valueOf(TranstotalAmount));


            String TransItems=cursor.getString(3);
            transactions.setTransItems(TransItems);

            String TransDate=cursor.getString(4);
            transactions.setTransDate(TransDate);

            String TransTime=cursor.getString(5);
            transactions.setTransTime(TransTime);

            String TransType=cursor.getString(6);
            transactions.setTransType(TransType);

            String TransStatus=cursor.getString(7);
            transactions.setTransStatus(TransStatus);

            String TransMode=cursor.getString(8);
            transactions.setTransMode(TransMode);

            String Transtoken=cursor.getString(9);
            transactions.setTranstoken(Transtoken);

            String TransDiscount=cursor.getString(10);
            transactions.setTransDiscount(TransDiscount);

            String TransAmountPaid=cursor.getString(11);
            transactions.setTransAmountPaid(TransAmountPaid);

            String TransTax=cursor.getString(12);
            transactions.setTransTax(TransTax);

            String TransChange=cursor.getString(13);
            transactions.setTransChange(TransChange);
            data.add(transactions);
            //buffer.append(name +  "" +price);

        }



        return  data;


    }
    public List<Transactions>getAllTransactionsByReference(Activity context,String refere){

        SQLiteDatabase db=dataBaseSchema.getWritableDatabase();

        List<Transactions> data=new ArrayList<>();
        String [] colums={
                "_id",
                "TransreferenceNumber",
                "TranstotalAmount",
                "TransItems",
                "TransDate",
                "TransTime",
                "TransType",
                "TransStatus",
                "TransMode",
                "Transtoken",
                "TransDiscount",
                "TransAmountPaid",
                "TransTax",
                "TransChange"};
        Cursor cursor=db.query("SALESHISTORY",colums,"TransreferenceNumber LIKE '%"+refere+"%'",null,null,null,null);
        context.startManagingCursor(cursor);
        while (cursor.moveToNext()){
            Transactions transactions=new Transactions();
            String id=cursor.getString(0);
            transactions.setTransid(id);

            String TransreferenceNumber=cursor.getString(1);
            transactions.setReferenceNumber(TransreferenceNumber);

            String TranstotalAmount=cursor.getString(2);
            Log.d("oxinbo","total Amount   : "+TranstotalAmount);
            transactions.setTranstotalAmount(Double.valueOf(TranstotalAmount));


            String TransItems=cursor.getString(3);
            transactions.setTransItems(TransItems);

            String TransDate=cursor.getString(4);
            transactions.setTransDate(TransDate);

            String TransTime=cursor.getString(5);
            transactions.setTransTime(TransTime);

            String TransType=cursor.getString(6);
            transactions.setTransType(TransType);

            String TransStatus=cursor.getString(7);
            transactions.setTransStatus(TransStatus);

            String TransMode=cursor.getString(8);
            transactions.setTransMode(TransMode);

            String Transtoken=cursor.getString(9);
            transactions.setTranstoken(Transtoken);

            String TransDiscount=cursor.getString(10);
            transactions.setTransDiscount(TransDiscount);

            String TransAmountPaid=cursor.getString(11);
            transactions.setTransAmountPaid(TransAmountPaid);

            String TransTax=cursor.getString(12);
            transactions.setTransTax(TransTax);

            String TransChange=cursor.getString(13);
            transactions.setTransChange(TransChange);
            data.add(transactions);
            //buffer.append(name +  "" +price);

        }



        return  data;


    }

    public List<Transactions>getAllTransactions(Activity context){

        SQLiteDatabase db=dataBaseSchema.getWritableDatabase();

        List<Transactions> data=new ArrayList<>();
        String [] colums={"_id",
                "TransreferenceNumber",
                "TranstotalAmount",
                "TransItems",
                "TransDate",
                "TransTime",
                "TransType",
                "TransStatus",
                "TransMode",
                "Transtoken","TransDiscount","TransAmountPaid","TransTax","TransChange"};
        Cursor cursor=db.query("SALESHISTORY",colums,null,null,null,null,null);
        context.startManagingCursor(cursor);
        while (cursor.moveToNext()){
            Transactions transactions=new Transactions();
            String id=cursor.getString(0);
            transactions.setTransid(id);

            String TransreferenceNumber=cursor.getString(1);
            transactions.setReferenceNumber(TransreferenceNumber);

            String TranstotalAmount=cursor.getString(2);
            transactions.setTranstotalAmount(Double.valueOf(TranstotalAmount));


            String TransItems=cursor.getString(3);
            transactions.setTransItems(TransItems);

            String TransDate=cursor.getString(4);
            transactions.setTransDate(TransDate);

            String TransTime=cursor.getString(5);
            transactions.setTransTime(TransTime);

            String TransType=cursor.getString(6);
            transactions.setTransType(TransType);

            String TransStatus=cursor.getString(7);
            transactions.setTransStatus(TransStatus);

            String TransMode=cursor.getString(8);
            transactions.setTransMode(TransMode);

            String Transtoken=cursor.getString(9);
            transactions.setTranstoken(Transtoken);

//TransDiscount,TransAmountPaid,TransTax,TransChange

            String TransDiscount=cursor.getString(10);
            transactions.setTransDiscount(TransDiscount);

            String TransAmountPaid=cursor.getString(11);
            transactions.setTransAmountPaid(TransAmountPaid);

            String TransTax=cursor.getString(12);
            transactions.setTransTax(TransTax);

            String TransChange=cursor.getString(13);
            transactions.setTransChange(TransChange);








            data.add(transactions);
            //buffer.append(name +  "" +price);

        }



        return  data;


    }
    public List<Products>getAllProductsLike(Activity context,String value){

        SQLiteDatabase db=dataBaseSchema.getWritableDatabase();

        List<Products> data=new ArrayList<>();
        String [] colums={"Name","Price","Category","ISBN","_id"};
        Cursor cursor=db.query("PRODUCT",colums,"Name"+" LIKE '%"+value+"%'",null,null,null,null);
        context.startManagingCursor(cursor);
        while (cursor.moveToNext()){
            Products product=new Products();
            String name=cursor.getString(0);
            product.setName(name);

            String price=cursor.getString(1);
            product.setPrice(Double.parseDouble(price));

            String category=cursor.getString(2);
            product.setCategory(category);


            String isbn=cursor.getString(3);
            product.setIsbn(isbn);

            String id=cursor.getString(4);
            product.setId(id);


            data.add(product);
            //buffer.append(name +  "" +price);

        }



        return  data;


    }
    public String getAllSales(Activity context,String date,String transType){
        SQLiteDatabase db=dataBaseSchema.getWritableDatabase();

        Double amoun=0.0;

        List<Transactions> data=new ArrayList<>();
        String [] colums={"TranstotalAmount"};
        Cursor cursor=db.query("SALESHISTORY",colums,"TransDate = '"+date+"'"+" and TransType = '"+transType+"'",null,null,null,null);
        context.startManagingCursor(cursor);
        while (cursor.moveToNext()){

            String value=cursor.getString(0);
            amoun=amoun+Double.parseDouble(value);
        }


        return String.valueOf(amoun);
    }
    public String getAllData(){
        SQLiteDatabase db=dataBaseSchema.getWritableDatabase();

        String [] colums={"Name","Price"};
        Cursor cursor=db.query("PRODUCT",colums,null,null,null,null,null);
        StringBuffer buffer=new StringBuffer();
        while (cursor.moveToNext()){

            String name = cursor.getString(0);
            String price = cursor.getString(1);
            buffer.append(name + "" + price);

        }

        return buffer.toString();
    }
    public String getItemPrice(String itemName){
        SQLiteDatabase db=dataBaseSchema.getWritableDatabase();
        String price="";
        String [] colums={"Price"};
        Cursor cursor=db.query("PRODUCT",colums,"Name = '"+itemName+"'",null,null,null,null);

        while (cursor.moveToNext()){

            String val=cursor.getString(0);

            price=val;

        }

        return  useful.formatMoney(Double.parseDouble(price));

    }
    public Cursor getAllDataCursor(){
        SQLiteDatabase db=dataBaseSchema.getWritableDatabase();

        String [] colums={"_id","Name","Price"};
        Cursor cursor=db.query("PRODUCT",colums,null,null,null,null,null);


        return  cursor;
    }
    public Cursor getAllDataCursor(String name){
        SQLiteDatabase db=dataBaseSchema.getWritableDatabase();

        String [] colums={"_id","Name","Price"};
        Cursor cursor=db.query("PRODUCT",colums,"Name"+" LIKE '%"+name+"%'",null,null,null,null);


        return  cursor;
    }

    //getAllProductByCategory
    public List<Product> getAllProductByCategory(Activity context,String value){

        SQLiteDatabase db=dataBaseSchema.getWritableDatabase();

        List<Product> data=new ArrayList<>();

        String [] colums={"Name","Price"};
        Cursor cursor = db.query("PRODUCT",colums,"Category"+" = '"+value+"'",null,null,null,null);
        context.startManagingCursor(cursor);
        while (cursor.moveToNext()){
            Product product=new Product();
            String name=cursor.getString(0);
            product.setName(name);
            String price=cursor.getString(1);
            product.setPrice(Double.parseDouble(price));
            data.add(product);
            //buffer.append(name +  "" +price);

        }
             return data;
    }
    public List<Product> getAllProductBeginningWithALetter(Activity context,String value){

        SQLiteDatabase db=dataBaseSchema.getWritableDatabase();

        List<Product> data=new ArrayList<>();

        String [] colums={"Name","Price"};
        Cursor cursor = db.query("PRODUCT",colums,"Name"+" LIKE '"+value+"%'",null,null,null,null);
        context.startManagingCursor(cursor);
        while (cursor.moveToNext()){
            Product product=new Product();
            String name=cursor.getString(0);
            product.setName(name);
            String price=cursor.getString(1);
            product.setPrice(Double.parseDouble(price));
            data.add(product);
            //buffer.append(name +  "" +price);

        }
        return data;
    }
    public List<TransHistory> getAllTransactionHistory(Activity activity){

        SQLiteDatabase db=dataBaseSchema.getWritableDatabase();

        List<TransHistory> data=new ArrayList<>();

/**
 * Date VARCHAR(255)," +
 "Time VARCHAR(255)," +
 "TransID VARCHAR(255)," +
 "SellerID VARCHAR(255)," +
 "MerchantID VARCHAR(255)," +
 "Type VARCHAR(255)," +
 "DetailsID VARCHAR(255)," +
 "Modetype VARCHAR(255)," +
 "Totalsale VARCHAR(225) )";
 *
 * */
        String [] colums={"Date","Time","TransID","SellerID","MerchantID","Type","DetailsID","Modetype","Totalsale"};
        Cursor cursor=db.query("TRANSACTIONS",colums,null,null,null,null,null);
        activity.startManagingCursor(cursor);
        while (cursor.moveToNext()){
            TransHistory th=new TransHistory();

            String date=cursor.getString(0);
            th.setDate(date);

            String time=cursor.getString(1);
            th.setTime(time);

            String transID=cursor.getString(2);
            th.setTransID(transID);

            String sellerID=cursor.getString(3);
            th.setSellerID(sellerID);

            String merchantID=cursor.getString(4);
            th.setMerchantID(merchantID);

            String type=cursor.getString(5);
            th.setType(type);


            String detailsID=cursor.getString(6);
            th.setDetailsID(detailsID);

            String modeType=cursor.getString(7);
            th.setModetype(modeType);


            String totalSale=cursor.getString(8);
            th.setTotalSale(totalSale);
            data.add(th);
        }
        return data;
    }
    public Product getAproduct(Activity context,String isbn){

        Product product = new Product();
        SQLiteDatabase db=dataBaseSchema.getWritableDatabase();

        String [] colums={"NAME","Price"};
        Cursor cursor=db.query("PRODUCT",colums,"ISBN"+" = '"+isbn+"'",null,null,null,null);
        context.startManagingCursor(cursor);
        while (cursor.moveToNext()){
            // Product product=new Product();
            String name=cursor.getString(0);
            product.setName(name);
            String price=cursor.getString(1);
            product.setPrice(Double.parseDouble(price));
            //data.add(product);
            //buffer.append(name +  "" +price);

        }

        return  product;
    }
    public Product getA_id(Activity context,String isbn){

        Product product = new Product();
        SQLiteDatabase db=dataBaseSchema.getWritableDatabase();

        String [] colums={"NAME","Price"};
        Cursor cursor=db.query("PRODUCT",colums,"_id"+" = '"+isbn+"'",null,null,null,null);
        context.startManagingCursor(cursor);
        while (cursor.moveToNext()){
           // Product product=new Product();
            String name=cursor.getString(0);
            product.setName(name);
            String price=cursor.getString(1);
            product.setPrice(Double.parseDouble(price));
            //data.add(product);
            //buffer.append(name +  "" +price);

        }

        return  product;
    }
    public Products getAproductByName(Activity context,String isbn){
/*
*  public Product getAproduct(Activity context,String isbn){

        Product product = new Product();
        SQLiteDatabase db=dataBaseSchema.getWritableDatabase();

        String [] colums={"NAME","Price"};
        Cursor cursor=db.query("PRODUCT",colums,"ISBN"+" = '"+isbn+"'",null,null,null,null);
        context.startManagingCursor(cursor);
        while (cursor.moveToNext()){
           // Product product=new Product();
            String name=cursor.getString(0);
            product.setName(name);
            String price=cursor.getString(1);
            product.setPrice(Double.parseDouble(price));
            //data.add(product);
            //buffer.append(name +  "" +price);

        }

        return  product;
    }
*
*
* */
        Products product = new Products();
        SQLiteDatabase db=dataBaseSchema.getWritableDatabase();

        String [] colums={"NAME","Price"};
        Cursor cursor=db.query("PRODUCT",colums,"NAME"+" = '"+isbn+"'",null,null,null,null);
        context.startManagingCursor(cursor);
        while (cursor.moveToNext()){
            // Product product=new Product();
            String name=cursor.getString(0);
            product.setName(name);
            String price=cursor.getString(1);
            product.setPrice(Double.parseDouble(price));
            //data.add(product);
            //buffer.append(name +  "" +price);

        }

        return  product;
    }
    public int UpdateProducts(Activity context,Products products,String id){


        SQLiteDatabase db=dataBaseSchema.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("ISBN",products.getIsbn());
        contentValues.put("Name",useful.capitalize(products.getName()) );
        contentValues.put("Price", products.getPrice());
        contentValues.put("Category", products.getCategory());
        String [] whereArgs={id};
        int count=db.update("PRODUCT",contentValues, "_id  =?" ,whereArgs);


        return count;

    }
    public int UpdateCategory(Activity context,CategoryValues categoryValues,String id){


        SQLiteDatabase db=dataBaseSchema.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("Name",categoryValues.name );
        String [] whereArgs={id};
        int count=db.update("CATEGORY",contentValues, "_id  =?" ,whereArgs);


        return count;

    }
    public int deleteProduct(Activity context,String id){
        SQLiteDatabase db=dataBaseSchema.getWritableDatabase();

        String [] whereArgs={id};
        int count=db.delete("PRODUCT", "_id  =?", whereArgs);


        return count;
    }


    public int deleteFavorite(Activity context,String name){
        SQLiteDatabase db=dataBaseSchema.getWritableDatabase();

        String [] whereArgs={name};
        int count=db.delete("FAVPRODUCT", "NAME  = ?", whereArgs);


        return count;
    }

    public int deleteCategory(Activity context,String id){
        SQLiteDatabase db=dataBaseSchema.getWritableDatabase();

        String [] whereArgs={id};
        int count=db.delete("CATEGORY", "_id  =?", whereArgs);


        return count;
    }
    public Products getAproducts(Activity context,String value){

        Products product = new Products();
        SQLiteDatabase db=dataBaseSchema.getWritableDatabase();

        String [] colums={"Name","Price","Category","ISBN","_id"};
        Cursor cursor=db.query("PRODUCT",colums,"_id"+" = '"+value+"'",null,null,null,null);
        context.startManagingCursor(cursor);
        while (cursor.moveToNext()){

            String name=cursor.getString(0);
            product.setName(name);

            String price=cursor.getString(1);
            product.setPrice(Double.parseDouble(price));

            String category=cursor.getString(2);
            product.setCategory(category);

            String isbn=cursor.getString(3);
            product.setIsbn(isbn);

            String id=cursor.getString(4);
            product.setId(id);

        }

        return  product;

    }
    public CategoryValues getCategoryValues(Activity context,String id){
        CategoryValues categoryValues = new CategoryValues();
        SQLiteDatabase db=dataBaseSchema.getWritableDatabase();

        String [] colums={"_id","Name"};
        Cursor cursor=db.query("CATEGORY",colums,"_id"+" = '"+id+"'",null,null,null,null);
        context.startManagingCursor(cursor);
        while (cursor.moveToNext()){

            String cid=cursor.getString(0);
            categoryValues.id=cid;
            String name=cursor.getString(1);
            categoryValues.name=name;
            categoryValues.selected=false;


        }
        return categoryValues;

    }
    public List<Product> getAProductByIsbn(Activity context,String value){
        SQLiteDatabase db=dataBaseSchema.getWritableDatabase();

        List<Product> data=new ArrayList<>();

        String [] colums={"Name","Price"};
        Cursor cursor=db.query("PRODUCT",colums,"ISBN"+" LIKE '%"+value+"%'",null,null,null,null);
        context.startManagingCursor(cursor);
        while (cursor.moveToNext()){
            Product product=new Product();
            String name=cursor.getString(0);
            product.setName(name);
            String price=cursor.getString(1);
            product.setPrice(Double.parseDouble(price));
            data.add(product);
            //buffer.append(name +  "" +price);

        }
        return data;


    }
    public List<Product> getAllProduct(Activity context,String value){

        SQLiteDatabase db=dataBaseSchema.getWritableDatabase();

        List<Product> data=new ArrayList<>();

        String [] colums={"Name","Price"};
        Cursor cursor=db.query("PRODUCT",colums,"Name"+" LIKE '%"+value+"%'",null,null,null,null);
        context.startManagingCursor(cursor);
        while (cursor.moveToNext()){
            Product product=new Product();
            String name=cursor.getString(0);
            product.setName(name);
            String price=cursor.getString(1);
            product.setPrice(Double.parseDouble(price));
            data.add(product);
            //buffer.append(name +  "" +price);

        }
        return data;
    }
    public List<Product> checkIfProduct(Activity context,String value){

        SQLiteDatabase db=dataBaseSchema.getWritableDatabase();

        List<Product> data=new ArrayList<>();

        String [] colums={"Name","Price"};
        Cursor cursor=db.query("PRODUCT",colums,"Name"+" LIKE '%"+value+"%'",null,null,null,null);
        context.startManagingCursor(cursor);
        while (cursor.moveToNext()){
            Product product=new Product();
            String name=cursor.getString(0);
            product.setName(name);
            String price=cursor.getString(1);
            product.setPrice(Double.parseDouble(price));
            data.add(product);
            //buffer.append(name +  "" +price);

        }
        return data;
    }
}
