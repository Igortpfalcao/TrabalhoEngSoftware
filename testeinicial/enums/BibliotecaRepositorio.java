package main.java.com.biblioteca.repositorio;

import java.util.List;

import main.java.com.biblioteca.modelos.Emprestimo;
import main.java.com.biblioteca.modelos.Exemplar;
import main.java.com.biblioteca.modelos.Livro;
import main.java.com.biblioteca.modelos.Reserva;
import main.java.com.biblioteca.modelos.Usuario;

public interface BibliotecaRepositorio {
    // Usuários
    Usuario buscarUsuario(int codigo);
    void salvarUsuario(Usuario usuario);
    
    // Livros
    Livro buscarLivro(int codigo);
    List<Livro> listarTodosLivros();
    void salvarLivro(Livro livro);
    
    // Exemplares
    Exemplar buscarExemplar(int codigo);
    List<Exemplar> buscarExemplaresDisponiveis(int codigoLivro);
    void salvarExemplar(Exemplar exemplar);
    
    // Empréstimos
    Emprestimo buscarEmprestimoAtivoPorExemplar(int codigoExemplar);
    List<Emprestimo> buscarEmprestimosPorUsuario(int codigoUsuario);
    void salvarEmprestimo(Emprestimo emprestimo);
    void atualizarEmprestimo(Emprestimo emprestimo);
    
    // Reservas
    List<Reserva> buscarReservasPorUsuario(int codigoUsuario);
    void salvarReserva(Reserva reserva);
    void cancelarReserva(int codigoReserva);
    
    // Observadores
    void registrarObservador(int codigoUsuario, int codigoLivro);
    List<Integer> buscarObservadoresPorLivro(int codigoLivro);
    void incrementarNotificacoes(int codigoUsuario);
    int buscarTotalNotificacoes(int codigoUsuario);

    // Para verificar débitos
    boolean usuarioEstaDevedor(int codigoUsuario);

    // Para atualizar status de débito
    void atualizarStatusDevedor(int codigoUsuario, boolean devedor);

    // Para notificações (professores)
    void incrementarNotificacoesProfessor(int codigoProfessor);
    List<Exemplar> buscarExemplaresPorLivro(int codigoLivro);
    List<Integer> buscarLivrosObservadosPorProfessor(int codigoProfessor);
    List<Reserva> buscarReservasPorLivro(int codigoLivro);
}
