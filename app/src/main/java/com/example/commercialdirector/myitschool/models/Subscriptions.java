package com.example.commercialdirector.myitschool.models;


import java.util.ArrayList;

public class Subscriptions {
    private ArrayList<Subscription> podpiskas;

    public Subscriptions() {

    }

    public ArrayList<Subscription> getPodpiskas() {
        return podpiskas;
    }

    public void setPodpiskas(ArrayList<Subscription> podpiskas) {
        this.podpiskas = podpiskas;
    }
}
