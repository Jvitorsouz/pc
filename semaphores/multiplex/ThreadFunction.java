import java.util.concurrent.Semaphore;

public class ThreadFunction implements Runnable{

    private Semaphore multiplex;
    private Contador contador;

    public ThreadFunction(Semaphore multiplex, Contador contador){
        this.multiplex = multiplex;
        this.contador = contador;
    }

    @Override
    public void run() {

        try {
            multiplex.acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        for(int i = 0; i<1000; i++){
            this.contador.setCount();
        }
        multiplex.release();
        
        
    }
    
}
