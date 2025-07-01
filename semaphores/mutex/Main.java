
import java.util.concurrent.Semaphore;

public class Main {
    
    private static final Semaphore semaforo = new Semaphore(1);

    public static void main(String[] args) {
        
        Contador contador = new Contador();
        Thread t1 = new Thread(new ThreadFunction(semaforo, contador));
        Thread t2 = new Thread(new ThreadFunction(semaforo, contador));

        t1.start();
        t2.start();

        /*Necessario para ver a variavel final com o valor correto*/
         try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println(contador.getCount());
    }
}
