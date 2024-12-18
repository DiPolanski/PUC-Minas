import java.io.*;
import java.net.*;
import java.util.Scanner;

class arquivoHTML {
    public static String getHtml(String endereco) {
        URL url;
        InputStream is = null;
        BufferedReader br;
        String resp = "", line;

        try {
            url = new URL(endereco);
            is = url.openStream(); // throws an IOException
            br = new BufferedReader(new InputStreamReader(is));

            while ((line = br.readLine()) != null) {
                resp += line + "\n";
            }
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        try {
            is.close();
        } catch (IOException ioe) {
            // nothing to see here

        }

        return resp;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String endereco, html, nomeSite;

        while (1) {
            nomeSite = scanner.nextLine();
            endereco = scanner.nextLine();
            html = getHtml(endereco);

            if (nomeSite.equalsIgnoreCase("FIM")) {
                break;
            }
            // Conta o número de ocorrências das tags <br> e <table>
            int brCont = contOcorrencias(html, "<br>");
            int tableCont = contOcorrencias(html, "<table>");

            // Exibe os resultados
            System.out.println("Número de ocorrências de <br>: " + brCont);
            System.out.println("Número de ocorrências de <table>: " + tableCont);

            // Conta o número de vogais (com ou sem acento) e consoantes no texto
            int[] vogaisCont = contarVogais(html);
            int consoantesCont = contarConsoantes(html);

            // Exibe os resultados das vogais e consoantes
            exibirResultadosVogais(vogaisCont);
            System.out.println("Número de consoantes: " + consoantesCont);
        }
    }

    private static int contOcorrencias(String texto, String pattern) {
        int cont = 0;
        int index = 0;
        while ((index = texto.indexOf(pattern, index)) != -1) {
            cont++;
            index += pattern.length();
        }
        return cont;
    }

    private static int[] contarVogais(String texto) {
        texto = texto.toLowerCase(); // Converte todo o texto para minúsculas para facilitar a contagem
        int[] vogaisCont = new int[23]; // Índices de 0 a 21 representam as vogais, o índice 22 representa consoantes

        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            switch (c) {
                case 'a':
                    vogaisCont[0]++;
                    break;
                case 'e':
                    vogaisCont[1]++;
                    break;
                case 'i':
                    vogaisCont[2]++;
                    break;
                case 'o':
                    vogaisCont[3]++;
                    break;
                case 'u':
                    vogaisCont[4]++;
                    break;
                case 'á':
                    vogaisCont[5]++;
                    break;
                case 'é':
                    vogaisCont[6]++;
                    break;
                case 'í':
                    vogaisCont[7]++;
                    break;
                case 'ó':
                    vogaisCont[8]++;
                    break;
                case 'ú':
                    vogaisCont[9]++;
                    break;
                case 'à':
                    vogaisCont[10]++;
                    break;
                case 'è':
                    vogaisCont[11]++;
                    break;
                case 'ì':
                    vogaisCont[12]++;
                    break;
                case 'ò':
                    vogaisCont[13]++;
                    break;
                case 'ù':
                    vogaisCont[14]++;
                    break;
                case 'ã':
                    vogaisCont[15]++;
                    break;
                case 'õ':
                    vogaisCont[16]++;
                    break;
                case 'â':
                    vogaisCont[17]++;
                    break;
                case 'ê':
                    vogaisCont[18]++;
                    break;
                case 'î':
                    vogaisCont[19]++;
                    break;
                case 'ô':
                    vogaisCont[20]++;
                    break;
                case 'û':
                    vogaisCont[21]++;
                    break;
                default:
                    vogaisCont[22]++;
                    break; // Consoante
            }
        }
        return vogaisCont;
    }

    private static int contarConsoantes(String texto) {
        int consoantesCont = 0;
        texto = texto.toLowerCase();

        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (c >= 'a' && c <= 'z') { // Verifica se o caractere é uma letra do alfabeto
                if (c != 'a' && c != 'e' && c != 'i' && c != 'o' && c != 'u' &&
                        c != 'á' && c != 'é' && c != 'í' && c != 'ó' && c != 'ú' &&
                        c != 'à' && c != 'è' && c != 'ì' && c != 'ò' && c != 'ù' &&
                        c != 'ã' && c != 'õ' && c != 'â' && c != 'ê' &&
                        c != 'î' && c != 'ô' && c != 'û') {
                    consoantesCont++;
                }
            }
        }
        return consoantesCont;
    }

    private static void exibirResultadosVogais(int[] vogaisCont) {
        String[] vogais = { "a", "e", "i", "o", "u", "á", "é", "í", "ó", "ú",
                "à", "è", "ì", "ò", "ù", "ã", "õ", "â", "ê", "î", "ô", "û" };

        for (int i = 0; i < vogais.length; i++) {
            System.out.println("Número de " + vogais[i] + "(" + (i + 1) + "): " + vogaisCont[i]);
        }
    }
}
