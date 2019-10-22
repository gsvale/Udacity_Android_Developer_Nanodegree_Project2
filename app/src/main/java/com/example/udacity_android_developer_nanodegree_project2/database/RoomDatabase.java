package com.example.udacity_android_developer_nanodegree_project2.database;


import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;

import com.example.udacity_android_developer_nanodegree_project2.objects.Movie;


// Room database used
@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class RoomDatabase extends androidx.room.RoomDatabase {

    private static final String LOG_TAG = RoomDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "movielist";

    // Database instance used
    private static RoomDatabase mDatabaseInstance;


    public static RoomDatabase getInstance(Context context) {
        if (mDatabaseInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                mDatabaseInstance = Room.databaseBuilder(context.getApplicationContext(),
                        RoomDatabase.class, RoomDatabase.DATABASE_NAME)
                        // COMPLETED (2) call allowMainThreadQueries before building the instance
                        // Queries should be done in a separate thread to avoid locking the UI
                        // We will allow this ONLY TEMPORALLY to see that our DB is working
                        .allowMainThreadQueries()
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return mDatabaseInstance;
    }

    public abstract MovieDao movieDao();

}
