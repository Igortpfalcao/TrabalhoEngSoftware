import java.util.Date;


public class Exemplar {
    private Livro codigo;
    private int codigoExemplar;
    private boolean status;
    private Date dataEmprestimo;
    private Date dataDevolucaoPrevista;
    private Usuario usuarioEmprestimo;
    private Livro livro;

    public Exemplar(Livro codigo, int codigoExemplar) {
        this.codigo = codigo;
        this.codigoExemplar = codigoExemplar;
        this.status = false;
    }

    public Livro getCodigo() {
        return codigo;
    }

    public int getCodigoExemplar() {
        return codigoExemplar;
    }

    public boolean isStatus() {
        return status;
    }

    public void emprestar(Usuario usuarioEmprestimo, Date dataEmprestimo, Date dataDevolucaoPrevista) {
        this.usuarioEmprestimo = usuarioEmprestimo;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
        this.status = false;
    }

    public void devolver() {
        this.status = true;
        this.usuarioEmprestimo = null;
        this.dataEmprestimo = null;
        this.dataDevolucaoPrevista = null;
}
    public Usuario getUsuarioEmprestimo() {
        return usuarioEmprestimo;
    }

    public Date getDataEmprestimo() {
        return dataEmprestimo;
    }

    public Date getDataDevolucaoPrevista() {
        return dataDevolucaoPrevista;
    }

    public Livro getLivro() {
        return this.livro;
    }
}