package main.java.com.biblioteca.repositorio;

import main.java.com.biblioteca.enums.StatusExemplar;
import main.java.com.biblioteca.enums.StatusEmprestimo;
import main.java.com.biblioteca.modelos.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MemoriaRepositorio implements BibliotecaRepositorio {
    
    // Estruturas de armazenamento principal (thread-safe)
    private final Map<Integer, Usuario> usuarios = new ConcurrentHashMap<>();
    private final Map<Integer, Livro> livros = new ConcurrentHashMap<>();
    private final Map<Integer, Exemplar> exemplares = new ConcurrentHashMap<>();
    
    // Estruturas de relacionamento
    private final Map<Integer, List<Emprestimo>> emprestimosPorUsuario = new ConcurrentHashMap<>();
    private final Map<Integer, Emprestimo> emprestimosAtivosPorExemplar = new ConcurrentHashMap<>();
    private final Map<Integer, List<Reserva>> reservasPorUsuario = new ConcurrentHashMap<>();
    private final Map<Integer, List<Reserva>> reservasPorLivro = new ConcurrentHashMap<>();
    private final Map<Integer, Reserva> reservasPorId = new ConcurrentHashMap<>();
    
    // Controles adicionais
    private final Map<Integer, Set<Integer>> observadores = new ConcurrentHashMap<>();
    private final Map<Integer, Integer> contadorNotificacoes = new ConcurrentHashMap<>();
    private final AtomicInteger contadorIds = new AtomicInteger(0);

    // Métodos de Usuário
    @Override
    public Usuario buscarUsuario(int codigo) {
        return usuarios.get(codigo);
    }

    @Override
    public void salvarUsuario(Usuario usuario) {
        Objects.requireNonNull(usuario, "Usuário não pode ser nulo");
        usuarios.put(usuario.getCodigo(), usuario);
        emprestimosPorUsuario.putIfAbsent(usuario.getCodigo(), new ArrayList<>());
        reservasPorUsuario.putIfAbsent(usuario.getCodigo(), new ArrayList<>());
    }

    // Métodos de Livro
    @Override
    public Livro buscarLivro(int codigo) {
        return livros.get(codigo);
    }

    @Override
    public List<Livro> listarTodosLivros() {
        return new ArrayList<>(livros.values());
    }

    @Override
    public void salvarLivro(Livro livro) {
        Objects.requireNonNull(livro, "Livro não pode ser nulo");
        livros.put(livro.getCodigo(), livro);
        reservasPorLivro.putIfAbsent(livro.getCodigo(), new ArrayList<>());
    }

    // Métodos de Exemplar
    @Override
    public Exemplar buscarExemplar(int codigo) {
        return exemplares.get(codigo);
    }

    @Override
    public List<Exemplar> buscarExemplaresDisponiveis(int codigoLivro) {
        return exemplares.values().stream()
            .filter(e -> e.getCodigoExemplar() == codigoLivro)
            .filter(e -> e.getStatus() == StatusExemplar.DISPONIVEL)
            .collect(Collectors.toList());
    }


    @Override
    public void salvarExemplar(Exemplar exemplar) {
        exemplares.put(exemplar.getCodigoExemplar(), exemplar);
}

    @Override

    // Métodos de Empréstimo
    public Emprestimo buscarEmprestimoAtivoPorExemplar(int codigoExemplar) {
        return emprestimosAtivosPorExemplar.get(codigoExemplar);
    }

    @Override
    public List<Emprestimo> buscarEmprestimosPorUsuario(int codigoUsuario) {
        return new ArrayList<>(emprestimosPorUsuario.getOrDefault(codigoUsuario, Collections.emptyList()));
    }

    @Override
    public void salvarEmprestimo(Emprestimo emprestimo) {
        Objects.requireNonNull(emprestimo, "Empréstimo não pode ser nulo");
        if (!usuarios.containsKey(emprestimo.getUsuario().getCodigo())) {
            throw new IllegalArgumentException("Usuário do empréstimo não existe");
        }
        if (!exemplares.containsKey(emprestimo.getExemplar().getCodigoExemplar())) {
            throw new IllegalArgumentException("Exemplar do empréstimo não existe");
        }

        emprestimo.setCodigo(contadorIds.incrementAndGet());
        emprestimosPorUsuario.computeIfAbsent(emprestimo.getUsuario().getCodigo(), k -> new ArrayList<>()).add(emprestimo);
        emprestimosAtivosPorExemplar.put(emprestimo.getExemplar().getCodigoExemplar(), emprestimo);
        
        // Atualiza status do exemplar
        emprestimo.getExemplar().setStatus(StatusExemplar.EMPRESTADO);
    }

    @Override
    public void atualizarEmprestimo(Emprestimo emprestimo) {
        Objects.requireNonNull(emprestimo, "Empréstimo não pode ser nulo");
        if (!emprestimosPorUsuario.containsKey(emprestimo.getUsuario().getCodigo())) {
            throw new IllegalArgumentException("Empréstimo inválido");
        }

        // Remove do mapa de ativos se foi devolvido
        if (emprestimo.getStatus() == StatusEmprestimo.DEVOLVIDO) {
            emprestimosAtivosPorExemplar.remove(emprestimo.getExemplar().getCodigoExemplar());
            emprestimo.getExemplar().setStatus(StatusExemplar.DISPONIVEL);
        }
    }

    // Métodos de Reserva
    @Override
    public List<Reserva> buscarReservasPorUsuario(int codigoUsuario) {
        return reservasPorUsuario.getOrDefault(codigoUsuario, Collections.emptyList()).stream()
            .filter(r -> !r.isCancelada())
            .collect(Collectors.toList());
    }

    @Override
    public void salvarReserva(Reserva reserva) {
        Objects.requireNonNull(reserva, "Reserva não pode ser nula");
        if (!usuarios.containsKey(reserva.getUsuario().getCodigo())) {
            throw new IllegalArgumentException("Usuário da reserva não existe");
        }
        if (!livros.containsKey(reserva.getLivro().getCodigo())) {
            throw new IllegalArgumentException("Livro da reserva não existe");
        }

        reserva.setCodigo(contadorIds.incrementAndGet());
        reservasPorId.put(reserva.getCodigo(), reserva);
        reservasPorUsuario.computeIfAbsent(reserva.getUsuario().getCodigo(), k -> new ArrayList<>()).add(reserva);
        reservasPorLivro.computeIfAbsent(reserva.getLivro().getCodigo(), k -> new ArrayList<>()).add(reserva);
    }

    @Override
    public void cancelarReserva(int codigoReserva) {
        Reserva reserva = reservasPorId.get(codigoReserva);
        if (reserva != null) {
            reserva.setCancelada(true);
        }
    }

    // Métodos de Observação/Notificação
    @Override
    public void registrarObservador(int codigoUsuario, int codigoLivro) {
        if (!usuarios.containsKey(codigoUsuario)) {
            throw new IllegalArgumentException("Usuário não existe");
        }
        if (!livros.containsKey(codigoLivro)) {
            throw new IllegalArgumentException("Livro não existe");
        }
        observadores.computeIfAbsent(codigoLivro, k -> ConcurrentHashMap.newKeySet()).add(codigoUsuario);
    }

    @Override
    public List<Integer> buscarObservadoresPorLivro(int codigoLivro) {
        return new ArrayList<>(observadores.getOrDefault(codigoLivro, Collections.emptySet()));
    }

    @Override
    public void incrementarNotificacoes(int codigoUsuario) {
        if (!usuarios.containsKey(codigoUsuario)) {
            throw new IllegalArgumentException("Usuário não existe");
        }
        contadorNotificacoes.merge(codigoUsuario, 1, Integer::sum);
    }

@Override
public int buscarTotalNotificacoes(int codigoUsuario) {
    // Verifica se o usuário existe
    if (!usuarios.containsKey(codigoUsuario)) {
        throw new IllegalArgumentException("Usuário não encontrado");
    }
    
    // Retorna o total de notificações ou 0 se não houver registros
    return contadorNotificacoes.getOrDefault(codigoUsuario, 0);
}

    // Métodos de Status de Devedor
    @Override
    public boolean usuarioEstaDevedor(int codigoUsuario) {
        Usuario usuario = usuarios.get(codigoUsuario);
        return usuario != null && usuario.isDevedor();
    }

    @Override
    public void atualizarStatusDevedor(int codigoUsuario, boolean devedor) {
        Usuario usuario = usuarios.get(codigoUsuario);
        if (usuario != null) {
            usuario.setDevedor(devedor);
        }
    }

    @Override
    public void incrementarNotificacoesProfessor(int codigoProfessor) {
    // Incrementa o contador de notificações para o professor
    contadorNotificacoes.merge(codigoProfessor, 1, Integer::sum);
}

    @Override
    public List<Exemplar> buscarExemplaresPorLivro(int codigoLivro) {
    // Cria uma lista vazia para os exemplares encontrados
        List<Exemplar> exemplaresDoLivro = new ArrayList<>();
    
    // Percorre todos os exemplares cadastrados no sistema
    for (Exemplar exemplar : exemplares.values()) {
        // Verifica se o exemplar pertence ao livro solicitado
        if (exemplar.getCodigoLivro() == codigoLivro) {
            exemplaresDoLivro.add(exemplar);
        }
    }
    
    // Retorna a lista de exemplares encontrados (pode ser vazia)
    return exemplaresDoLivro;
}
@Override
    public List<Integer> buscarLivrosObservadosPorProfessor(int codigoProfessor) {
    List<Integer> livrosObservados = new ArrayList<>();
    for (Map.Entry<Integer, Set<Integer>> entry : observadores.entrySet()) {
        if (entry.getValue().contains(codigoProfessor)) {
            livrosObservados.add(entry.getKey());
        }
    }
    return livrosObservados;
}

@Override
public List<Reserva> buscarReservasPorLivro(int codigoLivro) {
    List<Reserva> reservasAtivas = new ArrayList<>();
    
    for (Reserva reserva : reservasPorId.values()) {
        if (reserva.getCodigo() == codigoLivro && !reserva.isCancelada()) {
            reservasAtivas.add(reserva);
        }
    }
    
    reservasAtivas.sort(Comparator.comparing(Reserva::getDataReserva));
    return reservasAtivas;
}
}