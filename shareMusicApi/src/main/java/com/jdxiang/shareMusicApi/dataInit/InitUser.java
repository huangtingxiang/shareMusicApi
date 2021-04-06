package com.jdxiang.shareMusicApi.dataInit;

import com.jdxiang.shareMusicApi.entity.User;
import com.jdxiang.shareMusicApi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InitUser implements ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    UserRepository userRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        if (!userRepository.findAll().iterator().hasNext()) {
            List<User> users = new ArrayList<>();
            User user = new User();
            user.setUsername("admin");
            user.setPassword("123456");
            user.setNickName("admin");
            users.add(user);

            for (int i = 1; i <= 10; i++) {
                User user1 = new User();
                user1.setUsername("user" + i);
                user1.setPassword("123456");
                user1.setNickName("用户" + i);
                users.add(user1);
            }

            userRepository.saveAll(users);
        }
    }
}
