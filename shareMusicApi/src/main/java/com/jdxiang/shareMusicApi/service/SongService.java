package com.jdxiang.shareMusicApi.service;

import com.jdxiang.shareMusicApi.entity.Song;

import java.util.List;

public interface SongService {

    /**
     * 获取歌单的歌曲
     * @param playId
     * @return
     */
    List<Song> getAllByPlayList(Long playId);

    /**
     * 将歌曲创建到歌单中
     * @param playId
     * @param song
     * @return
     */
    Song createSongToPlayList(Long playId, Song song);
}
