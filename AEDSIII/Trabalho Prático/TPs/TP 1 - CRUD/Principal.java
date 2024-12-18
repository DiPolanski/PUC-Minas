import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

class Principal {

  public static void main(String args[]) {
    Scanner scanner = new Scanner(System.in);

    File f = new File("dados/livros.db");
    f.delete();

    Arquivo<Livro> arqLivros;

    try {
      arqLivros = new Arquivo<>("livros.db", Livro.class.getConstructor());
      int opcao = 0;

      while (opcao != 5) {
        System.out.println("\n--------------------------------");
        System.out.println("             MENU               ");
        System.out.println("--------------------------------");
        System.out.println("1. Criar");
        System.out.println("2. Ler");
        System.out.println("3. Deletar");
        System.out.println("4. Atualizar");
        System.out.println("5. Sair");
        System.out.println("Digite uma opção:");

        opcao = scanner.nextInt();

        // Create
        if (opcao == 1) {
          System.out.println("\n--------------------------------");
          System.out.println("              Criar             ");
          System.out.println("--------------------------------");

          System.out.println("Digite o ISBN do livro:");
          String isbn = scanner.next();

          scanner.nextLine();

          System.out.println("Digite o título do livro:");
          String titulo = scanner.nextLine();

          System.out.println("Digite o nome do autor do livro:");
          String autor = scanner.nextLine();

          System.out.println("Digite o preço do livro:");
          System.out.println("(Separe a casa deciamal por vírgula)");

          float preco;
          while (true) {
            try {
              preco = scanner.nextFloat();
              break; // Se a entrada for válida, saia do loop
            } catch (InputMismatchException e) {
              System.out.println("Por favor, insira um preço válido.");
              scanner.nextLine(); // Limpar o buffer do Scanner
            }
          }
          Livro l = new Livro(-1, isbn, titulo, autor, preco);
          arqLivros.create(l);
        } else if (opcao == 2) { // Read
          System.out.println("\n--------------------------------");
          System.out.println("               Ler              ");
          System.out.println("--------------------------------");
          System.out.println("Qual ID deseja ler?");
          int id = scanner.nextInt();

          Livro l = arqLivros.read(id);
          if (l != null) { // Verifica se o ID é null
            System.out.println(l.toString()); // Printa o resultado
          } else {
            System.out.println("Registro não encontrado");
          }
        } else if (opcao == 3) { // Delete
          System.out.println("\n--------------------------------");
          System.out.println("            Deletatar           ");
          System.out.println("--------------------------------");
          System.out.println("Qual ID deseja deletar?");
          int id = scanner.nextInt();

          arqLivros.delete(id); // Deleta o ID
        } else if (opcao == 4) { // Uptade
          int opcao2;

          System.out.println("Qual o id do livro que deseja atualizar?");
          int id = scanner.nextInt();

          System.out.println("\n--------------------------------");
          System.out.println("           Atualizar            ");
          System.out.println("--------------------------------");
          System.out.println("1. Título");
          System.out.println("2. Autor");
          System.out.println("3. Preço");
          System.out.println("O que deseja atualizar?");

          opcao2 = scanner.nextInt();

          if (opcao2 == 1) {
            System.out.println("Qual o novo título do livro?");
            scanner.nextLine();
            String titulo = scanner.nextLine();
            Livro l = arqLivros.read(id); // Procura o registro a ser atualizado
            if (l != null) {
              l.setTitulo(titulo); // Atualiza o objeto
            } else {
              System.out.println("Erro ao atualizar");
            }
            arqLivros.update(l); // Atualiza o registro
          } else if (opcao2 == 3) {
            float preco;

            System.out.println("Qual o novo preço do livro?");
            System.out.println("(Separe a casa deciamal por vírgula)");
            while (true) {
              try {
                preco = scanner.nextFloat();
                break; // Se a entrada for válida, saia do loop
              } catch (InputMismatchException e) {
                System.out.println("Por favor, insira um preço válido.");
                scanner.nextLine(); // Limpar o buffer do Scanner
              }
            }
            Livro l = arqLivros.read(id);
            if (l != null) {
              l.setPreco(preco);
            } else {
              System.out.println("Erro ao atualizar");
            }
            arqLivros.update(l);
          } else if (opcao2 == 2) {
            System.out.println("Qual o autor do livro?");
            scanner.nextLine();
            String autor = scanner.nextLine();
            Livro l = arqLivros.read(id); // Procura o registro a ser atualizado
            if (l != null) {
              l.setAutor(autor); // Atualiza o objeto
            } else {
              System.out.println("Erro ao atualizar");
            }
            arqLivros.update(l); // Atualiza o registro
          } else {
            System.out.println("Tente novamente valor inválido.");
          }
        }
      }
      arqLivros.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
  /*
   * Dicas de teste:
   * Create Livro 1 - ISBN: 100
   * Titulo: AEDS
   * Preço: 99,99;
   * 
   * Create Livro 2 - ISBN: 101
   * Título: Calculo
   * Preço: 49,90;
   * 
   * Delete Livro 2;
   * 
   * Create Livro 3 - ISBN: 105
   * Titulo: Contado
   * Preço: 50,00;
   * 
   * Uptade Livro 3 - Titulo: Banco de Dados;
   * 
   * Create Livro 4 - ISBN: 155
   * Titulo: Abacaxi
   * Preço: 80,00;
   * 
   * Uptade Livro 1 - Preço: 44,44;
   * 
   * Uptade Livro 1 - Titulo: Amor;
   * 
   * Uptade Livro 4 - Titulo: Dente;
   * 
   * Create Livro 5 - ISBN: 199
   * Titulo: New York City
   * Preço: 89,99;
   */
}