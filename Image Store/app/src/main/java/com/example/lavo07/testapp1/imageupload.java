package com.example.lavo07.testapp1;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

/**
 * Created by lavo07 on 7/18/2017.
 */


public class imageupload extends AsyncTask<String,Void ,Integer > {
        Context context;
        int response;
        imageupload(Context aa){
            context=aa;
        }
        @Override
        protected Integer doInBackground(String... strings) {
            try {
                for(int i=0;i<strings.length;i++) {
                    File file = new File(strings[i]);
                    String file_name = file.getName();
                    Multipartutility util = new Multipartutility("http://upload.hopto.org:3000/post");
                    util.addfile(file_name, file);
                    response = util.finish();
                }
            }
            catch (Exception e){
                Log.i("Response",e.toString());
            }
            return response;
        }

        @Override
        protected void onPostExecute(Integer s) {
            if(s==200)
                Toast.makeText(context,"Uploaded Succesfully!",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(context,"Check Server!",Toast.LENGTH_SHORT).show();
        }
    }
