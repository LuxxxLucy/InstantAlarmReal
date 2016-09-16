package com.rarchi.lucy.instantalarmreal;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

public class AlarmService extends Service {

    private int target;
    public AlarmService() {

    }


    @Override
    public IBinder onBind(Intent intent) {
        // igonred this for now
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");

        return null;
    }


    @Override
    public int onStartCommand(Intent intent,int flags,int startId)
    {
        super.onStartCommand(intent,flags,startId);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.v("alarm","start at "+ new Date().toString());
            }
        }).start();

        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        int anMinute = 60 *1000;

        target = intent.getIntExtra("duration",100);
        long triggerTime= SystemClock.elapsedRealtime() + target*anMinute;

        Intent alarm = new Intent(getApplicationContext(),AlarmReceiver.class);

        PendingIntent pend = PendingIntent.getBroadcast(getApplicationContext(),0,alarm,0);
        manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerTime,pend);

        return super.onStartCommand(intent,flags,startId);

    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        Resources res = getResources();
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.little)
                        .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.little))
                        .setContentTitle("Sleeppp")
                        .setContentText("You shall be having rest right now!");

        Intent intent = new Intent(this,FinishiActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,PendingIntent.FLAG_IMMUTABLE);
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // mId allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());

        startForeground(1,mBuilder.build());

        Toast.makeText(this,"alarm service created!",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        stopForeground(true);
    }
}

