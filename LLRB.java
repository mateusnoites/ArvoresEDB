import java.util.ArrayList;
import java.util.List;

public class LLRB implements Arvore {
    private static final boolean VERMELHO = true, PRETO = false;

    private class No {
        int chave;
        boolean cor = VERMELHO;
        No esq, dir;
        No(int chave) { this.chave = chave; }
    }

    @Override 
    public String nome() { return "Rubro-negra"; }

    private No raiz;
    private int rotacoes, ajustesDeCor;

    private boolean vermelho(No n) { return n != null && n.cor == VERMELHO; }

    private No rotEsq(No h) {
        rotacoes++;
        No x = h.dir;
        h.dir = x.esq;
        x.esq = h;
        x.cor = h.cor;
        h.cor = VERMELHO;
        return x;
    }

    private No rotDir(No h) {
        rotacoes++;
        No x = h.esq;
        h.esq = x.dir;
        x.dir = h;
        x.cor = h.cor;
        h.cor = VERMELHO;
        return x;
    }

    private void inverterCores(No h) {
        ajustesDeCor++;
        h.cor = !h.cor;
        h.esq.cor = !h.esq.cor;
        h.dir.cor = !h.dir.cor;
    }

    private No balancear(No h) {
        if (vermelho(h.dir) && !vermelho(h.esq)) h = rotEsq(h);
        if (vermelho(h.esq) && vermelho(h.esq.esq)) h = rotDir(h);
        if (vermelho(h.esq) && vermelho(h.dir)) inverterCores(h);
        return h;
    }

    @Override 
    public boolean buscar(int chave) {
        No n = raiz;
        while (n != null) {
            if (chave < n.chave) n = n.esq;
            else if (chave > n.chave) n = n.dir;
            else return true;
        }
        return false;
    }

    @Override
    public boolean inserir(int chave) {
        if (buscar(chave)) return false;
        raiz = inserir(raiz, chave);
        raiz.cor = PRETO;
        return true;
    }

    private No inserir(No h, int chave) {
        if (h == null) return new No(chave);
        if (chave < h.chave) h.esq = inserir(h.esq, chave);
        else if (chave > h.chave) h.dir = inserir(h.dir, chave);
        return balancear(h);
    }

    private No moverVermelhoEsq(No h) {
        inverterCores(h);
        if (vermelho(h.dir.esq)) {
            h.dir = rotDir(h.dir);
            h = rotEsq(h);
            inverterCores(h);
        }
        return h;
    }

    private No moverVermelhoDir(No h) {
        inverterCores(h);
        if (vermelho(h.esq.esq)) {
            h = rotDir(h);
            inverterCores(h);
        }
        return h;
    }

    private No removerMin(No h) {
        if (h.esq == null) return null;
        if (!vermelho(h.esq) && !vermelho(h.esq.esq)) h = moverVermelhoEsq(h);
        h.esq = removerMin(h.esq);
        return balancear(h);
    }

    @Override 
    public boolean remover(int chave) {
        if (!buscar(chave)) return false;
        if (!vermelho(raiz.esq) && !vermelho(raiz.dir)) raiz.cor = VERMELHO;
        raiz = remover(raiz, chave);
        if (raiz != null) raiz.cor = PRETO;
        return true;
    }

    private No remover(No h, int chave) {
        if (chave < h.chave) {
            if (!vermelho(h.esq) && !vermelho(h.esq.esq)) h = moverVermelhoEsq(h);
            h.esq = remover(h.esq, chave);
        } else {
            if (vermelho(h.esq)) h = rotDir(h);
            if (chave == h.chave && h.dir == null) return null;
            if (!vermelho(h.dir) && !vermelho(h.dir.esq)) h = moverVermelhoDir(h);
            if (chave == h.chave) {
                No sucessor = h.dir;
                while (sucessor.esq != null) sucessor = sucessor.esq;
                h.chave = sucessor.chave;
                h.dir = removerMin(h.dir);
            } else {
                h.dir = remover(h.dir, chave);
            }
        }
        return balancear(h);
    }

    @Override 
    public void emOrdem() {
        emOrdem(raiz);
        System.out.println();
    }

    private void emOrdem(No n) {
        if (n == null) return;
        emOrdem(n.esq);
        System.out.print(n.chave + (n.cor == VERMELHO ? "(V) " : "(P) "));
        emOrdem(n.dir);
    }

    @Override 
    public void visualizar() {
        if (raiz == null) System.out.println("(arvore vazia)");
        else visualizar(raiz, 0);
    }

    private void visualizar(No n, int nivel) {
        if (n == null) return;
        visualizar(n.dir, nivel + 1);
        System.out.println("    ".repeat(nivel) + n.chave + (n.cor == VERMELHO ? " (V)" : " (P)"));
        visualizar(n.esq, nivel + 1);
    }

    @Override
    public int altura() { return altura(raiz); }

    private int altura(No n) { return n == null ? 0 : 1 + Math.max(altura(n.esq), altura(n.dir)); }

    @Override
    public int rotacoes() { return rotacoes; }

    @Override
    public int ajustesDeCor() { return ajustesDeCor; }

    @Override
    public List<Integer> elementos() {
        List<Integer> lista = new ArrayList<>();
        coletar(raiz, lista);
        return lista;
    }

    private void coletar(No n, List<Integer> lista) {
        if (n == null) return;
        coletar(n.esq, lista);
        lista.add(n.chave);
        coletar(n.dir, lista);
    }

    @Override
    public boolean valida() {
        if (vermelho(raiz)) return false;
        return verificar(raiz, Long.MIN_VALUE, Long.MAX_VALUE) >= 0;
    }

    private int verificar(No n, long min, long max) {
        if (n == null) return 1;
        if (n.chave <= min || n.chave >= max) return -1;
        if (vermelho(n.dir)) return -1;
        if (vermelho(n) && vermelho(n.esq)) return -1;
        int be = verificar(n.esq, min, n.chave);
        int bd = verificar(n.dir, n.chave, max);
        if (be < 0 || bd < 0 || be != bd) return -1;
        return be + (vermelho(n) ? 0 : 1);
    }
}
