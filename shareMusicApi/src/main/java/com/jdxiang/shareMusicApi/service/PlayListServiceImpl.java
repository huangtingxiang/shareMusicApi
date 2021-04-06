package com.jdxiang.shareMusicApi.service;

import com.jdxiang.shareMusicApi.entity.PlayList;
import com.jdxiang.shareMusicApi.entity.User;
import com.jdxiang.shareMusicApi.repository.PlayListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayListServiceImpl implements PlayListService {

    @Autowired
    PlayListRepository playListRepository;
    @Autowired
    UserService userService;

    @Override
    public PlayList createPlayList(PlayList playList) {
        playList.setUser(userService.getCurrentUser());
        return playListRepository.save(playList);
    }

    @Override
    public List<PlayList> getPlayLists() {
        User user = userService.getCurrentUser();
        return playListRepository.findAllByUserIdOrderByIdDesc(user.getId());
    }
}
