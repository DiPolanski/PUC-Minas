import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

class Principal {

  static Scanner scanner = new Scanner(System.in);
  static Arquivo<Livro> arqLivros;
  static ListaInvertida listaTitulo;
  static ListaInvertida listaAutor;

  public static void main(String args[]) {
    try {
      arqLivros = new Arquivo<>("dados/livros.db", Livro.class.getConstructor());
      listaTitulo = new ListaInvertida(4, "dados/dicionario.listaTitulo.db",
          "dados/blocos.listaTitulo.db");
      listaAutor = new ListaInvertida(4, "dados/dicionario.listaAutor.db", "dados/blocos.listaAutor.db");

      int opcao = 0;

      while (opcao != 8) {
        mostrarMenu();
        try {
          opcao = scanner.nextInt();
          scanner.nextLine(); // Consume newline
          switch (opcao) {
            case 1:
              criarLivro();
              break;
            case 2:
              lerLivro();
              break;
            case 3:
              deletarLivro();
              break;
            case 4:
              atualizarLivro();
              break;
            case 5:
              mostrarLista();
              break;
            case 6:
              fazerBackup();
              break;
            case 7:
              recuperarBackup();
              break;
            case 8:
              System.out.println("Saindo...");
              break;
            default:
              System.out.println("Opção inválida, tente novamente.");
          }
        } catch (InputMismatchException e) {
          System.out.println("Entrada inválida, por favor tente novamente.");
          scanner.nextLine(); // Limpar o buffer do Scanner
        }
      }
      arqLivros.close();

    } catch (Exception e) {
      System.out.println("Erro: " + e.getMessage());
      e.printStackTrace();
    }
  }

  public static void mostrarMenu() {
    System.out.println("\n--------------------------------");
    System.out.println("             MENU               ");
    System.out.println("--------------------------------");
    System.out.println("1. Criar");
    System.out.println("2. Ler");
    System.out.println("3. Deletar");
    System.out.println("4. Atualizar");
    System.out.println("5. Mostrar a lista");
    System.out.println("6. Fazer Backup");
    System.out.println("7. Recuperar Backup");
    System.out.println("8. Sair");
    System.out.println("Digite uma opção:");
  }

  public static void criarLivro() {
    System.out.println("\n--------------------------------");
    System.out.println("              Criar             ");
    System.out.println("--------------------------------");

    System.out.println("Digite o ISBN do livro:");
    String isbn = scanner.next();

    scanner.nextLine(); // Consome a nova linha

    System.out.println("Digite o título do livro:");
    String titulo = scanner.nextLine();

    System.out.println("Digite o nome do autor do livro:");
    String autor = scanner.nextLine();

    System.out.println("Digite o preço do livro:");
    System.out.println("(Separe a casa decimal por vírgula)");

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

    try {
      Livro l = new Livro(-1, isbn, titulo, autor, preco);
      int id = arqLivros.create(l);
      listaTitulo.create(titulo.toLowerCase(), id);
      listaAutor.create(autor.toLowerCase(), id);
    } catch (Exception e) {
      System.out.println("Erro ao criar livro: " + e.getMessage());
    }
  }

  public static void lerLivro() {
    System.out.println("\n--------------------------------");
    System.out.println("               Ler              ");
    System.out.println("--------------------------------");
    System.out.println("1. Pesquisar pelo título.");
    System.out.println("2. Pesquisar pelo autor.");
    int opcao2 = scanner.nextInt();
    scanner.nextLine(); // Consome a nova linha

    try {
      if (opcao2 == 1) {
        System.out.println("Digite o título:");
        String titulo = scanner.nextLine();
        arqLivros.readListaTitulo(titulo.toLowerCase());
      } else if (opcao2 == 2) {
        System.out.println("Digite o autor:");
        String autor = scanner.nextLine();
        arqLivros.readListaAutor(autor.toLowerCase());
      } else {
        System.out.println("Opção inválida, tente novamente.");
      }
    } catch (Exception e) {
      System.out.println("Erro ao ler livro: " + e.getMessage());
    }
  }

