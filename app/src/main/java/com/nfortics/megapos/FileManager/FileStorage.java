package com.nfortics.megapos.FileManager;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by bigifre on 7/29/2015.
 */
public class FileStorage extends Activity{




    public void makeDirectory(String path,Context ctx){

       try {


           FileOutputStream fos= ctx.openFileOutput(path, ctx.MODE_PRIVATE);
           fos.write( path.getBytes());
           fos.close();
       }catch(Exception ex){


       }
   }
    public void makeLoginCacheDirectory(Context ctx){

               try{

                   File file=new File(getCacheDir(),"Logins");
                   if(!file.exists()){
                       file.mkdir();

                   }

               }catch(Exception e){



               }

           }
    public void makeLoginDExternalLoginDirectory(String filename,Context ctx){
        File folder=getExternalFilesDir("Logins");
        File myfilename=new File(folder,filename);
      //  ctx.wr




    }
}
