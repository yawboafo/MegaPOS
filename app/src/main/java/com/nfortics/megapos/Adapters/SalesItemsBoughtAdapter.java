package com.nfortics.megapos.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nfortics.megapos.DataBase.DataSets;
import com.nfortics.megapos.Helper.Typefacer;
import com.nfortics.megapos.Models.SalesItemsRow;
import com.nfortics.megapos.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by bigifre on 8/18/2015.
 */
public class SalesItemsBoughtAdapter extends RecyclerView.Adapter<SalesItemsBoughtAdapterViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    List<SalesItemsRow> data= Collections.emptyList();
  //  Typefacer typeface= new Typefacer();
      DataSets dataSets;


    public SalesItemsBoughtAdapter(Context context, List<SalesItemsRow> data) {
        inflater= LayoutInflater.from(context);
        this.data=data;
        this.context=context;
        dataSets = new DataSets(context);
    }

    @Override
    public SalesItemsBoughtAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= inflater.inflate(R.layout.transaction_sales_row, parent, false);

        SalesItemsBoughtAdapterViewHolder holder = new SalesItemsBoughtAdapterViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(SalesItemsBoughtAdapterViewHolder holder, int position) {

        Typefacer typeface= new Typefacer();
        SalesItemsRow items=data.get(position);

        holder.itemName.setText(items.itemName  +"\t x ("+items.qty+")"+"" );
        holder.itemName.setTypeface(typeface.getRoboCondensedRegular(context.getAssets()));

       /** holder.qty.setText(items.qty);
        holder.qty.setTypeface(typeface.getRoboCondensedRegular(context.getAssets()));

        holder.price.setText(items.price);
        holder.price.setTypeface(typeface.getRoboCondensedRegular(context.getAssets()));
        **/
        holder.total.setText(items.total);
        holder.total.setTypeface(typeface.getRoboCondensedRegular(context.getAssets()));



    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
class SalesItemsBoughtAdapterViewHolder extends RecyclerView.ViewHolder{

    TextView itemName,qty,price,total;

    public SalesItemsBoughtAdapterViewHolder(View itemView) {
        super(itemView);
         Typefacer typefacer= new Typefacer();

         itemName = (TextView) itemView.findViewById(R.id.txtItemName);
      /**   qty = (TextView) itemView.findViewById(R.id.txtQuantity);
         price = (TextView) itemView.findViewById(R.id.txtPrice);**/
         total = (TextView) itemView.findViewById(R.id.txtTotal);
         //icon.setOnClickListener(this);



    }



}

