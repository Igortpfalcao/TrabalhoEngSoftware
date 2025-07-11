package modelos;



import java.time.LocalDate;

public class Reserva {
    private int codigo;
    private Usuario usuario;
    private Livro livro;
    private LocalDate dataReserva;
    private boolean ativa;
    
    public Reserva(int codigo, Usuario usuario, Livro livro, LocalDate dataReserva) {
        this.codigo = codigo;
        this.usuario = usuario;
        this.livro = livro;
        this.dataReserva = dataReserva;
        this.ativa = true;
    }
    
    // Getters e Setters
    public int getCodigo() { return codigo; }
    public Usuario getUsuario() { return usuario; }
    public Livro getLivro() { return livro; }
    public LocalDate getDataReserva() { return dataReserva; }
    public boolean isAtiva() { return ativa; }
    public void setAtiva(boolean ativa) { this.ativa = ativa; }
}
