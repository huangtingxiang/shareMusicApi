package com.example.sharemusicplayer.httpService;

import com.example.sharemusicplayer.config.BaseConfig;
import com.example.sharemusicplayer.entity.Message;

public class MessageService {
    public static MessageService messageService;

    public static MessageService getInstance() {
        if (messageService == null) {
            messageService = new MessageService();
        }
        return messageService;
    }

    BaseHttpService httpService = BaseHttpService.getInstance();

    /**
     * 创建消息
     *
     * @param callBack
     * @param placeId
     * @param message
     */
    public void createMessage(BaseHttpService.CallBack callBack, Long placeId, Message message) {
        httpService.put(BaseConfig.LOCAL_URL + "message/" + placeId, message, callBack, Message.class);
    }

    /**
     * 通过圈子id 获取消息
     * @param callBack
     * @param placeId
     */
    public void getMessageByPlace(BaseHttpService.CallBack callBack, Long placeId) {
        httpService.get(BaseConfig.LOCAL_URL + "message/" + placeId, callBack, Message[].class);
    }
}
