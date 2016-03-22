package com.nfortics.megapos.Dialogs;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;

import com.nfortics.megapos.Helper.Message;
import com.nfortics.megapos.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateQuantity extends DialogFragment {

    NumberPicker Setnumberpicker;
    Button Acceptbutton;
    int number,itemPosition,newItemQuantity;
    Communicate communicator;
    public UpdateQuantity() {
        // Required empty public constructor
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        communicator=(Communicate)activity;
        number=this.getArguments().getInt("value");
        itemPosition=this.getArguments().getInt("position");
       // getDialog().setCancelable(false);
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if(manager.findFragmentByTag(tag)==null){
            super.show(manager, tag);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);

        View layout=inflater.inflate(R.layout.fragment_update_quantity, container, false);

        Acceptbutton=(Button)layout.findViewById(R.id.buttAccept);

        Setnumberpicker=(NumberPicker)layout.findViewById(R.id.quantityPicker);
        Setnumberpicker.setMaxValue(100);
        Setnumberpicker.setMinValue(0);
        Setnumberpicker.setWrapSelectorWheel(true);
        Setnumberpicker.setValue(0);
        Setnumberpicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

               // picker.clearFocus();
                // newItemQuantity=picker.getValue();
                //communicator.updateQuantity(newItemQuantity,itemPosition);
                //Message.message(getActivity(), "" + newVal);

            }
        });




        Acceptbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Setnumberpicker.clearFocus();
                newItemQuantity=Setnumberpicker.getValue();
                communicator.updateQuantity(newItemQuantity,itemPosition);
            }
        });

        return layout;
    }



    public interface Communicate{

        public void updateQuantity(int quantity,int position);

    }

}
