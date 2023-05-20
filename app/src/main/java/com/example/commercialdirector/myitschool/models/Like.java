package com.example.commercialdirector.myitschool.models;


public class Like {
    public int id_likei;
    public int id_music;
    public int id_user;

    public Like(int id_likei, int id_music, int id_user) {
        this.id_likei = id_likei;
        this.id_music = id_music;
        this.id_user = id_user;
    }

    public Like(int id_music, int id_user) {
        this.id_music = id_music;
        this.id_user = id_user;
    }

    public int getId_likei() {
        return id_likei;
    }

    public int getIdMusic() {
        return id_music;
    }

    public int getIdUser() {
        return id_user;
    }
}

