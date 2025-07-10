package main.java.com.biblioteca.modelos;

import main.java.com.biblioteca.enums.TipoUsuario;

public class AlunoPosGraduacao extends Usuario {
    
    private boolean devedor;
    private int codigo;
    private String nome;

    public AlunoPosGraduacao(int codigo, String nome, TipoUsuario tipo) {
        super(codigo, nome, TipoUsuario.ALUNO_POS_GRADUACAO);  // Chama o construtor de Usuario
        this.devedor = false;
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
        return TipoUsuario.ALUNO_POS_GRADUACAO.getLimiteEmprestimos();
    }
}
