package com.joonhee_pak.stricfilm;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.joonhee_pak.stricfilm.utility.AsyncUtility;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {
    @BindView(R.id.title) TextView titleTextView;
    @BindView(R.id.movieRelease) TextView movieRelease;
    @BindView(R.id.movieRating) TextView movieRating;
    @BindView(R.id.movieSynopsis) TextView movieSynopsis;
    @BindView(R.id.grid_item_image) ImageView imageView;
    @BindView(R.id.button) Button buttonTrailerOne;
    @BindView(R.id.button2) Button buttonTrailerTwo;
//    @BindView(R.id.buttonFavorite) Button buttonFavorite;
    @BindView(R.id.trailers) TextView trailers;
    @BindView(R.id.summary) TextView summary;
    @BindView(R.id.button3) Button buttonReviews;
    private ArrayList<String> videoList = new ArrayList<>();
    private ArrayList<String> reviewsList = new ArrayList<>();
    private Movies item = new Movies();
    private ArrayList<String> returnedReviews;
    private static final String BASE_MOVIE_URL = "http://api.themoviedb.org/3/movie/";
    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w185";
    String title;
    String image;
    String releaseDate;
    String rating;
    String synopsis;
    int id;
    Bundle bundle;

    public DetailsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
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
        int height = metrics.heightPixels;
        float scaleFactor = metrics.density;
        float widthDp = width / scaleFactor;
        float heightDp = height / scaleFactor;

        if (widthDp < 600 || heightDp < 500) {
            Bundle bundle = getActivity().getIntent().getExtras();
            title = bundle.getString("title");
            image = BASE_IMAGE_URL + bundle.getString("image");
            releaseDate = bundle.getString("releaseDate");
            rating = bundle.getString("rating");
            synopsis = bundle.getString("synopsis");
            id = bundle.getInt("id");
        } else if (bundle == null) {
            ButterKnife.bind(this, rootview);
            titleTextView.setVisibility(View.INVISIBLE);
            buttonTrailerOne.setVisibility(View.INVISIBLE);;
            buttonTrailerTwo.setVisibility(View.INVISIBLE);
//            buttonFavorite.setVisibility(View.INVISIBLE);
            trailers.setVisibility(View.INVISIBLE);
            summary.setVisibility(View.INVISIBLE);
            buttonReviews.setVisibility(View.INVISIBLE);
            return rootview;
        } else if (bundle == getArguments()) {
            title = bundle.getString("title");
            image = BASE_IMAGE_URL + bundle.getString("image");
            releaseDate = bundle.getString("releaseDate");
            rating = bundle.getString("rating");
            synopsis = bundle.getString("synopsis");
            id = bundle.getInt("id");
        }

        try {
            returnedReviews = new ArrayList<String>(new AsyncVideoTask().execute(BASE_MOVIE_URL + id + "/reviews" + "?api_key=a247f9509512beb8588090c3d377d6c9").get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        new AsyncVideoTask().execute(BASE_MOVIE_URL + id + "/videos" + "?api_key=a247f9509512beb8588090c3d377d6c9");

        ButterKnife.bind(this, rootview);
        
        buttonTrailerOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(videoList.get(0))));
            }
        });

        buttonTrailerTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoList.size() == 1) {
                    Toast.makeText(getActivity().getApplicationContext(), "Sorry no second trailer =(", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(videoList.get(1))));
                }
            }
        });

//        buttonFavorite.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                SharedPreferences settings = getActivity().getSharedPreferences("favoritesArray", getActivity().MODE_PRIVATE);
//                SharedPreferences.Editor editor = settings.edit();
//                String longString = settings.getString("longString", "").concat("|" + title.concat("|" + image));
//                editor.putString("longString", longString);
//                editor.apply();
//            }
//        });

        buttonReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (returnedReviews.size() == 0){
                    Toast.makeText(getActivity().getApplicationContext(), "Sorry this movie has no reviews =(", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getActivity(), ReviewsList.class);
                    intent.putStringArrayListExtra("reviewsArray", returnedReviews);
                    startActivity(intent);
                }
            }
        });

        titleTextView.setText(Html.fromHtml(title));
        movieRelease.setText(releaseDate);
        String divideTen = rating + "/10";
        movieRating.setText(divideTen);
        movieSynopsis.setText(synopsis);
        Picasso.with(getActivity())
                .load(image)
                .placeholder(R.drawable.user_placeholder_error)
                .error(R.drawable.user_placeholder)
                .into(imageView);

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
                String response = AsyncUtility.streamToString(stream);
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
                    item = new Movies();
                    JSONObject post = posts.optJSONObject(i);
                    String review = post.optString("content");
                    String author = post.optString("author");
                    String reviewAuthor = "\n" + review.concat("\n" + "\n" + "Review By: " + author + "\n");
                    reviewsList.add(reviewAuthor);
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
