import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;
import java.util.function.Supplier;

public class Testes {
    private static int total, falhas;

    private static void verificar(String descricao, boolean condicao) {
        total++;
        if (!condicao) falhas++;
        System.out.println((condicao ? "  [OK]    " : "  [FALHA] ") + descricao);
    }

    private static List<Integer> sequencia(String tipo, int n) {
        List<Integer> v = new ArrayList<>();
        for (int i = 1; i <= n; i++) v.add(i);
        if (tipo.equals("decrescente")) java.util.Collections.reverse(v);
        if (tipo.equals("aleatória")) java.util.Collections.shuffle(v, new Random(42));
        return v;
    }

    private static void cenarioBasico(Supplier<Arvore> fabrica) {
        Arvore a = fabrica.get();
        System.out.println("\n== " + a.nome() + " ==");

        verificar("árvore vazia: buscar retorna false", !a.buscar(1));
        verificar("árvore vazia: remover retorna false", !a.remover(1));
        verificar("árvore vazia: elementos vazio, altura 0 e válida",
                a.elementos().isEmpty() && a.altura() == 0 && a.valida());

        verificar("um elemento: inserir 10", a.inserir(10));
        verificar("um elemento: altura 1 e válida", a.altura() == 1 && a.valida());
        verificar("um elemento: duplicata rejeitada", !a.inserir(10));
        verificar("um elemento: remover 10 deixa a árvore vazia",
                a.remover(10) && a.elementos().isEmpty() && a.valida());

        for (int k : new int[]{50, 30, 70, 20, 40, 60, 80}) a.inserir(k);
        verificar("buscar existente (60)", a.buscar(60));
        verificar("buscar inexistente (65)", !a.buscar(65));
        verificar("remover inexistente (65) não altera a árvore",
                !a.remover(65) && a.elementos().equals(List.of(20, 30, 40, 50, 60, 70, 80)));

        verificar("remover folha (20)", a.remover(20) && a.valida()
                && a.elementos().equals(List.of(30, 40, 50, 60, 70, 80)));
        verificar("remover nó com um filho (30)", a.remover(30) && a.valida()
                && a.elementos().equals(List.of(40, 50, 60, 70, 80)));
        verificar("remover nó com dois filhos (70)", a.remover(70) && a.valida()
                && a.elementos().equals(List.of(40, 50, 60, 80)));
        verificar("remover a raiz (50)", a.remover(50) && a.valida()
                && a.elementos().equals(List.of(40, 60, 80)));
    }

    private static void cenarioInsercao(Supplier<Arvore> fabrica, String tipo, int n) {
        Arvore a = fabrica.get();
        List<Integer> entrada = sequencia(tipo, n);
        boolean sempreValida = true;
        for (int k : entrada) {
            a.inserir(k);
            if (!a.valida()) sempreValida = false;
        }
        verificar(a.nome() + ", " + tipo + " (" + n + "): válida após cada inserção", sempreValida);
        verificar(a.nome() + ", " + tipo + " (" + n + "): em ordem crescente e completa",
                a.elementos().equals(sequencia("crescente", n)));
    }

    private static void cenarioAleatorio(Supplier<Arvore> fabrica) {
        Arvore a = fabrica.get();
        TreeSet<Integer> ref = new TreeSet<>();
        Random r = new Random(7);
        boolean consistente = true;
        for (int i = 0; i < 20000; i++) {
            int k = r.nextInt(500);
            boolean ok;
            if (r.nextInt(3) > 0) ok = a.inserir(k) == ref.add(k);
            else ok = a.remover(k) == ref.remove(k);
            if (!ok || !a.valida() || !a.elementos().equals(new ArrayList<>(ref))) consistente = false;
        }
        verificar(a.nome() + ": 20000 operações aleatórias iguais ao TreeSet e sempre válidas", consistente);
    }

    private static void comparar(String titulo, int n, boolean comRemocao) {
        System.out.println("\n-- " + titulo + " (n = " + n + ") --");
        System.out.printf("%-14s %-8s %-9s %-14s %-9s%n", "Árvore", "Altura", "Rotações", "Inv. de cor", "Válida");
        for (Supplier<Arvore> fabrica : List.<Supplier<Arvore>>of(AVL::new, LLRB::new)) {
            Arvore a = fabrica.get();
            List<Integer> entrada = sequencia(titulo.split(" ")[0], n);
            for (int k : entrada) a.inserir(k);
            if (comRemocao) {
                List<Integer> remocao = sequencia("aleatória", n);
                for (int i = 0; i < n / 2; i++) a.remover(remocao.get(i));
            }
            System.out.printf("%-14s %-8d %-9d %-14d %-9s%n",
                    a.nome(), a.altura(), a.rotacoes(), a.ajustesDeCor(), a.valida() ? "sim" : "NÃO");
        }
    }

    public static void main(String[] args) {
        List<Supplier<Arvore>> fabricas = List.of(AVL::new, LLRB::new);

        System.out.println("### TESTES FUNCIONAIS ###");
        for (Supplier<Arvore> f : fabricas) cenarioBasico(f);

        System.out.println("\n### INSERÇÃO EM DIFERENTES ORDENS ###");
        for (Supplier<Arvore> f : fabricas)
            for (String tipo : new String[]{"crescente", "decrescente", "aleatória"})
                cenarioInsercao(f, tipo, 1000);

        System.out.println("\n### OPERAÇÕES ALEATÓRIAS ###");
        for (Supplier<Arvore> f : fabricas) cenarioAleatorio(f);

        System.out.println("\n### COMPARAÇÃO (mesmas sequências nas duas árvores) ###");
        for (int n : new int[]{1000, 10000, 100000}) {
            comparar("crescente - só inserções", n, false);
            comparar("decrescente - só inserções", n, false);
            comparar("aleatória - só inserções", n, false);
            comparar("aleatória - inserções e remoção de metade", n, true);
        }

        System.out.println("\nTotal: " + total + " verificações, " + falhas + " falhas.");
        if (falhas > 0) System.exit(1);
    }
}
