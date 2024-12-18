import numpy as np
import matplotlib.pyplot as plt

# Funções de ativação e suas derivadas
def sigmoid(x):
    return 1 / (1 + np.exp(-x))

def sigmoid_derivative(x):
    return x * (1 - x)

def tanh(x):
    return np.tanh(x)

def tanh_derivative(x):
    return 1 - np.tanh(x) ** 2

# Definir a classe da Rede Neural
class NeuralNetwork:
    def __init__(self, num_inputs, num_hidden_neurons, num_outputs, activation="sigmoid", learning_rate=0.1, use_bias=True):
        self.num_inputs = num_inputs
        self.num_hidden_neurons = num_hidden_neurons
        self.num_outputs = num_outputs
        self.learning_rate = learning_rate
        self.use_bias = use_bias

        # Inicializar pesos
        self.weights_input_to_hidden = np.random.uniform(-1, 1, (num_inputs, num_hidden_neurons))
        self.weights_hidden_to_output = np.random.uniform(-1, 1, (num_hidden_neurons, num_outputs))

        # Inicializar bias
        self.bias_hidden = np.random.uniform(-1, 1, (1, num_hidden_neurons)) if use_bias else np.zeros((1, num_hidden_neurons))
        self.bias_output = np.random.uniform(-1, 1, (1, num_outputs)) if use_bias else np.zeros((1, num_outputs))

        # Selecionar função de ativação
        if activation == "sigmoid":
            self.activation_function = sigmoid
            self.activation_derivative = sigmoid_derivative
        elif activation == "tanh":
            self.activation_function = tanh
            self.activation_derivative = tanh_derivative
        else:
            raise ValueError("Função de ativação não suportada")

    def forward(self, inputs):
        # Propagação para frente
        self.inputs = inputs
        self.hidden_layer_input = np.dot(inputs, self.weights_input_to_hidden) + self.bias_hidden
        self.hidden_layer_output = self.activation_function(self.hidden_layer_input)
        self.output_layer_input = np.dot(self.hidden_layer_output, self.weights_hidden_to_output) + self.bias_output
        self.output_layer_output = self.activation_function(self.output_layer_input)
        return self.output_layer_output

    def backward(self, inputs, expected_output, predicted_output):
        # Propagação para trás (Backpropagation)
        output_error = expected_output - predicted_output
        output_delta = output_error * self.activation_derivative(predicted_output)

        hidden_layer_error = np.dot(output_delta, self.weights_hidden_to_output.T)
        hidden_layer_delta = hidden_layer_error * self.activation_derivative(self.hidden_layer_output)

        # Atualizar pesos e bias
        self.weights_hidden_to_output += self.learning_rate * np.dot(self.hidden_layer_output.T, output_delta)
        self.weights_input_to_hidden += self.learning_rate * np.dot(inputs.T, hidden_layer_delta)
        if self.use_bias:
            self.bias_output += self.learning_rate * np.sum(output_delta, axis=0, keepdims=True)
            self.bias_hidden += self.learning_rate * np.sum(hidden_layer_delta, axis=0, keepdims=True)

    def train(self, inputs, expected_output, epochs):
        errors = []
        for epoch in range(epochs):
            predicted_output = self.forward(inputs)
            error = np.mean(np.abs(expected_output - predicted_output))
            errors.append(error)
            self.backward(inputs, expected_output, predicted_output)
        return errors

# Função utilitária para gerar tabela verdade para AND, OR, XOR com n entradas
def generate_truth_table(num_inputs, gate):
    from itertools import product
    inputs = np.array(list(product([0, 1], repeat=num_inputs)))
    if gate == "AND":
        outputs = np.all(inputs, axis=1, keepdims=True).astype(int)
    elif gate == "OR":
        outputs = np.any(inputs, axis=1, keepdims=True).astype(int)
    elif gate == "XOR":
        outputs = np.sum(inputs, axis=1, keepdims=True) % 2
    else:
        raise ValueError("Operação lógica não suportada")
    return inputs, outputs

# Função para treinar e exibir resultados de uma operação lógica
def train_and_evaluate(num_inputs, gate, activation="sigmoid", learning_rate=0.1, use_bias=True):
    inputs, expected_output = generate_truth_table(num_inputs, gate)

    nn = NeuralNetwork(num_inputs=num_inputs, num_hidden_neurons=num_inputs * 2, num_outputs=1,
                       activation=activation, learning_rate=learning_rate, use_bias=use_bias)

    print(f"\nOperação lógica: {gate}")
    print(f"Número de entradas: {num_inputs}")
    print(f"Taxa de aprendizado: {nn.learning_rate}")
    print(f"Bias utilizado: {nn.use_bias}")

    print("Treinando...")
    errors = nn.train(inputs, expected_output, epochs=10000)

    # Plotar erro ao longo das épocas
    plt.figure(figsize=(10, 6))
    plt.plot(errors, label=f"Erro médio por época ({gate})", color="blue")
    plt.xlabel("Épocas")
    plt.ylabel("Erro médio")
    plt.title(f"Erro ao longo do treinamento para {gate} com {num_inputs} entradas")
    plt.legend()
    plt.grid()
    plt.show()

    print("Resultados:")
    for i, input_set in enumerate(inputs):
        predicted_output = nn.forward(input_set.reshape(1, -1))
        print(f"Entrada: {input_set}, Previsto: {predicted_output.round()}, Esperado: {expected_output[i]}")

# Main
def main():
    num_inputs = int(input("Insira o número de entradas: "))
    gate = input("Escolha a operação lógica (AND, OR, XOR): ").strip().upper()

    train_and_evaluate(num_inputs, gate)

if __name__ == "__main__":
    main()
