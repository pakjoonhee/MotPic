package com.joonhee_pak.stricfilm;


import android.content.Intent;
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

import com.joonhee_pak.stricfilm.data.MoviesResponse;
import com.joonhee_pak.stricfilm.utility.ApiClient;
import com.joonhee_pak.stricfilm.utility.ApiInterface;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    private static final String TAG = "Sorry not working";
    private GridView mGridView;
    private GridViewAdapter mGridAdapter;
    private ArrayList<Movies> mGridData;
    private ArrayList<Movies> blah3;
    private Bundle args;
    private final static String API_KEY = "a247f9509512beb8588090c3d377d6c9";
    List<Movies> movies;
    List<Movies> movies2;
    ArrayList<Movies> blah4;


    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        mGridData = new ArrayList<>();
        movies = new ArrayList<>();
        movies2 = new ArrayList<>();
        blah3 = new ArrayList<>();
        blah4 = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mGridView = (GridView) rootView.findViewById(R.id.gridView);
        mGridAdapter = new GridViewAdapter(getActivity(), R.layout.grid_item_layout, mGridData);
        mGridView.setAdapter(mGridAdapter);

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<MoviesResponse> call = apiService.getPopularMovies(API_KEY);
        Call<MoviesResponse> call2 = apiService.getTopRatedMovies(API_KEY);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                movies = response.body().getResults();
                Log.d(TAG, "Number of movies received: " + movies.size() + " " + movies.get(0).getTitle());
            }

            @Override
            public void onFailure(Call<MoviesResponse>call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
        call2.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                movies2 = response.body().getResults();
            }

            @Override
            public void onFailure(Call<MoviesResponse>call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                DisplayMetrics metrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int width = metrics.widthPixels;
                int height = metrics.heightPixels;
                float scaleFactor = metrics.density;
                float widthDp = width / scaleFactor;
                float heightDp = height / scaleFactor;

                Movies item = (Movies) parent.getItemAtPosition(position);
                if (widthDp < 600 || heightDp < 500) {
                    Intent intent = new Intent(getActivity(), DetailsActivity.class);

                    intent.putExtra("title", item.getTitle()).
                            putExtra("image", item.getPosterPath()).
                            putExtra("releaseDate", item.getReleaseDate()).
                            putExtra("rating", item.getVoteAverage()).
                            putExtra("id", item.getId()).
                            putExtra("synopsis", item.getOverview());

                    startActivity(intent);

                } else {
                    args = new Bundle();
                    args.putString("title", item.getTitle());
                    args.putString("image", item.getPosterPath());
                    args.putString("releaseDate", item.getReleaseDate());
                    args.putString("rating", item.getVoteAverage());
                    args.putInt("id", item.getId());
                    args.putString("synopsis", item.getOverview());

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



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_main, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.popular_movies:
                if (mGridData.size() >= 20) {
                    mGridData.clear();
                }

                for (int i =0; i < movies.size(); i++) {
                    mGridData.add(movies.get(i));
                }
                mGridAdapter.setGridData(mGridData);
                break;


            case R.id.highest_rated:
                if (mGridData.size() >= 20) {
                    mGridData.clear();
                }

                for (int i =0; i < movies2.size(); i++) {
                    mGridData.add(movies2.get(i));
                }
                mGridAdapter.setGridData(mGridData);


                break;

            case R.id.favorites:
                Intent intent = new Intent(getActivity(), FavoriteList.class);
                this.startActivity(intent);
                break;
        }
        return true;
    }

}
