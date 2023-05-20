package com.shehzad.careerplacer.admin.model;

public class ResourceModel {
    String title, description, url, pdf;

    public ResourceModel() {
    }

    public ResourceModel(String title, String description, String url, String pdf) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.pdf = pdf;
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

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }
}
