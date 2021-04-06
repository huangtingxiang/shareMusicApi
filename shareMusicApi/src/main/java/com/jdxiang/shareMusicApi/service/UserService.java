package com.jdxiang.shareMusicApi.service;

import com.jdxiang.shareMusicApi.entity.User;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface UserService {
    String tokenHeader = "Authorization";

    User getCurrentUser(HttpServletRequest request);

    User getCurrentUser();

    // 通过token登陆
    User loginByToken(String userName, String password, HttpServletResponse response);

    User register(User user, HttpServletResponse response);

    // 修改头像
    String changeImage(MultipartFile file, User user);

    List<User> getAll();
}
