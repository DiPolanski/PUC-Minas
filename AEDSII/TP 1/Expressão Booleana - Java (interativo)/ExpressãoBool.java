import java.util.Stack;

public class ExpressãoBool {

    public static boolean avaliarExpressaoBooleana(String entrada) {
        // Divida a entrada em partes: número de entradas, valores binários e expressão
        String[] partes = entrada.split(" ");
        int n = Integer.parseInt(partes[0]); // Número de entradas
        String valores = partes[1]; // Valores binários
        String expressao = partes[2]; // Expressão booleana

        // Verifique se o número de entradas e valores coincide
        if (valores.length() != n) {
            return false; // Se não coincidir, a expressão é inválida
        }

        // Substitua as variáveis pelos valores correspondentes
        for (int i = 0; i < n; i++) {
            char variavel = (char) ('A' + i); // A primeira variável é 'A', a segunda é 'B' e assim por diante
            char valor = valores.charAt(i);
            expressao = expressao.replace(String.valueOf(variavel), String.valueOf(valor));
        }

        // Avalie a expressão booleana
        return avaliar(expressao);
    }

    public static boolean avaliar(String expressao) {
        // Implementação simples para avaliar a expressão booleana
        Stack<Character> operadores = new Stack<>();
        Stack<Boolean> operandos = new Stack<>();

        for (char caractere : expressao.toCharArray()) {
            if (caractere == '0' || caractere == '1') {
                operandos.push(caractere == '1');
            } else if (caractere == '&' || caractere == '|') {
                while (!operadores.isEmpty() && (operadores.peek() == '&' || operadores.peek() == '|') &&
                        (precedencia(operadores.peek()) >= precedencia(caractere))) {
                    aplicarOperador(operandos, operadores.pop());
                }
                operadores.push(caractere);
            } else if (caractere == '(') {
                operadores.push(caractere);
            } else if (caractere == ')') {
                while (!operadores.isEmpty() && operadores.peek() != '(') {
                    aplicarOperador(operandos, operadores.pop());
                }
                operadores.pop(); // Remova o '(' correspondente
            }
        }

        while (!operadores.isEmpty()) {
            aplicarOperador(operandos, operadores.pop());
        }

        return operandos.pop();
    }

    private static int precedencia(char operador) {
        if (operador == '&') {
            return 2;
        } else if (operador == '|') {
            return 1;
        } else {
            return 0;
        }
    }

    private static void aplicarOperador(Stack<Boolean> operandos, char operador) {
        boolean operando2 = operandos.pop();
        boolean operando1 = operandos.pop();
        if (operador == '&') {
            operandos.push(operando1 && operando2);
        } else if (operador == '|') {
            operandos.push(operando1 || operando2);
        }
    }

    public static void main(String[] args) {
        // Exemplo de entrada: "3 101 &A|B"
        String entrada = "3 101 &A|B";
        boolean resultado = avaliarExpressaoBooleana(entrada);

        // Exibe o resultado
        if (resultado) {
            System.out.println("SIM");
        } else {
            System.out.println("NÃO");
        }
    }
}
