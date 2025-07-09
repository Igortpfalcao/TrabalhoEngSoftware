package main.java.com.biblioteca.modelos;

import java.time.LocalDate;

import main.java.com.biblioteca.enums.StatusEmprestimo;

public class Emprestimo {
    private Usuario usuario;
    private Exemplar exemplar;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucaoPrevista;
    private LocalDate dataDevolucaoReal;
    private boolean devolvido;
    
    public Emprestimo(Usuario usuario, Exemplar exemplar, LocalDate dataEmprestimo, LocalDate dataDevolucaoPrevista) {
        this.usuario = usuario;
        this.exemplar = exemplar;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
        this.devolvido = false;
    }
    
    // Getters e Setters
    public Usuario getUsuario() { return usuario; }
    public Exemplar getExemplar() { return exemplar; }
    public LocalDate getDataEmprestimo() { return dataEmprestimo; }
    public LocalDate getDataDevolucaoPrevista() { return dataDevolucaoPrevista; }
    public LocalDate getDataDevolucaoReal() { return dataDevolucaoReal; }
    public boolean isDevolvido() { return devolvido; }
    
    public void setDataDevolucaoReal(LocalDate data) { this.dataDevolucaoReal = data; }
    public void setDevolvido(boolean devolvido) { this.devolvido = devolvido; }

    public int getCodigoExemplar() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCodigoExemplar'");
    }

    public StatusEmprestimo getStatus() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStatus'");
    }

    public Object getCodigo() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCodigo'");
    }

    public void setCodigo(int incrementAndGet) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setCodigo'");
    }
}