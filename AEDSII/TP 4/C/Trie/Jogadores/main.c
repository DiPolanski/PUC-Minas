#include <math.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

struct Jogador
{
    int id;
    char nome[1000];
    int altura;
    int peso;
    int anoNascimento;
    char *universidade;
    char *cidadeNascimento;
    char *estadoNascimento;
};

struct Jogador jogadores[3922];
int n = 3922;

struct Celula
{
    struct Jogador *elemento;
    struct Celula *prox;
};

struct Celula *primeiraCelula;
struct Celula *ultimaCelula;

struct Celula *novaCelula(struct Jogador *jogadorEscolhido)
{
    struct Celula *nova = (struct Celula *)malloc(sizeof(struct Celula));
    nova->elemento = jogadorEscolhido;
    nova->prox = NULL;
    return nova;
}

struct Celula *iniciarLista()
{
    struct Jogador *aux = (struct Jogador *)malloc(sizeof(struct Jogador));
    aux->id = 0;
    strcpy(aux->nome, "N/I");
    aux->altura = 0;
    aux->peso = 0;
    aux->anoNascimento = 0;
    aux->universidade = "N/I";
    aux->cidadeNascimento = "N/I";
    aux->estadoNascimento = "N/I";

    struct Celula *criada = novaCelula(aux);
    return criada;
}

struct Lista
{
    struct Celula *inicio;
    struct Celula *prox;
};

struct Hash
{
    struct Lista *lista[21];
    int tamanho;
};

struct Hash *iniciarHash()
{
    struct Hash *aux = (struct Hash *)malloc(sizeof(struct Hash));
    aux->tamanho = 21;
    for (int i = 0; i < aux->tamanho; i++)
    {
        aux->lista[i] = (struct Lista *)malloc(sizeof(struct Lista));
        aux->lista[i]->inicio = NULL;
        aux->lista[i]->prox = NULL;
    }
    return aux;
}

int h(int altura)
{
    return altura % 21;
}

bool pesquisar(struct Hash *hash, struct Jogador *jogadorEscolhido)
{
    bool existe = false;
    int pos = h(jogadorEscolhido->altura);
    struct Celula *inicioLista = hash->lista[pos]->inicio;
    while (inicioLista != NULL && existe == false)
    {
        if (strcmp(inicioLista->elemento->nome, jogadorEscolhido->nome) == 0)
        {
            existe = true;
        }
        inicioLista = inicioLista->prox;
    }
    return existe;
}

void inserirHash(struct Hash *tabHash, int altura, struct Jogador *jogadorEscolhido)
{
    int posTabHash = h(altura);
    struct Celula *novaCelula = (struct Celula *)malloc(sizeof(struct Celula));
    novaCelula->elemento = jogadorEscolhido;
    novaCelula->prox = tabHash->lista[posTabHash]->inicio;
    tabHash->lista[posTabHash]->inicio = novaCelula;
}

struct Jogador *buscaPorNome(char *nome)
{
    struct Jogador *aux = (struct Jogador *)malloc(sizeof(struct Jogador));
    for (int i = 0; i < n; i++)
    {
        if (strcmp(nome, jogadores[i].nome) == 0)
        {
            aux = &jogadores[i];
        }
    }
    return aux;
}

bool isFim(char *leitura)
{
    bool ehFim;
    if (leitura[0] == 'F' && leitura[1] == 'I' && leitura[2] == 'M')
        ehFim = true;
    else
        ehFim = false;
    return ehFim;
}

int ler()
{
    int resp;
    char leitura[20];
    fgets(leitura, sizeof(leitura), stdin);

    // Substituir os caracteres de nova linha e retorno de carro por '\0'
    leitura[strcspn(leitura, "\r\n")] = '\0';

    if (isFim(leitura))
        resp = -1;
    else
        resp = atoi(leitura);
    return resp;
}

bool vazio(char *palavra)
{
    return (palavra[0] == '\0');
}

int tamanho()
{
    int tam = 0;
    for (struct Celula *i = primeiraCelula; i != NULL; i = i->prox)
    {
        tam++;
    }
    return tam;
}

