package com.example.r028367.tutorialservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.webkit.HttpAuthHandler;

import com.github.kevinsawicki.http.HttpRequest;


/*
*
* Fazer um teste, implementar um servico que faz um request HTTP
* */

public class ServiceHttpRq extends Service implements Runnable{
    public ServiceHttpRq() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    /*
    * Como ja visto, eh uma boa pratica que um serviço inicie sua propria Thread.
    * Segundo a documentacao do ciclo de vida de um Service, o metodo onCreate eh
    * executado 1 unica vez, independentemente de quantas vezes o metodo startService(inntent)
    * for executado.
    *
    * Essa informacao no permite decidir se o nosso serviço deve ter uma unica Thread ou podemos
    * executar multplias Threads, caso um determinado serviço se iniciado varias vezes.
    *
    * Se quisermos manter uma unica Thread, instanciaremos-a no metodo onCreate(), se nossa
    * solucao exigir multiplas Threads, temos o metodo onStartCommand que eh executado a cada vez
    * que o metodo startService o eh tambem
    * */
    @Override
    public void onCreate() {
        Thread thread = new Thread(this); //
        thread.start();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void run() {
        try {
            // exemplo besta por enquanto
            HttpRequest rq = HttpRequest.get("https://github.com/kevinsawicki/http-request");
            String  body = rq.body();
        } catch (Exception e) {
            Log.e("HTTP_EXCEPTION",e.getMessage());
        }
        stopSelf();
    }
}
