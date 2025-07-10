package main.java.com.biblioteca.observer;

import main.java.com.biblioteca.enums.TipoUsuario;
import main.java.com.biblioteca.modelos.Livro;
import main.java.com.biblioteca.modelos.Usuario;

public class ProfessorObserver implements Observer {
    private Usuario professor;
    private int contadorNotificacoes = 0;

    public ProfessorObserver(Usuario professor) {
        if (!professor.getTipo().equals(TipoUsuario.PROFESSOR)) {
            throw new IllegalArgumentException("Apenas professores podem ser observadores");
        }
        this.professor = professor;
    }

    @Override
    public void atualizar(Livro livro) {
        contadorNotificacoes++;
        System.out.printf("Notificação #%d para Professor %s: " +
                        "O livro '%s' atingiu mais de 2 reservas simultâneas%n",
                contadorNotificacoes, professor.getNome(), livro.getTitulo());
    }

    public int getContadorNotificacoes() {
        return contadorNotificacoes;
    }
}