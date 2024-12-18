#include <stdio.h>
#include <string.h>
#include <stdlib.h>

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

void LerAtributosDoArquivo(FILE *arquivo, char *atributos, Jogador jogador)
{
    fscanf(arquivo, "%[^,\n]", atributos);
    fgetc(arquivo);
    strcpy(jogador.nome, atributos);

    fscanf(arquivo, "%[^,\n]", atributos);
    fgetc(arquivo);
    jogador.altura = atoi(atributos);

    fscanf(arquivo, "%[^,\n]", atributos);
    fgetc(arquivo);
    jogador.peso = atoi(atributos);

    fscanf(arquivo, "%[^,\n]", atributos);
    fgetc(arquivo);
    strcpy(jogador.universidade, (atoi(atributos) == jogador.peso) ? "nao informado" : atributos);

    fscanf(arquivo, "%[^,\n]", atributos);
    fgetc(arquivo);
    jogador.anoNascimento = atoi(atributos);

    fscanf(arquivo, "%[^,\n]", atributos);
    fgetc(arquivo);
    strcpy(jogador.cidadeNasc, (atoi(atributos) == jogador.anoNascimento) ? "nao informado" : atributos);

    fscanf(arquivo, "%[^,\n]", atributos);
    fgetc(arquivo);
    strcpy(jogador.estadoNasc, (atoi(atributos) == jogador.anoNascimento) ? "nao informado" : atributos);

    ImprimirJogador(jogador);
}

int main()
{
    const char nomeArquivo[] = "/tmp/players.csv";

    FILE *arquivo = fopen(nomeArquivo, "r");

    if (arquivo == NULL)
    {
        printf("Arquivo nao encontrado: %s\n", nomeArquivo);
        return 1;
    }

    char entradaUsuario[10];

    while (1)
    {
        scanf(" %[^\n]", entradaUsuario);

        if (strcmp(entradaUsuario, "FIM") == 0)
            break;

        Jogador jogador;

        char linhaAuxiliar[200];
        char idAuxiliar[6];
        rewind(arquivo);
        fgets(linhaAuxiliar, 80, arquivo);

        while (fscanf(arquivo, "%[^,]", idAuxiliar) != 0)
        {
            fgetc(arquivo);
            if (strcmp(idAuxiliar, entradaUsuario) == 0)
            {
                jogador.id = atoi(idAuxiliar);
                LerAtributosDoArquivo(arquivo, linhaAuxiliar, jogador);
                break;
            }
            else
                fgets(linhaAuxiliar, 200, arquivo);
        }
    }

    fclose(arquivo);

    return 0;
}
