import java.io.*;
import java.nio.file.*;
import java.util.*;

public class PasswordProcessorSerial {
    //Recebe os argumentos via linha de comando
    public static void main(String[] args) {
        //Caso não seja passado nenhum parametro 
        if (args.length < 1) {
            System.out.println("Uso: java PasswordProcessorSerial <caminho_do_diretorio>");
            return;
        }

        //Caso tenha recebido, pega a primeira posição do array
        String directoryPath = args[0]; // Recebe o caminho como argumento

        //Cria um file baseado no diretorio fornecido
        //permiti manipular o diretorio
        File directory = new File(directoryPath);
        //Caso o diretorio não exista ou não seja um diretorio
        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("Erro: Diretório não encontrado ou inválido.");
            return;
        }

        //Lista todos os arquivos do diretorio
        File[] files = directory.listFiles((dir, name) -> name.endsWith(""));
        if (files == null) {
            System.out.println("Erro ao listar arquivos no diretório.");
            return;
        }

        //Para cada arquvido do diretorio chama processfile
        for (File file : files) {
            //isso aqui é feito simultaneamente
            //para deixar concorrente precisamos fazer com que isso seja processado concorrentemente
            processFile(file);
        }
    }

    private static void processFile(File file) {
        System.out.println("Processing file: " + file.getName());
        //Esta linha sera usada para armazenar cada linha do arquivo apos passar pela função rot13
        List<String> obfuscatedLines = new ArrayList<>();


        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            //Loop que ler linha por linha e para cada linha chama rot13 e armazena na lista
            while ((line = reader.readLine()) != null) {
                obfuscatedLines.add(rot13(line)); // Adiciona a linha ofuscada à lista
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo " + file.getName() + ": " + e.getMessage());
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            //cria um arquivo para armazenar as linhas passadas pela função
            for (String obfuscatedLine : obfuscatedLines) {
                writer.write(obfuscatedLine);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao escrever no arquivo " + file.getName() + ": " + e.getMessage());
        }
    }


    private static String rot13(String input) {
        StringBuilder result = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (c >= 'a' && c <= 'z') {
                result.append((char) (((c - 'a' + 13) % 26) + 'a'));
            } else if (c >= 'A' && c <= 'Z') {
                result.append((char) (((c - 'A' + 13) % 26) + 'A'));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
}

