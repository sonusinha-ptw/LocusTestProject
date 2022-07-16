package com.example.locasandroidtest.viewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.locasandroidtest.App;
import com.example.locasandroidtest.model.ItemObject;
import com.example.locasandroidtest.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;

public class MainViewModel extends BaseViewModel {
    private MutableLiveData<List<ItemObject>> mutableLiveData=new MutableLiveData<>();
    private File currentCaptured;
    private ItemObject currentModifyingObject;

    public ItemObject getCurrentModifyingObject() {
        return currentModifyingObject;
    }

    public void setCurrentCaptured(File coverPhoto) {
        this.currentCaptured = coverPhoto;
    }

    public File getCurrentCaptured() {
        return currentCaptured;
    }

    public LiveData<List<ItemObject>> getLiveData() {
        return mutableLiveData;
    }

    public void getListData(){
        String jsonFileString = Utils.getJsonFromAssets(App.context, "ItemObject.json");
        Gson gson = new Gson();
        Type listUserType = new TypeToken<List<ItemObject>>() { }.getType();
        List<ItemObject> users = gson.fromJson(jsonFileString, listUserType);
        mutableLiveData.postValue(users);
    }

    public void setCurrentModifyingObject(ItemObject currentModifyingObject) {
        this.currentModifyingObject=currentModifyingObject;
    }
}
