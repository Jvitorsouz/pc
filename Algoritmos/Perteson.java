public class Perteson {
    
    private volatile boolean[] flag = new boolean[2];
    private volatile int victim;

    public void lock(){
        //Recupera o id da thread que esta execultando
        int my_id = (int) Thread.currentThread().getId();
        //Recupera o id da outra thread
        int j = 1 - my_id;
        //Sinaliza que deseja entrar na região crtica
        flag[my_id] = true;
        //Se declara como vitima, ficando disposta a espera caso outra thread esteja execultando a região critica
        this.victim = my_id;
        victim = my_id;

        //Fica em espera ocupada caso haja alguem execultando a região critica e a thread execultando seja a vitima
        while(flag[j] && victim == my_id);


    }

    public void unlock(){
        int my_id = (int) Thread.currentThread().getId();
        flag[my_id] = false;
    }
}
