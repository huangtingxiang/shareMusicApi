package com.jdxiang.shareMusicApi.service;

import com.jdxiang.shareMusicApi.entity.Place;
import com.jdxiang.shareMusicApi.entity.User;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PlaceService {

    /**
     * 判断用户是否创建了圈子
     *
     * @return
     */
    Boolean isCreatePlace(User user);

    /**
     * 创建圈子
     *
     * @param place
     * @return
     */
    Place createPlace(Place place, HttpServletRequest request);

    /**
     * 将用户添加到圈子
     *
     * @param users
     */
    void addUsersToPlace(Long id, List<User> users);

    /**
     * 上传封面图像
     *
     * @return
     */
    String uploadImage(MultipartFile file);

    /**
     * 通过id获取
     * @param id
     * @return
     */
    Place getById(Long id);

    /**
     * 获取当前登陆用户的圈子
     * @return
     */
    Place getCurrentPlace();

    /**
     * 获取当前登陆用户的关联圈子
     * @return
     */
    List<Place> getByCurrentUser();

}
