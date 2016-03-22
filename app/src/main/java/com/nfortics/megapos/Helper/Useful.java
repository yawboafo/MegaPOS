package com.nfortics.megapos.Helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.nfortics.megapos.Adapters.ItemsAdapter;
import com.nfortics.megapos.Models.Alphabets;
import com.nfortics.megapos.R;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by bigifre on 7/9/2015.
 */
public class Useful {
    Locale locale = new Locale("GH", "GH");
    Currency currency = Currency.getInstance(locale);
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
    public  static List<Alphabets> getAlphaBets() {

        List<Alphabets> data = new ArrayList<>();

        String [] alphabets={"View","Categories","Favorites","All","A","B","C",
                "D","E","F","G","H","I","J",
                "K","L","M","N","O","P","Q",
                "R","S","T","U","V","W",
                "X","Y","Z"};


        for(int i=0;i<alphabets.length;i++){


            Alphabets alphabet= new Alphabets();
            alphabet.alphabet=alphabets[i];
            data.add(alphabet);
        }

        return data;

    }

    public void InitializeSummaryBoard(TextView subTotalValue,
                                       TextView taxValue,
                                       TextView discountValue,
                                       TextView totalValue,
                                       ItemsAdapter itemsAdapter ,TextView Seconscreeen){

        subTotalValue.setText(formatMoney(itemsAdapter.getSubTotal()));
        taxValue.setText(formatMoney(0.0));
        discountValue.setText(formatMoney(0.0));
        totalValue.setText(formatMoney(itemsAdapter.getSubTotal()));
        Seconscreeen.setText(" " +itemsAdapter.getAllItemsBoughtQty() + "   ,   " + formatMoney(itemsAdapter.getSubTotal())+" ");

    }
    public void ClearSummaryBoard(TextView subTotalValue,
                                       TextView taxValue,
                                       TextView discountValue,
                                       TextView totalValue,TextView Seconscreeen){

        subTotalValue.setText(formatMoney(0.0));
        taxValue.setText(formatMoney(0.0));
        discountValue.setText(formatMoney(0.0));
        totalValue.setText(formatMoney(0.0));
        Seconscreeen.setText(" " +"0" + ", " +"0.0"+" ");

    }




    public  String generateReceipt(
            String shopname,
            String shopbranch,
            String userID)
    {

        SimpleDateFormat  sdf = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");
        Calendar calendar = new GregorianCalendar();
        int year       = calendar.get(Calendar.YEAR);
        int month      = calendar.get(Calendar.MONTH); // Jan = 0, dec = 11
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int dayOfWeek  = calendar.get(Calendar.DAY_OF_WEEK);
        int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
        int weekOfMonth= calendar.get(Calendar.WEEK_OF_MONTH);

        int hour       = calendar.get(Calendar.HOUR);        // 12 hour clock
        int hourOfDay  = calendar.get(Calendar.HOUR_OF_DAY); // 24 hour clock
        int minute     = calendar.get(Calendar.MINUTE);
        int second     = calendar.get(Calendar.SECOND);
        int millisecond= calendar.get(Calendar.MILLISECOND);

       // String _transactionType=transactionType.substring(0,1);

        String _shopname=shopname.substring(0, 1);;
        String _year=String.valueOf(year).substring(2);
        String millisecondplussecond=String.valueOf(second)+String.valueOf(millisecond);
        String millSec=millisecondplussecond.substring(2);


        return minute+_year+_shopname+second+millSec;
        //015-KoOs-221
        //015KoOs221-466
        //015KO-221-352
        //15KO-221-7
    }

    public String formatMoney(double money){

        NumberFormat df = NumberFormat.getCurrencyInstance();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setCurrencySymbol("Ghs ");
        dfs.setGroupingSeparator(',');
        dfs.setMonetaryDecimalSeparator('.');
        ((DecimalFormat) df).setDecimalFormatSymbols(dfs);

        return df.format(money);
    }
    public String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }
    public String  prepareString4double(String string){

        String prepareString=string.substring(3);
        String finalPreparedString=prepareString.replace(",","");

        return finalPreparedString;
    }
    public String cleanAstring(String value,int StartIndex){

        return value.substring(StartIndex);
    }
    public String getDateWithFormatter(){

        DateFormat dateFormat= new SimpleDateFormat("EEE, d MMM yyyy");
        Calendar cal=Calendar.getInstance();

        String dateNow=dateFormat.format(cal.getTime());

        return dateNow;
    }
    public String getTimeWithFormatter(){

        DateFormat dateFormat= new SimpleDateFormat("hh:mm a");
        Calendar cal=Calendar.getInstance();

        String dateNow=dateFormat.format(cal.getTime());

        return dateNow;
    }
}
