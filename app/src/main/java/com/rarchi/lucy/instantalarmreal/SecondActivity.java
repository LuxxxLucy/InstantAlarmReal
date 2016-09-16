package com.rarchi.lucy.instantalarmreal;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;


public class SecondActivity extends AppCompatActivity {
    MediaPlayer mp=null;
    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_second);

        b =(Button)findViewById(R.id.button2);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mp==null) {
                    try {
                        mp = MediaPlayer.create(getApplicationContext(), R.raw.song);
                        mp.start();
                    } catch (IllegalArgumentException e) {
                        Toast.makeText(getApplicationContext(), "ERROR!Media Service Failed", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
                else
                {
                    mp.stop();
                }
            }
        });
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if(mp!=null) {
            if (mp.isPlaying())
                mp.stop();
            mp.release();
        }
    }
}

