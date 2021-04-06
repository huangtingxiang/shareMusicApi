package com.example.sharemusicplayer.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.sharemusicplayer.MainActivity;
import com.example.sharemusicplayer.R;
import com.example.sharemusicplayer.entity.User;
import com.example.sharemusicplayer.httpService.BaseHttpService;
import com.example.sharemusicplayer.httpService.UserService;
import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {

    Button loginBtn;
    TextView userTextView;
    TextView passwordTextView;
    private UserService userService = UserService.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        loginBtn = findViewById(R.id.loginBtn);
        // 登陆按钮点击时
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 进行用户登陆
                final String password = ((TextView) findViewById(R.id.passwordText)).getText().toString();
                final String username = ((TextView) findViewById(R.id.userNameText)).getText().toString();
                User user = new User(username, password);
                userService.login(new BaseHttpService.CallBack() {
                    @Override
                    public void onSuccess(BaseHttpService.HttpTask.CustomerResponse result) {
                        // 登陆成功
                        if (result.getResponse().code() >= 200 && result.getResponse().code() < 300) {
                            userService.currentUser.onNext((User) result.getData());
                            // 存储token 用户名 密码
                            String token = result.getResponse().header(UserService.TOKEN_HEADER);
                            SharedPreferences.Editor edit = LoginActivity.this.getSharedPreferences("user_message", MODE_PRIVATE).edit();
                            BaseHttpService.setToken(token);
                            edit.putString("token", token);
                            edit.putString("username", username);
                            edit.putString("password", password);
                            edit.apply();
                            // 进入主页面
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            // 登陆失败 提示错误
                            Snackbar.make(loginBtn, "用户名或密码错误!", Snackbar.LENGTH_SHORT)
                                    .show();
                        }
                    }
                }, user);
            }
        });

        // 进入注册界面
//        Button registerBtn = findViewById(R.id.registerBtn);
//        registerBtn.setOnClickListener((View v) -> {
//            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
//            startActivity(intent);
//        });

        // 读取token 设置basehttp
        SharedPreferences pre = getSharedPreferences("user_message", MODE_PRIVATE);
        String token = pre.getString("token", "");
        String username = pre.getString("username", "");
        String password = pre.getString("password", "");
        if (token != null && !token.equals("")) {
            BaseHttpService.setToken(token);
            // 如果能获取到当前登陆旅客 则跳过登陆界面
            userService.getCurrentUser(new BaseHttpService.CallBack() {
                @Override
                public void onSuccess(BaseHttpService.HttpTask.CustomerResponse result) {
                    // 登陆成功 直接进入主页面
                    if (result.getResponse().code() >= 200 && result.getResponse().code() < 300) {
                        // 设置当前登陆用户
                        userService.currentUser.onNext((User) result.getData());
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }


        userTextView = findViewById(R.id.userNameText);
        passwordTextView = findViewById(R.id.passwordText);
        userTextView.setText(username);
        passwordTextView.setText(password);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//设置透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//设置透明导航栏
        }
    }
}
