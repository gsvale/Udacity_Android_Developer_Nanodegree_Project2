package com.example.udacity_android_developer_nanodegree_project2.database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.udacity_android_developer_nanodegree_project2.objects.Movie;

import java.util.List;

// Interface of Methods used on Room
@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie")
    LiveData<List<Movie>> loadAllMovies();

    @Query("SELECT * FROM movie WHERE id = :id")
    LiveData<Movie> loadMovie(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(Movie movie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);


}
