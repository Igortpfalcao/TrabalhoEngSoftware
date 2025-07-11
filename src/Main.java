package main.java.com.biblioteca;

import comandos.*;
import java.util.Scanner;

public class Main {
    private static final SistemaBiblioteca sistema = new SistemaBiblioteca(MemoriaRepositorio.getInstancia());
    private static final ExecutorDeComandos executor = new ExecutorDeComandos();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Cadastro.cadastrar(MemoriaRepositorio.getInstancia());
        System.out.println("Sistema inicializado com dados padrão!");

        registrarComandos();

        System.out.println("\n=== SISTEMA BIBLIOTECA ===");
        System.out.println("Digite comandos ou 'sai' para encerrar.");

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
