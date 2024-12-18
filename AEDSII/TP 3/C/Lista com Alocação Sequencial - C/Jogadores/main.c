#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

typedef struct Jogador
{
    int id;
    char nome[100];
    int altura;
    int peso;
    char universidade[100];
    int anoNascimento;
    char cidadeNasc[100];
    char estadoNasc[100];
} Jogador;


int getId(Jogador *j)
{
    return j->id;
}

void setId(Jogador *j, int id)
{
    j->id = id;
}

char *getNome(Jogador *j)
{
    return j->nome;
}

void setNome(Jogador *j, char nome[])
{
    strcpy(j->nome, nome);
}

int getAltura(Jogador *j)
{
    return j->altura;
}

void setAltura(Jogador *j, int altura)
{
    j->altura = altura;
}

int getPeso(Jogador *j)
{
    return j->peso;
}

void setPeso(Jogador *j, int peso)
{
    j->peso = peso;
}

char *getUniversidade(Jogador *j)
{
    return j->universidade;
}

void setUniversidade(Jogador *j, char universidade[])
{
    strcpy(j->universidade, universidade);
}

int getAnoNascimento(Jogador *j)
{
    return j->anoNascimento;
}

void setAnoNascimento(Jogador *j, int anoNascimento)
{
    j->anoNascimento = anoNascimento;
}

char *getCidadeNasc(Jogador *j)
{
    return j->cidadeNasc;
}

void setCidadeNasc(Jogador *j, char cidadeNasc[])
{
    strcpy(j->cidadeNasc, cidadeNasc);
}

char *getEstadoNasc(Jogador *j)
{
    return j->estadoNasc;
}

void setEstadoNasc(Jogador *j, char estadoNasc[])
{
    strcpy(j->estadoNasc, estadoNasc);
}

void Imprimir(Jogador *j)
{
    printf("[%d ## %s ## %d ## %d ## %d ## %s ## %s ## %s]\n", j->id, j->nome,
           j->altura, j->peso, j->anoNascimento, j->universidade, j->cidadeNasc, j->estadoNasc);
}

void Ler(Jogador *j, int id, char nome[], int altura, int peso, char universidade[], int anoNascimento,
         char cidadeNasc[], char estadoNasc[])
{
    setId(j, id);
    setNome(j, nome);
    setAltura(j, altura);
    setPeso(j, peso);
    if (strcmp(universidade, "") == 0)
    {
        setUniversidade(j, "nao informado");
    }
    else
    {
        setUniversidade(j, universidade);
    }
    setAnoNascimento(j, anoNascimento);
    if (strcmp(cidadeNasc, "") == 0)
    {
        setCidadeNasc(j, "nao informado");
    }
    else
    {
        setCidadeNasc(j, cidadeNasc);
    }
    if (strcmp(estadoNasc, "") == 0)
    {
        setEstadoNasc(j, "nao informado");
    }
    else
    {
        setEstadoNasc(j, estadoNasc);
    }
}

typedef struct Lista
{
    Jogador *array;
    int tamanho;
} Lista;

void NewLista(Lista *L, int tamanho)
{
    L->array = (Jogador *)malloc(tamanho * sizeof(Jogador));
    L->tamanho = 0;
}

void InserirInicio(Lista *L, Jogador x)
{
    for (int i = L->tamanho; i > 0; i--)
    {
        L->array[i] = L->array[i - 1];
    }

    L->array[0] = x;
    (L->tamanho)++;
}

void InserirFim(Lista *l, Jogador x)
{
    l->array[l->tamanho] = x;
    (l->tamanho)++;
}

void Inserir(Lista *l, Jogador x, int pos)
{
    for (int i = l->tamanho; i > pos; i--)
    {
        l->array[i] = l->array[i - 1];
    }

    l->array[pos] = x;
    (l->tamanho)++;
}

Jogador RemoverInicio(Lista *l)
{
    Jogador resp = l->array[0];
    (l->tamanho)--;

    for (int i = 0; i < l->tamanho; i++)
    {
        l->array[i] = l->array[i + 1];
    }

    return resp;
}

Jogador RemoverFim(Lista *L)
{
    return L->array[--(L->tamanho)];
}

Jogador Remover(Lista *L, int pos)
{
    Jogador resp = L->array[pos];
    (L->tamanho)--;

    for (int i = pos; i < L->tamanho; i++)
    {
        L->array[i] = L->array[i + 1];
    }

    return resp;
}

