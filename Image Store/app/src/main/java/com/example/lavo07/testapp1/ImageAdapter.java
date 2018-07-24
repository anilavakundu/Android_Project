package com.example.lavo07.testapp1;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by lavo07 on 7/16/2017.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private String[] mThumbIds;

    public ImageAdapter(Context c,List<String> urls) {
        mContext = c;
        if(urls!=null) {
            mThumbIds = new String[urls.size()];
            for (int i = 0; i < urls.size(); i++) {
                mThumbIds[i] = "http://upload.hopto.org:3000/" + urls.get(i);
            }
        }
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        int[] f_sel=ImagesView.get_value();
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(400, 400));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        Picasso.with(mContext)
                .load(mThumbIds[position])
                .placeholder(R.drawable.loader)
                .fit()
                .centerCrop().into(imageView);
        if(f_sel!=null) {
            if (f_sel[position]==1) {
                imageView.setBackgroundColor(0xFF0000FF);
            }
            else {
                imageView.setBackgroundColor(Color.WHITE);
            }
        }
        else {
            imageView.setBackgroundColor(Color.WHITE);
        }
        return imageView;
    }
}
