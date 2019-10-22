package com.example.udacity_android_developer_nanodegree_project2.models;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.udacity_android_developer_nanodegree_project2.database.RoomDatabase;
import com.example.udacity_android_developer_nanodegree_project2.objects.Movie;

import java.util.List;

public class MoviesViewModel extends AndroidViewModel {

    private LiveData<List<Movie>> movies;

    public MoviesViewModel(Application application) {
        super(application);
        RoomDatabase database = RoomDatabase.getInstance(this.getApplication());
        movies = database.movieDao().loadAllMovies();
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

}
