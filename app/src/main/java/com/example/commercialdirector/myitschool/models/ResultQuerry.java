package com.example.commercialdirector.myitschool.models;

import com.google.gson.annotations.SerializedName;

public class ResultQuerry {
    @SerializedName("error")
    private boolean error;

    @SerializedName("message")
    private String message;

    public ResultQuerry(boolean error, String message) {
        this.error = error;
        this.message = message;

    }

    public void setError(boolean error) { this.error = error; }

    public void setMessage(String message) { this.message = message; }

    public boolean isError() { return error; }

    public String getMessage() { return message; }

}
