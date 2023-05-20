package com.shehzad.careerplacer.admin.model;

public class EventModel {
    String title;
    String description;
    String url;
    String image;
    String uploadDate;
    String time;
    String key;

    public EventModel() {
    }

    public EventModel(String title, String description, String url, String image, String uploadDate, String time, String key) {

        this.title = title;
        this.description = description;
        this.url = url;
        this.image = image;
        this.uploadDate = uploadDate;
        this.time = time;
        this.key = key;
    }

    public EventModel(String title, String downloadImgUrl, String uploadDate, String time, String key) {
        this.title = title;
        this.image = downloadImgUrl;
        this.uploadDate = uploadDate;
        this.time = time;
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return uploadDate;
    }

    public void setDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
