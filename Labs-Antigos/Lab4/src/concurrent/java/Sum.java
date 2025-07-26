import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class Sum {

    private static int finalSum = 0;
    private static Semaphore mutex = new Semaphore(1);
    private static final Map<Long, List<String>> sumsByValue = new HashMap<>();
    private static Semaphore semaphore = new Semaphore(1);

    // A tarefa que será executada por cada thread
    static class SumTask implements Runnable {
        private final String path;
         private final Semaphore multiplex;

        public SumTask(String path, Semaphore multiplex) {
            this.path = path;
            this.multiplex = multiplex;
        }

        @Override
        public void run() {
            try {
				multiplex.acquire();
                long sum = sum(path);
                System.out.println(path + " : " + sum);
                semaphore.acquire();
                addMapa(sum, path);
                semaphore.release();
            } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            multiplex.release();
        }
    }

    public static void addMapa(long sum, String path){
        if (sumsByValue.containsKey(sum)) {
            // Se SIM, pegue a lista existente
            List<String> existingList = sumsByValue.get(sum);
            // E adicione o novo caminho a ela
            existingList.add(path);
        } else {
            // Se NÃO, crie uma nova lista
            List<String> newList = new ArrayList<>();
            // Adicione o caminho à nova lista
            newList.add(path);
            // E coloque a nova lista no mapa com a chave da soma
            sumsByValue.put(sum, newList);
        }
    }

    public static int sum(FileInputStream fis) throws IOException {
        int byteRead;
        int sum = 0;
        while ((byteRead = fis.read()) != -1) {
            sum += byteRead;
        }

        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finalSum += sum;
        mutex.release();


        return sum;
    }

    public static long sum(String path) throws IOException {
        Path filePath = Paths.get(path);
        if (Files.isRegularFile(filePath)) {
            // Usando try-with-resources para garantir que o FileInputStream seja fechado
            try (FileInputStream fis = new FileInputStream(filePath.toString())) {
                return sum(fis);
            }
        } else {
            throw new RuntimeException("Arquivo não regular: " + path);
        }
    }

    public static void main(String[] args) throws Exception {

        if (args.length < 1) {
            System.err.println("Uso: java Sum caminho/arquivo1 caminho/arquivo2 ...");
            System.exit(1);
        }
        Semaphore multiplex = new Semaphore(args.length/2);

        List<Thread> threads = new ArrayList<>();

        // 1. Criar e iniciar uma thread para cada arquivo
        for (String path : args) {
            SumTask task = new SumTask(path, multiplex);
            Thread thread = new Thread(task); 
            multiplex.acquire();
            thread.start(); 
            multiplex.release();
            threads.add(thread); 
        }

        for (Thread thread : threads) {
            thread.join(); 
        }

        System.out.println("\nSoma de todos os arquivos: " + finalSum);

        for (Map.Entry<Long, List<String>> entry : sumsByValue.entrySet()) {
            List<String> filesWithSameSum = entry.getValue();
            if (filesWithSameSum.size() > 1) {
                long sumValue = entry.getKey();
                String fileNames = String.join(" ", filesWithSameSum);
                System.out.println(sumValue + " " + fileNames);
            }
        }
        
        System.out.println("\nTodos os cálculos foram concluídos.");
    }
}