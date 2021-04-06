package com.example.sharemusicplayer.httpService;

import com.example.sharemusicplayer.config.BaseConfig;
import com.example.sharemusicplayer.entity.PlayList;

public class PlayListService {

    public static PlayListService playListService;

    public static PlayListService getInstance() {
        if (playListService == null) {
            playListService = new PlayListService();
        }
        return playListService;
    }

    BaseHttpService httpService = BaseHttpService.getInstance();

    /**
     * 创建歌单
     * @param callBack
     * @param playList
     */
    public void create(BaseHttpService.CallBack callBack, PlayList playList) {
        httpService.post(BaseConfig.LOCAL_URL + "playList", playList, callBack, PlayList.class);
    }

    /**
     * 获取当前登陆用户的歌单
     * @param callBack
     */
    public void getPlayLists(BaseHttpService.CallBack callBack) {
        httpService.get(BaseConfig.LOCAL_URL + "playList", callBack, PlayList[].class);
    }
}
