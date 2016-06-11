package com.javatechig.gridviewexample;


import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;

public class MainActivity extends ActionBarActivity {
    public boolean twoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        float scaleFactor = metrics.density;
        float widthDp = width / scaleFactor;
        twoPane = widthDp >= 600;
        if (widthDp < 600 && savedInstanceState == null) {
            setContentView(R.layout.activity_gridview);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MainFragment())
                    .commit();
        } else {
            setContentView(R.layout.two_panel_tablet);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container3, new DetailsFragment())
                    .commit();
        }

    }

}

