package com.example.sharemusicplayer.musicPlayer.music;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;

import androidx.annotation.RequiresApi;

import com.example.sharemusicplayer.entity.Song;
import com.example.sharemusicplayer.httpService.BaseHttpService;
import com.example.sharemusicplayer.httpService.SongService;

import java.io.IOException;
import java.util.HashMap;

import io.reactivex.rxjava3.subjects.BehaviorSubject;

@RequiresApi(api = Build.VERSION_CODES.O)
public class PlayerService extends Service {
    HashMap header = new HashMap(); // 请求头
    private MediaPlayer mediaPlayer;    // 播放器
    int position = 1;   // 当前播放歌曲
    Song[] songList = {};   // 歌曲列表
    public boolean isPause = false;    // 是否暂停
    SongService songService = SongService.getInstance();
    BehaviorSubject<Song> nowPlayingMusic = BehaviorSubject.createDefault(new Song());  // 当前播放的音乐
    public BehaviorSubject<Boolean> playing = BehaviorSubject.createDefault(false);    // 是否在播放音乐
    NullSongLink nullSongLink;


    private final IBinder mBinder = new LocalBinder();


    public PlayerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // 初始化播放器
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setLooping(false);
        mediaPlayer.setVolume(0.5f, 0.5f);
        mediaPlayer.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);
        // 设置播放错误监听
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                mediaPlayer.reset();
                return false;
            }
        });
        // 设置加载完成时
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                nowPlayingMusic.onNext(songList[position]);
            }
        });
        // 播放完毕后播放下一首
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                next();
            }
        });
        this.initHeader();
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        this.nowPlayingMusic.onComplete();
        return super.onUnbind(intent);
    }

    /**
     * 初始化播放器请求头
     */
    public void initHeader() {
        this.header.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
        this.header.put("Cache-Control", "no-cache");
        this.header.put("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36");
        this.header.put("Accept-Language", "zh-CN,zh;q=0.9");
        this.header.put("Cache-Control", " no-cache");
        this.header.put("Connection", "keep-alive");
        this.header.put("Host", "m8.music.126.net");
    }

    /**
     * 根据当前歌曲列表位置播放歌曲
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void play() {
        if (songList.length > 0) {
            if (isPause) {
                mediaPlayer.start();
            } else {
                // 如果歌曲链接不存在 尝试根据歌曲id获取歌曲链接
                final Song song = songList[position];
                if (song.getSong_url() == null && song.getSong_id() != null) {
                    songService.songLink(new BaseHttpService.CallBack() {
                        @Override
                        public void onSuccess(BaseHttpService.HttpTask.CustomerResponse result) {
                            String link = (String) result.getData();
                            if (link == null && nullSongLink != null) {
                                nullSongLink.nullSongLink(song);
                            } else {
                                playByUrl(link);
                            }
                        }
                    }, song.getSong_id());
                } else {
                    playByUrl(song.getSong_url());
                }
            }
            isPause = false;
            playing.onNext(true);
        }
    }

    public void playByUrl(String url) {
        // 从当前位置循环播放
        mediaPlayer.reset();
        Uri uri = Uri.parse(url);
        try {
            mediaPlayer.setDataSource(this, uri, new HashMap<String, String>());
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下一首
     */
    public void next() {
        position = (position + 1) % songList.length;
        play();
    }

    /**
     * 上一首
     */
    public void before() {
        if (position == 0) {
            position = songList.length - 1;
        } else {
            --position;
        }
        play();
    }

    public void setPosition(int position) {
        if (position > songList.length) {
            this.position = songList.length;
        } else if (position < 0) {
            this.position = 0;
        } else {
            this.position = position;
        }
    }


    /**
     * 是否播放中
     *
     * @return
     */
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    /**
     * 暂停播放器
     */
    public void pause() {
        if (isPlaying()) {
            mediaPlayer.pause();
            isPause = true;
            playing.onNext(false);
        }
    }

    /**
     * 获取当前播放进度
     *
     * @return
     */
    public int getPosition() {
        if (mediaPlayer != null && (isPlaying() || isPause)) {
            return mediaPlayer.getCurrentPosition() / 1000;
        }
        return 0;
    }

    /**
     * 设置播放列表 同时位置归0
     *
     * @param songList
     */
    public void settingPlayList(Song[] songList) {
        this.songList = songList;
        this.position = 0;
    }

    /**
     * 获取总时长
     *
     * @return
     */
    public int getDuration() {
        if (mediaPlayer != null && (isPlaying() || isPause)) {
            return mediaPlayer.getDuration() / 1000;
        }
        return 0;
    }

    public BehaviorSubject<Song> getNowPlayingSong() {
        return this.nowPlayingMusic;
    }

    /**
     * Class used for the client Binder. Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {

        public PlayerService getService() {
            // Return this instance of PlayerService so clients can call public methods
            return PlayerService.this;
        }
    }

    public void setNullSongLink(NullSongLink nullSongLink) {
        this.nullSongLink = nullSongLink;
    }

    public interface NullSongLink {
        void nullSongLink(Song song);
    }
}
