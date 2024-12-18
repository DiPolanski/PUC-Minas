import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;

public class Arquivo<T extends Registro> {

  private static int TAM_CABECALHO = 4;
  RandomAccessFile arq;
  String nomeArquivo = "";
  Constructor<T> construtor;

  public Arquivo(String na, Constructor<T> c) throws Exception {
    this.nomeArquivo = na;
    this.construtor = c;
    arq = new RandomAccessFile(na, "rw");
    if (arq.length() < TAM_CABECALHO) {
      arq.seek(0);
      arq.writeInt(0);
    }
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
    return obj.getID();

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
