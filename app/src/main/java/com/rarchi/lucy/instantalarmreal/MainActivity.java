package com.rarchi.lucy.instantalarmreal;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.Gravity;
import android.view.View;

import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {
    private SeekBar bar;

    private Button mButton;

    private TextView change_text;
    private TextView endtime_text;

    private TextView hint_text;

    private TextView current_hint;
    private Calendar calendar;
    private int target;
    private TextClock clock;
    private Typeface font;
    private Typeface font_italic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
        font_italic = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Italic.ttf");

        change_text = (TextView) findViewById(R.id.textView3);

        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog();
            }
        });


        bar = (SeekBar) findViewById(R.id.Bar);
        bar.setProgress(10);
        target = convetBarToTime(bar.getProgress());

        endtime_text = (TextView) findViewById(R.id.end_time);
        hint_text = (TextView) findViewById(R.id.textView5);
        current_hint = (TextView) findViewById(R.id.textView2);
        clock = (TextClock) findViewById(R.id.textClock);
        clock.setTypeface(font);
        current_hint.setTypeface(font);
        endtime_text.setTypeface(font);
        hint_text.setTypeface(font);
        change_text.setTypeface(font_italic);

        change_text.setText("seek bar to find value\nDefault offset is 15 minutes");
        change_text.setGravity(Gravity.CENTER_HORIZONTAL);


        calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, target);
        if (calendar.get(Calendar.AM) == 1) {
            endtime_text.setText(calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + " AM");
        } else {
            endtime_text.setText(calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + " PM");
        }

        hint_text.setGravity(Gravity.CENTER_HORIZONTAL);

        if (target <= 60)
            hint_text.setText("After " + target + " minutes\nIt would be ");
        else
            hint_text.setText("After " + target / 60 + " hour and  " + target % 60 + " minutes\nIt would be ");

        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                target = convetBarToTime(progress);

                calendar = Calendar.getInstance();
                calendar.add(Calendar.MINUTE, target);
                if (calendar.get(Calendar.AM_PM) == 0)
                    endtime_text.setText(calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + " AM");
                else
                    endtime_text.setText(calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + " PM");

                if (target <= 60) {
                    hint_text.setText("After " + target + " minutes\nIt would be ");
                    change_text.setText("seek bar to find value\n"+target + " minute ");
                } else {
                    hint_text.setText("After " + target / 60 + " hour and  " + target % 60 + " minutes\nIt would be ");
                    change_text.setText("seek bar to find value\n"+target / 60 + " hour and  " + target % 60 + " minutes");
                }
                handler.postDelayed(task, 1000);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });




    }


    protected void dialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, target);
        builder.setTitle("ARE YOU SURE to set up this alarm?\nEnd time would be " + calendar.get(Calendar.HOUR) + " : " + calendar.get(Calendar.MINUTE));
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                Intent start = new Intent(MainActivity.this, AlarmService.class);
                start.putExtra("duration", target);
                getApplicationContext().startService(start);

                Intent intent = new Intent(MainActivity.this, FinishiActivity.class);
                intent.putExtra("duration", target);
                startActivity(intent);

                dialog.dismiss();


            }

        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "You've canceled the alarm", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    private int convetBarToTime(int barValue) {
        //primary thinking 100==>3 hour,10=>15min ,20 =>30min,30=>45min

        int target = barValue * 15 / 10;
        return target;
    }

    private final Handler handler = new Handler();

    private final Runnable task = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            handler.postDelayed(this, 1000);
            calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, target);
            if (calendar.get(Calendar.AM_PM) == 0)
                endtime_text.setText(calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + " AM");
            else
                endtime_text.setText(calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + " PM");

            if (target <= 60)
                hint_text.setText("After " + target + " minutes\nIt would be ");
            else
                hint_text.setText("After " + target / 60 + " hour and  " + target % 60 + " minutes\nIt would be ");


        }


    };

}








