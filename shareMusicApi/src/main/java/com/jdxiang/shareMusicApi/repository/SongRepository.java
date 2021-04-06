package com.jdxiang.shareMusicApi.repository;

import com.jdxiang.shareMusicApi.entity.Song;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SongRepository extends CrudRepository<Song, Long> {

    /**
     * 获取歌单中歌曲
     * @param id
     * @return
     */
    List<Song> findAllByPlayListIdOrderByIdDesc(Long id);

}
