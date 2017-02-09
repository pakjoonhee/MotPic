package com.joonhee_pak.motpic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * Created by joonheepak on 6/12/16.
 */
public class ReviewsList extends Activity {
    ListView listView;
    private ArrayList<String> reviewsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_reviews);

        listView = (ListView) findViewById(R.id.listView);
        Intent i = getIntent();
        reviewsList = i.getStringArrayListExtra("reviewsArray");

        String[] reviewValues = new String[reviewsList.size()];
        reviewValues = reviewsList.toArray(reviewValues);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, reviewValues);

        listView.setAdapter(adapter);
    }
}
