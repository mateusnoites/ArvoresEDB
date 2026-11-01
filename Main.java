import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Arvore avl = new AVL();
        Arvore llrb = new LLRB();

        Scanner scan = new Scanner(System.in);

        while (true) {
            ConsoleUtils.clearScreen();
            System.out.println("Qual árvore usar?");
            System.out.println("[1] AVL");
            System.out.println("[2] Rubro-negra");
            System.out.println("[0] Finalizar");
            System.out.print("> ");

            Integer opcao = lerOpcao(scan);
            if (opcao == null) continue;

            switch (opcao) {
                case 1 -> usar(avl, scan);
                case 2 -> usar(llrb, scan);
                case 0 -> {
                    return;
                }
                default -> ConsoleUtils.mostrarErro("Opção inválida");
            }
        }
    }

    private static Integer lerOpcao(Scanner scan) {
        try {
            return Integer.parseInt(scan.nextLine().trim());
        } catch (NumberFormatException e) {
            ConsoleUtils.mostrarErro("Entrada inválida: digite apenas números.");
            return null;
        }
    }

    private static void usar(Arvore arvore, Scanner scan) {
        while (true) {
            ConsoleUtils.menu(arvore);

            Integer op = lerOpcao(scan);
            if (op == null) continue;

            switch (op) {
                case 1 -> {
                    Integer valor = ConsoleUtils.lerInteiro(scan, "Valor a inserir: ");
                    if (valor == null) continue;
                    System.out.println(arvore.inserir(valor)
                            ? valor + " inserido."
                            : valor + " já existe; inserção ignorada.");
                    ConsoleUtils.pausar(scan);
                }
                case 2 -> {
                    Integer valor = ConsoleUtils.lerInteiro(scan, "Valor a remover: ");
                    if (valor == null) continue;
                    System.out.println(arvore.remover(valor)
                            ? valor + " removido."
                            : valor + " não existe; nada foi removido.");
                    ConsoleUtils.pausar(scan);
                }
                case 3 -> {
                    Integer valor = ConsoleUtils.lerInteiro(scan, "Valor a buscar: ");
                    if (valor == null) continue;
                    System.out.println(arvore.buscar(valor)
                            ? valor + " encontrado."
                            : valor + " não encontrado.");
                    ConsoleUtils.pausar(scan);
                }
                case 4 -> {
                    arvore.emOrdem();
                    ConsoleUtils.pausar(scan);
                }
                case 5 -> {
                    arvore.visualizar();
                    ConsoleUtils.pausar(scan);
                }
                case 6 -> {
                    System.out.println("Altura: " + arvore.altura());
                    System.out.println("Rotações: " + arvore.rotacoes());
                    System.out.println("Inversões de cor: " + arvore.ajustesDeCor());
                    System.out.println("Propriedades preservadas: " + (arvore.valida() ? "sim" : "NÃO"));
                    ConsoleUtils.pausar(scan);
                }
                case 0 -> {
                    return;
                }
                default -> ConsoleUtils.mostrarErro("Opção inválida");
            }
        }
    }
}
