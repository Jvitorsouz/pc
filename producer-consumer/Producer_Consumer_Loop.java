import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Producer_Consumer_Loop {

    static final Semaphore mutex = new Semaphore(1);
    static final Semaphore prodSem = new Semaphore(0);
    static final Semaphore conSem = new Semaphore(10); //O semaforo é iniciado com a quantidade limite de objetos no buffer
    public static void main(String[] args) {

        List<Integer> buffer = new ArrayList<>();
        Random random = new Random();


        Thread producer = new Thread(new Producer(buffer, random));
        Thread consumer = new Thread(new Consumer(buffer));

        producer.start();
        consumer.start();
    }

    public static class Producer implements Runnable {

        private List<Integer> buffer;
        private Random random;

        public Producer(List<Integer> buffer, Random random){
            this.buffer = buffer;
            this.random = random;
        }

        public void add () throws InterruptedException {
            while (true){
                int item = random.nextInt(100);

                conSem.acquire(); // Caso o buffer esteja cheio
                mutex.acquire();
                buffer.add(item);
                System.out.println( "Produzido: " + item);
                mutex.release();
                prodSem.release();

                Thread.sleep(500);

            }


        }

        @Override
        public void run() {
            try {
                add();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class Consumer implements Runnable{

        private List<Integer> buffer;

        public Consumer(List<Integer> buffer){
            this.buffer = buffer;
        }

        public void get () throws InterruptedException {

            while (true){
                prodSem.acquire(); //Caso o buffer esteja vazio
                mutex.acquire();
                int data = buffer.remove(0);
                System.out.println( "Consumido: " + data);
                mutex.release();
                conSem.release();
                Thread.sleep(800);

            }

        }


        @Override
        public void run() {
            try {
                get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
