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
import com.nfortics.megapos.Helper.Typefacer;
import com.nfortics.megapos.Helper.Useful;
import com.nfortics.megapos.Models.Transactions;
import com.nfortics.megapos.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by bigifre on 8/11/2015.
 */
public class TransactionsAdapter  extends RecyclerView.Adapter<TransactionsAdapterViewHolder>{


    private Context context;
    private LayoutInflater inflater;
    public List<Transactions> data= Collections.emptyList();
    Useful useful=new Useful();
    private double subTotal;
    public int counter=2;
    DataSets dataSets;


    public TransactionsAdapter(Context context, List<Transactions> data) {
        inflater= LayoutInflater.from(context);
        this.data=data;
        this.context=context;
        dataSets = new DataSets(context);
    }


    @Override
    public TransactionsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= inflater.inflate(R.layout.transactions_history_row, parent, false);

        TransactionsAdapterViewHolder holder = new TransactionsAdapterViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(TransactionsAdapterViewHolder holder, int position) {
        Typefacer typeface= new Typefacer();
        Transactions items=data.get(position);

        holder.txtTotalValue.setTypeface(typeface.getRoboCondensedBold(context.getAssets()));
        holder.txtTotalValue.setText(useful.formatMoney(items.getTranstotalAmount()));

        holder.txtRefenerence.setText("Ref : " + items.getReferenceNumber());
        holder.txtRefenerence.setTypeface(typeface.getRoboCondensedLight(context.getAssets()));



        holder.txtTransType.setText(items.getTransType());
        setTransTypeColor(holder,items.getTransType());
        holder.txtTransType.setTypeface(typeface.getRoboCondensedRegular(context.getAssets()));

        holder.txtTransTime.setText(items.getTransTime());
        holder.txtTransTime.setTypeface(typeface.getDigital7normal(context.getAssets()));

    }


    void setTransTypeColor(TransactionsAdapterViewHolder holder,String value){
try{
    switch (value){

        case "Airtime":
            holder.txtTransType.setTextColor(Color.parseColor("#ff8f43"));
            break;
        case "BillPay":
            holder.txtTransType.setTextColor(Color.parseColor("#3db8c7"));
            break;
        case "Cash Out":  //#637ee9 #ff8f43 #2175d9
            holder.txtTransType.setTextColor(Color.parseColor("#637ee9"));
            break;
        case "Cash In":
            holder.txtTransType.setTextColor(Color.parseColor("#2175d9"));
            break;
        case "mFinance":
            holder.txtTransType.setTextColor(Color.parseColor("#448800"));
            break;
        default:
            holder.txtTransType.setTextColor(Color.parseColor("#000000"));
            break;
    }

}catch (Exception e){

    e.printStackTrace();

}


    }


    @Override
    public int getItemCount() {
        return data.size();
    }
}






class TransactionsAdapterViewHolder extends RecyclerView.ViewHolder{

    TextView txtTotalValue,txtRefenerence,txtTransType,txtTransTime;
    Button itemquantity;
    CheckBox itemselect;
    ImageView itemIcon;


    public TransactionsAdapterViewHolder(View itemView) {
        super(itemView);

        txtTotalValue=(TextView)itemView.findViewById(R.id.txtTotalValue);
        txtRefenerence=(TextView)itemView.findViewById(R.id.txtRefenerence);
        txtTransType=(TextView)itemView.findViewById(R.id.txtTransType);
        txtTransTime=(TextView)itemView.findViewById(R.id.txtTransTime);
    }


}