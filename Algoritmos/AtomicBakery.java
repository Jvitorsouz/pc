
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


public class AtomicBakery {

	//lista para declarar que a thread na posição i quer entrar na região critica
	private AtomicBoolean[] choosing;
	//lista responsável pelos tickes das threads
	private AtomicInteger[] tickets;
	//Numero de threads existentes
	private final int n;

	public AtomicBakery(int nthreads) {
		this.n = nthreads;
		this.choosing = new AtomicBoolean[n];
		this.tickets = new AtomicInteger[n];
		for(int i = 0; i<n; i++){
			this.choosing[i] = new AtomicBoolean(false);
			this.tickets[i] = new AtomicInteger(0);
		}
	}

	public void lock() {
		
		//Pega o id da thread
		int my_id = (int) Thread.currentThread().getId();
		
		//Sinaliza que a thread que entrar na região critica
		this.choosing[my_id].set(true);
		
		//Procura o maior valor da lista de tickets
		for (int j = 0; j < n; j++) {
			if (tickets[j].get() > tickets[my_id].get()) {
				tickets[my_id].set(tickets[j].get());
			}
		}
		//Após achar o maior valor ele incrementa em mais um!
		tickets[my_id].incrementAndGet();
		//Sinaliza que a thread já escolheu seu ticket
		choosing[my_id].set(false);

		//Verifica todas as threads para saber se é sua vez de entrar na região critica
		for (int j = 0; j < n; j++) {
			//Fica em uma espera ocupada equanto uma thread esta escolhendo seu ticket
			while (choosing[j].get());
			while (	
					(tickets[j].get() != 0) && //Verifica se a thread esta competindo para entrar dentro da região critica, ou seja possui um ticket
					//Verifica se há outra thread cujo o id é menor que o da thread atual
					( (tickets[j].get() < tickets[my_id].get()) || 
					//Caso pegue o mesmo número desempata por quem possui o menor id de thread
					//A thread que possui menor id, vence!
					( (tickets[j].get() == tickets[my_id].get()) && (j < my_id) ) ) 
				   );

		}
	}

	public void unlock() {
		int my_id = (int) Thread.currentThread().getId();
		tickets[my_id].set(0);
	}
}