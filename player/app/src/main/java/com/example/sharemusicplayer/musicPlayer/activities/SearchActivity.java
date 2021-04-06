package com.example.sharemusicplayer.musicPlayer.activities;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.sharemusicplayer.R;
import com.example.sharemusicplayer.entity.Song;
import com.example.sharemusicplayer.httpService.BaseHttpService;
import com.example.sharemusicplayer.musicPlayer.view.SongsAdapter;
import com.google.gson.Gson;

public class SearchActivity extends PlayerActivity {

    private RecyclerView recyclerView;
    private SongsAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Song[] searchSongs = {};

    public static final String GET_RESULT = "get_result";
    public static final String SONG_RESULT = "song_result";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // 显示返回按钮
        getSupportActionBar().setDisplayShowTitleEnabled(false);    // 不显示标题

        recyclerView = findViewById(R.id.songs_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        String action = getIntent().getAction();
        if (action == null) {
            mAdapter = new SongsAdapter(searchSongs, new SearchAndListener());
        } else {
            switch (action) {
                case GET_RESULT:
                    mAdapter = new SongsAdapter(searchSongs, new SearchAndResult());
                    break;
                default:
                    mAdapter = new SongsAdapter(searchSongs, new SearchAndListener());
                    break;
            }
        }
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.serach_menu, menu);

        // 当搜索确认时 发送后台请求获取歌曲信息
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView =
                (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                SearchActivity.this.search(query, new BaseHttpService.CallBack() {
                    @Override
                    public void onSuccess(BaseHttpService.HttpTask.CustomerResponse result) {
                        searchSongs = (Song[]) result.getData();
                        mAdapter.setSongs(searchSongs);
                    }
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    /**
     * 点击搜索结果后播放
     */
    private class SearchAndListener implements SongsAdapter.SongClickListener {

        @Override
        public void onClick(Song song, int position) {
            setPlayList(searchSongs, position);
            replay();
        }
    }

    /**
     * 点击搜索结果后回调结果
     */
    private class SearchAndResult implements SongsAdapter.SongClickListener {

        @Override
        public void onClick(Song song, int position) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra(SONG_RESULT, new Gson().toJson(song));
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }
}
