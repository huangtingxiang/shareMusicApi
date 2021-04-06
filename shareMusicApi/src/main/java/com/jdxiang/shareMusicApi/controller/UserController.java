package com.jdxiang.shareMusicApi.controller;

import com.jdxiang.shareMusicApi.entity.User;
import com.jdxiang.shareMusicApi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/loginByToken")
    public User loginByToken(@RequestBody User user, HttpServletResponse response) {
        return userService.loginByToken(user.getUsername(), user.getPassword(), response);
    }

    @PostMapping("/register")
    public User register(@RequestBody User user, HttpServletResponse response) {
        return userService.register(user, response);
    }

    @GetMapping("/currentUser")
    public User getCurrentUser(HttpServletRequest request) {
        return this.userService.getCurrentUser(request);
    }

    @PutMapping("/changeImage")
    public String changeImage(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        User user = userService.getCurrentUser(request);
        return userService.changeImage(file, user);
    }

    @GetMapping("/getAllUser")
    public List<User> getAll() {
        return userService.getAll();
    }
}
