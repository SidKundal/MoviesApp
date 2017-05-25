package com.rizort.movieapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rizort.movieapp.R;
import com.rizort.movieapp.activities.DescriptionScreen;
import com.rizort.movieapp.dtos.MovieDTO;
import com.rizort.movieapp.utils.Constants;
import com.rizort.movieapp.utils.Util;

import java.util.ArrayList;

public class MoviesResultsListingAdapter extends RecyclerView.Adapter<MoviesResultsListingAdapter.MovieViewHolder>{

    public ArrayList<MovieDTO> mMovies;
    public Context mContext;
//    private final View.OnClickListener mOnClickListener = new MyOnClickListener();

    public MoviesResultsListingAdapter(ArrayList<MovieDTO> movies, Context context) {
        mMovies = movies;
        mContext = context;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        MovieDTO movie = mMovies.get(position);
        holder.movieTitleView   .setText(movie.getTitle());
        holder.movieYearView    .setText(movie.getYear());

        String moviePosterURL = movie.getPoster();
        Glide
                .with(mContext)
                .load(moviePosterURL)
                .placeholder(R.mipmap.ic_launcher)
                .centerCrop()
                .crossFade()
                .into(holder.moviePosterView);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_movie_result, parent, false);

//        int height = parent.getMeasuredHeight();
//        int width = parent.getMeasuredWidth() / 4;
//
//        view.setLayoutParams(new RecyclerView.LayoutParams(width, height));

        return new MovieViewHolder(view);
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView movieTitleView;
        private TextView movieYearView;
        private ImageView moviePosterView;

        public MovieViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            movieTitleView  = (TextView)    view.findViewById(R.id.movieTitleView);
            movieYearView   = (TextView)    view.findViewById(R.id.movieYearView);
            moviePosterView = (ImageView)   view.findViewById(R.id.moviewPosterView);
        }

        @Override
        public void onClick(View view) {
            Util.log("Position: " + getAdapterPosition());
            Intent openMovieDetailsScreenIntent = new Intent(mContext, DescriptionScreen.class);
            openMovieDetailsScreenIntent.putExtra(Constants.INTENT_EXTRA_MOVIE, mMovies.get(getAdapterPosition()));
            openMovieDetailsScreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(openMovieDetailsScreenIntent);
        }
    }
}
