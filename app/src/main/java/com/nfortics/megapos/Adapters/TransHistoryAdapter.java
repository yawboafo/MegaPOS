package com.nfortics.megapos.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.nfortics.megapos.DataBase.DataSets;
import com.nfortics.megapos.Helper.Message;
import com.nfortics.megapos.Helper.Typefacer;
import com.nfortics.megapos.Helper.Useful;
import com.nfortics.megapos.Models.TransHistory;
import com.nfortics.megapos.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by bigifre on 7/30/2015.
 */
public class TransHistoryAdapter extends RecyclerView.Adapter<TransViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    public List<TransHistory> data= Collections.emptyList();
    Useful useful=new Useful();
    private double subTotal;
    public int counter=2;
    DataSets dataSets;

    public TransHistoryAdapter(Context context, List<TransHistory> data) {
        inflater= LayoutInflater.from(context);
        this.data=data;
        this.context=context;
        dataSets = new DataSets(context);
    }

    @Override
    public TransViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view= inflater.inflate(R.layout.history_activity_layout_row,parent,false);

        TransViewHolder holder = new TransViewHolder(view);

        return holder;


    }

    @Override
    public void onBindViewHolder(TransViewHolder holder, int position) {
      Typefacer typeface= new Typefacer();
        TransHistory items=data.get(position);

        holder.date.setText(items.getDate());
        holder.date.setTypeface(typeface.getRoboCondensedRegular(context.getAssets()));

        holder.totalAmount.setText(items.getTotalSale());
        holder.totalAmount.setTypeface(typeface.getRoboCondensedLight(context.getAssets()));

        holder.itemIcon.setImageResource(R.drawable.forward);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
     class TransViewHolder extends RecyclerView.ViewHolder{

       TextView date,totalAmount;
       ImageView itemIcon;


        public TransViewHolder(View itemView) {
        super(itemView);

        date=(TextView)itemView.findViewById(R.id.txtDate);
        totalAmount=(TextView)itemView.findViewById(R.id.txtSales);
        itemIcon=(ImageView)itemView.findViewById(R.id.imgIcon);


    }


}