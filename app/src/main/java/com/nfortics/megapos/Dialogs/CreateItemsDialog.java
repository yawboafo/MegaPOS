package com.nfortics.megapos.Dialogs;


import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nfortics.megapos.Adapters.CategoryValuesAdapter;
import com.nfortics.megapos.Application.MyApplication;
import com.nfortics.megapos.DataBase.DataSets;
import com.nfortics.megapos.Helper.Message;
import com.nfortics.megapos.Models.CategoryValues;
import com.nfortics.megapos.Models.Product;
import com.nfortics.megapos.Models.Products;
import com.nfortics.megapos.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateItemsDialog extends DialogFragment {

    RecyclerView catgoryList;
    EditText categoryName,productISBN,productName,productPrice;
    Button saveItems,buttAddCategory,neutralButton,productCategory,deleteItems;
    RelativeLayout productsLayout,CategoryLayout;
    DataSets dataSets;
    String ISBN,name,price,category;
     TextView selectedCategory;
    public String selectCategoryValue;
    OnFragmentInteractionListener onFragmentInteractionListener;
    CategoryValuesAdapter categoryValuesAdapter;

    private static final String ARG_ID = "param0";
    private static final String ARG_ISBN = "param1";
    private static final String ARG_NAME = "param2";
    private static final String ARG_CATEGORY = "param3";
    private static final String ARG_PRICE = "param4";
   // private static final boolean ARG_Boolean =;
    private static final String CATEGORY_ARG_ISBN = "param1";

    // TODO: Rename and change types of parameters
    private String mISBN;
    private String mNAME;
    private String mCATEGORY;
    private String mPRICE;
    private String mID;
    private String _categoryName;
   private static boolean Setcategory;
    private static boolean Setcateg;
    private String cateID;
    public CreateItemsDialog() {
        // Required empty public constructor
    }



    public static CreateItemsDialog newCategoryInstance(String id,String name,boolean cal){

        CreateItemsDialog fragment = new CreateItemsDialog();
        Bundle args = new Bundle();
        args.putString("ARGS_CATEGORY_ID", id);
        args.putString(CATEGORY_ARG_ISBN, name);
        args.putBoolean("ARGS_BOOLEAN", cal);
        Setcategory=cal;
        fragment.setArguments(args);
        return fragment;
    }
    public static CreateItemsDialog CategoryInstance(boolean cal){

        CreateItemsDialog fragment = new CreateItemsDialog();
        Bundle args = new Bundle();
        args.putBoolean("ARGS_BOOLEAN2", cal);
        Setcateg=cal;
        fragment.setArguments(args);
        return fragment;
    }

    public static CreateItemsDialog newInstance(String isbn, String name,String category,String price,String id) {
        CreateItemsDialog fragment = new CreateItemsDialog();
        Bundle args = new Bundle();
        args.putString(ARG_ISBN, isbn);
        args.putString(ARG_NAME, name);
        args.putString(ARG_CATEGORY, category);
        args.putString(ARG_PRICE, price);
        args.putString(ARG_ID, id);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        onFragmentInteractionListener=(OnFragmentInteractionListener)activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mISBN = getArguments().getString(ARG_ISBN);
            mNAME = getArguments().getString(ARG_NAME);
            mCATEGORY = getArguments().getString(ARG_CATEGORY);
            mPRICE = getArguments().getString(ARG_PRICE);
            mID = getArguments().getString(ARG_ID);
            _categoryName= getArguments().getString(CATEGORY_ARG_ISBN);
            cateID= getArguments().getString("ARGS_CATEGORY_ID");
            Setcategory=getArguments().getBoolean("ARGS_BOOLEAN");

            Setcateg=getArguments().getBoolean("ARGS_BOOLEAN2");
        }

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
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);

        try{

            dataSets=new DataSets(getActivity());
        }catch(Exception e){

            Log.e("error",e.toString());

        }
        return inflater.inflate(R.layout.fragment_create_items_dialog, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        InitializeViews(view);
        InitializeClicklistners();
        InitializeRecycleViewer();
        RecycleViewClickListners();
        setEditValues();
       // setCategoryEdit();



    }
    public void InitializeViews(View view){

        productISBN=(EditText)view.findViewById(R.id.edtISBN) ;
        productName=(EditText)view.findViewById(R.id.edtName) ;
        productPrice=(EditText)view.findViewById(R.id.edtPrice) ;
                //productCategory=(Button)view.findViewById(R.id.buttAddCategory) ;

        selectedCategory=(TextView)view.findViewById(R.id.selectedCategory);
        selectedCategory.setText("");
        buttAddCategory=(Button)view.findViewById(R.id.buttAddCategory) ;
        saveItems=(Button)view.findViewById(R.id.butSave) ;
        neutralButton=(Button)view.findViewById(R.id.butCancel) ;

        deleteItems=(Button)view.findViewById(R.id.deleteItems) ;

        neutralButton.setText("Create Item");
        catgoryList=(RecyclerView)view.findViewById(R.id.categoryVales) ;
        categoryName=(EditText)view.findViewById(R.id.editcategoryName) ;
        productsLayout=(RelativeLayout)view.findViewById(R.id.layoutCreateItems) ;
        CategoryLayout=(RelativeLayout)view.findViewById(R.id.layoutCreateCategory) ;



    }
    public void InitializeClicklistners(){

        saveItems_OnclickListener();
        buttAddCategory_OnclickListener();
        neutralButton_OnclickListener();
        deleteItems_OnclickListener();
    }
    public void InitializeRecycleViewer(){

        /**
        List<CategoryValues> datatmp = new ArrayList<>();
        categoryValuesAdapter = new CategoryValuesAdapter(getActivity(), datatmp);
        catgoryList.setAdapter(categoryValuesAdapter);
        catgoryList.setLayoutManager(new LinearLayoutManager(getActivity()));
        catgoryList.setItemAnimator(new DefaultItemAnimator());
**/
        new LoadCategoryItems().execute();

    }
    public void RecycleViewClickListners(){

        catgoryList.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                catgoryList, new ClickListner() {
            @Override
            public void onClick(View view, final int position) {

                TextView tview = (TextView) view.findViewById(R.id.txtCategoryValueName);
                String val = tview.getText().toString();
                selectedCategory.setText(val);
                //Message.messageShort(getActivity(),val);
                selectCategoryValue = val;


            }

            @Override
            public void onLongClick(View view, int position) {

                TextView tview = (TextView) view.findViewById(R.id.txtCategoryValueName);
                String val = tview.getText().toString();
                selectedCategory.setText(val);
                //Message.messageShort(getActivity(),val);
                selectCategoryValue = val;


            }
        }));


    }
    RelativeLayout getLayout(RelativeLayout layout){

        RelativeLayout newLayoutInstance=null;

        List<RelativeLayout> layouts = new ArrayList<>();
        layouts.add(productsLayout);
        layouts.add(CategoryLayout);

        for(RelativeLayout alayout:layouts){

            if(alayout==layout){
                alayout.setVisibility(View.GONE);

            }else {
                newLayoutInstance=alayout;
                //return alayout;
            }

        }

        newLayoutInstance.setVisibility(View.VISIBLE);
        return newLayoutInstance;


    }
    void setEditValues(){

        try{
            if (getArguments() != null){
         // Message.messageShort(getActivity(),Setcategory+"");
                if(Setcategory){

                    getLayout(productsLayout);
                    _categoryName = getArguments().getString(CATEGORY_ARG_ISBN);
                    categoryName.setText(_categoryName);
                    deleteItems.setVisibility(View.VISIBLE);
                    saveItems.setText("Update");
                    new LoadCategoryNames().execute();
                }else if(Setcateg){

                    getLayout(productsLayout);
                    neutralButton.setText("");
                    deleteItems.setVisibility(View.GONE);
                    saveItems.setText("Save");
                    new LoadCategoryNames().execute();
                }else{

                    getLayout(CategoryLayout);
                    productISBN.setText("" + mISBN);
                    productName.setText("" + mNAME);
                    productPrice.setText("" + mPRICE);
                    buttAddCategory.setText("" + mCATEGORY);

                    deleteItems.setVisibility(View.VISIBLE);
                    saveItems.setText("Update");
                }
            }
        }catch(Exception ex){


        }

    }
    void setCategoryEdit(){


        if (getArguments() != null) {

            if (getArguments().getString(CATEGORY_ARG_ISBN) != null) {


                getLayout(productsLayout);
                _categoryName = getArguments().getString(CATEGORY_ARG_ISBN);
                categoryName.setText(_categoryName);
            }

        }


    }
    void decideHowtoSave(String value){
     try{

         switch (value){
             case "Category":
                 category=categoryName.getText().toString();
                     if(category.length()==0){

                     }else{
                         long Catid = dataSets.insertCategory(category);
                         if(Catid<1){
                             Message.message(MyApplication.getAppContext(),"SAVIOR COULD NOT SAVED");
                         }else{
                             Log.d("long id", "" + Catid);
                             new LoadCategoryNames().execute();
                             Message.message(MyApplication.getAppContext(), "SAVIOR SAVED");
                             categoryName.setText("");
                             onFragmentInteractionListener.UpdateRecycleView("Category");
                         }
                     }
                break;
            case "Create Item":
                ISBN=productISBN.getText().toString();
                name=productName.getText().toString();
                price=productPrice.getText().toString();
                category=(buttAddCategory.getText().toString().equals("Category")?"Nothing":
                        buttAddCategory.getText().toString());

                if(ISBN.length()==0
                        ||name.length()==0
                        ||price.length()==0
                ||category.length()==0

                ){

            }else{
                    Log.d("things ",""+ISBN);
                    long id = dataSets.insertPRODUCT(
                            ISBN,
                            name,
                            price,
                            category);

                    if(id<1){
                        Message.message(MyApplication.getAppContext(), "SAVIOR COULD NOT SAVED");
                    }else{

                        productISBN.setText("");
                        productPrice.setText("");
                        productName.setText("");
                        buttAddCategory.setText("Category");
                        Log.d("long id",""+id);
                        onFragmentInteractionListener.UpdateRecycleView("Items");
                        Message.message(MyApplication.getAppContext(), "SAVIOR SAVED");
                    }



                }



                    break;

             case "":
                 category=categoryName.getText().toString();
                 if(category.length()==0){

                 }else{
                     long Catid = dataSets.insertCategory(category);
                     if(Catid<1){
                         Message.message(MyApplication.getAppContext(),"SAVIOR COULD NOT SAVED");
                     }else{
                         Log.d("long id", "" + Catid);
                         new LoadCategoryNames().execute();
                         Message.message(MyApplication.getAppContext(), "SAVIOR SAVED");
                         categoryName.setText("");
                         onFragmentInteractionListener.UpdateRecycleView("Category");
                     }
                 }
                 break;

                }

     }catch(Exception e){}
        }
    void decideNeutralButtonActions(String value){
       try{
    switch (value){

        case "Category":

            int imageResource =R.drawable.close;
            neutralButton.setText("Create Item");
            neutralButton.setCompoundDrawablesWithIntrinsicBounds(imageResource, 0, 0, 0);
            getLayout(CategoryLayout);

            if (selectCategoryValue.length()==0){

                buttAddCategory.setText("Category");
            }else{

                buttAddCategory.setText(selectCategoryValue);
            }
            selectedCategory.setText("");

            break;

        case "Create Item":
            dismiss();
            break;

        default:
            dismiss();
            break;
    }

}catch(Exception ec){



}

    }
    void saveItems_OnclickListener(){

       saveItems.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {


               try{


                   switch (saveItems.getText().toString()){

                       case "Update":
                           if(Setcategory){

                               UpdateACategory();
                           }else{
                               UpdateAnItem();

                           }


                           break;

                       case "Save":
                           decideHowtoSave(neutralButton.getText().toString());
                           break;

                   }



                   Log.d("neutral",neutralButton.getText().toString());
               }catch(Exception e){


               }


           }
       });
    }

    void UpdateACategory(){

        if(categoryName.getText().toString().length()==0){



        }else{
            CategoryValues categoryValues= new CategoryValues();
            categoryValues.name= categoryName.getText().toString();
            dataSets.UpdateCategory(getActivity(),categoryValues,cateID);

            onFragmentInteractionListener.UpdateRecycleView("Category");

        }

    }
    void UpdateAnItem(){

        ISBN=productISBN.getText().toString();
        name=productName.getText().toString();
        price=productPrice.getText().toString();
        category=(buttAddCategory.getText().toString().equals("Category")?"Nothing":
                buttAddCategory.getText().toString());
        Products p= new Products();
        p.setIsbn(ISBN);
        p.setName(name);
        p.setPrice(Double.parseDouble(price));
        p.setCategory(category);

        if(ISBN.length()==0
                ||name.length()==0
                ||price.length()==0
                ||category.length()==0

                ){

        }else{

            int i=  dataSets.UpdateProducts(getActivity(),p,mID);


            if(i<1){

                Message.message(MyApplication.getAppContext(), "SAVIOR Failed");

            }else{

                Message.message(MyApplication.getAppContext(), "SAVIOR Success");
                productISBN.setText("");
                productPrice.setText("");
                productName.setText("");
                buttAddCategory.setText("Category");
                onFragmentInteractionListener.UpdateRecycleView("Items");
            }


            Log.d("long id >>>>>> ",""+i);


        }


    }
    void buttAddCategory_OnclickListener(){
    buttAddCategory.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            new LoadCategoryNames().execute();
            int imageResource =R.drawable.back;
            neutralButton.setCompoundDrawablesWithIntrinsicBounds(imageResource,0,0,0);
            neutralButton.setText("Category");
            getLayout(productsLayout);
            deleteItems.setVisibility(View.GONE);
        }
    });

}
    void neutralButton_OnclickListener(){

        neutralButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                decideNeutralButtonActions(neutralButton.getText().toString());


            }
        });

    }
    void deleteItems_OnclickListener(){

        deleteItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Products product;
                if(Setcategory){
                    dataSets.deleteCategory(getActivity(), cateID);
                    onFragmentInteractionListener.UpdateRecycleView("Category");
                    dismiss();
                }else{

                    product=dataSets.getAproducts(getActivity(), mID);
                    dataSets.deleteProduct(getActivity(),mID);
                    Log.d("oxinbo", product.getName() + "ISBN " + mID + " product name in deletClick  ");
                    dataSets.deleteFavorite(getActivity(), product.getName());
                    dismiss();
                onFragmentInteractionListener.UpdateRecycleView("Items");  }
            }
        });

    }
    class LoadCategoryItems extends AsyncTask<Void,Void,Void> {




        @Override
        protected Void doInBackground(Void... params) {

            List<CategoryValues> datatmp = new ArrayList<>();
            categoryValuesAdapter = new CategoryValuesAdapter(getActivity(), datatmp);

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            catgoryList.setAdapter(categoryValuesAdapter);
            catgoryList.setLayoutManager(new LinearLayoutManager(getActivity()));
            catgoryList.setItemAnimator(new DefaultItemAnimator());
        }
    }
    public  interface OnFragmentInteractionListener{

        public void UpdateRecycleView(String val);

    }
    class LoadCategoryNames extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {
            categoryValuesAdapter=new CategoryValuesAdapter(getActivity(),dataSets.getCategoryValues(getActivity()));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            catgoryList.setAdapter(categoryValuesAdapter);
            catgoryList.setLayoutManager(new LinearLayoutManager(getActivity()));
            catgoryList.setItemAnimator(new DefaultItemAnimator());
        }
    }
    public static interface ClickListner {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }
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
}
