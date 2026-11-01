import java.util.Scanner;

public class ConsoleUtils {
    static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    static void mostrarErro(String mensagem) {
        System.out.println(mensagem);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        ConsoleUtils.clearScreen();
    }

    static void menu(Arvore a) {
        clearScreen();
        System.out.printf("[Árvore %s]\n", a.nome());
        System.out.println();
        System.out.println("[1] Inserir elemento");
        System.out.println("[2] Remover elemento");
        System.out.println("[3] Buscar elemento");
        System.out.println("[4] Percurso em ordem");
        System.out.println("[5] Visualizar árvore");
        System.out.println("[6] Estatísticas (altura, rotações)");
        System.out.println("[0] Voltar");
        System.out.println();
        System.out.print("> ");
    }

    static Integer lerInteiro(Scanner scan, String rotulo) {
        System.out.print(rotulo);
        try {
            return Integer.parseInt(scan.nextLine().trim());
        } catch (NumberFormatException e) {
            mostrarErro("Entrada inválida: digite apenas números inteiros.");
            return null;
        }
    }

    static void pausar(Scanner scan) {
        System.out.println();
        System.out.print("Pressione ENTER para continuar...");
        scan.nextLine();
    }
}
