package com.rizort.movieapp.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.rizort.movieapp.R;
import com.rizort.movieapp.dtos.MovieDTO;
import com.rizort.movieapp.dtos.MovieDetailDTO;
import com.rizort.movieapp.dtos.RatingDTO;
import com.rizort.movieapp.network.Requests;
import com.rizort.movieapp.utils.Constants;
import com.rizort.movieapp.utils.Util;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DescriptionScreen extends AppCompatActivity implements View.OnClickListener{

    private ProgressBar mProgressBar;

    private ImageView   mRefreshActionView;

    private TextView mMovieTitleView;
    private TextView mMovieActorsView;
    private TextView mMovieDirectorView;
    private TextView mMovieRatedView;
    private TextView mMovieReleaseDateView;
    private TextView mMovieRatingsView;

    private ImageView mMoviePosterView;

    private MovieDTO mMovie;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_description_screen);

        initializeViews();

        setUpOnClickListeners();

        readDataFromIntent();

        setUpToolbar();

    }

    private void initializeViews() {
        mProgressBar            = (ProgressBar) findViewById(R.id.progressBar);
        mRefreshActionView      = (ImageView)   findViewById(R.id.refreshActionView);

        mMovieTitleView         = (TextView) findViewById(R.id.tvMovieTitle);
        mMovieDirectorView      = (TextView) findViewById(R.id.tvMovieDirector);
        mMovieActorsView        = (TextView) findViewById(R.id.tvMovieActors);
        mMovieReleaseDateView   = (TextView) findViewById(R.id.tvMovieReleaseDate);
        mMovieRatedView         = (TextView) findViewById(R.id.tvMovieRated);
        mMovieRatingsView       = (TextView) findViewById(R.id.tvMovieRatings);

        mMoviePosterView        = (ImageView) findViewById(R.id.moviePosterView);
    }

    private void setUpToolbar() {
        Toolbar toolbar         = (Toolbar) findViewById(R.id.descriptionPageToolbar);
        TextView mToolbarTitle  = (TextView) findViewById(R.id.descriptionPageToolbarTitle);
        setSupportActionBar(toolbar);
        mToolbarTitle.setText(mMovie.getTitle());

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpOnClickListeners() {
        mRefreshActionView.setOnClickListener(this);
    }

    private void readDataFromIntent() {
        if(getIntent().getExtras() != null) {
            mMovie = getIntent().getParcelableExtra(Constants.INTENT_EXTRA_MOVIE);

            fetchMovieData();
        }
    }

    private void fetchMovieData() {
        if(mMovie == null) {
            finish();
        } else {
            setUIStateWaiting();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Requests network = retrofit.create(Requests.class);
            Call<MovieDetailDTO> responseCall = network.getMovieDetails(mMovie.getImdbID());
            try {
                setUIStateWaiting();

                Util.log("Sending request for movie details");
                responseCall.enqueue(new Callback<MovieDetailDTO>() {

                    @Override
                    public void onResponse(Call<MovieDetailDTO> call, Response<MovieDetailDTO> response) {
                        MovieDetailDTO responseGot = response.body();

                        String status = response.code() + "";
                        Util.log("Status : " + status);
                        if (responseGot != null) {

                            setUIStateReady();

                            mMovieTitleView.setText(responseGot.getTitle());
                            mMovieActorsView.setText(responseGot.getActors());
                            mMovieDirectorView.setText(responseGot.getDirector());
                            mMovieReleaseDateView.setText(responseGot.getReleased());
                            mMovieRatedView.setText(responseGot.getRated());

                            String movieRatings = "";
                            for(RatingDTO rating : responseGot.getRatings()) {
                                if(!movieRatings.equals("")) {
                                    movieRatings = movieRatings + "\n";
                                }
                                movieRatings = movieRatings + rating.getSource() + " (" + rating.getValue() + ")";
                            }
                            mMovieRatingsView.setText(movieRatings);

                            String moviePosterURL = responseGot.getPoster();
                            Glide
                                    .with(getApplicationContext())
                                    .load(moviePosterURL)
                                    .placeholder(R.mipmap.ic_launcher)
                                    .centerCrop()
                                    .crossFade()
                                    .into(mMoviePosterView);

                        } else {

                            Util.log("Response object not proper");
                            setUIToShowFailure();
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieDetailDTO> call, Throwable t) {
                        Log.d(Constants.TAG, "Failure occurred while fetching home page request :" + t.getMessage());
                        setUIToShowFailure();
                    }
                });
            } catch (Exception e) {
                Util.log("Exception found : " + e);
                setUIToShowFailure();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUIStateWaiting() {
        mProgressBar.setVisibility(View.VISIBLE);
        mRefreshActionView.setVisibility(View.GONE);
    }

    private void setUIStateReady() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mRefreshActionView.setVisibility(View.GONE);
    }

    private void setUIToShowFailure() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mRefreshActionView.setVisibility(View.VISIBLE);
        Toast.makeText(this, "Unable to make request. Try later.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.refreshActionView:
                fetchMovieData();
                break;
        }
    }
}
