package com.example.sharemusicplayer.personal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;

import com.example.sharemusicplayer.R;
import com.example.sharemusicplayer.entity.User;
import com.example.sharemusicplayer.httpService.BaseHttpService;
import com.example.sharemusicplayer.httpService.DownloadImageTask;
import com.example.sharemusicplayer.httpService.UserService;
import com.example.sharemusicplayer.login.LoginActivity;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class PersonalActivity extends AppCompatActivity {

    UserService userService = UserService.getInstance();
    User user;
    TextView userName;
    TextView nickName;
    CircleImageView personImage;
    AppCompatButton logoutBtn;

    private static final int WRITE_EXTERNAL_STORAGE_CODE = 0;
    private static final int ImageCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        userName = findViewById(R.id.username_tex);
        nickName = findViewById(R.id.nick_name_tex);
        personImage = findViewById(R.id.person_image);
        logoutBtn = findViewById(R.id.logout);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
                // 返回登陆界面
                Intent intentToLogin = new Intent(PersonalActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentToLogin);
            }
        });

        userService.currentUser.subscribe(new Observer<User>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull User user) {
                PersonalActivity.this.user = user;
                updateMessage(user);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        // 修改头像
        findViewById(R.id.visitor_image_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 判断是否有相册权限
                if (ContextCompat.checkSelfPermission(PersonalActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PersonalActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_CODE);
                } else {
                    openCamera();
                }

            }
        });
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
                    Snackbar.make(personImage, "请允许打开相册权限!", Snackbar.LENGTH_SHORT)
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
                            userService.uploadImage(req, new BaseHttpService.CallBack() {
                                @Override
                                public void onSuccess(BaseHttpService.HttpTask.CustomerResponse result) {
                                    user.setImageUrl((String) result.getData());
                                    updateMessage(user);
                                    userService.currentUser.onNext(user);
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


    /**
     * 更新用户信息
     * @param user
     */
    public void updateMessage(User user) {
        userName.setText(user.getUsername());
        nickName.setText(user.getNickName());

        if (user.getImageUrl() != null && !user.getImageUrl().equals("")) {
            String urlString = BaseHttpService.BASE_URL + user.getImageUrl();
            new DownloadImageTask(personImage)
                    .execute(urlString);
        }
    }

    private void logout() {
        // 退出登陆 修改basehttp token信息 并修改数据库中token
        BaseHttpService.setToken("");
        SharedPreferences.Editor edit = PersonalActivity.this.getSharedPreferences("user_message", MODE_PRIVATE).edit();
        edit.putString("token", "");
        edit.apply();
    }
}
