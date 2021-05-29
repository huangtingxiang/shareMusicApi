package com.example.sharemusicplayer.localPlayer.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.sharemusicplayer.R;
import com.example.sharemusicplayer.Utils.MusicUtils;
import com.example.sharemusicplayer.entity.Song;
import com.example.sharemusicplayer.localPlayer.view.ActionMenuAdapter;
import com.example.sharemusicplayer.localPlayer.view.LocalSongsAdapter;
import com.example.sharemusicplayer.musicPlayer.activities.PlayerActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnItemClickListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class LocalPlayerFragment extends Fragment {
    private RecyclerView songsView; // 歌曲列表滑动条
    private RecyclerView.LayoutManager layoutManager;
    private LocalSongsAdapter songsAdapter; // 本地歌曲adapter
    private Toolbar myToolbar;  // 操作栏
    ActionMenuAdapter.ActionMenu deleteAction;
    ActionMenuAdapter.ActionMenu removeAction;
    ActionMenuAdapter.ActionMenu openAction;

    private static final int OPEN_DIRECTORY = 1;
    private static final int WRITE_EXTERNAL_STORAGE_CODE = 2;

    Song[] songList = new Song[0];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.local_player_fragment, container, false);
        // 设置操作栏动作
        removeAction = new ActionMenuAdapter.ActionMenu();
        removeAction.setDrawable(getContext().getDrawable(R.drawable.ic_baseline_remove_circle_outline_24));
        removeAction.setText("从列表中删除");
        removeAction.setL((view) -> {

        });

        deleteAction = new ActionMenuAdapter.ActionMenu();
        deleteAction.setDrawable(getContext().getDrawable(R.drawable.ic_baseline_delete_24));
        deleteAction.setText("删除本地文件");
        deleteAction.setL((view) -> {
            return;
        });

        openAction = new ActionMenuAdapter.ActionMenu();
        openAction.setDrawable(getContext().getDrawable(R.drawable.ic_baseline_folder_open_24));
        openAction.setText("打开本地文件");
        openAction.setL((view) -> {
            return;
        });


        // 设置本地歌曲列表
        songsView = rootView.findViewById(R.id.local_songs);
        songsView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        songsView.setLayoutManager(layoutManager);
        songsAdapter = new LocalSongsAdapter(songList, new LocalSongsAdapter.SongClickListener() {
            @Override
            public void onClick(Song song, int position) {
                ((PlayerActivity) getActivity()).setPlayList(songsAdapter.getSongList(), position);
                ((PlayerActivity) getActivity()).replay();
            }
        }, false, new LocalSongsAdapter.ActionClickListener() {
            @Override
            public void onClick(View view, Song song) {
                // 点击action时显示dialog
                List<ActionMenuAdapter.ActionMenu> actionMenus = new ArrayList<>();
                actionMenus.add(removeAction);
                actionMenus.add(openAction);
                actionMenus.add(deleteAction);
                DialogPlus dialog = DialogPlus.newDialog(LocalPlayerFragment.this.getActivity())
                        .setAdapter(new ActionMenuAdapter(actionMenus))
                        .setContentHolder(new ListHolder())
                        .setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                            }
                        })
                        .setCancelable(true)
                        .setGravity(Gravity.BOTTOM)
                        .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                        .setExpanded(false)  // This will enable the expand feature, (similar to android L share dialog)
                        .create();

                removeAction.setL((view1) -> {
                    songsAdapter.removeSong(song);
                    LocalPlayerFragment.this.songList = songsAdapter.getSongList();
                    saveSongList();
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                });

                openAction.setL((view1) -> {
                    Uri selectedUri = Uri.parse(song.getSong_url());
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setDataAndType(selectedUri, "resource/folder");
                    startActivity(intent);

                    startActivity(intent);
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                });

                deleteAction.setL((view1) -> {
                    songsAdapter.removeSong(song);
                    LocalPlayerFragment.this.songList = songsAdapter.getSongList();
                    saveSongList();
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        songsView.setAdapter(songsAdapter);

        // 从本地数据库中读取存储的歌曲信息
        SharedPreferences pre = this.getActivity().getSharedPreferences(getResources().getString(R.string.local_songs), MODE_PRIVATE);
        final String songListJson = pre.getString(getResources().getString(R.string.local_songs_list), "");
        Gson gson = new Gson();
        Song[] songs = gson.fromJson(songListJson, Song[].class);
        if (songs != null) {
            songList = songs;
            songsAdapter.setSongs(songList);
        }


        // 设置搜索菜单
        myToolbar = rootView.findViewById(R.id.my_toolbar2);
        myToolbar.inflateMenu(R.menu.local_menu);
        setHasOptionsMenu(true);

        // 监听搜索框清空、文字变动的事件
        final SearchView searchView = (SearchView) myToolbar.getMenu().findItem(R.id.local_songs_search).getActionView();
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                songsAdapter.setSongs(LocalPlayerFragment.this.songList);
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Song[] songs = Arrays.stream(LocalPlayerFragment.this.songList).filter(x -> x.getName().contains(newText)).toArray(Song[]::new);
                songsAdapter.setSongs(songs);
                return false;
            }
        });

        // 监听导入本地音乐和清空播放列表
        myToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    // 导入本地音乐选项
                    case R.id.import_local_music:
                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            selectFiles();  // 选择文件
                        } else {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_CODE);
                        }
                        break;
                    case R.id.clear_local_music:
                        // 清除保存歌曲列表
                        SharedPreferences.Editor edit = LocalPlayerFragment.this.getActivity().getSharedPreferences(getResources().getString(R.string.local_songs), MODE_PRIVATE).edit();
                        edit.putString(getResources().getString(R.string.local_songs_list), "");
                        edit.apply();
                        songList = new Song[0];
                        songsAdapter.setSongs(songList);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        return rootView;
    }

    /**
     * 打开选择框选择音频文件
     */
    public void selectFiles() {
        new LFilePicker()
                .withSupportFragment(LocalPlayerFragment.this)
                .withStartPath("/storage/emulated/0/netease/cloudmusic/Music/")
                .withRequestCode(OPEN_DIRECTORY)
                .withFileFilter(new String[]{".mp3", ".MP3"})
                .withMutilyMode(true)
                .withTitle("选择导入音频文件")
                .start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectFiles();
                } else {
                    Snackbar.make(myToolbar, "请允许读写存储器权限!", Snackbar.LENGTH_SHORT)
                            .show();
                }
                return;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LocalPlayerFragment.OPEN_DIRECTORY:
                if (resultCode == Activity.RESULT_OK) {
                    // 获取导入文件的列表
                    // 根据文件路径读取相应歌曲信息 并转化为song实体
                    // 显示到列表中
                    List<Song> songList = new ArrayList<>();
                    for (String path :
                            data.getStringArrayListExtra("paths")) {
                        songList.add(MusicUtils.getMusicData(path));
                    }
                    songsAdapter.addSongsToFirst(songList.toArray(new Song[songList.size()]));
                    LocalPlayerFragment.this.songList = songsAdapter.getSongList();
                    saveSongList();
                }
                break;
        }
    }

    private void saveSongList() {
        // 将本地歌单保存到数据库中
        Gson gson = new Gson();
        String songListJson = gson.toJson(LocalPlayerFragment.this.songList);  // 将歌单列表化为json字符串
        SharedPreferences.Editor edit = LocalPlayerFragment.this.getActivity().getSharedPreferences(getResources().getString(R.string.local_songs), MODE_PRIVATE).edit();
        edit.putString(getResources().getString(R.string.local_songs_list), songListJson);
        edit.apply();
    }
}