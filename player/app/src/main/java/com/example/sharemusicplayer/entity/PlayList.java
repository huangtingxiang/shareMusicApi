package com.example.sharemusicplayer.entity;

public class PlayList {
    private Long id;
    private String name;
    private String des_name;
    private Long play_list_id;
    private String pic_url;


    private User user;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes_name() {
        return des_name;
    }

    public void setDes_name(String des_name) {
        this.des_name = des_name;
    }

    public Long getPlay_list_id() {
        return play_list_id;
    }

    public void setPlay_list_id(Long play_list_id) {
        this.play_list_id = play_list_id;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}