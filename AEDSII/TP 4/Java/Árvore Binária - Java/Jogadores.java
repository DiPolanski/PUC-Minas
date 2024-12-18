import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

class Jogador {
    private int id;
    private String nome;
    private int altura;
    private int peso;
    private String universidade;
    private int anoNascimento;
    private String cidadeNascimento;
    private String estadoNascimento;

    public Jogador() {
    }

    public Jogador(String linha) {
        String campos[] = linha.split(",");
        this.id = Integer.parseInt(campos[0]);
        this.nome = campos[1];
        this.altura = Integer.parseInt(campos[2]);
        this.peso = Integer.parseInt(campos[3]);
        this.universidade = (campos[4].isEmpty()) ? "nao informado" : campos[4];
        this.anoNascimento = Integer.parseInt(campos[5]);
        if (campos.length > 6) {
            this.cidadeNascimento = (campos[6].isEmpty()) ? "nao informado" : campos[6];
            if (campos.length < 8) {
                this.estadoNascimento = "nao informado";
            } else {
                this.estadoNascimento = campos[7];
            }
        } else {
            this.cidadeNascimento = "nao informado";
            this.estadoNascimento = "nao informado";
        }
    }

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

    public void setAnoNascimento(int anoNascimento) {
        this.anoNascimento = anoNascimento;
    }

    public int getAnoNascimento() {
        return anoNascimento;
    }

    public String getUniversidade() {
        return universidade;
    }

    public void setUniversidade(String universidade) {
        this.universidade = universidade;
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

    public void clone(Jogador J) {

        this.setId(J.getId());
        this.setCidadeNascimento(J.getCidadeNascimento());
        this.setEstadoNascimento(J.getEstadoNascimento());
        this.setNome(J.getNome());
        this.setAltura(J.getAltura());
        this.setPeso(J.getPeso());
        this.setAnoNascimento(J.getAnoNascimento());
        this.setUniversidade(J.getUniversidade());

    }

    public String toString() {
        String str = "[" + getId() + " ## " + getNome() + " ## " + getAltura() + " ## " + getPeso() + " ## "
                + getAnoNascimento()
                + " ## " + getUniversidade() + " ## " + getCidadeNascimento() + " ## " + getEstadoNascimento() + "]";
        return str;
    }
}

class No {
    public Jogador elemento;
    public No esq, dir;

    public No(Jogador elemento) {
        this(elemento, null, null);
    }

    public No(Jogador elemento, No esq, No dir) {
        this.elemento = elemento;
        this.esq = esq;
        this.dir = dir;
    }
}

class ArvoreBinaria {
    private No raiz;
    int totalComparacoes = 0;

    public ArvoreBinaria() {
        raiz = null;
    }

    public boolean pesquisar(String x) {
        System.out.print(x + " raiz ");
        return pesquisar(x, raiz);
    }

    private boolean pesquisar(String x, No i) {
        boolean resp;
        if (i == null) {
            resp = false;

        } else if (x.compareTo(i.elemento.getNome()) == 0) {
            resp = true;

        } else if (x.compareTo(i.elemento.getNome()) < 0) {
            System.out.print("esq ");
            resp = pesquisar(x, i.esq);

        } else {
            System.out.print("dir ");
            resp = pesquisar(x, i.dir);
        }
        return resp;
    }

    public boolean pesquisarSemPrint(String x) {
        return pesquisarSem(x, raiz);
    }

    private boolean pesquisarSem(String x, No i) {
        boolean resp;
        if (i == null) {
            totalComparacoes++; // Incrementa a contagem de comparações
            resp = false;

        } else if (x.compareTo(i.elemento.getNome()) == 0) {
            totalComparacoes++; // Incrementa a contagem de comparações
            resp = true;

        } else if (x.compareTo(i.elemento.getNome()) < 0) {
            totalComparacoes++; // Incrementa a contagem de comparações
            resp = pesquisarSem(x, i.esq);

        } else {
            totalComparacoes++; // Incrementa a contagem de comparações
            resp = pesquisarSem(x, i.dir);
        }
        return resp;
    }

