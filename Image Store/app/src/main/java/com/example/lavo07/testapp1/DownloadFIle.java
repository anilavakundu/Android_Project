package com.example.lavo07.testapp1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lavo07 on 8/14/2017.
 */

public class DownloadFIle extends AsyncTask<List<String>,Void,Integer> {
    Context cont;
    String path;
    List<String> urls;
    DownloadFIle(Context aa){
        cont=aa;
    }
    @Override
    protected Integer doInBackground(List<String>... strings) {
        try {
            urls=new ArrayList<String>();
            urls=strings[0];
            for (int i = 0; i < urls.size(); i++) {
                path = "http://upload.hopto.org:3000/"+urls.get(i);
                get_file(path,urls.get(i));
            }
        }
        catch (Exception e){
            Log.i("Response",e.toString());
        }
        return 1;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        if(integer==1)
            Toast.makeText(cont,"Downloaded",Toast.LENGTH_SHORT).show();
    }

    public void get_file(String file_url,String fl_nm)throws IOException{
        String path= Environment.getExternalStorageDirectory()+"/TestappPictures/"+fl_nm;
        InputStream input=new java.net.URL(file_url).openStream();
        Bitmap temp= BitmapFactory.decodeStream(input);
        FileOutputStream output=new FileOutputStream(path);
        temp.compress(Bitmap.CompressFormat.JPEG,100,output);
        output.flush();
        input.close();
 }
}
