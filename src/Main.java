package main.java.com.biblioteca;

import main.java.com.biblioteca.enums.StatusEmprestimo;
import main.java.com.biblioteca.enums.StatusExemplar;
import main.java.com.biblioteca.enums.TipoUsuario;
import main.java.com.biblioteca.modelos.*;
import main.java.com.biblioteca.observer.LivroObservable;
import main.java.com.biblioteca.observer.ProfessorObserver;
import main.java.com.biblioteca.repositorio.BibliotecaRepositorio;
import main.java.com.biblioteca.repositorio.Cadastro;
import main.java.com.biblioteca.repositorio.MemoriaRepositorio;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final BibliotecaRepositorio repositorio = MemoriaRepositorio.getInstancia();
    private static final SistemaBiblioteca sistemaBiblioteca = new SistemaBiblioteca(repositorio);
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        inicializarDados(); // Popula com dados de teste
        exibirMenuPrincipal();
    }

    private static void inicializarDados() {
        // Chama o cadastro padrão que já está implementado
        Cadastro.cadastrar(repositorio);
        System.out.println("Sistema inicializado com dados padrão!");
    }

    private static void exibirMenuPrincipal() {
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
            String comando = scanner.nextLine().trim();
            
            if (comando.equalsIgnoreCase("sai")) {
                System.out.println("Sistema encerrado.");
                break;
            }
            
            String resultado = processarComando(comando);
            System.out.println(resultado);
        }
        scanner.close();
    }

private static String processarComando(String comando) {
    String[] partes = comando.split(" ");
    String acao = partes[0].toLowerCase();
    
    try {
        switch (acao) {
            case "emp":
                if (partes.length == 3) {
                    int codigoUsuario = Integer.parseInt(partes[1]);
                    int codigoLivro = Integer.parseInt(partes[2]);
                    return sistemaBiblioteca.realizarEmprestimo(codigoUsuario, codigoLivro);
                }
                return "Formato inválido! Use: emp <usuário> <livro>";
                
            case "dev":
                if (partes.length == 3) {
                    int codigoUsuario = Integer.parseInt(partes[1]);
                    int codigoLivro = Integer.parseInt(partes[2]);
                    return sistemaBiblioteca.realizarDevolucao(codigoUsuario, codigoLivro);
                }
                return "Formato inválido! Use: dev <usuário> <livro>";
            
            case "res":
                if (partes.length == 3) {
                    int codigoUsuario = Integer.parseInt(partes[1]);
                    int codigoLivro = Integer.parseInt(partes[2]);
                    return sistemaBiblioteca.realizarReserva(codigoUsuario, codigoLivro);
                }
                return "Formato inválido! Use: res <usuário> <livro>";
            case "obs":
                if (partes.length == 3) {
                    int codigoUsuario = Integer.parseInt(partes[1]);
                    int codigoLivro = Integer.parseInt(partes[2]);
                    return sistemaBiblioteca.registrarObservador(codigoUsuario, codigoLivro);
                }
                return "Formato inválido! Use: obs <usuário> <livro>";

            case "liv":
                if (partes.length == 2) {
                    int codigoLivro = Integer.parseInt(partes[1]);
                    return sistemaBiblioteca.consultarLivro(codigoLivro);
                }
                return "Formato inválido! Use: liv <código livro>";

            case "usu":
                if (partes.length == 2) {
                    int codigoUsuario = Integer.parseInt(partes[1]);
                    return sistemaBiblioteca.consultarUsuario(codigoUsuario);
                }
                return "Formato inválido! Use: usu <código usuário>";

            case "ntf":
                if (partes.length == 2) {
                    int codigoUsuario = Integer.parseInt(partes[1]);
                    return sistemaBiblioteca.consultarNotificacoes(codigoUsuario);
                }
                return "Formato inválido! Use: ntf <código usuário>";


            default:
                return "Comando não reconhecido!";
        }
    } catch (NumberFormatException e) {
        return "Código inválido! Deve ser um número.";
    }
}
}
