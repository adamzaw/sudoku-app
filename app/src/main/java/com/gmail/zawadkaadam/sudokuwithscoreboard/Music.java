package com.gmail.zawadkaadam.sudokuwithscoreboard;

import android.content.Context;
import android.media.MediaPlayer;

public class Music {

    private static MediaPlayer mp = null;

    public static void play(Context context, int res){
        stop(context);
        mp = MediaPlayer.create(context, res);
        mp.setLooping(true);
        mp.start();
    }

    public static void stop(Context context){
        if(mp != null){
            mp.stop();
            mp.release();
            mp = null;
        }
    }
}
