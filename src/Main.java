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
    private static final BibliotecaRepositorio repositorio = new MemoriaRepositorio();
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
                    return realizarEmprestimo(codigoUsuario, codigoLivro);
                }
                return "Formato inválido! Use: emp <usuário> <livro>";
                
            case "dev":
                if (partes.length == 3) {
                    int codigoUsuario = Integer.parseInt(partes[1]);
                    int codigoLivro = Integer.parseInt(partes[2]);
                    return realizarDevolucao(codigoUsuario, codigoLivro);
                }
                return "Formato inválido! Use: dev <usuário> <livro>";
            
            case "res":
                if (partes.length == 3) {
                    int codigoUsuario = Integer.parseInt(partes[1]);
                    int codigoLivro = Integer.parseInt(partes[2]);
                    return realizarReserva(codigoUsuario, codigoLivro);
                }
                return "Formato inválido! Use: res <usuário> <livro>";
            case "obs":
                if (partes.length == 3) {
                    int codigoUsuario = Integer.parseInt(partes[1]);
                    int codigoLivro = Integer.parseInt(partes[2]);
                    return registrarObservador(codigoUsuario, codigoLivro);
                }
                return "Formato inválido! Use: obs <usuário> <livro>";

            case "liv":
                if (partes.length == 2) {
                    int codigoLivro = Integer.parseInt(partes[1]);
                    return consultarLivro(codigoLivro);
                }
                return "Formato inválido! Use: liv <código livro>";

            case "usu":
                if (partes.length == 2) {
                    int codigoUsuario = Integer.parseInt(partes[1]);
                    return consultarUsuario(codigoUsuario);
                }
                return "Formato inválido! Use: usu <código usuário>";

            case "ntf":
                if (partes.length == 2) {
                    int codigoUsuario = Integer.parseInt(partes[1]);
                    return consultarNotificacoes(codigoUsuario);
                }
                return "Formato inválido! Use: ntf <código usuário>";


            default:
                return "Comando não reconhecido!";
        }
    } catch (NumberFormatException e) {
        return "Código inválido! Deve ser um número.";
    }
}

