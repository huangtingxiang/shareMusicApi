package com.jdxiang.shareMusicApi.interceptor;

import com.jdxiang.shareMusicApi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class BaseInterceptor implements HandlerInterceptor {

    @Autowired
    UserService userService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 从请求头获取token进行判断
        String token = request.getHeader("Authorization");
        if (token != null && !token.equals("")) {
            // 通过token获取登陆用户
            userService.getCurrentUser(request);
            return true;// 只有返回true才会继续向下执行，返回false取消当前请求
        }
        return false;
    }
}