import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Main{


    static final Semaphore mutex = new Semaphore(1);
    static final Semaphore prodSem = new Semaphore(0);
    static final Semaphore conSem = new Semaphore(10); //O semaforo é iniciado com a quantidade limite de objetos no buffer
    public static void main(String[] args) {
        
        List<Integer> Buffer = new ArrayList<>();
    }



    public static class Producer{

        private List<Integer> buffer;

        public Producer(List<Integer> buffer){
            this.buffer = buffer;
        }


        public void add (){

        }

    }

    public static class Consumer{

        private List<Integer> buffer;

        public Consumer(List<Integer> buffer){
            this.buffer = buffer;
        }

        public int get (){
            return -1;
        }

        

    }
}