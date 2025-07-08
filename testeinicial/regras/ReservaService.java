package main.java.com.biblioteca.regras;

import main.java.com.biblioteca.modelos.*;
import main.java.com.biblioteca.repositorio.BibliotecaRepositorio;
import java.time.LocalDate;
import java.util.List;

public class ReservaService {
    private final BibliotecaRepositorio repositorio;

    public ReservaService(BibliotecaRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    public String realizarReserva(int codigoUsuario, int codigoLivro) {
        // 1. Validar usuário e livro
        Usuario usuario = repositorio.buscarUsuario(codigoUsuario);
        if (usuario == null) {
            return "Usuário não encontrado.";
        }

        Livro livro = repositorio.buscarLivro(codigoLivro);
        if (livro == null) {
            return "Livro não encontrado.";
        }

        // 2. Verificar se há exemplares disponíveis
        if (repositorio.buscarExemplaresDisponiveis(codigoLivro).size() > 0) {
            return "Não é possível reservar. Há exemplares disponíveis para empréstimo.";
        }

        // 3. Verificar se usuário já tem reserva ativa para este livro
        boolean jaPossuiReserva = repositorio.buscarReservasPorUsuario(codigoUsuario).stream()
                .anyMatch(r -> r.getLivro().getCodigo() == codigoLivro && !r.isCancelada());

        if (jaPossuiReserva) {
            return "Usuário já possui uma reserva ativa para este livro.";
        }

        // 5. Verificar se usuário está em débito
        if (usuario.isDevedor()) {
            return "Usuário está com pendências. Não é possível realizar reservas.";
        }

        // 6. Criar e salvar a reserva
        Reserva reserva = new Reserva(usuario, livro, LocalDate.now());
        repositorio.salvarReserva(reserva);

        return String.format(
            "Reserva realizada com sucesso.%n" +
            "Livro: %s (Código: %d)%n" +
            "Data da reserva: %s%n" +
            livro.getTitulo(),
            livro.getCodigo(),
            reserva.getDataReserva()
        );
    }

    public String cancelarReserva(int codigoUsuario, int codigoReserva) {
        Reserva reserva = repositorio.buscarReservasPorUsuario(codigoUsuario).stream()
                .filter(r -> r.getCodigo() == codigoReserva)
                .findFirst()
                .orElse(null);

        if (reserva == null) {
            return "Reserva não encontrada ou não pertence ao usuário.";
        }

        if (reserva.isCancelada()) {
            return "Reserva já está cancelada.";
        }

        repositorio.cancelarReserva(reserva.getCodigo());
        return "Reserva cancelada com sucesso.";
    }

    public String listarReservasUsuario(int codigoUsuario) {
        List<Reserva> reservas = repositorio.buscarReservasPorUsuario(codigoUsuario);

        if (reservas.isEmpty()) {
            return "Usuário não possui reservas ativas.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Reservas ativas:\n");

        reservas.forEach(r -> {
            if (!r.isCancelada()) {
                sb.append(String.format(
                    "- Código: %d | Livro: %s (Código: %d) | Data: %s%n",
                    r.getCodigo(),
                    r.getLivro().getTitulo(),
                    r.getLivro().getCodigo(),
                    r.getDataReserva()
                ));
            }
        });

        return sb.toString();
    }
}