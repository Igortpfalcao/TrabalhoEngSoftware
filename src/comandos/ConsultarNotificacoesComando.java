package comandos;

import main.java.com.biblioteca.SistemaBiblioteca;

public class ConsultarNotificacoesComando implements Comando {
    private final SistemaBiblioteca sistema;

    public ConsultarNotificacoesComando(SistemaBiblioteca sistema) {
        this.sistema = sistema;
    }

    @Override
    public String executar(String[] parametros) {
        if (parametros.length != 1) return "Formato inválido! Use: ntf <código usuário>";
        try {
            int usuarioId = Integer.parseInt(parametros[0]);
            return sistema.consultarNotificacoes(usuarioId);
        } catch (NumberFormatException e) {
            return "Código inválido! Deve ser um número.";
        }
    }
}