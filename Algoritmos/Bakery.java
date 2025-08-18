
public class Bakery {

	//lista para declarar que a thread na posição i quer entrar na região critica
	private volatile boolean[] choosing;
	//lista responsável pelos tickes das threads
	private volatile int[] tickets;
	//Numero de threads existentes
	private final int n;

	public Bakery(int nthreads) {
		this.n = nthreads;
		this.choosing = new boolean[n];
		this.tickets = new int[n];
		for(int i = 0; i<n; i++){
			this.choosing[i] = false;
			this.tickets[i] = 0;
		}
	}

	public void lock() {
		
		//Pega o id da thread
		int my_id = (int) Thread.currentThread().getId();
		
		//Sinaliza que a thread que entrar na região critica
		choosing[my_id] = true;
		
		//Procura o maior valor da lista de tickets
		for (int j = 0; j < n; j++) {
			if (tickets[j] > tickets[my_id]) {
				tickets[my_id] = tickets[j];
			}
		}
		//Após achar o maior valor ele incrementa em mais um!
		tickets[my_id]++;
		//Sinaliza que a thread já escolheu seu ticket
		choosing[my_id] = false;

		//Verifica todas as threads para saber se é sua vez de entrar na região critica
		for (int j = 0; j < n; j++) {
			//Fica em uma espera ocupada equanto uma thread esta escolhendo seu ticket
			while (choosing[j]);
			while (	
					(tickets[j] != 0) && //Verifica se a thread esta competindo para entrar dentro da região critica, ou seja possui um ticket
					//Verifica se há outra thread cujo o id é menor que o da thread atual
					( (tickets[j] < tickets[my_id]) || 
					//Caso pegue o mesmo número desempata por quem possui o menor id de thread
					//A thread que possui menor id, vence!
					( (tickets[j] == tickets[my_id]) && (j < my_id) ) ) 
				   );

		}
	}

	public void unlock() {
		int my_id = (int) Thread.currentThread().getId();
		tickets[my_id] = 0;
	}
}