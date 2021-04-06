package com.jdxiang.shareMusicApi.service;

import com.jdxiang.shareMusicApi.entity.Place;
import com.jdxiang.shareMusicApi.entity.User;
import com.jdxiang.shareMusicApi.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class PlaceServiceImpl implements PlaceService {
    @Autowired
    CommonService commonService;
    @Autowired
    PlaceRepository placeRepository;
    @Autowired
    UserService userService;
    @Autowired
    HttpServletRequest request;

    @Override
    public Boolean isCreatePlace(User user) {
        return placeRepository.existsByBelongUserId(user.getId());
    }

    @Override
    public Place createPlace(Place place, HttpServletRequest request) {
        User user = userService.getCurrentUser(request);
        place.setBelongUser(user);
        return placeRepository.save(place);
    }

    @Override
    public void addUsersToPlace(Long id, List<User> users) {
        Place place = placeRepository.findById(id).get();
        for (User user : users) {
            place.getAllUser().add(user);
        }
        placeRepository.save(place);
    }

    @Override
    public String uploadImage(MultipartFile file) {
        String imageUrl = commonService.uploadImageByPath(file, CommonService.IMAGE_PATH + "place/");
        return imageUrl;
    }

    @Override
    public Place getById(Long id) {
        return placeRepository.findById(id).get();
    }

    @Override
    public Place getCurrentPlace() {
        User user = userService.getCurrentUser(request);
        return placeRepository.findByBelongUserId(user.getId());
    }

    @Override
    public List<Place> getByCurrentUser() {
        User user = userService.getCurrentUser(request);
        return placeRepository.findAllByAllUser(user);
    }
}
