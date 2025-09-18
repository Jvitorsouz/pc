import java.util.Arrays;
import java.util.Random;

public class TwoPhaseSleep {

    public static void main(String[] args) throws InterruptedException{
        

        Random random = new Random();
        int n = random.nextInt(10) + 1;
        Thread[] threads = new Thread[n];
        //Barreira para auxiliar
        Barrier barrier = new Barrier(n);
        Integer[] sorteios = new Integer[n];

        for(int i=0; i<n; i++){
            final int threadIndex = i;
            Thread t = new Thread(() -> {
                //FASE 1
                try {
                    int sleepTime = random.nextInt(5000);
                    Thread.sleep(sleepTime);
                    System.out.println("Thread " + threadIndex + " dormiu por " + sleepTime + " segundos");
                    
                    //Iniciando sorteio
                    int numSorteio = sorteio(random);
                    sorteios[threadIndex] = numSorteio;
                    System.out.println("Numero sorteado: " + numSorteio);
                    //ESPERAR ATÉ QUE TODAS TENHA SORTEADO 
                    barrier.waitBarrier();

                     //FASE 2
                     if(threadIndex == 0){
                        Thread.sleep(sorteios[(n - 1)] * 1000);
                         System.out.println("Thread " + threadIndex + " dormiu por " + sorteios[(n - 1)] + " segundos");
                     } else{
                        Thread.sleep(sorteios[(threadIndex - 1)] * 1000);
                        System.out.println("Thread " + threadIndex + " dormiu por " + sorteios[(threadIndex - 1)] + " segundos");
                     }
                    
                } catch (InterruptedException ex) {
                }
                
            });
            threads[threadIndex] = t;
            t.start();
        }

        //Encerrar todas as threads
        for(Thread thread: threads){
            thread.join();
        }

        System.out.println(Arrays.toString(sorteios));

    }

    static int sorteio(Random random){
        return random.nextInt(10);
    }

}