package com.nfortics.megapos.Fragments.InnerFragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.nfortics.megapos.Adapters.AlphabetSorter;
import com.nfortics.megapos.Adapters.CategoryAdapter;
import com.nfortics.megapos.Adapters.ProductAdapter;
import com.nfortics.megapos.DataBase.DataSets;
import com.nfortics.megapos.Helper.Message;
import com.nfortics.megapos.Helper.Typefacer;
import com.nfortics.megapos.Helper.Useful;
import com.nfortics.megapos.Models.Product;
import com.nfortics.megapos.Models.Products;
import com.nfortics.megapos.R;

import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StartFragment extends Fragment {
    Useful useful = new Useful();
    RecyclerView itemsSort,tmpRecycleView,cateGoryDisplay;

    AlphabetSorter alphabetSorterAdapter;
    TextView alphabetText,productItemName,productPrice;


    CategoryAdapter categoryAdapter;

    Button filter;
    InsertItemsToListView communicator;
    GridView sortedItems;
    ProductAdapter productAdapter;
    RelativeLayout searchLayout;
    InputMethodManager IMM;
    EditText queryStringText;
    DataSets dataSets;

    boolean checkListiners;
    Spinner spinner;

    public StartFragment() {


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        communicator=(InsertItemsToListView)activity;

        checkListiners=true;
    }

    public void startFloatBut(){


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        Typefacer typefacer=new Typefacer();

        View layout = inflater.inflate(R.layout.fragment_start, container, false);


        dataSets= new DataSets(getActivity());

        spinner =(Spinner)layout.findViewById(R.id.SortByspinner);
        ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(getActivity(), R.array.filterItems, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                String item = parent.getItemAtPosition(position).toString();

                try {
                    switch (item) {

                        case "Item Name":
                            queryStringText.setHint("Search By Item Name");
                            queryStringText.setEnabled(true);
                            queryStringText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                            queryStringText.setTextColor(Color.parseColor("#000000"));
                            IMM.showSoftInput(queryStringText, InputMethodManager.SHOW_IMPLICIT);
                            //  refreshProductList(s.toString());
                            break;
                        case "Isbn":
                            queryStringText.setHint("Search By Isbn");
                            queryStringText.setEnabled(true);
                            queryStringText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                            queryStringText.setTextColor(Color.parseColor("#000000"));
                            IMM.showSoftInput(queryStringText, InputMethodManager.SHOW_IMPLICIT);
                            break;
                        case "Category":
                            queryStringText.setHint("Search By Category");
                            queryStringText.setEnabled(true);
                            queryStringText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                            queryStringText.setTextColor(Color.parseColor("#000000"));
                            IMM.showSoftInput(queryStringText, InputMethodManager.SHOW_IMPLICIT);
                            break;

                        case "Barcode Mode":
                            queryStringText.setHint("Barcode Mode ");
                            // queryStringText.setEnabled(false);
                            queryStringText.setBackgroundColor(Color.parseColor("#FF0000"));
                            queryStringText.setTextColor(Color.parseColor("#000000"));
                            IMM.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                            queryStringText.requestFocus();
                            break;
                        default:
                            queryStringText.setHint("Search....");
                            break;

                    }
                } catch (Exception ex) {


                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        RecyclerView.LayoutManager mlayout=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);

        RecyclerView.LayoutManager mlyout2=new GridLayoutManager(getActivity(),5,GridLayoutManager.HORIZONTAL,false);
        searchLayout=(RelativeLayout)layout.findViewById(R.id.searchLayout);
        searchLayout.setVisibility(View.GONE);


        queryStringText=(EditText)layout.findViewById(R.id.getQueryString);
        queryStringText.setHint("Search....");

        queryStringText.addTextChangedListener(BarcodeWatcher);


        Button searchButton=(Button)layout.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    if (searchLayout.getVisibility() == View.VISIBLE) {
                        // queryStringText.setInputType(InputType.TYPE_NULL);
                        itemsSort.setVisibility(View.VISIBLE);
                        IMM = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        IMM.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

                        searchLayout.setVisibility(View.GONE);
                    } else {

                        //IMM.showSoftInput(queryStringText, InputMethodManager.SHOW_IMPLICIT);
                        itemsSort.setVisibility(View.INVISIBLE);
                        spinner.refreshDrawableState();
                        queryStringText.refreshDrawableState();
                        searchLayout.setVisibility(View.VISIBLE);
                        queryStringText.requestFocus();
                        //queryStringText
                        //IMM.showSoftInput(queryStringText, InputMethodManager.SHOW_IMPLICIT);

                    }
                } catch (Exception ex) {


                }


            }
        });


        tmpRecycleView=(RecyclerView)layout.findViewById(R.id.itemsDisplay);
        tmpRecycleView.setAdapter(productAdapter);
        tmpRecycleView.setLayoutManager(mlyout2);
        tmpRecycleView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), tmpRecycleView,
                new ClickListner() {
                    @Override
                    public void onClick(View view, int position) {


                        try {


                            productItemName = (TextView) view.findViewById(R.id.productName);
                            productPrice = (TextView) view.findViewById(R.id.price);


                            communicator.addToPreparedItems(productItemName.getText().toString(), productPrice.getText().toString());
                        } catch (Exception ex) {


                        }


                    }

                    @Override
                    public void onLongClick(View view, int position) {


                        productItemName = (TextView) view.findViewById(R.id.productName);
                        productPrice = (TextView) view.findViewById(R.id.price);






                        new getAproductByName(productItemName.getText().toString()).execute();


                        Message.messageShort(getActivity(), "A Favorite ! ");
                        Log.d("oxinbo", "The log was clicked " + productItemName.getText().toString());










                    }
                }));


        try{
            categoryAdapter=new CategoryAdapter(getActivity(),dataSets.getCategoryValues(getActivity()));
            cateGoryDisplay=(RecyclerView)layout.findViewById(R.id.categoryDisplay);
            cateGoryDisplay.setAdapter(categoryAdapter);
            cateGoryDisplay.setLayoutManager(new LinearLayoutManager(getActivity()));
            cateGoryDisplay.setItemAnimator(new DefaultItemAnimator());

            cateGoryDisplay.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                    cateGoryDisplay, new ClickListner() {
                @Override
                public void onClick(View view, int position) {

                    TextView categoryName=(TextView)view.findViewById(R.id.txtCatgoryName);
                    String value=categoryName.getText().toString();
                    filterProductListByCategory(value);
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));

        }catch(Exception ex){
            ex.printStackTrace();
        }




        try{




            itemsSort=(RecyclerView)layout.findViewById(R.id.itemSort);
            alphabetSorterAdapter=new AlphabetSorter(getActivity(),useful.getAlphaBets());
            itemsSort.setAdapter(alphabetSorterAdapter);
            itemsSort.setLayoutManager(mlayout);
            itemsSort.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                    itemsSort, new ClickListner() {
                @Override
                public void onClick(View view, int position) {

                    alphabetText = (TextView) view.findViewById(R.id.txtAlphabets);
                    String value=alphabetText.getText().toString();
                    try{

                        //  Message.messageShort(getActivity(),value);

                        if(value.equals("View")){

                            communicator. changeView();
                        }else if(value.equals("Categories")){

                            if(cateGoryDisplay.getVisibility()==View.GONE){
                                tmpRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
                              cateGoryDisplay.setVisibility(View.VISIBLE);
                            }else{
                                tmpRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), 5));
                                cateGoryDisplay.setVisibility(View.GONE);
                            }



                        }else  if(value.equals("All")){
                            cateGoryDisplay.setVisibility(View.GONE);
                            getAllProducts();


                        }else if(value.equals("Favorites")) {
                            cateGoryDisplay.setVisibility(View.GONE);
                            new geAllFavorites().execute();

                        }else{
                            cateGoryDisplay.setVisibility(View.GONE);
                            filterProductList(alphabetText.getText().toString());
                        }


                    }catch(Exception ex){



                    }




                    //Message.message(getActivity(),alphabetText.getText().toString());

                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));


        }catch (Exception e){e.printStackTrace();}



        return layout;
    }

    public void refreshProductList(String value){

        new refreshProductList(value).execute();
        /**
         productAdapter = new ProductAdapter(getActivity(), dataSets.getAllProduct(getActivity(), value));
         tmpRecycleView.setAdapter(productAdapter);
         tmpRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), 5));
         **/
    }
    public void filterProductListByCategory(String value){


        new filterProductListByCategory(value).execute();
        /**
         productAdapter = new ProductAdapter(getActivity(),dataSets.getAllProductBeginningWithALetter(getActivity(), value));
         tmpRecycleView.setAdapter(productAdapter);
         tmpRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), 5));
         **/
    }
    public void getAllProducts(){
        new  getAllProducts("").execute();
    }
    public void filterProductList(String value){


        new filterProductList(value).execute();
        /**
         productAdapter = new ProductAdapter(getActivity(),dataSets.getAllProductBeginningWithALetter(getActivity(), value));
         tmpRecycleView.setAdapter(productAdapter);
         tmpRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), 5));
         **/
    }
    public void refreshProductList(int value){


        new refreshIntProductList(value).execute();

        /**
         List<Product> alist= Collections.emptyList();
         productAdapter = new ProductAdapter(getActivity(),alist);
         tmpRecycleView.setAdapter(productAdapter);
         tmpRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), 5));
         **/
    }
    public void refreshProductList(ProductAdapter productAdapter,RecyclerView recyclerView) {


        recyclerView.setAdapter(productAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 5));


    }
    private TextWatcher BarcodeWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String retrievedProductName1;


            switch (spinner.getSelectedItem().toString()) {

                case "Item Name":


                    if (s.toString().length() <= 0) {

                        refreshProductList(0);
                    } else {

                        refreshProductList(s.toString());

                    }

                    break;
                case "Isbn":
                    productAdapter = new ProductAdapter(getActivity(), dataSets.getAProductByIsbn(getActivity(), s.toString()));

                    if (s.toString().length() <= 0) {

                        refreshProductList(0);
                    } else {

                        refreshProductList(productAdapter, tmpRecycleView);

                    }


                    break;
                case "Category":

                    break;
                case "Barcode Mode":

                    try {

                        Product bproduct;
                        bproduct = dataSets.getAproduct(getActivity(), s.toString());

                        retrievedProductName1 = bproduct.getName();
                        Double retrievedPrice1 = bproduct.getPrice();

                        if (retrievedProductName1.length() <= 0) {

                            Message.message(getActivity(), "" + retrievedProductName1.length());

                        } else {


                            communicator.addToPreparedItems(bproduct.getName(), useful.formatMoney(bproduct.getPrice()));
                            queryStringText.setText("");
                            queryStringText.requestFocus();
                            //  checkListiners=false;
                        }


                    } catch (Exception ex) {


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                queryStringText.removeTextChangedListener(BarcodeWatcher);
                                queryStringText.setText("");
                                //queryStringText.requestFocus();
                                queryStringText.addTextChangedListener(BarcodeWatcher);
                            }
                        }, 3000);


                    }


                    break;


            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private GestureDetector gestureDetector;
        private ClickListner clickListner;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListner clickListner){

            this.clickListner=clickListner;

            gestureDetector= new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){

                @Override
                public boolean onSingleTapUp(MotionEvent e) {

                    View child= recyclerView.findChildViewUnder(e.getX(),e.getY());

                    if(child !=null & clickListner !=null){


                        clickListner.onClick(child, recyclerView.getChildPosition(child));

                    }

                    return super.onSingleTapUp(e);
                }

                @Override
                public void onLongPress(MotionEvent e) {



                    View child= recyclerView.findChildViewUnder(e.getX(),e.getY());

                    if(child !=null & clickListner !=null){


                        clickListner.onLongClick(child,recyclerView.getChildPosition(child));

                    }
                    // super.onLongPress(e);
                }
            });



        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child= rv.findChildViewUnder(e.getX(),e.getY());

            if(child !=null & clickListner !=null && gestureDetector.onTouchEvent(e) ){


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
    public static  interface ClickListner{
        public void onClick(View view,int position);
        public void onLongClick(View view,int position);
    }
    public static  interface InsertItemsToListView{


        public void addToPreparedItems(String name,String price);
        public void changeView();

    }
    class refreshProductList extends AsyncTask<Void,Void,Void>{

        public String value;
        ProgressDialog pDialog ;
        public refreshProductList(String value) {
            this.value = value;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog  = new ProgressDialog(getActivity());

            pDialog.setMessage("Please  wait...");
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            productAdapter = new ProductAdapter(getActivity(), dataSets.getAllProduct(getActivity(), value));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            tmpRecycleView.setAdapter(productAdapter);
            tmpRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), 5));
            pDialog.hide();

        }
    }
    class filterProductList extends AsyncTask<Void,Void,Void>{

        public String value;
        ProgressDialog pDialog ;
        public filterProductList(String value) {
            this.value = value;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog  = new ProgressDialog(getActivity());

            pDialog.setMessage("Please  wait...");
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            productAdapter = new ProductAdapter(getActivity(),dataSets.getAllProductBeginningWithALetter(getActivity(), value));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            tmpRecycleView.setAdapter(productAdapter);
            tmpRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), 5));
            pDialog.hide();

        }
    }
    class filterProductListByCategory extends AsyncTask<Void,Void,Void>{

        public String value;
        ProgressDialog pDialog ;
        public filterProductListByCategory(String value) {
            this.value = value;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog  = new ProgressDialog(getActivity());

            pDialog.setMessage("Please  wait...");
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            productAdapter = new ProductAdapter(getActivity(),dataSets.getAllProductByCategory(getActivity(), value));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            tmpRecycleView.setAdapter(productAdapter);
            tmpRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
            pDialog.hide();

        }
    }
    class refreshIntProductList extends AsyncTask<Void,Void,Void>{

        public int value;
        ProgressDialog pDialog ;
        public refreshIntProductList(int value) {
            this.value = value;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog  = new ProgressDialog(getActivity());

            pDialog.setMessage("Please  wait..");
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            List<Product> alist= Collections.emptyList();
            productAdapter = new ProductAdapter(getActivity(),alist);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            tmpRecycleView.setAdapter(productAdapter);
            tmpRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), 5));
            pDialog.hide();

        }
    }
    class refreshProductListAdapter extends AsyncTask<Void,Void,Void>{

        ProductAdapter productAdapter;
        RecyclerView recyclerView;
        ProgressDialog pDialog ;
        public refreshProductListAdapter(ProductAdapter productAdapter,RecyclerView recyclerView) {
            this.productAdapter = productAdapter;
            this.recyclerView =recyclerView;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog  = new ProgressDialog(getActivity());

            pDialog.setMessage("Please  wait...Confirming Sap");
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            recyclerView.setAdapter(productAdapter);
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 5));
            pDialog.hide();

        }
    }
    class geAllFavorites extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {


            //getAllFavorites

            productAdapter = new ProductAdapter(getActivity(),dataSets.getAllFavorites(getActivity()));

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            tmpRecycleView.setAdapter(productAdapter);
            tmpRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), 5));
        }
    }
    class getAproductByName  extends AsyncTask<Void,Void,Void>{

        private String name;
        Products product;

        public  getAproductByName(String name){
            this.name=name;
        }

        @Override
        protected Void doInBackground(Void... params) {

            product=dataSets.getAproductByName(getActivity(),name);

            long id=0;
            if(dataSets.insertFavoriteProduct(getActivity(),name)){

                productAdapter = new ProductAdapter(getActivity(),
                        dataSets.getAllFavorites(getActivity()));
                //   id=dataSets.deleteFavorite(getActivity(), name);

                // insertFavoritePRODUCT(product);
            }else {

                id=   dataSets.insertFavoritePRODUCT(product);


            }

            Log.d("oxinbo",   id+ "<< long "+    product.getPrice()+" >> "+" >> "+product.getName());
            //  long id=  dataSets.insertFavoritePRODUCT(product);
            //  Log.d("oxinbo","Long ID iNSEC "+id);
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            tmpRecycleView.setAdapter(productAdapter);
            tmpRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), 5));
        }
    }
    class getAllProducts extends AsyncTask<Void,Void,Void>{

        public String value;
        ProgressDialog pDialog ;
        public getAllProducts(String value) {
            this.value = value;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog  = new ProgressDialog(getActivity());

            pDialog.setMessage("Please  wait...");
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            productAdapter = new ProductAdapter(getActivity(),dataSets.getAllProduct(getActivity()));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            tmpRecycleView.setAdapter(productAdapter);
            tmpRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), 5));
            pDialog.hide();

        }
    }
}
