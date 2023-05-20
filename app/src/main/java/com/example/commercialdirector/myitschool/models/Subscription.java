package com.example.commercialdirector.myitschool.models;

public class Subscription {
    public int id_podpiska;
    public int id_user1;
    public int id_user2;

    public Subscription(int id_user1, int id_user2) {
        this.id_user1 = id_user1;
        this.id_user2 = id_user2;
    }

    public int getId_podpiska() {
        return id_podpiska;
    }

    public int getId_user1() {
        return id_user1;
    }

    public int getId_user2() {
        return id_user2;
    }
}
