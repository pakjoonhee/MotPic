package com.joonhee_pak.stricfilm;


import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;


public class DetailsActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details_view);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container2, new DetailsFragment())
                    .commit();
        }

    }

}
