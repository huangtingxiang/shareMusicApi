package com.example.sharemusicplayer.recommend.fragment;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sharemusicplayer.MainActivity;
import com.example.sharemusicplayer.R;
import com.example.sharemusicplayer.entity.OriginType;
import com.example.sharemusicplayer.entity.PlayList;
import com.example.sharemusicplayer.entity.Song;
import com.example.sharemusicplayer.httpService.BaseHttpService;
import com.example.sharemusicplayer.httpService.SongService;
import com.example.sharemusicplayer.musicPlayer.view.PlayListAdapter;
import com.example.sharemusicplayer.musicPlayer.view.SongsAdapter;
import com.example.sharemusicplayer.recommend.activities.TopListActivity;

import java.util.Arrays;

public class RecommendFragment extends Fragment {
    /**
     * 排行榜
     */
    public static final String EXTRA_TOP_LIST_ID = "top_list_id";
    public static final String EXTRA_TOP_LIST_NAME = "top_list_name";
    public static final String EXTRA_TOP_LIST_IMAGE = "top_list_image";

    /**
     * 推荐歌单
     */
    private RecyclerView playListView;
    private PlayListAdapter playListAdapter;
    private GridLayoutManager gridLayoutManager;

    /**
     * 推荐歌曲
     */
    private RecyclerView songsView;
    private SongsAdapter songsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Song[] songs = {};

    private SongService songService = SongService.getInstance();

    private RecommendViewModel mViewModel;

    public static RecommendFragment newInstance() {
        return new RecommendFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recommend_fragment, container, false);

        // 热歌榜
        view.findViewById(R.id.top_list_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TopListActivity.class);
                intent.putExtra(EXTRA_TOP_LIST_ID, 1L);
                intent.putExtra(EXTRA_TOP_LIST_NAME, R.string.top_list_1);
                intent.putExtra(EXTRA_TOP_LIST_IMAGE, R.drawable.hot_songs);
                getContext().startActivity(intent);
            }
        });

        // 新歌榜
        view.findViewById(R.id.top_list_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TopListActivity.class);
                intent.putExtra(EXTRA_TOP_LIST_ID, 0L);
                intent.putExtra(EXTRA_TOP_LIST_NAME, R.string.top_list_2);
                intent.putExtra(EXTRA_TOP_LIST_IMAGE, R.drawable.new_songs);
                getContext().startActivity(intent);
            }
        });

        // 飙升榜
        view.findViewById(R.id.top_list_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TopListActivity.class);
                intent.putExtra(EXTRA_TOP_LIST_ID, 3L);
                intent.putExtra(EXTRA_TOP_LIST_NAME, R.string.top_list_3);
                intent.putExtra(EXTRA_TOP_LIST_IMAGE, R.drawable.up_songs);
                getContext().startActivity(intent);
            }
        });

        // 设置推荐歌单
        playListView = view.findViewById(R.id.recommend_play_list);
        playListView.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        playListView.setLayoutManager(gridLayoutManager);
        playListAdapter = new PlayListAdapter(new PlayList[0], OriginType.NETEASE_MUSIC);
        playListView.setAdapter(playListAdapter);
        songService.recommendPlayList(new BaseHttpService.CallBack() {
            @Override
            public void onSuccess(BaseHttpService.HttpTask.CustomerResponse result) {
                // 最多显示8个歌单
                PlayList[] playLists = (PlayList[]) result.getData();
                PlayList[] showList = playLists.length > 8 ? Arrays.copyOfRange(playLists, 0, 8) : playLists;
                playListAdapter.setPlayLists(showList);
            }
        });

        // 设置推荐歌曲
        songsView = view.findViewById(R.id.recommend_songs);
        songsView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        songsView.setLayoutManager(layoutManager);
        // 当点击时 切换播放列表
        songsAdapter = new SongsAdapter(songs, new SongsAdapter.SongClickListener() {
            @Override
            public void onClick(Song song, int position) {
                ((MainActivity) getActivity()).setPlayList(songs, position);
                ((MainActivity) getActivity()).replay();
            }
        }, false);

        songsView.setAdapter(songsAdapter);
        // 获取推荐歌曲
        songService.recommendSongs(new BaseHttpService.CallBack() {
            @Override
            public void onSuccess(BaseHttpService.HttpTask.CustomerResponse result) {
                songs = (Song[]) result.getData();
                songsAdapter.setSongs(songs);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(RecommendViewModel.class);
        // TODO: Use the ViewModel
    }
}
