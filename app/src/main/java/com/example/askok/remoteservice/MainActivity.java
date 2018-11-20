package com.example.askok.remoteservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button bcolor, bmessage;

    Messenger myService = null;
    boolean isBound;

    String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(getApplicationContext(),
                Remote.class);

        bcolor = findViewById(R.id.btncolor);
        bmessage = findViewById(R.id.btnmessage);

        bindService(intent, myConnection, Context.BIND_AUTO_CREATE);

        bmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    public void run(){
                        sendMessage();
                    }
                }, 5000);
            }
        });

        bcolor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setActivityBackgroundColor();
            }
        });
    }

    private ServiceConnection myConnection =
            new ServiceConnection() {
                public void onServiceConnected(ComponentName className,
                                               IBinder service) {
                    myService = new Messenger(service);
                    isBound = true;
                }

                public void onServiceDisconnected(ComponentName className) {
                    myService = null;
                    isBound = false;
                }
            };

        public void sendMessage()
        {
            if (!isBound) return;

            Message msg = Message.obtain();

            Bundle bundle = new Bundle();
            bundle.putString("MyString", date);

            msg.setData(bundle);

            try {
                myService.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

    public int getRandomColor(){
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    public void setActivityBackgroundColor() {
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(getRandomColor());
    }



}
