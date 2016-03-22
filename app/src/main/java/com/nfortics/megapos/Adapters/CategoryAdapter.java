package com.nfortics.megapos.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.nfortics.megapos.DataBase.DataSets;
import com.nfortics.megapos.Helper.Message;
import com.nfortics.megapos.Helper.Typefacer;
import com.nfortics.megapos.Helper.Useful;
import com.nfortics.megapos.Models.CategoryValues;
import com.nfortics.megapos.R;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by bigifre on 8/20/2015.
 */
public class CategoryAdapter  extends RecyclerView.Adapter<Categoryholder>  {


    private Context context;
    private LayoutInflater inflater;
    public List<CategoryValues> data= Collections.emptyList();
    Useful useful=new Useful();
    private double subTotal;
    public int counter=2;
    DataSets dataSets;


    public void Initializechecked(int position){


        Iterator<CategoryValues> eunice=data.iterator();
        while (eunice.hasNext()){

            CategoryValues item=eunice.next();
            if(item.name==data.get(position).name){
                item.selected=true;
                notifyItemChanged(position);
                // Message.messageShort(MyApplication.getAppContext(),""+item.name);
            }else{

                item.selected=false;
                notifyItemChanged(position);

            }
        }


        notifyDataSetChanged();



    }
    public void RadioChecker(int position){

        CategoryValues product=data.get(position);
        Message.messageShort(context, "" + product.selected);
        Iterator<CategoryValues> eunice=data.iterator();
        while (eunice.hasNext()){

            if(eunice.next().name.equals(product.name)){

                product.selected=true;

                // Message.messageShort(context, "gooooood");
            }else{

                // Message.messageShort(context, "falseeeee");

            }


        }

        notifyDataSetChanged();



        // data.get(position).selected=false;




    }
    public CategoryAdapter(Context context, List<CategoryValues> data) {
        inflater= LayoutInflater.from(context);
        this.data=data;
        this.context=context;
        dataSets = new DataSets(context);
    }
    @Override
    public Categoryholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= inflater.inflate(R.layout.rounded_category, parent, false);

        Categoryholder holder = new Categoryholder(view);

        return holder;
    }
    @Override
    public void onBindViewHolder(Categoryholder holder, int position) {
        Typefacer typeface= new Typefacer();
        CategoryValues categoryValues=data.get(position);

        holder.itemname.setText(categoryValues.name);
        holder.itemname.setTypeface(typeface.getRoboMediumitalic(context.getAssets()));
        holder.id.setText(categoryValues.id);



        //holder.checkBox.setActivated(categoryValues.selected);
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
}
class Categoryholder extends RecyclerView.ViewHolder{

    TextView itemname,id;
    CheckBox checkBox;

    public Categoryholder(View itemView) {
        super(itemView);
        id=(TextView)itemView.findViewById(R.id.txtCateID);
        itemname=(TextView)itemView.findViewById(R.id.txtCatgoryName);

        //checkBox.setChecked(false);

    }


}