package com.rizort.movieapp.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rizort.movieapp.R;
import com.rizort.movieapp.adapters.MoviesResultsListingAdapter;
import com.rizort.movieapp.dtos.MovieDTO;
import com.rizort.movieapp.dtos.SearchQueryResultDTO;
import com.rizort.movieapp.network.Requests;
import com.rizort.movieapp.utils.Constants;
import com.rizort.movieapp.utils.Util;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ResultsListingScreen extends AppCompatActivity{

    private RecyclerView                mRecyclerView;
    private MoviesResultsListingAdapter mResultsAdapter;
    private LinearLayoutManager         mLayoutManager;

    private TextView mResultsFoundView;

    private String mSearchTerm;

    private ProgressBar mSearchProgressBar;

    private ArrayList<MovieDTO> mMoviesList;

    private boolean loading = false;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    int currentPage = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_results_listing_screen);

        setUpToolbar();

        initializeViews();

        setUpOnClickListeners();

        readDataFromIntent();
    }

    private void readDataFromIntent() {
        if(getIntent().getExtras() != null) {
            mSearchTerm = getIntent().getStringExtra(Constants.INTENT_EXTRA_SEARCH_TERM);
            SearchQueryResultDTO results = getIntent().getParcelableExtra(Constants.INTENT_EXTRA_RESULTS);
            setUpResults(results);
        }
    }

    private void setUpResults(SearchQueryResultDTO results) {
        mMoviesList = results.getSearch();
        mResultsAdapter = new MoviesResultsListingAdapter(mMoviesList, getApplicationContext());
        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mResultsAdapter);

        mResultsFoundView.setText(results.getTotalResults() + " Results Found :");


        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(dy > 0) {

                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (!loading) {

                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            //came to end of list
                            makeServerRequestToFetchMoreData(mSearchTerm);
                        }
                    }
                }
            }
        });
    }

    private void makeServerRequestToFetchMoreData(String searchTerm) {
        setUIStateWaiting();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Requests network = retrofit.create(Requests.class);
        Call<SearchQueryResultDTO> responseCall = network.getSearchResults(searchTerm, currentPage);
        Util.log("page being fetched : " + currentPage);
        try {
            setUIStateWaiting();

            Util.log("Sending request for home");
            responseCall.enqueue(new Callback<SearchQueryResultDTO>() {

                @Override
                public void onResponse(Call<SearchQueryResultDTO> call, Response<SearchQueryResultDTO> response) {
                    SearchQueryResultDTO responseGot = response.body();

                    String status = response.code() + "";
                    Util.log("Status : " + status);
                    if (responseGot != null) {

                        setUIStateReady();

                        Util.log("Came here");
                        Util.log("results : " + responseGot.getTotalResults());
                        if(responseGot.getSearch() != null && !responseGot.getSearch().isEmpty()) {
                            Util.log("Results found : " + responseGot.getSearch().size());
                            mMoviesList.addAll(responseGot.getSearch());
                            mResultsAdapter.notifyDataSetChanged();
                            currentPage++;
                        } else {
                            Toast.makeText(ResultsListingScreen.this, "Unable to fetch more results.", Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        Util.log("Response object not proper");
                        setUIToShowFailure();
                    }
                }

                @Override
                public void onFailure(Call<SearchQueryResultDTO> call, Throwable t) {
                    Log.d(Constants.TAG, "Failure occurred while fetching home page request :" + t.getMessage());
                    setUIToShowFailure();
                }
            });
        } catch (Exception e) {
            Util.log("Exception found : " + e);
            setUIToShowFailure();
        }
    }

    private void setUIStateWaiting() {
        mSearchProgressBar.setVisibility(View.VISIBLE);
        loading = true;
    }

    private void setUIStateReady() {
        mSearchProgressBar.setVisibility(View.INVISIBLE);
        loading = false;
    }

    private void setUIToShowFailure() {
        mSearchProgressBar.setVisibility(View.INVISIBLE);
        loading = false;
        Toast.makeText(this, "Unable to make request. Try later.", Toast.LENGTH_SHORT).show();
    }

    private void initializeViews() {
        mRecyclerView       = (RecyclerView)    findViewById(R.id.resultsRecyclerView);
        mResultsFoundView   = (TextView)        findViewById(R.id.resultsFoundView);
        mSearchProgressBar  = (ProgressBar)     findViewById(R.id.searchProgressBar);
    }

    private void setUpOnClickListeners() {

    }

    private void setUpToolbar() {
        Toolbar toolbar         = (Toolbar) findViewById(R.id.resultsPageToolbar);
        TextView mToolbarTitle  = (TextView) findViewById(R.id.resultsPageToolbarTitle);
        setSupportActionBar(toolbar);
        mToolbarTitle.setText(R.string.results_screen_title);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //just to make sure that the last activity gets refreshed and the recent searches get updated
        NavUtils.navigateUpFromSameTask(this);
    }
}
