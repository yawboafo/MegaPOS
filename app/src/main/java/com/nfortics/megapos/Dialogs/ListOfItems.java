package com.nfortics.megapos.Dialogs;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.nfortics.megapos.Adapters.ItemsAdapter;
import com.nfortics.megapos.Fragments.InnerFragments.StartFragment;
import com.nfortics.megapos.Fragments.PosTopLayout;
import com.nfortics.megapos.Helper.Message;
import com.nfortics.megapos.Helper.Typefacer;
import com.nfortics.megapos.Models.Items;
import com.nfortics.megapos.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListOfItems extends DialogFragment {

     ItemsAdapter
            itemsAdapter;
    private RecyclerView
            recyclerView;


    MessageSender communicator;
    Button one;
    public ListOfItems() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        communicator=(MessageSender)activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Typefacer typeface= new Typefacer();

        View layout=inflater.inflate(R.layout.fragment_list_of_items, container, false);

        recyclerView=(RecyclerView)layout.findViewById(R.id.preparedItems2);

        one=(Button)layout.findViewById(R.id.button);

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                communicator.sendMessage();
            }
        });

    //    PosTopLayout startFragment;

       // List<Items> datatmp=new ArrayList<>();
       // datatmp=ItemsAdapter.;

      //  Message.message(getActivity(),""+datatmp.size());



      //  itemsAdapter= new ItemsAdapter(getActivity(),datatmp);
      //  recyclerView.setAdapter(itemsAdapter);
      //  recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
     //   recyclerView.setItemAnimator(new DefaultItemAnimator());


        return  layout;
    }




   public interface  MessageSender{

        public void sendMessage();

    }

}
