package com.example.udacity_android_developer_nanodegree_project2.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.net.Uri;

import androidx.annotation.Nullable;

import com.example.udacity_android_developer_nanodegree_project2.R;
import com.example.udacity_android_developer_nanodegree_project2.objects.Movie;
import com.example.udacity_android_developer_nanodegree_project2.utils.HttpUtils;

import java.util.List;

public class MoviesLoader extends AsyncTaskLoader<List<Movie>> {

    private static final String SCHEME = "http";
    private static final String AUTHORITY = "api.themoviedb.org";
    private static final String PATH = "3/movie";

    private static final String ORDER_BY_MOST_POPULAR_VALUE = "popular";
    private static final String ORDER_BY_HIGHEST_RATE_VALUE = "top_rated";

    private static final String PATH_KEY = "api_key";

    // INSERT API KEY HERE:
    private static final String PATH_KEY_VALUE = "";

    private String mQuery;

    public MoviesLoader(Context context, String query) {
        super(context);
        mQuery = query;
    }

    // First is executed OnStartLoading() that will call forceLoad() method which triggers the loadInBackground() method to execute
    @Override
    protected void onStartLoading() {
        forceLoad();
    }


    // The background thread, that will fetch the Movies feed data
    @Nullable
    @Override
    public List<Movie> loadInBackground() {
        if (mQuery == null) {
            return null;
        }

        // Build URL to be used, with the query of Order By received
        Uri.Builder builder = new Uri.Builder();

        String orderByQueryValue;

        if(mQuery.equals(getContext().getString(R.string.settings_order_by_most_popular))){
            orderByQueryValue = ORDER_BY_MOST_POPULAR_VALUE;
        }
        else{
            orderByQueryValue = ORDER_BY_HIGHEST_RATE_VALUE;
        }

        builder.scheme(SCHEME)
                .authority(AUTHORITY)
                .appendEncodedPath(PATH)
                .appendPath(orderByQueryValue)
                .appendQueryParameter(PATH_KEY, PATH_KEY_VALUE);

        return HttpUtils.fetchMoviesData(builder.build().toString());

    }


}
