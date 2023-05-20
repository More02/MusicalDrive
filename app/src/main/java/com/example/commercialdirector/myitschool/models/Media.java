package com.example.commercialdirector.myitschool.models;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

public class Media
{
    private final MediaPlayer mPlayer;

    public Media(String music,Context ctx)
    {
        this.mPlayer = MediaPlayer.create(ctx, Uri.parse(music));
    }

    public void releaseMedia()
    {
        mPlayer.release();
    }
    public void stop()
    {
        mPlayer.stop();
    }
    public void play()
    {
        mPlayer.start();
    }
    public void pause()
    {
        mPlayer.pause();
    }
}