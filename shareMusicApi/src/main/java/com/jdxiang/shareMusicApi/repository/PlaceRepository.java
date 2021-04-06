package com.jdxiang.shareMusicApi.repository;

import com.jdxiang.shareMusicApi.entity.Place;
import com.jdxiang.shareMusicApi.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PlaceRepository extends CrudRepository<Place, Long> {

    /**
     * 判断用户是否创建了圈子
     *
     * @param id
     * @return
     */
    boolean existsByBelongUserId(Long id);

    Place findByBelongUserId(Long id);

    List<Place> findAllByAllUser(User user);

}
