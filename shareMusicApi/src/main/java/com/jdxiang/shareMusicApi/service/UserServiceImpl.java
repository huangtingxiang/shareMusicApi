package com.jdxiang.shareMusicApi.service;

import com.jdxiang.shareMusicApi.entity.User;
import com.jdxiang.shareMusicApi.exception.NotAuthenticationException;
import com.jdxiang.shareMusicApi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    CommonService commonService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    HttpServletRequest request;

    @Override
    public User getCurrentUser(HttpServletRequest request) {
        String token = request.getHeader(UserServiceImpl.tokenHeader);
        Long id = Long.valueOf(CommonService.parseJWT(token).getId());

        User user = userRepository.findById(id).get();
        if (user == null) {
            throw new NotAuthenticationException("请先登陆");
        }
        return user;
    }

    @Override
    public User getCurrentUser() {
        return getCurrentUser(request);
    }

    @Override
    public User loginByToken(String username, String password, HttpServletResponse response) {
        // 进行用户名密码验证
        User user = userRepository.findAllByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            NotAuthenticationException exception = new NotAuthenticationException("用户名或密码错误");
            throw exception;
        }
        // 判断成功 返回头加入token
        String token = CommonService.createJwtToken(user.getId());
        response.setHeader(UserService.tokenHeader, token);
        return user;
    }

    @Override
    public User register(User user, HttpServletResponse response) {
        // 保存用户
        user.setId(null);
        user = userRepository.save(user);
        // 生成token 返回token
        String token = CommonService.createJwtToken(user.getId());
        response.setHeader(tokenHeader, token);
        return user;
    }

    @Override
    public String changeImage(MultipartFile file, User user) {
        String imageUrl = commonService.uploadImage(file);
        user.setImageUrl(imageUrl);
        userRepository.save(user);
        return imageUrl;
    }

    @Override
    public List<User> getAll() {
        return (List<User>) userRepository.findAll();
    }
}
