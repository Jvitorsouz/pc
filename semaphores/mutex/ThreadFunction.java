
import java.util.concurrent.Semaphore;

public class ThreadFunction implements Runnable {

    private Semaphore semaforo;
    private Contador contador;

    public ThreadFunction(Semaphore semaforo, Contador contador){
        this.semaforo = semaforo;
        this.contador = contador;
    }

    @Override
    public void run(){
        for(int i = 0; i<1000; i++){ 
            try {
                semaforo.acquire();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            this.contador.setCount();
            semaforo.release();
            //this.contador.setCount();
        }
    }
    
}
