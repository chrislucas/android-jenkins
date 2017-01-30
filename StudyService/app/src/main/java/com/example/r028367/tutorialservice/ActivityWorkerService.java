package com.example.r028367.tutorialservice;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class ActivityWorkerService extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_service);
        intent = new Intent(this, ServiceWorkerThread.class);
    }


    public void startNewService(View view) {
        startService(intent);
        ComponentName componentName = startService(intent);
        if(componentName != null)
            Log.i("LOG", String.format("%s", componentName.toString()));
        Log.i("LOG", String.format("%d", System.currentTimeMillis()));
    }

    public void stopAllServices(View view) {
        stopService(intent);
    }
}
