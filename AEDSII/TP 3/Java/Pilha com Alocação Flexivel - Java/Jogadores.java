import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

class Jogador {
    private int id;
    private String nome;
    private int altura;
    private int peso;
    private String universidade;
    private int anoNascimento;
    private String cidadeNascimento;
    private String estadoNascimento;

    public Jogador(int id, String nome, int altura, int peso, String universidade, int anoNascimento,
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

    // setters e getters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public String getUniversidade() {
        return universidade;
    }

    public void setUniversidade(String universidade) {
        this.universidade = universidade;
    }

    public int getAnoNascimento() {
        return anoNascimento;
    }

    public void setAnoNascimento(int anoNascimento) {
        this.anoNascimento = anoNascimento;
    }

    public String getCidadeNascimento() {
        return cidadeNascimento;
    }

    public void setCidadeNascimento(String cidadeNascimento) {
        this.cidadeNascimento = cidadeNascimento;
    }

    public String getEstadoNascimento() {
        return estadoNascimento;
    }

    public void setEstadoNascimento(String estadoNascimento) {
        this.estadoNascimento = estadoNascimento;
    }

    public String toString() {
        return " ## " + this.nome + " ## " + this.altura + " ## " + this.peso + " ## "
                + this.anoNascimento + " ## " + this.universidade + " ## " + this.cidadeNascimento + " ## "
                + this.estadoNascimento + " ##";
    }

    public Jogador clone() {
        Jogador clone = new Jogador(id, nome, altura, peso, universidade, anoNascimento, cidadeNascimento,
                estadoNascimento);

        clone.id = this.id;
        clone.nome = this.nome;
        clone.altura = this.altura;
        clone.peso = this.peso;
        clone.universidade = this.universidade;
        clone.anoNascimento = this.anoNascimento;
        clone.cidadeNascimento = this.cidadeNascimento;
        clone.estadoNascimento = this.estadoNascimento;

        return clone;
    }

    public static Jogador[] lerDadosDoArquivo(String nomeArquivo) {
        Jogador[] arrayDeJogadores = new Jogador[4000];
        int index = 0;

        try (BufferedReader file = new BufferedReader(new InputStreamReader(new FileInputStream(nomeArquivo)))) {
            String linhaString;
            file.readLine(); // Ignorar o cabeçalho

            while ((linhaString = file.readLine()) != null) {
                arrayDeJogadores[index] = new Jogador(linhaString);
                index++;
            }
        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
        }

        return arrayDeJogadores;
    }

    public Jogador(String linhaString) {
        String[] atributos = linhaString.split(",", -1); // -1 para incluir campos vazios

        for (int i = 0; i < atributos.length; i++) {
            if (atributos[i].equals("")) {
                atributos[i] = "nao informado";
            }
        }

        this.id = Integer.parseInt(atributos[0]);
        this.nome = atributos[1];
        this.altura = Integer.parseInt(atributos[2]);
        this.peso = Integer.parseInt(atributos[3]);
        this.universidade = atributos[4];
        this.anoNascimento = Integer.parseInt(atributos[5]);
        this.cidadeNascimento = atributos[6];
        this.estadoNascimento = atributos[7];
    }

    public static Jogador buscarPorId(int id, Jogador[] jogadores) {
        for (int i = 0; i < jogadores.length; i++) {
            if (jogadores[i] != null && jogadores[i].getId() == id) {
                return jogadores[i];
            }
        }
        return null; // Retorna null se o jogador com o ID especificado não for encontrado.
    }

}

class Celula {
    public Jogador elemento;
    public Celula prox;

    public Celula() {
        this(0);
    }

    public Celula(Jogador elemento) {
        this.elemento = elemento;
        this.prox = null;
    }

    public Celula(int i) {
    }
}

class Pilha {
    private Celula topo;

    public Pilha() {
        topo = null;
    }

    public void inserir(Jogador x) {
        Celula tmp = new Celula(x);
        tmp.prox = topo;
        topo = tmp;
        tmp = null;
    }

    public Jogador remover() throws Exception {
        if (topo == null) {
            throw new Exception("Erro ao remover!");
        }
        Jogador resp = topo.elemento;
        Celula tmp = topo;
        topo = topo.prox;
        tmp.prox = null;
        tmp = null;
        return resp;
    }

    public int tamanho() {
        int tamanho = 0;
        for (Celula i = topo; i != null; i = i.prox) {
            tamanho++;
        }
        return tamanho;
    }

    public void mostrar() {
        int tam = 0;
        Celula[] elementos = new Celula[tamanho()];

        for (Celula i = topo; i != null; i = i.prox, tam++) {
            elementos[tam] = i;
        }

        // Imprime os elementos ao contrário
        for (int i = tam - 1; i >= 0; i--) {
            System.out.print("[" + (tam - i - 1) + "]" + elementos[i].elemento + "\n");
        }
    }

}

public class Jogadores {
    public static void main(String[] args) throws Exception {
        Pilha pilha = new Pilha();

        List<Jogador> jogadoresRemovidos = new ArrayList<>();

        Jogador[] jogadores = Jogador.lerDadosDoArquivo("/tmp/players.csv");

        Scanner scanner = new Scanner(System.in);
        int id;

        while (true) {
            String entrada1 = scanner.nextLine();
            if (entrada1.equals("FIM")) {
                break;
            }
            id = Integer.parseInt(entrada1);
            Jogador jogadorPesquisado = Jogador.buscarPorId(id, jogadores);

            if (jogadorPesquisado != null) {
                pilha.inserir(jogadorPesquisado);
            } else {
                System.out.println("ID " + id + " não encontrado.");
            }
        }

        int quantidade = scanner.nextInt();
        scanner.nextLine();
        String entrada2;

        for (int i = 0; i < quantidade; i++) {
            entrada2 = scanner.nextLine();

            String comando = entrada2.substring(0, 1);
            String[] splitted = entrada2.split(" ");

            if (comando.equals("I")) { // inserir
                String num = splitted[1];
                id = Integer.parseInt(num);
                Jogador jogadorPesquisado = Jogador.buscarPorId(id, jogadores);
                if (jogadorPesquisado != null) {
                    pilha.inserir(jogadorPesquisado);
                } else {
                    System.out.println("ID " + id + " não encontrado.");
                }

            } else { // remover
                Jogador jogadorRemovido = pilha.remover();
                jogadoresRemovidos.add(jogadorRemovido);
            }

        }
        // nomes dos jogadores removidos
        for (Jogador jogador : jogadoresRemovidos) {
            System.out.println("(R) " + jogador.getNome());
        }

        pilha.mostrar();
        scanner.close();
    }
}
