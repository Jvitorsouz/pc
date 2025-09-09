import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ForkSleepJoin {

    public static void main(String[] args) {

        Random rand = new Random();
        int n = rand.nextInt(10);

        List<Thread> threads = new ArrayList<>();
        for(int i=0; i<n; i++){
            Sleep task = new Sleep(i+1, rand.nextInt(5));
            Thread thread = new Thread(task);
            threads.add(thread);
            thread.start();
        }

        for(Thread thread: threads){
            try {
                thread.join();
            } catch (InterruptedException ex) {
            }
        }
        
    }

    static class Sleep implements Runnable {

        private int i;
        private int random;

        public Sleep(int i, int random){
            this.i = i;
            this.random = random;
        }

        @Override
        public void run() {
            System.out.println("Thread " + this.i + " Dormindo por " + this.random + " Segundos");
            try {
                sleep(this.random);
            } catch (InterruptedException ex) {
            }
        }
    }
}