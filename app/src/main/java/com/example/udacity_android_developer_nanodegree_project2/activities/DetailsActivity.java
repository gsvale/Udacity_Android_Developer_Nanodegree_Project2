package com.example.udacity_android_developer_nanodegree_project2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.udacity_android_developer_nanodegree_project2.R;
import com.example.udacity_android_developer_nanodegree_project2.objects.Movie;
import com.example.udacity_android_developer_nanodegree_project2.utils.ImageUtils;
import com.squareup.picasso.Picasso;

/**
 * The type Details activity.
 */
public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // If intent is null, finish activity and show toast message
        Intent intent = getIntent();
        if (intent == null) {
            closeActivity();
            return;
        }

        // If movie is null, finish activity and show toast message
        Movie movie = intent.getParcelableExtra(Movie.MOVIE_TAG);
        if (movie == null) {
            closeActivity();
            return;
        }

        // Set Movie item values

        ImageView detailImageIv = findViewById(R.id.detail_image_iv);

        Picasso.get()
                .load(ImageUtils.getImagePath(movie.getDetailsImageUrl()))
                .placeholder(R.drawable.ic_movie_icon_default)
                .error(R.drawable.ic_movie_icon_default)
                .into(detailImageIv);

        TextView plotSynopsisTv = findViewById(R.id.plot_synopsis_tv);
        TextView userRatingTv = findViewById(R.id.user_rating_tv);
        TextView releaseDateTv = findViewById(R.id.release_date_tv);

        plotSynopsisTv.setText(movie.getPlotSynopsis());
        userRatingTv.setText(movie.getUserRating());
        releaseDateTv.setText(movie.getReleaseDate());

        setTitle(movie.getOriginalTitle());

    }

    // Method to close detail activity ans show error toast message
    private void closeActivity()  {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

}
