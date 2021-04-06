package com.jdxiang.shareMusicApi.repository;


import com.jdxiang.shareMusicApi.entity.User;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    User findAllByUsername(String userName);
}
