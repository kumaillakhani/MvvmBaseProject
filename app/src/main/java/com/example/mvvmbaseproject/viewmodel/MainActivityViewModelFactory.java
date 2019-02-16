package com.example.mvvmbaseproject.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.mvvmbaseproject.interfaces.MainActivityResultCallbacks;

public class MainActivityViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private MainActivityResultCallbacks mainActivityResultCallbacks;

    public MainActivityViewModelFactory(MainActivityResultCallbacks mainActivityResultCallbacks) {
        this.mainActivityResultCallbacks = mainActivityResultCallbacks;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainActivityViewModel(mainActivityResultCallbacks);
    }

}
