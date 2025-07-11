package modelos;

import java.time.LocalDate;

import enums.StatusEmprestimo;

public class Emprestimo {
    private int codigo;
    private Exemplar exemplar;
    private Usuario usuario;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucaoPrevista;
    private LocalDate dataDevolucaoReal;
    private StatusEmprestimo status;

    public Emprestimo(Usuario usuario, Exemplar exemplar, LocalDate dataEmprestimo) {
        this.usuario = usuario;
        this.exemplar = exemplar;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucaoPrevista = calcularDataDevolucaoPrevista();
        this.status = StatusEmprestimo.ATIVO;
    }

    private LocalDate calcularDataDevolucaoPrevista() {
        // Obtém o prazo de empréstimo do tipo de usuário
        int prazoDias = usuario.getTipo().getTempoEmprestimoDias();
        return dataEmprestimo.plusDays(prazoDias);
    }
    // Getters e Setters
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigoExemplar() {
        return exemplar.getCodigo();
    }

    public Exemplar getExemplar() {
        return exemplar;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public LocalDate getDataDevolucaoPrevista() {
        return dataDevolucaoPrevista;
    }

    public LocalDate getDataDevolucaoReal() {
        return dataDevolucaoReal;
    }

    public void setDataDevolucaoReal(LocalDate data) {
        this.dataDevolucaoReal = data;
    }

    public StatusEmprestimo getStatus() {
        return status;
    }

    public void setStatus(StatusEmprestimo status) {
        this.status = status;
    }

    // Método para finalizar empréstimo
    public void finalizar() {
        this.dataDevolucaoReal = LocalDate.now();
        this.status = StatusEmprestimo.DEVOLVIDO;
    }
}
