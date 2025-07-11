package repositorio;

import enums.StatusExemplar;
import modelos.AlunoGraduacao;
import modelos.AlunoPosGraduacao;
import modelos.Exemplar;
import modelos.Livro;
import modelos.Professor;

public class Cadastro {

    public static void cadastrar(BibliotecaRepositorio repository) {
        // Cadastro de Livros
        Livro livro100 = new Livro(100, "Engenharia de Software", "Addison Wesley", "Ian Sommervile", "6ª", 2000);
        Livro livro101 = new Livro(101, "UML - Guia do Usuário", "Campus", "Grady Booch, James Rumbaugh, Ivar Jacobson", "7ª", 2000);
        Livro livro200 = new Livro(200, "Code Complete", "Microsoft Press", "Steve McConnell", "2ª", 2014);
        Livro livro201 = new Livro(201, "Agile Software Development, Principles, Patterns and Practices", "Prentice Hall", "Robert Martin", "1ª", 2002);
        Livro livro300 = new Livro(300, "Refactoring: Improving the Design of Existing Code", "Addison Wesley Professional", "Martin Fowler", "1ª", 1999);
        Livro livro301 = new Livro(301, "Software Metrics: A rigorous and Practical Approach", "CRC Press", "Norman Fenton, James Bieman", "3ª", 2014);
        Livro livro400 = new Livro(400, "Design Patterns: Element of Reusable Object-Oriented Software", "Addison Wesley Professional", "Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides", "1ª", 1994);
        Livro livro401 = new Livro(401, "UML Distilled: A Brief Guide to the Standard Object Modeling Language", "Addison Wesley Professional", "Martin Fowler", "3ª", 2003);

        repository.salvarLivro(livro100);
        repository.salvarLivro(livro101);
        repository.salvarLivro(livro200);
        repository.salvarLivro(livro201);
        repository.salvarLivro(livro300);
        repository.salvarLivro(livro301);
        repository.salvarLivro(livro400);
        repository.salvarLivro(livro401);

        System.out.println("Livros cadastrados com sucesso!");

        // Cadastro de Exemplares (agora com referência ao objeto Livro)
        repository.salvarExemplar(new Exemplar(1, livro100, StatusExemplar.DISPONIVEL));
        repository.salvarExemplar(new Exemplar(2, livro100, StatusExemplar.DISPONIVEL));
        repository.salvarExemplar(new Exemplar(3, livro101, StatusExemplar.DISPONIVEL));
        repository.salvarExemplar(new Exemplar(4, livro200, StatusExemplar.DISPONIVEL));
        repository.salvarExemplar(new Exemplar(5, livro201, StatusExemplar.DISPONIVEL));
        repository.salvarExemplar(new Exemplar(6, livro300, StatusExemplar.DISPONIVEL));
        repository.salvarExemplar(new Exemplar(7, livro300, StatusExemplar.DISPONIVEL));
        repository.salvarExemplar(new Exemplar(8, livro400, StatusExemplar.DISPONIVEL));
        repository.salvarExemplar(new Exemplar(9, livro400, StatusExemplar.DISPONIVEL));

        System.out.println("Exemplares cadastrados com sucesso!");

        // Cadastro de Usuários
        repository.salvarUsuario(new AlunoGraduacao(123, "João da Silva", null));
        repository.salvarUsuario(new AlunoGraduacao(789, "Pedro Paulo", null));
        repository.salvarUsuario(new AlunoPosGraduacao(456, "Luiz Fernando Rodrigues", null));
        repository.salvarUsuario(new Professor(100, "Carlos Lucena"));
        repository.salvarUsuario(new AlunoGraduacao(26,"Andréa Dias", null));
        
        System.out.println("Usuários cadastrados com sucesso!");
    }
}
