package com.example.sharemusicplayer.localPlayer.fragment;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sharemusicplayer.R;
import com.example.sharemusicplayer.entity.Song;
import com.example.sharemusicplayer.httpService.BaseHttpService;
import com.example.sharemusicplayer.httpService.SongService;
import com.example.sharemusicplayer.localPlayer.view.ActionMenuAdapter;
import com.example.sharemusicplayer.localPlayer.view.LocalSongsAdapter;
import com.example.sharemusicplayer.musicPlayer.activities.PlayerActivity;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnItemClickListener;


public class LocalPlayerFragment extends Fragment {
    private RecyclerView songsView; // 歌曲列表滑动条
    private RecyclerView.LayoutManager layoutManager;
    private LocalSongsAdapter songsAdapter; // 本地歌曲adapter

    SongService songService = SongService.getInstance();
    Song[] songs = {};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.local_player_fragment, container, false);

        // 设置本地歌曲列表
        songsView = rootView.findViewById(R.id.local_songs);
        songsView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        songsView.setLayoutManager(layoutManager);
        songsAdapter = new LocalSongsAdapter(songs, new LocalSongsAdapter.SongClickListener() {
            @Override
            public void onClick(Song song, int position) {
                ((PlayerActivity) getActivity()).setPlayList(songs, position);
                ((PlayerActivity) getActivity()).replay();
            }
        }, false, new LocalSongsAdapter.ActionClickListener() {
            @Override
            public void onClick(View view) {
                // TODO 修改弹出框为对应操作
                // 点击action时显示dialog
                DialogPlus dialog = DialogPlus.newDialog(LocalPlayerFragment.this.getActivity())
                        .setAdapter(new ActionMenuAdapter())
                        .setContentHolder(new ListHolder())
                        .setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                            }
                        })
                        .setCancelable(true)
                        .setGravity(Gravity.BOTTOM)
                        .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                        .setExpanded(true)  // This will enable the expand feature, (similar to android L share dialog)
                        .create();
                dialog.show();
            }
        });

        songsView.setAdapter(songsAdapter);

        // TODO 获取本地歌曲并转化为songs格式
        songService.recommendSongs(new BaseHttpService.CallBack() {
            @Override
            public void onSuccess(BaseHttpService.HttpTask.CustomerResponse result) {
                songs = (Song[]) result.getData();
                songsAdapter.setSongs(songs);
            }
        });

        // 设置搜索菜单 TODO 监听各个选项的点击
        Toolbar myToolbar = rootView.findViewById(R.id.my_toolbar2);
        myToolbar.inflateMenu(R.menu.local_menu);
        setHasOptionsMenu(true);
        return rootView;
    }

}