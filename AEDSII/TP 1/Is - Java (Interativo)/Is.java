import java.util.Scanner;

class Is {

    public static boolean ConfereReal(String string) {
        try {
            double doubleValue = Double.parseDouble(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean ConfereInteiro(String string) {
        try {
            int intValue = Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean TemLetra(String string) {
        for (int i = 0; i < string.length(); i++) {
            if (!Character.isLetter(string.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean ConfereVogal(String string) {
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) != 'A' && string.charAt(i) != 'E' && string.charAt(i) != 'I' && string.charAt(i) != 'O'
                    && string.charAt(i) != 'U') {
                return false;
            }
        }
        return true;
    }

    public static boolean ConfereConsoante(String string) {
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == 'A' || string.charAt(i) == 'E' || string.charAt(i) == 'I' || string.charAt(i) == 'O'
                    || string.charAt(i) == 'U') {
                return false;
            }
        }
        return true;
    }

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        String string;

        while (true) {
            string = scanner.nextLine();
            string = string.toUpperCase();

            if (string.equals("FIM")) {
                break; // Sai do loop quando a string é "FIM"
            }

            boolean temVogal = false;
            boolean temConsoante = false;
            boolean temInteiro = false;
            boolean temReal = false;

            boolean temLetra = TemLetra(string);

            if (temLetra) {
                if (ConfereVogal(string)) {
                    temVogal = true;
                } else {
                    if (ConfereConsoante(string)) {
                        temConsoante = true;
                    }
                }
            }

            if (!temLetra) {
                if (ConfereInteiro(string)) {
                    temInteiro = true;
                } else if (ConfereReal(string)) {
                    temReal = true;
                }
            }

            if (temVogal == true && temConsoante == false && temInteiro == false && temReal == false) {
                System.out.println("SIM " + "NAO " + "NAO " + "NAO");
            } else if (temVogal == false && temConsoante == true && temInteiro == false && temReal == false) {
                System.out.println("NAO " + "SIM " + "NAO " + "NAO");
            } else if (temVogal == false && temConsoante == false && temInteiro == true && temReal == false) {
                System.out.println("NAO " + "NAO " + "SIM " + "NAO");
            } else if (temVogal == false && temConsoante == false && temInteiro == false && temReal == true) {
                System.out.println("NAO " + "NAO " + "NAO " + "SIM");
            } else {
                System.out.println("NAO " + "NAO " + "NAO " + "NAO");
            }
        }
    }
}
