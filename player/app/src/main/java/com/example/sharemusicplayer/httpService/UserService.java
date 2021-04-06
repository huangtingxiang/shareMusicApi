package com.example.sharemusicplayer.httpService;

import com.example.sharemusicplayer.config.BaseConfig;
import com.example.sharemusicplayer.entity.User;

import io.reactivex.rxjava3.subjects.BehaviorSubject;
import okhttp3.RequestBody;

public class UserService {

    public static UserService userService;

    public static UserService getInstance() {
        if (userService == null) {
            userService = new UserService();
        }
        return userService;
    }

    public static final String TOKEN_HEADER = "Authorization";

    public BehaviorSubject<User> currentUser = BehaviorSubject.createDefault(new User());  // 当前登陆用户

    BaseHttpService httpService = BaseHttpService.getInstance();

    /**
     * 注册旅客
     *
     * @param callBack
     * @param user
     */
    public void register(BaseHttpService.CallBack callBack, User user) {
        httpService.post(BaseConfig.LOCAL_URL + "user/register", user, callBack, User.class);
    }

    /**
     * 登陆
     *
     * @param callBack
     * @param user
     */
    public void login(BaseHttpService.CallBack callBack, User user) {
        httpService.post(BaseConfig.LOCAL_URL + "user/loginByToken", user, callBack, User.class);
    }

    /**
     * 获取当前登陆用户
     *
     * @param callBack
     */
    public void getCurrentUser(BaseHttpService.CallBack callBack) {
        httpService.get(BaseConfig.LOCAL_URL + "user/currentUser", callBack, User.class);
    }

    /**
     * 修改头像
     *
     * @param data
     * @param callBack
     */
    public void uploadImage(RequestBody data, BaseHttpService.CallBack callBack) {
        httpService.putByForm(BaseConfig.LOCAL_URL + "user/changeImage", data, callBack, String.class);
    }

    /**
     * 获取所有的用户
     * @param callBack
     */
    public void getAllUser(BaseHttpService.CallBack callBack) {
        httpService.get(BaseConfig.LOCAL_URL + "user/getAllUser", callBack, User[].class);
    }
}
