#include <stdio.h>
#include <stdbool.h>
#include <string.h>
#include <time.h>
#include <stdlib.h>
#define MAX_ATTRIBUTES 8
#define MAX_LEN 100

typedef struct Jogador
{
    int id;
    char nome[50];
    int altura;
    int peso;
    char universidade[100];
    int anoNascimento;
    char cidadeNascimento[50];
    char estadoNascimento[50];

} Jogador;

void imprimir(Jogador jogadores)
{
    printf(" ## %s ## %d ## %d ## %d ## %s ## %s ## %s ##\n",
           jogadores.nome,
           jogadores.altura,
           jogadores.peso,
           jogadores.anoNascimento,
           jogadores.universidade,
           jogadores.cidadeNascimento,
           jogadores.estadoNascimento);
}

typedef struct Split
{
    char linha[MAX_ATTRIBUTES][MAX_LEN];
} Split;

void split(char linha[], char substrings[8][100])
{
    int qtSubstrings = 0;
    int cS = 0;
    int c = 0;

    for (int i = 0; i < 8; i++)
    {
        for (int j = 0; j < 100; j++)
        {
            substrings[i][j] = '\0';
        }
    }

    while (linha[c] != '\0')
    {
        if (linha[c] != ',')
        {
            while (linha[c] != ',' && linha[c] != '\0')
            {
                if (linha[c] == '\n')
                    c++;

                else
                {
                    substrings[qtSubstrings][cS] = linha[c];
                    c++;
                    cS++;
                }
            }
            cS = 0;
            qtSubstrings++;
        }
        else
        {
            if (linha[c + 1] == ',' || linha[c + 1] == '\n' || linha[c + 1] == '\0')
            {
                strcpy(substrings[qtSubstrings], "nao informado");
                qtSubstrings++;
            }
            c++;
        }
    }
}

void leitura(Jogador jogadores[], FILE *file)
{
    char linha[200];
    int qtJogadores = -1;

    while (fgets(linha, sizeof(linha), file) != NULL)
    {
        char substrings[8][100];
        if (qtJogadores >= 0)
        {
            split(linha, substrings);
            int ID = atoi(substrings[0]);
            int h = atoi(substrings[2]);
            int p = atoi(substrings[3]);
            int ano = atoi(substrings[5]);

            jogadores[qtJogadores].id = ID;
            strcpy(jogadores[qtJogadores].nome, substrings[1]);
            jogadores[qtJogadores].altura = h;
            jogadores[qtJogadores].peso = p;
            strcpy(jogadores[qtJogadores].universidade, substrings[4]);
            jogadores[qtJogadores].anoNascimento = ano;
            strcpy(jogadores[qtJogadores].cidadeNascimento, substrings[6]);
            strcpy(jogadores[qtJogadores].estadoNascimento, substrings[7]);
            qtJogadores++;
        }
        else
            qtJogadores++;
    }
}

Split SplitSpace()
{
    Split Split;

    for (int i = 0; i < 3; i++)
    {
        scanf("%[^ \n]", Split.linha[i]);
        if (getchar() == '\n')
            i = 3;
    }

    return Split;
}

typedef struct Celula
{
    Jogador jogador;
    struct Celula *low;
} Celula;

Celula *novaCelula(Jogador elemento)
{
    Celula *nova = (Celula *)malloc(sizeof(Celula));
    nova->jogador = elemento;
    nova->low = NULL;

    return nova;
}

typedef struct Pilha
{
    Celula *ultimo;
    int size;

    void (*Inserir)(struct Pilha *, Jogador x);

    Jogador (*Remover)(struct Pilha *);

    void (*Mostrar)(struct Pilha);

    void (*Close)(struct Pilha *);

} Pilha;

void Inserirfimpilha(Pilha *pilha, Jogador x)
{
    Celula *tmp = novaCelula(x);
    tmp->low = pilha->ultimo;
    pilha->ultimo = tmp;
    pilha->size++;
}

Jogador Removerfimpilha(Pilha *pilha)
{
    if (pilha->ultimo == NULL)
    {
        printf("Erro ao remover!");
        exit(1);
    }

    Celula *tmp = pilha->ultimo;
    Jogador removido = tmp->jogador;
    pilha->ultimo = pilha->ultimo->low;

    tmp->low = NULL;
    free(tmp);
    tmp = NULL;

    pilha->size--;

    return removido;
}

void mostrapilha(int size, Celula *ultimo)
{
    if (ultimo->low != NULL)
    {
        mostrapilha(size - 1, ultimo->low);
    }

    printf("[%d]", size);
    imprimir(ultimo->jogador);
}

void MostrarPilha(Pilha pilha)
{
    if (pilha.size == 0)
    {
        printf("Pilha Vazia");
        exit(1);
    }

    mostrapilha(pilha.size - 1, pilha.ultimo);
}

void ClosePilha(Pilha *Pilha)
{
    Celula *i = Pilha->ultimo;
    while (i != NULL)
    {
        i = i->low;
    }
}

Pilha newPilha()
{
    Pilha Pilha;

    Pilha.size = 0;
    Pilha.ultimo = NULL;

    Pilha.Inserir = Inserirfimpilha;
    Pilha.Remover = Removerfimpilha;
    Pilha.Mostrar = MostrarPilha;
    Pilha.Close = ClosePilha;

    return Pilha;
}

void docomandoo(Pilha *pilha, Jogador jogadores[])
{
    Split split = SplitSpace();

    if (strcmp(split.linha[0], "I") == 0)
    {
        int valor = atoi(split.linha[1]);
        pilha->Inserir(pilha, jogadores[valor]);
    }

    if (strcmp(split.linha[0], "R") == 0)
    {
        Jogador jogador = pilha->Remover(pilha);
        printf("(R) %s\n", jogador.nome);
    }
}

int main()
{
    char id[50];
    Jogador jogadores[3922];
    Pilha Pilha = newPilha();

    FILE *file = fopen("/tmp/players.csv", "r");
    do
    {
        scanf("%s", id);
        if (strcmp(id, "FIM") != 0 && strcmp(id, "fim") != 0)
        {
            int identificador = atoi(id);
            leitura(jogadores, file);
            Pilha.Inserir(&Pilha, jogadores[identificador]);
        }
    } while ((strcmp(id, "FIM") != 0) && (strcmp(id, "fim") != 0));
    fclose(file);

    int action;
    scanf("%i", &action);

    for (int i = 0; i <= action; i++)
    {
        docomandoo(&Pilha, jogadores);
    }

    Pilha.Mostrar(Pilha);
    Pilha.Close(&Pilha);
}
