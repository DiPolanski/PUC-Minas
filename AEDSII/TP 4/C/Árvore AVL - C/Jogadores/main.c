#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <math.h>

#define MAX_TAM 5
#define bool short
#define true 1
#define false 0

#define TAM_MAX_LINHA 250

bool eFim(char *str){
    return (strlen(str) >= 3 && str[0] == 'F' &&
            str[1] == 'I' && str[2] == 'M');
}

char *substituirCaractere(char *str, char paraBuscar, char paraSubstituir){
    char *ptr = strchr(str, paraBuscar);
    if (ptr != NULL)
        *ptr = paraSubstituir;
    return ptr;
}

void lerLinhaArquivo(char *str, int tamanhoMaximo, FILE *arquivo)//leitor de linha
{
    fgets(str, tamanhoMaximo, arquivo);
    substituirCaractere(str, '\r', '\0');
    substituirCaractere(str, '\n', '\0');
}

typedef struct Jogador {
    int id;
    int altura;
    int peso;
    int anoNascimento;
    char nome[100];
    char universidade[100];
    char cidadeNascimento[100];
    char estadoNascimento[100];
} Jogador;

typedef struct CelulaDupla {
    Jogador elemento;
    struct CelulaDupla *prox;
    struct CelulaDupla *ant;
} CelulaDupla;

void inserirNaoInformado(char *linha, char *novaLinha){
    int tam = strlen(linha);
    for (int i = 0; i <= tam; i++, linha++)
    {
        *novaLinha++ = *linha;
        if (*linha == ',' && (*(linha + 1) == ',' || *(linha + 1) == '\0'))
        {
            strcpy(novaLinha, "nao informado");
            novaLinha += strlen("nao informado");
        }
    }
}

void removerQuebraDeLinha(char linha[]){
    int tam = strlen(linha);

    if (linha[tam - 2] == '\r' && linha[tam - 1] == '\n')
        linha[tam - 2] = '\0';
    else if (linha[tam - 1] == '\r' || linha[tam - 1] == '\n')
        linha[tam - 1] = '\0';
}

void preencherJogador(Jogador *jogador, char linha[]){
    char novaLinha[TAM_MAX_LINHA];
    removerQuebraDeLinha(linha);
    inserirNaoInformado(linha, novaLinha);

    jogador->id = atoi(strtok(novaLinha, ","));
    strcpy(jogador->nome, strtok(NULL, ","));
    jogador->altura = atoi(strtok(NULL, ","));
    jogador->peso = atoi(strtok(NULL, ","));
    strcpy(jogador->universidade, strtok(NULL, ","));
    jogador->anoNascimento = atoi(strtok(NULL, ","));
    strcpy(jogador->cidadeNascimento, strtok(NULL, ","));
    strcpy(jogador->estadoNascimento, strtok(NULL, ","));
}

void lerArquivo(char linhas_corrigidas[][TAM_MAX_LINHA]){
    FILE *arquivo;
    arquivo = fopen("/tmp/players.csv", "r");

    char linhas[4000][TAM_MAX_LINHA];

    int i = 0;
    lerLinhaArquivo(linhas[0], TAM_MAX_LINHA, arquivo);
    do
    {
        lerLinhaArquivo(linhas[i++], TAM_MAX_LINHA, arquivo);
    } while (!feof(arquivo));
    i--;

    for (int i = 0; i < 4000; i++)
    {
        inserirNaoInformado(linhas[i], linhas_corrigidas[i]);
    }

    fclose(arquivo);
}

typedef struct No {
    Jogador *elemento;
    int nivel;
    struct No *esq, *dir;
} No;

No *raiz;

No *novoNo(Jogador *elemento){
    No *novo = (No *)malloc(sizeof(No));
    novo->elemento = elemento;
    novo->esq = NULL;
    novo->dir = NULL;
    return novo;
}

void iniciarArvore(){
    raiz = NULL;
}

bool pesquisarRecursivo(char *nome, No *no){
    bool resp;
    if (no == NULL)
    {
        resp = false;
    }
    else if (strcmp(nome, no->elemento->nome) == 0)
    {
        resp = true;
        printf("SIM\n");
    }
    else if (strcmp(nome, no->elemento->nome) < 0)
    {
        printf("esq ");
        resp = pesquisarRecursivo(nome, no->esq);
    }
    else
    {
        printf("dir ");
        resp = pesquisarRecursivo(nome, no->dir);
    }
    return resp;
}

