package com.example.lavo07.testapp1;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lavo07 on 8/20/2017.
 */

public class DownImagesView extends Fragment {
    View view;
    SwipeRefreshLayout swipe;
    GridView gridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.downloaded_images, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        view = getView();
        swipe = view.findViewById(R.id.swiperef);
        gridView = view.findViewById(R.id.gridv);
        new getfile().execute();
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
                new getfile().execute();
            }

        });
    }

    class getfile extends AsyncTask<Void, Void, ArrayList<String>> {
        File file;
        File[] listfile;
        ArrayList<String> f = new ArrayList<String>();

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            file = new File(Environment.getExternalStorageDirectory(), "TestappPictures");
            if (file.isDirectory()) {
                listfile = file.listFiles();
                for (int i = 0; i < listfile.length; i++) {
                    f.add(listfile[i].getAbsolutePath());
                }
            }
            return f;
        }

        @Override
        protected void onPostExecute(final ArrayList<String> strings) {
            downImageAdapter imgadapter = new downImageAdapter(getContext(), strings);
            gridView.setAdapter(imgadapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    Intent i= new Intent(getContext(),Download_Fullimage_Activity.class);
                    i.putExtra("pos",position);
                    i.putExtra("files",(ArrayList<String>) strings);
                    try {
                        startActivityForResult(i, 0);
                    }
                    catch (Exception e){
                        Log.i("Response",e.toString());
                    }
                    //Toast.makeText(getContext(), strings.get(position),Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
