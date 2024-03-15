package com.example.doanlttbdd;

public class story {
    private int id;
    private String title;
    private String author;
    private String description;



    public story(String title, String author, String description) {
        // Constructor mặc định
    }

    public story(int id, String title, String author, String description, String image) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}