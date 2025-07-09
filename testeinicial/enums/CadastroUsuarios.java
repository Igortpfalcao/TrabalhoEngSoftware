package main.java.com.biblioteca.repositorio;

import main.java.com.biblioteca.enums.TipoUsuario;
import main.java.com.biblioteca.modelos.Aluno;
import main.java.com.biblioteca.modelos.Professor; 

public class CadastroUsuarios {

    public static void cadastrar(BibliotecaRepositorio repository) {
        // Alunos de Graduação
        repository.salvarUsuario(new Aluno(123, "João da Silva", TipoUsuario.ALUNO_GRADUACAO));
        repository.salvarUsuario(new Aluno(789, "Pedro Paulo", TipoUsuario.ALUNO_GRADUACAO));

        // Aluno de Pós-Graduação
        repository.salvarUsuario(new Aluno(456, "Luiz Fernando Rodrigues", TipoUsuario.ALUNO_POS_GRADUACAO));

        // Professor
        repository.salvarUsuario(new Professor(100, "Carlos Lucena"));
        
        System.out.println("Usuários cadastrados com sucesso!");
    }
}
