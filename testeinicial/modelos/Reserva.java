package main.java.com.biblioteca.modelos;

import java.time.LocalDate;

public class Reserva {
    private int codigo;
    private Usuario usuario;
    private Livro livro;
    private LocalDate dataReserva;
    private boolean cancelada;

    public Reserva(Usuario usuario, Livro livro, LocalDate dataReserva) {
        this.usuario = usuario;
        this.livro = livro;
        this.dataReserva = dataReserva;
        this.cancelada = false;
    }

    // Getters e Setters
    public int getCodigo() { return codigo; }
    public Usuario getUsuario() { return usuario; }
    public Livro getLivro() { return livro; }
    public LocalDate getDataReserva() { return dataReserva; }
    public boolean isCancelada() { return cancelada; }

    public void setCodigo(int codigo) { this.codigo = codigo; }
    public void setCancelada(boolean cancelada) { this.cancelada = cancelada; }
}