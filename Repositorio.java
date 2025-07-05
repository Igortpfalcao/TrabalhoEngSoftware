import java.util.ArrayList;
import java.util.List;

public class Repositorio {
    private static List<Livro> livros;
    private static List<Exemplar> exemplares;
    private static List<IUsuario> usuarios;
    
    public Repositorio() {
        Repositorio.livros = new ArrayList<>();
        Repositorio.exemplares = new ArrayList<>();
        Repositorio.usuarios = new ArrayList<>();
        inicializarDados();
    }

	public static void inicializarDados() {
		livros.add(new Livro(100, "Engenharia de Software", "Addison Wesley", "Ian Sommervile", 6, 2000));
		livros.add(new Livro(101, "UML - Guia do Usuário ", "Campus", "Grady Booch, James Rumbaugh, Ivar Jacobson", 7, 2000));
		livros.add(new Livro(200, "Code Complete", "Microsoft Press", "Steve McConnell ", 2, 2014));
        livros.add(new Livro(201, "Agile Software Development, Principles, Patterns and Practices", "Prentice Hall ", "Robert Martin ", 1, 2002));
		livros.add(new Livro(300, "Refactoring: Improving the Design of Existing Code", "Addison Wesley Professional", "Martin Fowler ", 1, 1999));
        livros.add(new Livro(301, "Software Metrics: A rigorous and Practical Approach", "CRC Press ", "Norman Fenton, James Bieman", 3, 2014));
		livros.add(new Livro(400, "Design Patterns: Element of Reusable Object-Oriented Software", "Addison Wesley Professional", "Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides", 1, 1994));
        livros.add(new Livro(401, "UML Distilled: A Brief Guide to the Standard Object Modeling Language", "Addison Wesley Professional", "Martin Fowler ", 3, 2003));

        exemplares.add(new Exemplar(buscarLivro(100, livros), 1));
        exemplares.add(new Exemplar(buscarLivro(100, livros), 2));
        exemplares.add(new Exemplar(buscarLivro(101, livros), 3));
        exemplares.add(new Exemplar(buscarLivro(200, livros), 4));
        exemplares.add(new Exemplar(buscarLivro(201, livros), 5));
        exemplares.add(new Exemplar(buscarLivro(300, livros), 6));
        exemplares.add(new Exemplar(buscarLivro(300, livros), 7));
        exemplares.add(new Exemplar(buscarLivro(400, livros), 8));
        exemplares.add(new Exemplar(buscarLivro(400, livros), 9));
		
        usuarios.add(new AlunoGraduacao(123, "João da Silva"));
        usuarios.add(new AlunoPosGraduacao(456, "Luiz Fernando Rodrigues"));
        usuarios.add(new AlunoGraduacao(789, "Pedro Paulo"));
        usuarios.add(new Professor(100, "Carlos Lucena"));
	}


    public static Livro buscarLivro(int codigo, List<Livro> livros) {
        for (Livro livro : livros) {
            if (livro.getCodigo() == codigo) { 
                return livro;
            }
        }
        return null;
    }
    public static Livro buscarLivroPorCodigo(int codigo) {
        for (Livro livro : livros) {
            if (livro.getCodigo() == codigo) { 
                return livro;
            }
        }
        return null;
    }


public void consultarLivro(int codigoLivro) {
    Livro livro = buscarLivroPorCodigo(codigoLivro);
    
    if (livro == null) {
        System.out.println("Livro com código " + codigoLivro + " não encontrado.");
        return;
    }

    System.out.println("(i) Título: " + livro.getTitulo());

    // (ii) Reservas
    List<Reserva> reservas = livro.getReservas() != null ? livro.getReservas() : new ArrayList<>();
    System.out.println("(ii) Quantidade de reservas: " + reservas.size());
    
    if (!reservas.isEmpty()) {
        System.out.println("Usuários que reservaram:");
        for (Reserva reserva : reservas) {
            if (reserva != null && reserva.getUsuario() != null) {
                System.out.println(" - " + reserva.getUsuario().getNome());
            }
        }
    }

    // (iii) Exemplares
    System.out.println("(iii) Exemplares:");
    boolean encontrouExemplares = false;
    
    for (Exemplar exemplar : exemplares) {
        if (exemplar.getLivro() != null && exemplar.getLivro().getCodigo() == livro.getCodigo()) {
            System.out.println(" - Código do exemplar: " + exemplar.getCodigo());
            
            if (exemplar.isStatus()) {
                System.out.println("   Status: Emprestado");
                if (exemplar.getUsuarioEmprestimo() != null) {
                    System.out.println("   Usuário: " + exemplar.getUsuarioEmprestimo().getNome());
                }
                System.out.println("   Data empréstimo: " + exemplar.getDataEmprestimo());
                System.out.println("   Data prevista devolução: " + exemplar.getDataDevolucaoPrevista());
            } else {
                System.out.println("   Status: Disponível");
            }
            
            encontrouExemplares = true;
        }
    }

    if (!encontrouExemplares) {
        System.out.println(" - Nenhum exemplar cadastrado para este livro.");
    }
}
}