# TP 3 – Backup compactado

**Grupo:** Diego Polanski, Guilherme de Almeida, Gabriel Rangel e Raphael Arnault 

Durante a execução do projeto, começamos criando no menu a possibilidade de fazer e recuperar backups, escolhendo a opção 6 e 7 respectivamente. Então fomos pensar como seria a organização para salvar esses backups. Então iriamos guarda-los com a data e hora que fizemos o backup. Contruimos uma função então para abstrair a versão do backup. Ao ter o momento exato que foi salvo, formatamos a string para criar o arquvio, a formatação foi "dd_mm_yy hh_mm". 
Então entramos no arquivo de registros e pegamos todos os registros válidos, compactamos usando o algoritmo LZW, e então salvamos o array de bytes no arquivo backup. Criamos assim a rotina de "cópias".

Para recuperar os backups, listamos para o usuário todos os arquivos dentro da pasta então ele escolheria digitando o nome do arquivo. A partir da escolha feita, chamamos a função de recuperar backup, onde ela guardará todos os dados do arquivo dentro de um array de bytes. Então, este array é encamihado para ser descompatado. E o resultado é guardado dentro de outro array de bytes.

Dessa forma a rotina de backups pode ser feita de forma completa e organizada, sendo possível a escolha de recuperação do usuário.

Acreditamos esse ter sido o TP mais difícil, por ter que lidar com diversos backups e não apenas um. Porém após pesquisas, entedemos a maneira mais fácil e completa de atingir todos os objetivos.

* Há uma rotina de compactação usando o algoritmo LZW para fazer backup dos arquivos?
  - Sim, é feita de forma organizada na função FazerBackup, que recebe como parâmetro a versão do backup. Dentro da função é feita a compactação de cada registro válido.
* Há uma rotina de descompactação usando o algoritmo LZW para recuperação dos arquivos?
  - Sim, a descompactação é feita a partir da escolha do usuário , após ser listado todos os backups com o nome de cada arquivo. Então o conteúdo do arquivo é todo descompactado e guardado dentro de um array de byte.
* O usuário pode escolher a versão a recuperar?
  - Sim, litamos todas as opções de backup disponíveis.
* Qual foi a taxa de compressão alcançada por esse backup? (Compare o tamanho dos arquivos compactados com os arquivos originais)
  - A taxa de compressão foi cerca de 3 vezes menor.
* O trabalho está funcionando corretamente?
  - O trabalho esta sim, funcionado perfeitamente, com tudo que foi pedido.
* O trabalho está completo?
  - Sim, está completo.
* O trabalho é original e não a cópia de um trabalho de um colega?
  - Trabalho é original, feito pelo grupo.
