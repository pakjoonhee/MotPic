package com.javatechig.gridviewexample;

public class GridItem {
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
}
