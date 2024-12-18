
public interface Registro {

  public void setID(int id);

  public int getID();

  public String getTitulo();

  public String getAutor();


  public byte[] toByteArray() throws Exception;

  public void fromByteArray(byte[] ba) throws Exception;

}
