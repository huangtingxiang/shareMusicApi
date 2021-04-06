package com.example.sharemusicplayer.myPlace.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.sharemusicplayer.R;
import com.example.sharemusicplayer.entity.User;
import com.example.sharemusicplayer.httpService.BaseHttpService;
import com.example.sharemusicplayer.httpService.PlaceService;
import com.example.sharemusicplayer.httpService.UserService;
import com.example.sharemusicplayer.myPlace.view.UserListAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

/**
 * 初始化圈子选择用户
 */
public class ChooseUserActivity extends AppCompatActivity {

    public static final String PLACE_ID = "place_id";

    /**
     * 用户列表
     */
    private RecyclerView userListView;
    private UserListAdapter userListAdapter;
    private RecyclerView.LayoutManager layoutManager;

    /**
     * 选择的用户数据
     */
    UserService userService = UserService.getInstance();
    User[] chooseUsers = {};
    PlaceService placeService = PlaceService.getInstance();

    Toolbar myToolbar;
    Long id;    // 圈子id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_user);

        // 设置导航栏
        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // 显示返回按钮
        getSupportActionBar().setDisplayShowTitleEnabled(false);    // 不显示标题

        // 获取当前编辑的圈子id
        id = getIntent().getLongExtra(PLACE_ID, 0L);

        // 设置用户列表
        userListView = findViewById(R.id.user_list);
        userListView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        userListView.setLayoutManager(layoutManager);
        userListAdapter = new UserListAdapter(chooseUsers);

        // 获取所有的用户
        userService.getAllUser(new BaseHttpService.CallBack() {
            @Override
            public void onSuccess(BaseHttpService.HttpTask.CustomerResponse result) {
                User[] users = (User[]) result.getData();
                userListAdapter.setUsers(users);
            }
        });

        userListView.setAdapter(userListAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.save_menu, menu);

        // 当保存时
        MenuItem saveItem = menu.findItem(R.id.action_save);
        saveItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // 获取所有选择的用户
                List<User> users = userListAdapter.getSelectUsers();
                placeService.addUsersToPlace(new BaseHttpService.CallBack() {
                    @Override
                    public void onSuccess(BaseHttpService.HttpTask.CustomerResponse result) {
                        Snackbar snackbar = Snackbar.make(myToolbar, "添加成功!", Snackbar.LENGTH_SHORT);
                        snackbar.addCallback(new Snackbar.Callback() {
                            @Override
                            public void onDismissed(Snackbar snackbar, int event) {
                                if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                                    // 跳转到我的圈子
                                    Intent intent = new Intent(ChooseUserActivity.this, MyPlaceActivity.class);
                                    intent.putExtra(MyPlaceActivity.PLACE_ID, id);
                                    startActivity(intent);
                                    finish();
                                }
                            }

                            @Override
                            public void onShown(Snackbar snackbar) {
                            }
                        });
                        snackbar.show();
                    }
                }, id, users.toArray(new User[users.size()]));
                return false;
            }
        });
        return true;
    }
}
