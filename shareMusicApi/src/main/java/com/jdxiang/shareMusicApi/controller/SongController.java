package com.jdxiang.shareMusicApi.controller;


import com.fasterxml.jackson.annotation.JsonView;
import com.jdxiang.shareMusicApi.entity.Song;
import com.jdxiang.shareMusicApi.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("song")
public class SongController {
    @Autowired
    SongService songService;

    /**
     * 通过歌单id获取歌曲列表
     * @param id
     * @return
     */
    @GetMapping("/getByPlayList/{id}")
    public List<Song> getByPlayList(@PathVariable Long id) {
        return songService.getAllByPlayList(id);
    }

    /**
     * 创建歌曲到歌单 中
     * @param id
     * @param song
     * @return
     */
    @PostMapping("/createByPlayList/{id}")
    @JsonView(BaseJsonView.class)
    public Song createByPlayList(@PathVariable Long id, @RequestBody Song song) {
        return songService.createSongToPlayList(id, song);
    }

    private interface BaseJsonView {
    }
}
