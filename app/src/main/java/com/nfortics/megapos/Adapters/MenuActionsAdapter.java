package com.nfortics.megapos.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.nfortics.megapos.Models.MenuActions;
import com.nfortics.megapos.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by bigifre on 7/22/2015.
 */
public class MenuActionsAdapter  extends RecyclerView.Adapter<MenuActionViewolder> {

    private Context context;
    private LayoutInflater inflater;
    public List<MenuActions> data= Collections.emptyList();

    public MenuActionsAdapter(Context context, List<MenuActions> data) {
        inflater= LayoutInflater.from(context);
        this.data=data;
        this.context=context;
        //dataSets = new DataSets(context);
    }

    @Override
    public MenuActionViewolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= inflater.inflate(R.layout.mfinance_menu_items_row,parent,false);

        MenuActionViewolder holder = new MenuActionViewolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MenuActionViewolder holder, int position) {
        MenuActions items=data.get(position);

       // items.setIcon(data.get(position).getIcon());
        //items.setTextLabel(data.get(position).getTextLabel());
        holder.itemname.setText(items.getTextLabel());
        holder.itemIcon.setImageResource(items.getIcon());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}



class MenuActionViewolder extends RecyclerView.ViewHolder{

    TextView itemname;
    ImageView itemIcon;


    public MenuActionViewolder(View itemView) {
        super(itemView);

        itemname=(TextView)itemView.findViewById(R.id.menuText);
        itemIcon=(ImageView)itemView.findViewById(R.id.MenuImageView);
        //  itemselect.setVisibility(View.GONE);



    }


}