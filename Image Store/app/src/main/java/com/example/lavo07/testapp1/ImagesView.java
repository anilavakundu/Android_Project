package com.example.lavo07.testapp1;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by lavo07 on 7/16/2017.
 */

public class ImagesView extends Fragment {
    SwipeRefreshLayout s_refresh;
    GridView gridView;
    View view;
    List<String> fl_nm = null;
    String[] path = null;
    static int[] selected;
    static boolean toggle=false;
    List<String> sel_names=null;
    FloatingActionButton f_button;
    FloatingActionButton f_down;
    URL url;
    HttpURLConnection conn;
    File picDirectory;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.images_view, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        view = getView();
        f_button = view.findViewById(R.id.fabup);
        f_down=view.findViewById(R.id.fabdown);
        new get_urls().execute();
        s_refresh = view.findViewById(R.id.swiperefresh);

        s_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                s_refresh.setRefreshing(false);
                new get_urls().execute();
            }
        });
        f_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, 42);
            }
        });
        f_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // Toast.makeText(getContext(),Environment.getExternalStorageDirectory().toString(),Toast.LENGTH_SHORT).show();
                    picDirectory = new File(Environment.getExternalStorageDirectory(),"TestappPictures");
                    if (!picDirectory.exists())
                        picDirectory.mkdirs();
                    new DownloadFIle(getContext()).execute(sel_names);
                }
                catch (Exception e){
                    Log.i("Response",e.toString());
                }
            }
        });
    }

    class get_urls extends AsyncTask<Void, Void, Integer> {
        List<String> f_names = new ArrayList<String>();
        BufferedReader reader;
        URL url;
        HttpURLConnection conn;
        GridView gg;
        Context c;


        @Override
        protected Integer doInBackground(Void... voids) {
            int response = 0;
            try {
                url = new URL("http://upload.hopto.org:3000/images/files");
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(3000);
                conn.setReadTimeout(3000);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("User-Agent", "Mozilla/5.0");
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    f_names.add(line);
                }
                reader.close();
                response = conn.getResponseCode();
            } catch (Exception e) {
                Log.i("Response", e.toString());
            }
            return response;
        }

        @Override
        protected void onPostExecute(Integer r) {
            if(r==200) {
                fl_nm = f_names;
                view = getView();
                gridView = view.findViewById(R.id.gridview);
                final ImageAdapter myadapter = new ImageAdapter(getContext(), fl_nm);
                gridView.setAdapter(myadapter);
                try {
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View v,
                                                int position, long id) {
                            if (!toggle) {
                                Intent i = new Intent(getContext(), FullimageActivity.class);
                                i.putExtra("id", position);
                                i.putExtra("urls", (ArrayList<String>) fl_nm);
                                startActivityForResult(i, 0);
                            } else {
                                if (selected[position] == 1) {
                                    selected[position] = 0;
                                    for (int i = 0; i < sel_names.size(); i++) {
                                        if (sel_names.get(i).equals(fl_nm.get(position))) {
                                            sel_names.remove(i);
                                            //Toast.makeText(getContext(),fl_nm.get(position),Toast.LENGTH_SHORT).show();
                                            break;
                                        }
                                    }
                                } else {
                                    selected[position] = 1;
                                    sel_names.add(fl_nm.get(position));
                                    //Toast.makeText(getContext(),fl_nm.get(position),Toast.LENGTH_SHORT).show();
                                }
                                myadapter.notifyDataSetChanged();
                            }
                        }
                    });
                    gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                            view.performHapticFeedback(View.HAPTIC_FEEDBACK_ENABLED);
                            if (!toggle) {
                                selected = new int[fl_nm.size()];
                                sel_names = new ArrayList<String>();
                                selected[gridView.getPositionForView(view)] = 1;
                                sel_names.add(fl_nm.get(gridView.getPositionForView(view)));
                                f_button.setVisibility(View.GONE);
                                f_down.setVisibility(View.VISIBLE);
                                toggle = true;
                                myadapter.notifyDataSetChanged();

                                //Toast.makeText(getContext(), "True", Toast.LENGTH_SHORT).show();
                            } else {
                                toggle = false;
                                f_down.setVisibility(View.GONE);
                                f_button.setVisibility(View.VISIBLE);
                                myadapter.notifyDataSetChanged();
                                //Toast.makeText(getContext(), "false", Toast.LENGTH_SHORT).show();

                            }
                            return true;
                        }
                    });
                } catch (Exception e) {
                    Log.i("Response", e.toString());
                }
            }
            else {
                Toast.makeText(getContext(),"Server is not connected!",Toast.LENGTH_LONG).show();
            }
        }


    }

     public void onActivityResult(int requestCode, int resultCode, Intent data) {
        File file;
        Uri uri;
        switch (requestCode) {
            case 42:
                if (resultCode == RESULT_OK) {
                    if (data.getData() != null) {
                        path = new String[1];
                        uri = data.getData();
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                                path[0] = get_file(uri);
                            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                                path[0] = getDataColumn(getContext(), uri, null, null);
                            }
                        } else {
                            file = new File(uri.toString());
                            try {
                                path[0] = URLDecoder.decode(file.getAbsolutePath(), "UTF-8");
                            } catch (Exception e) {
                                Log.i("Response", e.toString());
                            }
                            path[0] = path[0].substring(path[0].indexOf(':') + 1);
                        }
                    } else {
                        ClipData clipdata = data.getClipData();
                        path = new String[clipdata.getItemCount()];
                        for (int i = 0; i < clipdata.getItemCount(); i++) {
                            uri = clipdata.getItemAt(i).getUri();
                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                                if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                                    path[i] = get_file(uri);
                                } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                                    path[i] = getDataColumn(getContext(), uri, null, null);
                                }
                            } else {
                                file = new File(uri.toString());
                                try {
                                    path[i] = URLDecoder.decode(file.getAbsolutePath(), "UTF-8");
                                } catch (Exception e) {
                                    Log.i("Response", e.toString());
                                }
                                path[i] = path[i].substring(path[i].indexOf(':') + 1);
                            }
                        }
                    }

                    if (path != null)
                        new imageupload(getContext()).execute(path);
                    else
                        Toast.makeText(getContext(), "Please Select File!", Toast.LENGTH_SHORT).show();
                }
        }
    }

    String get_file(Uri uri) {
        final String docId = DocumentsContract.getDocumentId(uri);
        final String[] split = docId.split(":");
        final String type = split[0];
        Uri contentUri = null;
        if ("image".equals(type)) {
            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else if ("video".equals(type)) {
            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        } else if ("audio".equals(type)) {
            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }

        final String selection = "_id=?";
        final String[] selectionArgs = new String[]{
                split[1]
        };
        return getDataColumn(getContext(), contentUri, selection, selectionArgs);
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } catch (Exception e) {
            Log.i("Response", e.toString());
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
    public static int[] get_value(){
        if(!toggle){
            return null;
        }
        return selected;
    }
}

