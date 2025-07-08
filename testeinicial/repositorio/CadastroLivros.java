package main.java.com.biblioteca.repositorio;

import main.java.com.biblioteca.modelos.Livro;

public class CadastroLivros {

    public static void cadastrar(BibliotecaRepositorio repository) {
        // Livros da tabela
        repository.salvarLivro(new Livro(100, "Engenharia de Software", "Addison Wesley", "Ian Sommervile", "6ª", 2000));
        repository.salvarLivro(new Livro(101, "UML - Guia do Usuário", "Campus", "Grady Booch, James Rumbaugh, Ivar Jacobson", "7ª", 2000));
        repository.salvarLivro(new Livro(200, "Code Complete", "Microsoft Press", "Steve McConnell", "2ª", 2014));
        repository.salvarLivro(new Livro(201, "Agile Software Development, Principles, Patterns and Practices", "Prentice Hall", "Robert Martin", "1ª", 2002));
        repository.salvarLivro(new Livro(300, "Refactoring: Improving the Design of Existing Code", "Addison Wesley Professional", "Martin Fowler", "1ª", 1999));
        repository.salvarLivro(new Livro(301, "Software Metrics: A rigorous and Practical Approach", "CRC Press", "Norman Fenton, James Bieman", "3ª", 2014));
        repository.salvarLivro(new Livro(400, "Design Patterns: Element of Reusable Object-Oriented Software", "Addison Wesley Professional", "Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides", "1ª", 1994));
        repository.salvarLivro(new Livro(401, "UML Distilled: A Brief Guide to the Standard Object Modeling Language", "Addison Wesley Professional", "Martin Fowler", "3ª", 2003));

        System.out.println("Livros cadastrados com sucesso!");
    }
}