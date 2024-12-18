import java.util.Scanner;
import java.io.*;

class Arquivo {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String nomeArquivo = "meuarquivo.txt"; // Nome do arquivo criado

        int n;
        n = scanner.nextInt();
        scanner.nextLine(); // Consuma a quebra de linha após o número inteiro

        try {
            // Crie um objeto FileOutputStream para escrever no arquivo
            FileOutputStream arquivoOutput = new FileOutputStream(nomeArquivo);

            for (int i = 0; i < n; i++) {
                // Escreve dados no arquivo, incluindo uma quebra de linha para separar as
                // entradas
                String conteudo = scanner.nextLine() + "\n";
                arquivoOutput.write(conteudo.getBytes());
            }
            // Fecha o arquivo
            arquivoOutput.close();

            // Reabre o arquivo para leitura
            FileInputStream arquivoInput = new FileInputStream(nomeArquivo);
            InputStreamReader leitor = new InputStreamReader(arquivoInput);
            BufferedReader bufferedReader = new BufferedReader(leitor);

            // Determine o número de linhas no arquivo
            int numLinhas = 0;
            while (bufferedReader.readLine() != null) {
                numLinhas++;
            }

            // Fecha o arquivo de leitura
            bufferedReader.close();

            // Reabre o arquivo para leitura novamente
            arquivoInput = new FileInputStream(nomeArquivo);
            leitor = new InputStreamReader(arquivoInput);
            bufferedReader = new BufferedReader(leitor);

            // Crie um array para armazenar as linhas
            String[] linhas = new String[numLinhas];

            // Leitura das linhas e armazenamento no array
            for (int i = 0; i < numLinhas; i++) {
                linhas[i] = bufferedReader.readLine();
            }

            // Fecha o arquivo de leitura
            bufferedReader.close();

            // Imprime os números na ordem inversa, formatando-os adequadamente
            for (int i = numLinhas - 1; i >= 0; i--) {
                String linha = linhas[i];
                if (linha.contains(".")) {
                    // Se a linha contém um ponto, imprime como número de ponto flutuante
                    System.out.println(Double.parseDouble(linha));
                } else {
                    // Caso contrário, imprime como número inteiro
                    System.out.println(Integer.parseInt(linha));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
