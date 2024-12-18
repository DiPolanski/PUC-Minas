#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <sys/time.h>

// Definicao da estrutura Jogador
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

void Heapify(Jogador arr[], int n, int i, int comp, int mov)
{
    int maior = i;
    int esquerda = 2 * i + 1;
    int direita = 2 * i + 2;

    if (esquerda < n && (arr[esquerda].altura > arr[maior].altura ||
                          (arr[esquerda].altura == arr[maior].altura &&
                            strcmp(arr[esquerda].nome, arr[maior].nome) > 0))){
        comp++;
        maior = esquerda;
    }
    if (direita < n && (arr[direita].altura > arr[maior].altura ||
                        (arr[direita].altura == arr[maior].altura &&
                         strcmp(arr[direita].nome, arr[maior].nome) > 0))){
        comp++;
        maior = direita;
    }
    if (maior != i)
    {
        Jogador temp = arr[i];
        arr[i] = arr[maior];
        arr[maior] = temp;
        comp++;
        mov+=3;
        Heapify(arr, n, maior, comp, mov);
    }
}

void HeapSort(Jogador jogadores[], int n, int comp, int mov, int k)
{
    for (int i = n / 2 - 1; i >= 0; i--)
        Heapify(jogadores, n, i, comp, mov);

    for (int i = n - 1; i >= 0; i--)
    {
        Jogador temp = jogadores[0];
        jogadores[0] = jogadores[i];
        jogadores[i] = temp;
        mov+=3;
        Heapify(jogadores, i, 0, comp, mov);
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

    Jogador jogadores[4500];
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


    int comparacoes = 0; // Contador de comparacoes
    int movimentacoes = 0;
    long tempo_micros; // Variavel para armazenar o tempo em microssegundos

        gettimeofday(&inicio, NULL); // Inicio da medicao de tempo

        HeapSort(jogadores, numJogadores, comparacoes, movimentacoes, 10);
    for (int i = 0; i < 10; i++)
        {
            ImprimirJogador(jogadores[i]);
        }

        gettimeofday(&fim, NULL); // Fim da medicao de tempo

        tempo_micros = (fim.tv_sec - inicio.tv_sec) * 1000000 + (fim.tv_usec - inicio.tv_usec);

    FILE *outputFile = fopen("802736_selecaoRecursiva.txt", "w"); // Abre o arquivo de sa�da para escrita
    fprintf(outputFile, "802736/t");
    fprintf(outputFile, "%d/t", comparacoes);
    fprintf(outputFile, "%d/t", movimentacoes);
    fprintf(outputFile, "%ld/t", tempo_micros);
    fclose(arquivo);
    return 0;
}
