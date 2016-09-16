package com.rarchi.lucy.instantalarmreal;


import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;


public class PlayMusic extends Service {


    private MediaPlayer mp;

    public PlayMusic() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");

    }


    @Override
    public int onStartCommand(Intent intent,int flags,int startId)
    {
        super.onStartCommand(intent,flags,startId);

        try {
            mp = MediaPlayer.create(getApplicationContext(),R.raw.alarm);
            mp.setLooping(true);
            mp.start();
        } catch (IllegalArgumentException e) {
            Toast.makeText(getApplicationContext(),"error1!",Toast.LENGTH_SHORT).show();

            e.printStackTrace();
        }
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });

        return super.onStartCommand(intent,flags,startId);


    }

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mp.stop();
        mp.release();
    }
}
