package com.example.commercialdirector.myitschool.models;


import com.google.gson.annotations.SerializedName;

public class Result_Music {
    @SerializedName("error")
    private boolean error;

    @SerializedName("message")
    private String message;

    @SerializedName("music")
    private Music music;

    public Result_Music(boolean error, String message, Music music) {
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
