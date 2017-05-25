package com.rizort.movieapp.network;

import com.rizort.movieapp.dtos.MovieDetailDTO;
import com.rizort.movieapp.dtos.SearchQueryResultDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Requests {

    @GET("?apikey=5d1a3f80&type=movie")
    Call<SearchQueryResultDTO> getSearchResults(@Query("s") String searchTerm, @Query("page") int pageNumber);


    @GET("?apikey=5d1a3f80")
    Call<MovieDetailDTO> getMovieDetails(@Query("i") String imdbID);
}
