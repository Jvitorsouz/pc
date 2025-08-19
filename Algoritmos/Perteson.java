public class Perteson {
    
    private volatile boolean[] flag = new boolean[2];
    private volatile int victim;


    public void lock(){
        int my_id = (int) Thread.currentThread().getId();
        int j = 1 - my_id;
        flag[my_id] = true;
        victim = my_id;

        while(flag[j] && victim == my_id);


    }

    public void unlock(){
        int my_id = (int) Thread.currentThread().getId();
        flag[my_id] = false;
    }
}
