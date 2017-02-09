package com.joonhee_pak.motpic;



import android.os.Bundle;

import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;

public class MainActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        float scaleFactor = metrics.density;
        float widthDp = width / scaleFactor;
        float heightDp = height / scaleFactor;
        if (widthDp < 600 || heightDp < 500) {
            setContentView(R.layout.activity_gridview);
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new MainFragment())
                        .commit();
            }
        }
        else if (widthDp >= 600 || heightDp > 500){
            setContentView(R.layout.two_panel_tablet);
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container3, new DetailsFragment())
                        .commit();
            }

        }

    }

}

