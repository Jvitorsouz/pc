import java.util.concurrent.Semaphore;

class Producer implements Runnable {
    private final Buffer buffer;
    private final int maxItems;
    private final int sleepTime;
    private final int id;
    private final Semaphore mutex;
    private final Semaphore prodSemaphore;
    private final Semaphore consumeSemaphore;
    
    public Producer(int id, Buffer buffer, int maxItems, int sleepTime, Semaphore mutex, Semaphore prodSemaphore, Semaphore consumeSemaphore) {
        this.id = id;
        this.buffer = buffer;
        this.maxItems = maxItems;
        this.sleepTime = sleepTime;
        this.mutex = mutex;
        this.prodSemaphore = prodSemaphore;
        this.consumeSemaphore = consumeSemaphore;
    }
    
    public void produce() throws InterruptedException {
        for (int i = 0; i < maxItems; i++) {
            consumeSemaphore.acquire();
            mutex.acquire();
            try {
                Thread.sleep(sleepTime);
                int item = (int) (Math.random() * 100);
                System.out.println("Producer " + id + " produced item " + item);
                buffer.put(item);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            mutex.release();
            prodSemaphore.release();
        }
    }

    @Override
    public void run() {
        try {
            produce();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
