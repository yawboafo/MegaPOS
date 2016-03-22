package com.nfortics.megapos.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nfortics.megapos.Helper.Typefacer;
import com.nfortics.megapos.Helper.Useful;
import com.nfortics.megapos.Models.Products;
import com.nfortics.megapos.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by bigifre on 8/4/2015.
 */
public class ProductsAdapter extends RecyclerView.Adapter<productsViewHolder> {


    Typefacer typefacer;
    private Context context;
    private LayoutInflater inflater;
    List<Products> data= Collections.emptyList();
    Useful useful=new Useful();

    public ProductsAdapter(Context context, List<Products> data) {
        inflater= LayoutInflater.from(context);
        this.data=data;
        this.context=context;
        typefacer= new Typefacer();

    }




    @Override
    public productsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= inflater.inflate(R.layout.stock_items_rows,parent,false);

        productsViewHolder holder = new productsViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(productsViewHolder holder, int position) {
        Typefacer typeface= new Typefacer();
        Products product =data.get(position);

        // holder.productIcon.setImageResource(product.icon);
        holder.productName.setText(product.getName());
        holder.price.setText(useful.formatMoney(product.getPrice()));
        holder.category.setText(product.getCategory());
        holder.hiddenID.setText(product.getId());
        // holder.quantity.setText("" + product.getQuantity());
        holder.price.setTypeface(typeface.getRoboCondensedLight(context.getAssets()));
        holder.productName.setTypeface(typeface.getRoboCondensedBold(context.getAssets()));
        holder.category.setTypeface(typeface.getRoboCondensedRegular(context.getAssets()));
        //  holder.quantity.setTypeface(typeface.getRoboCondensedLight(context.getAssets()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
class productsViewHolder extends RecyclerView.ViewHolder{
    //ImageView productIcon;
    TextView productName,price,quantity,category,hiddenID;


    public productsViewHolder(View itemView) {
        super(itemView);

        // productIcon =(ImageView)itemView.findViewById(R.id.productimage);
        productName=(TextView)itemView.findViewById(R.id.txtProductName);
        price=(TextView)itemView.findViewById(R.id.txtProductsPrice);
        category=(TextView)itemView.findViewById(R.id.txtProductCategory);
        hiddenID=(TextView)itemView.findViewById(R.id.hiddenID);
    }



}