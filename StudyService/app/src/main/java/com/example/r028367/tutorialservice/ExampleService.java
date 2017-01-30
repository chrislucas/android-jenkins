package com.example.r028367.tutorialservice;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;

/*
*
* Um compoenente que permite fazer operacoes longaas em segundo plano
* sem atrapalhar o desempenho da aplicacao.
*
* Um compoenente da aplicacao pode iniciar um serviço e qunado tal componente
* for destruido (sair da memoria) o seviço pode continuar, indefinidamente,
* se necessario.
*
* Um serviço possue 2 formas, essencialmente
*
* Started
* Quando um componente da aplicacao executar o metodo onStartCommand
* Um serviço iniciado geralmente executa uma opperacao e nao retorna nenhuma informacao.
*
*
* Binded
*
* Um servico eh vinculado quando um componente chama o metodo
* bindService. Um servico vinculado oferece uma inerface servidor-cliente
* onde os compoenentes a aplicacao podem solicitar algo do servico. O servico
* fica ativo enquanto houver componentes vinculados ao servico
*
* */

public class ExampleService extends Service implements Runnable {
    /*
    * Ponto importante do service: Um serviço se não configurado
    * será executado na Thread principal da aplicacao (UI Thread, pelo o que entendi).
    * Para não prejudicar o desempenho do app, eh melhor fazer com que a classe
    * Service implemente sua propria Thread, ou AsyncTask, como exemplo simples abaixo
    *
    *
    * */

    public interface Communicator {
        public void notify(int id);
    }

    private Communicator communicator;

    public static final String CATEGORY = "SERVICE_CREATE_EXAMPLE";
    public static final int MAX_EXEC = 30;
    private int count;
    private boolean status;

    public ExampleService() {
    }

    public ExampleService(Communicator communicator) {
        this.communicator = communicator;
    }

/*
Obsoleto
https://android-developers.googleblog.com/2010/02/service-api-changes-starting-with.html
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }
*/
    ArrayList<Integer> threadsId;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        count = 0;
        status = true;
        /*
        * para controlar as Threads que sao criadas (caso o service implemente sua propria Thread),
        * utilizamos o argumento int startId
        * */

        // comunicar quem iniciou esse Service da nvova THread
        if(communicator != null) {
            communicator.notify(startId);
        }

        String text = String.format("onStartCommand() Thread ID: %d", startId);
        (new Thread(this, text)).start();
        Log.i(CATEGORY, text);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        // throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }


    @Override
    public boolean bindService(Intent service, ServiceConnection conn, int flags) {
        return super.bindService(service, conn, flags);
    }
/*
*
* O sistema chama esse metodo sempre que um serviço eh criado pela primeira vez,
* antes de executar o metodo onStartCommand ou onBind, para que possamos fazer alguma
* configuracao inicial.
* Se o serviço estiver em execucao, esse metodo nao eh executado
* */

    /*
    * Sobre ciclo de vida:
    *
    * O metodo oncreate eh executado somente quando o Servico eh criado, independente
    * de quantas chamadas ao metodo startService sejam feitas. Se mais de uma chamada
    * for feita, o metodo que sera executado a partir da segunda vez eh o onStartCommand.
    *
    * Assim, se quiser que so uma Thread seja iniciada, ela deve ser criada no metodo onCreate.
    *
    * Se vc quiser um implementacao multithreading, pode inicializar as threads no metodo onStartCommand
    *
    * */
    @Override
    public void onCreate() {
        super.onCreate();
        threadsId = new ArrayList<>();
        Log.i(CATEGORY, "onCreate() Inicio Exemplo Service");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        status = false;
        Log.i(CATEGORY, "Service onDestroy");
    }

    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }

    @Override
    public void run() {
        while(status && count <= MAX_EXEC) {
            doSomething(1000);
            Log.i(CATEGORY, String.format("%s %d", String.valueOf(status), count));
            count++;
        }
        Log.i(CATEGORY, "Fim Exemplo Service");

        /*
        * O metodo stopSelf tem uma versao que recebe como argumento um int startID, que
        * eh o ID de um Servico que foi iniciado, assim podemos parar um serviço especifico
        * caso. Caso implememtemos uma classe que representa um serviço e ela seja multithreading
        * parar um sertico usando stopSelf(startId) eh algo bem util. Se o metodo stopSelf (sem argumento)
        * foi executado, todos os serviços serao encerrados, 1 a 1.
        *
        * Quando encerramos um servico usando stopSelf(startId), garantimos que numa implementacao
        * multithread, somente  a Thread vinculada a 'startId' sera encerrada.
        *
        * */
        // encerrar o ciclo de vida so Service
        stopSelf();
    }

    private void doSomething(int sleep) {
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException iexcp) {
            Log.e(CATEGORY, iexcp.getMessage());
        }
    }
}
