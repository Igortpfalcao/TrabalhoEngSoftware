package main.java.com.biblioteca.enums;

public enum StatusEmprestimo {
    ATIVO("Em curso"),
    DEVOLVIDO("Finalizado"),
    ATRASADO("Em atraso");

    private final String descricao;

    StatusEmprestimo(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
