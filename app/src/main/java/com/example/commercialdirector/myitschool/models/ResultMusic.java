package com.example.commercialdirector.myitschool.models;


import com.google.gson.annotations.SerializedName;

public class ResultMusic {
    @SerializedName("error")
    private final boolean error;

    @SerializedName("message")
    private final String message;

    @SerializedName("music")
    private final Music music;

    public ResultMusic(boolean error, String message, Music music) {
        this.error = error;
        this.message = message;
        this.music = music;
    }

    public boolean getError() {
        return error;
    }
    public String getMessage() {
        return message;
    }
    public Music getMusic() {
        return music;
    }
}
