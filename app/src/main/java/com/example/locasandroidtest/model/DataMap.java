
package com.example.locasandroidtest.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DataMap {

    @SerializedName("options")
    private List<String> mOptions;

    public List<String> getOptions() {
        return mOptions;
    }

    public void setOptions(List<String> options) {
        mOptions = options;
    }

}
