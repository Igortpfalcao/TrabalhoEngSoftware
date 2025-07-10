package main.java.com.biblioteca.modelos;

import main.java.com.biblioteca.enums.StatusExemplar;
import main.java.com.biblioteca.modelos.Usuario;

public class Exemplar {
    private Livro livro;
    private int codigo;
    private int codigoLivro;
    private StatusExemplar status;
    private Usuario reservadoPor;  // Referência ao usuário que reservou

    public Exemplar(int codigo, Livro livro, StatusExemplar status) {
        this.codigo = codigo;
        this.livro = livro;
        this.status = status;
        this.reservadoPor = null;  // Inicialmente sem reserva
    }

    // Getters e Setters básicos
    public int getCodigo() {
        return codigo;
    }

    public int getCodigoLivro() {
        return codigoLivro;
    }

    public StatusExemplar getStatus() {
        return status;
    }

    public void setStatus(StatusExemplar status) {
        this.status = status;
    }

    // Métodos para reserva
    public Usuario getReservadoPor() {
        return reservadoPor;
    }

    public void setReservadoPor(Usuario usuario) {
        this.reservadoPor = usuario;
        
        // Atualiza status automaticamente ao reservar/liberar reserva
        if (usuario != null) {
            this.status = StatusExemplar.RESERVADO;
        } else if (this.status == StatusExemplar.RESERVADO) {
            this.status = StatusExemplar.DISPONIVEL;
        }
    }

    // Método auxiliar para verificar disponibilidade
    public boolean isDisponivel() {
        return status == StatusExemplar.DISPONIVEL && reservadoPor == null;
    }

    public Object getTitulo() {
        return livro.getTitulo();
    }
    public Livro getLivro(){
        return livro;
    }
}