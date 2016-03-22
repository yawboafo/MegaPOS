package com.nfortics.megapos.Fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nfortics.megapos.Adapters.CategoryValuesAdapter;
import com.nfortics.megapos.Adapters.ProductAdapter;
import com.nfortics.megapos.Adapters.ProductsAdapter;
import com.nfortics.megapos.DataBase.DataSets;
import com.nfortics.megapos.Dialogs.CreateItemsDialog;
import com.nfortics.megapos.Helper.Message;
import com.nfortics.megapos.Helper.Typefacer;
import com.nfortics.megapos.Models.CategoryValues;
import com.nfortics.megapos.Models.Products;
import com.nfortics.megapos.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Stocking extends Fragment {
    CommunicateWithMainFragment
            communicateWithMainFragment;

    Button Butt_All_Items,
            Butt_All_Category,
            Butt_All_Discount,
            Butt_CreateSomething,butLongDelete;

    ProductsAdapter productsAdapter;
    CategoryValuesAdapter categoryValuesAdapter;
    DataSets dataSets;

    List<String> selectCategoryList;
    List<String> selectItemsList;


    EditText editTextSearch;
    RecyclerView recyclerViewAllItems;
    Typefacer typefacer;

    public Stocking() {
        // Required empty public constructor
    }

   /////>>>>>DEFAULT <<<<//////
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataSets = new DataSets(getActivity());
        selectCategoryList=new ArrayList<>();
        selectItemsList=new ArrayList<>();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        communicateWithMainFragment = (CommunicateWithMainFragment) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stocking, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        try {
            super.onViewCreated(view, savedInstanceState);
            InitializeViews(view);
            SetTypeFacer();
            InitializeClickListeners();
          //  InitializeRecycleViewer();
            EventListenetRecycleView();
            new LoadProductItems().execute();
        } catch (Exception e) {


        }

    }
//////////////////////////////////////////////////

    ////>>FRAGMENT VIEW METHODS<<////
    public void InitializeViews(View view) {

        ///***Initialize Buttons****///

        Butt_All_Items = (Button) view.findViewById(R.id.butAll_items);

        Butt_All_Category = (Button) view.findViewById(R.id.butAll_categories);
        Butt_All_Discount = (Button) view.findViewById(R.id.butAll_discount);
        Butt_CreateSomething = (Button) view.findViewById(R.id.butCreate);
        Butt_CreateSomething.setText("CREATE ITEM");
        butLongDelete = (Button) view.findViewById(R.id.butLongDelete);
        butLongDelete.setVisibility(View.GONE);

        ///***Initialize EditText****///
        editTextSearch = (EditText) view.findViewById(R.id.edtSearch);
        editTextSearch.addTextChangedListener(SearchItemsWatcher);
        ////////****InitializeRecycleView****////
        recyclerViewAllItems = (RecyclerView) view.findViewById(R.id.AllItems);

    }
    public void InitializeClickListeners() {
        Butt_All_Items_ClickListener();
        Butt_All_Category_ClickListener();
      //  Butt_All_Discount_ClickListener();
        Butt_CreateSomething_ClickListener();
    }
   //////////////////////////////////////////////////


    ///>>>BUTTON EVENTLISTENERS<<<</////
    void Butt_All_Items_ClickListener() {
        Butt_All_Items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Butt_CreateSomething_setText("CREATE ITEM");
                editTextSearch.setHint("Search All Items");
                changeSelectedButtonColor(Butt_All_Items);
                new LoadProductItems().execute();
                communicateWithMainFragment.changeTitle("All Items");
            }
        });

    }
    void Butt_All_Category_ClickListener() {
        Butt_All_Category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Butt_CreateSomething_setText("CREATE CATEGORY");
                editTextSearch.setHint("Search All Category");
                changeSelectedButtonColor(Butt_All_Category);
                new LoadProductCategory().execute();
                communicateWithMainFragment.changeTitle("All Categories");
            }
        });

    }
    void Butt_All_Discount_ClickListener() {
        Butt_All_Discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSelectedButtonColor(Butt_All_Discount);
                Butt_CreateSomething_setText("CREATE DISCOUNT");
                editTextSearch.setHint("Search All Category");
                communicateWithMainFragment.changeTitle("All Discounts");
            }
        });

    }
    void Butt_CreateSomething_ClickListener() {
        Butt_CreateSomething.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateItemsDialog createItemsDialog = new CreateItemsDialog();
                switch (Butt_CreateSomething.getText().toString()){


                    case "CREATE ITEM":

                        showADialog(createItemsDialog);
                        break;
                    case "CREATE CATEGORY":

                        showADialog(createItemsDialog.CategoryInstance(true));
                        break;
                }


            }
        });


    }
    void Butt_CreateSomething_setText(String text) {

        Butt_CreateSomething.setText(text);
    }
