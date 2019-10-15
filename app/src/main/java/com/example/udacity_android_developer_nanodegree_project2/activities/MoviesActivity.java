package com.example.udacity_android_developer_nanodegree_project2.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.udacity_android_developer_nanodegree_project2.R;
import com.example.udacity_android_developer_nanodegree_project2.adapters.MoviesAdapter;
import com.example.udacity_android_developer_nanodegree_project2.loaders.MoviesLoader;
import com.example.udacity_android_developer_nanodegree_project2.objects.Movie;
import com.example.udacity_android_developer_nanodegree_project2.utils.NetworkUtils;

import java.util.List;

public class MoviesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>>,
        MoviesAdapter.MoviesAdapterClickHandler,
        SharedPreferences.OnSharedPreferenceChangeListener {

    // Number of items in grid per row
    private static final int GRID_LAYOUT_SPAN_COUNT = 2;

    // Loader ID
    private static final int MOVIES_LOADER_ID = 1;

    private RecyclerView mMoviesRecyclerView;
    private MoviesAdapter mMoviesAdapter;
    private TextView mEmptyViewTextView;
    private ProgressBar mLoadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        // Get SharedPreferences for this app
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Register to be notified of preference changes
        prefs.registerOnSharedPreferenceChangeListener(this);

        /* RecyclerView reference to set adapter to be used */
        mMoviesRecyclerView = findViewById(R.id.movies_rv);

        /* This TextView display errors if they exist */
        mEmptyViewTextView = findViewById(R.id.empty_view_tv);

        /* Progress bar that shows while loading movies data */
        mLoadingProgressBar = findViewById(R.id.loading_pb);

        // Set GridLayoutManager for RecyclerView , and show two items per row
        GridLayoutManager layoutManager
                = new GridLayoutManager(this, GRID_LAYOUT_SPAN_COUNT);

        // Set GridLayoutManager to recyclerView
        mMoviesRecyclerView.setLayoutManager(layoutManager);

        // Create adapter with an empty list on start
        mMoviesAdapter = new MoviesAdapter(this);

        // Set adapter in RecyclerView
        mMoviesRecyclerView.setAdapter(mMoviesAdapter);

        // Check if network/internet is available
        if (NetworkUtils.isConnected(this)) {
            // If its available, init loader
            getLoaderManager().initLoader(MOVIES_LOADER_ID, null, this);
            mLoadingProgressBar.setVisibility(View.VISIBLE);
        } else {
            // If not available, display no internet connection warning
            mLoadingProgressBar.setVisibility(View.GONE);
            mEmptyViewTextView.setText(R.string.no_internet_connection);
            mMoviesRecyclerView.setVisibility(View.GONE);
            mEmptyViewTextView.setVisibility(View.VISIBLE);
        }

    }

    // Method called to create the loader
    @NonNull
    @Override
    public Loader<List<Movie>> onCreateLoader(int id, @Nullable Bundle args) {


        // Get preferences order by value and sent in loader constructor
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String queryOrder = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_most_popular)
        );

        return new MoviesLoader(this, queryOrder);
    }

    // Method called when loading of information/background thread is finished, updating the UI
    @Override
    public void onLoadFinished(@NonNull Loader<List<Movie>> loader, List<Movie> data) {
        mLoadingProgressBar.setVisibility(View.GONE);

        // In case no news are available to display
        mEmptyViewTextView.setText(R.string.no_movies_available);

        // Clear adapter before updating it
        mMoviesAdapter.clearData();

        // Add data to adapter, if there is data, or else shows empty view
        if (data != null && !data.isEmpty()) {
            mMoviesAdapter.setMoviesData(data);
            mMoviesRecyclerView.setVisibility(View.VISIBLE);
            mEmptyViewTextView.setVisibility(View.GONE);
        } else {
            mMoviesRecyclerView.setVisibility(View.GONE);
            mEmptyViewTextView.setVisibility(View.VISIBLE);
        }
    }

    // Method that is called when loader is reset, and then adapter is cleared
    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        mMoviesAdapter.clearData();
    }

    // This method handle click events on RecyclerView items, receiving a Movie object
    @Override
    public void onClick(Movie movie) {
        Intent detailsIntent = new Intent(this, DetailsActivity.class);
        detailsIntent.putExtra(Movie.MOVIE_TAG, movie);
        startActivity(detailsIntent);
    }

    // Inflate main.xml as a menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // Method called when menu item is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // If Settings option is clicked, it starts the Settings Activity;
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Method called when preferences have been changed, then the loader is restarted checking the new information
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        mMoviesAdapter.clearData();
        mEmptyViewTextView.setVisibility(View.GONE);
        mLoadingProgressBar.setVisibility(View.VISIBLE);
        getLoaderManager().restartLoader(MOVIES_LOADER_ID, null, this);
    }
}
