
public class exThread{


    public static void main(String[] args) {
        Runnable tarefa = new ThreadFunction();

        //"Criando uma nova thread que executará o método run() da classe ExThread"
        Thread t1 = new Thread(tarefa);
        //dando inicio a ela
        t1.start();

        System.out.println("Rodando na thread principal: " + Thread.currentThread().getName());
    }
    
}
