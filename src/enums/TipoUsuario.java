package main.java.com.biblioteca.enums;

public enum TipoUsuario {
    ALUNO_GRADUACAO(2, 4),    // Limite: 2 livros
    ALUNO_POS_GRADUACAO(3, 5), // Limite: 3 livros
    PROFESSOR(Integer.MAX_VALUE, 8); // Sem limite

    private final int limiteEmprestimos;
    private final int tempoEmprestimoDias;

    TipoUsuario(int limite, int tempoEmprestimoDias) {
        this.limiteEmprestimos = limite;
        this.tempoEmprestimoDias = tempoEmprestimoDias;
    }

    public int getLimiteEmprestimos() {
        return limiteEmprestimos;
    }

    public int getTempoEmprestimoDias(){
        return tempoEmprestimoDias;
    }
}