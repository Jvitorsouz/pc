
import static java.lang.Thread.sleep;
import java.util.concurrent.Semaphore;

public class ThreadB implements Runnable {

    private Semaphore semaforo;

    public ThreadB(Semaphore semaforo){
        this.semaforo = semaforo;
    }

    @Override
    public void run() {
        try {
            semaforo.acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("INICIO DA THEARD B");
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("FIM DA THEARD B");
    }
    
}
