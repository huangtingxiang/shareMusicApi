package com.jdxiang.shareMusicApi.controller;


import com.fasterxml.jackson.annotation.JsonView;
import com.jdxiang.shareMusicApi.entity.PlayList;
import com.jdxiang.shareMusicApi.service.PlayListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("playList")
public class PlayListController {
    @Autowired
    PlayListService playListService;

    /**
     * 创建歌单
     *
     * @param playList
     * @return
     */
    @PostMapping
    @JsonView(BaseJsonView.class)
    public PlayList create(@RequestBody PlayList playList) {
        return playListService.createPlayList(playList);
    }

    /**
     * 获取当前登陆用户的歌单
     * @return
     */
    @GetMapping
    @JsonView(BaseJsonView.class)
    public List<PlayList> getPlayLists() {
        return playListService.getPlayLists();
    }


    private interface BaseJsonView extends PlayList.UserJsonView {
    }
}
