package com.example.udacity_android_developer_nanodegree_project2.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.udacity_android_developer_nanodegree_project2.R;
import com.example.udacity_android_developer_nanodegree_project2.database.RoomDatabase;
import com.example.udacity_android_developer_nanodegree_project2.executors.AppExecutors;
import com.example.udacity_android_developer_nanodegree_project2.loaders.MovieInfoLoader;
import com.example.udacity_android_developer_nanodegree_project2.models.MovieDetailViewModel;
import com.example.udacity_android_developer_nanodegree_project2.models.MovieDetailViewModelFactory;
import com.example.udacity_android_developer_nanodegree_project2.objects.Movie;
import com.example.udacity_android_developer_nanodegree_project2.objects.MovieReview;
import com.example.udacity_android_developer_nanodegree_project2.objects.MovieTrailer;
import com.example.udacity_android_developer_nanodegree_project2.utils.ImageUtils;
import com.example.udacity_android_developer_nanodegree_project2.utils.NetworkUtils;
import com.example.udacity_android_developer_nanodegree_project2.utils.TrailerUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * The type Details activity.
 */
public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Movie> {

    private LinearLayout mMovieDetailsLinearLayout;
    private ProgressBar mLoadingProgressBar;
    private TextView mEmptyViewTextView;

    // Loader ID
    private static final int MOVIE_DATA_LOADER_ID = 2;

    // Movie Item
    private Movie mMovie;

    // Database instance
    private RoomDatabase mDatabase;

    // Boolean if movie is favorite
    private boolean isFavorite = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Initialize database variable
        mDatabase = RoomDatabase.getInstance(getApplicationContext());

        /* This LinearLayout displays movie details */
        mMovieDetailsLinearLayout = findViewById(R.id.movie_details_ll);
        mMovieDetailsLinearLayout.setVisibility(View.GONE);

        /* This TextView display errors if they exist */
        mEmptyViewTextView = findViewById(R.id.empty_view_tv);

        /* Progress bar that shows while loading movie trailers and reviews */
        mLoadingProgressBar = findViewById(R.id.loading_pb);

        // If intent is null, finish activity and show toast message
        Intent intent = getIntent();
        if (intent == null) {
            closeActivity();
            return;
        }

        // If movie is null, finish activity and show toast message
        mMovie = intent.getParcelableExtra(Movie.MOVIE_TAG);
        if (mMovie == null) {
            closeActivity();
            return;
        }

        setTitle(mMovie.getOriginalTitle());

        // Check if network/internet is available
        if (NetworkUtils.isConnected(this)) {
            getLoaderManager().initLoader(MOVIE_DATA_LOADER_ID, null, this);
            mLoadingProgressBar.setVisibility(View.VISIBLE);
        } else {
            mLoadingProgressBar.setVisibility(View.GONE);
            mEmptyViewTextView.setVisibility(View.GONE);
            populateUI();
        }

    }

    // Method called to create the loader
    @Override
    public Loader<Movie> onCreateLoader(int id, Bundle args) {
        return new MovieInfoLoader(this, mMovie);
    }

    // Method called when loading of information/background thread is finished, populating the UI
    @Override
    public void onLoadFinished(Loader<Movie> loader, Movie data) {
        mLoadingProgressBar.setVisibility(View.GONE);
        mEmptyViewTextView.setVisibility(View.GONE);

        if (data != null) {
            mMovie = data;
        }

        populateUI();
    }

    // Method that is called when loader is reset
    @Override
    public void onLoaderReset(Loader<Movie> loader) {
        mMovieDetailsLinearLayout.setVisibility(View.GONE);
    }

    // Method to populate the UI using the movie item obtained
    private void populateUI() {
        // Set Movie item values
        ImageView detailImageIv = findViewById(R.id.detail_image_iv);

        Picasso.get()
                .load(ImageUtils.getImagePath(mMovie.getDetailsImageUrl()))
                .placeholder(R.drawable.ic_movie_icon_default)
                .error(R.drawable.ic_movie_icon_default)
                .into(detailImageIv);

        TextView plotSynopsisTv = findViewById(R.id.plot_synopsis_tv);
        TextView userRatingTv = findViewById(R.id.user_rating_tv);
        TextView releaseDateTv = findViewById(R.id.release_date_tv);

        plotSynopsisTv.setText(mMovie.getPlotSynopsis());
        userRatingTv.setText(mMovie.getUserRating());
        releaseDateTv.setText(mMovie.getReleaseDate());

        LinearLayout movieTrailersLinearLayout = findViewById(R.id.movie_trailers_ll);
        LinearLayout movieReviewsLinearLayout = findViewById(R.id.movie_reviews_ll);

        // Set movie trailers and reviews, if there are any
        if (mMovie.getTrailers() != null && !mMovie.getTrailers().isEmpty()) {

            LinearLayout trailerItemsLinearLayout = findViewById(R.id.trailer_items_ll);
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            List<MovieTrailer> trailers = mMovie.getTrailers();

            for (final MovieTrailer trailer : trailers) {
                View itemView = layoutInflater.inflate(R.layout.trailer_item_layout, null);

                TextView trailerNameTv = itemView.findViewById(R.id.trailer_name_tv);
                trailerNameTv.setText(trailer.getName());

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Uri.Builder builder = new Uri.Builder();

                        builder.scheme(TrailerUtils.SCHEME)
                                .authority(TrailerUtils.AUTHORITY)
                                .appendPath(TrailerUtils.PATH)
                                .appendQueryParameter(TrailerUtils.PATH_V, trailer.getKey());

                        Intent trailerIntent = new Intent(Intent.ACTION_VIEW);
                        trailerIntent.setData(builder.build());

                        if (trailerIntent.resolveActivity(getPackageManager()) != null) {
                            startActivity(trailerIntent);
                        }
                    }
                });

                trailerItemsLinearLayout.addView(itemView);
            }

            movieTrailersLinearLayout.setVisibility(View.VISIBLE);
        } else {
            movieTrailersLinearLayout.setVisibility(View.GONE);
        }

        if (mMovie.getReviews() != null && !mMovie.getReviews().isEmpty()) {

            LinearLayout reviewsItemsLinearLayout = findViewById(R.id.review_items_ll);
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            List<MovieReview> reviews = mMovie.getReviews();

            for (MovieReview review : reviews) {
                View itemView = layoutInflater.inflate(R.layout.review_item_layout, null);

                TextView reviewAuthorTv = itemView.findViewById(R.id.review_author_tv);
                reviewAuthorTv.setText(getString(R.string.author, review.getAuthor()));

                TextView reviewContentTv = itemView.findViewById(R.id.review_content_tv);
                reviewContentTv.setText(review.getContent());

                reviewsItemsLinearLayout.addView(itemView);
            }

            movieReviewsLinearLayout.setVisibility(View.VISIBLE);
        } else {
            movieReviewsLinearLayout.setVisibility(View.GONE);
        }

        // Verify if movie is favorite or not, then update favorite button
        MovieDetailViewModelFactory factory = new MovieDetailViewModelFactory(getApplication(), mMovie.getId());

        MovieDetailViewModel viewModel = ViewModelProviders.of(this, factory).get(MovieDetailViewModel.class);
        viewModel.getMovie().observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(@Nullable Movie movie) {
                isFavorite = movie != null;
                updateFavoriteButton();
            }
        });


        mMovieDetailsLinearLayout.setVisibility(View.VISIBLE);
    }

    // Method call when clicked on Favorite button, to delete or insert a new favorite movie
    public void setFavorite(View view) {

        // Add or remove favorite on background thread
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (isFavorite) {
                    mDatabase.movieDao().deleteMovie(mMovie);
                    isFavorite = false;
                } else {
                    mDatabase.movieDao().insertMovie(mMovie);
                    isFavorite = true;
                }

                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (isFavorite) {
                            Toast.makeText(DetailsActivity.this, getString(R.string.set_as_favorite_message), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(DetailsActivity.this, getString(R.string.set_as_no_favorite_message), Toast.LENGTH_SHORT).show();
                        }
                        updateFavoriteButton();
                    }
                });

            }
        });
    }

    // Update favourite button, depending of boolean variable isFavorite value
    private void updateFavoriteButton() {

        Button favoriteBt = findViewById(R.id.favorite_bt);

        if (isFavorite) {
            favoriteBt.setText(getString(R.string.set_as_no_favorite));

            Drawable drawable = ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_favorite
            );

            favoriteBt.setCompoundDrawablesWithIntrinsicBounds(
                    drawable, null, null, null
            );
        } else {
            favoriteBt.setText(getString(R.string.set_as_favorite));

            Drawable drawable = ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_no_favorite
            );

            favoriteBt.setCompoundDrawablesWithIntrinsicBounds(
                    drawable, null, null, null
            );
        }
    }

    // Method to close detail activity ans show error toast message
    private void closeActivity() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }
}
