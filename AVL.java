import java.util.ArrayList;
import java.util.List;

public class AVL implements Arvore {
    private class No {
        int chave, altura = 1;
        No esq, dir;
        No(int chave) { this.chave = chave; }
    }

    @Override 
    public String nome() { return "AVL"; }

    private No raiz;
    private int rotacoes;

    private int altura(No n) { return n == null ? 0 : n.altura; }

    private void atualizar(No n) { n.altura = 1 + Math.max(altura(n.esq), altura(n.dir)); }

    private int fator(No n) { return n == null ? 0 : altura(n.esq) - altura(n.dir); }

    private No rotDir(No y) {
        rotacoes++;
        No x = y.esq;
        y.esq = x.dir;
        x.dir = y;
        atualizar(y);
        atualizar(x);
        return x;
    }

    private No rotEsq(No x) {
        rotacoes++;
        No y = x.dir;
        x.dir = y.esq;
        y.esq = x;
        atualizar(x);
        atualizar(y);
        return y;
    }

    private No balancear(No n) {
        atualizar(n);
        if (fator(n) > 1) {
            if (fator(n.esq) < 0) n.esq = rotEsq(n.esq);
            return rotDir(n);
        }
        if (fator(n) < -1) {
            if (fator(n.dir) > 0) n.dir = rotDir(n.dir);
            return rotEsq(n);
        }
        return n;
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
        return true;
    }

    private No inserir(No n, int chave) {
        if (n == null) return new No(chave);
        if (chave < n.chave) n.esq = inserir(n.esq, chave);
        else if (chave > n.chave) n.dir = inserir(n.dir, chave);
        else return n;
        return balancear(n);
    }

    @Override
    public boolean remover(int chave) {
        if (!buscar(chave)) return false;
        raiz = remover(raiz, chave);
        return true;
    }

    private No remover(No n, int chave) {
        if (n == null) return null;
        if (chave < n.chave) n.esq = remover(n.esq, chave);
        else if (chave > n.chave) n.dir = remover(n.dir, chave);
        else {
            if (n.esq == null) return n.dir;
            if (n.dir == null) return n.esq;
            No sucessor = n.dir;
            while (sucessor.esq != null) sucessor = sucessor.esq;
            n.chave = sucessor.chave;
            n.dir = remover(n.dir, sucessor.chave);
        }
        return balancear(n);
    }

    @Override 
    public void emOrdem() {
        emOrdem(raiz);
        System.out.println();
    }

    private void emOrdem(No n) {
        if (n == null) return;
        emOrdem(n.esq);
        System.out.print(n.chave + " ");
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
        System.out.println("    ".repeat(nivel) + n.chave);
        visualizar(n.esq, nivel + 1);
    }

    @Override
    public int altura() { return altura(raiz); }

    @Override
    public int rotacoes() { return rotacoes; }

    @Override
    public int ajustesDeCor() { return 0; }

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
    public boolean valida() { return verificar(raiz, Long.MIN_VALUE, Long.MAX_VALUE) >= 0; }

    private int verificar(No n, long min, long max) {
        if (n == null) return 0;
        if (n.chave <= min || n.chave >= max) return -1;
        int he = verificar(n.esq, min, n.chave);
        int hd = verificar(n.dir, n.chave, max);
        if (he < 0 || hd < 0) return -1;
        if (Math.abs(he - hd) > 1) return -1;
        int h = 1 + Math.max(he, hd);
        return h == n.altura ? h : -1;
    }
}
