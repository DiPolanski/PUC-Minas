class Celula {
    int elemento;
    Celula esq, dir, inf, sup;

    Celula() {
        this(0);
    }

    Celula(int elemento) {
        this(elemento, null, null, null, null);
    }

    Celula(int elemento, Celula esq, Celula dir, Celula sup, Celula inf) {
        this.elemento = elemento;
        this.esq = esq;
        this.dir = dir;
        this.sup = sup;
        this.inf = inf;
    }
}

class Matriz {
    int linha;
    int coluna;
    Celula inicio;

    Matriz() {
        this(0, 0);
    }

    Matriz(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
    }

    void criarMatriz() {
        Celula u = new Celula();
        inicio = u;
        Celula y = inicio;
        for (int j = 1; j < coluna; j++) {
            u.dir = new Celula();
            u.dir.esq = u;
            u = u.dir;
        }

        for (int i = 1; i < linha; i++) {
            y.inf = new Celula();
            y.inf.sup = y;
            y = y.inf;
            u = y;

            for (int j = 1; j < coluna; j++) {
                u.dir = new Celula();
                u.dir.esq = u;
                u = u.dir;
                u.sup = u.esq.sup.dir;
                u.sup.inf = u;
            }
        }
    }

    void inserir(int array[]) {
        Celula y1, u1;
        y1 = u1 = inicio;
        int index = 0;
        for (int i = 0; i < linha; i++) {
            for (int j = 0; j < coluna; j++) {
                u1.elemento = array[index++];
                u1 = u1.dir;
            }
            y1 = y1.inf;
            u1 = y1;
        }
    }

    void ler() {
        int x = MyIO.readInt();
        Celula u = new Celula(x);
        inicio = u;
        Celula y = inicio;
        for (int j = 1; j < coluna; j++) {
            x = MyIO.readInt();
            u.dir = new Celula(x);
            u.dir.esq = u;
            u = u.dir;
        }

        for (int i = 1; i < linha; i++) {
            x = MyIO.readInt();
            y.inf = new Celula(x);
            y.inf.sup = y;
            y = y.inf;
            u = y;

            for (int j = 1; j < coluna; j++) {
                x = MyIO.readInt();
                u.dir = new Celula(x);
                u.dir.esq = u;
                u = u.dir;
                u.sup = u.esq.sup.dir;
                u.sup.inf = u;
            }
        }
    }

    void mostrar() {
        Celula y = inicio;
        Celula u = inicio;
        for (int i = 0; i < linha; i++) {
            for (int j = 0; j < coluna; j++) {
                System.out.print(u.elemento + " ");
                u = u.dir;
            }
            System.out.println();
            y = y.inf;
            u = y;
        }
    }

    Matriz soma(Matriz m2) {
        Celula y1, y2, u1, u2;
        y1 = u1 = this.inicio;
        y2 = u2 = m2.inicio;
        int index = 0;
        int tmp[] = new int[linha * coluna];
        for (int i = 0; i < linha; i++) {
            for (int j = 0; j < coluna; j++) {
                tmp[index++] = u1.elemento + u2.elemento;
                u1 = u1.dir;
                u2 = u2.dir;
            }
            y1 = y1.inf;
            u1 = y1;
            y2 = y2.inf;
            u2 = y2;
        }
        Matriz soma = new Matriz(linha, coluna);
        soma.criarMatriz();
        soma.inserir(tmp);
        return soma;
    }

    void mostrarDiagonalPrincipal() {
        Celula u, y;
        u = y = inicio;
        for (int i = 0; i < linha; i++) {
            for (int j = 0; j < coluna; j++) {
                if (i == j)
                    System.out.print(u.elemento + " ");
                u = u.dir;
            }
            y = y.inf;
            u = y;
        }
        System.out.println();
    }

    void mostrarDiagonalSecundaria() {
        Celula u, y;
        u = y = inicio;
        for (int i = 0; i < linha; i++) {
            for (int j = 0; j < coluna; j++) {
                if (i + j == linha - 1)
                    System.out.print(u.elemento + " ");
                u = u.dir;
            }
            y = y.inf;
            u = y;
        }
        System.out.println();
    }

    Matriz multiplicacao(Matriz m2) {
        Celula y1, y2, u1, u2;
        y1 = u1 = this.inicio;
        y2 = u2 = m2.inicio;
        int index = 0;
        int tmp[] = new int[linha * coluna];
        for (int i = 0; i < coluna; i++) {
            for (int t = 0; t < linha; t++) {
                for (int j = 0; j < linha; j++) {
                    tmp[index] += u1.elemento * y2.elemento;
                    u1 = u1.dir;
                    y2 = y2.inf;
                }
                index++;
                u1 = y1;
                u2 = u2.dir;
                y2 = u2;
            }
            y1 = y1.inf;
            u1 = y1;
            u2 = y2 = m2.inicio;
        }
        Matriz multi = new Matriz(linha, coluna);
        multi.criarMatriz();
        multi.inserir(tmp);
        return multi;
    }

    public static void main(String args[]) {
        int instancias = MyIO.readInt();
        for (int i = 0; i < instancias; i++) {
            int linha = MyIO.readInt();
            int coluna = MyIO.readInt();
            Matriz m1 = new Matriz(linha, coluna);
            m1.ler();
            linha = MyIO.readInt();
            coluna = MyIO.readInt();
            Matriz m2 = new Matriz(linha, coluna);
            m2.ler();
            Matriz soma = m1.soma(m2);
            m1.mostrarDiagonalPrincipal();
            m1.mostrarDiagonalSecundaria();
            soma.mostrar();
            Matriz multi = m1.multiplicacao(m2);
            multi.mostrar();

        }

    }
}