package com.nfortics.megapos.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.m2i.m835.api.BIO;
import org.m2i.m835.api.IBIOConnState;

import com.nfortics.megapos.Helper.Message;
import com.nfortics.megapos.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class mFinance extends ActionBarActivity {

    Button but_activateBio;
    TextView m_status;
    ImageView m_FpImageViewer;
   EditText m_editUserID; //= (EditText)findViewById(R.id.editUserID);
public  int success=0;
    byte[] bTemplate;
    final String TEMPLATE_PATH = "sdcard/template.bin";

    private static BIO m_bio;

    int         m_nUserID, m_nParam, m_nImgWidth, m_nImgHeight;
    long	m_nPassedTime;
    byte[]      m_binImage, m_bmpImage;
    String      m_strPost;
    boolean     m_bCancel, m_bConCapture;



    public void InitializeWidget(){

         but_activateBio=(Button)findViewById(R.id.butActivateBio);
         m_status=(TextView)findViewById(R.id.txtStatus);
          m_FpImageViewer =(ImageView)findViewById(R.id.ivImageViewer);
         m_editUserID=(EditText)findViewById(R.id.editUserID);

        if (m_bio == null){
            m_bio = new BIO(this, m_IConnectionHandler);
        }

        m_binImage = new byte[1024*100];
        m_bmpImage = new byte[1024*100];



}

    public void SetClickListeners(){

        but_activateBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (but_activateBio.getText().toString().equals("Activate Biometric")) {
                    OnOpenDeviceBtn();
                    but_activateBio.setText("Biometric Activated");
                    return;
                }

                if (but_activateBio.getText().toString().equals("Biometric Activated")) {
                    m_FpImageViewer.setImageResource(android.R.color.transparent);


                    OnGetImageBtn();


                }
            }
        });

    }



    public void ThreadRunner(){


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (success == 1) {
                    startAnIntent();

                } else {


                }

            }
        }, 1000);
    }



    public void startAnIntent(){
        Intent intent = new Intent(getBaseContext(), Home.class);
        intent.putExtra("DetectView", 1);
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_finance);
        InitializeWidget();
        SetClickListeners();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_m_finance, menu);
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



    public void OnOpenDeviceBtn()
    {

        String[] w_strInfo = new String[1];

        if (m_bio != null) {
            if (!m_bio.isOpen()) {
                if (m_bio.open()) {
                } else {
                    m_status.setText("Failed init usb!");
                }
            } else {
                if (m_bio.testConnection() == BIO.ERR_SUCCESS) {
                    if (m_bio.getDeviceInfo(w_strInfo) == BIO.ERR_SUCCESS) {
                        m_status.setText("Open Success!\r\nDevice Info : " + w_strInfo[0]);
                       // but_activateBio.setText("Biometric Activated");
                      //  EnableCtrl(true);
                        //m_btnOpenDevice.setEnabled(false);
                        //m_btnCloseDevice.setEnabled(true);
                    } else
                        m_status.setText("Can not connect to device!");
                } else
                    m_status.setText("Can not connect to device!");
            }
        }
    }

    public void OnCloseDeviceBtn()
    {
        m_bio.close();
        SetInitialState();
    }
    public void SetInitialState()
    {
        m_status.setText("Please open device!");
       // m_btnOpenDevice.setEnabled(true);
        //m_btnCloseDevice.setEnabled(false);
        //EnableCtrl(false);
    }
    public void OnEnrollBtn()
    {
        int		w_nRet;
        int[]	w_nState = new int[1];

        if (!m_bio.isOpen())
            return;

        if (!CheckUserID())
            return;

        // Check if fp is exist
        w_nRet = m_bio.getStatus(m_nUserID, w_nState);

        if (w_nRet != BIO.ERR_SUCCESS) {
            m_status.setText(GetErrorMsg(w_nRet));
            return;
        }

        if (w_nState[0] == BIO.GD_TEMPLATE_NOT_EMPTY) {
            m_status.setText("Template is already exist");
            return;
        }

        m_status.setText("Press finger : " + m_nUserID);
       // EnableCtrl(false);
        //m_btnCloseDevice.setEnabled(false);
       // m_btnCancel.setEnabled(true);
        m_bio.SLEDControl(1);
        m_bCancel = false;

        new Thread(new Runnable() {
            int w_nRet, w_nUserID, w_nEnrollStep = 0, w_nGenCount = 3;
            int[] w_nDupID = new int[1];

            @Override
            public void run()
            {

                w_nUserID = m_nUserID;

                while (w_nEnrollStep < w_nGenCount) {
                    m_strPost = String.format("Input finger #%d!", w_nEnrollStep+1);
                    m_FpImageViewer.post(runShowStatus);

                    // Capture
                    if (Capturing() < 0)
                        return;

                    m_strPost = "Release your finger.";
                    m_FpImageViewer.post(runShowStatus);

                    // Create Template
                    w_nRet = m_bio.generate(w_nEnrollStep);

                    if (w_nRet != BIO.ERR_SUCCESS) {
                        if (w_nRet == BIO.ERR_BAD_QUALITY) {
                            m_strPost = "Bad quality. Try Again!";
                            m_FpImageViewer.post(runShowStatus);
                            continue;
                        } else {
                            m_strPost = GetErrorMsg(w_nRet);
                            m_FpImageViewer.post(runShowStatus);
                            m_FpImageViewer.post(runEnableCtrl);
                            return;
                        }
                    }
                    w_nEnrollStep++;
                }

                // Merge
                if (w_nGenCount != 1) {
                    //. Merge Template
                    w_nRet = m_bio.merge(0, w_nGenCount);

                    if (w_nRet != BIO.ERR_SUCCESS) {
                        m_strPost = GetErrorMsg(w_nRet);
                        m_FpImageViewer.post(runShowStatus);
                        m_FpImageViewer.post(runEnableCtrl);
                        return;
                    }
                }

                //. Store template
                w_nRet = m_bio.storeChar(w_nUserID, 0, w_nDupID);

                if (w_nRet != BIO.ERR_SUCCESS) {
                    if (w_nRet == BIO.ERR_DUPLICATION_ID)
                        m_strPost = String.format("Result : Fail\r\nDuplication ID = %d", w_nDupID[0]);
                    else
                        m_strPost = GetErrorMsg(w_nRet);
                } else
                    m_strPost = String.format("Result : Success\r\nTemplate No : %d", m_nUserID);

                m_FpImageViewer.post(runShowStatus);
                m_FpImageViewer.post(runEnableCtrl);
            }
        }).start();
    }













    public void OnIdentifyBtn(){
        if (!m_bio.isOpen())
            return;

      //  EnableCtrl(false);
      //  m_btnCloseDevice.setEnabled(false);
       // m_btnCancel.setEnabled(true);
        m_bio.SLEDControl(1);
        m_bCancel = false;

        m_strPost = "";

        new Thread(new Runnable() {
            int		w_nRet;
            int[]	w_nID = new int[1];
            int[]	w_nLearned = new int[1];

            @Override
            public void run() {

                while (true) {
                    if (m_strPost.isEmpty())
                        m_strPost = "Input your finger.";
                    else
                        m_strPost = m_strPost + "\r\nInput your finger.";
                    m_FpImageViewer.post(runShowStatus);

                    if (Capturing() < 0)
                        return;

                    m_strPost = "Release your finger.";
                    m_FpImageViewer.post(runShowStatus);

                    // Create template
                    m_nPassedTime = SystemClock.elapsedRealtime();
                    w_nRet = m_bio.generate(0);

                    if (w_nRet != BIO.ERR_SUCCESS) {
                        m_strPost = GetErrorMsg(w_nRet);
                        m_FpImageViewer.post(runShowStatus);

                        if (w_nRet == BIO.ERR_CONNECTION)
                            return;
                        else {
                            SystemClock.sleep(1000);
                            continue;
                        }
                    }

                    // Identify
                    w_nRet = m_bio.search(0, 1, BIO.GD_MAX_RECORD_COUNT, w_nID, w_nLearned);
                    m_nPassedTime = SystemClock.elapsedRealtime() - m_nPassedTime;

                    if (w_nRet == BIO.ERR_SUCCESS)
                        m_strPost = String.format("Result : Success\r\nTemplate No : %d, Learn Result : %d\r\nMatch Time : %dms", w_nID[0], w_nLearned[0], m_nPassedTime);
                    else
                    {
                        m_strPost = String.format("\r\nMatch Time : %dms", m_nPassedTime);
                        m_strPost = GetErrorMsg(w_nRet) + m_strPost;
                    }
                }
            }
        }).start();
    }

    public void OnVerifyBtn()
    {
        int		w_nRet;
        int[]	w_nState = new int[1];

        if (!m_bio.isOpen())
            return;

        if (!CheckUserID())
            return;

        w_nRet = m_bio.getStatus(m_nUserID, w_nState);

        //dummy Text
        //m_txtDummy.setText(""+w_nState[0]);



        if (w_nRet != BIO.ERR_SUCCESS) {
            m_status.setText(GetErrorMsg(w_nRet));
            return;
        }

        if (w_nState[0] == BIO.GD_TEMPLATE_EMPTY ) {
            m_status.setText("Template is empty");
            return;
        }

        m_status.setText("Press finger");
       // EnableCtrl(false);
       // m_btnCloseDevice.setEnabled(false);
      //  m_btnCancel.setEnabled(true);
        m_bio.SLEDControl(1);
        m_bCancel = false;

        new Thread(new Runnable() {
            int		w_nRet;
            int[]	w_nLearned = new int[1];

            @Override
            public void run()
            {

                if (Capturing() < 0)
                    return;

                m_strPost = "Release your finger.";
                m_FpImageViewer.post(runShowStatus);

                // Create template
                m_nPassedTime = SystemClock.elapsedRealtime();
                w_nRet = m_bio.generate(0);

                if (w_nRet != BIO.ERR_SUCCESS) {
                    m_strPost = GetErrorMsg(w_nRet);
                    m_FpImageViewer.post(runShowStatus);
                    m_FpImageViewer.post(runEnableCtrl);
                    return;
                }

                // Verify
                w_nRet = m_bio.verify(m_nUserID, 0, w_nLearned);
                m_nPassedTime = SystemClock.elapsedRealtime() - m_nPassedTime;

                if (w_nRet == BIO.ERR_SUCCESS)
                    m_strPost = String.format("Result : Success\r\nTemplate No : %d, Learn Result : %d\r\nMatch Time : %dms", m_nUserID, w_nLearned[0], m_nPassedTime);
                else
                    m_strPost = GetErrorMsg(w_nRet);

                m_FpImageViewer.post(runShowStatus);
                m_FpImageViewer.post(runEnableCtrl);
            }
        }).start();
    }


    public void OnDetectFingerBtn()
    {
    }
    public void stureTemplate(){

        try {
            File fd = new File(TEMPLATE_PATH);
            OutputStream wfd = new FileOutputStream(fd);
            wfd.write(bTemplate);
            wfd.flush();
            wfd.close();



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void OnGetImageBtn()
    {
        GetConCaptureState();

        if (m_bConCapture) {
          //  EnableCtrl(false);
            //m_btnCloseDevice.setEnabled(false);
            //m_btnCancel.setEnabled(true);
        }

        //EnableCtrl(false);
        //m_btnCloseDevice.setEnabled(false);
        //m_btnCancel.setEnabled(true);
        m_bio.SLEDControl(1);
        m_bCancel = false;
        m_status.setText("Input finger!");

        new Thread(new Runnable() {
            int		w_nRet;
            int[] 	width = new int[1];
            int[] 	height = new int[1];

            @Override
            public void run()
            {

                if (Capturing() < 0)
                    return;

                w_nRet = m_bio.upImage(0, m_binImage, width, height);

                if (w_nRet != BIO.ERR_SUCCESS) {

                    m_strPost = GetErrorMsg(w_nRet);
                    m_FpImageViewer.post(runShowStatus);
                    m_FpImageViewer.post(runEnableCtrl);
                    success=0;
                    return;
                }
                success=1;
                m_nImgWidth = width[0]; m_nImgHeight = height[0];
                m_strPost = "Get Image OK !"+success;
                m_FpImageViewer.post(runShowStatus);
                m_FpImageViewer.post(runDrawImage);
                m_FpImageViewer.post(runEnableCtrl);

            }
        }).start();


        if (success==1) {
            startAnIntent();

        } else {


        }

    }

    public void OnCancelBtn()
    {
        m_bCancel = true;
    }

    public void OnGetUserCount()
    {

        int w_nRet;
        int[] w_nEnrollCount = new int[1];

        if (!m_bio.isOpen())
            return;

        w_nRet = m_bio.getEnrollCount(1, BIO.GD_MAX_RECORD_COUNT, w_nEnrollCount);

        if (w_nRet != BIO.ERR_SUCCESS) {
            m_status.setText(GetErrorMsg(w_nRet));
            return;
        }

        m_status.setText(String.format("Result : Success\r\nEnroll Count = %d", w_nEnrollCount[0]));
    }

    public void OnGetEmptyID()
    {
        int w_nRet;
        int[] w_nEmptyID = new int[1];

        if (!m_bio.isOpen())
            return;

        w_nRet = m_bio.getEmptyID(1, BIO.GD_MAX_RECORD_COUNT, w_nEmptyID);

        if (w_nRet != BIO.ERR_SUCCESS) {
            m_status.setText(GetErrorMsg(w_nRet));
            return;
        }

        m_status.setText(String.format("Result : Success\r\nEmpty ID = %d", w_nEmptyID[0]));
        m_editUserID.setText(String.format("%d", w_nEmptyID[0]));
    }

    public void OnDeleteIDBtn()
    {
        int	w_nRet;

        if (!m_bio.isOpen())
            return;

        if (!CheckUserID())
            return;

        w_nRet = m_bio.delChar(m_nUserID, m_nUserID);

        if (w_nRet != BIO.ERR_SUCCESS) {
            m_status.setText(GetErrorMsg(w_nRet));
            return;
        }

        m_status.setText("Delete OK !");
    }

    public void OnDeleteAllBtn()
    {
        int		w_nRet;

        if (!m_bio.isOpen())
            return;

        w_nRet = m_bio.delChar(1, BIO.GD_MAX_RECORD_COUNT);

        if (w_nRet != BIO.ERR_SUCCESS) {
            m_status.setText(GetErrorMsg(w_nRet));
            return;
        }

        m_status.setText("Delete all OK !");
    }

  /**  public void OnSetIDNoteBtn()
    {
        int	w_nRet;
        String	w_strNote;

        if (!m_bio.isOpen())
            return;

        if (!CheckUserID())
            return;

        w_strNote = m_editIDNote.getText().toString();

        if(w_strNote == "" ) {
            m_txtStatus.setText("Please input id note!");
            return;
        }

        if (w_strNote.length() > BIO.ID_NOTE_SIZE-1) {
            m_txtStatus.setText(String.format("Please input id note less than %d letters!", BIO.ID_NOTE_SIZE-1));
            return;
        }

        w_nRet = m_bio.setIDNote(m_nUserID, w_strNote);

        if (w_nRet != BIO.ERR_SUCCESS) {
            m_txtStatus.setText(GetErrorMsg(w_nRet));
            return;
        }

        m_txtStatus.setText(String.format("Result : Success"));
    }
**/
    public void OnGetIDNoteBtn()
    {
        int			w_nRet;
        String[]	w_strNote = new String[1];

        if (!m_bio.isOpen())
            return;

        if (!CheckUserID())
            return;

        w_nRet = m_bio.getIDNote(m_nUserID, w_strNote);

        if (w_nRet != BIO.ERR_SUCCESS) {
            m_status.setText(GetErrorMsg(w_nRet));
            return;
        }

        //m_editIDNote.setText(w_strNote[0]);

        m_status.setText(String.format("Result : Success\r\nNote = %s", w_strNote[0]));
    }

  /**  public void OnSetSNBtn()
    {
        int		w_nRet;
        String	w_strModuleSN;

        if (!m_bio.isOpen())
            return;

        w_strModuleSN = m_editModuleSN.getText().toString();

        if (w_strModuleSN == "") {
            m_txtStatus.setText("Please input module sn!");
            return;
        }

        if (w_strModuleSN.length() > BIO.MODULE_SN_LEN-1) {
            m_txtStatus.setText(String.format("Please input module sn less than %d letters!", BIO.MODULE_SN_LEN-1));
            return;
        }

        w_nRet = m_bio.setModuleSN(w_strModuleSN);

        if (w_nRet != BIO.ERR_SUCCESS) {
            m_txtStatus.setText(GetErrorMsg(w_nRet));
            return;
        }

        m_txtStatus.setText(String.format("Result : Success"));
    }
**/
    public void OnGetSNBtn()
    {
        int	 w_nRet;
        String[] w_strModuleSN = new String[1];

        if (!m_bio.isOpen())
            return;

        w_nRet = m_bio.getModuleSN(w_strModuleSN);

        if (w_nRet != BIO.ERR_SUCCESS) {
            m_status.setText(GetErrorMsg(w_nRet));
            return;
        }

        //m_editModuleSN.setText(w_strModuleSN[0]);

        m_status.setText(String.format("Result : Success\r\nSN = %s", w_strModuleSN[0]));
    }

    public void OnSetLevelBtn()
    {
        int		w_nRet;

        if (!m_bio.isOpen())
            return;

        if (!CheckParam(BIO.MIN_SECURITY_LEVEL, BIO.MAX_SECURITY_LEVEL))
            return;

        w_nRet = m_bio.setParam(BIO.DP_SECURITY_LEVEL, m_nParam);

        if (w_nRet != BIO.ERR_SUCCESS) {
            m_status.setText(GetErrorMsg(w_nRet));
            return;
        }

        m_status.setText(String.format("Result : Success"));
    }

    public void OnGetLevelBtn()
    {
        int		w_nRet;
        int[]	w_nParam = new int[1];

        if (!m_bio.isOpen())
            return;

        w_nRet = m_bio.getParam(BIO.DP_SECURITY_LEVEL, w_nParam);

        if (w_nRet != BIO.ERR_SUCCESS) {
            m_status.setText(GetErrorMsg(w_nRet));
            return;
        }

        m_status.setText(String.format("Result : Success\r\nSecurity Level = %d", w_nParam[0]));
    }

    public void OnSetDupCheckBtn()
    {
        int		w_nRet;

        if (!m_bio.isOpen())
            return;

        if (!CheckParam(0, 1))
            return;

        w_nRet = m_bio.setParam(BIO.DP_DUP_CHECK, m_nParam);

        if (w_nRet != BIO.ERR_SUCCESS) {
            m_status.setText(GetErrorMsg(w_nRet));
            return;
        }

        m_status.setText(String.format("Result : Success"));
    }

    public void OnGetDupCheckBtn()
    {
        int		w_nRet;
        int[]	w_nParam = new int[1];

        if (!m_bio.isOpen())
            return;

        w_nRet = m_bio.getParam(BIO.DP_DUP_CHECK, w_nParam);

        if (w_nRet != BIO.ERR_SUCCESS) {
            m_status.setText(GetErrorMsg(w_nRet));
            return;
        }

        m_status.setText(String.format("Result : Success\r\nDuplication Check = %d", w_nParam[0]));
    }

    public void OnSetAutoLearnBtn()
    {
        int		w_nRet;

        if (!m_bio.isOpen())
            return;

        if (!CheckParam(0, 1))
            return;

        w_nRet = m_bio.setParam(BIO.DP_AUTO_LEARN, m_nParam);

        if (w_nRet != BIO.ERR_SUCCESS) {
            m_status.setText(GetErrorMsg(w_nRet));
            return;
        }

        m_status.setText(String.format("Result : Success"));
    }

    public void OnGetAutoLearnBtn()
    {
        int		w_nRet;
        int[]	w_nParam = new int[1];

        if (!m_bio.isOpen())
            return;

        w_nRet = m_bio.getParam(BIO.DP_AUTO_LEARN, w_nParam);

        if (w_nRet != BIO.ERR_SUCCESS) {
            m_status.setText(GetErrorMsg(w_nRet));
            return;
        }

        m_status.setText(String.format("Result : Success\r\nAuto Learn = %d", w_nParam[0]));
    }

    public void OnSetDeviceIDBtn()
    {
        int w_nRet;

        if (!m_bio.isOpen())
            return;

        if (!CheckParam(BIO.MIN_DEVICE_ID, BIO.MAX_DEVICE_ID))
            return;

        w_nRet = m_bio.setParam(BIO.DP_DEVICE_ID, m_nParam);

        if (w_nRet != BIO.ERR_SUCCESS) {
            m_status.setText(GetErrorMsg(w_nRet));
            return;
        }

        m_status.setText(String.format("Result : Success"));
    }

    public void OnGetDeviceIDBtn()
    {
        int		w_nRet;
        int[]	w_nParam = new int[1];

        if (!m_bio.isOpen())
            return;

        w_nRet = m_bio.getParam(BIO.DP_DEVICE_ID, w_nParam);

        if (w_nRet != BIO.ERR_SUCCESS) {
            m_status.setText(GetErrorMsg(w_nRet));
            return;
        }

        m_status.setText(String.format("Result : Success\r\nDevice ID = %d", w_nParam[0]));
    }

    public boolean CheckUserID()
    {
        String str;

        str = m_editUserID.getText().toString();

        if (str == "" ) {
            m_status.setText("Please input user id");
            return false;
        }

        try {
            m_nUserID = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            m_status.setText("Please input correct user id(1~" + BIO.GD_MAX_RECORD_COUNT + ")");
            return false;
        }

        if(m_nUserID > (BIO.GD_MAX_RECORD_COUNT) || m_nUserID < 1) {
            m_status.setText("Please input correct user id(1~" + BIO.GD_MAX_RECORD_COUNT + ")");
            //Toast.makeText(this,BIO.GD_MAX_RECORD_COUNT,Toast.LENGTH_LONG);
            return false;
        }

        return true;
    }

   public boolean CheckParam(int nMin, int nMax)
    {
        String str;

        str = "1";//m_editParam.getText().toString();

        if (str == "" ) {
            m_status.setText("Please input parameter!");
            return false;
        }

        try {
            m_nParam = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            m_status.setText(String.format("Please input parameter (%d~%d)!", nMin, nMax));
            return false;
        }

        if (m_nParam > nMax || m_nParam < nMin) {
            m_status.setText(String.format("Please input correct parameter (%d~%d)!", nMin, nMax));
            return false;
        }

        return true;
    }

    private void GetConCaptureState(){}

    private int Capturing(){
        int		w_nRet;
        while(true){

            w_nRet = m_bio.getImage();

            if (w_nRet == BIO.ERR_CONNECTION)
            {
                m_strPost = "Communication error!";
                m_FpImageViewer.post(runShowStatus);
                m_FpImageViewer.post(runEnableCtrl);
                return -1;
            }
            else if (w_nRet == BIO.ERR_SUCCESS)
                break;

            if (m_bCancel){
                StopOperation();
                return -1;
            }
        }

        return 0;
    }

    private void StopOperation(){
        m_strPost = "Canceled";
        m_FpImageViewer.post(runShowStatus);
        m_FpImageViewer.post(runEnableCtrl);
    }

    private String GetErrorMsg(int nErrorCode)
    {
        String  str = new String("");

        switch(nErrorCode)
        {
            case BIO.ERR_SUCCESS:
                str = "Succcess";
                break;
            case BIO.ERR_VERIFY:
                str = "Verify NG";
                break;
            case BIO.ERR_IDENTIFY:
                str = "Identify NG";
                break;
            case BIO.ERR_EMPTY_ID_NOEXIST:
                str = "Empty Template no Exist";
                break;
            case BIO.ERR_BROKEN_ID_NOEXIST:
                str = "Broken Template no Exist";
                break;
            case BIO.ERR_TMPL_NOT_EMPTY:
                str = "Template of this ID Already Exist";
                break;
            case BIO.ERR_TMPL_EMPTY:
                str = "This Template is Already Empty";
                break;
            case BIO.ERR_INVALID_TMPL_NO:
                str = "Invalid Template No";
                break;
            case BIO.ERR_ALL_TMPL_EMPTY:
                str = "All Templates are Empty";
                break;
            case BIO.ERR_INVALID_TMPL_DATA:
                str = "Invalid Template Data";
                break;
            case BIO.ERR_DUPLICATION_ID:
                str = "Duplicated ID : ";
                break;
            case BIO.ERR_BAD_QUALITY:
                str = "Bad Quality Image";
                break;
            case BIO.ERR_MERGE_FAIL:
                str = "Merge failed";
                break;
            case BIO.ERR_NOT_AUTHORIZED:
                str = "Device not authorized.";
                break;
            case BIO.ERR_MEMORY:
                str = "Memory Error ";
                break;
            case BIO.ERR_INVALID_PARAM:
                str = "Invalid Parameter";
                break;
            case BIO.ERR_GEN_COUNT:
                str = "Generation Count is invalid";
                break;
            case BIO.ERR_INVALID_BUFFER_ID:
                str = "Ram Buffer ID is invalid.";
                break;
            case BIO.ERR_INVALID_OPERATION_MODE:
                str = "Invalid Operation Mode!";
                break;
            case BIO.ERR_FP_NOT_DETECTED:
                str = "Finger is not detected.";
                break;
            default:
                str = String.format("Fail, error code=%d", nErrorCode);
                break;
        }

        return str;
    }

    Runnable runShowStatus = new Runnable() {
        public void run()
        {
            m_status.setText(m_strPost);
        }
    };

    Runnable runDrawImage = new Runnable() {
        public void run()
        {
            int	nSize;

            MakeBMPBuf(m_binImage, m_bmpImage, m_nImgWidth, m_nImgHeight);

            if ((m_nImgWidth % 4) != 0)
                nSize = m_nImgWidth + (4 - (m_nImgWidth % 4));
            else
                nSize = m_nImgWidth;

            nSize = 1078 + nSize * m_nImgHeight;

            Bitmap image = BitmapFactory.decodeByteArray(m_bmpImage, 0, nSize);

            m_FpImageViewer.setImageBitmap(image);
        }
    };

    Runnable runEnableCtrl = new Runnable() {
        public void run()
        {
           // EnableCtrl(true);
            //m_btnOpenDevice.setEnabled(false);
            //m_btnCloseDevice.setEnabled(true);
            m_bio.SLEDControl(0);
        }
    };

    private void   MakeBMPBuf(byte[] Input, byte[] Output, int iImageX, int iImageY)
    {

        byte[] w_bTemp = new byte[4];
        byte[] head = new byte[1078];
        byte[] head2={
                /***************************/
                //file header
                0x42,0x4d,//file type
                //0x36,0x6c,0x01,0x00, //file size***
                0x0,0x0,0x0,0x00, //file size***
                0x00,0x00, //reserved
                0x00,0x00,//reserved
                0x36,0x4,0x00,0x00,//head byte***
                /***************************/
                //infoheader
                0x28,0x00,0x00,0x00,//struct size

                //0x00,0x01,0x00,0x00,//map width***
                0x00,0x00,0x0,0x00,//map width***
                //0x68,0x01,0x00,0x00,//map height***
                0x00,0x00,0x00,0x00,//map height***

                0x01,0x00,//must be 1
                0x08,0x00,//color count***
                0x00,0x00,0x00,0x00, //compression
                //0x00,0x68,0x01,0x00,//data size***
                0x00,0x00,0x00,0x00,//data size***
                0x00,0x00,0x00,0x00, //dpix
                0x00,0x00,0x00,0x00, //dpiy
                0x00,0x00,0x00,0x00,//color used
                0x00,0x00,0x00,0x00,//color important
        };

        int		i,j, num, iImageStep;

        Arrays.fill(w_bTemp, (byte) 0);

        System.arraycopy(head2, 0, head, 0, head2.length);

        if ((iImageX % 4) != 0)
            iImageStep = iImageX + (4 - (iImageX % 4));
        else
            iImageStep = iImageX;

        num=iImageX; head[18]= (byte)(num & (byte)0xFF);
        num=num>>8;  head[19]= (byte)(num & (byte)0xFF);
        num=num>>8;  head[20]= (byte)(num & (byte)0xFF);
        num=num>>8;  head[21]= (byte)(num & (byte)0xFF);

        num=iImageY; head[22]= (byte)(num & (byte)0xFF);
        num=num>>8;  head[23]= (byte)(num & (byte)0xFF);
        num=num>>8;  head[24]= (byte)(num & (byte)0xFF);
        num=num>>8;  head[25]= (byte)(num & (byte)0xFF);

        j=0;
        for (i=54;i<1078;i=i+4) {
            head[i]=head[i+1]=head[i+2]=(byte)j;
            head[i+3]=0;
            j++;
        }

        System.arraycopy(head, 0, Output, 0, 1078);

        if (iImageStep == iImageX){
            for( i = 0; i < iImageY; i ++){
                System.arraycopy(Input, i*iImageX, Output, 1078+i*iImageX, iImageX);
            }
        } else {
            iImageStep = iImageStep - iImageX;

            for( i = 0; i < iImageY; i ++){
                System.arraycopy(Input, i*iImageX, Output, 1078+i*(iImageX+iImageStep), iImageX);
                System.arraycopy(w_bTemp, 0, Output, 1078+i*(iImageX+iImageStep)+iImageX, iImageStep);
            }
        }
    }


    private final IBIOConnState m_IConnectionHandler = new IBIOConnState() {
        @Override
        public void onUsbConnected() {
            String[] w_strInfo = new String[1];

            if (m_bio.testConnection() == BIO.ERR_SUCCESS) {
                if (m_bio.getDeviceInfo(w_strInfo) == BIO.ERR_SUCCESS) {

                    m_status.setText("Open Success!\r\nDevice Info : " + w_strInfo[0]);
                }
            } else
                m_status.setText("Can not connect to device!");
        }

        @Override
        public void onUsbPermissionDenied() {
            m_status.setText("Permission denied!");
        }

        @Override
        public void onDeviceNotFound() {
            m_status.setText("Can not find usb device!");
        }
    };
}
