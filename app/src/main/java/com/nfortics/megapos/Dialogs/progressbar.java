package com.nfortics.megapos.Dialogs;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.nfortics.megapos.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class progressbar extends Fragment {

    Button
     Startdownload;ImageView imageView ;

    public progressbar() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_progressbar, container, false);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // Startdownload=(Button)
    }
}