struct Celula *pegaPosicao(int pos)
{
    struct Celula *resp = primeiraCelula;
    for (int i = 0; i < pos; i++)
    {
        resp = resp->prox;
    }
    return resp;
}

void inserir(struct Jogador *jogador)
{
    ultimaCelula->prox = novaCelula(jogador);
    ultimaCelula = ultimaCelula->prox;
}

int main()
{
    FILE *arquivo = fopen("/tmp/players.csv", "r");
    clock_t inicio = clock();
    int movimentacoes = 0;

    char linha[200];
    fgets(linha, sizeof(linha), arquivo);

    char *atributos[8];
    for (int i = 0; i < 8; ++i)
    {
        atributos[i] = (char *)calloc(100, sizeof(char));
    }

    for (int i = 0; i < 3922; i++)
    {
        fscanf(arquivo, "%[^,]", atributos[0]);
        jogadores[i].id = atoi(atributos[0]);
        fgetc(arquivo);

        fscanf(arquivo, "%[^,]", atributos[1]);
        strcpy(jogadores[i].nome, atributos[1]);
        fgetc(arquivo);

        fscanf(arquivo, "%[^,]", atributos[2]);
        jogadores[i].altura = atoi(atributos[2]);
        fgetc(arquivo);

        fscanf(arquivo, "%[^,]", atributos[3]);
        jogadores[i].peso = atoi(atributos[3]);
        fgetc(arquivo);

        jogadores[i].universidade = "nao informado";
        atributos[4] = (char *)calloc(100, sizeof(char));
        fscanf(arquivo, "%[^,]", atributos[4]);
        if (!vazio(atributos[4]))
            jogadores[i].universidade = atributos[4];
        fgetc(arquivo);

        fscanf(arquivo, "%[^,]", atributos[5]);
        jogadores[i].anoNascimento = atoi(atributos[5]);
        fgetc(arquivo);

        jogadores[i].cidadeNascimento = "nao informado";
        atributos[6] = (char *)calloc(100, sizeof(char));
        fscanf(arquivo, "%[^,]", atributos[6]);
        if (!vazio(atributos[6]))
            jogadores[i].cidadeNascimento = atributos[6];
        fgetc(arquivo);

        jogadores[i].estadoNascimento = "nao informado";
        atributos[7] = (char *)calloc(100, sizeof(char));
        fscanf(arquivo, "%[^,\r\n]", atributos[7]);
        if (!vazio(atributos[7]))
            jogadores[i].estadoNascimento = atributos[7];
        fgetc(arquivo);
    }

    fclose(arquivo);

    struct Hash *inicioHash = iniciarHash();

    int jogadorEscolhido = 0;

    while (jogadorEscolhido != -1)
    {
        jogadorEscolhido = ler();
        if (jogadorEscolhido != -1)
        {
            inserirHash(inicioHash, jogadores[jogadorEscolhido].altura,
                        &jogadores[jogadorEscolhido]);
        }
    }

    bool existe;
    char nomePesquisa[50];
    fgets(nomePesquisa, sizeof(nomePesquisa), stdin);

    while (strcmp(nomePesquisa, "FIM") != 0)
    {
        // Substituir os caracteres de nova linha e retorno de carro por '\0'
        nomePesquisa[strcspn(nomePesquisa, "\r\n")] = '\0';

        struct Jogador *jogadorAssociado = buscaPorNome(nomePesquisa);
        existe = pesquisar(inicioHash, jogadorAssociado);

        printf("%s %s\n", nomePesquisa, (existe ? "SIM" : "NAO"));

        fgets(nomePesquisa, sizeof(nomePesquisa), stdin);
    }
    clock_t fim = clock();
    double tempo_microssegundos = (double)(fim - inicio) * 1e6 / CLOCKS_PER_SEC;
    FILE *arquivoSaida = fopen("802736_hashIndireta.txt", "w");
    fprintf(arquivoSaida, "802736\t");
    fprintf(arquivoSaida, "%d\t", movimentacoes);
    fprintf(arquivoSaida, "%ld\t", (long)tempo_microssegundos);
    fclose(arquivoSaida);
}
