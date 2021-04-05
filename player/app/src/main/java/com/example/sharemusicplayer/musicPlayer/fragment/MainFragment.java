package com.example.sharemusicplayer.musicPlayer.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharemusicplayer.R;
import com.example.sharemusicplayer.entity.OriginType;
import com.example.sharemusicplayer.entity.PlayList;
import com.example.sharemusicplayer.httpService.BaseHttpService;
import com.example.sharemusicplayer.httpService.PlayListService;
import com.example.sharemusicplayer.musicPlayer.view.PlayListAdapter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;

    private RecyclerView playListView;
    private PlayListAdapter playListAdapter;
    private GridLayoutManager gridLayoutManager;

    PlayListService playListService = PlayListService.getInstance();

    private Button addPlayList;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_fragment, container, false);

        // 设置弹出框
        addPlayList = view.findViewById(R.id.add_play_list);
        addPlayList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
                builder.setTitle("创建歌单");
                final View dialogView = inflater.inflate(R.layout.play_list_dialog, container, false);
                builder.setView(dialogView);
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final PlayList playList = new PlayList();
                        playList.setName(((TextView) dialogView.findViewById(R.id.play_list_name)).getText().toString());
                        playList.setDes_name(((TextView) dialogView.findViewById(R.id.play_list_des)).getText().toString());
                        playListService.create(new BaseHttpService.CallBack() {
                            @Override
                            public void onSuccess(BaseHttpService.HttpTask.CustomerResponse result) {
                                playListAdapter.addPlayList(playList);
                            }
                        }, playList);
                        // 确认会调
                    }
                });

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
               builder.show();
            }
        });
        // 设置我的歌单
        playListView = view.findViewById(R.id.play_list);
        playListView.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        playListView.setLayoutManager(gridLayoutManager);
        playListAdapter = new PlayListAdapter(new PlayList[0], OriginType.LOCAL);
        playListView.setAdapter(playListAdapter);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        // 获取歌单列表
        playListService.getPlayLists(new BaseHttpService.CallBack() {
            @Override
            public void onSuccess(BaseHttpService.HttpTask.CustomerResponse result) {
                if (result.getData() != null) {
                    playListAdapter.setPlayLists((PlayList[]) result.getData());
                }
            }
        });
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

}
