package com.example.sharemusicplayer.musicPlayer.activities;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharemusicplayer.R;
import com.example.sharemusicplayer.entity.OriginType;
import com.example.sharemusicplayer.entity.Song;
import com.example.sharemusicplayer.httpService.BaseHttpService;
import com.example.sharemusicplayer.httpService.DownloadImageTask;
import com.example.sharemusicplayer.httpService.SongService;
import com.example.sharemusicplayer.musicPlayer.view.PlayListAdapter;
import com.example.sharemusicplayer.musicPlayer.view.SongsAdapter;

/**
 * 用于显示 歌单的歌曲
 */
public class SongsActivity extends PlayerActivity {

    SongService songService = SongService.getInstance();
    Song[] songs = {};

    /**
     * 歌曲列表
     */
    private RecyclerView songsView;
    private SongsAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    /**
     * 歌曲信息
     */
    private ImageView imageView;
    private Toolbar myToolbar;
    private Long id;
    private String image;
    private OriginType type;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs);
        // 设置导航栏
        myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // 显示返回按钮
        imageView = findViewById(R.id.songs_image);

        // 设置歌曲列表
        songsView = findViewById(R.id.songs_view);
        songsView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        songsView.setLayoutManager(layoutManager);
        mAdapter = new SongsAdapter(songs, new SongsAdapter.SongClickListener() {
            @Override
            public void onClick(Song song, int position) {
                setPlayList(songs, position);
                replay();
            }
        }, false);
        songsView.setAdapter(mAdapter);

        // 获取并设置歌曲信息
        id = getIntent().getLongExtra(PlayListAdapter.PLAY_LIST_ID, 0L);
        image = getIntent().getStringExtra(PlayListAdapter.PLAY_LIST_IMAGE);
        type = OriginType.valueOf(getIntent().getStringExtra(PlayListAdapter.ORIGIN_TYPE));
        name = getIntent().getStringExtra(PlayListAdapter.PLAY_LIST_NAME);
        new DownloadImageTask(imageView)
                .execute(image);

        // 根据来源类型获取歌单列表
        type = OriginType.valueOf(getIntent().getStringExtra(PlayListAdapter.ORIGIN_TYPE));
        switch (type) {
            case NETEASE_MUSIC:
                songService.playlistDetail(new BaseHttpService.CallBack() {
                    @Override
                    public void onSuccess(BaseHttpService.HttpTask.CustomerResponse result) {
                        songs = (Song[]) result.getData();
                        if (songs != null) {
                            mAdapter.setSongs(songs);
                        }
                    }
                }, id);
                break;
            case LOCAL:
                // todo 请求后台数据库
                songService.playListDetailForLocal(new BaseHttpService.CallBack() {
                    @Override
                    public void onSuccess(BaseHttpService.HttpTask.CustomerResponse result) {
                        songs = (Song[]) result.getData();
                        if (songs != null) {
                            mAdapter.setSongs(songs);
                        }
                    }
                }, id);
                break;
        }

        getSupportActionBar().setTitle(name);
    }
}
