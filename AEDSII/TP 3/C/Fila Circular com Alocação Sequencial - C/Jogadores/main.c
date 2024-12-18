#include <stdio.h>
#include <stdbool.h>
#include <string.h>
#include <stdlib.h>

#define MAX_ATTRIBUTES 8
#define MAX_LEN 100
#define MAXTAM 6

typedef struct Jogador {
    int id;
    char nome[50];
    int altura;
    int peso;
    char universidade[100];
    int anoNascimento;
    char cidadeNascimento[50];
    char estadoNascimento[50];
} Jogador;

typedef struct Split {
    char linha[MAX_ATTRIBUTES][MAX_LEN];
} Split;

void split(char linha[], char substrings[8][100]) {
    int qtSubstrings = 0, cS = 0, c = 0;

    for (int i = 0; i < 8; i++) {
        for (int j = 0; j < 100; j++) {
            substrings[i][j] = '\0';
        }
    }

    while (linha[c] != '\0') {
        if (linha[c] != ',') {
            while (linha[c] != ',' && linha[c] != '\0') {
                if (linha[c] == '\n') c++;
                else {
                    substrings[qtSubstrings][cS] = linha[c];
                    c++;
                    cS++;
                }
            }
            cS = 0;
            qtSubstrings++;
        } else {
            if (linha[c + 1] == ',' || linha[c + 1] == '\n' || linha[c + 1] == '\0') {
                strcpy(substrings[qtSubstrings], "nao informado");
                qtSubstrings++;
            }
            c++;
        }
    }
}

Split SplitSpace() {
    Split Split;

    for (int i = 0; i < 3; i++) {
        scanf("%[^ \n]", Split.linha[i]);
        if (getchar() == '\n') i = 3;
    }

    return Split;
}

void leitura(Jogador jogadores[], FILE *file) {
    char linha[200];
    int qtJogadores = -1;

    while (fgets(linha, sizeof(linha), file) != NULL) {
        char substrings[8][100];
        if (qtJogadores >= 0) {
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
        } else
            qtJogadores++;
    }
}

void imprimir(Jogador jogadores) {
    printf(" ## %s ## %d ## %d ## %d ## %s ## %s ## %s ##\n",
           jogadores.nome,
           jogadores.altura,
           jogadores.peso,
           jogadores.anoNascimento,
           jogadores.universidade,
           jogadores.cidadeNascimento,
           jogadores.estadoNascimento);
}

typedef struct Fila {
    Jogador *array;
    int inicio, fim, size;

    void (*InserirFim)(struct Fila *, Jogador x);

    Jogador (*RemoverInicio)(struct Fila *);

    void (*Mostrar)(struct Fila);

    float (*getMediaAlturas)(struct Fila);

    void (*Close)(struct Fila *);

} Fila;

float GetMediaAlturasFilaCircular(Fila fila) {
    float media = 0;
    int jogador = fila.inicio;
    for (int i = 0; i < fila.size; i++) {
        media += fila.array[jogador].altura;
        jogador = (jogador + 1) % MAXTAM;
    }
    return media / fila.size;
}

void InserefFilaCircular(Fila *fila, Jogador x) {
    if ((fila->fim + 1) % MAXTAM == fila->inicio) {
        fila->RemoverInicio(fila);
    }

    fila->size++;
    fila->array[fila->fim] = x;
    fila->fim = (fila->fim + 1) % MAXTAM;

    printf("%.f\n", fila->getMediaAlturas(*fila));
}

Jogador RemoverInicioFilaCircular(Fila *fila) {
    if (fila->inicio == fila->fim) {
        printf("Erro ao remover!");
        exit(1);
    }

    Jogador removido = fila->array[fila->inicio];
    fila->size--;
    fila->inicio = (fila->inicio + 1) % MAXTAM;

    return removido;
}

void MostrafilaCircular(Fila fila) {
    for (int i = fila.size, jogador = fila.inicio, j = 0; i > 0; i--, j++) {
        printf("[%i]", j);
        imprimir(fila.array[i]);
        jogador = (jogador + 1) % MAXTAM;
    }
}

void CloseFilaCircular(Fila *fila) {
    free(fila->array);
}

Fila newFila() {
    Fila fila;
    fila.fim = fila.inicio = fila.size = 0;
    fila.array = (Jogador *)malloc((MAXTAM + 1) * sizeof(Jogador));
    fila.getMediaAlturas = GetMediaAlturasFilaCircular;
    fila.InserirFim = InserefFilaCircular;
    fila.RemoverInicio = RemoverInicioFilaCircular;
    fila.Mostrar = MostrafilaCircular;
    fila.Close = CloseFilaCircular;

    return fila;
}

void doComando(Fila *fila, Jogador jogadores[]) {
    Split split = SplitSpace();

    if (strcmp(split.linha[0], "I") == 0) {
        int valor = atoi(split.linha[1]);
        fila->InserirFim(fila, jogadores[valor]);
    }

    if (strcmp(split.linha[0], "R") == 0) {
        Jogador jogador = fila->RemoverInicio(fila);
        printf("(R) %s\n", jogador.nome);
    }
}

int main() {
    char id[50];
    Jogador jogadores[3922];
    Fila fila = newFila();

    FILE *file = fopen("/tmp/players.csv", "r");

    do {
        scanf("%s", id);
        if (strcmp(id, "FIM") != 0 && strcmp(id, "fim") != 0) {
            int identificador = atoi(id);
            leitura(jogadores, file);
            fila.InserirFim(&fila, jogadores[identificador]);
        }
    } while ((strcmp(id, "FIM") != 0) && (strcmp(id, "fim") != 0));

    fclose(file);

    int action;
    scanf("%i", &action);

    for (int i = 0; i <= action; i++) {
        doComando(&fila, jogadores);
    }

    fila.Mostrar(fila);
    fila.Close(&fila);

    return 0;
}
