import java.io.*;
import java.util.concurrent.*;
import java.util.*;

public class ContadorPalavras4 {

    // Callable que conta as palavras de um arquivo
    static class ContadorArquivo implements Callable<Integer> {
        private final String nomeArquivo;

        public ContadorArquivo(String nomeArquivo) {
            this.nomeArquivo = nomeArquivo;
        }

        @Override
        public Integer call() throws Exception {
            int qtd = contarPalavras(nomeArquivo);
            System.out.println("Arquivo \"" + nomeArquivo + "\": " + qtd + " palavras");
            return qtd;
        }
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        if (args.length == 0) {
            System.out.println("Uso: java ContadorPalavras4 <arquivo1> <arquivo2> ...");
            return;
        }

        // Escolha do executor (pode mudar para testar outros tipos)
        ExecutorService executor = Executors.newFixedThreadPool(10);

        List<Future<Integer>> futuros = new ArrayList<>();

        long startTime = System.currentTimeMillis();

        // Submete uma tarefa Callable para cada arquivo
        for (String arquivo : args) {
            futuros.add(executor.submit(new ContadorArquivo(arquivo)));
        }

        int totalPalavras = 0;

        // Coleta os resultados via Future
        for (Future<Integer> f : futuros) {
            totalPalavras += f.get(); // get() bloqueia até a tarefa terminar
        }

        long endTime = System.currentTimeMillis();

        System.out.println("Total de palavras em todos os arquivos: " + totalPalavras);
        System.out.println("Tempo de execução: " + (endTime - startTime) + " ms");

        executor.shutdown();
    }

    // Função para contar palavras em um arquivo
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
