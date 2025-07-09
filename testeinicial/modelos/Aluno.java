package main.java.com.biblioteca.modelos;

import main.java.com.biblioteca.enums.TipoUsuario;

public class Aluno extends Usuario {
    private boolean devedor;

    public Aluno(int codigo, String nome, TipoUsuario tipo) {
        super(codigo, nome, tipo);
        this.devedor = false; // Inicialmente não é devedor
    }

    // Métodos específicos para alunos
    public boolean isDevedor() {
        return devedor;
    }

    public void setDevedor(boolean devedor) {
        this.devedor = devedor;
    }

    // Sobrescreve o método getLimiteEmprestimos()
    @Override
    public int getLimiteEmprestimos() {
        return this.getTipo().getLimiteEmprestimos();
    }
}