private static String realizarEmprestimo(int codigoUsuario, int codigoLivro) {
    // 1. Busca usuário e livro
    Usuario usuario = repositorio.buscarUsuario(codigoUsuario);
    Livro livro = repositorio.buscarLivro(codigoLivro);
    
    if (usuario == null) return "Usuário não encontrado!";
    if (livro == null) return "Livro não encontrado!";
    
    if (usuario.isDevedor()) return "Usuário em débito! Regularize sua situação.";
    if (usuario.atingiuLimiteEmprestimos()) {
        return "Limite de empréstimos atingido! " +
               "(Máximo: " + usuario.getTipo().getLimiteEmprestimos() + " livros)";
    }
    
    if (usuario.getEmprestimosAtivos().stream()
        .anyMatch(emp -> emp.getExemplar().getLivro().getCodigo() == codigoLivro)) {
        return "Não foi possível realizar o empréstimo, pois o usuário já tem um exemplar deste mesmo livro em empréstimo no momento.";
    }

    // 2. Busca exemplares disponíveis para este livro
    List<Exemplar> exemplares = repositorio.buscarExemplaresDisponiveis(codigoLivro);
    if (exemplares.isEmpty()) {
        return "Não há exemplares disponíveis para este livro!";
    }

    // 3. Pega o primeiro exemplar disponível
    Exemplar exemplar = exemplares.get(0);
    
    // 4. Atualiza status e registra empréstimo
    exemplar.setStatus(StatusExemplar.EMPRESTADO);
    Emprestimo emprestimo = new Emprestimo(usuario, exemplar, LocalDate.now());
    
    // 5. Cancela reserva se existir
    // Dentro do realizarEmprestimo, após emprestar o exemplar:
    List<Reserva> reservas = repositorio.buscarReservasAtivasPorLivro(codigoLivro);
    if (!reservas.isEmpty()) {
    Reserva reserva = reservas.get(0);
    // Implementar lógica de notificação aqui
    reserva.setAtiva(false);
}

    usuario.adicionarEmprestimo(emprestimo);
    repositorio.salvarEmprestimo(emprestimo);

    return String.format(
        "Empréstimo realizado com sucesso!%n" +
        "Livro: %s%n" +
        "Exemplar: %d%n" +
        "Data de devolução: %s \n" +
        "Usuário: %s \n", 
        livro.getTitulo(),
        exemplar.getCodigo(),
        LocalDate.now().plusDays(usuario.getTipo().getTempoEmprestimoDias())
            .format(DateTimeFormatter.ISO_DATE),
        usuario.getNome()

    );
}
private static String realizarDevolucao(int codigoUsuario, int codigoLivro) {
    try {
        // 1. Busca usuário e livro
        Usuario usuario = repositorio.buscarUsuario(codigoUsuario);
        Livro livro = repositorio.buscarLivro(codigoLivro);
        
        if (usuario == null) return "Usuário não encontrado!";
        if (livro == null) return "Livro não encontrado!";
        
        // 2. Busca empréstimo ativo para este usuário e livro
        Emprestimo emprestimo = repositorio.buscarEmprestimoAtivoPorLivro(usuario, codigoLivro);
        
        if (emprestimo == null) {
            return "Este usuário não tem este livro emprestado atualmente!";
        }
        
        Exemplar exemplar = emprestimo.getExemplar();
        
        // 3. Processa a devolução
        exemplar.setStatus(StatusExemplar.DISPONIVEL);
        usuario.removerEmprestimo(emprestimo);
        LocalDate dataDevolucao = LocalDate.now();
        emprestimo.setDataDevolucaoReal(dataDevolucao);
        emprestimo.setStatus(StatusEmprestimo.DEVOLVIDO);

        List<Reserva> reservas = repositorio.buscarReservasAtivasPorLivro(codigoLivro);
        if (!reservas.isEmpty()) {
            Reserva reserva = reservas.get(0); // Pega a reserva com maior prioridade
            reserva.setAtiva(false);
    
            // Implemente sua lógica de notificação aqui
            System.out.println("Notificação: Exemplar disponível para reserva de " 
            + reserva.getUsuario().getNome() + "\n");
}
        
        // 4. Verifica se houve atraso (com proteção contra null)
        boolean atrasado = false;
        if (emprestimo.getDataDevolucaoPrevista() != null) {
            atrasado = dataDevolucao.isAfter(emprestimo.getDataDevolucaoPrevista());
        }
        
        // 5. Prepara mensagem
        String mensagem = String.format(
            "Devolução realizada com sucesso!%n" +
            "Livro: %s%n" +
            "Exemplar: %d \n" +
            "Usuário: %s",
            livro.getTitulo(),
            exemplar.getCodigo(),
            usuario.getNome()
        );
        
        if (atrasado) {
            mensagem += "\nAtenção: Devolução feita com atraso!";
            usuario.setDevedor(true);
        }
        
        return mensagem;
    } catch (Exception e) {
        return "Ocorreu um erro ao processar a devolução: " + e.getMessage();
    }
}
private static String realizarReserva(int codigoUsuario, int codigoLivro) {
    try {
        // 2. Busca usuário e livro
        Usuario usuario = repositorio.buscarUsuario(codigoUsuario);
        Livro livro = repositorio.buscarLivro(codigoLivro);
        
        if (usuario == null) return "Usuário não encontrado!";
        if (livro == null) return "Livro não encontrado!";
        
        // 3. Verifica se já existe reserva ativa para este usuário e livro
        if (repositorio.existeReservaAtiva(usuario, livro)) {
            return "Você já tem uma reserva ativa para este livro!";
        }

        // 4. Busca exemplares do livro
        List<Exemplar> exemplares = repositorio.buscarExemplaresPorLivro(codigoLivro);
        
        // 5. Verifica se há exemplares disponíveis para reserva
        boolean todosEmprestados = exemplares.stream()
            .allMatch(e -> e.getStatus() == StatusExemplar.EMPRESTADO);
        
        if (!todosEmprestados) {
            return "Não é possível reservar: existem exemplares disponíveis para empréstimo imediato!";
        }

        // 6. Cria a reserva
        Reserva reserva = new Reserva(
            repositorio.getProximoCodigoReserva(),
            usuario,
            livro,
            LocalDate.now()
        );
        
        repositorio.salvarReserva(reserva);
        
        return String.format(
            "Reserva realizada com sucesso!%n" +
            "Livro: %s%n" +
            "Data da reserva: %s%n" +
            "Você será notificado quando um exemplar estiver disponível.",
            livro.getTitulo(),
            reserva.getDataReserva().format(DateTimeFormatter.ISO_DATE)
        );
        
    } catch (Exception e) {
        return "Erro ao processar reserva: " + e.getMessage();
    }
}
private static String registrarObservador(int codigoUsuario, int codigoLivro) {
    try {
        Usuario usuario = repositorio.buscarUsuario(codigoUsuario);
        Livro livro = repositorio.buscarLivro(codigoLivro);
        
        if (usuario == null) return "Usuário não encontrado!";
        if (livro == null) return "Livro não encontrado!";
        
        // Verifica se já é observador deste livro
        if (repositorio.isObservador(usuario, livro)) {
            return "Este usuário já é observador deste livro!";
        }
        
        // Cria e registra o observador
        ProfessorObserver observer = new ProfessorObserver(usuario);
        LivroObservable livroObservable = repositorio.getLivroObservable(livro);
        livroObservable.registrarObserver(observer);
        
        repositorio.salvarObservador(usuario, livro, observer);
        
        return String.format(
            "Professor %s registrado como observador do livro '%s'%n" +
            "Será notificado quando houver mais de 2 reservas simultâneas.",
            usuario.getNome(), livro.getTitulo()
        );
        
    } catch (Exception e) {
        return "Erro ao registrar observador: " + e.getMessage();
    }
}
private static String consultarLivro(int codigoLivro) {
    try {
        // 1. Busca o livro
        Livro livro = repositorio.buscarLivro(codigoLivro);
        if (livro == null) {
            return "Livro não encontrado!";
        }

        StringBuilder resultado = new StringBuilder();
        
        // 2. Informações básicas do livro
        resultado.append(String.format(
            "=== Informações do Livro ===%n" +
            "Código: %d%n" +
            "Título: %s%n" +
            "Autor(es): %s%n" +
            "Edição: %s%n" +
            "Ano: %d%n%n",
            livro.getCodigo(),
            livro.getTitulo(),
            livro.getAutores(),
            livro.getEdicao(),
            livro.getAnoPublicacao()
        ));

        // 3. Reservas ativas
        List<Reserva> reservas = repositorio.buscarReservasAtivasPorLivro(codigoLivro);
        resultado.append(String.format("Reservas ativas: %d%n", reservas.size()));
        
        if (!reservas.isEmpty()) {
            resultado.append("Usuários com reserva:\n");
            for (Reserva reserva : reservas) {
                resultado.append(String.format("- %s (desde %s)%n",
                    reserva.getUsuario().getNome(),
                    reserva.getDataReserva().format(DateTimeFormatter.ISO_DATE)
                ));
            }
            resultado.append("\n");
        }

        // 4. Informações dos exemplares
        List<Exemplar> exemplares = repositorio.buscarExemplaresPorLivro(codigoLivro);
        resultado.append(String.format("Exemplares: %d%n", exemplares.size()));
        
        for (Exemplar exemplar : exemplares) {
            resultado.append(String.format(
                "Exemplar #%d - Status: %s",
                exemplar.getCodigo(),
                exemplar.getStatus()
            ));

            // Se estiver emprestado, mostra detalhes do empréstimo
            if (exemplar.getStatus() == StatusExemplar.EMPRESTADO) {
                Emprestimo emprestimo = repositorio.buscarEmprestimoAtivoPorExemplar(exemplar);
                if (emprestimo != null) {
                    resultado.append(String.format(
                        "%n  Emprestado para: %s" +
                        "%n  Data do empréstimo: %s" +
                        "%n  Data prevista devolução: %s",
                        emprestimo.getUsuario().getNome(),
                        emprestimo.getDataEmprestimo().format(DateTimeFormatter.ISO_DATE),
                        emprestimo.getDataDevolucaoPrevista().format(DateTimeFormatter.ISO_DATE)
                    ));
                }
            }
            resultado.append("\n\n");
        }

        return resultado.toString();

    } catch (Exception e) {
        return "Erro ao consultar livro: " + e.getMessage();
    }
}
private static String consultarUsuario(int codigoUsuario) {
    try {
        // 1. Busca o usuário
        Usuario usuario = repositorio.buscarUsuario(codigoUsuario);
        if (usuario == null) {
            return "Usuário não encontrado!";
        }

        StringBuilder resultado = new StringBuilder();
        
        // 2. Informações básicas do usuário
        resultado.append(String.format(
            "=== Informações do Usuário ===%n" +
            "Código: %d%n" +
            "Nome: %s%n" +
            "Tipo: %s%n" +
            "Status: %s%n%n",
            usuario.getCodigo(),
            usuario.getNome(),
            usuario.getTipo(),
            usuario.isDevedor() ? "Devedor" : "Regular"
        ));

        // 3. Empréstimos do usuário
        List<Emprestimo> emprestimos = repositorio.buscarEmprestimosPorUsuario(usuario);
        resultado.append(String.format("=== Empréstimos (%d) ===%n", emprestimos.size()));
        
        if (emprestimos.isEmpty()) {
            resultado.append("Nenhum empréstimo registrado.\n");
        } else {
            for (Emprestimo emp : emprestimos) {
                resultado.append(String.format(
                    "Livro: %s%n" +
                    "Data empréstimo: %s%n" +
                    "Status: %s%n" +
                    "Data %s: %s%n" +
                    "Exemplar: %d%n%n",
                    emp.getExemplar().getLivro().getTitulo(),
                    emp.getDataEmprestimo().format(DateTimeFormatter.ISO_DATE),
                    emp.getStatus() == StatusEmprestimo.ATIVO ? "Em curso" : "Finalizado",
                    emp.getStatus() == StatusEmprestimo.ATIVO ? "prevista devolução" : "de devolução",
                    emp.getStatus() == StatusEmprestimo.ATIVO ? 
                        emp.getDataDevolucaoPrevista().format(DateTimeFormatter.ISO_DATE) :
                        emp.getDataDevolucaoReal() != null ? 
                            emp.getDataDevolucaoReal().format(DateTimeFormatter.ISO_DATE) : "Não devolvido",
                    emp.getExemplar().getCodigo()
                ));
            }
        }

        // 4. Reservas do usuário
        List<Reserva> reservas = repositorio.buscarReservasPorUsuario(usuario);
        resultado.append(String.format("\n=== Reservas (%d) ===%n", reservas.size()));
        
        if (reservas.isEmpty()) {
            resultado.append("Nenhuma reserva registrada.\n");
        } else {
            for (Reserva res : reservas) {
                resultado.append(String.format(
                    "Livro: %s%n" +
                    "Data reserva: %s%n" +
                    "Status: %s%n%n",
                    res.getLivro().getTitulo(),
                    res.getDataReserva().format(DateTimeFormatter.ISO_DATE),
                    res.isAtiva() ? "Ativa" : "Inativa"
                ));
            }
        }

        // 5. Notificações (se for professor)
        if (usuario.getTipo() == TipoUsuario.PROFESSOR) {
            int totalNotificacoes = repositorio.getTotalNotificacoes(usuario);
            resultado.append(String.format("\n=== Notificações Recebidas ===%n" +
                                        "Total: %d%n", totalNotificacoes));
        }

        return resultado.toString();

    } catch (Exception e) {
        return "Erro ao consultar usuário: " + e.getMessage();
    }
}
private static String consultarNotificacoes(int codigoUsuario) {
    try {
        // 1. Busca o usuário
        Usuario usuario = repositorio.buscarUsuario(codigoUsuario);
        if (usuario == null) {
            return "Usuário não encontrado!";
        }

        // 2. Obtém o total de notificações
        int totalNotificacoes = repositorio.getTotalNotificacoes(usuario);
        
        return String.format(
            "=== Notificações Recebidas ===%n" +
            "Usuário: %s (Código: %d)%n" +
            "Total de notificações: %d%n" +
            "%nNotificações são geradas quando livros observados " +
            "atingem mais de 2 reservas simultâneas.",
            usuario.getNome(),
            usuario.getCodigo(),
            totalNotificacoes
        );

    } catch (Exception e) {
        return "Erro ao consultar notificações: " + e.getMessage();
    }
}
}