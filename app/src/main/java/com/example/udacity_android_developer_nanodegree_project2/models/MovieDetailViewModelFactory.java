package com.example.udacity_android_developer_nanodegree_project2.models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class MovieDetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {


    private final Application mApplication;
    private final String mId;


    public MovieDetailViewModelFactory(Application application, String id) {
        mApplication = application;
        mId = id;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MovieDetailViewModel(mApplication, mId);
    }

}
