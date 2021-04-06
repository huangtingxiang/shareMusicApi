package com.example.sharemusicplayer.httpService;

import com.example.sharemusicplayer.config.BaseConfig;
import com.example.sharemusicplayer.entity.Place;
import com.example.sharemusicplayer.entity.User;

import java.util.List;

import okhttp3.RequestBody;

public class PlaceService {
    public static PlaceService placeService;

    public static PlaceService getInstance() {
        if (placeService == null) {
            placeService = new PlaceService();
        }
        return placeService;
    }

    BaseHttpService httpService = BaseHttpService.getInstance();


    /**
     * 判断当前登陆用户 是否创建过圈子
     *
     * @param callBack
     */
    public void isCreatePlace(BaseHttpService.CallBack callBack) {
        httpService.get(BaseConfig.LOCAL_URL + "place/isCreate", callBack, Boolean.class);
    }

    /**
     * 上传圈子封面
     *
     * @param data
     * @param callBack
     */
    public void uploadImage(RequestBody data, BaseHttpService.CallBack callBack) {
        httpService.postByForm(BaseConfig.LOCAL_URL + "place/uploadImage", data, callBack, String.class);
    }

    /**
     * 创建圈子
     *
     * @param place
     * @param callBack
     * @return
     */
    public void createPlace(Place place, BaseHttpService.CallBack callBack) {
        httpService.post(BaseConfig.LOCAL_URL + "place", place, callBack, Place.class);
    }

    /**
     * 将用户添加到圈子
     *
     * @param callBack
     * @param id
     * @param users
     */
    public void addUsersToPlace(BaseHttpService.CallBack callBack, Long id, User[] users) {
        httpService.put(BaseConfig.LOCAL_URL + "place/addUsers/" + id, users, callBack, null);
    }

    /**
     * 通过id获取圈子
     *
     * @param callBack
     * @param id
     */
    public void getById(BaseHttpService.CallBack callBack, Long id) {
        httpService.get(BaseConfig.LOCAL_URL + "place/" + id, callBack, Place.class);
    }

    /**
     * 获取当前登陆用户的圈子
     * @param callBack
     */
    public void getCurrentPlace(BaseHttpService.CallBack callBack) {
        httpService.get(BaseConfig.LOCAL_URL + "place/currentPlace", callBack, Place.class);
    }

    /**
     * 获取当前登陆用户关联的圈子
     * @param callBack
     */
    public void getCurrentPlaces(BaseHttpService.CallBack callBack) {
        httpService.get(BaseConfig.LOCAL_URL + "place/currentPlaces", callBack, Place[].class);
    }
}
