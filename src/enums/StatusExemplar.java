package main.java.com.biblioteca.enums;

public enum StatusExemplar {
    DISPONIVEL("Disponível para empréstimo"),
    EMPRESTADO("Emprestado atualmente"),
    RESERVADO("Reservado para um usuário"),
    EM_MANUTENCAO("Em manutenção"),
    INDISPONIVEL("Indisponível para empréstimo");

    private final String descricao;

    StatusExemplar(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public boolean isDisponivel() {
        return this == DISPONIVEL;
    }
}