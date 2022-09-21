package com.example.commercialdirector.myitschool.models;


public class New {
    public int id_user;
    public String name;
    public String name_music;
    public String path;
    public String date_music;

    public New(int id_user, String name, String name_music, String path, String date_music) {
        this.id_user = id_user;
        this.name = name;
        this.name_music = name_music;
        this.path = path;
        this.date_music = date_music;
    }

    public int getId_user() {
        return id_user;
    }

    public String getName() {
        return name;
    }

    public String getName_music() {
        return name_music;
    }

    public String getPath() {
        return path;
    }

    public String getDate_music() {
        return date_music;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setName_music(String name_music) {
        this.name_music = name_music;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setDate_music(String date_music) {
        this.date_music = date_music;
    }
}
