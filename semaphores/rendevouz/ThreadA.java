import java.util.concurrent.Semaphore;

public class ThreadA implements Runnable{

    
    private Semaphore semaforoA;
    private Semaphore semaforoB;

    public ThreadA(Semaphore semaforoA, Semaphore semaforoB){
        this.semaforoA = semaforoA;
        this.semaforoB = semaforoB;
    }
    /*A2 PRECISA ACONTECER DEPOIS DE B1 */

    @Override
    public void run(){

        System.out.println("INSTRUÇÃO A1");
        semaforoA.release(); //signal
        try {
            semaforoB.acquire(); //wait
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("INSTRUÇÃO A2");

    }
}