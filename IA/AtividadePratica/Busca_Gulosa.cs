using System.Collections.Generic;
using UnityEngine;
using TMPro;
using UnityEngine.UI;

public class GerenciadorDePerguntas : MonoBehaviour
{
    public TextMeshProUGUI textoPergunta;
    public TextMeshProUGUI textoFeedback;
    public Button[] botoesResposta;
    public Button botaoProximaPergunta;

    private BancoDePerguntas bancoDePerguntas;
    private Pergunta perguntaAtual;
    private int nivelAtual = 1;
    private int acertos = 0, erros = 0;
    private List<Pergunta> perguntasAtuais = new List<Pergunta>();
    private int maxNivel = 5; // Nível máximo do jogo

    void Start()
    {
        // Carregar banco de perguntas
        TextAsset jsonFile = Resources.Load<TextAsset>("perguntas");
        bancoDePerguntas = JsonUtility.FromJson<BancoDePerguntas>(jsonFile.text);

        // Configurar botões
        foreach (Button botao in botoesResposta)
        {
            botao.onClick.AddListener(() => VerificarResposta(botao));
        }
        botaoProximaPergunta.onClick.AddListener(ProximaPergunta);

        AtualizarPerguntasAtuais();
        ProximaPergunta();
    }

    void AtualizarPerguntasAtuais()
    {
        perguntasAtuais.Clear();
        foreach (var categoria in bancoDePerguntas.categorias)
        {
            perguntasAtuais.AddRange(categoria.perguntas.FindAll(p => p.dificuldade == nivelAtual));
        }

        if (perguntasAtuais.Count == 0)
        {
            Debug.LogWarning("Não há perguntas disponíveis para o nível " + nivelAtual);
        }
    }

    void ProximaPergunta()
    {
        textoFeedback.gameObject.SetActive(false);

        if (perguntasAtuais.Count == 0)
        {
            nivelAtual = BuscaEmProfundidadeProximoNivel();
            if (nivelAtual > maxNivel)
            {
                Debug.Log("Todas as perguntas foram respondidas.");
                textoPergunta.text = "Parabéns! Você completou o jogo.";
                botaoProximaPergunta.gameObject.SetActive(false);
                return;
            }
            AtualizarPerguntasAtuais();
        }

        if (perguntasAtuais.Count == 0)
        {
            Debug.LogWarning("Não há perguntas disponíveis para o nível " + nivelAtual);
            return;
        }

        int indicePergunta = Random.Range(0, perguntasAtuais.Count);
        perguntaAtual = perguntasAtuais[indicePergunta];
        perguntasAtuais.RemoveAt(indicePergunta);

        // Atualizar UI
        textoPergunta.text = perguntaAtual.pergunta;
        for (int i = 0; i < botoesResposta.Length; i++)
        {
            if (i < perguntaAtual.alternativas.Count)
            {
                botoesResposta[i].GetComponentInChildren<TextMeshProUGUI>().text = perguntaAtual.alternativas[i];
                botoesResposta[i].gameObject.SetActive(true);
            }
            else
            {
                botoesResposta[i].gameObject.SetActive(false); // Esconde botões extras
            }
        }

        Debug.Log("Pergunta atualizada: " + perguntaAtual.pergunta + " | Nível: " + nivelAtual);
    }

    void VerificarResposta(Button botao)
    {
        string respostaSelecionada = botao.GetComponentInChildren<TextMeshProUGUI>().text;
        if (respostaSelecionada == perguntaAtual.correta)
        {
            textoFeedback.text = "Correto!";
            textoFeedback.color = Color.green;
            acertos++;
        }
        else
        {
            textoFeedback.text = "Errado!";
            textoFeedback.color = Color.red;
            erros++;
        }

        textoFeedback.gameObject.SetActive(true);
    }

    int BuscaEmProfundidadeProximoNivel()
    {
        // Implementação da Busca em Profundidade para determinar o próximo nível
        Stack<int> pilhaNiveis = new Stack<int>();
        pilhaNiveis.Push(nivelAtual);

        while (pilhaNiveis.Count > 0)
        {
            int nivel = pilhaNiveis.Pop();
            float taxaDeAcertos = (float)acertos / (acertos + erros);

            // Verifica se deve aumentar ou diminuir o nível
            if (taxaDeAcertos > 0.8f && nivel < maxNivel)
            {
                pilhaNiveis.Push(nivel + 1);
                return nivel + 1; // Próximo nível em profundidade
            }
            else if (taxaDeAcertos < 0.5f && nivel > 1)
            {
                pilhaNiveis.Push(nivel - 1);
                return nivel - 1; // Retrocede para um nível anterior
            }
        }

        return nivelAtual; // Mantém o nível atual caso não encontre mudanças
    }
}

// Estruturas auxiliares
[System.Serializable]
public class BancoDePerguntas
{
    public List<Categoria> categorias;
}

[System.Serializable]
public class Categoria
{
    public string nome;
    public List<Pergunta> perguntas;
}

[System.Serializable]
public class Pergunta
{
    public string pergunta;
    public List<string> alternativas;
    public string correta;
    public int dificuldade;
}
