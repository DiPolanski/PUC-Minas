import java.util.Scanner;

class Palindromo {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {

            String palavra = scanner.nextLine();

            if (palavra.equalsIgnoreCase("FIM")) {
                break;
            }

            if (EhPalindromo(palavra)) {
                System.out.println("SIM");
            } else {
                System.out.println("NAO");
            }
        }
    }

    public static boolean EhPalindromo(String palavra) {

        int esquerda = 0;
        int direita = palavra.length() - 1;

        while (esquerda < direita) {
            if (palavra.charAt(esquerda) != palavra.charAt(direita)) {
                return false;
            }
            esquerda++;
            direita--;
        }
        return true;
    }
}