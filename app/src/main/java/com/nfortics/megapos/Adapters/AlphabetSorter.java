package com.nfortics.megapos.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.nfortics.megapos.Helper.Typefacer;
import com.nfortics.megapos.Models.Alphabets;
import com.nfortics.megapos.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by bigifre on 7/10/2015.
 */
public class AlphabetSorter extends RecyclerView.Adapter<AlphaBetViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    List<Alphabets> data= Collections.emptyList();
    Typefacer typeface= new Typefacer();

    public AlphabetSorter(Context context, List<Alphabets> data) {
        inflater= LayoutInflater.from(context);
        this.data=data;
        this.context=context;
    }


    @Override
    public AlphaBetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= inflater.inflate(R.layout.items_sorter,parent,false);

        AlphaBetViewHolder holder = new AlphaBetViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(AlphaBetViewHolder holder, int position) {
        Alphabets alphabet= data.get(position);
        holder.title.setText(alphabet.alphabet);

        //alphabetText = (TextView)layout.findViewById(R.id.txtAlphabets);
      //  alphabetText.setTypeface(typefacer.getRoboCondensedRegular(getActivity().getAssets()));
        holder.title.setTypeface(typeface.getRoboCondensedRegular(context.getAssets()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
class AlphaBetViewHolder extends RecyclerView.ViewHolder{

    TextView title;

    public AlphaBetViewHolder(View itemView) {
        super(itemView);


// TxtUsername.setTypeface(typefacer.getRoboCondensedRegular(getActivity().getAssets()));

        title=(TextView)itemView.findViewById(R.id.txtAlphabets);


        //icon.setOnClickListener(this);
        //numberPicker =(NumberPicker)itemView.findViewById(R.id.numberPicker);




    }



}