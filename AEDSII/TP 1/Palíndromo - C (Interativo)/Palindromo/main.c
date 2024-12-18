#include <stdio.h>
#include <stdbool.h>
#include <string.h>
#include <ctype.h>

bool PalindromoIterativo(char string[], int tamanho) {
    int esquerda = 0;
    int direita = tamanho - 1;

    while (esquerda < direita) { // while para verificar se e um palindromo
        if (string[esquerda] != string[direita]) {
            return false;
        }
        esquerda++;
        dir--;
    }
    return true;
}

int main(void) {
    char string[1000];
    char encerra[] = "FIM";

    while (1) {
        if (scanf(" %[^\n]", string) != 1) {
            return 1; // Sai do loop se a leitura falhar
        }

        if (strcasecmp(string, encerra) == 0) {
            return 0; // Encerra o programa se a entrada for "FIM"
        }

        int tamanho = strlen(string); // tamanho da string

        if (PalindromoIterativo(string, tamanho)) {
            printf("SIM\n"); // se verdadeiro, imprima "SIM"
        } else {
            printf("NAO\n"); // se falso, imprima "NAO"
        }
    }

    return 0;
}
