package com.javatechig.gridviewexample.utility;

import com.javatechig.gridviewexample.data.MoviesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by joonheepak on 11/11/16.
 */

public interface ApiInterface {
    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRateMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<MoviesResponse> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);
}
