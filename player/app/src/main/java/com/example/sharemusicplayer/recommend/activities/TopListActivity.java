package com.example.sharemusicplayer.recommend.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharemusicplayer.R;
import com.example.sharemusicplayer.entity.Song;
import com.example.sharemusicplayer.httpService.BaseHttpService;
import com.example.sharemusicplayer.httpService.SongService;
import com.example.sharemusicplayer.musicPlayer.activities.PlayerActivity;
import com.example.sharemusicplayer.musicPlayer.view.SongsAdapter;
import com.example.sharemusicplayer.recommend.fragment.RecommendFragment;

/**
 * 显示排行榜
 */
public class TopListActivity extends PlayerActivity {

    SongService songService = SongService.getInstance();
    private Song[] allSongs = {};

    /**
     * 排行榜歌曲信息
     */
    private RecyclerView recyclerView;
    private SongsAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Toolbar myToolbar;
    private ImageView imageView;

    private Long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_list_view);

        // 设置导航栏
        myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // 显示返回按钮

        // 设置排行榜信息
        Intent intent = getIntent();
        getSupportActionBar().setTitle(intent.getIntExtra(RecommendFragment.EXTRA_TOP_LIST_NAME, 0));
        imageView = findViewById(R.id.top_list_image);
        imageView.setImageResource(intent.getIntExtra(RecommendFragment.EXTRA_TOP_LIST_IMAGE, 0));

        // 根据排行榜id获取歌曲信息
        id = intent.getLongExtra(RecommendFragment.EXTRA_TOP_LIST_ID, 0L);
        songService.topList(new BaseHttpService.CallBack() {
            @Override
            public void onSuccess(BaseHttpService.HttpTask.CustomerResponse result) {
                // 列表数目10条10条显示
                allSongs = (Song[]) result.getData();
                mAdapter.setSongs(allSongs);
            }
        }, id);
        recyclerView = findViewById(R.id.songs_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        // 当点击时 切换播放列表
        mAdapter = new SongsAdapter(allSongs, new SongsAdapter.SongClickListener() {
            @Override
            public void onClick(Song song, int position) {
                setPlayList(allSongs, position);
                replay();
            }
        }, false);

        recyclerView.setAdapter(mAdapter);
    }
}
