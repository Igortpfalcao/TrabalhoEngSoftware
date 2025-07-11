package modelos;

import enums.TipoUsuario;

public class AlunoGraduacao extends Usuario {
    private boolean devedor;

    // Construtor que chama o construtor de Usuario
    public AlunoGraduacao(int codigo, String nome, TipoUsuario tipo) {
        super(codigo, nome, TipoUsuario.ALUNO_GRADUACAO);  // Chama o construtor de Usuario
        this.devedor = false;
    }

    public boolean isDevedor() {
        return devedor;
    }

    public void setDevedor(boolean devedor) {
        this.devedor = devedor;
    }

    @Override
    public int getLimiteEmprestimos() {
        return TipoUsuario.ALUNO_GRADUACAO.getLimiteEmprestimos();
    }
}
