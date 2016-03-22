package com.nfortics.megapos.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.nfortics.megapos.Models.Items;
import com.nfortics.megapos.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by bigifre on 7/21/2015.
 */
public class RegisteredFirmsAdapter extends RecyclerView.Adapter<AViewholder>  {

    private Context context;
    private LayoutInflater inflater;
    public List<String> data= Collections.emptyList();

    public RegisteredFirmsAdapter(Context context, List<String> data) {
        inflater= LayoutInflater.from(context);
        this.data=data;
        this.context=context;

    }

    @Override
    public AViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= inflater.inflate(R.layout.registered_firms_row,parent,false);

        AViewholder holder = new AViewholder(view);

        return holder;
    }

    @Override
    public int getItemCount() {
       return data.size();
    }

    @Override
    public void onBindViewHolder(AViewholder holder, int position) {

        String newVal=data.get(position);
        holder.button.setText(newVal);
    }


}
     class AViewholder extends RecyclerView.ViewHolder{

    Button button;
    public AViewholder(View itemView) {
        super(itemView);

        button=(Button)itemView.findViewById(R.id.butAfirm);
    }
}