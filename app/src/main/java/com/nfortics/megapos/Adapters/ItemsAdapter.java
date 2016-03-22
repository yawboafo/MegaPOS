package com.nfortics.megapos.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.nfortics.megapos.Models.Items;
import com.nfortics.megapos.R;


import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by bigifre on 6/28/2015.
 */
public class ItemsAdapter extends RecyclerView.Adapter<myViewholder> {

    private Context context;
    private LayoutInflater inflater;
    public List<Items> data= Collections.emptyList();
    Useful useful=new Useful();
    private double subTotal;
    public int counter=2;
    DataSets dataSets;


    public ItemsAdapter(Context context, List<Items> data) {
        inflater= LayoutInflater.from(context);
        this.data=data;
        this.context=context;
        dataSets = new DataSets(context);
    }


    public int getCounter() {
        return counter;
    }
    public void setCounter(int counter) {
        this.counter = counter;
    }
    public int getselectedIemsIndata(){
        int i=0;
        for(Items s :data){

            if(s.select){

                i++;
            }

        }


        return i;

    }
    public void addItem(Items item){


        if(data.isEmpty()){

            data.add(0,item);
            notifyItemInserted(0);

        }else {
            String itemName="";
            int itemposition=0;
            boolean foundsomething=false;
            for (int i = 0; i < data.size(); i++) {
                if (item.name.equals(data.get(i).name)){
                       foundsomething=true;
                       itemposition=i;

                } else {


                }

            }

            if(foundsomething){

                if(item.name.contains("Item")) {
                    item.name = "Item " + counter;
                    counter = counter + 1;
                    data.add(0, item);

                }else if(itemName.contains("CashIn") ||
                        itemName.contains("CashOut") ||
                        itemName.contains("BillPaid") ||
                        itemName.contains("Airtime") ||
                        itemName.contains("CashOut")){
                    data.add(0, item);

                }else {
                    int tmpQz = Integer.parseInt(data.get(itemposition).quatity);
                    tmpQz++;
                    data.get(itemposition).quatity = "" + tmpQz;
                    double tmpAmt = Double.parseDouble(useful.prepareString4double(item.amount));
                    double finalAmt = tmpAmt * tmpQz;
                    data.get(itemposition).amount = useful.formatMoney(finalAmt);
                }
                //notifyItemChanged(itemposition);
            }else{
                data.add(0,item);
              //notifyItemInserted(0);


            }

        }

       notifyDataSetChanged();

    }
    public void latestAmount(Items items,int itemposition){

        int tmpQz=Integer.parseInt(data.get(itemposition).quatity);
        tmpQz=tmpQz-1;
        if(tmpQz<1){


        }else{

            data.get(itemposition).quatity=""+tmpQz;
            double tmpAmt=Double.parseDouble(items.amount);
            double finalAmt=tmpAmt*tmpQz;
            data.get(itemposition).amount=""+finalAmt;
            notifyItemChanged(itemposition);

        }



    }

    /***public void decreaseQuantity(Items items,int position){

        int qty=Integer.parseInt(data.get(position).quatity);

        if(qty<=1){


        }else{
            String tmpDoublePrice4Item = dataSets.getItemPrice(items.name);

            double tmpAmount = Double.parseDouble(tmpDoublePrice4Item);

            qty=qty-1;

            String preapresTrring= useful.prepareString4double(data.get(position).amount) ;



            double tmpAmt=Double.parseDouble(preapresTrring);
            double finalAmt=tmpAmount*qty;

            data.get(position).amount= useful.formatMoney(finalAmt);
            data.get(position).quatity=""+qty;

            //items.quatity=String.valueOf(qty);
           // items.amount=String.valueOf(finalAmt);
            notifyItemChanged(position);
        }

    }**/
    /**public void decreaseQuantity(Items items,int position,double tmpAmount){

        int qty=Integer.parseInt(data.get(position).quatity);

        if(qty<=1){


        }else{
            qty=qty-1;

            String preapresTrring= useful.prepareString4double(data.get(position).amount) ;



            double tmpAmt=Double.parseDouble(preapresTrring);
            double finalAmt=tmpAmount*qty;

            data.get(position).amount= useful.formatMoney(finalAmt);
            data.get(position).quatity=""+qty;

            //items.quatity=String.valueOf(qty);
            // items.amount=String.valueOf(finalAmt);
            notifyItemChanged(position);
        }

    }**/


