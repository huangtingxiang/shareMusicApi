package com.jdxiang.shareMusicApi.service;

import com.jdxiang.shareMusicApi.entity.Message;
import com.jdxiang.shareMusicApi.entity.Place;
import com.jdxiang.shareMusicApi.entity.User;
import com.jdxiang.shareMusicApi.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    UserService userService;
    @Autowired
    HttpServletRequest requestHttpRequest;

    @Override
    public List<Message> getByPlace(Long placeId) {
        return messageRepository.findAllByPlaceIdOrderByIdDesc(placeId);
    }

    @Override
    public Message createMessage(Long placeId, Message message) {
        User user = userService.getCurrentUser(requestHttpRequest);

        Place place = new Place();
        place.setId(placeId);
        message.setPlace(place);
        message.setCreateTime(new Date());
        message.setUser(user);
        return messageRepository.save(message);
    }
}