/////////////////////////////////////////////////////



    /////>>>>CONTROL METHODS<<<<<///////////////
    void changeSelectedButtonColor(Button button) {

        List<Button> buttons = new ArrayList<>();
        buttons.add(Butt_All_Items);
        buttons.add(Butt_All_Category);
        buttons.add(Butt_All_Discount);


        for (Button butt : buttons) {
            if (butt == button) {

                butt.setTextColor(Color.parseColor("#FF0000"));
            } else {

                butt.setTextColor(Color.parseColor("#000000"));

            }

        }

        //button.setTextColor(android.R.color.holo_red_light);

    }
    void showADialog(DialogFragment dialogFragment) {

        FragmentManager manager = getActivity().getSupportFragmentManager();
        // CashPaymentDialog dialog= new CashPaymentDialog();
        DialogFragment dialog = dialogFragment;
        dialog.show(manager, "");

    }
    void SetTypeFacer() {
        typefacer = new Typefacer();
        Butt_All_Items.setTypeface(typefacer.getRoboCondensedBold(getActivity().getAssets()));
        Butt_All_Category.setTypeface(typefacer.getRoboCondensedBold(getActivity().getAssets()));
        Butt_All_Discount.setTypeface(typefacer.getRoboCondensedBold(getActivity().getAssets()));
        Butt_CreateSomething.setTypeface(typefacer.getRoboCondensedBold(getActivity().getAssets()));
    }
    ////////////////////////////////////

    ///<<<<EVENT LISTENERS>>>>>>/////////
    void SetEventListenersRecycleview(){


        recyclerViewAllItems.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                recyclerViewAllItems, new ClickListner() {
            @Override
            public void onClick(View view, int position) {


                String value = Butt_CreateSomething.getText().toString();
                switch (value) {

                    case "CREATE ITEM":


                        TextView hidden = (TextView) view.findViewById(R.id.hiddenID);

                        //  Message.messageShort(getActivity(),value);

                        new GetAproduct(hidden.getText().toString()).execute();
                        break;

                    case "CREATE CATEGORY":

                        TextView hiddena = (TextView) view.findViewById(R.id.txtCateID);
                        //Message.messageShort(getActivity(),hiddena.getText().toString());
                        new GetAcategory(hiddena.getText().toString()).execute();
                        break;


                }

            }

            @Override
            public void onLongClick(View view, int position) {

                /**    String value = Butt_CreateSomething.getText().toString();
                 switch (value) {

                 case "CREATE ITEM":


                 TextView itemname = (TextView) view.findViewById(R.id.txtProductName);
                 String astring = itemname.getText().toString();
                 // selectItemsList.add(astring);
                 IterateThingsToDelete(astring);
                 itemname.setTextColor(Color.parseColor("#FF0000"));
                 butLongDelete.setVisibility(View.VISIBLE);
                 butLongDelete.setText("Delete (" + selectItemsList.size() + ")");
                 break;

                 case "CREATE CATEGORY":

                 break;


                 }**/

            }
        }));
    }
    boolean IterateThingsToDelete(String cal){

         boolean isTrue=false;
        for (Iterator<String> iterator = selectItemsList.iterator(); iterator.hasNext();) {
            String string = iterator.next();
            if (string.equals(cal)) {
                // Remove the current element from the iterator and the list.


            }else{


                selectItemsList.add(cal);
            }
        }




        return isTrue;
    }
    public void InitializeRecycleViewerCatgory(){

        new LoadProductCategory().execute();
    }
    public void InitializeRecycleViewer() {


       new LoadProductItems().execute();



    }
    public void EventListenetRecycleView(){
        List<Products>  data= Collections.emptyList();
        productsAdapter = new ProductsAdapter(getActivity(), data);
        recyclerViewAllItems.setAdapter(productsAdapter);
        recyclerViewAllItems.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewAllItems.setItemAnimator(new DefaultItemAnimator());
        SetEventListenersRecycleview();

    }
    ///////////////////////


               ////<<<<INTERFACES<<<//////

    public static interface CommunicateWithMainFragment {
        public void changeTitle(String title);

    }
    public static interface ClickListner {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    ////<<<>>>>TEXTWATCHER/////////////////////

    private TextWatcher SearchItemsWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

                      try{


                       switch (Butt_CreateSomething.getText().toString()){

                              case "CREATE ITEM":

                              new SearchProduct(s.toString()).execute();
                                 break;
                              case "CREATE CATEGORY":

                                  new SearchCategory(s.toString()).execute();
                               break;
                          }
                   }catch (Exception e){


}


        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
////////////////////////////

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListner clickListner;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListner clickListner) {

            this.clickListner = clickListner;

            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent e) {

                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());

                    if (child != null & clickListner != null) {


                        clickListner.onClick(child, recyclerView.getChildPosition(child));

                    }

                    return super.onSingleTapUp(e);
                }

                @Override
                public void onLongPress(MotionEvent e) {


                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());

                    if (child != null & clickListner != null) {


                        clickListner.onLongClick(child, recyclerView.getChildPosition(child));

                    }
                    // super.onLongPress(e);
                }
            });


        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());

            if (child != null & clickListner != null && gestureDetector.onTouchEvent(e)) {


                clickListner.onClick(child, rv.getChildPosition(child));

            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }


    ///>>>>ASYNC  TASK<<<</////
    class LoadProductItems extends AsyncTask<Void, Void, Void> {


        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());

            pDialog.setMessage("Loading...");
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
try{

    productsAdapter = new ProductsAdapter(getActivity(), dataSets.getAllProducts(getActivity()));

      }catch(Exception e){

                e.printStackTrace();
            }


            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            recyclerViewAllItems.setAdapter(productsAdapter);
            recyclerViewAllItems.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerViewAllItems.setItemAnimator(new DefaultItemAnimator());
           // SetEventListenersRecycleview();
            pDialog.hide();
        }
    }
    class LoadProductCategory extends AsyncTask<Void, Void, Void> {


        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());

            pDialog.setMessage("Loading...");
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            categoryValuesAdapter=new CategoryValuesAdapter(getActivity(),dataSets.getCategoryValues(getActivity()));

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            recyclerViewAllItems.setAdapter(categoryValuesAdapter);
            recyclerViewAllItems.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerViewAllItems.setItemAnimator(new DefaultItemAnimator());

            pDialog.hide();
        }
    }
    class GetAproduct extends AsyncTask<Void,Void,Void>{


        String id;
        Products products;

        public GetAproduct(String id){
            this.id=id;
        }

        @Override
        protected Void doInBackground(Void... params) {
            products=dataSets.getAproducts(getActivity(),id);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            CreateItemsDialog createItemsDialog = new CreateItemsDialog();
            showADialog(createItemsDialog.newInstance(products.getIsbn(), products.getName(), products.getCategory(), products.getPrice() + "", id));
        }
    }
    class GetAcategory extends AsyncTask<Void,Void,Void>{

        String id;
        CategoryValues categoryValues;

        public GetAcategory(String id){
            this.id=id;
        }

        @Override
        protected Void doInBackground(Void... params) {
            categoryValues=dataSets.getCategoryValues(getActivity(), id);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            CreateItemsDialog createItemsDialog = new CreateItemsDialog();
            showADialog(createItemsDialog.newCategoryInstance(id, categoryValues.name, true));
        }
    }
    class SearchProduct extends AsyncTask<Void,Void,Void>{

        public String value;
       List<Products>  products;
        ProductsAdapter productsAdapter;

        public SearchProduct(String value){
            this.value=value;

        }



        @Override
        protected Void doInBackground(Void... params) {

            productsAdapter = new ProductsAdapter(getActivity(), dataSets.getAllProductsLike(getActivity(), value));

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            recyclerViewAllItems.setAdapter(productsAdapter);
            recyclerViewAllItems.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerViewAllItems.setItemAnimator(new DefaultItemAnimator());
        }
    }
    class SearchCategory extends AsyncTask<Void,Void,Void>{

        public String value;
        List<Products>  products;

       CategoryValuesAdapter  categoryValuesAdapter;
        public SearchCategory(String value){
            this.value=value;

        }



        @Override
        protected Void doInBackground(Void... params) {

            categoryValuesAdapter = new CategoryValuesAdapter(getActivity(), dataSets.getCategoryValuesLike(getActivity(), value));

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            recyclerViewAllItems.setAdapter(categoryValuesAdapter);
            recyclerViewAllItems.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerViewAllItems.setItemAnimator(new DefaultItemAnimator());
        }
    }
    ////////////////////////////
}