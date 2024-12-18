import java.util.Scanner;

class Ciframento {

    public static String Cifra(String mensagemOri, int cont) {
        String mensagemCifrada = ""; // Inicializa a mensagem cifrada dentro do método

        if (mensagemOri.charAt(cont) != 65533) { // Confere se há algum caractere especial ou corrompido
            mensagemCifrada += (char) (mensagemOri.charAt(cont) + 3);
        } else {
            mensagemCifrada += (char) (mensagemOri.charAt(cont));
        }

        cont++;

        if (cont < mensagemOri.length()) {
            mensagemCifrada += Cifra(mensagemOri, cont); // Concatena o resultado da chamada recursiva
        }

        return mensagemCifrada;
    }

    public static void main(String args[]) {
        Scanner leitor = new Scanner(System.in);
        String mensagem;
        int cont = 0;

        while (true) {
            mensagem = leitor.nextLine();
            if (mensagem.equals("FIM")) {
                leitor.close();
                System.exit(-1);
            } // Confere o final do loop com "FIM"

            System.out.println(Cifra(mensagem, cont));
        }
    }
}
