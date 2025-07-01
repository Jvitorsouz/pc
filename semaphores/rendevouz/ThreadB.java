import java.util.concurrent.Semaphore;

public class ThreadB  implements Runnable{

    private Semaphore semaforoA;
    private Semaphore semaforoB;

    public ThreadB(Semaphore semaforoA, Semaphore semaforoB){
        this.semaforoA = semaforoA;
        this.semaforoB = semaforoB;
    }

    /*B2 PRECISA ACONTECER DEPOIS DE A1 */
    @Override
    public void run(){

        System.out.println("INSTRUÇÃO B1");
        semaforoB.release();
        try {
            semaforoA.acquire(); //wait
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("INSTRUÇÃO B2");

    }

    
}
