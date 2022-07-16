package com.example.locasandroidtest.viewModel;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.example.locasandroidtest.view.BaseView;

import java.lang.ref.WeakReference;

public abstract class BaseViewModel<N extends BaseView> extends ViewModel {



    private WeakReference<N> mNavigator;

    public @Nullable
    N getNavigator() {
        if (mNavigator != null) {
            return mNavigator.get();
        }
        return null;
    }

    public void setNavigator(N navigator) {
        this.mNavigator = new WeakReference<>(navigator);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (mNavigator != null) {
            mNavigator = null;
        }
    }
}
