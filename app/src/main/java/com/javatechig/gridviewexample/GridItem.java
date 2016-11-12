package com.javatechig.gridviewexample;

import com.google.gson.annotations.SerializedName;

public class GridItem  {
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



    public GridItem() {
        super();
    }

//    protected GridItem(Parcel in) {
//        posterPath = in.readString();
//        title = in.readString();
//        releaseDate = in.readString();
//        voteAverage = in.readString();
//        overview = in.readString();
//        id = in.readInt();
//        review = in.readString();
//    }

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


//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeString(posterPath);
//        parcel.writeString(title);
//        parcel.writeString(releaseDate);
//        parcel.writeString(voteAverage);
//        parcel.writeString(overview);
//        parcel.writeString(review);
//        parcel.writeInt(id);
//    }
//
//    public static final Creator<GridItem> CREATOR = new Creator<GridItem>() {
//        @Override
//        public GridItem createFromParcel(Parcel parcel) {
//            return new GridItem(parcel);
//        }
//
//        @Override
//        public GridItem[] newArray(int i) {
//            return new GridItem[i];
//        }
//    };
}
