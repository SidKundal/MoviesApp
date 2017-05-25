package com.rizort.movieapp.dtos;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieDTO implements Parcelable {

    String Title;
    String Year;
    String imdbID;
    String Type;
    String Poster;

    public MovieDTO(String title, String year, String imdbID, String type, String poster) {
        Title = title;
        Year = year;
        this.imdbID = imdbID;
        Type = type;
        Poster = poster;
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

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getPoster() {
        return Poster;
    }

    public void setPoster(String poster) {
        Poster = poster;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Title);
        dest.writeString(this.Year);
        dest.writeString(this.imdbID);
        dest.writeString(this.Type);
        dest.writeString(this.Poster);
    }

    protected MovieDTO(Parcel in) {
        this.Title = in.readString();
        this.Year = in.readString();
        this.imdbID = in.readString();
        this.Type = in.readString();
        this.Poster = in.readString();
    }

    public static final Parcelable.Creator<MovieDTO> CREATOR = new Parcelable.Creator<MovieDTO>() {
        @Override
        public MovieDTO createFromParcel(Parcel source) {
            return new MovieDTO(source);
        }

        @Override
        public MovieDTO[] newArray(int size) {
            return new MovieDTO[size];
        }
    };
}

