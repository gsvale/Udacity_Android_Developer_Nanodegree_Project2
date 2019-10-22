package com.example.udacity_android_developer_nanodegree_project2.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.net.Uri;

import androidx.annotation.Nullable;

import com.example.udacity_android_developer_nanodegree_project2.objects.Movie;
import com.example.udacity_android_developer_nanodegree_project2.utils.ApiUtils;
import com.example.udacity_android_developer_nanodegree_project2.utils.HttpUtils;
import com.example.udacity_android_developer_nanodegree_project2.utils.JsonUtils;

public class MovieInfoLoader extends AsyncTaskLoader<Movie> {

    private Movie mMovie;

    // Variable to save movie data results
    private Movie mMovieData;

    public MovieInfoLoader(Context context, Movie movie) {
        super(context);
        mMovie = movie;
    }

    // First is executed OnStartLoading() that will call forceLoad() method which triggers the loadInBackground() method to execute
    @Override
    protected void onStartLoading() {

        // If there is cached data, deliver it, or else force load is called
        if(mMovieData != null){
            deliverResult(mMovieData);
        }
        else{
            forceLoad();
        }
    }

    // Return cached data from loader
    @Override
    public void deliverResult(Movie movie) {
        mMovieData = movie;
        super.deliverResult(movie);
    }

    // The background thread, that will fetch the Movie trailers and reviews
    @Nullable
    @Override
    public Movie loadInBackground() {
        if (mMovie == null) {
            return null;
        }

        // Build URL to be used
        Uri.Builder builder = new Uri.Builder();


        // Build URL for API call, to retrieve trailers and reviews

        builder.scheme(ApiUtils.SCHEME)
                .authority(ApiUtils.AUTHORITY)
                .appendEncodedPath(ApiUtils.PATH)
                .appendPath(mMovie.getId())
                .appendPath(ApiUtils.PATH_VIDEOS)
                .appendQueryParameter(ApiUtils.PATH_KEY, ApiUtils.PATH_KEY_VALUE);

        String trailersJsonString = HttpUtils.fetchMovieData(builder.build().toString());

        builder = new Uri.Builder();

        builder.scheme(ApiUtils.SCHEME)
                .authority(ApiUtils.AUTHORITY)
                .appendEncodedPath(ApiUtils.PATH)
                .appendPath(mMovie.getId())
                .appendPath(ApiUtils.PATH_REVIEWS)
                .appendQueryParameter(ApiUtils.PATH_KEY, ApiUtils.PATH_KEY_VALUE);

        String reviewsJsonString = HttpUtils.fetchMovieData(builder.build().toString());


        return JsonUtils.extractMovieDataFromJsons(mMovie,trailersJsonString,reviewsJsonString);

    }

}
