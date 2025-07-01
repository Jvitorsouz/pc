public class ThreadFunction implements Runnable{

    private Barrier barrier;

    public ThreadFunction(Barrier barrier){
        this.barrier = barrier;
    }

    @Override
    public void run() {
        barrier.waitBarrier();
    }
    
}