    public void caminharPre() {
        caminharPre(raiz);
    }

    private void caminharPre(No i) {
        if (i != null) {
            System.out.println(i.elemento.toString() + " ");
            caminharPre(i.esq);
            caminharPre(i.dir);
        }
    }

    public void inserir(Jogador x) throws Exception {
        raiz = inserir(x, raiz);
    }

    private No inserir(Jogador x, No i) throws Exception {
        if (i == null) {
            i = new No(x);

        } else if (x.getNome().compareTo(i.elemento.getNome()) < 0) {
            totalComparacoes++; // Incrementa a contagem de comparações
            i.esq = inserir(x, i.esq);

        } else if (x.getNome().compareTo(i.elemento.getNome()) > 0) {
            totalComparacoes++; // Incrementa a contagem de comparações
            i.dir = inserir(x, i.dir);

        } else {
            throw new Exception("Erro ao inserir!");
        }

        return i;
    }

    public Jogador getRaiz() throws Exception {
        return raiz.elemento;
    }

    public static boolean igual(ArvoreBinaria a1, ArvoreBinaria a2) {
        return igual(a1.raiz, a2.raiz);
    }

    private static boolean igual(No i1, No i2) {
        boolean resp;
        if (i1 != null && i2 != null) {
            resp = (i1.elemento == i2.elemento) && igual(i1.esq, i2.esq) && igual(i1.dir, i2.dir);
        } else if (i1 == null && i2 == null) {
            resp = true;
        } else {
            resp = false;
        }
        return resp;
    }
}

public class Jogadores {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        Scanner scanner = new Scanner(System.in);
        String[] entrada = new String[1000];
        int numEntrada = 0;

        do {
            entrada[numEntrada] = scanner.nextLine();
        } while (isFim(entrada[numEntrada++]) == false);
        numEntrada--;

        int ids[] = new int[numEntrada];

        for (int i = 0; i < numEntrada; i++) {
            ids[i] = Integer.parseInt(entrada[i]);
        }

        String[] entrada2 = new String[4000];

        try {
            entrada2 = ler();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        ArvoreBinaria arvore = new ArvoreBinaria();

        for (int i = 0; i < numEntrada; i++) {
            Jogador j = new Jogador(entrada2[ids[i]]);
            try {
                if (arvore.pesquisarSemPrint(j.getNome()) == false)
                    arvore.inserir(j);
            } catch (Exception e) {
                System.out.println("impossivel");
            }
        }

        numEntrada = 0;
        do {
            entrada[numEntrada] = scanner.nextLine();
        } while (isFim(entrada[numEntrada++]) == false);
        numEntrada--;

        for (int i = 0; i < numEntrada; i++) {
            if (arvore.pesquisar(entrada[i]))
                System.out.println("SIM");
            else
                System.out.println("NAO");
        }

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        String filename = "802736_arvoreBinaria.txt.";

        try {
            FileWriter outputFile = new FileWriter(filename);
            outputFile.write("802736\t");
            outputFile.write(arvore.totalComparacoes + "\t");
            outputFile.write(totalTime + "\t");
            outputFile.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        
    }

    public static boolean isFim(String s) {
        return (s.length() >= 3 && s.charAt(0) == 'F' && s.charAt(1) == 'I' && s.charAt(2) == 'M');
    }

    public static String[] ler() throws Exception {

        String[] entrada = new String[4000];
        int numEntrada = 0;
        File file = new File("players.csv");

        BufferedReader br = new BufferedReader(new FileReader(file));
        String lixo = br.readLine();
        do {
            entrada[numEntrada] = br.readLine();
        } while (entrada[numEntrada++] != null);
        numEntrada--;

        br.close();
        return entrada;
    }
}