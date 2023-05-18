package com.example.commercialdirector.myitschool.models;


public class Music {
    public int id_music;
    public String name_music;
    public int id_user;
    public String path;
    public int likei;

    public Music(int id_music, String name_music, int id_user, String path, int likei) {
        this.id_music = id_music;
        this.name_music = name_music;
        this.id_user = id_user;
        this.path = path;
        this.likei = likei;
    }

    public Music(int id_music,String path,String name_music, int likei) {
        this.id_music = id_music;
        this.name_music = name_music;
        this.path = path;
        this.likei = likei;
    }
    public Music(String name_music, int id_user, String path, int likei) {
        this.name_music = name_music;
        this.id_user = id_user;
        this.path = path;
        this.likei = likei;
    }

    public int getId_music() {
        return id_music;
    }

    public String getName_music() {
        return name_music;
    }

    public int getId_user() {
        return id_user;
    }

    public String getPath() {
        return path;
    }

    public int getLikei() { return likei; }

}
