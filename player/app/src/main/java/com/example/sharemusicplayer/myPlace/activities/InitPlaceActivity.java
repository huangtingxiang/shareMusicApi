package com.example.sharemusicplayer.myPlace.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.sharemusicplayer.R;
import com.example.sharemusicplayer.entity.Place;
import com.example.sharemusicplayer.httpService.BaseHttpService;
import com.example.sharemusicplayer.httpService.DownloadImageTask;
import com.example.sharemusicplayer.httpService.PlaceService;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class InitPlaceActivity extends AppCompatActivity {

    Toolbar myToolbar;
    private static final int WRITE_EXTERNAL_STORAGE_CODE = 0;
    private static final int ImageCode = 1;
    Place createPlace = new Place(); // 当前创建的圈子
    private ImageView imageView;

    EditText nameTex;
    EditText desNameTex;
    EditText labelTex;

    PlaceService placeService = PlaceService.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_place);

        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // 显示返回按钮
        getSupportActionBar().setDisplayShowTitleEnabled(false);    // 不显示标题


        // 修改圈子封面
        imageView = findViewById(R.id.image);
        nameTex = findViewById(R.id.place_name_tex);
        desNameTex = findViewById(R.id.place_des_tex);
        labelTex = findViewById(R.id.place_label);
        findViewById(R.id.image_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 判断是否有相册权限
                if (ContextCompat.checkSelfPermission(InitPlaceActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(InitPlaceActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_CODE);
                } else {
                    openCamera();
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.save_menu, menu);

        MenuItem saveItem = menu.findItem(R.id.action_save);
        saveItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // 保存圈子信息 进入添加用户步骤
                createPlace.setName(nameTex.getText().toString());
                createPlace.setDesName(desNameTex.getText().toString());
                createPlace.setLabel(labelTex.getText().toString());

                placeService.createPlace(createPlace, new BaseHttpService.CallBack() {
                    @Override
                    public void onSuccess(BaseHttpService.HttpTask.CustomerResponse result) {
                        Intent intent = new Intent(InitPlaceActivity.this, ChooseUserActivity.class);
                        intent.putExtra(ChooseUserActivity.PLACE_ID, ((Place) result.getData()).getId());
                        startActivity(intent);
                        finish();
                    }
                });
                return false;
            }
        });
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    Snackbar.make(myToolbar, "请允许打开相册权限!", Snackbar.LENGTH_SHORT)
                            .show();
                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case ImageCode:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = intent.getData();

                    String filePath = getPath(selectedImage);
                    String file_extn = filePath.substring(filePath.lastIndexOf(".") + 1);
                    if (file_extn.equals("img") || file_extn.equals("jpg") || file_extn.equals("jpeg") || file_extn.equals("gif") || file_extn.equals("png")) {
                        if (filePath != null) {
                            File file = new File(filePath);
                            MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
                            final RequestBody req = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("file", file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file))
                                    .build();
                            placeService.uploadImage(req, new BaseHttpService.CallBack() {
                                @Override
                                public void onSuccess(BaseHttpService.HttpTask.CustomerResponse result) {
                                    String url = (String) result.getData();
                                    createPlace.setPicUrl(url);
                                    new DownloadImageTask(imageView)
                                            .execute(BaseHttpService.BASE_URL + url);
                                }
                            });
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        String imagePath = cursor.getString(column_index);

        return imagePath;
    }

    /**
     * 打开相册
     */
    private void openCamera() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, ImageCode);
    }
}
