package com.project.a_star_fitness.posts.model;

import java.io.Serializable;

public class Comment implements Serializable {
    private String content;
    private String id;
    private String author;
    private String time;

    public Comment(String content, String author, String time) {
        this.content = content;

        this.author = author;
        this.time = time;
    }

    public Comment() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
