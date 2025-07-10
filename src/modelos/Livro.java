package main.java.com.biblioteca.modelos;

public class Livro {
    private int codigo;
    private String titulo;
    private String editora;
    private String autores;
    private String edicao;
    private int anoPublicacao;
    private boolean emprestado;
    private Usuario reservadoPor;

    public Livro(int codigo, String titulo, String editora, String autores, String edicao, int anoPublicacao) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.editora = editora;
        this.autores = autores;
        this.edicao = edicao;
        this.anoPublicacao = anoPublicacao;
        this.emprestado = false;
        this.reservadoPor = null;
    }

    // Getters (métodos de acesso)
    public int getCodigo() { return codigo; }
    public String getTitulo() { return titulo; }
    public Usuario getReservadoPor() { return reservadoPor; }
    public String getAutores(){ return autores; }
    public int getAnoPublicacao(){ return anoPublicacao;}
    public String getEdicao(){ return edicao; }
    
    
    public void setEmprestado(boolean emprestado) { 
        this.emprestado = emprestado; 
    }

    public void setReservadoPor(Usuario usuario) {
        this.reservadoPor = usuario;
    }

    public boolean isEmprestado() {
        return this.emprestado;
    }
}

