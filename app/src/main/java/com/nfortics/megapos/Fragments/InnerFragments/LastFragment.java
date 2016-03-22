package com.nfortics.megapos.Fragments.InnerFragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nfortics.megapos.Helper.CalculatorBrain;
import com.nfortics.megapos.Helper.CurrencyTextWatcher;
import com.nfortics.megapos.Helper.Message;
import com.nfortics.megapos.Helper.TextViewTextWatcher;
import com.nfortics.megapos.Helper.Typefacer;
import com.nfortics.megapos.Helper.Useful;
import com.nfortics.megapos.R;

import java.text.DecimalFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class LastFragment extends Fragment implements View.OnClickListener {
    private TextView mCalculatorDisplay;
    private Boolean userIsInTheMiddleOfTypingANumber = false;
    private CalculatorBrain mCalculatorBrain;
    private static final String DIGITS = "0123456789.";

    EditText display;

    private  int counter;
    Useful useful= new Useful();
    StartFragment.InsertItemsToListView communicator;
    DecimalFormat df = new DecimalFormat("@###########");

     Typefacer typefacer=new Typefacer();

    public LastFragment() {
        // Required empty public constructor
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        communicator=(StartFragment.InsertItemsToListView)activity;
      // Message.message(getActivity(),"lastfragment called ");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view= inflater.inflate(R.layout.calculator_layout, null);
        // hide the window title.




        mCalculatorBrain = new CalculatorBrain();
        mCalculatorDisplay = (TextView)view.findViewById(R.id.textView1);
        mCalculatorDisplay.setTypeface(typefacer.getDigital7normal(getActivity().getAssets()));
        df.setMinimumFractionDigits(0);
        df.setMinimumIntegerDigits(1);
        df.setMaximumIntegerDigits(8);

        view.findViewById(R.id.button0).setOnClickListener(this);
        view.findViewById(R.id.button1).setOnClickListener(this);
        view.findViewById(R.id.button2).setOnClickListener(this);
        view.findViewById(R.id.button3).setOnClickListener(this);
        view.findViewById(R.id.button4).setOnClickListener(this);
        view. findViewById(R.id.button5).setOnClickListener(this);
        view.findViewById(R.id.button6).setOnClickListener(this);
        view.findViewById(R.id.button7).setOnClickListener(this);
        view.findViewById(R.id.button8).setOnClickListener(this);
        view. findViewById(R.id.button9).setOnClickListener(this);

     //   view.findViewById(R.id.buttonAdd).setOnClickListener(this);
       // view. findViewById(R.id.buttonSubtract).setOnClickListener(this);
       // view. findViewById(R.id.buttonMultiply).setOnClickListener(this);
       // view.findViewById(R.id.buttonDivide).setOnClickListener(this);
       // view.findViewById(R.id.buttonToggleSign).setOnClickListener(this);
        view.findViewById(R.id.buttonDecimalPoint).setOnClickListener(this);
        view. findViewById(R.id.buttonEquals).setOnClickListener(this);
        view. findViewById(R.id.buttonClear).setOnClickListener(this);
      //  view. findViewById(R.id.buttonClearMemory).setOnClickListener(this);
       // view. findViewById(R.id.buttonAddToMemory).setOnClickListener(this);
       // view. findViewById(R.id.buttonSubtractFromMemory).setOnClickListener(this);
       // view. findViewById(R.id.buttonRecallMemory).setOnClickListener(this);


        return view;
    }

    StringBuilder stringBuilder=new StringBuilder();
    @Override
    public void onClick(View v) {

        String buttonPressed = ((Button) v).getText().toString();

        if (DIGITS.contains(buttonPressed)) {

            // digit was pressed
            if (userIsInTheMiddleOfTypingANumber) {

                if (buttonPressed.equals(".") && mCalculatorDisplay.getText().toString().contains(".")) {
                    // ERROR PREVENTION
                    // Eliminate entering multiple decimals
                } else {
                    mCalculatorDisplay.append(buttonPressed);
                }

            } else {

                if (buttonPressed.equals(".")) {
                    // ERROR PREVENTION
                    // This will avoid error if only the decimal is hit before an operator, by placing a leading zero
                    // before the decimal
                    mCalculatorDisplay.setText(0 + buttonPressed);
                } else {
                    mCalculatorDisplay.setText(buttonPressed);
                }

                userIsInTheMiddleOfTypingANumber = true;
            }

        } else {

            if(buttonPressed.equals("+")){

                          if(mCalculatorDisplay.getText().length()<=0){

                          }else {

                              String tmpdop = useful.formatMoney(Double.parseDouble(mCalculatorDisplay.getText().toString()));
                              communicator.addToPreparedItems("Item "+1, tmpdop);
                              //counter = counter + 1;
                              // Message.message(getActivity(),"operand");
                              mCalculatorDisplay.setText("");
                          }
            }else if(buttonPressed.equals("C")){

                mCalculatorDisplay.setText("");
            }



        }

    }


}
