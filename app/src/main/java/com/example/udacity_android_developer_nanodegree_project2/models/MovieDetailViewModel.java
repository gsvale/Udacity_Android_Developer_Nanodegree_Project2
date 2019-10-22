package com.example.udacity_android_developer_nanodegree_project2.models;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.udacity_android_developer_nanodegree_project2.database.RoomDatabase;
import com.example.udacity_android_developer_nanodegree_project2.objects.Movie;


public class MovieDetailViewModel extends AndroidViewModel {


    private LiveData<Movie> movie;

    public MovieDetailViewModel(Application application, String id) {
        super(application);
        RoomDatabase database = RoomDatabase.getInstance(this.getApplication());
        movie = database.movieDao().loadMovie(id);
    }

    public LiveData<Movie> getMovie() {
        return movie;
    }


}
