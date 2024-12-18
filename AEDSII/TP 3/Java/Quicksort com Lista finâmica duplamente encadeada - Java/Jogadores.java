import java.util.Scanner;
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
    public static int totalComparacoes = 0;
    public static int totalMovimentacoes = 0;
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
        ListaSeq L = new ListaSeq();

        setJogadores(jogadores); // Definir o array de jogadores na classe Jogadores

        while (!(qualId = scanner.nextLine()).equals("FIM")) {
            Jogadores jogador = ler(caminhoDoArquivoCSV, qualId, jogadores, numJogadores); // Passa numJogadores como
                                                                                           // argumento
            L.InserirFinal(jogador);
            numJogadores++; // Incrementa o índice do próximo jogador após a leitura
        }
        L.quickSort(L.primeiro.prox, L.ultimo, totalMovimentacoes, totalMovimentacoes);
        L.Mostrar();
        scanner.close();

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        String filename = "802736_quicksort2.txt.";

        try {
            FileWriter outputFile = new FileWriter(filename);
            outputFile.write("802736\t");
            outputFile.write(totalComparacoes + "\t");
            outputFile.write(totalMovimentacoes + "\t");
            outputFile.write(totalTime + "\t");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ListaSeq {
    Celula primeiro;
    Celula ultimo;

    ListaSeq() {
        primeiro = new Celula();
        ultimo = primeiro;
    }

    public int Tamanho() {
        int tamanho = 0;
        for (Celula i = primeiro; i != ultimo; i = i.prox, tamanho++)
            ;
        return tamanho;
    }

    public void InserirFinal(Jogadores jogador) {
        ultimo.prox = new Celula(jogador);
        ultimo.prox.ant = ultimo;
        ultimo = ultimo.prox;
    }

    void quickSort(Celula esq, Celula dir, int totalMovimentacoes, int totalComparacoes) {
        if (esq != null && dir != null && esq != dir && esq != dir.prox) {
            Celula p = partition(esq, dir, totalMovimentacoes, totalComparacoes);
            quickSort(esq, p.ant, totalMovimentacoes, totalMovimentacoes);
            quickSort(p.prox, dir, totalMovimentacoes, totalComparacoes);
        }
    }

    Celula partition(Celula esq, Celula dir, int totalMovimentacoes, int totalComparacoes) {
        Jogadores pivo = dir.jogador;
        Celula i = esq.ant;

        for (Celula j = esq; j != dir; j = j.prox) {
            int cmp = j.jogador.getEstadoNascimento().compareTo(pivo.getEstadoNascimento());
            if (cmp < 0 || (cmp == 0 && j.jogador.getNome().compareTo(pivo.getNome()) <= 0)) {
                i = (i == null) ? esq : i.prox;
                Jogadores temp = i.jogador;
                i.jogador = j.jogador;
                j.jogador = temp;
            }
            totalComparacoes += 2;
        }
        i = (i == null) ? esq : i.prox;
        Jogadores temp = i.jogador;
        i.jogador = dir.jogador;
        dir.jogador = temp;
        totalMovimentacoes += 3;
        return i;
    }

    void Mostrar() {

        for (Celula i = primeiro.prox; i != null; i = i.prox) {
            System.out.println("[" + i.jogador.getId() + " ## " + i.jogador.getNome() + " ## " + i.jogador.getAltura()
                    + " ## " + i.jogador.getPeso() + " ## " + i.jogador.getAnoNascimento() + " ## "
                    + i.jogador.getUniversidade() + " ## " + i.jogador.getCidadeNascimento() + " ## "
                    + i.jogador.getEstadoNascimento() + "]");
        }
    }

    class Celula {
        Jogadores jogador;
        Celula prox, ant;

        Celula() {
            this(null);
        }

        Celula(Jogadores x) {
            this.jogador = x;
            this.prox = this.ant = null;
        }
    }
}