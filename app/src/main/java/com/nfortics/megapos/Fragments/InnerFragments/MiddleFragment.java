package com.nfortics.megapos.Fragments.InnerFragments;


import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.nfortics.megapos.DataBase.DataSets;
import com.nfortics.megapos.Helper.Message;
import com.nfortics.megapos.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MiddleFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    Communicator communicator;
    EditText isbnEdit,nameEdit,priceEdit;
    Button butSave,butCategory;
    Spinner spinner;
    DataSets dataSets;
    String ISBN,name,price,category;

    public MiddleFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        communicator=(Communicator)activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout=inflater.inflate(R.layout.fragment_middle, container, false);

        dataSets=new DataSets(getActivity());

        isbnEdit=(EditText)layout.findViewById(R.id.edtIsbn);
        nameEdit=(EditText)layout.findViewById(R.id.edtName);
        priceEdit=(EditText)layout.findViewById(R.id.edtPrice);

        butSave=(Button)layout.findViewById(R.id.butSave);
        butCategory=(Button)layout.findViewById(R.id.butCategory);
        spinner=(Spinner)layout.findViewById(R.id.CategorySpinner);

        butSave.setOnClickListener(this);
        butCategory.setOnClickListener(this);

        spinner.setOnItemSelectedListener(this);
        return layout;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.butSave:

                ISBN=isbnEdit.getText().toString();
                name=nameEdit.getText().toString();
                price=priceEdit.getText().toString();
                //category=isbnEdit.getText().toString();

                if(ISBN.length()<=0 ||name.length()<=0 ||price.length()<=0){
                    Message.message(getActivity(),"Detected empty Fields");

                }else{

               long id= dataSets.insertITEMS(
                        ISBN,
                        name,
                        price,
                        category);

            if(id<0){
                Message.message(getActivity(),"Error occured");

            }else{

                Message.message(getActivity(), "SUCCESS");

                isbnEdit.setText("");
                nameEdit.setText("");
                priceEdit.setText("");

                //communicator.populateListView();
             //  Message.message(getActivity(),dataSets.getAllData());

            }}
                break;

            case R.id.butCategory:

                break;

        }


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        category=parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


   /** public void populateListView(ListView listView){
        Cursor cursor=dataSets.getAllDataCursor();

        getActivity().startManagingCursor(cursor);

        //Adapter Cursor...

        //Setting up Mapping
        String[] fields={"Name","Price"};

        int viewID[]={R.id.txtItemName,R.id.txtItemprice};

        SimpleCursorAdapter mycursorAdapter=
                new SimpleCursorAdapter(
                   getActivity(),
                        R.layout.found_items_rows,
                         cursor,
                        fields,
                        viewID
                );



        ListView foundlist=(ListView)getActivity().findViewById(R.id.ItemsListView);
        foundlist.setAdapter(mycursorAdapter);

    }**/


    public static interface Communicator{

        public void populateListView();

    }

}
