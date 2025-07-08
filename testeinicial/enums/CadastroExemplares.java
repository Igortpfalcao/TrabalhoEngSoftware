package main.java.com.biblioteca.repositorio;

import main.java.com.biblioteca.enums.StatusExemplar;
import main.java.com.biblioteca.modelos.Exemplar;

public class CadastroExemplares {

    public static void cadastrar(BibliotecaRepositorio repository) {
        // Exemplares da tabela
        repository.salvarExemplar(new Exemplar(1, 100, StatusExemplar.DISPONIVEL));
        repository.salvarExemplar(new Exemplar(2, 100, StatusExemplar.DISPONIVEL));
        repository.salvarExemplar(new Exemplar(3, 101, StatusExemplar.DISPONIVEL));
        repository.salvarExemplar(new Exemplar(4, 200, StatusExemplar.DISPONIVEL));
        repository.salvarExemplar(new Exemplar(5, 201, StatusExemplar.DISPONIVEL));
        repository.salvarExemplar(new Exemplar(6, 300, StatusExemplar.DISPONIVEL));
        repository.salvarExemplar(new Exemplar(7, 300, StatusExemplar.DISPONIVEL));
        repository.salvarExemplar(new Exemplar(8, 400, StatusExemplar.DISPONIVEL));
        repository.salvarExemplar(new Exemplar(9, 400, StatusExemplar.DISPONIVEL));

        System.out.println("Exemplares cadastrados com sucesso!");
    }
}
