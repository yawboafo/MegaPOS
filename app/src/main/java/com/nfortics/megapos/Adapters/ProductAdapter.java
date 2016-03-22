package com.nfortics.megapos.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nfortics.megapos.DataBase.DataSets;
import com.nfortics.megapos.Helper.Message;
import com.nfortics.megapos.Helper.Typefacer;
import com.nfortics.megapos.Helper.Useful;
import com.nfortics.megapos.Models.Product;
import com.nfortics.megapos.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by bigifre on 7/10/2015.
 */
public class ProductAdapter extends RecyclerView.Adapter<productViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    List<Product> data= Collections.emptyList();
Useful useful=new Useful();

    public ProductAdapter(Context context, List<Product> data) {
        inflater= LayoutInflater.from(context);
        this.data=data;
        this.context=context;
    }


    @Override
    public productViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= inflater.inflate(R.layout.products_row,parent,false);

        productViewHolder holder = new productViewHolder(view);

        return holder;
    }
    @Override
    public void onBindViewHolder(productViewHolder holder, int position) {

        Typefacer typeface= new Typefacer();
        Product product =data.get(position);

        // holder.productIcon.setImageResource(product.icon);
        holder.productName.setText(product.getName());
        holder.price.setText(useful.formatMoney(product.getPrice()));
       // holder.quantity.setText("" + product.getQuantity());
        holder.price.setTypeface(typeface.getRoboCondensedLight(context.getAssets()));
        holder.productName.setTypeface(typeface.getRoboCondensedRegular(context.getAssets()));
      //  holder.quantity.setTypeface(typeface.getRoboCondensedLight(context.getAssets()));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
class productViewHolder extends RecyclerView.ViewHolder{
    //ImageView productIcon;
    TextView productName,price,quantity;


    public productViewHolder(View itemView) {
        super(itemView);

        // productIcon =(ImageView)itemView.findViewById(R.id.productimage);
        productName=(TextView)itemView.findViewById(R.id.productName);
        price=(TextView)itemView.findViewById(R.id.price);
        //quantity=(TextView)itemView.findViewById(R.id.quantity);
    }
}