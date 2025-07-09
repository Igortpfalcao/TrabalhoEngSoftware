package main.java.com.biblioteca.modelos;

import java.util.ArrayList;
import java.util.List;

import main.java.com.biblioteca.enums.TipoUsuario;

public abstract class Usuario {
    private int codigo;
    private String nome;
    private TipoUsuario tipo;
    private List<Emprestimo> emprestimosAtivos;
    private List<Reserva> reservasAtivas;
    private boolean devedor;


    public Usuario(int codigo, String nome, TipoUsuario tipo) {
        this.codigo = codigo;
        this.nome = nome;
        this.tipo = tipo;
        this.emprestimosAtivos = new ArrayList<>();
        this.reservasAtivas = new ArrayList<>();
        this.devedor = false;
        
    }

    // Métodos comuns a todos os usuários
    public abstract int getLimiteEmprestimos();

    // Getters
    public int getCodigo() { return codigo; }
    public String getNome() { return nome; }
    public TipoUsuario getTipo() { return tipo; }
    public List<Reserva> getReservasAtivas() { return reservasAtivas; }
    public boolean isDevedor() { return devedor; }
    public void setDevedor(boolean devedor) { this.devedor = devedor; }
    public void adicionarEmprestimo(Emprestimo emprestimo) {
        this.emprestimosAtivos.add(emprestimo);
    }
    
    public List<Emprestimo> getEmprestimosAtivos() {
        return new ArrayList<>(emprestimosAtivos); // Retorna cópia para evitar modificações externas
    }


}