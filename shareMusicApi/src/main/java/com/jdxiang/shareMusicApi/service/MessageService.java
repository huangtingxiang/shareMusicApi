package com.jdxiang.shareMusicApi.service;

import com.jdxiang.shareMusicApi.entity.Message;

import java.util.List;

public interface MessageService {

    /**
     * 根据圈子id获取消息
     * @param placeId
     * @return
     */
    List<Message> getByPlace(Long placeId);

    /**
     * 创建圈子消息
     * @param placeId
     * @return
     */
    Message createMessage(Long placeId, Message message);
}
