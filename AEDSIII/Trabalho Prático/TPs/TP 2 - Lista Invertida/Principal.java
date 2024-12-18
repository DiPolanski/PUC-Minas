import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

class Principal {

  public static void main(String args[]) {
    Scanner scanner = new Scanner(System.in);

    File f = new File("dados/livros.db");

    Arquivo<Livro> arqLivros;

    try {
      arqLivros = new Arquivo<>("dados/livros.db", Livro.class.getConstructor());
      ListaInvertida listaTitulo = new ListaInvertida(4, "dados/dicionario.listaTitulo.db",
          "dados/blocos.listaTitulo.db");
      ListaInvertida listaAutor = new ListaInvertida(4, "dados/dicionario.listaAutor.db", "dados/blocos.listaAutor.db");

      int opcao = 0;

      while (opcao != 6) {
        System.out.println("\n--------------------------------");
        System.out.println("             MENU               ");
        System.out.println("--------------------------------");
        System.out.println("1. Criar");
        System.out.println("2. Ler");
        System.out.println("3. Deletar");
        System.out.println("4. Atualizar");
        System.out.println("5. Mostrar a lista");
        System.out.println("6. Sair");
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
          System.out.println("1. Pesquisar pelo título.");
          System.out.println("2. Pesquisar pelo autor.");
          int opcao2 = scanner.nextInt();

          if (opcao2 == 1) {
            System.out.println("Digite o título:");
            scanner.nextLine();
            String titulo = scanner.nextLine(); // Ler o título
            arqLivros.readListaTitulo(titulo.toLowerCase());// Faz a leitura do livro (minuscula)

          } else if (opcao2 == 2) {
            System.out.println("Digite o autor:");
            scanner.nextLine();
            String autor = scanner.nextLine(); // Ler o autor
            arqLivros.readListaAutor(autor.toLowerCase()); // Faz a leitura do autor (minuscula)

          } else {
            System.out.println("Opção inválida, tente novamente.");
          }

        } else if (opcao == 3) { // Delete
          System.out.println("\n--------------------------------");
          System.out.println("            Deletatar           ");
          System.out.println("--------------------------------");
          System.out.println("Qual o id que deseja deletar?");
          int id = scanner.nextInt();

          Livro l = arqLivros.read(id);// Acha o livro de acordo com ID
          String titulo = l.getTitulo(); // Coloca um novo título ao registro
          String autor = l.getAutor(); // Coloca um novo título ao registro

          listaTitulo.delete(titulo.toLowerCase(), id);
          listaAutor.delete(autor.toLowerCase(), id);
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

          if (opcao2 == 1) {// Atualizar titulo
            System.out.println("Qual o novo título do livro?");
            scanner.nextLine();
            String titulo = scanner.nextLine();
            Livro l = arqLivros.read(id); // Procura o registro a ser atualizado
            if (l != null) {
              String tituloAntigo = l.getTitulo();

              listaTitulo.delete(tituloAntigo.toLowerCase(), l.getID());// Deleta o antigo título
              l.setTitulo(titulo); // Atualiza o objeto
              listaTitulo.create(titulo.toLowerCase(), id); // Cria o novo titulo
            } else {
              System.out.println("Erro ao atualizar");
            }
            arqLivros.update(l); // Atualiza o registro
          } else if (opcao2 == 3) { // Atualizar preço
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

          } else if (opcao2 == 2) {// Atualizar autor
            System.out.println("Qual o novo autor do livro?");
            scanner.nextLine();
            String autor = scanner.nextLine();
            Livro l = arqLivros.read(id); // Procura o registro a ser atualizado
            if (l != null) {
              String autorAntigo = l.getAutor();

              listaAutor.delete(autorAntigo.toLowerCase(), l.getID());// Deleta o antigo autor
              l.setAutor(autor); // Atualiza o objeto
              listaAutor.create(autor.toLowerCase(), id); // Cria o novo autor
            } else {
              System.out.println("Erro ao atualizar");
            }
            arqLivros.update(l); // Atualiza o registro
          } else {
            System.out.println("Tente novamente valor inválido.");
          }
        } else if (opcao == 5) { // Mostrar as listas
          System.out.println("\n--------------------------------");
          System.out.println("           Mostrar           ");
          System.out.println("--------------------------------");
          System.out.println("1. Lista de Título");
          System.out.println("2. Lista de Autor");

          int opcao2 = scanner.nextInt();

          if (opcao2 == 1) {
            System.out.println("\n-------------------------------");
            System.out.println("         Lista de Títulos         ");
            System.out.println("----------------------------------");
            listaTitulo.print();
          } else if (opcao2 == 2) {
            System.out.println("\n--------------------------------");
            System.out.println("         Lista de Autores         ");
            System.out.println("----------------------------------");
            listaAutor.print();
          }

        }
      }
      arqLivros.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}