package observer;

import modelos.Livro;
import modelos.Usuario;

public class ProfessorObserver implements Observer {
    private Usuario professor;
    private int contadorNotificacoes = 0;

    public ProfessorObserver(Usuario professor) {
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