void Mostrar(Lista *L)
{
    for (int i = 0; i < L->tamanho; i++)
    {
        printf("[%d] ## %s ## %d ## %d ## %d ## %s ## %s ## %s ##\n", i, getNome(&(L->array[i])), getAltura(&(L->array[i])),
               getPeso(&(L->array[i])), getAnoNascimento(&(L->array[i])), getUniversidade(&(L->array[i])),
               getCidadeNasc(&(L->array[i])), getEstadoNasc(&(L->array[i])));
    }
}

int main()
{
    FILE *arq = fopen("/tmp/players.csv", "r");
    int arqtam = 3923;
    char linha[1000];
    Jogador jogador[arqtam];
    for (int i = 0; i < arqtam; i++)
    {
        fscanf(arq, "%[^\n]", linha);
        fgetc(arq);
        char *atributos[8];
        char *tk = strtok(linha, ",");
        int count = 0;
        for (int j = 0; tk; j++)
        {
            count++;
            atributos[j] = tk;
            tk = strtok(NULL, ",");
        }
        if (count == 7 && strlen(atributos[7]) == 0)
        {
            atributos[7] = atributos[6];
        }
        else if (count == 5)
        {
            atributos[5] = atributos[4];
            atributos[4] = "";
        }
        if (atoi(atributos[5]) == 0)
        {
            atributos[6] = atributos[5];
        }
        if (atoi(atributos[4]) != 0)
        {
            atributos[5] = atributos[4];
        }
        int id = atoi(atributos[0]);
        char nome[100];
        strcpy(nome, atributos[1]);
        int altura = atoi(atributos[2]);
        int peso = atoi(atributos[3]);
        char universidade[100];
        strcpy(universidade, atributos[4]);
        int anoNascimento = atoi(atributos[5]);
        char cidadeNascimento[100];
        strcpy(cidadeNascimento, atributos[6]);
        char estadoNascimento[100];
        strcpy(estadoNascimento, atributos[7]);
        Ler(&jogador[i], id, nome, altura, peso, universidade, anoNascimento, cidadeNascimento, estadoNascimento);
    }
    fclose(arq);

    char c;
    int fim = 0;
    char entrada[500];
    Lista jog;
    NewLista(&jog, arqtam);
    do
    {
        scanf("%[^\n]", entrada);
        c = getchar();
        if (strcmp(entrada, "FIM") == 0)
        {
            fim = 1;
        }
        else
        {
            Jogador entrjog = jogador[atoi(entrada) + 1];
            InserirFim(&jog, entrjog);
        }
    } while (fim == 0);

    int n;
    scanf("%d", &n);
    c = getchar();
    for (int i = 0; i < n; i++)
    {
        int pos;
        Jogador entrjog;
        scanf("%[^\n]", entrada);
        c = getchar();
        char *tok[3];
        char *tk = strtok(entrada, " ");
        for (int j = 0; tk; j++)
        {
            tok[j] = tk;
            tk = strtok(NULL, " ");
        }
        if (strcmp(tok[0], "II") == 0)
        {
            entrjog = jogador[atoi(tok[1]) + 1];
            InserirInicio(&jog, entrjog);
        }
        else if (strcmp(tok[0], "I*") == 0)
        {
            pos = atoi(tok[1]);
            entrjog = jogador[atoi(tok[2]) + 1];
            Inserir(&jog, entrjog, pos);
        }
        else if (strcmp(tok[0], "IF") == 0)
        {
            entrjog = jogador[atoi(tok[1]) + 1];
            InserirFim(&jog, entrjog);
        }
        else if (strcmp(tok[0], "RI") == 0)
        {
            entrjog = RemoverInicio(&jog);
            printf("(R) %s\n", getNome(&entrjog));
        }
        else if (strcmp(tok[0], "R*") == 0)
        {
            pos = atoi(tok[1]);
            entrjog = Remover(&jog, pos);
            printf("(R) %s\n", getNome(&entrjog));
        }
        else if (strcmp(tok[0], "RF") == 0)
        {
            entrjog = RemoverFim(&jog);
            printf("(R) %s\n", getNome(&entrjog));
        }
    }
    Mostrar(&jog);

    return 0;
}
