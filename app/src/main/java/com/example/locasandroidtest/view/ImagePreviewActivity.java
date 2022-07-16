package com.example.locasandroidtest.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.locasandroidtest.R;
import com.example.locasandroidtest.model.ItemObject;
import com.example.locasandroidtest.viewModel.BaseViewModel;

public class ImagePreviewActivity extends BaseActivity {
    public static String key="IMAGE_DATA";
    private ItemObject itemObject;

    @Override
    protected BaseViewModel getViewModel() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_image_preview;
    }

    @Override
    public void setUpUI(@Nullable Bundle savedInstanceState) {
        getIntentData();
        ImageView imageView = findViewById(R.id.imageView);
        Glide.with(this).load(itemObject.getCapturedImageFile()).into(imageView);
    }

    private void getIntentData() {
        if (getIntent()!=null && getIntent().hasExtra(key));
        itemObject=getIntent().getParcelableExtra(key);
    }
}