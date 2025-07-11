package comandos;

import java.util.HashMap;
import java.util.Map;

public class ExecutorDeComandos {
    private final Map<String, Comando> comandos = new HashMap<>();

    public void registrar(String nome, Comando comando) {
        comandos.put(nome, comando);
    }

    public String executar(String entrada) {
        String[] partes = entrada.trim().split("\\s+");
        if (partes.length == 0) return "Nenhum comando inserido.";

        String nomeComando = partes[0].toLowerCase();
        Comando comando = comandos.get(nomeComando);

        if (comando == null) return "Comando não reconhecido!";
        String[] parametros = new String[partes.length - 1];
        System.arraycopy(partes, 1, parametros, 0, parametros.length);

        return comando.executar(parametros);
    }
}
