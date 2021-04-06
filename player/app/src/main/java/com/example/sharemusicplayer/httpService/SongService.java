package com.example.sharemusicplayer.httpService;

import com.example.sharemusicplayer.config.BaseConfig;
import com.example.sharemusicplayer.entity.PlayList;
import com.example.sharemusicplayer.entity.Song;

/**
 * 歌曲服务
 */
public class SongService {

    public static SongService songService;

    public static SongService getInstance() {
        if (songService == null) {
            songService = new SongService();
        }
        return songService;
    }

    BaseHttpService httpService = new BaseHttpService();

    /**
     * 搜索歌曲 会调函数中传递歌曲列表
     *
     * @param callBack
     */
    public void search(BaseHttpService.CallBack callBack, String name) {
        httpService.get(BaseConfig.SPIDER_URL + "search/" + name, callBack, Song[].class);
    }


    /**
     * 获取榜单信息
     *
     * @param callBack
     * @param id
     */
    public void topList(BaseHttpService.CallBack callBack, Long id) {
        httpService.get(BaseConfig.SPIDER_URL + "top/list/" + id, callBack, Song[].class);
    }

    /**
     * 获取歌曲链接
     *
     * @param callBack
     * @param id
     */
    public void songLink(BaseHttpService.CallBack callBack, Long id) {
        httpService.get(BaseConfig.SPIDER_URL + "song/link/" + id, callBack, String.class);
    }

    /**
     * 推荐歌单
     *
     * @param callBack
     */
    public void recommendPlayList(BaseHttpService.CallBack callBack) {
        httpService.get(BaseConfig.SPIDER_URL + "recommend/playList", callBack, PlayList[].class);
    }

    /**
     * 推荐歌曲
     *
     * @param callBack
     */
    public void recommendSongs(BaseHttpService.CallBack callBack) {
        httpService.get(BaseConfig.SPIDER_URL + "recommend/songs", callBack, Song[].class);
    }

    /**
     * 歌单详情
     *
     * @param callBack
     * @param id
     */
    public void playlistDetail(BaseHttpService.CallBack callBack, Long id) {
        httpService.get(BaseConfig.SPIDER_URL + "playlist/detail/" + id, callBack, Song[].class);
    }

    /**
     * 创建歌曲到歌单中 本地服务
     * @param callBack
     * @param playId
     * @param song
     */
    public void createSongToPlayList(BaseHttpService.CallBack callBack, Long playId, Song song) {
        httpService.post(BaseConfig.LOCAL_URL + "song/createByPlayList/" + playId, song, callBack, Song.class);
    }

    public void playListDetailForLocal(BaseHttpService.CallBack callBack, Long id) {
        httpService.get(BaseConfig.LOCAL_URL + "song/getByPlayList/" + id, callBack, Song[].class);
    }
}
