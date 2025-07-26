import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.Semaphore;

public class FileIndexingPipeline {

    static Map<String, Map<String, Integer>> fileIndex = new HashMap<>();
    //Primeiro produtor -> cosumidor Leitor -> Token
    static Semaphore mutexOne = new Semaphore(1);
    static Semaphore producerReader = new Semaphore(0);
    static Semaphore consumerToken = new Semaphore(50);//Capacidade limite

    //Segundo produtor -> consumidor Token -> Index
    static Semaphore mutexTwo = new Semaphore(1);
    static Semaphore producerToken = new Semaphore(0);
    static Semaphore consumerIndex = new Semaphore(50);//Capacidade limite

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Uso: java IndexadorPipeline <arquivo1.txt> <arquivo2.txt> ...");
            return;
        }

        Buffer<FileData> readBuffer = new Buffer<>();
        Buffer<FileData> tokenBuffer = new Buffer<>();

        List<Thread> todasAsThreads = new ArrayList<>();

        for (String pathStr : args) {

            Thread leitor = new Thread(new ReadFile(pathStr, readBuffer));
            Thread tokenizador = new Thread(new Tokenize(readBuffer, tokenBuffer));
            Thread indexador = new Thread(new Index(tokenBuffer));

            leitor.start();
            tokenizador.start();
            indexador.start();

            todasAsThreads.add(leitor);
            todasAsThreads.add(tokenizador);
            todasAsThreads.add(indexador);
        }

        for (Thread t : todasAsThreads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        System.out.println("fileIndex:");
        for (var word : fileIndex.keySet()) {
            System.out.println(word + " -> " + fileIndex.get(word));
        }
    }

    static class ReadFile implements Runnable {

        public final String pathStr;
        public final Buffer<FileData> readBuffer;

        public ReadFile(String pathStr,  Buffer<FileData> readBuffer) {
            this.pathStr = pathStr;
            this.readBuffer = readBuffer;
        }

        public void readFile() throws InterruptedException, IOException {
            consumerToken.acquire();
            mutexOne.acquire();
            Path path = Paths.get(pathStr);
            String content = Files.readString(path);
            readBuffer.insert(new FileData(path.getFileName().toString(), content));
            mutexOne.release();
            producerReader.release();
        }

        @Override
        public void run() {
            try {
                readFile();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                System.err.println("Erro ao ler arquivo " + this.pathStr + ": " + e.getMessage());
            }

        }
    }


    static class Tokenize implements Runnable {

        public final Buffer<FileData> readBuffer;
        public final Buffer<FileData> tokenBuffer;

        public Tokenize(Buffer<FileData> readBuffer, Buffer<FileData> tokenBuffer) {
            this.readBuffer = readBuffer;
            this.tokenBuffer = tokenBuffer;
        }

        public void tokenize() throws InterruptedException {
            //Parte do consumidor
            producerReader.acquire();
            mutexOne.acquire();
            FileData fileData = readBuffer.remove();
            if (fileData == null) return;
            mutexOne.release();
            consumerToken.release();

            //Parte do produtor
            consumerIndex.acquire();
            mutexTwo.acquire();
            String[] words = fileData.content.split("\\s+");
            String newContent = String.join(",", words);
            tokenBuffer.insert(new FileData(fileData.name, newContent));
            mutexTwo.release();
            producerToken.release();
        }

        @Override
        public void run() {
            try {
                tokenize();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }


    static class Index implements Runnable {

        public final Buffer<FileData> tokenBuffer;

        public Index(Buffer<FileData> tokenBuffer) {
            this.tokenBuffer = tokenBuffer;
        }

        public void index() throws InterruptedException {
            producerToken.acquire();
            mutexTwo.acquire();
            FileData fileData = tokenBuffer.remove();
            if (fileData == null) return;
            String[] words = fileData.content.split(",");
            for (String word : words) {
                fileIndex.putIfAbsent(word, new HashMap<>());
                Map<String, Integer> fileDatas = fileIndex.get(word);
                fileDatas.put(fileData.name, fileDatas.getOrDefault(fileData.name, 0) + 1);
            }
            mutexTwo.release();
            consumerIndex.release();

        }
        @Override
        public void run() {
            try {
                index();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }

    static class FileData {
        public final String name;
        public final String content;

        public FileData(String name, String content) {
            this.name = name;
            this.content = content;
        }
    }

    static class Buffer<T> {
        private final Queue<T> queue = new LinkedList<>();

        public void insert(T item) {
            queue.add(item);
        }

        public T remove() {
            return queue.poll();
        }

        public boolean isEmpty() {
            return queue.isEmpty();
        }
    }
}
