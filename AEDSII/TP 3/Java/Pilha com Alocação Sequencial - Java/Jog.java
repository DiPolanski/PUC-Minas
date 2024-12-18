import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

class Jogadores {
    private int id;
    private String nome;
    private int altura;
    private int peso;
    private String universidade;
    private int anoNascimento;
    private String cidadeNascimento;
    private String estadoNascimento;
    private static Jogadores[] jogadores;

    public static void setJogadores(Jogadores[] array) {
        jogadores = array;
    }

    public static Jogadores[] getJogadores() {
        return jogadores;
    }

    public Jogadores() {
    }

    public Jogadores(int id, String nome, int altura, int peso, String universidade, int anoNascimento,
            String cidadeNascimento, String estadoNascimento) {
        this.id = id;
        this.nome = nome;
        this.altura = altura;
        this.peso = peso;
        this.universidade = universidade;
        this.anoNascimento = anoNascimento;
        this.cidadeNascimento = cidadeNascimento;
        this.estadoNascimento = estadoNascimento;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getAltura() {
        return altura;
    }

    public int getPeso() {
        return peso;
    }

    public String getUniversidade() {
        return universidade;
    }

    public int getAnoNascimento() {
        return anoNascimento;
    }

    public String getCidadeNascimento() {
        return cidadeNascimento;
    }

    public String getEstadoNascimento() {
        return estadoNascimento;
    }

    public Jogadores clone() {
        return new Jogadores(id, nome, altura, peso, universidade, anoNascimento, cidadeNascimento, estadoNascimento);
    }

    public static void imprimir(Jogadores[] jogador, int i) {
        System.out
                .println("[" + jogador[i].getId() + " ## " + jogador[i].getNome() + " ## " + jogador[i].getAltura()
                        + " ## " + jogador[i].getPeso() + " ## " + jogador[i].getAnoNascimento() + " ## "
                        + jogador[i].getUniversidade() + " ## " + jogador[i].getCidadeNascimento() + " ## "
                        + jogador[i].getEstadoNascimento() + "]");

    }

    public static Jogadores ler(String arqCSV, String ID, Jogadores[] jogador, int numJogadores)
            throws NumberFormatException, IOException {
        FileReader arq = new FileReader(arqCSV);
        BufferedReader leitorBuffer = new BufferedReader(arq);

        String linha;
        String[] colunas;
        Jogadores jogador1 = null;

        while ((linha = leitorBuffer.readLine()) != null) {
            colunas = linha.split(",", -1);

            if (ID.equals(colunas[0])) {
                int id = Integer.parseInt(colunas[0]);
                String nome = colunas[1];
                int altura = Integer.parseInt(colunas[2]);
                int peso = Integer.parseInt(colunas[3]);
                String universidade = (colunas[4].isEmpty() ? "nao informado" : colunas[4]);
                int anoNascimento = Integer.parseInt(colunas[5]);
                String cidadeNascimento = (colunas[6].isEmpty() ? "nao informado" : colunas[6]);
                String estadoNascimento = (colunas[7].isEmpty() ? "nao informado" : colunas[7]);

                jogador1 = new Jogadores(id, nome, altura, peso, universidade, anoNascimento,
                        cidadeNascimento, estadoNascimento);
                break; // Break after finding the matching ID
            }
        }

        leitorBuffer.close(); // Close the reader outside the loop
        return jogador1;
    }

}

public class Jog {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String arqCSV = "/tmp/players.csv";
        String ID;
        int maxJogadores = 4000;
        Jogadores[] jogadores = new Jogadores[maxJogadores];
        int numJogadores = 0;
        PilhaSeq pilha = new PilhaSeq();

        Jogadores.setJogadores(jogadores);
        try {
            while (!(ID = scanner.nextLine()).equals("FIM")) {
                Jogadores jogador = Jogadores.ler(arqCSV, ID, jogadores, numJogadores);
                pilha.Inserir(jogador);
                numJogadores++;
            }

            int qntRegistros = scanner.nextInt();

            for (int i = 0; i < qntRegistros + 1; i++) {
                String entrada = scanner.nextLine();

                String[] itens = entrada.split(" ");
                String entradaC = itens[0];

                if (entradaC.equals("R")) {
                    pilha.Remover();
                } else if (entradaC.equals("I")) {
                    Jogadores jogador = Jogadores.ler(arqCSV, itens[1], jogadores, numJogadores);
                    pilha.Inserir(jogador);
                }
            }

            pilha.Mostrar();
        } catch (Exception e) {
            // Handle the exception appropriately, for example, print an error message
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    static class PilhaSeq {
        Jogadores[] pilha;
        int topo; // Index of the top element

        PilhaSeq() {
            this(1000);
        }

        PilhaSeq(int tamanho) {
            pilha = new Jogadores[tamanho];
            topo = -1; // Initialize the top index for an empty stack
        }

        void Inserir(Jogadores jogador) throws Exception {
            if (topo == pilha.length - 1) {
                throw new Exception("Erro! Pilha cheia.");
            }

            pilha[++topo] = jogador; // Increment topo and insert the element
        }

        Jogadores Remover() throws Exception {
            if (topo == -1) {
                throw new Exception("Erro! Pilha vazia.");
            }

            Jogadores removido = pilha[topo]; // Get the element at the top
            topo--; // Decrement topo to remove the top element
            System.out.println("(R) " + removido.getNome());
            return removido;
        }

        void Mostrar() {
            for (int i = 0; i <= topo; i++) {
                System.out.println("[" + i + "]" + " ## " + pilha[i].getNome() + " ## " + pilha[i].getAltura()
                        + " ## " + pilha[i].getPeso() + " ## " + pilha[i].getAnoNascimento() + " ## "
                        + pilha[i].getUniversidade() + " ## " + pilha[i].getCidadeNascimento() + " ## "
                        + pilha[i].getEstadoNascimento() + " ##");
            }
        }
    }

}
