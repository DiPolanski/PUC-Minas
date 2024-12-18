#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>
#include <time.h>

int controle = -1;
int comparacoes = 0;    // Variável para contar comparações
int movimentacoes = 0;  // Variável para contar movimentações

typedef struct {
    int id;
    char nome[100];
    int altura;
    int peso;
    char universidade[100];
    int ano_nascimento;
    char cidade_nascimento[100];
    char estado_nascimento[100];
} Jogador;

void Distribuir(FILE* arq, Jogador objeto[], char* LerAtributo) {
    fscanf(arq, " %[^,\n]", LerAtributo);
    fgetc(arq);
    strcpy(objeto[controle].nome, LerAtributo);

    fscanf(arq, " %[^,\n]", LerAtributo);
    fgetc(arq);
    objeto[controle].altura = atoi(LerAtributo);

    fscanf(arq, " %[^,\n]", LerAtributo);
    fgetc(arq);
    objeto[controle].peso = atoi(LerAtributo);

    fscanf(arq, " %[^,\n]", LerAtributo);
    fgetc(arq);
    strcpy(objeto[controle].universidade, (atoi(LerAtributo) == objeto[controle].peso ? "nao informado" : LerAtributo));

    fscanf(arq, " %[^,\n]", LerAtributo);
    fgetc(arq);
    objeto[controle].ano_nascimento = atoi(LerAtributo);

    fscanf(arq, " %[^,\n]", LerAtributo);
    fgetc(arq);
    strcpy(objeto[controle].cidade_nascimento, ((atoi(LerAtributo) == objeto[controle].ano_nascimento) ? "nao informado" : LerAtributo));

    fscanf(arq, " %[^,\n]", LerAtributo);
    fgetc(arq);
    strcpy(objeto[controle].estado_nascimento, (atoi(LerAtributo) == objeto[controle].id + 1 ? "nao informado" : LerAtributo));
}

void LerLinha(FILE* arq, Jogador objeto[], char* pub_in) {
    char lerAtributo[300];
    rewind(arq);
    fgets(lerAtributo, 300, arq); // Ler o cabeçalho
    while (fscanf(arq, " %[^,\n]", lerAtributo) != 0) {
        fgetc(arq);
        if (strcmp(lerAtributo, pub_in) == 0) {
            controle++;
            objeto[controle].id = atoi(lerAtributo);
            Distribuir(arq, objeto, lerAtributo);
            break;
        } else {
            fgets(lerAtributo, 300, arq);
        }
    }
}

bool isFim(char* pub_in) {
    scanf(" %[^\n]", pub_in);
    return (strcmp(pub_in, "FIM") == 0);
}

void Imprimir(Jogador objeto[]) {
    for (int i = 0; i <= controle; i++) {
        printf("[%d ## %s ## %d ## %d ## %d ## %s ## %s ## %s]\n",
               objeto[i].id, objeto[i].nome, objeto[i].altura, objeto[i].peso,
               objeto[i].ano_nascimento, objeto[i].universidade,
               objeto[i].cidade_nascimento, objeto[i].estado_nascimento);
    }
}

void swap(Jogador objeto[], int posA, int posB) {
    Jogador tmp = objeto[posA];
    objeto[posA] = objeto[posB];
    objeto[posB] = tmp;
    movimentacoes += 3; // Incrementa a contagem de movimentações
}

int OrganizarPivo(Jogador objeto[], int p, int r) {
    int pivo = r;
    int barraA = p - 1;
    int barraB;
    for (barraB = p; barraB < pivo; barraB++) {
        comparacoes++; // Incrementa a contagem de comparações
        if (strcmp(objeto[barraB].estado_nascimento, objeto[pivo].estado_nascimento) < 0 ||
            (strcmp(objeto[barraB].estado_nascimento, objeto[pivo].estado_nascimento) == 0 &&
             strcmp(objeto[barraB].nome, objeto[pivo].nome) < 0)) {
            barraA++;
            swap(objeto, barraA, barraB);
        }
    }
    swap(objeto, barraA + 1, pivo);
    return barraA + 1;
}

void QuickSort(Jogador objeto[], int p, int r) {
    if (p < r) {
        int q = OrganizarPivo(objeto, p, r);
        QuickSort(objeto, p, q - 1);
        QuickSort(objeto, q + 1, r);
    }
}

int main() {
    FILE* arq = fopen("/tmp/players.csv", "r");
    char pub_in[5];

    Jogador objeto[4000];

    while (!isFim(pub_in)) {
        LerLinha(arq, objeto, pub_in);
    }

    clock_t start_time, end_time; // Variáveis para medir o tempo
    start_time = clock();

    QuickSort(objeto, 0, controle);
    Imprimir(objeto);

    end_time = clock();
    double time_taken = ((double)(end_time - start_time)) / CLOCKS_PER_SEC;



    fclose(arq);
    FILE *outputFile = fopen("802736_quicksort.txt", "w");
    fprintf(outputFile, "802736\t");
    fprintf(outputFile, "%d\t", comparacoes);
    fprintf(outputFile, "%d\t", movimentacoes);
    fprintf(outputFile, "%ld\t", time_taken);
    fclose(outputFile);

    return 0;
}
