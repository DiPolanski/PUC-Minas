#include <stdio.h>
#include <stdbool.h>
#include <string.h>
#include <ctype.h>

bool PalindromoRecursivo(char string[], int esquerda, int direita) {
    if (esquerda >= direita) {
        return true;
    }

    // Ignora espaços em branco na verificação
    while (esquerda < direita && !isalnum(string[esquerda])) {
        esquerda++;
    }
    while (esquerda < direita && !isalnum(string[direita])) {
        direita--;
    }

    if (tolower(string[esquerda]) != tolower(string[direita])) {
        return false;
    }

    return PalindromoRecursivo(string, esquerda + 1, direita - 1);
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

        int tamanho = strlen(string);

        if (PalindromoRecursivo(string, 0, tamanho - 1)) {
            printf("SIM\n");
        } else {
            printf("NAO\n");
        }
    }

    return 0;
}
