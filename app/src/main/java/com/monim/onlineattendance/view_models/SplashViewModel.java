package com.monim.onlineattendance.view_models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SplashViewModel extends ViewModel {

    private MutableLiveData<Boolean> liveData = new MutableLiveData<>();

    public LiveData<Boolean> getLiveData() {
        liveData.setValue(true);
        return liveData;
    }

}
