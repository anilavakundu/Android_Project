package com.example.lavo07.testapp1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.List;

/**
 * Created by lavo07 on 7/17/2017.
 */

public class FullimageActivity extends Activity {
    TouchImageView img;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.fullimageview);
        Intent i = getIntent();
        int position = i.getExtras().getInt("id");
        List<String> urls =i.getExtras().getStringArrayList("urls");
        ImageAdapter imageAdapter = new ImageAdapter(this,null);
        img =new TouchImageView(this);
        img.setBackgroundColor(0xFF000000);
        new DownloadImage().execute(urls.get(position));
    }
    class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... URL) {
            String imageURL ="http://upload.hopto.org:3000/"+URL[0];
            Bitmap bitmap = null;
            try {
                InputStream input = new java.net.URL(imageURL).openStream();
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                Log.i("Response",e.toString());
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            img.setImageBitmap(result);
            img.setMaxZoom(4f);
            setContentView(img);
        }
    }
}
