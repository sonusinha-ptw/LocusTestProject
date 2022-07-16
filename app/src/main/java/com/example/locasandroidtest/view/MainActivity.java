package com.example.locasandroidtest.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.locasandroidtest.R;
import com.example.locasandroidtest.adapter.ItemListAdapter;
import com.example.locasandroidtest.model.ItemObject;
import com.example.locasandroidtest.utils.PermissionUtils;
import com.example.locasandroidtest.utils.Utils;
import com.example.locasandroidtest.view.BaseActivity;
import com.example.locasandroidtest.viewModel.BaseViewModel;
import com.example.locasandroidtest.viewModel.MainViewModel;

import java.io.File;
import java.util.List;

public class MainActivity extends BaseActivity implements Utils.OnDialogbuttonClickListner, ItemListAdapter.onItemClickListener {

    private static final int CAMERA_REQUEST = 1001;
    private static final int CAMERA_PERMISSION_CODE = 10001;
    private ItemListAdapter itemListAdapter;
    private RecyclerView recyclerView;

    @Override
    protected MainViewModel getViewModel() {
        return new ViewModelProvider(this).get(MainViewModel.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void setUpUI(@Nullable Bundle savedInstanceState) {
        setUpRecyclerView();
        getViewModel().getListData();
        bindObserver();
    }

    private void setUpRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        itemListAdapter = new ItemListAdapter(this, getViewModel(), this);
        recyclerView.setAdapter(itemListAdapter);

    }

    private void bindObserver() {
        getViewModel().getLiveData().observe(this, itemObjects -> {
            if (itemObjects != null)
                itemListAdapter.updateData();
        });
    }

    private void takePictureFromCamera() {
        boolean hasCamPermission = PermissionUtils.hasPermission(this, Manifest.permission.CAMERA);
        if (hasCamPermission)
            launchCamera();
        else
            requestCameraPermission();
    }

    public void launchCamera() {
        Uri mCapturedImageURI;
        getViewModel().setCurrentCaptured(new File(getExternalCacheDir(),
                "image" + System.currentTimeMillis() + ".jpg"));
        mCapturedImageURI = Uri.fromFile(getViewModel().getCurrentCaptured());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
        } else {
            File file = new File(mCapturedImageURI.getPath());
            Uri photoUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    public void requestCameraPermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSION_CODE:
                if (requestCode == CAMERA_PERMISSION_CODE) {

                    if (grantResults.length > 0) {
                        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                            //operation on permission granted
                            launchCamera();
                        } else {
                            // shouldShowRequestPermissionRationale will return true if you select Deny and don't check "Never ask again"
                            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                                Utils.showCustomDialog(this, "Alert", "App needs Camera permissions for capturing image.Do you want to allow?", "Yes", "No", "camPermussion", this);
                            } else {
                                Utils.showCustomDialog(this, "Alert", "App needs Camera permission.Please enable Permissions from application setting.", "Yes", "No", "camPermussionAgain", this);
                            }
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onButtonYesClick(String from) {

        if (from.equalsIgnoreCase("camPermussionAgain")) {
            moveToAppDetailsSettingScreen();
            finish();
        } else if (from.equalsIgnoreCase("camPermussion"))
            requestCameraPermission();
    }

    protected void moveToAppDetailsSettingScreen() {
        try {

            Intent intent = new Intent();
            intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        } catch (Exception e) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            ItemObject currentModifyingObject = getViewModel().getCurrentModifyingObject();
            currentModifyingObject.setCapturedImageFile(getCapturedUri(getViewModel().getCurrentCaptured()));
            itemListAdapter.notifyItemChanged(getViewModel().getLiveData().getValue().indexOf(getViewModel().getCurrentModifyingObject()));
        }
    }

    @Override
    public void onImageCaptureClicked(int position, ItemListAdapter.ImageTypeViewHolder holder, ItemObject itemObject) {
        takePictureFromCamera();
        getViewModel().setCurrentModifyingObject(itemObject);
    }

    @Override
    public void onViewImageClicked(ItemObject itemObject) {
        Intent intent=new Intent(this,ImagePreviewActivity.class);
        intent.putExtra(ImagePreviewActivity.key,itemObject);
        startActivity(intent);
    }

    private Uri getCapturedUri(File file) {
        Uri mCapturedImageURI = Uri.fromFile(file);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return mCapturedImageURI;
        } else {
            File providerFile = new File(mCapturedImageURI.getPath());
            Uri providerUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", providerFile);
            return providerUri;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_submit:
                for (ItemObject itemObject:getViewModel().getLiveData().getValue()){
                    Log.d("Items",itemObject.toString());
                }
                Utils.showCustomDialog(this,"Alert","Logged Sucessfully","Ok","","",false,null);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}