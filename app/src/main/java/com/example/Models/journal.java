package com.example.Models;

public class journal {
    private int id,likes,comments;
    private String date,about,photo,tag,feelings;
    private User user;
    private boolean selfLike;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getFeelings() {
        return feelings;
    }

    public void setFeelings(String feelings) {
        this.feelings = feelings;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isSelfLike() {
        return selfLike;
    }

    public void setSelfLike(boolean selfLike) {
        this.selfLike = selfLike;
    }
}