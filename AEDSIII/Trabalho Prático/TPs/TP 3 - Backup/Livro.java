import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.text.NumberFormat;

public class Livro implements Registro {

  private int ID;
  private String isbn;
  private String titulo;
  private String autor;
  private float preco;

  public Livro() {
    this(-1, "", "", "", 0F);
  }

  public Livro(String i, String t, String a, float p) {
    this(-1, i, t, a, p);
  }

  public Livro(int id, String i, String t, String a, float p) {
    this.ID = id;
    this.isbn = i;
    this.titulo = t;
    this.autor = a;
    this.preco = p;
  }

  public int getID() {
    return this.ID;
  }

  public void setID(int id) {
    this.ID = id;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public String getIsbn() {
    return isbn;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  public float getPreco() {
    return preco;
  }

  public void setPreco(float preco) {
    this.preco = preco;
  }

  public String getAutor() {
    return autor;
  }

  public void setAutor(String autor) {
    this.autor = autor;
  }

  public byte[] toByteArray() throws Exception {
    ByteArrayOutputStream ba_out = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(ba_out);
    dos.writeInt(this.ID);
    dos.writeUTF(this.isbn);
    dos.writeUTF(this.titulo);
    dos.writeUTF(this.autor);
    dos.writeFloat(this.preco);
    return ba_out.toByteArray();
  }

  public void fromByteArray(byte[] ba) throws Exception {
    ByteArrayInputStream ba_in = new ByteArrayInputStream(ba);
    DataInputStream dis = new DataInputStream(ba_in);

    this.ID = dis.readInt();
    this.isbn = dis.readUTF();
    this.titulo = dis.readUTF();
    this.autor = dis.readUTF();
    this.preco = dis.readFloat();
  }

  public String toString() {
    return "ID: " + this.ID +
        "\nISBN: " + this.isbn +
        "\nTítulo: " + this.titulo +
        "\nAutor: " + this.autor +
        "\nPreço: " + NumberFormat.getCurrencyInstance().format(this.preco);
  }

  

}
