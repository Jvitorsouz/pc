import java.io.*;
import java.util.concurrent.*;
import java.util.*;

public class ContadorPalavras3 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        if (args.length == 0) {
            System.out.println("Uso: java ContadorPalavras3 <arquivo1> <arquivo2> ...");
            return;
        }

        // Testar diferentes tipos de ExecutorService
        ExecutorService[] executors = {
            Executors.newSingleThreadExecutor(),
            Executors.newCachedThreadPool(),
            Executors.newFixedThreadPool(10)
        };

        String[] nomesExecutors = {
            "SingleThreadExecutor",
            "CachedThreadPool",
            "FixedThreadPool(10)"
        };

        for (int i = 0; i < executors.length; i++) {
            ExecutorService executor = executors[i];
            String nomeExecutor = nomesExecutors[i];

            System.out.println("\n--- Usando " + nomeExecutor + " ---");

            long startTime = System.currentTimeMillis();

            List<Future<Integer>> resultados = new ArrayList<>();

            // Submete uma tarefa para cada arquivo
            for (String arquivo : args) {
                resultados.add(executor.submit(() -> contarPalavras(arquivo)));
            }

            int totalPalavras = 0;
            for (int j = 0; j < args.length; j++) {
                int qtd = resultados.get(j).get();
                System.out.println("Arquivo \"" + args[j] + "\": " + qtd + " palavras");
                totalPalavras += qtd;
            }

            long endTime = System.currentTimeMillis();
            System.out.println("Total de palavras: " + totalPalavras);
            System.out.println("Tempo de execução: " + (endTime - startTime) + " ms");

            executor.shutdown();
        }
    }

    static int contarPalavras(String nomeArquivo) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(nomeArquivo));
        int count = 0;
        String linha;
        while ((linha = br.readLine()) != null) {
            if (!linha.trim().isEmpty()) {
                count += linha.trim().split("\\s+").length;
            }
        }
        br.close();
        return count;
    }
}
