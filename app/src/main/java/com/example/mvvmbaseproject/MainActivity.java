package com.example.mvvmbaseproject;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mvvmbaseproject.databinding.ActivityMainBinding;
import com.example.mvvmbaseproject.interfaces.MainActivityResultCallbacks;
import com.example.mvvmbaseproject.viewmodel.MainActivityViewModel;
import com.example.mvvmbaseproject.viewmodel.MainActivityViewModelFactory;

import com.example.mvvmbaseproject.utils.Common;

public class MainActivity extends AppCompatActivity implements MainActivityResultCallbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        Common.mContext = this;
        ActivityMainBinding activityMainBinding = DataBindingUtil
                .setContentView(this, R.layout.activity_main);
        activityMainBinding.setViewModel(ViewModelProviders.of(
                this,
                new MainActivityViewModelFactory(this))
                .get(MainActivityViewModel.class));
    }

    @Override
    public void onSuccess(String message) {

    }

    @Override
    public void onError(String message) {

    }
}
