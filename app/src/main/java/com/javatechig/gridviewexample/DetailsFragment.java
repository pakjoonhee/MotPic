package com.javatechig.gridviewexample;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {
    private TextView titleTextView;
    private TextView movieRelease;
    private TextView movieRating;
    private TextView movieSynopsis;
    private ImageView imageView;
    private ArrayList<String> videoList = new ArrayList<>();
    private ArrayList<String> reviewsList = new ArrayList<>();
    private GridItem item = new GridItem();
    private ArrayList<String> returnedReviews;
    String title;
    String image;
    String releaseDate;
    String rating;
    String synopsis;
    int id;
    Bundle bundle;

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
        bundle = getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview =  inflater.inflate(R.layout.fragment_details, container, false);
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        float scaleFactor = metrics.density;
        float widthDp = width / scaleFactor;
        boolean onePane = widthDp < 600;

        if (onePane) {
            Bundle bundle = getActivity().getIntent().getExtras();
            title = bundle.getString("title");
            image = bundle.getString("image");
            releaseDate = bundle.getString("releaseDate");
            rating = bundle.getString("rating");
            synopsis = bundle.getString("synopsis");
            id = bundle.getInt("id");
        } else if (bundle == null) {
            titleTextView = (TextView) rootview.findViewById(R.id.title);
            titleTextView.setVisibility(View.INVISIBLE);
            Button buttonOne = (Button)rootview.findViewById(R.id.button);
            buttonOne.setVisibility(View.INVISIBLE);
            Button buttonTwo = (Button)rootview.findViewById(R.id.button2);
            buttonTwo.setVisibility(View.INVISIBLE);
            Button buttonFavorite = (Button)rootview.findViewById(R.id.buttonFavorite);
            buttonFavorite.setVisibility(View.INVISIBLE);
            TextView trailers = (TextView) rootview.findViewById(R.id.trailers);
            trailers.setVisibility(View.INVISIBLE);
            return rootview;
        } else if (bundle == getArguments()) {
            title = bundle.getString("title");
            image = bundle.getString("image");
            releaseDate = bundle.getString("releaseDate");
            rating = bundle.getString("rating");
            synopsis = bundle.getString("synopsis");
            id = bundle.getInt("id");
        }

        try {
            returnedReviews = new ArrayList<String>(new AsyncVideoTask().execute("http://api.themoviedb.org/3/movie/" + id + "/reviews" + "?api_key=a247f9509512beb8588090c3d377d6c9").get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        new AsyncVideoTask().execute("http://api.themoviedb.org/3/movie/" + id + "/videos" + "?api_key=a247f9509512beb8588090c3d377d6c9");

        Button buttonOne = (Button)rootview.findViewById(R.id.button);
        buttonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(videoList.get(0))));
            }
        });

        Button buttonTwo = (Button)rootview.findViewById(R.id.button2);
        buttonTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoList.size() == 1) {
                    Toast.makeText(getActivity().getApplicationContext(), "Sorry no second trailer =(", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(videoList.get(1))));
                }
            }
        });

        Button buttonFavorite = (Button)rootview.findViewById(R.id.buttonFavorite);
        buttonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences settings = getActivity().getSharedPreferences("favoritesArray", getActivity().MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                String longString = settings.getString("longString", "").concat("|" + title.concat("|" + image));
                editor.putString("longString", longString);
                editor.apply();
            }
        });

        Button buttonReviews = (Button)rootview.findViewById(R.id.button3);
        buttonReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ReviewsList.class);
                intent.putStringArrayListExtra("reviewsArray", returnedReviews);
                startActivity(intent);
            }
        });

        titleTextView = (TextView) rootview.findViewById(R.id.title);
        titleTextView.setText(Html.fromHtml(title));

        movieRelease = (TextView) rootview.findViewById(R.id.movieRelease);
        movieRelease.setText(releaseDate);

        movieRating = (TextView) rootview.findViewById(R.id.movieRating);
        String divideTen = rating + "/10";
        movieRating.setText(divideTen);

        movieSynopsis = (TextView) rootview.findViewById(R.id.movieSynopsis);
        movieSynopsis.setText(synopsis);

        imageView = (ImageView) rootview.findViewById(R.id.grid_item_image);
        Picasso.with(getActivity()).load(image).into(imageView);


        return rootview;

    }

    public class AsyncVideoTask extends AsyncTask<String, Void, ArrayList<String>> {
        HttpURLConnection connection = null;

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            ArrayList<String> result = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                String response = MainFragment.streamToString(stream);
                if (params[0].contains("videos")) {
                    result = parseId(response);
                } else {
                    result = parseReview(response);
                }

            } catch (Exception e) {
                Log.d("tag", e.getLocalizedMessage());
            }
            return result;
        }

        private ArrayList<String> parseId(String result) {
            try {
                JSONObject response = new JSONObject(result);
                JSONArray posts = response.optJSONArray("results");
                for (int i = 0; i < posts.length() && i < 2; i++) {
                    JSONObject post = posts.optJSONObject(i);
                    String id = post.optString("key");
                    String youTube = "https://www.youtube.com/watch?v=" + id;
                    videoList.add(youTube);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return videoList;
        }

        private ArrayList<String> parseReview(String result) {
            try {
                JSONObject response = new JSONObject(result);
                JSONArray posts = response.optJSONArray("results");
                for (int i = 0; i < posts.length(); i++) {
                    item = new GridItem();
                    JSONObject post = posts.optJSONObject(i);
                    String review = post.optString("content");
                    String author = post.optString("author");
                    reviewsList.add(review);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return reviewsList;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            super.onPostExecute(result);

        }
    }

}
