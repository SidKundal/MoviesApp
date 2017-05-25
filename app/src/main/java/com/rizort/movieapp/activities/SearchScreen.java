package com.rizort.movieapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rizort.movieapp.R;
import com.rizort.movieapp.adapters.RecentSearchesAdapter;
import com.rizort.movieapp.dtos.RecentSearchDTO;
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

public class SearchScreen extends AppCompatActivity implements View.OnClickListener{

    private EditText    mSearchField;
    private ImageView   mSearchButtonView;
    private ProgressBar mSearchProgressBar;

    private LinearLayout    mRecentSearchesLayout;
    private TextView        mRecentSearchesHeaderView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_search_screen);

        setUpToolbar();

        initializeViews();

        setUpOnClickListeners();

        setUpRecentSearches();
    }

    private void setUpToolbar() {
        Toolbar toolbar         = (Toolbar) findViewById(R.id.searchPageToolbar);
        TextView mToolbarTitle  = (TextView) findViewById(R.id.searchPageToolbarTitle);
        setSupportActionBar(toolbar);
        mToolbarTitle.setText(R.string.search_screen_title);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initializeViews() {
        mSearchField        = (EditText)    findViewById(R.id.searchField);
        mSearchButtonView   = (ImageView)   findViewById(R.id.searchActionView);
        mSearchProgressBar  = (ProgressBar) findViewById(R.id.searchProgressBar);

        mRecentSearchesHeaderView   = (TextView)        findViewById(R.id.recentSearchesHeaderView);
        mRecentSearchesLayout       = (LinearLayout)    findViewById(R.id.recentSearchItemsLayout);
    }

    private void setUpRecentSearches() {
        final ArrayList<RecentSearchDTO> recentSearches = Util.getListOfRecentSearches(getApplicationContext());
        int numRecentSearches = recentSearches.size();

        if (numRecentSearches > 0) {
            mRecentSearchesHeaderView.setText("Recent Searches (" + numRecentSearches + ")");

            mRecentSearchesLayout.removeAllViews();
            RecentSearchesAdapter recentSearchesAdapter = new RecentSearchesAdapter(getApplicationContext(), recentSearches);
            for(int i=0; i < numRecentSearches; i++) {
                final int position = i;
                View viewToAdd = recentSearchesAdapter.getView(i, null, null);
                viewToAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RecentSearchDTO recentSearch = recentSearches.get(position);

                        Intent openResultsPageIntent = new Intent(getApplicationContext(), ResultsListingScreen.class);
                        openResultsPageIntent.putExtra(Constants.INTENT_EXTRA_RESULTS       , recentSearch.getSearchResult());
                        openResultsPageIntent.putExtra(Constants.INTENT_EXTRA_SEARCH_TERM   , recentSearch.getSearchTerm());
                        startActivity(openResultsPageIntent);
                    }
                });
                mRecentSearchesLayout.addView(viewToAdd);
            }
        }
    }

    private void setUpOnClickListeners() {
        mSearchButtonView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.searchActionView:
                String searchTerm = mSearchField.getText().toString();
                if(!Util.isNullOrBlank(searchTerm)) {
                    makeRequestToFetchDataFromServer(searchTerm.trim());
                } else {
                    Toast.makeText(this, "Enter something to search", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void makeRequestToFetchDataFromServer(final String searchTerm) {
        setUIStateWaiting();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Requests network = retrofit.create(Requests.class);
        Call<SearchQueryResultDTO> responseCall = network.getSearchResults(searchTerm, 1);
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
                        if(responseGot.getSearch() != null && !responseGot.getSearch().isEmpty()) {
                            mSearchField.setText("");

                            RecentSearchDTO recentSearch = new RecentSearchDTO(searchTerm, responseGot);
                            Util.addToListOfRecentSearches(getApplicationContext(), recentSearch);

                            Intent openResultsPageIntent = new Intent(getApplicationContext(), ResultsListingScreen.class);
                            openResultsPageIntent.putExtra(Constants.INTENT_EXTRA_RESULTS, responseGot);
                            openResultsPageIntent.putExtra(Constants.INTENT_EXTRA_SEARCH_TERM, searchTerm);
                            startActivity(openResultsPageIntent);
                        } else {
                            Toast.makeText(SearchScreen.this, "Unable to fetch any results. Try again.", Toast.LENGTH_SHORT).show();
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
        mSearchButtonView.setClickable(false);
    }

    private void setUIStateReady() {
        mSearchProgressBar.setVisibility(View.INVISIBLE);
        mSearchButtonView.setClickable(true);
    }

    private void setUIToShowFailure() {
        mSearchProgressBar.setVisibility(View.INVISIBLE);
        mSearchButtonView.setClickable(true);
        Toast.makeText(this, "Unable to make request. Try later.", Toast.LENGTH_SHORT).show();
    }
}
