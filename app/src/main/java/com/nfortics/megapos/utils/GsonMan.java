package com.nfortics.megapos.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nfortics.megapos.Domain.BankModel;
import com.nfortics.megapos.Domain.BillerModel;
import com.nfortics.megapos.Domain.LoginModel;
import com.nfortics.megapos.Domain.TelcoModel;
import com.nfortics.megapos.Helper.Message;
import com.nfortics.megapos.Models.SalesItemsRow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by bigifre on 7/29/2015.
 */
public class GsonMan {
    JSONObject jsonObject = new JSONObject();
    BufferedWriter writer;
    Gson gson = new Gson();
    LoginModel  merchant;
    String jsonStruture="";

     public boolean makeJsonFile(String FilePath,String fileName,Context ctx){

         try{
             FileWriter writer=new FileWriter(ctx.getExternalFilesDir("Logins"));
             writer.write(fileName);
             writer.close();

            return  true;

        }catch(Exception ex){


             Message.messageShort(ctx,ex.toString());

           return  false;
        }





    }
     LoginModel obj;
    public String readJsonFile(String fileName,Context ctx){

         try{

             File cachDir=ctx.getExternalCacheDir();
             File filenew = new File(cachDir,fileName);

             BufferedReader br=new BufferedReader(new FileReader(filenew));

              obj=gson.fromJson(br, LoginModel.class);
             // Message.message(ctx, "file pathe Name " + br);


         } catch(Exception ex){



         }

         return obj.getName().toString();

     }
    public String readGsonFile(String filename){

        String result = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            result = sb.toString();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return result;

    }
    public String readJsonFromExternalFile(String file){


        String name="";


        String jsonData = readGsonFile(file);
        try {
            JSONObject jobj = new JSONObject(jsonData);
            name=jobj.getString("name").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return name;
    }
    public LoginModel getMerchantFromLogin(String file){


        String name="";
        String code="";
        obj=new LoginModel();
        List<BankModel> bankModelList=new ArrayList<>();
        List<BillerModel> billerModelList=new ArrayList<>();
        List<TelcoModel> telcosModelList=new ArrayList<>();
        String jsonData = readGsonFile(file);
        try {
            JSONObject jobj = new JSONObject(jsonData);

            obj=gson.fromJson(jsonData, LoginModel.class);
            name=jobj.getString("name").toString();
            obj.setName(name);
            code=jobj.getString("id");
            obj.setId(code);
            JSONArray banksObject = new JSONArray(jobj.getJSONArray("banks").toString());
            for(int i = 0; i < banksObject.length(); i++) {
                BankModel bankModel=new BankModel();
                JSONObject object = banksObject.getJSONObject(i);
                bankModel.setCode(object.getInt("code"));
                bankModel.setName(object.getString("name"));
                bankModelList.add(bankModel);
                //obj.setName(object.getString("code"));
            }
            obj.setBanks(bankModelList);

            JSONArray billerObject = new JSONArray(jobj.getJSONArray("billers").toString());
            for(int i = 0; i < billerObject.length(); i++) {
                BillerModel billerModel=new BillerModel();
                JSONObject object = billerObject.getJSONObject(i);
                billerModel.setCode(object.getInt("code"));
                billerModel.setName(object.getString("name"));
                billerModelList.add(billerModel);
                //obj.setName(object.getString("code"));
            }
            obj.setBillers(billerModelList);

            JSONArray telcoObject = new JSONArray(jobj.getJSONArray("telcos").toString());
            for(int i = 0; i < telcoObject.length(); i++) {
                TelcoModel telcoModel=new TelcoModel();
                JSONObject object = telcoObject.getJSONObject(i);
                telcoModel.setCode(object.getInt("code"));
                telcoModel.setName(object.getString("name"));
                telcosModelList.add(telcoModel);
                //obj.setName(object.getString("code"));
            }
            obj.setTelcos(telcosModelList);



        } catch (Exception e) {
            e.printStackTrace();
        }


        return obj;
    }
    public LoginModel getMerchantDetails(String fileName,Context ctx){


        try{

            if(getExternalFile(ctx, fileName + ".json")!=null){

                String fileLoc=getExternalFile(ctx, fileName + ".json");
                merchant=getMerchantFromLogin(fileLoc);

                // Message.message(getApplicationContext()," Final  Name : "  +anotherValue);
            }


        }catch(Exception ex){

            Log.d(" error ", ex.toString());

        }


        return merchant;
    }
    public boolean getOffLineMerchat(String fileName,Context ctx){

        boolean val=false;

        try{
        File i=new File(getExternalFile(ctx, fileName + ".json"));
        if(i.exists()){


            return true;
        }

        }catch (Exception e){

           // val=false;

        }


        return false;

    }
    public String getExternalFile(Context ctx,String filename){

        return ctx.getExternalFilesDir(null).getPath() + File.separator+filename;
    }
    public String merChantJsonStructure(LoginModel loginModel){


        try{

            jsonStruture= gson.toJson(loginModel);

        }catch(Exception ex){}



        return jsonStruture;
    }
    public LoginModel merChantString(String json){

        LoginModel loginModel=null;
        try{

            loginModel=gson.fromJson(json, LoginModel.class);

        }catch(Exception ex){



        }



        return  loginModel;
    }
    public LoginModel deserialieJson(String cal) throws JSONException {


        JSONObject obj = new JSONObject(cal);
        LoginModel logmodel=new LoginModel();
        String id = (String) obj.get("Id");
        logmodel.setId(id);
        String name = (String) obj.get("Name");
        logmodel.setName(name);

        /** JSONArray banks = obj.getJSONArray("Banks");
       // JSONArray names = (JSONArray) obj.get("Banks");
        List<BankModel> bankModel=new ArrayList<>();
        BankModel bk=new BankModel();
        for(int i=0;i<banks.length();i++){
            JSONObject c = banks.getJSONObject(i);
            int code=c.getInt("code");
            bk.setCode(code);
            String namex=c.getString("name");
            bk.setName(namex);
            bankModel.add(bk);


        }
        logmodel.setBanks(bankModel);


        JSONArray telco = obj.getJSONArray("Telcos");
        // JSONArray names = (JSONArray) obj.get("Banks");
        List<TelcoModel> telcom=new ArrayList<>();
        TelcoModel tc=new TelcoModel();
        for(int i=0;i<telco.length();i++){
            JSONObject c = telco.getJSONObject(i);
            int code=c.getInt("code");
            tc.setCode(code);
            String namex=c.getString("name");
            tc.setName(namex);
            telcom.add(tc);


        }
        logmodel.setTelcos(telcom);

**/

        return logmodel;
    }
    public void writeJson(String val,String fileName,Context ctx) throws IOException {

        // FileWriter jsonFileWriter = new FileWriter(getExternalFilesDir(null), "json.txt");
        File traceFile = new File(ctx.getExternalFilesDir(null), fileName);
        if (!traceFile.exists()){
            traceFile.createNewFile();
            writer = new BufferedWriter(new FileWriter(traceFile));
            writer.write(val);
        }else{
            traceFile.delete();
            writer = new BufferedWriter(new FileWriter(traceFile));
            writer.write(val);
        }



        writer.close();



    }
    public void ReadJson()throws IOException{

     //   JSONParser parser = new JSONParser();

    }



    public List<SalesItemsRow> GsonArraySalesItems(String jsonArray) throws Exception {

        Log.d("oxinbo",   " >>> "+jsonArray);
//[{"Amount":"Ghs 3.00","ItemName":"Item 2","Quantity":"1","RealPrice":"Ghs 3.00"}
    List<SalesItemsRow> salesItemsRows=new ArrayList<>();
        JSONArray SalesObject = new JSONArray(jsonArray);
        for(int i = 0; i < SalesObject.length(); i++) {
            SalesItemsRow salesItemsRow=new SalesItemsRow();
            JSONObject object = SalesObject.getJSONObject(i);
            salesItemsRow.itemName= object.getString("ItemName");
            salesItemsRow.qty=object.getString("Quantity");
            salesItemsRow.price=object.getString("RealPrice");
            salesItemsRow.total=object.getString("Amount");

            Log.d("oxinbo",   "GsonMan TotaL >>> "+  salesItemsRow.total);

            salesItemsRows.add(salesItemsRow);
        }


        return salesItemsRows;
    }

}
