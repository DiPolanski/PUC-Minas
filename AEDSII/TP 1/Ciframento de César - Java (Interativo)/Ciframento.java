import java.util.Scanner;

public class Ciframento {
    public static String Cifra(String mensagemOri) {
        String mensagemCifrada = "";// inicializa a mensagem cifrada sem nada
        for (int i = 0; i < mensagemOri.length(); i++) {
            if (mensagemOri.charAt(i) != 65533)// confere se há algum caracter especial ou corrompido
                mensagemCifrada += (char) (mensagemOri.charAt(i) + 3);
            else
                mensagemCifrada += (char) (mensagemOri.charAt(i));

        }
        return mensagemCifrada;
    }

    public static void main(String args[]) {
        Scanner leitor = new Scanner(System.in);
        String mensagem;
        while (true) {
            mensagem = leitor.nextLine();
            if (mensagem.equals("FIM")) {
                leitor.close();
                System.exit(-1);
            } // confere o final do loop com "FIM"

            System.out.println(Cifra(mensagem));
        }

    }
}
