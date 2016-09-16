package com.rarchi.lucy.instantalarmreal;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.util.Calendar;

import az.plainpie.PieView;

public class FinishiActivity extends AppCompatActivity {

    private Button finishi_button;
    private Vibrator vib;
    private boolean run;
    private int duration;
    private int left;
    private SoundPool soundPool;
    private Calendar now,end;
    private PieView pieView;
    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        if(left==0)
        {
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.cancelAll();
            if(vib!=null)
                vib.cancel();
            if(soundPool!=null)
                soundPool.stop(1);

            Intent stop_music = new Intent(FinishiActivity.this, PlayMusic.class);
            getApplicationContext().stopService(stop_music);
        }
        else
        {

            Toast.makeText(getApplicationContext(),"alarm is not canceled!",Toast.LENGTH_SHORT).show();
        }



    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finishi);


        Intent intent= getIntent();
        duration = intent.getIntExtra("duration",15)*60;
        left=duration;


        end = Calendar.getInstance();
        end.add(Calendar.SECOND,duration);

        run = true;



        finishi_button = (Button) findViewById(R.id.finishbutton);
        finishi_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(run==true)
                    warn_dialog();
                else
                {
                    NotificationManager mNotificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.cancelAll();
                    vib.cancel();
                    soundPool.stop(1);
                    //mp.stop();
                    finish();
                }
            }
        });



        pieView = (PieView) findViewById(R.id.pieView);

//      Change the color fill of the bar representing the current percentage
        pieView.setPercentageBackgroundColor(getResources().getColor(R.color.colorPrimary));
//      Change the color fill of the center of the widget
        //pieView.setCenterBackgroundColor(R.color.colorLight);
      pieView.setInnerBackgroundColor(getResources().getColor(R.color.colorAccent));

        pieView.setPieInnerPadding(40);
        pieView.setPadding(120,120,100,100);

        // Update the visibility of the widget text
        pieView.setInnerTextVisibility(View.VISIBLE);

        //Change the text size of the widget
        pieView.setPercentageTextSize(35);


        pieView.setInnerText(left/60+"\r\n minutes\nleft");





        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "dev info", Snackbar.LENGTH_LONG)
                        .setAction("See", new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                Intent start_second = new Intent(getApplicationContext(),SecondActivity.class);
                                startActivity(start_second);

                            }
                        }).show();


            }
        });


        handler.postDelayed(task, 1000);
    }

    private final Handler handler = new Handler();



    private final Runnable task = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            if (run) {
                handler.postDelayed(this, 1000);

                now=Calendar.getInstance();



                left=end.get(Calendar.SECOND)+end.get(Calendar.MINUTE)*60+end.get(Calendar.HOUR_OF_DAY)*60*60
                        -(now.get(Calendar.SECOND)+now.get(Calendar.MINUTE)*60+now.get(Calendar.HOUR_OF_DAY)*60*60);
            }

            if(left<=0)
            {
                run=false;
                left=0;

                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.cancel(1);

                Intent stopintent = new Intent(getApplicationContext(),AlarmService.class);
                stopService(stopintent);

                Toast.makeText(getApplicationContext(),"Time Is Over!",Toast.LENGTH_SHORT).show();


                vib = (Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
                vib.vibrate(new long[]{300,100,300,100},0);

                AudioManager mgr = (AudioManager)
                        getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                // 获取系统声音的当前音量
                float currentVolume = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
                // 获取系统声音的最大音量
                float maxVolume = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                // 获取当前音量的百分比
                float volume = currentVolume / maxVolume;

                soundPool= new SoundPool(10, AudioManager.STREAM_SYSTEM,5);
                soundPool.load(getApplicationContext(),R.raw.alarm,1);
                soundPool.play(1,volume, volume, 1,1, 1f);

                pieView.setmPercentage(0);
                pieView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                pieView.setInnerText("KEEP\nCALM\nAND\nGET\nUP!");


            }
            else
            {
                float display = (float) (100 * (left* 1.0) / duration);

                pieView.setmPercentage( display);
                pieView.setInnerText((left/60) + "\r\n minutes\nleft");
            }


        }
    };

    protected void warn_dialog()
    {

        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("are you sure to cancel the alarm?");
        builder.setPositiveButton("back", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog,int which)
            {

                dialog.dismiss();


            }

        });
        builder.setNegativeButton("yes cancel this alarm", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"You've canceled the alarm",Toast.LENGTH_LONG).show();


                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.cancel(1);

                run=false;
                if(left!=0) {
                    Intent stopintent = new Intent(getApplicationContext(), AlarmService.class);
                    stopService(stopintent);

                }
                Intent intent = new Intent(getApplicationContext(),AlarmReceiver.class);
                PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
                AlarmManager am = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
                am.cancel(pi);

                finish();

                dialog.dismiss();
            }
        });
        builder.create().show();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}




