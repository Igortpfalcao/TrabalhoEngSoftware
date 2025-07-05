public class Main {
    public static void main(String[] args) {
        Repositorio sistema = new Repositorio();
        java.util.Scanner scanner = new java.util.Scanner(System.in);

        System.out.println("Digite o comando 'liv' seguido do código do livro (ex: liv 123)");
        String input = scanner.nextLine();

        if (input.startsWith("liv ")) {
            try {
                int codigoLivro = Integer.parseInt(input.substring(4).trim());
                sistema.consultarLivro(codigoLivro);
            } catch (NumberFormatException e) {
                System.out.println("Código do livro deve ser um número inteiro.");
            }
        } else {
            System.out.println("Comando inválido. Use 'liv' seguido do código do livro (ex: liv 123).");
        }

        scanner.close();
    }
}
