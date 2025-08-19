public class LockTwo {

    private volatile int victim;

    public void lock(){
        int my_id = (int) Thread.currentThread().getId();
        victim = my_id;

        while(victim == my_id);

    }

    public void unlock(){}
    
}
