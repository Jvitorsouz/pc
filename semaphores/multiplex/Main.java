import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Main {

    private static Semaphore multiplex;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Informe o número: ");
        int n = scanner.nextInt();

        Contador contador = new Contador();
        multiplex = new Semaphore(n);

        for(int i=0; i<=n; i++){
            Thread thread = new Thread(new ThreadFunction(multiplex, contador));
            thread.start();
        }

        System.out.println(contador.getCount());
    }
    
}
