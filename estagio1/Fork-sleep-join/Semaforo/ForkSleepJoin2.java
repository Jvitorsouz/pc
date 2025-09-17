package Semaforo;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class ForkSleepJoin2 {

    public static void main(String[] args) throws InterruptedException {
        
        Random randon = new Random();
        int n = randon.nextInt(10) + 1;

        Semaphore semaforo = new Semaphore(0);

        for(int i=0; i<n; i++){
            Thread thread = new Thread(() -> {
                int sleepTime = randon.nextInt(5000);
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                System.out.println("Thread dormiu por " + sleepTime + " segundos");
                semaforo.release();
                
            });
            thread.start();
        }

        //É FEITO N VEZES PORQUE PRECISA QUE SEJA BLOQUEADA 
        for(int i=0; i<n; i++){
            semaforo.acquire(); //BLOQUEIA CASO NENHUMA THREAD AINDA EXECULTOU
        }
        System.out.println("Quantidades de threads que execultou: " + n);
        
    }
    
}
