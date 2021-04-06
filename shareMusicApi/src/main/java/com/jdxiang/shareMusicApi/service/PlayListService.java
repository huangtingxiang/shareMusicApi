package com.jdxiang.shareMusicApi.service;

import com.jdxiang.shareMusicApi.entity.PlayList;

import java.util.List;

public interface PlayListService {

    /**
     * 创建用户歌单
     * @param playList
     * @return
     */
    PlayList createPlayList(PlayList playList);

    /**
     * 获取当前登陆用户的歌单
     * @return
     */
    List<PlayList> getPlayLists();
}
