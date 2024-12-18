import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Backup {

    public static String getVersaoBackUp() {
        // Obter a data e hora atual
        LocalDateTime now = LocalDateTime.now();

        // Definir o formato desejado
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        // Formatar a data e hora atual
        String formattedDateTime = now.format(formatter);
        System.out.println(formattedDateTime);
        // Retornar a data e hora formatadas
        return formattedDateTime;
    }

    public static void salvarBackup(byte[] dados, String nomeArquivo) throws IOException {

        nomeArquivo = nomeArquivo.replaceAll("[\\\\/:*?\"<>|]", "_");
        // Criar o diretório de backup se ele ainda não existir
        File diretorioBackup = new File("backup");
        if (!diretorioBackup.exists()) {
            if (!diretorioBackup.mkdir()) {
                throw new IOException("Não foi possível criar o diretório de backup");
            }
        }

        // Usar try-with-resources para garantir que o FileOutputStream seja fechado
        // corretamente
        try (FileOutputStream fos = new FileOutputStream(new File(diretorioBackup, nomeArquivo))) {
            // Escrever os dados no arquivo de backup
            fos.write(dados);
        }
    }

    public static byte[] lerBackup(String nomeArquivo) throws IOException {
        // Ler os dados do arquivo de backup
        File arquivo = new File(nomeArquivo);
        byte[] dados = new byte[(int) arquivo.length()];
        FileInputStream fis = new FileInputStream(arquivo);

        // Ler os dados
        fis.read(dados);
        fis.close();
        return dados;
    }

    public static void ListarBackUps() {
        // Caminho da pasta "backups"
        String directoryPath = "backup";

        // Cria um objeto File para a pasta
        File directory = new File(directoryPath);

        // Verifica se o caminho é um diretório
        if (directory.isDirectory()) {
            // Lista os nomes dos arquivos na pasta
            String[] fileList = directory.list();

            if (fileList != null) {
                // Junta os nomes dos arquivos em uma única String
                StringBuilder fileNames = new StringBuilder();
                for (String fileName : fileList) {
                    fileNames.append(fileName).append("\n");
                }

                // Converte o StringBuilder para String
                String allFileNames = fileNames.toString();

                // Imprime os nomes dos arquivos
                System.out.println(allFileNames);
            } else {
                System.out.println("A pasta está vazia ou não é possível listar os arquivos.");
            }
        } else {
            System.out.println("O caminho especificado não é um diretório.");
        }
    }
}
