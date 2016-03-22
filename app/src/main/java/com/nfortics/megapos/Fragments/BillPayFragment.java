package com.nfortics.megapos.Fragments;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.nfortics.megapos.Application.MyApplication;
import com.nfortics.megapos.Helper.ControlHelpers;
import com.nfortics.megapos.Helper.Message;
import com.nfortics.megapos.Helper.Useful;
import com.nfortics.megapos.Network.VolleySingleton;
import com.nfortics.megapos.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BillPayFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BillPayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class BillPayFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    Button Submit_Button, Cancel_Button;


    ProgressBar progressBar;
    ControlHelpers controlHelpers;

    Spinner ProviderSpinner;

    Useful useful;
    JSONObject jsonObject;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private OnFragmentInteractionListener mListener;
    String provider,billReferenceString;

    EditText money, billReference;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BillPayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BillPayFragment newInstance(String param1, String param2) {
        BillPayFragment fragment = new BillPayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public BillPayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);

            mParam2 = getArguments().getString(ARG_PARAM2);


        }
        controlHelpers = new ControlHelpers();
        useful = new Useful();
        jsonObject = new JSONObject();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bill_pay, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InitializeView(view);
        InitializeSpinners();
        InitializeClickListeners();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    void InitializeView(View view) {


        money = (EditText) view.findViewById(R.id.editMoney);
        billReference = (EditText) view.findViewById(R.id.editBillreference);
        Submit_Button = (Button) view.findViewById(R.id.butSubmit);
        Cancel_Button = (Button) view.findViewById(R.id.butCancel);
        ProviderSpinner = (Spinner) view.findViewById(R.id.ProviderSpinner);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
    }


    void InitializeClickListeners() {


        Submit_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if(controlHelpers.OptionDialog(getActivity(),"Submit Bill \n Payment ?")==-2)
                provider=ProviderSpinner.getSelectedItem().toString();
                billReferenceString=   billReference.getText().toString();
                PayBills(
                        billReference.getText().toString(),
                        money.getText().toString(),
                        ProviderSpinner.getSelectedItem().toString(),
                        MyApplication.getPayment_Provider(),
                        MyApplication.getApplicationClient(),
                        MyApplication.getApp_name(),
                        MyApplication.getApp_Country(),
                        MyApplication.getApplicationSeriousness() + "");
                Message.messageShort(getActivity(), "No was clicked");
                //  }else{
                //   Message.messageShort(getActivity(),"Yes was clicked");

                // }


            }
        });
        //OptionDialog


    }

    void InitializeSpinners() {
        controlHelpers.makeListForBillersSpinners(getActivity(), ProviderSpinner, MyApplication.getUserName(getActivity()));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    void Dismiss(){
       // dismiss();
        mListener.BillPayPrint("BillPay", provider, billReferenceString, useful.formatMoney(Double.parseDouble(money.getText().toString())));
        //mListener.BillPaymentPrint(confirmLabel1.getText().toString(), BuildStringTopPRINT());
        mListener.BillPayPositiveSecondScreen();
    }
    public void PayBills(String billReference,
                         String amount,
                         String serviceProvider,
                         String provider,
                         String client,
                         String application,
                         String country,
                         String test) {
        final ProgressBar progressBar;
        final HashMap<String, String> params = new HashMap<String, String>();

        progressBar = (ProgressBar) getActivity().findViewById(R.id.progressBar);
        progressBar.setMax(15);
        progressBar.setIndeterminate(true);



        //progressBar.setVisibility(View.VISIBLE);
        params.put("billReference",billReference);
        params.put("amount", amount);
        params.put("serviceProvider", serviceProvider);
        params.put("provider", provider);
        params.put("client", client);
        params.put("application",application);
        params.put("country", country);
        params.put("test", test);


        volleySingleton= VolleySingleton.getsInstance();
        requestQueue=VolleySingleton.getRequestQueue();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                MyApplication.getBillpayUrl(),
                new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //pDialog.hide();
                        try {

                            if(response.getString("success").toString().equals("true")){

                              //  progressBar.setVisibility(View.GONE);
                                Log.d("Response : ", response.toString());
                                 Dismiss();

                            }else if(response.getString("success").toString().equals("false")){


                                controlHelpers.AnAlertertDialog(getActivity(),
                                        response.getString("message").toString(),"Sorry Try Again").show();

                            }

                        } catch (Exception e) {

                            e.printStackTrace();
                        }



                        //  Message.messageShort(MyApplication.getAppContext(),""+tokenValue+"\n"+response.toString()+"\n"+booleaner);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                String ErrorMessage="Connection Failed !";
                String successMessage="ErrorMessage";


                try {
                    VolleyError earror = new VolleyError(new String(error.networkResponse.data));
                    JSONObject jobj = new JSONObject(earror.getMessage().toString());
                    progressBar.setVisibility(View.GONE);
                    ErrorMessage=jobj.getString("message");
                    successMessage=jobj.getString("success");
                    Log.e("oxinbo", earror.getMessage().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }



                controlHelpers.AnAlertertDialog(getActivity(),ErrorMessage,"Sorry Try Again").show();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type","application/json; charset=utf-8");
                headers.put("User-agent", "Ozinbo");
                return headers;
            }
        };
        int socketTimeout = 240000000;//4 minutes - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        requestQueue.add(request);




    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
        public void BillPayPositiveSecondScreen();
        public void BillPayPrint(String name,String provider,String account,String Amount);
    }


}
