package com.example.udacity_android_developer_nanodegree_project2.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {

    // Verify connection to the network/internet
    public static boolean isConnected(Context context){

        // Get connectivity manager
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get Network info
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // Return if or not network is available and internet is connected
        return networkInfo != null && networkInfo.isConnected();
    }


}
