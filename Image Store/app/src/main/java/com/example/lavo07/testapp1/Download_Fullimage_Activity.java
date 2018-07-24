package com.example.lavo07.testapp1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import static android.R.attr.bitmap;

/**
 * Created by lavo07 on 11/23/2017.
 */

public class Download_Fullimage_Activity extends Activity {
    TouchImageView img;
    Bitmap my_bit;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        img=new TouchImageView(this);
        img.setBackgroundColor(0xFF000000);
        Intent i=getIntent();
        int position=i.getExtras().getInt("pos");
        List<String> files=i.getExtras().getStringArrayList("files");
        new set_image().execute(files.get(position));

    }
    class set_image extends AsyncTask<String,Void,Bitmap>{
        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                my_bit = BitmapFactory.decodeFile(strings[0]);
            }
            catch (Exception e){
                Log.i("Response",e.toString());
            }
            return my_bit;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            img.setImageBitmap(bitmap);
            img.setMaxZoom(4f);
            setContentView(img);
        }
    }
}
