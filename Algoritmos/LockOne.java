//Esse codigo garante exclusão mutua
//Esse codigo não esta livre de deadlock
public class LockOne {
  
    //Responsável por sinalizar e gerenciar threads que querem entrar na região critica
    private volatile boolean[] flag = new boolean[2];

    /*Estamos supondo que vamos possui apenas id 0 e 1 */
    public void lock(){
        //Recupera o id da thread
        int my_id = (int) (Thread.currentThread().getId());
        //Recupera o id da outra thread
        int j = 1 - my_id;

        //Declarar que quero entrar dentro da região critica
        flag[my_id] = true;

        //Espera ocupada caso alguma thread esteja executando a região critica!
        while(flag[j]);

    }

    public void unlock(){
        //Recupero o id da thread
        int my_id = (int) (Thread.currentThread().getId());
        //Sinaliza que saiu da região critica
        flag[my_id] = false;

    }


    
}
