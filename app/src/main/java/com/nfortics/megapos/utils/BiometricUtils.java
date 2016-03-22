package com.nfortics.megapos.utils;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.m2i.m835.api.BIO;
import org.m2i.m835.api.IBIOConnState;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class BiometricUtils {

    final String TEMPLATE_PATH = "sdcard/template.bin";

    private static BIO m_bio;

    int         m_nUserID, m_nParam, m_nImgWidth, m_nImgHeight;
    long	    m_nPassedTime;
    byte[]      m_binImage, m_bmpImage;
    String      m_strPost;
    boolean     m_bCancel, m_bConCapture;

    Activity ctx;
    byte[] bTemplate = new byte[1024 * 100];
    EditText 	m_editUserID;
    Button 	   m_btnOpenDevice;
    Button      m_btnCloseDevice;
    Button      m_btnEnroll;
    Button      m_btnVerify;
    Button      m_btnIdentify;
    Button      m_btnCaptureImage;
    TextView 	m_txtStatus;
    ImageView 	m_FpImageViewer;


    public BiometricUtils(Activity ctx,
                          TextView m_txtStatus,
                          ImageView m_FpImageViewer,
                          EditText 	m_editUserID) {


        this.ctx=ctx;
        this.m_txtStatus=m_txtStatus;
        this.m_FpImageViewer=m_FpImageViewer;
        this.m_editUserID=m_editUserID;

        try{

            InitWidget();
        }catch(Exception ex){


        }


    }

    public void InitWidget()
    {


        if (m_bio == null){
            m_bio = new BIO(ctx, m_IConnectionHandler);
        }

        m_binImage = new byte[1024*100];
        m_bmpImage = new byte[1024*100];
        //OnOpenDeviceBtn();
    }



    public void EnableCtrl(boolean bEnable)
    {

    }

    public void SetInitialState( )
    {
        m_txtStatus.setText("Please open device!");

        EnableCtrl(false);
    }

    public void OnOpenDeviceBtn( )
    {
        String[] w_strInfo = new String[1];

        if (m_bio != null) {
            if (!m_bio.isOpen()) {
                if (m_bio.open()) {
                } else {
                    m_txtStatus.setText("Failed init usb!");
                }
            } else {
                if (m_bio.testConnection() == BIO.ERR_SUCCESS) {
                    if (m_bio.getDeviceInfo(w_strInfo) == BIO.ERR_SUCCESS) {
                        m_txtStatus.setText("Open Success!\r\nDevice Info : " + w_strInfo[0]);
                        EnableCtrl(true);
                     // m_btnOpenDevice.setEnabled(false);
                      //  m_btnCloseDevice.setEnabled(true);
                    } else
                        m_txtStatus.setText("Can not connect to device!");
                } else
                    m_txtStatus.setText("Can not connect to device!");
            }
        }
    }

    public void OnCloseDeviceBtn( )
    {
        m_bio.close();
        SetInitialState( );
    }

    public void OnEnrollBtn( )
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
            m_txtStatus.setText(GetErrorMsg(w_nRet));
            return;
        }

        if (w_nState[0] == BIO.GD_TEMPLATE_NOT_EMPTY) {
            m_txtStatus.setText("Template is already exist");
            return;
        }

        m_txtStatus.setText("Press finger : " + m_nUserID);
        EnableCtrl(false);
       // m_btnCloseDevice.setEnabled(false);
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

                w_nRet = m_bio.downChar(w_nUserID, bTemplate);
                if (w_nRet != BIO.ERR_SUCCESS) {
				/* ERROR Helllp */
                } else {
				/* TODO: Store bTemplate into Server */
                    try {
                        File fd = new File(TEMPLATE_PATH);
                        OutputStream wfd = new FileOutputStream(fd);
                        wfd.write(bTemplate);
                        wfd.flush();
                        wfd.close();

					/* For comparing do: */
					/* [1] Get Empty Slot from Device */
                        int[] b_EmptyID = new int[1];
                        m_bio.getEmptyID(5, 10, b_EmptyID);

					/* [2] Store the received template from server to device */
                        m_bio.upChar((byte)b_EmptyID[0], bTemplate);

					/* [3] Match */
                        m_bio.match(w_nUserID, (byte)b_EmptyID[0]);
					/* Check results and others */

					/* [4] delete Bio from device */
                        m_bio.delChar(0, 2000);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                m_FpImageViewer.post(runShowStatus);
                m_FpImageViewer.post(runEnableCtrl);
            }
        }).start();
    }

    public void OnIdentifyBtn(){
        if (!m_bio.isOpen())
            return;

        EnableCtrl(false);
       // m_btnCloseDevice.setEnabled(false);
      //  m_btnCancel.setEnabled(true);
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

        if (w_nRet != BIO.ERR_SUCCESS) {
            m_txtStatus.setText(GetErrorMsg(w_nRet));
            return;
        }

        if (w_nState[0] == BIO.GD_TEMPLATE_EMPTY ) {
            m_txtStatus.setText("Template is empty");
            return;
        }

        m_txtStatus.setText("Press finger");
        EnableCtrl(false);
       // m_btnCloseDevice.setEnabled(false);
       // m_btnCancel.setEnabled(true);
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

    public void OnGetImageBtn()
    {
        GetConCaptureState();

        if (m_bConCapture) {
            EnableCtrl(false);
         //   m_btnCloseDevice.setEnabled(false);
         //   m_btnCancel.setEnabled(true);
        }

        EnableCtrl(false);
       // m_btnCloseDevice.setEnabled(false);
       // m_btnCancel.setEnabled(true);
        m_bio.SLEDControl(1);
        m_bCancel = false;
        m_txtStatus.setText("Input finger!");

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
                    return;
                }

                m_nImgWidth = width[0]; m_nImgHeight = height[0];
                m_strPost = "Get Image OK !";
                m_FpImageViewer.post(runShowStatus);
                m_FpImageViewer.post(runDrawImage);
                m_FpImageViewer.post(runEnableCtrl);
            }
        }).start();
    }





    public boolean CheckUserID()
    {
        String str;

        str = m_editUserID.getText().toString();

        if (str == "" ) {
            m_txtStatus.setText("Please input user id");
            return false;
        }

        try {
            m_nUserID = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            m_txtStatus.setText("Please input correct user id(1~" + BIO.GD_MAX_RECORD_COUNT + ")");
            return false;
        }

        if(m_nUserID > (BIO.GD_MAX_RECORD_COUNT) || m_nUserID < 1) {
            m_txtStatus.setText("Please input correct user id(1~" + BIO.GD_MAX_RECORD_COUNT + ")");
            return false;
        }

        return true;
    }



    private void GetConCaptureState(){}

    private int Capturing()
    {
        int		w_nRet;
        while (true) {

            w_nRet = m_bio.getImage();

            if (w_nRet == BIO.ERR_CONNECTION) {
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
            m_txtStatus.setText(m_strPost);
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
            EnableCtrl(true);
            m_btnOpenDevice.setEnabled(false);
            m_btnCloseDevice.setEnabled(true);
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
                    EnableCtrl(true);
                    m_btnOpenDevice.setEnabled(false);
                    m_btnCloseDevice.setEnabled(true);
                    m_txtStatus.setText("Open Success!\r\nDevice Info : " + w_strInfo[0]);
                }
            } else
                m_txtStatus.setText("Can not connect to device!");
        }

        @Override
        public void onUsbPermissionDenied() {
            m_txtStatus.setText("Permission denied!");
        }

        @Override
        public void onDeviceNotFound() {
            m_txtStatus.setText("Can not find usb device!");
        }
    };
}
