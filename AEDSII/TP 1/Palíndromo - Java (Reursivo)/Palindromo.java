import java.util.Scanner;

class Palindromo {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {

            String palavra = scanner.nextLine();// escaneia a pavra

            int esquerda = 0;
            int direita = palavra.length() - 1;

            if (palavra.equalsIgnoreCase("FIM")) {
                break;
            }

            if (EhPalindromo(palavra, direita, esquerda)) {// keva as informaçoes para conferir
                System.out.println("SIM");
            } else {
                System.out.println("NAO");
            }
        }

    }

    public static boolean EhPalindromo(String palavra, int direita, int esquerda) {

        if (palavra.charAt(esquerda) != palavra.charAt(direita)) {// confere os termos
            return false;
        }
        esquerda++;
        direita--;

        if (esquerda < direita) {
            EhPalindromo(palavra, direita, esquerda);// volta para a função, com novos valores em esquerdae e direita
        }
        return true;
    }
}