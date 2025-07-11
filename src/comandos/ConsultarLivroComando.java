package comandos;

import repositorio.SistemaBiblioteca;

public class ConsultarLivroComando implements Comando {
    private final SistemaBiblioteca sistema;

    public ConsultarLivroComando(SistemaBiblioteca sistema) {
        this.sistema = sistema;
    }

    @Override
    public String executar(String[] parametros) {
        if (parametros.length != 1) return "Formato inválido! Use: liv <código livro>";
        try {
            int livroId = Integer.parseInt(parametros[0]);
            return sistema.consultarLivro(livroId);
        } catch (NumberFormatException e) {
            return "Código inválido! Deve ser um número.";
        }
    }
}
