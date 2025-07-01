import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        
        Scanner scanner = new Scanner(System.in);

        System.out.print("Informe o número de threads: ");
        int n = scanner.nextInt();
        Barrier barrier = new Barrier(n);

        for(int i=0; i<n; i++){
            Thread t = new Thread(new ThreadFunction(barrier));
            t.start();

        }
    }
    
}
