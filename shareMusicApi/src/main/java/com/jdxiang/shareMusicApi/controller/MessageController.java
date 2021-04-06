package com.jdxiang.shareMusicApi.controller;


import com.fasterxml.jackson.annotation.JsonView;
import com.jdxiang.shareMusicApi.entity.Message;
import com.jdxiang.shareMusicApi.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("message")
public class MessageController {
    @Autowired
    MessageService messageService;

    /**
     * 通过圈子获取消息
     * @param placeId
     * @return
     */
    @GetMapping("{placeId}")
    @JsonView(BaseJsonView.class)
    public List<Message> getAllByPlaceId(@PathVariable Long placeId) {
        return messageService.getByPlace(placeId);
    }

    /**
     * 创建圈子消息
     * @param placeId
     * @param message
     * @return
     */
    @PutMapping("{placeId}")
    @JsonView(BaseJsonView.class)
    public Message createMessage(@PathVariable Long placeId, @RequestBody Message message) {
        return messageService.createMessage(placeId, message);
    }

    private interface BaseJsonView extends Message.SongJsonView {
    }
}
