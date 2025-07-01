
import java.util.concurrent.Semaphore;

public class Main {
    
    private static final Semaphore semaforo = new Semaphore(0);

    public static void main(String[] args) {

        Thread t1 = new Thread(new ThreadA(semaforo));
        Thread t2 = new Thread(new ThreadB(semaforo));

        t2.start();
        t1.start();

    }
}
