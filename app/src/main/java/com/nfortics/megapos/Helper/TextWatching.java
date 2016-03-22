package com.nfortics.megapos.Helper;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by bigifre on 7/15/2015.
 */
public class TextWatching implements TextWatcher {

    private  EditText editText;
    private Spinner spinner;
    public TextWatching(EditText editText,Spinner spinner) {


        this.spinner =spinner;
        this.editText=editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public synchronized void afterTextChanged(Editable s) {

        editText.requestFocus();
        editText.setText("");

    }
}
