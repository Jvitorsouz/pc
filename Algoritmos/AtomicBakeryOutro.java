import java.util.concurrent.atomic.AtomicInteger;

public class AtomicBakeryOutro{

    private AtomicInteger count;
    private int n;
    private int[] tickets;

    public AtomicBakeryOutro (int n){
        this.count = new AtomicInteger(0);
        this.tickets = new int[n];
        this.n = n;
    }

    public void lock(){
        int id = (int) Thread.currentThread().getId();
        this.tickets[id] = this.count.incrementAndGet();

        for(int j=0; j<this.n; j++){
            while(this.tickets[j] != 0 && this.tickets[j] < this.tickets[id]);
        }
    }

    public void unlock(){
        int id = (int) Thread.currentThread().getId();
        this.tickets[id] = 0;
    }
}