void pesquisarNome(char *nome){
    printf("%s raiz ", nome);
    if (pesquisarRecursivo(nome, raiz) == false)
        printf("NAO\n");
}

int obterNivel(No *no){
    return (no == NULL) ? 0 : no->nivel;
}

void definirNivel(No *no){
    no->nivel = 1 + fmax(obterNivel(no->esq), obterNivel(no->dir));
}

No *rotacionarDireita(No *no){
    No *noEsq = no->esq;
    No *noEsqDir = noEsq->dir;

    noEsq->dir = no;
    no->esq = noEsqDir;

    definirNivel(no);
    definirNivel(noEsq);

    return noEsq;
}

No *rotacionarEsquerda(No *no){
    No *noDir = no->dir;
    No *noDirEsq = noDir->esq;

    noDir->esq = no;
    no->dir = noDirEsq;

    definirNivel(no);
    definirNivel(noDir);

    return noDir;
}

No *balancearArvore(No *no){
    if (no != NULL)
    {
        int fator = obterNivel(no->dir) - obterNivel(no->esq);

        if (abs((long)fator) <= 1)
        {
            definirNivel(no);
        }
        else if (fator == 2)
        {
            int fatorFilhoDir = obterNivel(no->dir->dir) - obterNivel(no->dir->esq);

            if (fatorFilhoDir == -1)
            {
                no->dir = rotacionarDireita(no->dir);
            }
            no = rotacionarEsquerda(no);
        }
        else if (fator == -2)
        {
            int fatorFilhoEsq = obterNivel(no->esq->dir) - obterNivel(no->esq->esq);

            if (fatorFilhoEsq == 1)
            {
                no->esq = rotacionarEsquerda(no->esq);
            }
            no = rotacionarDireita(no);
        }
    }

    return no;
}

void inserirRecursivo(Jogador *jogador, No **no){
    if (*no == NULL)
    {
        *no = novoNo(jogador);
    }
    else if (strcmp(jogador->nome, (*no)->elemento->nome) < 0)
    {
        inserirRecursivo(jogador, &((*no)->esq));
    }
    else if (strcmp(jogador->nome, (*no)->elemento->nome) > 0)
    {
        inserirRecursivo(jogador, &((*no)->dir));
    }
    else
    {
        printf("Erro ao inserir!");
    }

    *no = balancearArvore(*no);
}

void percorrerPreOrdemRecursivo(No *no){
    if (no != NULL)
    {
        printf("%s\n", no->elemento->nome);
        percorrerPreOrdemRecursivo(no->esq);
        percorrerPreOrdemRecursivo(no->dir);
    }
}

void percorrerPreOrdem(){
    percorrerPreOrdemRecursivo(raiz);
}

void inserirNaArvore(Jogador *jogador){
    inserirRecursivo(jogador, &raiz);
}

int main(int argc, char **argv){
    char entradaUsuarios[1000][10];
    int numEntradas = 0;
    do
    {
        lerLinhaArquivo(entradaUsuarios[numEntradas], 10, stdin);
    } while (!eFim(entradaUsuarios[numEntradas++]));

    numEntradas--;

    int entradasInteiras[1000];

    for (int i = 0; i < 1000; i++)
    {
        sscanf(entradaUsuarios[i], "%d", &entradasInteiras[i]);
    }

    char saidaArquivo[4000][TAM_MAX_LINHA];

    lerArquivo(saidaArquivo);

    iniciarArvore();

    for (int i = 0; i < numEntradas; i++)
    {
        Jogador *jogador = (Jogador *)malloc(sizeof(Jogador));
        preencherJogador(jogador, saidaArquivo[entradasInteiras[i]]);
        inserirNaArvore(jogador);
    }

    char *linha = (char *)malloc(100 * sizeof(char));
    lerLinhaArquivo(linha, 100, stdin);

    while (!eFim(linha))
    {
        pesquisarNome(linha);
        lerLinhaArquivo(linha, 100, stdin);
    }

    return 0;
}
