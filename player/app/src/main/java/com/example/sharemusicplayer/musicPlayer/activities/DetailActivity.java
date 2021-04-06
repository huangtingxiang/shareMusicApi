package com.example.sharemusicplayer.musicPlayer.activities;

import androidx.annotation.RequiresApi;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.transition.Transition;
import android.view.View;
import android.widget.ImageView;

import com.andremion.music.MusicCoverView;
import com.example.sharemusicplayer.R;
import com.example.sharemusicplayer.entity.PlayList;
import com.example.sharemusicplayer.entity.Song;
import com.example.sharemusicplayer.httpService.BaseHttpService;
import com.example.sharemusicplayer.httpService.DownloadImageTask;
import com.example.sharemusicplayer.httpService.PlayListService;
import com.example.sharemusicplayer.httpService.SongService;
import com.example.sharemusicplayer.musicPlayer.view.TransitionAdapter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableObserver;

/**
 * 歌曲详情
 */
public class DetailActivity extends PlayerActivity {

    private MusicCoverView mCoverView;
    DisposableObserver<Song> sub;
    ImageView add_to_play_list;
    PlayList[] playLists;
    String[] playListName;
    Song nowPlayingSong;

    PlayListService playListService = PlayListService.getInstance();
    SongService songService = SongService.getInstance();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mCoverView = (MusicCoverView) findViewById(R.id.cover);

        getWindow().getSharedElementEnterTransition().addListener(new TransitionAdapter() {
            @Override
            public void onTransitionEnd(Transition transition) {
                mCoverView.start();
            }
        });

        // 获取所有歌单
        add_to_play_list = findViewById(R.id.add_to_play_list);
        playListService.getPlayLists(new BaseHttpService.CallBack() {
            @Override
            public void onSuccess(BaseHttpService.HttpTask.CustomerResponse result) {
                playLists = (PlayList[]) result.getData();
                if (playLists != null) {
                    playListName = new String[playLists.length];
                    for (int i = 0; i < playLists.length; i++) {
                        playListName[i] = playLists[i].getName();
                    }
                    add_to_play_list.setVisibility(View.VISIBLE);
                }
            }
        });

        // 将当前播放歌曲添加到歌单中
        add_to_play_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(DetailActivity.this);
                builder.setTitle("选择歌单");
                builder.setSingleChoiceItems(playListName, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        songService.createSongToPlayList(new BaseHttpService.CallBack() {
                            @Override
                            public void onSuccess(BaseHttpService.HttpTask.CustomerResponse result) {
                                Snackbar.make(mCoverView, "成功添加到歌单" + playListName[which], Snackbar.LENGTH_SHORT)
                                        .show();
                                dialog.dismiss();
                            }
                        }, playLists[which].getId(), nowPlayingSong);
                    }
                });
                builder.show();
            }
        });
    }

    public void rewindClick(View view) {
        before();
    }

    public void forwardClick(View view) {
        next();
    }

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sub.dispose();
    }

    /**
     * 当服务绑定完成后 获取当前播放的音乐 更改图片
     */
    @Override
    public void onServiceFinish() {
        sub = getNowPlayingSong().subscribeWith(new DisposableObserver<Song>() {
            @Override
            public void onNext(@NonNull Song song) {
                nowPlayingSong = song;
                new DownloadImageTask(DetailActivity.this.mCoverView)
                        .execute(song.getPic_url());
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void onFabClick(View view) {
        if (play()) {
            mCoverView.start();
        } else {
            mCoverView.stop();
        }
    }
}
