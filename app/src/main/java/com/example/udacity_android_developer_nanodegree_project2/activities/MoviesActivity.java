package com.example.udacity_android_developer_nanodegree_project2.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.udacity_android_developer_nanodegree_project2.R;
import com.example.udacity_android_developer_nanodegree_project2.adapters.MoviesAdapter;
import com.example.udacity_android_developer_nanodegree_project2.database.RoomDatabase;
import com.example.udacity_android_developer_nanodegree_project2.loaders.MoviesLoader;
import com.example.udacity_android_developer_nanodegree_project2.models.MoviesViewModel;
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

    // Query that informs the Order By preference selected
    private String mQuery;

    // Database instance
    private RoomDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        // Initialize database variable
        mDatabase = RoomDatabase.getInstance(getApplicationContext());

        // Get SharedPreferences for this app
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Register to be notified of preference changes
        prefs.registerOnSharedPreferenceChangeListener(this);

        updatePreferencesQuery();

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

        // Fetch movies data method call
        loadMovieData();
    }

    // Method to retrieve movies data, depending of network info and preferences selected
    private void loadMovieData() {

        // Check if network/internet is available
        if (NetworkUtils.isConnected(this) && !mQuery.equals(getString(R.string.settings_order_by_favorites))) {

            // If its available, init loader, if loader already exists , restart it
            LoaderManager loaderManager = getLoaderManager();

            if (loaderManager.getLoader(MOVIES_LOADER_ID) != null) {
                getLoaderManager().restartLoader(MOVIES_LOADER_ID, null, this);
            } else {
                getLoaderManager().initLoader(MOVIES_LOADER_ID, null, this);
            }

            mLoadingProgressBar.setVisibility(View.VISIBLE);
        } else if (mQuery.equals(getString(R.string.settings_order_by_favorites))) {

            // If favourites preference is selected, dont use loader , but get data from Room

            MoviesViewModel viewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);
            viewModel.getMovies().observe(this, new Observer<List<Movie>>() {
                @Override
                public void onChanged(@Nullable List<Movie> movies) {
                    if (mQuery.equals(getString(R.string.settings_order_by_favorites))) {
                        updateUI(movies);
                    }
                }
            });

            mLoadingProgressBar.setVisibility(View.VISIBLE);
        } else {
            // If not available, display no internet connection warning
            mLoadingProgressBar.setVisibility(View.GONE);
            mEmptyViewTextView.setText(R.string.no_internet_connection);
            mMoviesRecyclerView.setVisibility(View.GONE);
            mEmptyViewTextView.setVisibility(View.VISIBLE);
        }
    }

    // Update mQuery variable that decides the sort order of the movie list
    private void updatePreferencesQuery() {

        // Get preferences order by value and sent in loader constructor
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        mQuery = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_most_popular)
        );
    }

    // Method called to create the loader
    @NonNull
    @Override
    public Loader<List<Movie>> onCreateLoader(int id, @Nullable Bundle args) {
        return new MoviesLoader(this, mQuery);
    }

    // Method called when loading of information/background thread is finished, updating the UI
    @Override
    public void onLoadFinished(@NonNull Loader<List<Movie>> loader, List<Movie> data) {
        updateUI(data);
    }


    // Method to update UI according to data received
    private void updateUI(List<Movie> movies) {
        mLoadingProgressBar.setVisibility(View.GONE);

        // In case no news are available to display
        mEmptyViewTextView.setText(R.string.no_movies_available);

        // Clear adapter before updating it
        mMoviesAdapter.clearData();

        // Add data to adapter, if there is data, or else shows empty view
        if (movies != null && !movies.isEmpty()) {
            mMoviesAdapter.setMoviesData(movies);
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

    // Unregister SharedPreferenceChangeListener on destroy;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    // Method called when preferences have been changed, then the loader is restarted checking the new information
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        mMoviesAdapter.clearData();
        mEmptyViewTextView.setVisibility(View.GONE);
        mLoadingProgressBar.setVisibility(View.VISIBLE);
        updatePreferencesQuery();
        loadMovieData();
    }
}
