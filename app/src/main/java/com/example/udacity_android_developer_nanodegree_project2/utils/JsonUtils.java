package com.example.udacity_android_developer_nanodegree_project2.utils;

import android.text.TextUtils;
import android.util.Log;

import com.example.udacity_android_developer_nanodegree_project2.objects.Movie;
import com.example.udacity_android_developer_nanodegree_project2.objects.MovieReview;
import com.example.udacity_android_developer_nanodegree_project2.objects.MovieTrailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static final String RESULTS_TAG = "results";
    private static final String ID_TAG = "id";
    private static final String POSTER_PATH_TAG = "poster_path";
    private static final String BACKDROP_PATH_TAG = "backdrop_path";
    private static final String ORIGINAL_TITLE_TAG = "original_title";
    private static final String OVERVIEW_TAG = "overview";
    private static final String VOTE_AVERAGE_TAG = "vote_average";
    private static final String RELEASE_DATE_TAG = "release_date";
    private static final String NAME_TAG = "name";
    private static final String KEY_TAG = "key";
    private static final String AUTHOR_TAG = "author";
    private static final String CONTENT_TAG = "content";

    private static final String DATA_NOT_AVAILABLE = "Data not available";


    // Parse Json data and create Movie item objects from that information
    static List<Movie> extractMoviesDataFromJson(String jsonString) {

        // If Json string is empty or null, return null
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }

        List<Movie> movies = new ArrayList<>();

        try {

            // Create Root Json Object
            JSONObject rootJsonResponse = new JSONObject(jsonString);

            // Create Movies json Array associated with key "results"
            JSONArray moviesJsonArray = rootJsonResponse.getJSONArray(RESULTS_TAG);

            int moviesJsonArraySize = moviesJsonArray.length();

            // Parse all items of array results to create Movie items
            for (int i = 0; i < moviesJsonArraySize; i++) {

                JSONObject movieJsonObject = moviesJsonArray.getJSONObject(i);

                String id;
                String posterImageUrl;
                String detailsImageUrl;
                String originalTitle;
                String plotSynopsis;
                String userRating;
                String releaseDate;

                // If no data is received, display a default message DATA_NOT_AVAILABLE
                id = movieJsonObject.optString(ID_TAG, DATA_NOT_AVAILABLE);
                posterImageUrl = movieJsonObject.optString(POSTER_PATH_TAG, DATA_NOT_AVAILABLE);
                detailsImageUrl = movieJsonObject.optString(BACKDROP_PATH_TAG, DATA_NOT_AVAILABLE);
                originalTitle = movieJsonObject.optString(ORIGINAL_TITLE_TAG, DATA_NOT_AVAILABLE);
                plotSynopsis = movieJsonObject.optString(OVERVIEW_TAG, DATA_NOT_AVAILABLE);
                userRating = movieJsonObject.optString(VOTE_AVERAGE_TAG, DATA_NOT_AVAILABLE);
                releaseDate = movieJsonObject.optString(RELEASE_DATE_TAG, DATA_NOT_AVAILABLE);

                // Create Movie item with values parsed and extracted from Json data
                Movie movieItem = new Movie(id, posterImageUrl, detailsImageUrl, originalTitle, plotSynopsis, userRating, releaseDate);
                movies.add(movieItem);
            }

        } catch (JSONException e) {
            Log.e(HttpUtils.LOG_TAG, "Problem parsing the movies JSON results", e);
        }

        return movies;
    }

    // Parse Json data and get Movie info, regarding trailers and reviews, and update Movie item with that info
    public static Movie extractMovieDataFromJsons(Movie movieItem, String trailersJsonString, String reviewsJsonString) {

        // If Json strings is empty or null, return null
        if (movieItem == null || (TextUtils.isEmpty(trailersJsonString) && TextUtils.isEmpty(reviewsJsonString))) {
            return null;
        }

        List<MovieTrailer> movieTrailers = new ArrayList<>();
        List<MovieReview> movieReviews = new ArrayList<>();

        try {

            // Create Root Json Object
            JSONObject rootJsonResponse = new JSONObject(trailersJsonString);

            // Create Trailers json Array associated with key "results"
            JSONArray trailersJsonArray = rootJsonResponse.getJSONArray(RESULTS_TAG);

            int trailersJsonArraySize = trailersJsonArray.length();

            // Parse all items of array results to create Movie trailer items
            for (int i = 0; i < trailersJsonArraySize; i++) {

                JSONObject trailerJsonObject = trailersJsonArray.getJSONObject(i);

                String name;
                String key;

                // If no data is received, display a default message DATA_NOT_AVAILABLE
                name = trailerJsonObject.optString(NAME_TAG, DATA_NOT_AVAILABLE);
                key = trailerJsonObject.optString(KEY_TAG, DATA_NOT_AVAILABLE);

                movieTrailers.add(new MovieTrailer(name, key));
            }

        } catch (JSONException e) {
            Log.e(HttpUtils.LOG_TAG, "Problem parsing the movie trailers JSON results", e);
        }

        movieItem.setTrailers(movieTrailers);

        try {

            // Create Root Json Object
            JSONObject rootJsonResponse = new JSONObject(reviewsJsonString);

            // Create Reviews json Array associated with key "results"
            JSONArray reviewsJsonArray = rootJsonResponse.getJSONArray(RESULTS_TAG);

            int reviewsJsonArraySize = reviewsJsonArray.length();

            // Parse all items of array results to create Movie review items
            for (int i = 0; i < reviewsJsonArraySize; i++) {

                JSONObject reviewJsonObject = reviewsJsonArray.getJSONObject(i);

                String author;
                String content;

                // If no data is received, display a default message DATA_NOT_AVAILABLE
                author = reviewJsonObject.optString(AUTHOR_TAG, DATA_NOT_AVAILABLE);
                content = reviewJsonObject.optString(CONTENT_TAG, DATA_NOT_AVAILABLE);

                movieReviews.add(new MovieReview(author, content));
            }


        } catch (JSONException e) {
            Log.e(HttpUtils.LOG_TAG, "Problem parsing the movie reviews JSON results", e);
        }

        movieItem.setReviews(movieReviews);

        return movieItem;
    }
}
