public class LockTwo {

    //Responsável por armazena o id da thread que esta disposta a esperar
    private volatile int victim;

    public void lock(){
        //Recupera o id da thread que está execultando
        int my_id = (int) Thread.currentThread().getId();
        //Atribuição
        this.victim = my_id;

        //Espera ocupada
        while(victim == my_id);

    }

    public void unlock(){}
    
}
