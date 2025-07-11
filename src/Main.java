import java.util.Scanner;

import comandos.ConsultarLivroComando;
import comandos.ConsultarNotificacoesComando;
import comandos.ConsultarUsuarioComando;
import comandos.DevolucaoComando;
import comandos.EmprestimoComando;
import comandos.ExecutorDeComandos;
import comandos.ObservadorComando;
import comandos.ReservaComando;
import repositorio.Cadastro;
import repositorio.MemoriaRepositorio;
import repositorio.SistemaBiblioteca;

public class Main {
    private static final SistemaBiblioteca sistema = new SistemaBiblioteca(MemoriaRepositorio.getInstancia());
    private static final ExecutorDeComandos executor = new ExecutorDeComandos();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Cadastro.cadastrar(MemoriaRepositorio.getInstancia());
        System.out.println("Sistema inicializado com dados padrão!");

        registrarComandos();

        System.out.println("\n=== SISTEMA BIBLIOTECA ===");
        System.out.println("Comandos disponíveis:");
        System.out.println("  emp <usuário> <livro>    - Realizar empréstimo");
        System.out.println("  dev <usuário> <livro>    - Realizar devolução");
        System.out.println("  res <usuário> <livro>    - Fazer reserva");
        System.out.println("  obs <usuário> <livro>    - Fazer observação");
        System.out.println("  liv <livro>              - Informações do Livro");
        System.out.println("  usu <usuario>            - Informações do Usuario");
        System.out.println("  ntf <usuario>            - Notificações do Usuario");
        System.out.println("  sai                      - Sair do sistema");

        while (true) {
            System.out.print("\n> ");
            String entrada = scanner.nextLine().trim();
            if (entrada.equalsIgnoreCase("sai")) break;

            String resposta = executor.executar(entrada);
            System.out.println(resposta);
        }

        System.out.println("Sistema encerrado.");
    }

    private static void registrarComandos() {
        executor.registrar("emp", new EmprestimoComando(sistema));
        executor.registrar("dev", new DevolucaoComando(sistema));
        executor.registrar("res", new ReservaComando(sistema));
        executor.registrar("obs", new ObservadorComando(sistema));
        executor.registrar("liv", new ConsultarLivroComando(sistema));
        executor.registrar("usu", new ConsultarUsuarioComando(sistema));
        executor.registrar("ntf", new ConsultarNotificacoesComando(sistema));
    }
}
