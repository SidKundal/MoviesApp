package com.rizort.movieapp.dtos;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class SearchQueryResultDTO implements Parcelable {

    ArrayList<MovieDTO> Search;
    String              totalResults;
    String              Response;

    public SearchQueryResultDTO(ArrayList<MovieDTO> search, String totalResults, String response) {
        Search = search;
        this.totalResults = totalResults;
        Response = response;
    }

    public ArrayList<MovieDTO> getSearch() {
        return Search;
    }

    public void setSearch(ArrayList<MovieDTO> search) {
        Search = search;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.Search);
        dest.writeString(this.totalResults);
        dest.writeString(this.Response);
    }

    protected SearchQueryResultDTO(Parcel in) {
        this.Search = in.createTypedArrayList(MovieDTO.CREATOR);
        this.totalResults = in.readString();
        this.Response = in.readString();
    }

    public static final Parcelable.Creator<SearchQueryResultDTO> CREATOR = new Parcelable.Creator<SearchQueryResultDTO>() {
        @Override
        public SearchQueryResultDTO createFromParcel(Parcel source) {
            return new SearchQueryResultDTO(source);
        }

        @Override
        public SearchQueryResultDTO[] newArray(int size) {
            return new SearchQueryResultDTO[size];
        }
    };
}
