package com.example.udacity_android_developer_nanodegree_project2.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.net.Uri;

import androidx.annotation.Nullable;

import com.example.udacity_android_developer_nanodegree_project2.R;
import com.example.udacity_android_developer_nanodegree_project2.objects.Movie;
import com.example.udacity_android_developer_nanodegree_project2.utils.ApiUtils;
import com.example.udacity_android_developer_nanodegree_project2.utils.HttpUtils;

import java.util.List;

public class MoviesLoader extends AsyncTaskLoader<List<Movie>> {

    // Query that informs the Order By preference selected
    private String mQuery;

    // Variable to save movie data results
    private List<Movie> mJsonMovieData;

    public MoviesLoader(Context context, String query) {
        super(context);
        mQuery = query;
    }

    // First is executed OnStartLoading() that will call forceLoad() method which triggers the loadInBackground() method to execute
    @Override
    protected void onStartLoading() {

        // If there is cached data, deliver it, or else force load is called
        if(mJsonMovieData != null){
            deliverResult(mJsonMovieData);
        }
        else{
            forceLoad();
        }
    }

    // Return cached data from loader
    @Override
    public void deliverResult(List<Movie> movies) {
        mJsonMovieData = movies;
        super.deliverResult(movies);
    }


    // The background thread, that will fetch the Movies feed data
    @Nullable
    @Override
    public List<Movie> loadInBackground() {
        if (mQuery == null) {
            return null;
        }

        return fetchMoviesDataFromAPI();
    }

    // Method called to fetch Movies data from the Movies API
    private List<Movie> fetchMoviesDataFromAPI(){

        Uri.Builder builder = new Uri.Builder();

        String orderByQueryValue;

        // Check order by query selected from preferences
        if(mQuery.equals(getContext().getString(R.string.settings_order_by_most_popular))){
            orderByQueryValue = ApiUtils.ORDER_BY_MOST_POPULAR_VALUE;
        }
        else{
            orderByQueryValue = ApiUtils.ORDER_BY_HIGHEST_RATE_VALUE;
        }

        // Build URL for API call
        builder.scheme(ApiUtils.SCHEME)
                .authority(ApiUtils.AUTHORITY)
                .appendEncodedPath(ApiUtils.PATH)
                .appendPath(orderByQueryValue)
                .appendQueryParameter(ApiUtils.PATH_KEY, ApiUtils.PATH_KEY_VALUE);

        return HttpUtils.fetchMoviesData(builder.build().toString());
    }


}
