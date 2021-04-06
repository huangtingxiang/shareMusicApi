package com.example.sharemusicplayer.entity;

import java.util.ArrayList;
import java.util.List;

public class Place {

    private Long id;

    User belongUser;    // 所属用户

    List<User> allUser = new ArrayList<>(); // 所有用户

    private String name; // 圈子名

    private String desName; // 描述名

    private String label; // 标签

    private String picUrl; // 封面

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getBelongUser() {
        return belongUser;
    }

    public void setBelongUser(User belongUser) {
        this.belongUser = belongUser;
    }

    public List<User> getAllUser() {
        return allUser;
    }

    public void setAllUser(List<User> allUser) {
        this.allUser = allUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesName() {
        return desName;
    }

    public void setDesName(String desName) {
        this.desName = desName;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public interface BelongsUserJsonView {
    }

    public interface AllUserJsonView {
    }
}

