#include <stdio.h>
#include <stdlib.h>
#include <time.h>

char Troca(char string[], char letra1, char letra2) {
    char novaString[1000];
    int i = 0;

    while (string[i] != '\0') {
        if (string[i] == letra1) {
            novaString[i] = letra2;
        } else {
            novaString[i] = string[i];
        }
        i++;
    }

    novaString[i] = '\0';

    return novaString;
}

int main(void) {
    char string[1000];
    char letra1;
    char letra2;

    srand(time(NULL)); // Inicializa a semente de números aleatórios

    while (1) {
        letra1 = 'a' + rand() % 26; // Sorteia a primeira letra
        letra2 = 'a' + rand() % 26; // Sorteia a segunda letra

        fgets(string, sizeof(string), stdin);
        string[strlen(string) - 1] = '\0'; // Remove o caractere de nova linha

        if (strcmp(string, "FIM") == 0) {
            break;
        }

        char novaString[1000];
        strcpy(novaString, Troca(string, letra1, letra2));

        printf("%s\n", novaString);
    }

    return 0;
}
