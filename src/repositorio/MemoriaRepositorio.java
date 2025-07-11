package repositorio;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import enums.StatusEmprestimo;
import enums.StatusExemplar;
import modelos.Emprestimo;
import modelos.Exemplar;
import modelos.Livro;
import modelos.Reserva;
import modelos.Usuario;
import observer.LivroObservable;
import observer.Observer;

public class MemoriaRepositorio implements BibliotecaRepositorio {
    private static MemoriaRepositorio instancia;

    private final Map<ParUsuarioLivro, Observer> observadores = new HashMap<>();
    private Map<Integer, Usuario> usuarios = new HashMap<>();
    private Map<Integer, Livro> livros = new HashMap<>();
    private Map<Integer, Exemplar> exemplares = new HashMap<>();
    private List<Emprestimo> emprestimos = new ArrayList<>();
    private List<Reserva> reservas = new ArrayList<>();
    private int ultimoCodigoReserva = 0;
    private Map<Integer, LivroObservable> livrosObservables = new HashMap<>();
    private Map<Integer, Integer> contadorNotificacoes = new HashMap<>();

    @Override
    public Livro buscarLivro(int codigoLivro) {
        return livros.get(codigoLivro);
    }

    @Override
    public List<Exemplar> buscarExemplaresDisponiveis(int codigoLivro) {
        return exemplares.values().stream()
            .filter(exemplar -> exemplar.getLivro().getCodigo() == codigoLivro)
            .filter(exemplar -> exemplar.getStatus() == StatusExemplar.DISPONIVEL)
            .collect(Collectors.toList());
    }

    // Outros métodos do repositório
    @Override
    public void salvarLivro(Livro livro) {
        livros.put(livro.getCodigo(), livro);
    }

    @Override
    public void salvarExemplar(Exemplar exemplar) {
        exemplares.put(exemplar.getCodigo(), exemplar);
    }

    @Override
    public void salvarUsuario(Usuario usuario) {
        usuarios.put(usuario.getCodigo(), usuario);
    }

    @Override
    public Usuario buscarUsuario(int codigoUsuario) {
        return usuarios.get(codigoUsuario);
    }

    @Override
    public Exemplar buscarExemplar(int codigoExemplar) {
        return exemplares.get(codigoExemplar);
    }

    @Override
    public void salvarEmprestimo(Emprestimo emprestimo) {
        emprestimos.add(emprestimo);
    }

@Override
    public Emprestimo buscarEmprestimoAtivo(Usuario usuario, Livro livro) {
    return emprestimos.stream()
        .filter(e -> e.getUsuario().equals(usuario))
        .filter(e -> e.getExemplar().equals(livro))
        .filter(e -> e.getStatus() == StatusEmprestimo.ATIVO)
        .findFirst()
        .orElse(null);
}

@Override
public Emprestimo buscarEmprestimoAtivoPorLivro(Usuario usuario, int codigoLivro) {
    return emprestimos.stream()
        .filter(e -> e.getUsuario().equals(usuario))
        .filter(e -> e.getExemplar().getLivro().getCodigo() == codigoLivro)
        .filter(e -> e.getStatus() == StatusEmprestimo.ATIVO)
        .findFirst()
        .orElse(null);
}

@Override
public int getProximoCodigoReserva() {
    return ++ultimoCodigoReserva;
}

@Override
public boolean existeReservaAtiva(Usuario usuario, Livro livro) {
    return reservas.stream()
        .anyMatch(r -> r.getUsuario().equals(usuario) 
                    && r.getLivro().equals(livro) 
                    && r.isAtiva());
}

@Override
public List<Exemplar> buscarExemplaresPorLivro(int codigoLivro) {
    return exemplares.values().stream()
        .filter(e -> e.getLivro().getCodigo() == codigoLivro)
        .collect(Collectors.toList());
}

@Override
public List<Reserva> buscarReservasAtivasPorLivro(int codigoLivro) {
    return reservas.stream()
        .filter(r -> r.getLivro().getCodigo() == codigoLivro && r.isAtiva())
        .sorted(Comparator.comparing(Reserva::getDataReserva)) // Ordena por data mais antiga primeiro
        .collect(Collectors.toList());
}


    @Override
    public LivroObservable getLivroObservable(Livro livro) {
        return livrosObservables.computeIfAbsent(livro.getCodigo(), k -> new LivroObservable(livro));
    }
    @Override
    public void incrementarNotificacoes(Usuario usuario) {
        contadorNotificacoes.compute(usuario.getCodigo(), (k, v) -> v == null ? 1 : v + 1);
    }
@Override
public void salvarReserva(Reserva reserva) {
    reservas.add(reserva);
    
    // Verifica se precisa notificar os observadores
    int codigoLivro = reserva.getLivro().getCodigo();
    int totalReservas = contarReservasAtivas(codigoLivro);
    
    if (totalReservas > 2) {
        LivroObservable livroObservable = getLivroObservable(reserva.getLivro());
        livroObservable.notificarObservers();
    }
}

@Override
public int contarReservasAtivas(int codigoLivro) {
    int contador = 0;
    
    // Percorre todas as reservas
    for (Reserva reserva : reservas) {
        // Verifica se a reserva é para o livro especificado e se está ativa
        if (reserva.getLivro().getCodigo() == codigoLivro && reserva.isAtiva()) {
            contador++;
        }
    }
    
    return contador;
}

@Override
public boolean isObservador(Usuario usuario, Livro livro) {
    // Verifica se os parâmetros são válidos
    if (usuario == null || livro == null) {
        return false;
    }
    
    // Cria a chave composta (usuário + livro)
    ParUsuarioLivro chave = new ParUsuarioLivro(usuario.getCodigo(), livro.getCodigo());
    
    // Verifica se existe um observador para esta chave
    return observadores.containsKey(chave);
}

    @Override
    public void salvarObservador(Usuario usuario, Livro livro, Observer observer) {
        ParUsuarioLivro chave = new ParUsuarioLivro(usuario.getCodigo(), livro.getCodigo());
        observadores.put(chave, observer);
    }
@Override
public Emprestimo buscarEmprestimoAtivoPorExemplar(Exemplar exemplar) {
    return emprestimos.stream()
            .filter(e -> e.getExemplar().equals(exemplar) && e.getStatus() == StatusEmprestimo.ATIVO)
            .findFirst()
            .orElse(null);
}
@Override
public List<Emprestimo> buscarEmprestimosPorUsuario(Usuario usuario) {
    return emprestimos.stream()
            .filter(e -> e.getUsuario().equals(usuario))
            .sorted(Comparator.comparing(Emprestimo::getDataEmprestimo).reversed())
            .collect(Collectors.toList());
}

@Override
public List<Reserva> buscarReservasPorUsuario(Usuario usuario) {
    return reservas.stream()
            .filter(r -> r.getUsuario().equals(usuario))
            .sorted(Comparator.comparing(Reserva::getDataReserva).reversed())
            .collect(Collectors.toList());
}

@Override
public int getTotalNotificacoes(Usuario usuario) {
    return contadorNotificacoes.getOrDefault(usuario.getCodigo(), 0);
}
    private MemoriaRepositorio() { }

    public static synchronized MemoriaRepositorio getInstancia() {
        if (instancia == null) {
            instancia = new MemoriaRepositorio();
        }
        return instancia;
    }

}
