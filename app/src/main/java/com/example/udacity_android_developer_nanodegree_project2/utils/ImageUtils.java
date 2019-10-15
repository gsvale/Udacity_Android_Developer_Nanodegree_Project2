package com.example.udacity_android_developer_nanodegree_project2.utils;

import android.net.Uri;

public class ImageUtils {

    private static final String SCHEME = "http";
    private static final String AUTHORITY = "image.tmdb.org";
    private static final String PATH_T = "t";
    private static final String PATH_P = "p";
    private static final String PATH_SIZE = "w185";

    // Method that receives from movie item a image string url,
    // and then returns a String created from Uri.Builder of poster image with the encoded string path received
    public static String getImagePath(String posterImageUrl) {

        Uri.Builder builder = new Uri.Builder();

        builder.scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(PATH_T)
                .appendPath(PATH_P)
                .appendPath(PATH_SIZE)
                .appendEncodedPath(posterImageUrl);

        return builder.build().toString();
    }


}
