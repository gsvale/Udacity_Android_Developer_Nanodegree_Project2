package com.example.udacity_android_developer_nanodegree_project2.utils;

import android.text.TextUtils;
import android.util.Log;

import com.example.udacity_android_developer_nanodegree_project2.objects.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class JsonUtils {

    private static final String RESULTS_TAG = "results";
    private static final String POSTER_PATH_TAG = "poster_path";
    private static final String BACKDROP_PATH_TAG = "backdrop_path";
    private static final String ORIGINAL_TITLE_TAG = "original_title";
    private static final String OVERVIEW_TAG = "overview";
    private static final String VOTE_AVERAGE_TAG = "vote_average";
    private static final String RELEASE_DATE_TAG = "release_date";

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

                String posterImageUrl;
                String detailsImageUrl;
                String originalTitle;
                String plotSynopsis;
                String userRating;
                String releaseDate;

                // If no data is received, display a default message DATA_NOT_AVAILABLE
                posterImageUrl = movieJsonObject.optString(POSTER_PATH_TAG, DATA_NOT_AVAILABLE);
                detailsImageUrl = movieJsonObject.optString(BACKDROP_PATH_TAG, DATA_NOT_AVAILABLE);
                originalTitle = movieJsonObject.optString(ORIGINAL_TITLE_TAG, DATA_NOT_AVAILABLE);
                plotSynopsis = movieJsonObject.optString(OVERVIEW_TAG, DATA_NOT_AVAILABLE);
                userRating = movieJsonObject.optString(VOTE_AVERAGE_TAG, DATA_NOT_AVAILABLE);
                releaseDate = movieJsonObject.optString(RELEASE_DATE_TAG, DATA_NOT_AVAILABLE);

                // Create Movie item with values parsed and extracted from Json data
                Movie movieItem = new Movie(posterImageUrl, detailsImageUrl, originalTitle, plotSynopsis, userRating, releaseDate);
                movies.add(movieItem);
            }

        } catch (JSONException e) {
            Log.e(HttpUtils.LOG_TAG, "Problem parsing the movies JSON results", e);
        }

        return movies;
    }

}
