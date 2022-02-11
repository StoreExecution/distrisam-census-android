package com.storexecution.cocacola.viewmodel;

import android.content.ClipData;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PhotoViewModel extends ViewModel {

    private final MutableLiveData<String> photo = new MutableLiveData<String>();

    public void setPhoto(String item) {
        photo.setValue(item);
    }

    public LiveData<String> getPhoto() {
        return photo;
    }
}
