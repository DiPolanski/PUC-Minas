import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Arquivo<T extends Registro> {
  String[] stopWords = { "de", "a", "o", "que", "e", "do", "da", "em", "um", "para", "é", "com", "não", "uma", "os",
      "no", "se", "na", "por", "mais", "as", "dos", "como", "mas", "foi", "ao", "ele", "das", "tem", "à", "seu", "sua",
      "ou", "ser", "quando", "muito", "há", "nos", "já", "está", "eu", "também", "só", "pelo", "pela", "até", "isso",
      "ela", "entre", "era", "depois", "sem", "mesmo", "aos", "ter", "seus", "quem", "nas", "me", "esse", "eles",
      "estão", "você", "tinha", "foram", "essa", "num", "nem", "suas", "meu", "às", "minha", "têm", "numa", "pelos",
      "elas", "havia", "seja", "qual", "será", "nós", "tenho", "lhe", "deles", "essas", "esses", "pelas", "este",
      "fosse", "dele", "tu", "te", "vocês", "vos", "lhes", "meus", "minhas", "teu", "tua", "teus", "tuas", "nosso",
      "nossa", "nossos", "nossas", "dela", "delas", "esta", "estes", "estas", "aquele", "aquela", "aqueles", "aquelas",
      "isto", "aquilo", "estou", "está", "estamos", "estão", "estive", "esteve", "estivemos", "estiveram", "estava",
      "estávamos", "estavam", "estivera", "estivéramos", "esteja", "estejamos", "estejam", "estivesse", "estivéssemos",
      "estivessem", "estiver", "estivermos", "estiverem", "hei", "há", "havemos", "hão", "houve", "houvemos",
      "houveram", "houvera", "houvéramos", "haja", "hajamos", "hajam", "houvesse", "houvéssemos", "houvessem", "houver",
      "houvermos", "houverem", "houverei", "houverá", "houveremos", "houverão", "houveria", "houveríamos", "houveriam",
      "sou", "somos", "são", "era", "éramos", "eram", "fui", "foi", "fomos", "foram", "fora", "fôramos", "seja",
      "sejamos", "sejam", "fosse", "fôssemos", "fossem", "for", "formos", "forem", "serei", "será", "seremos", "serão",
      "seria", "seríamos", "seriam", "tenho", "tem", "temos", "tém", "tinha", "tínhamos", "tinham", "tive", "teve",
      "tivemos", "tiveram", "tivera", "tivéramos", "tenha", "tenhamos", "tenham", "tivesse", "tivéssemos", "tivessem",
      "tiver", "tivermos", "tiverem", "terei", "terá", "teremos", "terão", "teria", "teríamos", "teriam" };
  private static int TAM_CABECALHO = 4;
  RandomAccessFile arq;
  String nomeArquivo = "";
  Constructor<T> construtor;

  ListaInvertida listaTitulo = new ListaInvertida(4, "dados/dicionario.listaTitulo.db", "dados/blocos.listaTitulo.db");
  ListaInvertida listaAutor = new ListaInvertida(4, "dados/dicionario.listaAutor.db", "dados/blocos.listaAutor.db");

  public Arquivo(String na, Constructor<T> c) throws Exception {
    this.nomeArquivo = na;
    this.construtor = c;
    arq = new RandomAccessFile(na, "rw");
    if (arq.length() < TAM_CABECALHO) {
      arq.seek(0);
      arq.writeInt(0);
    }
  }

  public String[] SplitString(String titulo) {

    String[] stringDividida = titulo.split(" ");

    return stringDividida;
  }

  public int create(T obj) throws Exception {
    arq.seek(0);
    int ultimoID = arq.readInt();
    ultimoID++;
    arq.seek(0);
    arq.writeInt(ultimoID);
    obj.setID(ultimoID);

    byte[] novoObjb = obj.toByteArray(); // transforma o novo registro em bytes
    short tamNovoReg = (short) novoObjb.length;

    arq.seek(TAM_CABECALHO);
    while (arq.getFilePointer() < arq.length()) { // Procura um lápide registro apagado
      long enderecoApagado = arq.getFilePointer(); // Salva o endereço do possível registro deletado
      byte lapideApagado = arq.readByte();
      short tamApagado = arq.readShort();

      if (lapideApagado == '*' && tamApagado == tamNovoReg) { // Confere se o registro apagado é do mesmo tamanho que o
                                                              // novo registro
        arq.seek(enderecoApagado);
        arq.write(' ');
        arq.writeShort(tamNovoReg);
        arq.write(novoObjb);

        System.out.println("Registro criado com o ID: " + obj.getID());
        return obj.getID();
      }
      arq.skipBytes(tamApagado);
    }

    arq.seek(arq.length());
    arq.write(' ');
    arq.writeShort(tamNovoReg);
    arq.write(novoObjb);

    System.out.println("Registro criado com o ID: " + obj.getID());

    String[] tituloSeparado = SplitString(obj.getTitulo().toLowerCase()); // Separa as palavras do titulo
    String[] autorSeparado = SplitString(obj.getAutor().toLowerCase());// Separa as palavras do autor

    for (int i = 0; i < tituloSeparado.length; i++) {
      boolean isStopWord = false;
      // Verifica se a palavra atual é uma stop word
      for (String stopWord : stopWords) {
        if (tituloSeparado[i].equalsIgnoreCase(stopWord)) {
          isStopWord = true;
          break;
        }
      }
      if (!isStopWord) {
        listaTitulo.create(tituloSeparado[i], obj.getID()); // Cria o título na lista (minúscula)
      }

    }

    for (int i = 0; i < autorSeparado.length; i++) {
      listaAutor.create(autorSeparado[i], obj.getID()); // Cria o autor na lista (minuscula)
    }

    return obj.getID();

  }

  public void readListaTitulo(String titulo) throws Exception {
    try {
      String[] tituloSeparado = SplitString(titulo);

      int[] id = ArrayDeTitulos(tituloSeparado);

      System.out.println("IDs encontrados para o título " + titulo + ": " + Arrays.toString(id));
      for (int i = 0; i < id.length; i++) {
        System.out.println(read(id[i]).toString());
      }
    } catch (Exception e) {
      System.err.println("Ocorreu um erro ao ler os detalhes do título: " + e.getMessage());
      // Trate a exceção conforme necessário
    }
  }

  public void readListaAutor(String autor) throws Exception {
    try {
      String[] autorSeparado = SplitString(autor);

      int[] id = ArrayDeAutores(autorSeparado);

      System.out.println("IDs encontrados para o autor " + autor + ": " + Arrays.toString(id));
      for (int i = 0; i < id.length; i++) {
        System.out.println(read(id[i]).toString());
      }
    } catch (Exception e) {
      System.err.println("Ocorreu um erro ao ler os detalhes do título: " + e.getMessage());
      // Trate a exceção conforme necessário
    }
  }

  // Retorna o array com os titulos da pesquisa
  public int[] ArrayDeTitulos(String[] titulo) throws Exception {
    ArrayList<Integer> listaIDs = new ArrayList<>();

    // Itera sobre os títulos para coletar os IDs
    for (int i = 0; i < titulo.length; i++) {
      int[] ids = listaTitulo.read(titulo[i]);

      for (int j = 0; j < ids.length; j++) {
        int id = ids[j];
        listaIDs.add(id);
      }
    }

    Set<Integer> conjunto = new HashSet<>(listaIDs);

    listaIDs.clear();
    listaIDs.addAll(conjunto);

    // Converte a lista de IDs em um array de inteiros
    int[] resultado = new int[listaIDs.size()];
    for (int i = 0; i < listaIDs.size(); i++) {
      resultado[i] = listaIDs.get(i);
    }

    return resultado;
  }

  // Retorna o array com os autores da pesquisa
  public int[] ArrayDeAutores(String[] autores) throws Exception {
    ArrayList<Integer> listaIDs = new ArrayList<>();

    // Itera sobre os títulos para coletar os IDs
    for (int i = 0; i < autores.length; i++) {
      int[] ids = listaAutor.read(autores[i]);

      for (int j = 0; j < ids.length; j++) {
        int id = ids[j];
        listaIDs.add(id);
      }
    }

    Set<Integer> conjunto = new HashSet<>(listaIDs);

    listaIDs.clear();
    listaIDs.addAll(conjunto);

    // Converte a lista de IDs em um array de inteiros
    int[] resultado = new int[listaIDs.size()];
    for (int i = 0; i < listaIDs.size(); i++) {
      resultado[i] = listaIDs.get(i);
    }

    return resultado;
  }

  public T read(int id) throws Exception {
    T l = construtor.newInstance();
    byte[] ba;
    short tam;
    byte lapide;

    arq.seek(TAM_CABECALHO);
    while (arq.getFilePointer() < arq.length()) {

      lapide = arq.readByte();
      tam = arq.readShort();
      if (lapide == ' ') {
        ba = new byte[tam];
        arq.read(ba);
        l.fromByteArray(ba);
        if (l.getID() == id)
          return l;
      } else {
        arq.skipBytes(tam);
      }
    }
    return null;
  }

  public boolean delete(int id) throws Exception {
    T l = construtor.newInstance();
    byte[] ba;
    short tam;
    byte lapide;
    long endereco;

    arq.seek(TAM_CABECALHO);
    while (arq.getFilePointer() < arq.length()) {

      endereco = arq.getFilePointer();
      lapide = arq.readByte();
      tam = arq.readShort();
      if (lapide == ' ') {
        ba = new byte[tam];
        arq.read(ba);
        l.fromByteArray(ba);
        if (l.getID() == id) {
          arq.seek(endereco);
          arq.writeByte('*');
          System.out.println("Registro deletado com o ID:" + id);
          return true;
        }
      } else {
        arq.skipBytes(tam);
      }
    }
    System.out.println("Registro não deletado");
    return false;
  }

  public boolean update(T objAlterado) throws Exception {
    T l = construtor.newInstance();
    byte[] ba;
    short tam;
    byte lapide;
    long endereco;

    arq.seek(TAM_CABECALHO);
    while (arq.getFilePointer() < arq.length()) {

      endereco = arq.getFilePointer();
      lapide = arq.readByte();
      tam = arq.readShort();
      if (lapide == ' ') {

        ba = new byte[tam];
        arq.read(ba);
        l.fromByteArray(ba);

        if (l.getID() == objAlterado.getID()) {
          byte[] ba2 = objAlterado.toByteArray();
          short tam2 = (short) ba2.length;

          if (tam2 == tam) {
            arq.seek(endereco + 1 + 2);
            arq.write(ba2);
          } else {
            arq.seek(TAM_CABECALHO);
            while (arq.getFilePointer() < arq.length()) {
              long enderecoApagado = arq.getFilePointer();
              byte lapideApagado = arq.readByte();
              short tamApagado = arq.readShort();

              if (tamApagado == tam2 && lapideApagado == '*') {
                System.out.println("teste");
                arq.seek(endereco);
                arq.write('*');
                arq.seek(enderecoApagado);
                arq.write(' ');
                arq.write(tam2);
                arq.write(ba2);

                System.out.println("Registro atualizado com sucesso");
                return true;
              } else {
                arq.skipBytes(tamApagado);
              }
            }
            arq.seek(endereco);
            arq.write('*');
            arq.seek(arq.length());
            arq.write(' ');
            arq.writeShort(tam2);
            arq.write(ba2);
          }

          System.out.println("Registro atualizado com sucesso");

          return true;
        }
      } else {
        arq.skipBytes(tam);
      }
    }
    System.out.println("Erro ao atualizar o registro");
    return false;
  }

  public void close() throws Exception {
    arq.close();
  }
}
