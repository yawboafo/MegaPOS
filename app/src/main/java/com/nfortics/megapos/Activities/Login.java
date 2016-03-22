package com.nfortics.megapos.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.nfortics.megapos.Application.MyApplication;
import com.nfortics.megapos.DataBase.DataBaseSchema;
import com.nfortics.megapos.Helper.ControlHelpers;
import com.nfortics.megapos.Helper.Message;
import com.nfortics.megapos.Helper.Typefacer;
import com.nfortics.megapos.Network.ConnectionDetector;
import com.nfortics.megapos.Network.VolleySingleton;
import com.nfortics.megapos.R;
import com.nfortics.megapos.ThirrtySevenSignals.RootAccess;
import com.nfortics.megapos.utils.GsonMan;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.util.HashMap;
import java.util.Map;


public class Login extends Activity implements View.OnClickListener {
    TextView
            TxtOzimbo,
            TxtresetPassword;

    Button
            signInbutton;

    EditText
            emailValueText,
            passwordValueText,
            domainEditText;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    ConnectionDetector cd ;
    RootAccess rootAccess;
    GsonMan gsonMan;
    JSONObject jsonObject = new JSONObject();
    BufferedWriter writer;
    DataBaseSchema dataBaseSchema;

    ControlHelpers controlHelpers;
  //  public static  String username;
    //private final String url="http://api.androidhive.info/volley/person_object.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            setContentView(R.layout.activity_login);
            InitializeView();
            InitializeNetworkAdapters();
            rootAccess=new RootAccess();
            gsonMan=new GsonMan();
            controlHelpers=new ControlHelpers();
            dataBaseSchema= new DataBaseSchema(MyApplication.getAppContext());
            SQLiteDatabase db=dataBaseSchema.getWritableDatabase();
            dataBaseSchema.onCreate(db);
        }catch(Exception e){

        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btSignIn:

                Intent intent = new Intent(MyApplication.getAppContext(), Home.class);
                intent.putExtra("UserDetails","yawboafo77@gmail.com");

                startActivity(intent);
               /** if(rootAccess.formFillsCheck(domainEditText.getText().toString(),
                        emailValueText.getText().toString(),
                        passwordValueText.getText().toString()))
                {

                    startActivity(new Intent(MyApplication.getAppContext(), Home.class));
                }  else if(domainEditText.getText().equals("000")&&emailValueText.getText().equals("000")&&passwordValueText.getText().equals("000")){
                    Intent intent = new Intent(MyApplication.getAppContext(), Home.class);
                    intent.putExtra("UserDetails","yawboafo77@gmail.com");

                    startActivity(intent);

                }else {

                    SignIn(domainEditText.getText().toString(),
                            emailValueText.getText().toString(),
                            passwordValueText.getText().toString());

                }***/
                break;

            default:
                //Memory leaked
                break;

        }
    }
    public void InitializeView(){


        try{

            Typefacer typefacer = new Typefacer();

            TxtOzimbo = (TextView) findViewById(R.id.txtozimbo);
            TxtresetPassword = (TextView) findViewById(R.id.txtResetPassword);

            ///EditTextBox
            emailValueText = (EditText) findViewById(R.id.edtEmail);
            passwordValueText = (EditText) findViewById(R.id.edtPassword);
            domainEditText = (EditText) findViewById(R.id.edtDomainName);

            //Buttons

            signInbutton = (Button) findViewById(R.id.btSignIn);
            // Loading Font Face
            // roboMediumitalic = Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-MediumItalic.ttf");
            // roboThin = Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-ThinItalic.ttf");
            //  roboCondensed = Typeface.createFromAsset(this.getAssets(), "fonts/RobotoCondensed-Light.ttf");
            // roboCondensedItalic = Typeface.createFromAsset(this.getAssets(), "fonts/RobotoCondensed-LightItalic.ttf");

            //TxtOzimbo.setTypeface(roboCondensed);
            TxtOzimbo.setTypeface(typefacer.getRoboCondensedRegular(getAssets()));

            //emailValueText.setTypeface(roboThin);
            emailValueText.setTypeface(typefacer.getRoboCondensedRegular(getAssets()));

            //passwordValueText.setTypeface(roboThin);
            passwordValueText.setTypeface(typefacer.getRoboCondensedRegular(getAssets()));

            // passwordValueText.setTypeface(roboThin);
         //   passwordValueText.setTypeface(typefacer.getRobThin(getAssets()));


            //signInbutton.setTypeface(roboCondensed);
            signInbutton.setTypeface(typefacer.getRoboCondensedRegular(getAssets()));

            // TxtresetPassword.setTypeface(roboCondensedItalic);

            TxtresetPassword.setTypeface(typefacer.getRoboCondensedRegular(getAssets()));

            // domainEditText.setTypeface(roboThin);
            domainEditText.setTypeface(typefacer.getRoboCondensedRegular(getAssets()));

            signInbutton.setOnClickListener(this);



        }catch (Exception ex){

            Toast.makeText(this,""+ex.toString(),Toast.LENGTH_LONG);



        }

    }
    public boolean validateInputFields(EditText domain,EditText username,EditText password){

        if(domain.getText().toString().length()!=0
                & username.getText().toString().length()!=0
                & password.getText().toString().length()!=0
                ){

            return true;

        }
            return false;



    }
    public void InitializeNetworkAdapters(){

        try{

            cd = new ConnectionDetector(getApplicationContext());
            volleySingleton=VolleySingleton.getsInstance();
            requestQueue=VolleySingleton.getRequestQueue();
        }catch(Exception ex){

        }

    }
    public void SignIn(String domain,String username,String password){

        Boolean isInternetPresent = cd.isConnectingToInternet();


        if(validateInputFields(
                domainEditText,
                emailValueText,
                passwordValueText)){
            if(isInternetPresent){

                final  ProgressDialog pDialog  = new ProgressDialog(this);
                pDialog.setMessage("Please  wait...");
                pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pDialog.setIndeterminate(true);
                pDialog.setCancelable(false);
                pDialog.show();

                HashMap<String, String> params = new HashMap<String, String>();

                params.put("Domain", domainEditText.getText().toString().trim());
                params.put("Phonenumber", emailValueText.getText().toString().trim());
                params.put("Password", passwordValueText.getText().toString().trim());

               // Message.messageShort(MyApplication.getAppContext(), params.toString());

                volleySingleton=VolleySingleton.getsInstance();
                requestQueue=VolleySingleton.getRequestQueue();
                JsonObjectRequest request = new JsonObjectRequest(
                        Request.Method.POST,

                        MyApplication.getLoginURL(),

                        new JSONObject(params),

                        new Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {


                                VolleyLog.d("Response Login",response.toString());
                                pDialog.hide();
                                  emailValueText.getText().toString();

                                try {
                                   gsonMan.writeJson(response.toString(), emailValueText.getText().toString() + ".json", MyApplication.getAppContext());

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                Intent intent = new Intent(MyApplication.getAppContext(), Home.class);
                                  intent.putExtra("UserDetails", emailValueText.getText().toString());
                                try {
                                    Message.messageShort(MyApplication.getAppContext(),gsonMan.deserialieJson(response.toString()).getName() );
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                startActivity(intent);

                            }
                        }, new ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                      //  controlHelpers.AnAler,"Connection Failed !","Sorry Try Again").show();

                        VolleyLog.d("Error Login"," "+error.toString());
                    }
                }) {

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        headers.put("User-agent", "Ozinbo");
                        return headers;
                    }
                };

               // Message.messageShort(getApplicationContext(),  request.toString());
                requestQueue.add(request);

            }

            /**else if(gsonMan.getOffLineMerchat( emailValueText.getText().toString(), MyApplication.getAppContext())) {

                Intent intent = new Intent(MyApplication.getAppContext(), Home.class);
                intent.putExtra("UserDetails", emailValueText.getText().toString());

                startActivity(intent);
**/

        //}
        else if(!isInternetPresent &&  gsonMan.getOffLineMerchat( emailValueText.getText().toString(), MyApplication.getAppContext())){

               // AlertingMessages();
              // Message.messageShort(getApplicationContext(),"yes exist");
                Intent intent = new Intent(MyApplication.getAppContext(), Home.class);
                intent.putExtra("UserDetails", emailValueText.getText().toString());

                startActivity(intent);
            }else{

                controlHelpers.AnAlertertDialog(this,"No Internet Connections \nAnd You dont have an Offline Account","Error").show();
               // Message.messageShort(getApplicationContext(),"No Internet Connections \nAnd You dont have an Offline Account");
            }



        }

        else{

            Message.messageShort(getApplicationContext(), "Fill All Input Fields");

        }








    }


}
