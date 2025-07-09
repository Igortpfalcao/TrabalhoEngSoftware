package main.java.com.biblioteca;

import main.java.com.biblioteca.enums.StatusExemplar;
import main.java.com.biblioteca.modelos.*;
import main.java.com.biblioteca.repositorio.BibliotecaRepositorio;
import main.java.com.biblioteca.repositorio.MemoriaRepositorio;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final BibliotecaRepositorio repositorio = new MemoriaRepositorio();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        exibirMenuPrincipal();
    }

    private static void exibirMenuPrincipal() {
        System.out.println("\n=== SISTEMA BIBLIOTECA ===");
        System.out.println("Comandos disponíveis:");
        System.out.println("  emp <usuário> <livro>    - Realizar empréstimo");
        System.out.println("  dev <usuário> <exemplar>  - Realizar devolução");
        System.out.println("  res <usuário> <livro>     - Fazer reserva");
        System.out.println("  obs <usuário> <livro>     - Registrar observador");
        System.out.println("  liv <código>             - Consultar livro");
        System.out.println("  usu <código>             - Consultar usuário");
        System.out.println("  not <usuário>             - Consultar notificações");
        System.out.println("  sai                      - Sair do sistema");
        
        while (true) {
            System.out.print("\n> ");
            String comando = scanner.nextLine().trim();
            
            if (comando.equalsIgnoreCase("sai")) {
                System.out.println("Sistema encerrado.");
                break;
            }
            
            processarComando(comando);
        }
        scanner.close();
    }

    private static void processarComando(String comando) {
        String[] partes = comando.split(" ");
        String acao = partes[0].toLowerCase();
        
        try {
            switch (acao) {
                case "emp":
                    if (partes.length == 3) {
                        emprestarLivro(Integer.parseInt(partes[1]), Integer.parseInt(partes[2]));
                    } else {
                        System.out.println("Uso: emp <usuário> <livro>");
                    }
                    break;
                    
                case "dev":
                    if (partes.length == 3) {
                        devolverExemplar(Integer.parseInt(partes[1]), Integer.parseInt(partes[2]));
                    } else {
                        System.out.println("Uso: dev <usuário> <exemplar>");
                    }
                    break;
                    
                case "res":
                    if (partes.length == 3) {
                        fazerReserva(Integer.parseInt(partes[1]), Integer.parseInt(partes[2]));
                    } else {
                        System.out.println("Uso: res <usuário> <livro>");
                    }
                    break;
                    
                case "obs":
                    if (partes.length == 3) {
                        registrarObservador(Integer.parseInt(partes[1]), Integer.parseInt(partes[2]));
                    } else {
                        System.out.println("Uso: obs <usuário> <livro>");
                    }
                    break;
                    
                case "liv":
                    if (partes.length == 2) {
                        System.out.println(consultarLivro(Integer.parseInt(partes[1])));
                    } else {
                        System.out.println("Uso: liv <código>");
                    }
                    break;
                    
                case "usu":
                    if (partes.length == 2) {
                        consultarUsuario(Integer.parseInt(partes[1]));
                    } else {
                        System.out.println("Uso: usu <código>");
                    }
                    break;
                    
                case "not":
                    if (partes.length == 2) {
                        consultarNotificacoes(Integer.parseInt(partes[1]));
                    } else {
                        System.out.println("Uso: not <usuário>");
                    }
                    break;
                    
                default:
                    System.out.println("Comando inválido!");
                    exibirMenuPrincipal();
            }
        } catch (NumberFormatException e) {
            System.out.println("Erro: Códigos devem ser números inteiros!");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void emprestarLivro(int codigoUsuario, int codigoLivro) {
        // Implementação do empréstimo
        System.out.println("Empréstimo realizado para usuário " + codigoUsuario + ", livro " + codigoLivro);
    }

    private static void devolverExemplar(int codigoUsuario, int codigoExemplar) {
        // Implementação da devolução
        System.out.println("Devolução realizada para usuário " + codigoUsuario + ", exemplar " + codigoExemplar);
    }

    private static void fazerReserva(int codigoUsuario, int codigoLivro) {
        Usuario usuario = repositorio.buscarUsuario(codigoUsuario);
        Livro livro = repositorio.buscarLivro(codigoLivro);
        
        if (usuario == null || livro == null) {
            System.out.println("Usuário ou livro não encontrado!");
            return;
        }
        
        Reserva reserva = new Reserva(usuario, livro, LocalDate.now());
        repositorio.salvarReserva(reserva);
        System.out.println("Reserva registrada com sucesso!");
    }

    private static void registrarObservador(int codigoUsuario, int codigoLivro) {
        repositorio.registrarObservador(codigoUsuario, codigoLivro);
        System.out.println("Observador registrado com sucesso!");
    }

public static String consultarLivro(int codigoLivro) {
    // Busca o livro
    Livro livro = repositorio.buscarLivro(codigoLivro);
    if (livro == null) {
        return "Livro não encontrado!";
    }

    StringBuilder info = new StringBuilder();
    info.append("\n=== INFORMAÇÕES DO LIVRO ===\n");
    info.append("Título: ").append(livro.getTitulo()).append("\n");

    // 1. Informações de Reservas
    List<Reserva> reservas = repositorio.buscarReservasPorLivro(codigoLivro);
    info.append("\nReservas: ").append(reservas.size());
    
    if (!reservas.isEmpty()) {
        info.append("\nDetalhes das reservas:\n");
        for (Reserva res : reservas) {
            Usuario usuario = repositorio.buscarUsuario(res.getCodigo());
            info.append("- ").append(usuario != null ? usuario.getNome() : "Usuário desconhecido")
               .append(" (Data: ").append(res.getDataReserva()).append(")\n");
        }
    }

    // 2. Informações de Exemplares
    List<Exemplar> exemplares = repositorio.buscarExemplaresPorLivro(codigoLivro);
    info.append("\nExemplares: ").append(exemplares.size());
    
    for (Exemplar ex : exemplares) {
        info.append("\n- Código: ").append(ex.getCodigoExemplar())
           .append(" | Status: ").append(ex.getStatus());
        
        if (ex.getStatus() == StatusExemplar.EMPRESTADO) {
            Emprestimo emp = repositorio.buscarEmprestimoAtivoPorExemplar(ex.getCodigoExemplar());
            if (emp != null) {
                Usuario usuario = repositorio.buscarUsuario(emp.getCodigo());
                info.append("\n  Emprestado para: ")
                   .append(usuario != null ? usuario.getNome() : "Usuário desconhecido")
                   .append("\n  Data do empréstimo: ").append(emp.getDataEmprestimo())
                   .append("\n  Devolução prevista: ").append(emp.getDataDevolucaoPrevista());
            }
        }
    }

    return info.toString();
}

public static String consultarUsuario(int codigoUsuario) {
    Usuario usuario = repositorio.buscarUsuario(codigoUsuario);
    if (usuario == null) return "Usuário não encontrado!";
    
    StringBuilder info = new StringBuilder();
    info.append("Usuário: ").append(usuario.getNome()).append("\n");
    
    // Empréstimos
    info.append("Empréstimos:\n");
    for (Emprestimo emp : repositorio.buscarEmprestimosPorUsuario(codigoUsuario)) {
            Livro livro = repositorio.buscarLivro(Exemplar.getCodigoLivro());
            String titulo = livro != null ? livro.getTitulo() : "Desconhecido";
    }
    
    // Reservas
    info.append("Reservas:\n");
    for (Reserva res : repositorio.buscarReservasPorUsuario(codigoUsuario)) {
        info.append("- ").append(res.getLivro().getTitulo()).append("\n");
    }
    
    return info.toString();
}

    private static void consultarNotificacoes(int codigoUsuario) {
    int totalNotificacoes = repositorio.buscarTotalNotificacoes(codigoUsuario);
    System.out.println("Total de notificações recebidas: " + totalNotificacoes);
    
    // Mostrar detalhes das observações (opcional)
    List<Integer> livrosObservados = repositorio.buscarLivrosObservadosPorProfessor(codigoUsuario);
    if (!livrosObservados.isEmpty()) {
        System.out.println("\nLivros observados por este professor:");
        for (int codigoLivro : livrosObservados) {
            Livro livro = repositorio.buscarLivro(codigoLivro);
            int reservas = repositorio.buscarReservasPorLivro(codigoLivro).size();
            System.out.printf("- %s (Cód: %d) | Reservas atuais: %d%n", 
                livro.getTitulo(), codigoLivro, reservas);
        }
    }
}
}