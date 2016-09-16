package com.rarchi.lucy.instantalarmreal;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by lucy on 5/6/16.
 */
public class AlarmReceiver extends BroadcastReceiver {
    private MediaPlayer mp ;

    @Override
    public  void   onReceive(Context context, Intent intent)
    {
        Log.v("alarm","end at " + new Date());
        Toast.makeText(context,"message receieved : now sleep time is over",Toast.LENGTH_SHORT).show();


        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(1);

        Resources res = context.getResources();
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.little)
                        .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.little))
                        .setDefaults(Notification.DEFAULT_SOUND)//设置声音，此为默认声音
                        .setVibrate(new long[]{300,100,300,100})
                        .setContentTitle("Time is over!")
                        .setContentText("Ba-ba-shaka-laka");

        Intent intent_ok = new Intent(context,FinishiActivity.class);
        intent_ok.putExtra("duration",0);

        TaskStackBuilder stackBuilder= TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent_ok);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        mNotificationManager.notify(2, mBuilder.build());

        Toast.makeText(context,"now that time is over!",Toast.LENGTH_SHORT).show();

        Intent start_music= new Intent(context,PlayMusic.class);
        context.startService(start_music);


    }


}

