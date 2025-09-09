import java.io.*;

public class ContadorPalavras2 {
    private static int totalPalavras = 0;

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Uso: java ContadorPalavras2 <arquivo1> <arquivo2> ...");
            return;
        }

        Thread[] threads = new Thread[args.length];

        for (int i = 0; i < args.length; i++) {
            String nomeArquivo = args[i];

            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        int qtd = contarPalavras(nomeArquivo);
                        System.out.println("Arquivo \"" + nomeArquivo + "\": " + qtd + " palavras");

                        // Atualiza o total de forma thread-safe
                        adicionarTotal(qtd);

                    } catch (IOException e) {
                        System.err.println("Erro ao ler o arquivo " + nomeArquivo + ": " + e.getMessage());
                    }
                }
            });

            threads[i].start();
        }

        // Espera todas as threads terminarem
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Total de palavras em todos os arquivos: " + totalPalavras);
    }

    private static synchronized void adicionarTotal(int qtd) {
        totalPalavras += qtd;
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
