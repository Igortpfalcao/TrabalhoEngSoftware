package repositorio;

import java.util.List;

import modelos.Emprestimo;
import modelos.Exemplar;
import modelos.Livro;
import modelos.Reserva;
import modelos.Usuario;
import observer.LivroObservable;
import observer.Observer;

public interface BibliotecaRepositorio {
    // Usuários
    Usuario buscarUsuario(int codigo);
    void salvarUsuario(Usuario usuario);
    void salvarLivro(Livro livro);
    void salvarExemplar(Exemplar exemplar);
    boolean isObservador(Usuario usuario, Livro livro);
    
    // Livros
    Livro buscarLivro(int codigo);
    
    // Exemplares
    Exemplar buscarExemplar(int codigo);
    List<Exemplar> buscarExemplaresDisponiveis(int codigoLivro);
    
    // Empréstimos
    void salvarEmprestimo(Emprestimo emprestimo);
    Emprestimo buscarEmprestimoAtivo(Usuario usuario, Livro livro);
    // Devoluções
    Emprestimo buscarEmprestimoAtivoPorLivro(Usuario usuario, int codigoLivro);
    
    // Reservas
    void salvarReserva(Reserva reserva);
    List<Exemplar> buscarExemplaresPorLivro(int codigoLivro);
    boolean existeReservaAtiva(Usuario usuario, Livro livro);
    int getProximoCodigoReserva();
    
    // Observadores
    LivroObservable getLivroObservable(Livro livro);
    void salvarObservador(Usuario usuario, Livro livro, Observer observer);

    // Para verificar informações
    Emprestimo buscarEmprestimoAtivoPorExemplar(Exemplar exemplar);
    List<Emprestimo> buscarEmprestimosPorUsuario(Usuario usuario);
    List<Reserva> buscarReservasPorUsuario(Usuario usuario);
    List<Reserva> buscarReservasAtivasPorLivro(int codigoLivro);

    // Para notificações (professores)
    int contarReservasAtivas(int codigoLivro);
    void incrementarNotificacoes(Usuario usuario);
    int getTotalNotificacoes(Usuario usuario);
}
