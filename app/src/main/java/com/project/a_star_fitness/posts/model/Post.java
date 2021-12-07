package com.project.a_star_fitness.posts.model;


import java.io.Serializable;

public class Post implements Serializable {
    private String id;
    private String title;
    private String content;
    private String picture;
    private String author;
    private String time;

    public Post(String title, String content, String picture, String author,String time) {
        this.title = title;
        this.content = content;
        this.picture = picture;
        this.author = author;
        this.time = time;
    }

    public Post() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}