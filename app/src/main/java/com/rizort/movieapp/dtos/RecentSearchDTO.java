package com.rizort.movieapp.dtos;

import android.os.Parcel;
import android.os.Parcelable;

public class RecentSearchDTO implements Parcelable {

    String                  searchTerm;
    SearchQueryResultDTO    searchResult;

    public RecentSearchDTO(String searchTerm, SearchQueryResultDTO searchResult) {
        this.searchTerm = searchTerm;
        this.searchResult = searchResult;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public SearchQueryResultDTO getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(SearchQueryResultDTO searchResult) {
        this.searchResult = searchResult;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.searchTerm);
        dest.writeParcelable(this.searchResult, flags);
    }

    protected RecentSearchDTO(Parcel in) {
        this.searchTerm = in.readString();
        this.searchResult = in.readParcelable(SearchQueryResultDTO.class.getClassLoader());
    }

    public static final Parcelable.Creator<RecentSearchDTO> CREATOR = new Parcelable.Creator<RecentSearchDTO>() {
        @Override
        public RecentSearchDTO createFromParcel(Parcel source) {
            return new RecentSearchDTO(source);
        }

        @Override
        public RecentSearchDTO[] newArray(int size) {
            return new RecentSearchDTO[size];
        }
    };
}
