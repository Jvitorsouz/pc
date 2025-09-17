package JOIN;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ForkSleepJoin1 {

    //VERSÃO USANDO JOIN
    public static void main(String[] args) throws InterruptedException {


        //GERANDO UM NUMERO ALEATORIO PARA SER N
        Random random = new Random();
        int n = random.nextInt(10) + 1;

        //Lista de threads para auxiliar no join
        List<Thread> threads = new ArrayList<>();

        for(int i=0; i<n; i++){
            //USANDO LAMBDA
            Thread t = new Thread(() -> {
                int sleepTime = random.nextInt(5000) + 1;
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                System.out.println("Thread dormiu por " + sleepTime / 1000 + " segundos");
            });
            threads.add(t);
            t.start();
        }

        //USO DE JOIN
        for (Thread thread: threads){
            thread.join();
        }

        System.out.println("Quantidades de threads que execultou: " + n);
        
    }

    
}
