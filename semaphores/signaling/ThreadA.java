import static java.lang.Thread.sleep;
import java.util.concurrent.Semaphore;

public class ThreadA implements Runnable {

    private Semaphore semaforo;

    public ThreadA(Semaphore semaforo){
        this.semaforo = semaforo;
    }
    
    @Override
    public void run() {
        System.out.println("INICIO DA THEARD A");
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("FIM DA THEARD A");
        semaforo.release();
    } 
}
