import java.util.concurrent.Semaphore;

public class Barrier{

    private int n;
    private static final Semaphore mutex = new Semaphore(1);
    private static final Semaphore cart = new Semaphore(0);
    private int count;

    public Barrier(int n){
        this.n = n;
        this.count = 0;
    }

    public void waitBarrier(){

        try {
            mutex.acquire(); //wait
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        this.count++;
        //System.out.println("INICIO DA THREAD " + this.count);
        mutex.release();

        if(count == n){
            cart.release();
        }

        try{
            cart.acquire();
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
        //this.count--;
        cart.release();
        //System.out.println("FIM DA THREAD " + this.count);

    }
}