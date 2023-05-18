package com.example.commercialdirector.myitschool.models;


import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;


public class Media
{
    private String music;
    private MediaPlayer mPlayer;
    Context ctx;

    public Media(String music,Context ctx)
    {
        this.music = music;
        this.mPlayer = MediaPlayer.create(ctx, Uri.parse(music));
    }

    public void releaseMedia()
    {
        mPlayer.release();
    }
    public void stop()
    {
        mPlayer.stop();
//        try
//        {
//            mPlayer.prepare();
//            mPlayer.seekTo(0);
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//        }

    }
    public void play(View view)
    {
        mPlayer.start();
    }
    public void pause(View view)
    {
        mPlayer.pause();
    }
}
