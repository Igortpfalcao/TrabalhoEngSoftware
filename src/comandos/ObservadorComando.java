package comandos;

import repositorio.SistemaBiblioteca;

public class ObservadorComando implements Comando {
    private final SistemaBiblioteca sistema;

    public ObservadorComando(SistemaBiblioteca sistema) {
        this.sistema = sistema;
    }

    @Override
    public String executar(String[] parametros) {
        if (parametros.length != 2) return "Formato inválido! Use: obs <usuário> <livro>";
        try {
            int usuarioId = Integer.parseInt(parametros[0]);
            int livroId = Integer.parseInt(parametros[1]);
            return sistema.registrarObservador(usuarioId, livroId);
        } catch (NumberFormatException e) {
            return "Código inválido! Deve ser um número.";
        }
    }
}
