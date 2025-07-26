import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        if (args.length != 5) {
            System.out.println("Use: java Main <num_producers> <max_items_per_producer> <producing_time> <num_consumers> <consuming_time>");
            return;
        }
        
        int numProducers = Integer.parseInt(args[0]);
        int maxItemsPerProducer = Integer.parseInt(args[1]);
        int producingTime = Integer.parseInt(args[2]);
        int numConsumers = Integer.parseInt(args[3]);
        int consumingTime = Integer.parseInt(args[4]);
        
        Buffer buffer = new Buffer();

        Semaphore mutex = new Semaphore(1);
        Semaphore prodSemaphore = new Semaphore(0);
        Semaphore consumeSemaphore = new Semaphore(100); //Quantidade total de itens no buffer

        List<Thread> producers = new ArrayList<>();
        List<Thread> consumers = new ArrayList<>();
        
        for (int i = 1; i <= numProducers; i++) {
            Producer producer = new Producer(i, buffer, maxItemsPerProducer, producingTime, mutex, prodSemaphore, consumeSemaphore);
            Thread thread = new Thread(producer);
            producers.add(thread);
            thread.start();
        }
        
        for (int i = 1; i <= numConsumers; i++) {
            Consumer consumer = new Consumer(i, buffer, consumingTime, mutex, prodSemaphore, consumeSemaphore);
            Thread thread = new Thread(consumer);
            consumers.add(thread);
            thread.start();
        }

        for (Thread thread : producers) {
            thread.join();
        }

        for (Thread thread : consumers) {
            thread.join();
        }
    }
}
