package com.example.lavo07.testapp1;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.net.URLDecoder;

public class MainActivity extends AppCompatActivity {
    PagerAdapter adapter;
    FloatingActionButton f_button;
    String[] path=null;
    String[] perms = {"android.permission.WRITE_EXTERNAL_STORAGE"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getpermission(perms);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("Images"));
        tabs.addTab(tabs.newTab().setText("Downloads"));
        //tabs.addTab(tabs.newTab().setText("Tab 3"));
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new PagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ImagesView(), "Images");
        adapter.addFragment(new DownImagesView(),"Downloads");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
        tabs.getTabAt(1).select();
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    boolean shouldAskPermission(){

        return(Build.VERSION.SDK_INT> Build.VERSION_CODES.LOLLIPOP_MR1);

    }
    boolean hasPermission(String[] permission){
        if(shouldAskPermission()){
            return (checkSelfPermission(permission[0])== PackageManager.PERMISSION_GRANTED);

        }
        return true;
    }
    void getpermission(String[] permissions ){
        if(!hasPermission(permissions)){
            int permsRequestCode = 200;
            requestPermissions(permissions, permsRequestCode);
        }
    }
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults){

        switch(permsRequestCode){

            case 200:

                boolean writeAccepted = grantResults[0]==PackageManager.PERMISSION_GRANTED;

                break;

        }

    }
}