  public static void deletarLivro() {
    System.out.println("\n--------------------------------");
    System.out.println("            Deletar             ");
    System.out.println("--------------------------------");
    System.out.println("Qual o id que deseja deletar?");
    int id = scanner.nextInt();

    try {
      Livro l = arqLivros.read(id);
      if (l != null) {
        String titulo = l.getTitulo();
        String autor = l.getAutor();
        listaTitulo.delete(titulo.toLowerCase(), id);
        listaAutor.delete(autor.toLowerCase(), id);
        arqLivros.delete(id);
      } else {
        System.out.println("Livro não encontrado.");
      }
    } catch (Exception e) {
      System.out.println("Erro ao deletar livro: " + e.getMessage());
    }
  }

  public static void atualizarLivro() {
    System.out.println("Qual o id do livro que deseja atualizar?");
    int id = scanner.nextInt();

    System.out.println("\n--------------------------------");
    System.out.println("           Atualizar            ");
    System.out.println("--------------------------------");
    System.out.println("1. Título");
    System.out.println("2. Autor");
    System.out.println("3. Preço");
    System.out.println("O que deseja atualizar?");

    int opcao2 = scanner.nextInt();
    scanner.nextLine(); // Consome a nova linha

    try {
      Livro l = arqLivros.read(id);
      if (l != null) {
        switch (opcao2) {
          case 1:
            System.out.println("Qual o novo título do livro?");
            String titulo = scanner.nextLine();
            String tituloAntigo = l.getTitulo();
            listaTitulo.delete(tituloAntigo.toLowerCase(), l.getID());
            l.setTitulo(titulo);
            listaTitulo.create(titulo.toLowerCase(), id);
            arqLivros.update(l);
            break;
          case 2:
            System.out.println("Qual o novo autor do livro?");
            String autor = scanner.nextLine();
            String autorAntigo = l.getAutor();
            listaAutor.delete(autorAntigo.toLowerCase(), l.getID());
            l.setAutor(autor);
            listaAutor.create(autor.toLowerCase(), id);
            arqLivros.update(l);
            break;
          case 3:
            float preco;
            System.out.println("Qual o novo preço do livro?");
            System.out.println("(Separe a casa decimal por vírgula)");
            while (true) {
              try {
                preco = scanner.nextFloat();
                break;
              } catch (InputMismatchException e) {
                System.out.println("Por favor, insira um preço válido.");
                scanner.nextLine();
              }
            }
            l.setPreco(preco);
            arqLivros.update(l);
            break;
          default:
            System.out.println("Tente novamente, valor inválido.");
        }
      } else {
        System.out.println("Livro não encontrado.");
      }
    } catch (Exception e) {
      System.out.println("Erro ao atualizar livro: " + e.getMessage());
    }
  }

  public static void mostrarLista() {
    System.out.println("\n--------------------------------");
    System.out.println("           Mostrar              ");
    System.out.println("--------------------------------");
    System.out.println("1. Lista de Títulos");
    System.out.println("2. Lista de Autores");

    int opcao2 = scanner.nextInt();
    scanner.nextLine(); // Consome a nova linha

    try {
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
      } else {
        System.out.println("Opção inválida, tente novamente.");
      }
    } catch (Exception e) {
      System.out.println("Erro ao mostrar lista: " + e.getMessage());
    }
  }

  public static void fazerBackup() {
    System.out.println("\n------------------------------------");
    System.out.println("             Fazendo Backup           ");
    System.out.println("--------------------------------------");
    try {
      String versaoBackUp = Backup.getVersaoBackUp();
      arqLivros.FazerBackup(versaoBackUp);
      String nomeArq = versaoBackUp.replace("/", "_");
      nomeArq = nomeArq.replace(":", "_");
      System.out.println("Backup feito com sucesso.");
      System.out.println("Nome do arquivo: " + nomeArq);
    } catch (Exception e) {
      System.out.println("Erro ao fazer backup: " + e.getMessage());
    }
  }

  public static void recuperarBackup() {
    System.out.println("\n------------------------------------");
    System.out.println("             Recuperar Backup           ");
    System.out.println("--------------------------------------");

    System.out.println("Lista de backups:");
    System.out.println("dia_mês_ano hora_minuto");
    Backup.ListarBackUps();
    System.out.println("Qual versão do backup?");
    String nomeArq = scanner.nextLine();
    try {
      byte[] dados = arqLivros.RecuperarBackUp(nomeArq);
      byte[] dadosDecodificados = LZW.decodifica(dados);

      System.out.println("Backup " + nomeArq + " recuperado com sucesso");
      // Aqui você pode fazer o que desejar com os dados recuperados
    } catch (Exception e) {
      System.out.println("Erro ao recuperar backup: " + e.getMessage());
    }
  }
}
