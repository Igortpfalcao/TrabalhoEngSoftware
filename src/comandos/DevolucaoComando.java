package comandos;

import main.java.com.biblioteca.SistemaBiblioteca;

public class DevolucaoComando implements Comando {
    private final SistemaBiblioteca sistema;

    public DevolucaoComando(SistemaBiblioteca sistema) {
        this.sistema = sistema;
    }

    @Override
    public String executar(String[] parametros) {
        if (parametros.length != 2) return "Formato inválido! Use: dev <usuário> <livro>";
        try {
            int usuarioId = Integer.parseInt(parametros[0]);
            int livroId = Integer.parseInt(parametros[1]);
            return sistema.realizarDevolucao(usuarioId, livroId);
        } catch (NumberFormatException e) {
            return "Código inválido! Deve ser um número.";
        }
    }
}