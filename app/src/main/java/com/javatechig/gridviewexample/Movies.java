package com.javatechig.gridviewexample;

import com.google.gson.annotations.SerializedName;

public class Movies {
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("title")
    private String title;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("vote_average")
    private String voteAverage;
    @SerializedName("overview")
    private String overview;
    @SerializedName("id")
    private int id;



    public Movies(String posterPath, String title, String releaseDate, String voteAverage, String overview, int id) {
        this.posterPath = posterPath;
        this.title = title;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.overview = overview;
        this.id = id;
    }

    public Movies() {

    }


    public String getPosterPath() { return posterPath; }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() { return releaseDate; }

    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }

    public String getVoteAverage() { return voteAverage; }

    public void setVoteAverage(String voteAverage) { this.voteAverage = voteAverage; }

    public String getOverview() { return overview; }

    public void setOverview(String overview) { this.overview = overview; }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

}
