package com.example.locasandroidtest.view;

import android.app.ActionBar;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;

import com.example.locasandroidtest.R;
import com.example.locasandroidtest.viewModel.BaseViewModel;


public abstract class BaseActivity<V extends BaseViewModel> extends AppCompatActivity implements LifecycleOwner {

    protected V mViewModel;

    /**
     * Create the {@link androidx.lifecycle.ViewModel} corresponding to this activity.
     *
     * @return #ViewModel for the current activity.
     */
    protected abstract V getViewModel();

    /**
     * Return the layout file resourceId to initialize for this activity.
     *
     * @return Layout file resource id.
     */
    public abstract @LayoutRes
    int getLayoutId();

    /**
     * Use this to initialize all the UI views.
     */
    public abstract void setUpUI(@Nullable Bundle savedInstanceState);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getColor(R.color.black)));
        mViewModel = getViewModel();
        //Viewmodel can null for some of the fragments like imagepreview
        setUpUI(savedInstanceState);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
            if (mViewModel != null) {
                mViewModel.setNavigator(null);
        } else {
            //It's an orientation change.
        }

    }

}
