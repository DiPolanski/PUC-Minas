#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <sys/time.h>

// Definição da estrutura Jogador
typedef struct Jogador
{
    int id;
    int altura;
    int peso;
    int anoNascimento;
    char nome[100];
    char universidade[100];
    char cidadeNasc[100];
    char estadoNasc[100];

} Jogador;

void ImprimirJogador(Jogador jogador)
{
    printf("[%d ## %s ## %d ## %d ## %d ## %s ## %s ## %s]\n",
           jogador.id, jogador.nome, jogador.altura, jogador.peso,
           jogador.anoNascimento, jogador.universidade,
           jogador.cidadeNasc, jogador.estadoNasc);
}

void LerAtributosDoArquivo(FILE *arquivo, char *atributos, Jogador *jogador)
{
    fscanf(arquivo, "%[^,\n]", atributos);
    fgetc(arquivo);
    strcpy(jogador->nome, atributos);

    fscanf(arquivo, "%[^,\n]", atributos);
    fgetc(arquivo);
    jogador->altura = atoi(atributos);

    fscanf(arquivo, "%[^,\n]", atributos);
    fgetc(arquivo);
    jogador->peso = atoi(atributos);

    fscanf(arquivo, "%[^,\n]", atributos);
    fgetc(arquivo);
    strcpy(jogador->universidade, (atoi(atributos) == jogador->peso) ? "nao informado" : atributos);

    fscanf(arquivo, "%[^,\n]", atributos);
    fgetc(arquivo);
    jogador->anoNascimento = atoi(atributos);

    fscanf(arquivo, "%[^,\n]", atributos);
    fgetc(arquivo);
    strcpy(jogador->cidadeNasc, (atoi(atributos) == jogador->anoNascimento) ? "nao informado" : atributos);

    fscanf(arquivo, "%[^,\n]", atributos);
    fgetc(arquivo);
    strcpy(jogador->estadoNasc, (atoi(atributos) == jogador->anoNascimento) ? "nao informado" : atributos);
}

int compararPorNome(const void *a, const void *b)
{
    return strcmp(((Jogador *)a)->nome, ((Jogador *)b)->nome);
}

Jogador* pesquisaBinariaRecursiva(Jogador jogadores[], int inicio, int fim, const char* nome, int* totalComparacoes) {
    if (inicio > fim) {
        totalComparacoes++;
        return NULL; // O elemento não foi encontrado
    }

    int meio = (inicio + fim) / 2;
    int cmp = strcmp(jogadores[meio].nome, nome);

    if (cmp == 0) {
        totalComparacoes++;
        return &jogadores[meio];
    } else if (cmp < 0) {
        totalComparacoes++;
        return pesquisaBinariaRecursiva(jogadores, meio + 1, fim, nome, totalComparacoes);
    } else {
        totalComparacoes++;
        return pesquisaBinariaRecursiva(jogadores, inicio, meio - 1, nome, totalComparacoes);
    }
}


int main()
{
    struct timeval inicio, fim;
    const char nomeArquivo[] = "/tmp/players.csv";

    FILE *arquivo = fopen(nomeArquivo, "r");

    if (arquivo == NULL)
    {
        printf("Arquivo nao encontrado: %s\n", nomeArquivo);
        return 1;
    }

    char entradaUsuario[10];

    Jogador jogadores[1000]; // Suponha que haja no máximo 1000 jogadores
    int numJogadores = 0;

    while (1)
    {
        scanf(" %[^\n]", entradaUsuario);

        if (strcmp(entradaUsuario, "FIM") == 0)
            break;

        Jogador jogador;

        char linhaAuxiliar[200];
        char idAuxiliar[6];
        rewind(arquivo);
        fgets(linhaAuxiliar, 200, arquivo);

        while (fscanf(arquivo, "%[^,]", idAuxiliar) != 0)
        {
            fgetc(arquivo);
            if (strcmp(idAuxiliar, entradaUsuario) == 0)
            {
                jogador.id = atoi(idAuxiliar);
                LerAtributosDoArquivo(arquivo, linhaAuxiliar, &jogador);
                jogadores[numJogadores] = jogador;
                numJogadores++;
                break;
            }
            else
                fgets(linhaAuxiliar, 200, arquivo);
        }
    }

    fclose(arquivo);

    // Agora, o array "jogadores" contém os jogadores inseridos pelo usuário

    // Ordenar o array de jogadores por nome para fazer pesquisa binária
    qsort(jogadores, numJogadores, sizeof(Jogador), compararPorNome);

    int comparacoes = 0; // Contador de comparações


    long tempo_micros; // Variável para armazenar o tempo em microssegundos


    while (1) {
        char nomePesquisado[100];
        scanf(" %[^\n]", nomePesquisado);

        if (strcmp(nomePesquisado, "FIM") == 0)
            break;

        gettimeofday(&inicio, NULL); // Início da medição de tempo

        int comparacoes = 0; // Contador de comparações
        Jogador *resultado = pesquisaBinariaRecursiva(jogadores, 0, numJogadores - 1, nomePesquisado, &comparacoes);

        gettimeofday(&fim, NULL); // Fim da medição de tempo

        tempo_micros = (fim.tv_sec - inicio.tv_sec) * 1000000 + (fim.tv_usec - inicio.tv_usec);

        if (resultado != NULL) {
            printf("SIM\n");
        } else {
            printf("NAO\n");
        }

    }
    FILE *outputFile = fopen("802736_binaria.txt", "w"); // Abre o arquivo de saída para escrita
    fprintf(outputFile, "802736/t");
    fprintf(outputFile, "%d/t", comparacoes);
    fprintf(outputFile, "%ld/t", tempo_micros);
    fclose(outputFile);
    return 0;
}
