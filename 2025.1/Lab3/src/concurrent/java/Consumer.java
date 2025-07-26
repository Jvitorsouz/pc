import java.util.concurrent.Semaphore;

class Consumer implements Runnable {
    private final Buffer buffer;
    private final int sleepTime;
    private final int id;
    private final Semaphore mutex;
    private final Semaphore prodSemaphore;
    private final Semaphore consumeSemaphore;

    
    public Consumer(int id, Buffer buffer, int sleepTime, Semaphore mutex, Semaphore prodSemaphore, Semaphore consumeSemaphore) {
        this.id = id;
        this.buffer = buffer; this.mutex = mutex;
        this.prodSemaphore = prodSemaphore;
        this.consumeSemaphore = consumeSemaphore;
        this.sleepTime = sleepTime;

    }
    
    public void process() throws InterruptedException {
        while (true) {
            prodSemaphore.acquire();
            mutex.acquire();
            int item = buffer.remove();
            if (item == -1) break;
            System.out.println("Consumer " + id + " consumed item " + item);
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            mutex.release();
            consumeSemaphore.release();
        }
    }

    @Override
    public void run() {
        try {
            process();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}