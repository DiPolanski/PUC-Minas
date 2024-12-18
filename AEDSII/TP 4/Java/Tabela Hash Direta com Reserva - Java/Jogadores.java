import java.util.Scanner;

import javax.swing.JOptionPane;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

// cls; javac Jogadores.java; java Jogadores < pub.in > result.txt

class Jogadores {
    private int id;
    private String nome;
    private int altura; // em centimetros
    private int peso; // em KG
    private String universidade;
    private int anoNascimento;
    private String cidadeNascimento;
    private String estadoNascimento;
    private static Jogadores[] jogadores; // Array de jogadores

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
    } // cria um clone para manipulação

    public static void imprimir(Jogadores[] jogador, int i) {
        System.out
                .println("[" + jogador[i].getId() + " ## " + jogador[i].getNome() + " ## " + jogador[i].getAltura()
                        + " ## " + jogador[i].getPeso() + " ## " + jogador[i].getAnoNascimento() + " ## "
                        + jogador[i].getUniversidade() + " ## " + jogador[i].getCidadeNascimento() + " ## "
                        + jogador[i].getEstadoNascimento() + "]");

    }// imprime da maneira pedida no exercício

    public static int linhaAtual = 1;

    public static Jogadores ler(String caminhoDoArquivoCSV, String qualId, Jogadores[] jogador, int numJogadores)
            throws NumberFormatException, IOException {
        FileReader arq = new FileReader(caminhoDoArquivoCSV);
        BufferedReader leitorBuffer = new BufferedReader(arq);
        // linhaAtual como variavel global, se nao ela reiniciaria o valor dela toda
        // hora
        // para 1
        // int linhaAtual = 1; // começa na linha 1, pois linha 0 e cabeçalho
        String linha;
        String[] colunas;
        while ((linha = leitorBuffer.readLine()) != null) {
            // precisa por o -1, senao ele nao vai alocar espaço para os nao tem informação
            colunas = linha.split(",", -1); // divide a linha em colunas, como sendo separação a ","
            if (qualId.equals(colunas[0])) {

                int id = Integer.parseInt(colunas[0]);
                String nome = colunas[1];
                int altura = Integer.parseInt(colunas[2]);
                int peso = Integer.parseInt(colunas[3]);
                String universidade = (colunas[4].isEmpty() ? "nao informado" : colunas[4]);
                int anoNascimento = Integer.parseInt(colunas[5]);
                String cidadeNascimento = (colunas[6].isEmpty() ? "nao informado" : colunas[6]);
                String estadoNascimento = (colunas[7].isEmpty() ? "nao informado" : colunas[7]);

                Jogadores jogador1 = new Jogadores(id, nome, altura, peso, universidade, anoNascimento,
                        cidadeNascimento, estadoNascimento);

                leitorBuffer.close();
                return jogador1;
            }

        }
        return null;

    }

    public static void main(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();
        Scanner scanner = new Scanner(System.in);

        String caminhoDoArquivoCSV = "/tmp/players.csv"; // nome do arquivo
        String qualId; // inicia a procura de id como vazia
        int maxJogadores = 4000;
        Jogadores[] jogadores = new Jogadores[maxJogadores];
        int numJogadores = 0; // Número de jogadores lidos
        setJogadores(jogadores); // Definir o array de jogadores na classe Jogadores
        Hash tabela = new Hash();

        for (int i = 0; i < 3922; i++) {
            String j = Integer.toString(i);
            Jogadores jogador = ler(caminhoDoArquivoCSV, j, jogadores, numJogadores);

            jogadores[numJogadores] = jogador;
            numJogadores++; // Incrementa o índice do próximo jogador após a leitura
        }

        while (!(qualId = scanner.nextLine()).equals("FIM")) {
            int inden = Integer.parseInt(qualId);

            for (int i = 0; i < 3922; i++) {
                if (jogadores[i] != null && jogadores[i].id == inden) {
                    tabela.inserir(jogadores[i]);
                    i = 3922;
                }
            }
        }

        String pubin;
        while (!(pubin = scanner.nextLine()).equals("FIM")) {

            tabela.pesquisar(pubin);
        }
        scanner.close();

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        String filename = "802736_hashReserva.txt.";

        try {
            FileWriter outputFile = new FileWriter(filename);
            outputFile.write("802736" + "\t");
            outputFile.write(totalTime + "\t");
            outputFile.write(tabela.totalComparacoes + "\t");
            outputFile.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

class Hash {
    Jogadores tabela[];
    int m1, m2, m, reserva;
    final Jogadores NULO = null;
    int totalComparacoes;

    public Hash() {
        this(21, 9);
    }

    public Hash(int m1, int m2) {
        this.m1 = m1;
        this.m2 = m2;
        this.m = m1 + m2;
        this.tabela = new Jogadores[this.m];
        reserva = 0;
        totalComparacoes = 0;
    }

    public int hInserir(Jogadores elemento, int tentativa) {
        int hash = elemento.getNome().hashCode();
        return (Math.abs(hash) + tentativa) % m1;
    }

    public int hPesquisar(String elemento, int tentativa) {
        int hash = elemento.hashCode();
        return (Math.abs(hash) + tentativa) % m1;
    }

    public boolean inserir(Jogadores elemento) {
        if (elemento == NULO || reserva >= m2) {
            return false; // Não é possível inserir mais elementos na reserva
        }

        int tentativa = 0;
        int pos = hInserir(elemento, tentativa);

        while (tabela[pos] != NULO) {
            tentativa++;
            pos = hInserir(elemento, tentativa);
        }

        tabela[pos] = elemento;
        reserva++;
        return true;
    }

    public boolean pesquisar(String pubin) {
        int tentativa = 0;
        int pos = hPesquisar(pubin, tentativa);

        while (tabela[pos] != NULO) {
            totalComparacoes++;
            if (tabela[pos].getNome().equals(pubin)) {
                System.out.println(pubin + " SIM");
                return true;
            }
            tentativa++;
            pos = hPesquisar(pubin, tentativa);
        }
        System.out.println(pubin + " NAO");
        return false;
    }
}
