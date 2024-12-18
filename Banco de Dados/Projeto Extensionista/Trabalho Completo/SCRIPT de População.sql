INSERT INTO dentista (ID, Nome, Telefone, CRO, Especialidade, Email)
VALUES (1, 'Ana Clara', '98457-4436','12345', 'Ortodontia', 'anaclaraamfrocha@gmail.com');

INSERT INTO paciente (IdPaciente, Nome, DataNasc, Endereço, Telefone, Email, CPF)
VALUES (1, 'Diego', '04-11-2004', 'Rua Cecília Fonseca Coutinho', '31 988461110', 'polanskidiego@gmail.com', '15072726690');

INSERT INTO consulta (ID, Data, Hora, Status, Dentista_ID, Recepicionista_ID, Paciente_idPaciente, Pagamento_ID, Pagamento_Valor, Tratamento_ID)
VALUES (1, '21-04-2004', '12:00', 'Em adamento', '1', '1', '1', '1', '1', '300', '1');

INSERT INTO fornecedor (ID, Nome, DataNasc, Endereço, Telefone)
VALUES (1, 'Lucca', '08-05-2004', 'Rua Serro', '31 988461111');

INSERT INTO materialodontologico (ID, Nome, Descrição, Quantidade, Fornecedor_ID)
VALUES (1, 'Bisturi', 'Para cortar', '100', '1');

INSERT INTO pagamento (ID, Método, Data, Valor)
VALUES (1, 'Débito', '12-12-2022', '100');

INSERT INTO recepicionista (ID, Nome, Telefone, Email)
VALUES (1, 'Beatriz', '12-09-2004', 'Bia@gmail.com');

INSERT INTO tratamento (ID, Nome, Descrição, Custo)
VALUES (1, 'Cirurgia', 'exração de dente', '100');

INSERT INTO gasto (MeterialOdontolofico_ID, Tratamento_ID)
VALUES (1, 1);