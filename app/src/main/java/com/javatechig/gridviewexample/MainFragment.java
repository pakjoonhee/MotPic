package com.javatechig.gridviewexample;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    private static final String TAG = MainActivity.class.getSimpleName();
    private GridView mGridView;
    private GridViewAdapter mGridAdapter;
    private ArrayList<GridItem> mGridData;
    private String popularMoviesUrl = "http://api.themoviedb.org/3/movie/popular?api_key=a247f9509512beb8588090c3d377d6c9";
    private String highestRatedUrl = "http://api.themoviedb.org/3/movie/top_rated?api_key=a247f9509512beb8588090c3d377d6c9";
    private boolean menuIsInflated;


    public MainFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        if(savedInstanceState == null) {
            mGridData = new ArrayList<>();
        } else {
            mGridData = savedInstanceState.getParcelableArrayList("movies");
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies", mGridData);
        super.onSaveInstanceState(outState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mGridView = (GridView) rootView.findViewById(R.id.gridView);


        mGridAdapter = new GridViewAdapter(getActivity(), R.layout.grid_item_layout, mGridData);
        mGridView.setAdapter(mGridAdapter);



        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                DisplayMetrics metrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int width = metrics.widthPixels;
                int height = metrics.heightPixels;
                float scaleFactor = metrics.density;
                float widthDp = width / scaleFactor;
                float heightDp = height / scaleFactor;

                GridItem item = (GridItem) parent.getItemAtPosition(position);
                if (widthDp < 600 || heightDp < 500) {
                    Intent intent = new Intent(getActivity(), DetailsActivity.class);

                    intent.putExtra("title", item.getTitle()).
                            putExtra("image", item.getImage()).
                            putExtra("releaseDate", item.getReleaseDate()).
                            putExtra("rating", item.getRating()).
                            putExtra("id", item.getId()).
                            putExtra("synopsis", item.getSynopsis());

                    startActivity(intent);

                } else {

                    Bundle args = new Bundle();
                    args.putString("title", item.getTitle());
                    args.putString("image", item.getImage());
                    args.putString("releaseDate", item.getReleaseDate());
                    args.putString("rating", item.getRating());
                    args.putInt("id", item.getId());
                    args.putString("synopsis", item.getSynopsis());

                    DetailsFragment fragment = new DetailsFragment();
                    fragment.setArguments(args);

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container3, fragment, "TAG")
                            .commit();
                }
            }
        });

        return rootView;
    }

    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {
        HttpURLConnection connection = null;

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();

                String response = streamToString(stream);
                if (params[0] == popularMoviesUrl) {
                    parseResult(response);
                    result = 1;
                } else if (params[0] == highestRatedUrl) {
                    parseResult(response);
                    result = 1;
                }

            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }

            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {

            if (result == 1) {
                mGridAdapter.setGridData(mGridData);
            } else {
                Toast.makeText(getActivity(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    static String streamToString(InputStream stream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        String line;
        String result = "";
        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

        if (null != stream) {
            stream.close();
        }
        return result;
    }

    private void parseResult(String result) {
        try {
            JSONObject response = new JSONObject(result);
            JSONArray posts = response.optJSONArray("results");
            GridItem item;
            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.optJSONObject(i);
                String title = post.optString("title");
                String image = post.optString("poster_path");
                String finalImage = "http://image.tmdb.org/t/p/w185" + image;
                String releaseDate = post.optString("release_date").substring(0,4);
                String rating = post.optString("vote_average");
                String synopsis = post.optString("overview");
                int id = post.optInt("id");
                item = new GridItem();
                item.setTitle(title);
                item.setImage(finalImage);
                item.setReleaseDate(releaseDate);
                item.setRating(rating);
                item.setSynopsis(synopsis);
                item.setId(id);
                mGridData.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (!menuIsInflated) {
            inflater.inflate(R.menu.menu_main, menu);
            menuIsInflated = true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.popular_movies:
                if (mGridData.size() >= 20) {
                    mGridData.clear();
                }
                new AsyncHttpTask().execute(popularMoviesUrl);
                break;


            case R.id.highest_rated:
                if (mGridData.size() >= 20) {
                    mGridData.clear();
                }
                new AsyncHttpTask().execute(highestRatedUrl);
                break;

            case R.id.favorites:
                Intent intent = new Intent(getActivity(), FavoriteList.class);
                this.startActivity(intent);
                break;
        }
        return true;
    }

}
