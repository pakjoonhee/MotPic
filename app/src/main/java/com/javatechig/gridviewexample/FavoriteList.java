package com.javatechig.gridviewexample;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class FavoriteList extends ActionBarActivity {
    private GridView tGridView;
    private GridViewAdapter mGridAdapter;
    private ArrayList<GridItem> mGridData;
    private GridItem item = new GridItem();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);

        SharedPreferences settings = getSharedPreferences("favoritesArray", MODE_PRIVATE);

        String longString = settings.getString("longString", null).substring(1);
        String[] split = longString.split("\\|");
        ArrayList<String> list = new ArrayList<String>(Arrays.asList(split));
        Set<String> set = new LinkedHashSet<>(list);
        set.addAll(list);
        list.clear();
        list.addAll(set);
        mGridData = new ArrayList<>();
        for (int i=0; i < list.size(); i+=2) {
            item = new GridItem();
            String title = list.get(i);
            item.setTitle(title);
            String image = list.get(i + 1);
            item.setImage(image);
            mGridData.add(item);
        }

        tGridView = (GridView) findViewById(R.id.gridView);
        mGridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, mGridData);
        tGridView.setAdapter(mGridAdapter);
        mGridAdapter.setGridData(mGridData);
    }
}
