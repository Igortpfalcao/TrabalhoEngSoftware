package main.java.com.biblioteca.modelos;

import main.java.com.biblioteca.enums.TipoUsuario;

public class Professor extends Usuario {
    private boolean devedor;
    private int totalNotificacoes;

    public Professor(int codigo, String nome) {
        super(codigo, nome, TipoUsuario.PROFESSOR);
        this.devedor = false;
        this.totalNotificacoes = 0;
    }

    // Métodos específicos
    public boolean isDevedor() {
        return devedor;
    }

    public void setDevedor(boolean devedor) {
        this.devedor = devedor;
    }

    public int getTotalNotificacoes() {
        return totalNotificacoes;
    }

    public void incrementarNotificacoes() {
        this.totalNotificacoes++;
    }

    @Override
    public int getLimiteEmprestimos() {
        return Integer.MAX_VALUE; // Professores não têm limite
    }
}
