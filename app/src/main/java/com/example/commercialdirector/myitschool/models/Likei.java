package com.example.commercialdirector.myitschool.models;


public class Likei
{
    public int id_likei;
    public int id_music;
    public int id_user;

    public Likei(int id_likei, int id_music, int id_user) {
        this.id_likei = id_likei;
        this.id_music = id_music;
        this.id_user = id_user;
    }

    public Likei(int id_music, int id_user) {
        this.id_music = id_music;
        this.id_user = id_user;
    }



    public int getId_likei() { return id_likei; }

    public int getId_music() { return id_music; }

    public int getId_user() { return id_user; }
}
