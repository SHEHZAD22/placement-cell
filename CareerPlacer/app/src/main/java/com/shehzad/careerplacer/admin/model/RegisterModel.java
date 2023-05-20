package com.shehzad.careerplacer.admin.model;

public class RegisterModel {
    String key;
    String name;
    String image;

    public RegisterModel() {
    }

    public RegisterModel(String key,String name, String image) {
        this.key = key;
        this.name = name;
        this.image = image;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
