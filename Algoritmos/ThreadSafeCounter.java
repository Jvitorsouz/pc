
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadSafeCounter {
    
    private AtomicInteger counter;

    public ThreadSafeCounter(){
        counter = new AtomicInteger(0);
    }

    public void increment(){
        counter.incrementAndGet();
    }

    public void decrement(){
        counter.decrementAndGet();
    }

    public void reset(){
        counter.set(0);
    }

    public int get(){
        return counter.get();
    }

    public boolean addIfPositive(int value){
        while (true) {
            int current = counter.get();
            if(current < 0) return false;
            int soma_l = current + value;

            if(counter.compareAndSet(current, soma_l)){
                return true;
            }

        }
    }
}