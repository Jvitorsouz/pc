
import java.util.concurrent.Semaphore;

public class Main {
    
    private static final Semaphore semaforoA = new Semaphore(0);
    private static final Semaphore semaforoB = new Semaphore(0);

    public static void main(String[] args) {
        
        Thread t1 = new Thread(new ThreadA(semaforoA, semaforoB));
        Thread t2 = new Thread(new ThreadB(semaforoA, semaforoB));

        t2.start();
        t1.start();

    }
}
