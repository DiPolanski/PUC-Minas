#include <stdio.h>
#include <stdlib.h>

int main() {
    FILE *arquivo;
    char nomeArquivo[] = "meuarquivo.txt"; // Nome do arquivo criado

    int n;

    scanf("%d", &n);
    getchar(); // Consuma o caractere de nova linha após o número inteiro

    // Abra o arquivo para escrita
    arquivo = fopen(nomeArquivo, "w");

    if (arquivo == NULL) {
        printf("Erro ao abrir o arquivo para escrita.\n");
        return 1;
    }

    // Escreva os dados no arquivo
    for (int i = 0; i < n; i++) {
        char conteudo[256];

        fgets(conteudo, sizeof(conteudo), stdin);
        fprintf(arquivo, "%s", conteudo);
    }

    // Feche o arquivo
    fclose(arquivo);

    // Reabra o arquivo para leitura
    arquivo = fopen(nomeArquivo, "r");

    if (arquivo == NULL) {
        printf("Erro ao abrir o arquivo para leitura.\n");
        return 1;
    }

    // Determine o número de linhas no arquivo
    int numLinhas = 0;
    char linha[256];

    while (fgets(linha, sizeof(linha), arquivo) != NULL) {
        numLinhas++;
    }

    // Feche o arquivo de leitura
    fclose(arquivo);

    // Reabra o arquivo para leitura novamente
    arquivo = fopen(nomeArquivo, "r");

    if (arquivo == NULL) {
        printf("Erro ao abrir o arquivo para leitura novamente.\n");
        return 1;
    }

    // Crie um array para armazenar as linhas
    char linhas[numLinhas][256];

    // Leitura das linhas e armazenamento no array
    for (int i = 0; i < numLinhas; i++) {
        fgets(linhas[i], sizeof(linhas[i]), arquivo);
    }

    // Feche o arquivo de leitura
    fclose(arquivo);

    // Imprime os números na ordem inversa, formatando-os adequadamente
    for (int i = numLinhas - 1; i >= 0; i--) {
        char *linha = linhas[i];
        if (strchr(linha, '.')) {
            // Se a linha contém um ponto, imprime como número de ponto flutuante
            double numero = atof(linha);
            printf("%g\n", numero);
        } else {
            // Caso contrário, imprime como número inteiro
            int numero = atoi(linha);
            printf("%d\n", numero);
        }
    }

    return 0;
}
