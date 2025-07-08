package main.java.com.biblioteca.modelos;

import main.java.com.biblioteca.enums.StatusExemplar;

public class Exemplar {
    private int codigoExemplar;
    private static int codigoLivro;
    private StatusExemplar status;
    
    public Exemplar(int codigoExemplar, int codigoLivro, StatusExemplar status) {
        this.codigoExemplar = codigoExemplar;
        this.codigoLivro = codigoLivro;
        this.status = StatusExemplar.DISPONIVEL;
    }
    
    // Getters e Setters
    public int getCodigoExemplar() { return codigoExemplar; }
    public static int getCodigoLivro() { return codigoLivro; }
    public StatusExemplar getStatus() { return status; }
    
    public void setStatus(StatusExemplar status) {
        this.status = status;
    }
    
    // Método conveniente para verificar disponibilidade
    public boolean isDisponivel() {
        return status.isDisponivel();
    }

    public Livro getLivro() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getLivro'");
    }
}