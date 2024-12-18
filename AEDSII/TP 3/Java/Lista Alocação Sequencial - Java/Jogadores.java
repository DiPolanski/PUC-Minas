import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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

        int qntRegistros = scanner.nextInt();

        for (int i = 0; i < qntRegistros + 1; i++) {
            String entrada = scanner.nextLine();

            String[] itens = entrada.split(" ");
            if (itens.length == 1) {
                String comando = itens[0];

                if (comando.equals("RI")) {
                    L.RemoverInicio();
                } else if (comando.equals("RF")) {
                    L.RemoverFim();
                }
            } else if (itens.length == 2) {
                String comando = itens[0];
                String identificador = itens[1];
                int iden = Integer.parseInt(identificador);

                Jogadores jogador = ler(caminhoDoArquivoCSV, identificador, jogadores, numJogadores);

                if (comando.equals("II")) {
                    L.InserirInicio(jogador);
                } else if (comando.equals("IF")) {
                    L.InserirFinal(jogador);
                } else if (comando.equals("R*")) {
                    L.Remover(iden);
                }
            } else {
                String comando = itens[0];
                String posicao = itens[1];
                String identificador = itens[2];

                int pos = Integer.parseInt(posicao);
                int iden = Integer.parseInt(identificador);

                Jogadores jogador = ler(caminhoDoArquivoCSV, identificador, jogadores, numJogadores);

                if (comando.equals("I*")) {
                    L.Inserir(jogador, pos);
                }

            }
        }
        L.Mostrar();
        scanner.close();
    }
}

class ListaSeq {
    Celula primeiro;
    Celula ultimo;

    ListaSeq() {
        ultimo = primeiro = new Celula();
    }

    public int Tamanho() {
        int tamanho = 0;
        for (Celula i = primeiro; i != ultimo; i = i.prox, tamanho++)
            ;
        return tamanho;
    }

    void InserirInicio(Jogadores jogador) throws Exception {
        Celula tmp = new Celula(jogador);

        tmp.prox = primeiro.prox;
        primeiro.prox = tmp;

        if (primeiro == ultimo) {
            ultimo = tmp;
        }
        tmp = null;
    }

    void InserirFinal(Jogadores jogador) throws Exception {
        ultimo.prox = new Celula(jogador);
        ultimo = ultimo.prox;
    }

    void Inserir(Jogadores jogador, int pos) throws Exception {
        int tamanho = Tamanho();
        if (pos < 0 || pos > tamanho) {
            throw new Exception();
        } else if (pos == 0) {
            InserirInicio(jogador);
        } else if (pos == tamanho) {
            InserirFinal(jogador);
        } else {

            Celula tmp = new Celula(jogador);

            Celula i = primeiro;
            for (int j = 0; j < pos; j++) {
                i = i.prox;
            }

            tmp.prox = i.prox;
            i.prox = tmp;

            i = tmp = null;
        }

    }

    Jogadores RemoverInicio() throws Exception {
        if (primeiro == ultimo) {
            System.out.println("ERRO");
        }
        Celula i = primeiro.prox;
        Jogadores lixo = i.jogador;

        primeiro = i.prox;
        i = i.prox = null;

        return lixo;
    }

    Jogadores RemoverFim() throws Exception {
        if (primeiro == ultimo) {
            System.out.println("ERRO");
        }
        Celula i;
        for (i = primeiro; i.prox != ultimo; i = i.prox)
            ;

        Jogadores lixo = ultimo.jogador;
        ultimo = i;
        i = ultimo.prox = null;

        System.out.println("(R) " + lixo.getNome());
        return lixo;
    }

    Jogadores Remover(int pos) throws Exception {
        Jogadores elemento;
        int tamanho = Tamanho();
        if (primeiro == ultimo || pos < 0 || pos >= tamanho) {
            throw new Exception();
        } else if (pos == 0) {
            elemento = RemoverInicio();
        } else if (pos == tamanho - 1) {
            elemento = RemoverFim();
        } else {
            Celula j = primeiro;

            for (int i = 0; i < pos; i++, j = j.prox)
                ;

            Celula tmp = j.prox;
            elemento = tmp.jogador;

            j.prox = tmp.prox;
            tmp.prox = null;
            j = tmp = null;

        }
        return elemento;
    }

    void Mostrar() {
        for (Celula i = primeiro.prox; i != null; i = i.prox) {
            System.out.println("[" + i + "]" + " ## " + i.jogador.getNome() + " ## " + i.jogador.getAltura()
                    + " ## " + i.jogador.getPeso() + " ## " + i.jogador.getAnoNascimento() + " ## "
                    + i.jogador.getUniversidade() + " ## " + i.jogador.getCidadeNascimento() + " ## "
                    + i.jogador.getEstadoNascimento() + " ##");
        }
    }
}

class Celula {
    Jogadores jogador;
    Celula prox;

    Celula() {
        this.jogador = null;
        this.prox = null;
    }

    Celula(Jogadores x) {
        jogador = x;
        this.prox = null;
    }

}