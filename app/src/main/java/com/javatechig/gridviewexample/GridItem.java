package com.javatechig.gridviewexample;

import android.os.Parcel;
import android.os.Parcelable;

public class GridItem implements Parcelable {
    private String image;
    private String title;
    private String releaseDate;
    private String rating;
    private String synopsis;
    private int id;
    private String review;


    public GridItem() {
        super();
    }

    protected GridItem(Parcel in) {
        image = in.readString();
        title = in.readString();
        releaseDate = in.readString();
        rating = in.readString();
        synopsis = in.readString();
        id = in.readInt();
        review = in.readString();
    }

    public String getImage() { return image; }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() { return releaseDate; }

    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }

    public String getRating() { return rating; }

    public void setRating(String rating) { this.rating = rating; }

    public String getSynopsis() { return synopsis; }

    public void setSynopsis(String synopsis) { this.synopsis = synopsis; }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getReview() { return review; }

    public void setReview(String review) { this.review = review; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(image);
        parcel.writeString(title);
        parcel.writeString(releaseDate);
        parcel.writeString(rating);
        parcel.writeString(synopsis);
        parcel.writeString(review);
        parcel.writeInt(id);
    }

    public static final Creator<GridItem> CREATOR = new Creator<GridItem>() {
        @Override
        public GridItem createFromParcel(Parcel parcel) {
            return new GridItem(parcel);
        }

        @Override
        public GridItem[] newArray(int i) {
            return new GridItem[i];
        }
    };
}
