package com.example.doanlttbdd;

public class Story {
    private int id;
    private String title;
    private String author;
    private String description;

    private String content;

    public Story(String title, String author, String description, String content) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
        this.content = content;
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
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}