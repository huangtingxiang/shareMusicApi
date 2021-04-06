package com.jdxiang.shareMusicApi.repository;

import com.jdxiang.shareMusicApi.entity.PlayList;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PlayListRepository extends CrudRepository<PlayList, Long> {
    /**
     * 通过用户获取歌单
     * @param userId
     * @return
     */
    List<PlayList> findAllByUserIdOrderByIdDesc(Long userId);
}
