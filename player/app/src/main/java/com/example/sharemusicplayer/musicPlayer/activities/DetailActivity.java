package com.example.sharemusicplayer.musicPlayer.activities;

import androidx.annotation.RequiresApi;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Transition;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.andremion.music.MusicCoverView;
import com.example.sharemusicplayer.R;
import com.example.sharemusicplayer.entity.PlayList;
import com.example.sharemusicplayer.entity.Song;
import com.example.sharemusicplayer.httpService.BaseHttpService;
import com.example.sharemusicplayer.httpService.DownloadImageTask;
import com.example.sharemusicplayer.httpService.PlayListService;
import com.example.sharemusicplayer.httpService.SongService;
import com.example.sharemusicplayer.musicPlayer.view.TransitionAdapter;
import com.example.sharemusicplayer.musicPlayer.view.VolumeAdapter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ListHolder;

import java.io.IOException;
import java.io.InputStream;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableObserver;
import me.wcy.lrcview.LrcView;

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
    ImageView volumeControl;

    /**
     * 歌词显示所需要的控件
     */
    MusicCoverView coverView;
    LrcView lrcView;
    FloatingActionButton playerFab;
    View musicMessage;
    View lrcAction;
    ImageView lrcBack;

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

        // 获取标题 封面 歌词列表
        coverView = findViewById(R.id.cover);
        lrcView = findViewById(R.id.lrc_list);
        playerFab = findViewById(R.id.player_fab);
        musicMessage = findViewById(R.id.music_message);
        lrcAction = findViewById(R.id.lrc_action);
        lrcBack = lrcAction.findViewById(R.id.lrc_back);

        // 点击封面时候显示歌词 隐藏封面信息
        coverView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLyric();
            }
        });
        // 点击返回时 隐藏歌词 显示封面信息
        lrcBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hiddenLyric();
            }
        });

        // 调节音量
        volumeControl = findViewById(R.id.volume_control);
        volumeControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogPlus dialog = DialogPlus.newDialog(DetailActivity.this)
                        .setAdapter(new VolumeAdapter(DetailActivity.this))
                        .setContentHolder(new ListHolder())
                        .setCancelable(true)
                        .setGravity(Gravity.BOTTOM)
                        .setPadding(10, 10, 10, 10)
                        .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                        .setExpanded(false, 150)  // This will enable the expand feature, (similar to android L share dialog)
                        .create();
                dialog.show();
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
     * 获取歌词
     */
    @Override
    public void onServiceFinish() {
        sub = getNowPlayingSong().subscribeWith(new DisposableObserver<Song>() {
            @Override
            public void onNext(@NonNull Song song) {
                nowPlayingSong = song;
                // 根据歌曲id或歌曲名字获取歌词
                if (song.getSong_id() != null) {
                    songService.getLyricById(new BaseHttpService.CallBack() {
                        @Override
                        public void onSuccess(BaseHttpService.HttpTask.CustomerResponse result) {

                            String mainLrcText = result.getBody();
                            lrcView.loadLrc(mainLrcText, null);

                        }
                    }, song.getSong_id());
                } else if (song.getName() != null) {
                    songService.getLyricByName(new BaseHttpService.CallBack() {
                        @Override
                        public void onSuccess(BaseHttpService.HttpTask.CustomerResponse result) {
                            String mainLrcText = result.getBody();
                            lrcView.loadLrc(mainLrcText, null);
                        }
                    }, song.getName());
                }
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

    // 显示歌词
    public void showLyric() {
        lrcView.setVisibility(View.VISIBLE);
        coverView.setVisibility(View.GONE);
        playerFab.setVisibility(View.GONE);
        lrcAction.setVisibility(View.VISIBLE);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, R.id.lrc_list);
        musicMessage.setLayoutParams(params);
        handler.post(runnable);
    }

    // 隐藏歌词
    public void hiddenLyric() {
        lrcView.setVisibility(View.GONE);
        coverView.setVisibility(View.VISIBLE);
        playerFab.setVisibility(View.VISIBLE);
        lrcAction.setVisibility(View.GONE);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, R.id.cover);
        musicMessage.setLayoutParams(params);
        handler.removeCallbacks(runnable);
    }

    private String getLrcText(String fileName) {
        String lrcText = null;
        try {
            InputStream is = getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            lrcText = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lrcText;
    }

    /**
     * 每300毫秒 更新进度
     */
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mService.isPlaying()) {
                long time = mService.getMilliSecond();
                lrcView.updateTime(time);
            }
            handler.postDelayed(this, 300);
        }
    };
}
