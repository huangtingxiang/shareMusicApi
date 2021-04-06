package com.jdxiang.shareMusicApi.repository;

import com.jdxiang.shareMusicApi.entity.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Long> {

    List<Message> findAllByPlaceIdOrderByIdDesc(Long id);
}
