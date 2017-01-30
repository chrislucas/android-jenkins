package com.example.r028367.tutorialservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.WorkerThread;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ServiceWorkerThread extends Service {
    private static final int MAX = 200;
    private List<WorkerThread> threads;

    public class WorkerThread extends Thread {
        private boolean status;
        private int startId, count;

        public WorkerThread(int startId) {
            super("SERVICE" + startId);
            this.startId = startId;
            this.status = true;
            this.count = 0;
        }

        @Override
        public void run() {
            while(status && count < MAX) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    Log.e("EXCEPTION_THREAD", e.getMessage());
                }
                Log.i("THREAD_RUN", String.format("THREAD ID: %d Counter: %d", startId, count));
                count++;
            }
            Log.i("THREAD_STOP_SELF", String.format("THREAD ID: %d", startId));
            stopSelf(startId);
        }
    }

    public ServiceWorkerThread() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("ON_BIND", "WORKER_THREAD");
        return null;
    }

    @Override
    public void onCreate() {
        //super.onCreate();
        Log.i("ON_CREATE", "no ciclo de vida do Service esse metodo sera chamado 1 unica vez");
        threads = new ArrayList<>();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // toda vez que for chamado startService(intent) esse metodo sera executado
        Log.i("START_COMMAND", String.format("THREAD ID: %d", startId));
        WorkerThread workerThread = new WorkerThread(startId);
        threads.add(workerThread);
        workerThread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        for(WorkerThread thread : threads)
            thread.status = false;
        threads.clear();
        Log.i("ON_DESTROY_SERVICE", "Service onDestroy");
        super.onDestroy();
    }
}
