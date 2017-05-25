package com.rizort.movieapp.dtos;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class MovieDetailDTO implements Parcelable {

    String Title;
    String Year;
    String Released;
    String Director;
    String Actors;
    String Rated;
    String Poster;
    ArrayList<RatingDTO> Ratings;

    public MovieDetailDTO(String title, String year, String released, String director, String actors,
                          String rated, String poster, ArrayList<RatingDTO> ratings) {
        Title = title;
        Year = year;
        Released = released;
        Director = director;
        Actors = actors;
        Rated = rated;
        Poster = poster;
        Ratings = ratings;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getReleased() {
        return Released;
    }

    public void setReleased(String released) {
        Released = released;
    }

    public String getDirector() {
        return Director;
    }

    public void setDirector(String director) {
        Director = director;
    }

    public String getActors() {
        return Actors;
    }

    public void setActors(String actors) {
        Actors = actors;
    }

    public String getRated() {
        return Rated;
    }

    public void setRated(String rated) {
        Rated = rated;
    }

    public String getPoster() {
        return Poster;
    }

    public void setPoster(String poster) {
        Poster = poster;
    }

    public ArrayList<RatingDTO> getRatings() {
        return Ratings;
    }

    public void setRatings(ArrayList<RatingDTO> ratings) {
        Ratings = ratings;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Title);
        dest.writeString(this.Year);
        dest.writeString(this.Released);
        dest.writeString(this.Director);
        dest.writeString(this.Actors);
        dest.writeString(this.Rated);
        dest.writeString(this.Poster);
        dest.writeTypedList(this.Ratings);
    }

    protected MovieDetailDTO(Parcel in) {
        this.Title = in.readString();
        this.Year = in.readString();
        this.Released = in.readString();
        this.Director = in.readString();
        this.Actors = in.readString();
        this.Rated = in.readString();
        this.Poster = in.readString();
        this.Ratings = in.createTypedArrayList(RatingDTO.CREATOR);
    }

    public static final Parcelable.Creator<MovieDetailDTO> CREATOR = new Parcelable.Creator<MovieDetailDTO>() {
        @Override
        public MovieDetailDTO createFromParcel(Parcel source) {
            return new MovieDetailDTO(source);
        }

        @Override
        public MovieDetailDTO[] newArray(int size) {
            return new MovieDetailDTO[size];
        }
    };
}
