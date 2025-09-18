import java.util.Random;
import java.util.concurrent.Semaphore;

public class BlockingRateLimiting{

    static final Semaphore[] type = {new Semaphore(0), new Semaphore(0)};
    static int currentType = 0;
    static int currentCount = 0;

    public static void main(String[] args) throws InterruptedException {

        Semaphore multiplex = new Semaphore(3);
        Semaphore mutex = new Semaphore(1);

        Thread[] threads = new Thread[10];

        for(int i=0; i<10; i++){
            Thread t = new Thread(() -> {
                Request req = new Request();
                handle(req, multiplex, mutex);

            });
            threads[i] = t; 
            t.start();
        }

        for(Thread thread: threads ){
            thread.join();
        }
        
    }

    static void handle (Request req, Semaphore multiplex, Semaphore mutex){
        try {
            //DEFINIÇÃO DO TIPO CASO SEJA O PRIMEIRO
            mutex.acquire();
            if(currentType == 0 ){
                currentType = req.getType();
            }
            mutex.release();

            if(currentType != req.getType()){
                type[currentType-1].acquire();
            }

            multiplex.acquire();
            mutex.acquire();
            currentCount++;
            exec(req);
            mutex.release();
            multiplex.release();
            type[currentType-1].release();
        } catch (InterruptedException ex) {
        }


    }

    static void exec(Request req){
        System.out.println("EXECULTANDO REQ DE TIPO " + req.getType());
    }
    
    static class Request{
        private int type;
        private Random random;

        public Request(){
            this.random = new Random();
            this.type = this.random.nextInt(2) + 1;
        }

        public int getType(){
            return this.type;
        }
    }    
}
