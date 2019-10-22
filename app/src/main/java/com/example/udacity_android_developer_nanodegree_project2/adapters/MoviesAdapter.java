package com.example.udacity_android_developer_nanodegree_project2.adapters;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.udacity_android_developer_nanodegree_project2.R;
import com.example.udacity_android_developer_nanodegree_project2.objects.Movie;
import com.example.udacity_android_developer_nanodegree_project2.utils.ImageUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {

    private List<Movie> movies;

    private final MoviesAdapterClickHandler mClickHandler;

    // Interface MoviesAdapterClickHandler
    public interface MoviesAdapterClickHandler {
        void onClick(Movie movie);
    }

    // Constructor receive a clickHandler to be called in MoviesActivity
    public MoviesAdapter(MoviesAdapterClickHandler clickHandler) {
        mClickHandler = clickHandler;
        movies = new ArrayList<>();
    }

    @NonNull
    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item_layout, parent, false);
        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesAdapterViewHolder holder, int position) {
        Movie movieItem = movies.get(position);

        // Set movie title
        holder.mMovieTitleTv.setText(movieItem.getOriginalTitle());

        // Show image from poster with aid of Picasso library
        Picasso.get()
                .load(ImageUtils.getImagePath(movieItem.getPosterImageUrl()))
                .placeholder(R.drawable.ic_movie_icon_default)
                .error(R.drawable.ic_movie_icon_default)
                .into(holder.mMoviePosterIv);
    }

    @Override
    public int getItemCount() {
        if (null == movies) return 0;
        return movies.size();
    }

    // Clear data, and set new data then notify that the data has changed
    public void setMoviesData(List<Movie> moviesData) {
        movies.clear();
        movies.addAll(moviesData);
        notifyDataSetChanged();
    }

    // Clear data then notify
    public void clearData() {
        movies.clear();
        notifyDataSetChanged();
    }

    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mMovieTitleTv;
        ImageView mMoviePosterIv;

        MoviesAdapterViewHolder(View view) {
            super(view);
            mMoviePosterIv = view.findViewById(R.id.movie_poster_iv);
            mMovieTitleTv = view.findViewById(R.id.movie_title_tv);
            view.setOnClickListener(this);
        }

        // Notify when item view is clicked
        @Override
        public void onClick(View v) {
            mClickHandler.onClick(movies.get(getAdapterPosition()));
        }
    }

}