    public int getAllItemsBoughtQty(){

        int qty=0;
        for(Items val : data){

          qty=qty+Integer.parseInt(val.quatity);


        }


        return  qty;
    }
    public void decreaseQuantity(int position,double tmpAmount){

        int qty=Integer.parseInt(data.get(position).quatity);

        if(qty<=1){


        }else{
            qty=qty-1;

            Log.d("QUANTITY",data.get(position).quatity+"item name "+data.get(position).name );
/*
*    double tmpAmt=Double.parseDouble(useful.prepareString4double(item.amount));
                double finalAmt=tmpAmt*tmpQz;



                data.get(itemposition).amount= useful.formatMoney(finalAmt);
*
* */

            String preapresTrring= useful.prepareString4double(data.get(position).amount) ;



            double tmpAmt=Double.parseDouble(preapresTrring);
            double finalAmt=tmpAmount*qty;

            data.get(position).amount= useful.formatMoney(finalAmt);
            data.get(position).quatity=""+qty;

            //items.quatity=String.valueOf(qty);
            // items.amount=String.valueOf(finalAmt);
            notifyItemChanged(position);
        }

    }
    public void IncreaseQuantity(int quantity,int position){

        int qty=Integer.parseInt(data.get(position).quatity);

               qty=qty+quantity;
               String tmpDoublePrice4Item;
              tmpDoublePrice4Item = dataSets.getItemPrice(String.valueOf(data.get(position).name));
        Log.d("ITEM AMOUNT",tmpDoublePrice4Item);
              String preapresTrring= useful.prepareString4double(data.get(position).amount) ;

            double originalAmt = Double.parseDouble(preapresTrring);

            double tmpAmt=Double.parseDouble(preapresTrring);
            double finalAmt=originalAmt*qty;

            data.get(position).amount= useful.formatMoney(finalAmt);
            data.get(position).quatity=""+qty;

            //items.quatity=String.valueOf(qty);
            // items.amount=String.valueOf(finalAmt);
            notifyItemChanged(position);


    }
    public void setItemSelect(Items item,int position){

        if(item.select){

              item.select=false;
              notifyItemChanged(position);
            }else{

              item.select=true;
            notifyItemChanged(position);
        }



     //   Toast.makeText(context, ""+data.get(position).select,Toast.LENGTH_LONG).show();

            }
    public void removechecked(){


    Iterator<Items> eunice=data.iterator();
    while (eunice.hasNext()){

        Items item=eunice.next();
        if(item.select==true){
            eunice.remove();

        }
    }


notifyDataSetChanged();



}
    public double getSubTotal(){

        double tmpsubtotal=0;
        for(int i=0;i<data.size();i++){

            String tmpAmount=useful.prepareString4double(data.get(i).amount);

            tmpsubtotal=tmpsubtotal+Double.parseDouble(tmpAmount);

        }


        return tmpsubtotal;
    }

    public String getQty(String name){
        String quantity="";
        for(Items s : data){
            if(s.name.equals(name)){

                quantity= s.quatity;
            }

        }
        return quantity;
    }

    public List<Items> getData() {
        return data;
    }

    @Override
    public myViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= inflater.inflate(R.layout.items_layout, parent, false);

        myViewholder holder = new myViewholder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(myViewholder holder, final int position) {
        Typefacer typeface= new Typefacer();
        Items items=data.get(position);


        if(items.name.contains("CashIn")|
                items.name.contains("CashOut")|
                items.name.contains("BillPaid")|
                items.name.contains("Airtime")){
            holder.itemname.setTextColor(Color.parseColor("#007B16"));
            holder.itemname.setText(items.name);
            holder.itemname.setTypeface(typeface.getRoboRegular(context.getAssets()));
         //   holder.itemquantity.setVisibility(View.GONE);
            holder.itemselect.setChecked(items.select);
            holder.itemcost.setText(items.amount);
            holder.itemcost.setTypeface(typeface.getRoboCondensedLight(context.getAssets()));
        }else{
          //  holder.itemquantity.setVisibility(View.VISIBLE);
             holder.itemquantity.setText(items.quatity);
            holder.itemname.setTextColor(Color.parseColor("#000000"));
            holder.itemname.setText(items.name  +"\t\t - " +" "+ items.quatity+" "  );
            holder.itemname.setTypeface(typeface.getRoboRegular(context.getAssets()));
            holder.itemselect.setChecked(items.select);
            holder.itemcost.setText(items.amount);
            holder.itemcost.setTypeface(typeface.getRoboCondensedLight(context.getAssets()));
        }



        //holder.itemIcon.setImageResource(items.icon);

    }



    public String returnItemNameBasedonPosition(int position){

        return data.get(position).name;
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
}

class myViewholder extends RecyclerView.ViewHolder{

    TextView itemname,itemcost;
   Button itemquantity;
    CheckBox itemselect;
    ImageView itemIcon;


    public myViewholder(View itemView) {
        super(itemView);

        itemname=(TextView)itemView.findViewById(R.id.itemname);
        itemcost=(TextView)itemView.findViewById(R.id.itemcost);
       itemquantity=(Button)itemView.findViewById(R.id.itemqty);

        itemselect=(CheckBox)itemView.findViewById(R.id.itemchk);
        if(itemselect.isChecked()){

            itemselect.setChecked(false);

        }else{

            itemselect.setChecked(true);
        }
      //  itemselect.setVisibility(View.GONE);



    }


}
