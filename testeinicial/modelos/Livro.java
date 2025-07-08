package main.java.com.biblioteca.modelos;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Livro {
    private int codigo;
    private String titulo;
    private String editora;
    private String autores;
    private String edicao;
    private int anoPublicacao;
    private int exemplaresDisponiveis = 0; // Inicializado com 0
    private List<Reserva> reservas = new ArrayList<>(); // Inicializado vazio
    private List<Exemplar> exemplares = new ArrayList<>();

    public Livro(int codigo, String titulo, String editora, String autores, String edicao, int anoPublicacao) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.editora = editora;
        this.autores = autores;
        this.edicao = edicao;
        this.anoPublicacao = anoPublicacao;
    }

    // Getters (métodos de acesso)
    public int getCodigo() { return codigo; }
    public String getTitulo() { return titulo; }
    public String getEditora() { return editora; }
    public String getAutores() { return autores; }
    public String getEdicao() { return edicao; }
    public int getAnoPublicacao() { return anoPublicacao; }
    public List<Reserva> getReservas() { return reservas; }

    // Setters para os campos mutáveis
    public void setExemplaresDisponiveis(int exemplaresDisponiveis) {
        this.exemplaresDisponiveis = exemplaresDisponiveis;
    }
    
    // Método para adicionar exemplares
    public void adicionarExemplares(int quantidade) {
        if (quantidade > 0) {
            this.exemplaresDisponiveis += quantidade;
        }
    }
    
    // Método para remover exemplares (usado em empréstimos)
    public boolean removerExemplar() {
        if (this.exemplaresDisponiveis > 0) {
            this.exemplaresDisponiveis--;
            return true;
        }
        return false;
    }
    
    // Método para adicionar reserva
    public void adicionarReserva(Reserva reserva) {
        this.reservas.add(reserva);
    }
    
    // Método para remover reserva
    public boolean removerReserva(Reserva reserva) {
        return this.reservas.remove(reserva);
    }

    public List<Exemplar> getExemplaresDisponiveis() {
        return exemplares.stream()
                .filter(Exemplar::isDisponivel)
                .collect(Collectors.toList());
    }
    
    public boolean temExemplaresDisponiveis() {
        return exemplares.stream().anyMatch(Exemplar::isDisponivel);
    }
}
