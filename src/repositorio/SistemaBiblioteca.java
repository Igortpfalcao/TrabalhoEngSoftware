package repositorio;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import enums.StatusEmprestimo;
import enums.StatusExemplar;
import enums.TipoUsuario;
import modelos.Emprestimo;
import modelos.Exemplar;
import modelos.Livro;
import modelos.Reserva;
import modelos.Usuario;
import observer.LivroObservable;
import observer.ProfessorObserver;

public class SistemaBiblioteca {
    private final BibliotecaRepositorio repositorio;

    // Injeção de dependência (Singleton)
    public SistemaBiblioteca(BibliotecaRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    // -------- MÉTODOS DE EMPRÉSTIMO --------
public String realizarEmprestimo(int codigoUsuario, int codigoLivro) {
    // 1. Validação inicial
    if (codigoUsuario <= 0 || codigoLivro <= 0) {
        return "Códigos devem ser números positivos!";
    }

    // 2. Busca usuário e livro
    Usuario usuario = repositorio.buscarUsuario(codigoUsuario);
    Livro livro = repositorio.buscarLivro(codigoLivro);
    
    if (usuario == null) return "Usuário não encontrado!";
    if (livro == null) return "Livro não encontrado!";
    
    // 3. Validações do usuário
    if (usuario.isDevedor()) {
        return "Usuário em débito! Regularize sua situação.";
    }
    
    if (usuario.atingiuLimiteEmprestimos()) {
        return String.format(
            "Limite de empréstimos atingido! (Máximo: %d livros)",
            usuario.getTipo().getLimiteEmprestimos()
        );
    }
    
    // 4. Verifica se já tem o livro emprestado
    if (usuario.getEmprestimosAtivos().stream()
        .anyMatch(emp -> emp.getExemplar().getLivro().getCodigo() == codigoLivro)) {
        return "Você já tem um exemplar deste livro emprestado!";
    }

    // 5. Busca exemplares disponíveis
    List<Exemplar> exemplares = repositorio.buscarExemplaresDisponiveis(codigoLivro);
    if (exemplares.isEmpty()) {
        return "Não há exemplares disponíveis para este livro!";
    }

    // 6. Processa o empréstimo
    Exemplar exemplar = exemplares.get(0);
    exemplar.setStatus(StatusExemplar.EMPRESTADO);
    
    // 7. Cria e registra o empréstimo
    Emprestimo emprestimo = new Emprestimo(usuario, exemplar, LocalDate.now());
    usuario.adicionarEmprestimo(emprestimo);
    repositorio.salvarEmprestimo(emprestimo);
    
    // 8. Trata reservas existentes
    List<Reserva> reservas = repositorio.buscarReservasAtivasPorLivro(codigoLivro);
    if (!reservas.isEmpty()) {
        Reserva reserva = reservas.get(0);
        reserva.setAtiva(false);
        // Notificação pode ser implementada aqui
    }

    // 9. Retorna mensagem detalhada
    return String.format(
        """
        Empréstimo realizado com sucesso!
        Livro: %s
        Exemplar: %d
        Usuário: %s
        Data do empréstimo: %s
        Data prevista para devolução: %s
        """,
        livro.getTitulo(),
        exemplar.getCodigo(),
        usuario.getNome(),
        LocalDate.now().format(DateTimeFormatter.ISO_DATE),
        LocalDate.now().plusDays(usuario.getTipo().getTempoEmprestimoDias())
            .format(DateTimeFormatter.ISO_DATE)
    );
}
    // -------- MÉTODOS DE DEVOLUÇÃO --------
public String realizarDevolucao(int codigoUsuario, int codigoLivro) {
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

    // -------- MÉTODOS DE RESERVA --------
public String realizarReserva(int codigoUsuario, int codigoLivro) {
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
    // -------- OBSERVADOR --------
public String registrarObservador(int codigoUsuario, int codigoLivro) {
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

    // -------- CONSULTAS --------
public String consultarLivro(int codigoLivro) {
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

public String consultarUsuario(int codigoUsuario) {
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
public String consultarNotificacoes(int codigoUsuario) {
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
