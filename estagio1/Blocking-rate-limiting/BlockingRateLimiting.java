import java.util.Random;
import java.util.concurrent.Semaphore;

public class BlockingRateLimiting{

    static Semaphore multiplex = new Semaphore(3);
    static Semaphore mutex = new Semaphore(1);
    static Semaphore wait = new Semaphore(0);
    static int currentType = 0;
    static Integer[] currentRunning = {0,0};


    public static void main(String[] args) throws InterruptedException {


        Thread[] threads = new Thread[30];

        for(int i=0; i<30; i++){
            Thread t = new Thread(() -> {
                Request req = new Request();
                handle(req);

            });
            threads[i] = t; 
            t.start();
        }

        for(Thread thread: threads ){
            thread.join();
        }
        
    }


    //USANDO READ-WRITE PARA RESOLVER
    static void handle (Request req){
        try {

            //Caso seja o primeiro a execultar define o tipo da execução
            mutex.acquire();
            if(currentType == 0){
                currentType = req.getType();
            }
            mutex.release();
            
            if(currentType != req.getType()){
                wait.acquire();
            }
       
            //LIMITANDO A QUANTIDADE DE EXECUÇÃO DE EXEC
            multiplex.acquire();
            //INCREMENTAR QUANDO ESTIVER EXECULTANDO

            mutex.acquire();
            currentRunning[currentType-1]++;
            mutex.release();

            //REGIAO CRITICA
            exec(req);

            mutex.acquire();
            currentRunning[currentType-1]--;
            mutex.release();
            multiplex.release();

            //Caso não haja mais ninguem do tipo execultando
            mutex.acquire();
            if(currentRunning[currentType-1] == 0){
                currentType = 0;
            }
            wait.release();
            mutex.release();


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
