import java.util.List;

public interface Arvore {
    boolean inserir(int chave);
    boolean remover(int chave);
    boolean buscar(int chave);
    void emOrdem();
    void visualizar();
    String nome();
    int altura();
    int rotacoes();
    int ajustesDeCor();
    List<Integer> elementos();
    boolean valida();
}
