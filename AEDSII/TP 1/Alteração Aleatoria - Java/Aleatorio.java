import java.util.Random;
import java.util.Scanner;

class Aleatorio {

    public static String Troca(String string, char letra1, char letra2) {

        String novaString = "";

        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == letra1) { // confere onde tem a letra1 na String
                novaString += letra2;
            } else {
                novaString += (char) (string.charAt(i));
            }
        }

        return novaString;
    }

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);// scanner para a string
        String string;
        char letra1;
        char letra2;

        Random gerador = new Random();
        gerador.setSeed(4);// coloca a seed como 4

        while (true) {
            letra1 = (char) ('a' + (Math.abs(gerador.nextInt()) % 26));// sorteia a primeira letra
            letra2 = (char) ('a' + (Math.abs(gerador.nextInt()) % 26));// sorteia a segunda letra
            string = scanner.nextLine();
            if (string.equals("FIM")) {
                scanner.close();
                System.exit(-1);
            }

            String novaString = Troca(string, letra1, letra2);

            System.out.println(novaString);

        }
    }
}